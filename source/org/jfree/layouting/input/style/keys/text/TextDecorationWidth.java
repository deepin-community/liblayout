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
 * $Id: TextDecorationWidth.java 2739 2007-04-02 11:41:22Z taqua $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.layouting.input.style.keys.text;

import org.jfree.layouting.input.style.values.CSSConstant;

public class TextDecorationWidth
{
  /**
   * A bold is basicly an auto, that is thicker than the normal
   * auto value.
   */
  public static final CSSConstant BOLD = new CSSConstant("bold");

  /**
   * A dash is basicly an auto, that is thinner than the normal
   * auto value.
   */
  public static final CSSConstant DASH = new CSSConstant("dash");

  /**
   * The text decoration width is the normal text decoration width
   * for the nominal font. If no font characteristic exists for the
   * width of the text decoration in question, the user agent should
   * proceed as though 'auto' were specified.
   *
   * The computed value is 'normal'.
   */
  public static final CSSConstant NORMAL = new CSSConstant("normal");

  private TextDecorationWidth()
  {
  }
}
