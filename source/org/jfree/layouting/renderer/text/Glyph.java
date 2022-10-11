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
 * $Id: Glyph.java 6489 2008-11-28 14:53:40Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.renderer.text;

import org.pentaho.reporting.libraries.fonts.text.Spacing;


/**
 * A glyph represents a printable textual item. It can span more than one
 * codepoint (if it is a compound glyph).
 *
 * @author Thomas Morgner
 */
public final class Glyph
{
  private static final int[] EMPTY_EXTRA_CHARS = new int[0];

  public static final int SPACE_CHAR = 0;
  public static final int LETTER = 1;

  private int codepoint;
  private int breakWeight;
  private int classification;
  private Spacing spacing;
  private int width;
  private int height;
  private int baseLine;
  private int kerning;
  private int[] extraChars;

  public Glyph(final int codepoint,
               final int breakWeight,
               final int classification,
               final Spacing spacing,
               final int width,
               final int height,
               final int baseLine,
               final int kerning,
               final int[] extraChars)
  {

  //  Log.debug ("Glyph: -" + ((char) (0xffff & codepoint)) + "- [" + baseLine + ", " + height + "]");

    if (spacing == null)
    {
      this.spacing = Spacing.EMPTY_SPACING;
    }
    else
    {
      this.spacing = spacing;
    }
    if (extraChars == null)
    {
      this.extraChars = EMPTY_EXTRA_CHARS;
    }
    else
    {
      this.extraChars = (int[]) extraChars.clone();
    }

    this.baseLine = baseLine;
    this.codepoint = codepoint;
    this.breakWeight = breakWeight;
    this.width = width;
    this.height = height;
    this.classification = classification;
    this.kerning = kerning;
  }

  public int getClassification()
  {
    return classification;
  }

  public int[] getExtraChars()
  {
    return (int[]) extraChars.clone();
  }

  public int getBaseLine()
  {
    return baseLine;
  }

  public int getCodepoint()
  {
    return codepoint;
  }

  public int getBreakWeight()
  {
    return breakWeight;
  }

  public Spacing getSpacing()
  {
    return spacing;
  }

  public int getWidth()
  {
    return width;
  }

  public int getHeight()
  {
    return height;
  }

  public int getKerning()
  {
    return kerning;
  }

  public boolean equals(final Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (o == null || getClass() != o.getClass())
    {
      return false;
    }

    final Glyph glyph = (Glyph) o;

    if (breakWeight != glyph.breakWeight)
    {
      return false;
    }
    if (codepoint != glyph.codepoint)
    {
      return false;
    }
    if (height != glyph.height)
    {
      return false;
    }
    if (kerning != glyph.kerning)
    {
      return false;
    }
    if (width != glyph.width)
    {
      return false;
    }
    if (!spacing.equals(glyph.spacing))
    {
      return false;
    }

    return true;
  }

  public int hashCode()
  {
    int result = codepoint;
    result = 29 * result + breakWeight;
    result = 29 * result + spacing.hashCode();
    result = 29 * result + width;
    result = 29 * result + height;
    result = 29 * result + kerning;
    return result;
  }

  public String toString ()
  {
    return getClass().getName() + "={codepoint='" + ((char)(codepoint & 0xffff)) + ", extra-chars=" + extraChars.length + '}';
  }
}
