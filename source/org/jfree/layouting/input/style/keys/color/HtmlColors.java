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
 * $Id: HtmlColors.java 2739 2007-04-02 11:41:22Z taqua $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.layouting.input.style.keys.color;

import org.jfree.layouting.input.style.values.CSSColorValue;

/**
 * Contains all colors defined for HTML 4.01
 *
 * @author Thomas Morgner
 */
public final class HtmlColors
{
  public static final CSSColorValue BLACK = new CSSColorValue(0x000000, false);
  public static final CSSColorValue GREEN = new CSSColorValue(0x008000, false);
  public static final CSSColorValue SILVER = new CSSColorValue(0xC0C0C0, false);
  public static final CSSColorValue LIME = new CSSColorValue(0x00FF00, false);
  public static final CSSColorValue GRAY = new CSSColorValue(0x808080, false);
  public static final CSSColorValue OLIVE = new CSSColorValue(0x808000, false);
  public static final CSSColorValue WHITE = new CSSColorValue(0xFFFFFF, false);
  public static final CSSColorValue YELLOW = new CSSColorValue(0xFFFF00, false);
  public static final CSSColorValue MAROON = new CSSColorValue(0x800000, false);
  public static final CSSColorValue NAVY = new CSSColorValue(0x000080, false);
  public static final CSSColorValue RED = new CSSColorValue(0xFF0000, false);
  public static final CSSColorValue BLUE = new CSSColorValue(0x0000FF, false);
  public static final CSSColorValue PURPLE = new CSSColorValue(0x800080, false);
  public static final CSSColorValue TEAL = new CSSColorValue(0x008080, false);
  public static final CSSColorValue FUCHSIA = new CSSColorValue(0xFF00FF, false);
  public static final CSSColorValue AQUA = new CSSColorValue(0x00FFFF, false);

  private HtmlColors() {}
}
