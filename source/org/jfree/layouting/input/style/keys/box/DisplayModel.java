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
 * $Id: DisplayModel.java 2739 2007-04-02 11:41:22Z taqua $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.layouting.input.style.keys.box;

import org.jfree.layouting.input.style.values.CSSConstant;

/**
 * The DisplayModel selects the algorithm used to layout an element. This is
 * equal to selecting a LayoutManager in Java.
 * <p/>
 * Creation-Date: 27.10.2005, 21:03:12
 *
 * @author Thomas Morgner
 */
public class DisplayModel
{
  /**
   * If this is not an inline-level element, the effect is the same as for
   * 'block-inside'. Otherwise the element's inline-level children and text
   * sequences that come before the first block-level child are rendered as
   * additional inline boxes for the line boxes of the containing block. Ditto
   * for the text sequences and inline-level children after the last block-level
   * child. The other children and text sequences are rendered as for
   * 'block-inside'.
   */
  public static final CSSConstant INLINE_INSIDE = new CSSConstant(
          "inline-inside");

  /**
   * Child elements are rendered as described for their 'display-role'.
   * Sequences of inline-level elements and anonymous inline elements (ignoring
   * elements with a display-role of 'none') are rendered as one or more line
   * boxes. (How many line boxes depends on the line-breaking rules, see the
   * Text module [[!CSS3-text].)
   */
  public static final CSSConstant BLOCK_INSIDE =
          new CSSConstant("block-inside");

  /** See the table module */
  public static final CSSConstant TABLE = new CSSConstant("table");
  /** Not yet used. */
  public static final CSSConstant RUBY = new CSSConstant("ruby");

  /**
   * A JFreeReport compatibility setting. Enables the absolute positioning mode.
   */
  public static final CSSConstant ABSOLUTE = new CSSConstant("absolute");

  private DisplayModel()
  {
  }

}
