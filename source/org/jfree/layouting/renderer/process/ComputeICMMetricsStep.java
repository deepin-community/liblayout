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
 * $Id: ComputeICMMetricsStep.java 6489 2008-11-28 14:53:40Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.renderer.process;

import org.jfree.layouting.renderer.border.RenderLength;
import org.jfree.layouting.renderer.model.ParagraphRenderBox;
import org.jfree.layouting.renderer.model.RenderBox;
import org.jfree.layouting.renderer.model.RenderNode;
import org.jfree.layouting.renderer.model.RenderableReplacedContent;
import org.jfree.layouting.renderer.model.ComputedLayoutProperties;
import org.jfree.layouting.renderer.model.page.LogicalPageBox;
import org.jfree.layouting.util.geom.StrictDimension;

/**
 * This step performs the first layouting step. The ICM-step computes the
 * preferred size of all elements (that is the minimum size the element would
 * consume if there were infinite space available) and the minimum chunk size
 * (that is the biggest unbreakable content in an element).
 * <p/>
 * The preferred size is based on the box size (that means: No margins
 * included!).
 * <p/>
 * That step produces the preferred size for the nodes.
 * <p/>
 * As long as paragraphs can be nested, we cannot assume anything here. A nested
 * paragraph can have linebreaks all the time and thus it is dangerous to skip
 * the ICM-Computation on such nodes. Maybe we can tweak something with a change
 * counter or so.
 *
 * @author Thomas Morgner
 */
public class ComputeICMMetricsStep extends IterateVisualProcessStep
{
  public ComputeICMMetricsStep()
  {
  }

  public void compute(final LogicalPageBox root)
  {
    startProcessing(root);
  }

  protected void processParagraphChilds(final ParagraphRenderBox box)
  {
    processBoxChilds(box.getLineboxContainer());
    //startProcessing(box.getLineboxContainer());
  }


  protected boolean startBlockLevelBox(final RenderBox box)
  {
//    if (box.isOpen())
//    {
//      return true;
//    }
//    if (box.isIcmMetricsFinished() == false)
//    {
//      return true;
//    }
//    return false;
    return true;
  }

  protected boolean startInlineLevelBox(final RenderBox box)
  {
//    if (box.isOpen())
//    {
//      return true;
//    }
//    if (box.isIcmMetricsFinished() == false)
//    {
//      return true;
//    }
//    return false;
    return true;
  }


  protected void finishInlineLevelBox(final RenderBox box)
  {
    // Sum up the width; Maximize the height.; add borders and padding
    final ComputedLayoutProperties blp = box.getComputedLayoutProperties();

    // horizontal border and padding ..
    final long hbp = blp.getBorderLeft() + blp.getBorderRight() +
        blp.getPaddingLeft() + blp.getPaddingRight() +
        blp.getMarginLeft() + blp.getMarginRight();

    long minChunkWidth = 0;
    long maxBoxWidth = 0;

    RenderNode node = box.getVisibleFirst();
    while (node != null)
    {
      maxBoxWidth += node.getMaximumBoxWidth();

      final long childChunkWidth = node.getMinimumChunkWidth();
      if (childChunkWidth > minChunkWidth)
      {
        minChunkWidth = childChunkWidth;
      }
      node = node.getVisibleNext();
    }

    box.setMinimumChunkWidth(hbp + minChunkWidth);
    box.setMaximumBoxWidth(hbp + maxBoxWidth);
    box.setIcmMetricsFinished(box.isOpen() == false);
  }

  protected void finishBlockLevelBox(final RenderBox box)
  {
    // Sum up the height; Maximize the width.; add borders and padding
    final ComputedLayoutProperties blp = box.getComputedLayoutProperties();

    // horizontal border and padding ..
    final long hbp = blp.getBorderLeft() + blp.getBorderRight() +
        blp.getPaddingLeft() + blp.getPaddingRight() +
        blp.getMarginLeft() + blp.getMarginRight();

    if (box instanceof ParagraphRenderBox)
    {
      // No margins, no additional checks. And we can be sure that this one
      // is the only child. (This is a cheap shortcut).
      final ParagraphRenderBox paragraph = (ParagraphRenderBox) box;
      final RenderNode linebox = paragraph.getLineboxContainer();
      box.setMinimumChunkWidth(hbp + linebox.getMinimumChunkWidth());
      box.setMaximumBoxWidth(hbp + linebox.getMaximumBoxWidth());
      box.setIcmMetricsFinished(box.isOpen() == false);
      return;
    }

    long minChunkWidth = 0;
    long maxBoxWidth = 0;

    RenderNode node = box.getVisibleFirst();
    while (node != null)
    {
      final long childChunkWidth = node.getMinimumChunkWidth();
      if (childChunkWidth > minChunkWidth)
      {
        minChunkWidth = childChunkWidth;
      }

      final long childBoxWidth = node.getMaximumBoxWidth();
      if (childBoxWidth > maxBoxWidth)
      {
        maxBoxWidth = childBoxWidth;
      }

      node = node.getVisibleNext();
    }

    box.setMinimumChunkWidth(hbp + minChunkWidth);
    box.setMaximumBoxWidth(hbp + maxBoxWidth);
    box.setIcmMetricsFinished(box.isOpen() == false);
  }

  protected void processInlineLevelNode(final RenderNode node)
  {
    // These nodes have no real change tracker; they are almost immutable anyway
    if (node.isIcmMetricsFinished())
    {
      return;
    }

    if (node instanceof RenderableReplacedContent)
    {
      final RenderableReplacedContent rpc = (RenderableReplacedContent) node;
      final RenderLength requestedWidth = rpc.getRequestedWidth();
      final StrictDimension contentSize = rpc.getContentSize();

      if (requestedWidth == RenderLength.AUTO)
      {
        node.setMaximumBoxWidth(contentSize.getWidth());
      }
      else
      {
        node.setMaximumBoxWidth(requestedWidth.resolve(contentSize.getWidth()));
      }

      node.setMinimumChunkWidth(0);
      // replaced content cannot have any childs anymore, so it is safe to
      // set this flag to an unconditional true.
      node.setIcmMetricsFinished(true);
    }
    // Text and spacer nodes have been computed at construction time ..
  }

  protected void processBlockLevelNode(final RenderNode node)
  {
    // These nodes have no real change tracker; they are almost immutable anyway
    if (node.isIcmMetricsFinished())
    {
      return;
    }

    if (node instanceof RenderableReplacedContent)
    {
      final RenderableReplacedContent rpc = (RenderableReplacedContent) node;
      final RenderLength requestedWidth = rpc.getRequestedWidth();
      final StrictDimension contentSize = rpc.getContentSize();

      if (requestedWidth == RenderLength.AUTO)
      {
        node.setMaximumBoxWidth(contentSize.getWidth());
      }
      else
      {
        node.setMaximumBoxWidth(requestedWidth.resolve(contentSize.getWidth()));
      }

      node.setMinimumChunkWidth(0);
      // replaced content cannot have any childs anymore, so it is safe to
      // set this flag to an unconditional true.
      node.setIcmMetricsFinished(true);
    }
    // Text and spacer nodes have been computed at construction time ..
  }
}
