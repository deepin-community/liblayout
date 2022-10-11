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
 * $Id: FloatDisplace.java 2739 2007-04-02 11:41:22Z taqua $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.layouting.input.style.keys.box;

import org.jfree.layouting.input.style.values.CSSConstant;

/**
 * Creation-Date: 27.10.2005, 22:21:37
 *
 * @author Thomas Morgner
 */
public class FloatDisplace
{
  /**
   * Line boxes should be shortened and moved so as to avoid floats. The margin,
   * border, padding and background of the element are not affected by floats.
   * (This is the behavior as described in CSS2.)
   */
  public static final CSSConstant LINE = new CSSConstant("line");
  /**
   * The distance between the margin edge of the floats and the start of the
   * line box is set to the distance between the active reference indent edge
   * (see the 'indent-edge-reset' property) and the content edge of the block
   * box. This ensures that relative indents are preserved in the presence of
   * floats.
   */
  public static final CSSConstant INDENT = new CSSConstant("indent");
  /**
   * The containing block's width as used by the horizontal formatting model is
   * reduced by the width of the floats intruding upon its content area (not
   * taking into account floating descendants or floating elements that appear
   * later in the document tree). The block is then flowed in this reduced
   * containing block width.
   * <p/>
   * If the effective containing block width is, by the algorithm given above,
   * reduced to a value smaller than the sum of the margin-left,
   * border-width-left, padding-left, width, padding-right, border-width-right,
   * and margin-right values (treating any 'auto' values as zero) then the
   * margin-top of the block is increased until the effective containing block
   * width is no longer so constrained or until all floats have been cleared,
   * whichever occurs first.
   */
  public static final CSSConstant BLOCK = new CSSConstant("block");
  /**
   * As for the 'block' value, but the determination of intrusions that adjust
   * the width of the block is done separately on each page on which the block
   * appears. Thus, the block may be narrowed on the first page due to one or
   * more intrusions, but may expand (or contract) its width on subsequent pages
   * with different intrusions. The computed value of the 'width' property for
   * this case is...?
   */
  public static final CSSConstant BLOCK_WITHIN_PAGE =
          new CSSConstant("block-within-page");

  private FloatDisplace()
  {
  }
}
