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
 * $Id: InlineRenderBox.java 2755 2007-04-10 19:27:09Z taqua $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.renderer.model;

/**
 * An inline box is some floating text that might be broken down into lines. The
 * actual linebreaking is performed/initiated by the block context.
 * <p/>
 * Breaking behaviour: An InlineBox forwards all breaks to its childs and tries
 * to break them according to their rules. BlockContent and ImageContent is
 * considered not-very-breakable by default, breaks on that should be avoided.
 *
 * @author Thomas Morgner
 */
public class InlineRenderBox extends RenderBox
{
  public InlineRenderBox(final BoxDefinition boxDefinition)
  {
    super(boxDefinition);

    // hardcoded for now, content forms lines, which flow from top to bottom
    // and each line flows horizontally (later with support for LTR and RTL)

    // Major axis: All child boxes are placed from left-to-right
    setMajorAxis(HORIZONTAL_AXIS);
    // Minor: The childs might be aligned on their position (shifted up or down)
    setMinorAxis(VERTICAL_AXIS);
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
//    // Align the various baselines along the dominant baseline.
//    RenderNode node = getFirstChild();
//    while (node != null)
//    {
//      if (node.isIgnorableForRendering())
//      {
//        node = node.getNext();
//        continue;
//      }
//
//      ExtendedBaselineInfo baseLine = node.getBaselineInfo();
//      if (baseLine != null)
//      {
//        final long shift = getLeadingSpace(getMinorAxis()) + getLeadingInsets(getMinorAxis());
//        return baseLine.shift(shift);
//      }
//      node = node.getNext();
//    }
//    return null;
//  }
//
//  private AlignmentCollector createtAlignmentCollector ()
//  {
//    if (alignmentCollector == null)
//    {
//
//      // cache me, if you can ...
//      AlignmentCollector alignmentCollector =
//              new AlignmentCollector(getMinorAxis(), 0);
//
//      RenderNode child = getFirstChild();
//      while (child != null)
//      {
//        if (child.isIgnorableForRendering())
//        {
//          // empty childs may affect the margin computation or cause linebreaks,
//          // but they must not appear here.
//          child = child.getNext();
//          continue;
//        }
//
//        alignmentCollector.add(child);
//        child = child.getNext();
//      }
//
//      validate(RenderNodeState.LAYOUTING);
//      this.alignmentCollector = alignmentCollector;
//    }
//    return this.alignmentCollector;
//  }
//
}
