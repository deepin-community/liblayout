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
 * $Id: CSSColorValue.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.input.style.values;

import java.awt.Color;

/**
 * Creation-Date: 23.11.2005, 12:01:04
 *
 * @author Thomas Morgner
 */
public class CSSColorValue extends Color implements CSSValue
{
  public CSSColorValue(final int r, final int g, final int b, final int a)
  {
    super(r, g, b, a);
  }

  public CSSColorValue(final int rgba, final boolean hasalpha)
  {
    super(rgba, hasalpha);
  }

  public CSSColorValue(final float r, final float g, final float b, final float a)
  {
    super(r, g, b, a);
  }

  public CSSColorValue(final float r, final float g, final float b)
  {
    super(r, g, b);
  }

  public CSSColorValue(final int r, final int g, final int b)
  {
    super(r, g, b);
  }

  public CSSColorValue(final Color color)
  {
    super(color.getRGB());
  }

  public String getCSSText()
  {
    if (getAlpha() == 0)
    {
      return "transparent";
    }
    else if (getAlpha() == 255)
    {
      return "rgb(" + getRed() + ',' + getGreen() + ',' + getBlue() + ')';
    }
    else
    {
      return "rgba(" + getRed() + ',' + getGreen() + ',' + getBlue() + ',' + getAlpha() + ')';
    }
  }

  /**
   * Returns a string representation of this <code>Color</code>. This method is
   * intended to be used only for debugging purposes.  The content and format of
   * the returned string might vary between implementations. The returned string
   * might be empty but cannot be <code>null</code>.
   *
   * @return a string representation of this <code>Color</code>.
   */
  public String toString()
  {
    return getCSSText();
  }
}
