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
 * $Id: FontStretch.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

/**
 * Creation-Date: 26.10.2005, 14:42:15
 *
 * @author Thomas Morgner
 */
package org.jfree.layouting.input.style.keys.font;

import org.jfree.layouting.input.style.values.CSSConstant;
import org.jfree.layouting.input.style.values.CSSValue;

public class FontStretch
{
  public static final CSSConstant NORMAL =
          new CSSConstant("normal");
  public static final CSSConstant ULTRA_CONDENSED =
          new CSSConstant("ultra-condensed");
  public static final CSSConstant EXTRA_CONDENSED =
          new CSSConstant("extra-condensed");
  public static final CSSConstant CONDENSED =
          new CSSConstant("condensed");
  public static final CSSConstant SEMI_CONDENSED =
          new CSSConstant("semi-condensed");
  public static final CSSConstant SEMI_EXPANDED =
          new CSSConstant("semi-expanded");
  public static final CSSConstant EXPANDED =
          new CSSConstant("expanded");
  public static final CSSConstant EXTRA_EXPANDED =
          new CSSConstant("extra-expanded");
  public static final CSSConstant ULTRA_EXPANDED =
          new CSSConstant("ultra-expanded");

  public static final CSSConstant WIDER = new CSSConstant("wider");
  public static final CSSConstant NARROWER = new CSSConstant("narrower");

  private FontStretch()
  {
  }

  public static int getOrder(final CSSValue fs)
  {
    if (ULTRA_CONDENSED.equals(fs))
    {
      return -4;
    }
    if (EXTRA_CONDENSED.equals(fs))
    {
      return -3;
    }
    if (CONDENSED.equals(fs))
    {
      return -2;
    }
    if (SEMI_CONDENSED.equals(fs))
    {
      return -1;
    }
    if (NORMAL.equals(fs))
    {
      return 0;
    }
    if (SEMI_EXPANDED.equals(fs))
    {
      return 1;
    }
    if (EXPANDED.equals(fs))
    {
      return 2;
    }
    if (EXTRA_EXPANDED.equals(fs))
    {
      return 3;
    }
    if (ULTRA_EXPANDED.equals(fs))
    {
      return 4;
    }
    return 0;
  }

  public static CSSConstant getByOrder(final int order)
  {
    switch (order)
    {
      case -4:
        return ULTRA_CONDENSED;
      case -3:
        return EXTRA_CONDENSED;
      case -2:
        return CONDENSED;
      case -1:
        return SEMI_CONDENSED;
      case 1:
        return SEMI_EXPANDED;
      case 2:
        return EXPANDED;
      case 3:
        return EXTRA_EXPANDED;
      case 4:
        return ULTRA_EXPANDED;
      case 0:
        return NORMAL;
    }

    if (order < -4)
    {
      return ULTRA_CONDENSED;
    }
    return ULTRA_EXPANDED;
  }
}
