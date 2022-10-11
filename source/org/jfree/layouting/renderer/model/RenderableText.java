/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://reporting.pentaho.org/liblayout/
 *
 * (C) Copyright 2006-2007, by Pentaho Corporation and Contributors.
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * $Id: RenderableText.java 6489 2008-11-28 14:53:40Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.renderer.model;

import org.jfree.layouting.renderer.text.ExtendedBaselineInfo;
import org.jfree.layouting.renderer.text.Glyph;
import org.pentaho.reporting.libraries.fonts.text.breaks.BreakOpportunityProducer;
import org.pentaho.reporting.libraries.fonts.text.Spacing;

/**
 * The renderable text is a text chunk, enriched with layouting information,
 * such as break opportunities, character sizes, kerning information and spacing
 * information.
 * <p/>
 * Text is given as codepoints. Break opportunities are given as integer values,
 * where zero forbids breaking, and higher values denote better breaks. Spacing
 * and glyph sizes and kerning is given in micro-points; Spacing is the 'added'
 * space between codepoints if text-justification is enabled.
 * <p/>
 * The text is computed as grapheme clusters; this means that several unicode
 * codepoints may result in a single /virtual/ glyph/codepoint/character.
 * (Example: 'A' + accent symbols). If the font supports Lithurges, these
 * lithurges may also be represented as a single grapheme cluster (and thus
 * behave unbreakable).
 * <p/>
 * Grapheme clusters with more than one unicode char have the size of that char
 * added to the first codepoint, all subsequence codepoints of the same cluster
 * have a size/kerning/etc of zero and are unbreakable.
 * <p/>
 * This text chunk is perfectly suitable for horizontal text, going either from
 * left-to-right or right-to-left. (Breaking mixed text is up to the
 * textfactory).
 *
 * @author Thomas Morgner
 */
public class RenderableText extends RenderNode
{
  private Glyph[] glyphs;
  private int offset;
  private int length;
  private boolean ltr;
  private int script;

  private long minimumWidth;
  private long preferredWidth;
  private boolean forceLinebreak;
  private ExtendedBaselineInfo baselineInfo;

  protected RenderableText()
  {
  }

  public RenderableText(final ExtendedBaselineInfo baselineInfo,
                        final Glyph[] glyphs,
                        final int offset,
                        final int length,
                        final int script,
                        final boolean forceLinebreak)
  {
    initialize(glyphs, offset, length, baselineInfo, script, forceLinebreak);
  }

  protected void initialize(final Glyph[] glyphs,
                            final int offset,
                            final int length,
                            final ExtendedBaselineInfo baselineInfo,
                            final int script, final boolean forceLinebreak)
  {
    if (glyphs == null)
    {
      throw new NullPointerException();
    }
    if (glyphs.length < offset + length)
    {
      throw new IllegalArgumentException();
    }

    this.baselineInfo = baselineInfo;
    this.ltr = true; // this depends on the script value
    this.script = script;

    this.glyphs = glyphs;
    this.offset = offset;
    this.length = length;
    this.forceLinebreak = forceLinebreak;

    // Major axis: All child boxes are placed from left-to-right
    setMajorAxis(HORIZONTAL_AXIS);
    // Minor: The childs might be aligned on their position (shifted up or down)
    setMinorAxis(VERTICAL_AXIS);

    long wordMinChunkWidth = 0;
    long wordMinWidth = 0;
    long wordPrefWidth = 0;
    long wordMaxWidth = 0;

//    long heightAbove = 0;
//    long heightBelow = 0;
    long minimumChunkWidth = 0;

    final int lastPos = Math.min(glyphs.length, offset + length);
    for (int i = offset; i < lastPos; i++)
    {
      final Glyph glyph = glyphs[i];
      final Spacing spacing = glyph.getSpacing();
    //      heightAbove = Math.max(glyph.getBaseLine(), heightAbove);
    //      heightBelow = Math.max(glyph.getHeight() - glyph.getBaseLine(), heightBelow);
      final int kerning = glyph.getKerning();
      final int width = glyph.getWidth();
      if (glyph.getBreakWeight() <= BreakOpportunityProducer.BREAK_CHAR)
      {
        // for the layouting, we avoid inner-word breaks
        // Later, when we have to force breaks, we may take inner-breaks into
        // account.
        wordMinChunkWidth += width + spacing.getMinimum() - kerning;
        wordMinWidth += width + spacing.getMinimum() - kerning;
        wordPrefWidth += width + spacing.getOptimum() - kerning;
        wordMaxWidth += width + spacing.getMaximum() - kerning;
      }
      else
      {
        wordMinChunkWidth += width + spacing.getMinimum() - kerning;
        wordMinWidth += width + spacing.getMinimum() - kerning;
        wordPrefWidth += width + spacing.getOptimum() - kerning;
        wordMaxWidth += width + spacing.getMaximum() - kerning;

        minimumChunkWidth = Math.max(minimumChunkWidth, wordMinChunkWidth);
        wordMinWidth = 0;

        // Paranoid sanity checks: The word- and linebreaks should have been
        // replaced by other definitions in the text factory.
        if (glyph.getBreakWeight() == BreakOpportunityProducer.BREAK_LINE)
        {
          throw new IllegalStateException("A renderable text cannot and must " +
                  "not contain linebreaks.");
        }
      }
    }

    minimumChunkWidth = Math.max(minimumChunkWidth, wordMinChunkWidth);
    minimumWidth = Math.max(minimumWidth, wordMinWidth);
    preferredWidth = Math.max(preferredWidth, wordPrefWidth);
    final long maximumWidth = Math.max(minimumWidth, wordMaxWidth);

    setMaximumBoxWidth(maximumWidth);
    setMinimumChunkWidth(minimumChunkWidth);
    setIcmMetricsFinished(true);
  }


  public boolean isForceLinebreak()
  {
    return forceLinebreak;
  }

  public boolean isLtr()
  {
    return ltr;
  }

  public Glyph[] getGlyphs()
  {
    return glyphs;
  }

  public int getOffset()
  {
    return offset;
  }

  public int getLength()
  {
    return length;
  }
//
//  public BreakAfterEnum getBreakAfterAllowed(final int axis)
//  {
//    if (axis == getMinorAxis())
//    {
//      return BreakAfterEnum.BREAK_ALLOW;
//    }
//
//    if (isForceLinebreak())
//    {
//      return BreakAfterEnum.BREAK_ALLOW;
//    }
//
//    if (glyphs.length > 0)
//    {
//      final int breakWeight = glyphs[glyphs.length - 1].getBreakWeight();
//      if (breakWeight != BreakOpportunityProducer.BREAK_NEVER)
//      {
//        return BreakAfterEnum.BREAK_ALLOW;
//      }
//    }
//    return BreakAfterEnum.BREAK_DISALLOW;
//  }

  public String getRawText()
  {
    final Glyph[] gs = getGlyphs();
    final int length = getLength();
    final StringBuffer b = new StringBuffer();
    for (int i = getOffset(); i < length; i++)
    {
      final Glyph g = gs[i];
      b.append((char) (0xffff & g.getCodepoint()));
      final int[] extraCPs = g.getExtraChars();
      for (int j = 0; j < extraCPs.length; j++)
      {
        final int extraCP = extraCPs[j];
        b.append(extraCP);
      }
    }
    return b.toString();
  }

  public boolean isEmpty()
  {
    return length == 0;
  }

  public boolean isDiscardable()
  {
    if (forceLinebreak)
    {
      return false;
    }

    return glyphs.length == 0;
  }

  /**
   * Returns the baseline info for the given node. This can be null, if the node
   * does not have any baseline info.
   *
   * @return
   */
  public ExtendedBaselineInfo getBaselineInfo()
  {
    return baselineInfo;
  }

  public int getScript()
  {
    return script;
  }

  public long getMinimumWidth()
  {
    return minimumWidth;
  }

  public long getPreferredWidth()
  {
    return preferredWidth;
  }
//
//  /**
//   * Splits the text along its major axis (horizontal for roman text) at the
//   * given position (relative to the element's origin!)
//   *
//   * Split sizes are relative to the text's minimum size.
//   *
//   * @param position the split position.
//   * @return the split elements.
//   */
//  public RenderableText[] split (long position)
//  {
////    final Glyph[] glyphs,
////    final int offset,
////    final int length,
//
//    int breakPos = offset;
//    long size = 0;
//    final int maxPos = (offset + length);
//    for (int i = offset; i < maxPos; i++)
//    {
//      Glyph glyph = glyphs[i];
//      size += glyph.getWidth();
//      size += glyph.getSpacing().getMinimum();
//
//      if (size >= position)
//      {
//        breakPos = i;
//        break;
//      }
//    }
//
//    final RenderableText[] result = new RenderableText[2];
//    if (breakPos == offset || breakPos == maxPos)
//    {
//      result[0] = (RenderableText) derive(false);
//      result[1] = null;
//      return result;
//    }
//
//    result[0] = new RenderableText(baselineInfo, glyphs,
//            offset, breakPos - offset, script, false);
//    result[1] = new RenderableText(baselineInfo, glyphs,
//            breakPos, length - (breakPos - offset), script, forceLinebreak);
//    return result;
//  }
}
