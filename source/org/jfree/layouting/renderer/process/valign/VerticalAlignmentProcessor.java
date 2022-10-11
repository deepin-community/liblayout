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
 * $Id: VerticalAlignmentProcessor.java 6489 2008-11-28 14:53:40Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.renderer.process.valign;

import org.jfree.layouting.input.style.keys.line.BaselineShift;
import org.jfree.layouting.input.style.keys.line.VerticalAlign;
import org.jfree.layouting.input.style.values.CSSAutoValue;
import org.jfree.layouting.input.style.values.CSSConstant;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.renderer.border.RenderLength;
import org.jfree.layouting.renderer.model.RenderBox;
import org.jfree.layouting.renderer.model.RenderNode;
import org.jfree.layouting.renderer.process.BoxShifter;
import org.jfree.layouting.renderer.process.InfiniteMajorAxisLayoutStep;
import org.jfree.layouting.renderer.text.ExtendedBaselineInfo;
import org.jfree.layouting.renderer.text.TextUtility;

/**
 * There's only one alignment processor for the vertical layouting. The
 * processor is non-iterative, it receives a single primary sequence (which
 * represents a line) and processes that fully.
 * <p/>
 * As result, this processor generates a list of offsets and heights; the offset
 * of the outermost element is always zero and the height is equal to the height
 * of the whole line.
 *
 * @author Thomas Morgner
 */
public class VerticalAlignmentProcessor
{
  private long lineHeight;
  private long minTopPos;
  private long maxBottomPos;
  private BoxAlignContext rootContext;
  private long sourcePosition;

  public VerticalAlignmentProcessor()
  {
  }
  // 7% of our time is spent here ..
  public void align(final BoxAlignContext alignStructure,
                    final long y1,
                    final long lineHeight)
  {
    this.minTopPos = Long.MAX_VALUE;
    this.maxBottomPos = Long.MIN_VALUE;
    this.lineHeight = lineHeight;
    this.rootContext = alignStructure;
    this.sourcePosition = y1;

    performAlignment(alignStructure);
    performExtendedAlignment(alignStructure, alignStructure);
    normalizeAlignment(alignStructure);

    alignStructure.setAfterEdge(maxBottomPos);
    alignStructure.shift(-minTopPos + y1);
    apply (alignStructure);

    this.rootContext = null;
  }

  private void performAlignment(final BoxAlignContext box)
  {
    // We have a valid align structure here.
    AlignContext child = box.getFirstChild();
    while (child != null)
    {
      if (child instanceof InlineBlockAlignContext)
      {
        final InlineBlockAlignContext context = (InlineBlockAlignContext) child;
        final InfiniteMajorAxisLayoutStep majorAxisLayoutStep = new InfiniteMajorAxisLayoutStep();
        majorAxisLayoutStep.continueComputation((RenderBox) context.getNode());

        // todo: Allow to select other than the first baseline ..
      }

      BoxAlignContext parent = box;
      final CSSValue verticalAlignment = child.getNode().getVerticalAlignment();
      if (VerticalAlign.TOP.equals(verticalAlignment) ||
          VerticalAlign.BOTTOM.equals(verticalAlignment))
      {
        // Those alignments ignore the normal alignment rules and all boxes
        // align themself on the extended linebox.
        // I'm quite sure that the definition itself is unclean ..
        //continue;
        parent = rootContext;
      }


      // Now lets assume we have a valid structure...
      // All childs have been aligned. Now check how this box is positioned
      // in relation to its parent.
      final long shiftDistance = computeShift(child, parent);
      // The alignment baseline defines to which baseline of the parent we
      // will align this element
      final int alignmentBaseline = TextUtility.translateAlignmentBaseline
          (child.getNode().getAlignmentBaseline(), child.getDominantBaseline());

      // The alignment adjust defines, where the alignment point of this
      // child will be. The alignment adjust is relative to the child's
      // line-height. In the normal case, this will be zero to indicate, that
      // the alignment point is equal to the child's dominant baseline.
      final long childAlignmentPoint =
          computeAlignmentAdjust(child, alignmentBaseline);
      final long childAscent =
          child.getBaselineDistance(ExtendedBaselineInfo.BEFORE_EDGE);
      final long childPosition = (-childAscent + childAlignmentPoint) +
          child.getBeforeEdge();

      // If zero, the parent's alignment point is on the parent's dominant
      // baseline.
      final long parentAlignmentPoint =
          parent.getBaselineDistance(alignmentBaseline);
      final long parentAscent =
          parent.getBaselineDistance(ExtendedBaselineInfo.BEFORE_EDGE);

      final long parentPosition = (-parentAscent + parentAlignmentPoint) +
          parent.getBeforeEdge();

      final long alignment = parentPosition - childPosition;
      final long offset = shiftDistance + alignment;
      child.shift(offset);

      if (rootContext.getBeforeEdge() > child.getBeforeEdge())
      {
        rootContext.setBeforeEdge(child.getBeforeEdge());
      }

      if (rootContext.getAfterEdge() < child.getAfterEdge())
      {
        rootContext.setAfterEdge(child.getAfterEdge());
      }


      if (child instanceof BoxAlignContext)
      {
        performAlignment((BoxAlignContext) child);
      }

      child = child.getNext();
    }
  }


  /**
   * This simply searches the maximum shift that we have to do to normalize
   * the element.
   *
   * @param box
   * @return
   */
  private void normalizeAlignment(final BoxAlignContext box)
  {
    minTopPos = Math.min (minTopPos, box.getBeforeEdge());
    maxBottomPos = Math.max (maxBottomPos, box.getAfterEdge());

    AlignContext child = box.getFirstChild();
    while (child != null)
    {
      if (child instanceof BoxAlignContext)
      {
        normalizeAlignment((BoxAlignContext) child);
      }
      child = child.getNext();
    }
  }

  private long computeShift(final AlignContext child, final BoxAlignContext box)
  {
    final RenderNode node = child.getNode();
    final CSSValue baselineShift = node.getBaselineShift();

    if (baselineShift == null ||
        BaselineShift.BASELINE.equals(baselineShift))
    {
      return 0;
    }

    if (BaselineShift.SUB.equals(baselineShift))
    {
      // Not sure whether this is correct, but at least it looks better
      // than the other alternatives available ..
      return child.getBaselineDistance(ExtendedBaselineInfo.ALPHABETHIC) -
          child.getBaselineDistance(ExtendedBaselineInfo.MATHEMATICAL);
    }

    if (BaselineShift.SUPER.equals(baselineShift))
    {
      return box.getBaselineDistance(ExtendedBaselineInfo.MATHEMATICAL) -
          box.getBaselineDistance(ExtendedBaselineInfo.ALPHABETHIC);
    }

    final RenderLength baselineShiftResolved = node.getBaselineShiftResolved();
    if (baselineShiftResolved != null)
    {
      return baselineShiftResolved.resolve(lineHeight);
    }

    return 0;
  }

  private long computeAlignmentAdjust (final AlignContext context,
                                       final int defaultBaseLine)
  {
    // This is a length value and defines the ASCENT that is used as
    // alignment position.
    final RenderNode node = context.getNode();
    final CSSValue alignmentAdjust = node.getAlignmentAdjust();
    if (CSSAutoValue.getInstance().equals(alignmentAdjust))
    {
      return context.getBaselineDistance(defaultBaseLine);
    }
    if (alignmentAdjust instanceof CSSConstant)
    {
      final int baseline = TextUtility.translateAlignmentBaseline
          (alignmentAdjust, defaultBaseLine);
      return context.getBaselineDistance(baseline);
    }

    final RenderLength alLength = node.getAlignmentAdjustResolved();
    if (alLength != null)
    {
      /// this is not fully true. The line height depends on the context ..
      return alLength.resolve(lineHeight);
    }

    return context.getBaselineDistance(defaultBaseLine);
  }


  private void apply(final BoxAlignContext box)
  {
    final RenderNode node = box.getNode();
    node.setY(box.getBeforeEdge());
    node.setHeight(box.getAfterEdge() - box.getBeforeEdge());

    AlignContext child = box.getFirstChild();
    while (child != null)
    {
      if (child instanceof BoxAlignContext)
      {
        apply((BoxAlignContext) child);
      }
      else if (child instanceof InlineBlockAlignContext)
      {
        // also shift all the childs.
        final BoxShifter boxShifter = new BoxShifter();
        final long shift = child.getBeforeEdge() - sourcePosition;
        boxShifter.shiftBox((RenderBox) child.getNode(), shift);
      }
      else
      {
        final RenderNode childNode = child.getNode();
        childNode.setY(child.getBeforeEdge());
        childNode.setHeight(child.getAfterEdge() - child.getBeforeEdge());
      }

      child = child.getNext();
    }
  }

  protected void print (final BoxAlignContext alignContext, final int level)
  {
//    Log.debug ("Box: L:" + level + " Y1:" + alignContext.getBeforeEdge() +
//        " Y2:" + alignContext.getAfterEdge() +
//        " H:" + (alignContext.getAfterEdge() - alignContext.getBeforeEdge())
//    );
    // We have a valid align structure here.
    AlignContext child = alignContext.getFirstChild();
    while (child != null)
    {
      if (child instanceof BoxAlignContext)
      {
        print((BoxAlignContext) child, level + 1);
      }
//      else
//      {
//        Log.debug ("...: L:" + level + " Y1:" + child.getBeforeEdge() +
//            " Y2:" + (child.getAfterEdge()) +
//            " H:" + (child.getAfterEdge() - child.getBeforeEdge()));
//      }
      child = child.getNext();
    }
  }



  /**
   * Verify all elements with alignment top or bottom. This step is required,
   * as the extended linebox is allowed to change its height during the
   * ordinary alignment. Argh, I hate that specificiation.
   *
   * @param box
   */
  private void performExtendedAlignment(final BoxAlignContext box,
                                        final BoxAlignContext lineBox)
  {
    // Aligns elements with vertical-align TOP and vertical-align BOTTOM
    AlignContext child = box.getFirstChild();
    while (child != null)
    {
      final CSSValue verticalAlignment = child.getNode().getVerticalAlignment();
      if (VerticalAlign.TOP.equals(verticalAlignment))
      {
        final long childTopEdge = child.getBeforeEdge();
        final long parentTopEdge = lineBox.getBeforeEdge();
        child.shift(parentTopEdge - childTopEdge);
      }
      else if (VerticalAlign.BOTTOM.equals(verticalAlignment))
      {
        // Align the childs after-edge with the parent's after-edge
        final long childBottomEdge = child.getAfterEdge();
        final long parentBottomEdge = lineBox.getAfterEdge();
        child.shift(parentBottomEdge - childBottomEdge);
      }

      if (child instanceof BoxAlignContext)
      {
        performExtendedAlignment((BoxAlignContext) child, lineBox);
      }

      child = child.getNext();
    }
  }
}

