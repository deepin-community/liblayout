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
 * $Id: BlockRenderBox.java 2739 2007-04-02 11:41:22Z taqua $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.renderer.model;

/**
 * A block box behaves according to the 'display:block' layouting rules. In the
 * simplest case, all childs will consume the complete width and get stacked
 * below each other.
 * <p/>
 * Live would be boring if everything was simple: Floats and absolutly
 * positioned elements enrich and complicate the layout schema. Absolute
 * elements do not affect the normal flow, but inject an invisible placeholder
 * to keep track of their assumed normal-flow position.
 * <p/>
 * (Using Mozilla's behaviour as reference, I assume that absolutly positioned
 * elements are controled from outside the normal-flow (ie. parallel to the
 * normal flow, but positioned relative to their computed placeholder position
 * inside the normal flow. Weird? yes!)
 * <p/>
 * Float-elements are positioned inside the block context and reduce the size of
 * the linebox. Float-elements are positioned once when they are defined and
 * will not alter any previously defined content.
 * <p/>
 * As floats from previous boxes may overlap with later boxes, all floats must
 * be placed on a global storage. The only difference between absolutely
 * positioned and floating elements is *where* they can be placed, and whether
 * they influence the content under them. (Floats do, abs-pos dont).
 *
 * @author Thomas Morgner
 */
public class BlockRenderBox extends RenderBox
{
  public BlockRenderBox(final BoxDefinition boxDefinition)
  {
    super(boxDefinition);

    // hardcoded for now, content forms lines, which flow from top to bottom
    // and each line flows horizontally (later with support for LTR and RTL)

    // Major axis vertical means, all childs will be placed below each other
    setMajorAxis(VERTICAL_AXIS);
    // Minor axis horizontal: All childs may be shifted to the left or right
    // to do some text alignment
    setMinorAxis(HORIZONTAL_AXIS);
  }
//
//  /**
//   * Returns the baseline info for the given node. This can be null, if the node
//   * does not have any baseline info.
//   *
//   * @return
//   */
//  public ExtendedBaselineInfo getBaselineInfo()
//  {
//    long shift = getLeadingInsets(getMajorAxis()) + getLeadingSpace(getMajorAxis());
//    RenderNode firstChild = getFirstChild();
//    while (firstChild != null)
//    {
//      if (firstChild.isIgnorableForRendering() == false)
//      {
//        final ExtendedBaselineInfo baselineInfo = firstChild.getBaselineInfo();
//        if (baselineInfo == null)
//        {
//          return null;
//        }
//
//        return baselineInfo.shift(shift);
//      }
//      firstChild = firstChild.getNext();
//    }
//    return null;
//  }
}
