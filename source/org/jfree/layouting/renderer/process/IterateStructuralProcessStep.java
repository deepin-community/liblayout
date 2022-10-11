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
 * $Id: IterateStructuralProcessStep.java 2755 2007-04-10 19:27:09Z taqua $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.renderer.process;

import org.jfree.layouting.renderer.model.BlockRenderBox;
import org.jfree.layouting.renderer.model.InlineRenderBox;
import org.jfree.layouting.renderer.model.NormalFlowRenderBox;
import org.jfree.layouting.renderer.model.ParagraphRenderBox;
import org.jfree.layouting.renderer.model.RenderBox;
import org.jfree.layouting.renderer.model.RenderNode;
import org.jfree.layouting.renderer.model.page.LogicalPageBox;

/**
 * Iterates over the document tree using the display-role of the current node
 * as selector. Usually all structural processing steps use this iteration
 * strategy.
 *
 * @author Thomas Morgner
 */
public abstract class IterateStructuralProcessStep
{
  protected IterateStructuralProcessStep()
  {
  }

  protected void startProcessing (final RenderNode node)
  {
    if (node instanceof InlineRenderBox)
    {
      final InlineRenderBox box = (InlineRenderBox) node;
      if (startInlineBox(box))
      {
        processBoxChilds(box);
      }
      finishInlineBox(box);
    }
    else if (node instanceof NormalFlowRenderBox)
    {
      final NormalFlowRenderBox box = (NormalFlowRenderBox) node;
      startNormalFlow(box);
      final NormalFlowRenderBox[] flows = box.getFlows();
      for (int i = 0; i < flows.length; i++)
      {
        final NormalFlowRenderBox flow = flows[i];
        startProcessing(flow);
      }
      processBoxChilds(box);
      finishNormalFlow(box);
    }
    else if (node instanceof ParagraphRenderBox)
    {
      final ParagraphRenderBox box = (ParagraphRenderBox) node;
      if (startBlockBox(box))
      {
        processParagraphChilds(box);
      }
      finishBlockBox(box);
    }
    else if (node instanceof LogicalPageBox)
    {
      final LogicalPageBox box = (LogicalPageBox) node;
      if (startBlockBox(box))
      {
        startProcessing(box.getHeaderArea());
        processBoxChilds(box);
        startProcessing(box.getFooterArea());
      }
      finishBlockBox(box);
    }
    else if (node instanceof BlockRenderBox)
    {
      final BlockRenderBox box = (BlockRenderBox) node;
      if (startBlockBox(box))
      {
        processBoxChilds(box);
      }
      finishBlockBox(box);
    }
    else if (node instanceof RenderBox)
    {
      final RenderBox box = (RenderBox) node;
      startOtherBox(box);
      processBoxChilds(box);
      finishOtherBox(box);
    }
    else
    {
      startOtherNode(node);
      finishOtherNode(node);
    }
  }

  protected void processParagraphChilds(final ParagraphRenderBox box)
  {
    processBoxChilds(box.getPool());
  }

  protected void finishNormalFlow(final NormalFlowRenderBox box)
  {

  }

  protected void startNormalFlow(final NormalFlowRenderBox box)
  {

  }

  protected void processBoxChilds(final RenderBox box)
  {
    RenderNode node = box.getFirstChild();
    while (node != null)
    {
      startProcessing(node);
      node = node.getNext();
    }
  }

  protected void startOtherNode (final RenderNode node)
  {
  }

  protected void finishOtherNode (final RenderNode node)
  {
  }

  protected boolean startBlockBox (final BlockRenderBox box)
  {
    return true;
  }

  protected void finishBlockBox (final BlockRenderBox box)
  {
  }

  protected boolean startInlineBox (final InlineRenderBox box)
  {
    return true;
  }

  protected void finishInlineBox (final InlineRenderBox box)
  {
  }

  protected boolean startOtherBox (final RenderBox box)
  {
    return true;
  }

  protected void finishOtherBox (final RenderBox box)
  {
  }
}
