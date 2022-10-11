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
 * $Id: ComputeBreakabilityStep.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.layouting.renderer.process;

import org.jfree.layouting.renderer.model.FinishedRenderNode;
import org.jfree.layouting.renderer.model.ParagraphRenderBox;
import org.jfree.layouting.renderer.model.RenderBox;
import org.jfree.layouting.renderer.model.RenderNode;
import org.jfree.layouting.renderer.model.page.LogicalPageBox;
import org.jfree.layouting.renderer.model.table.TableRenderBox;
import org.jfree.layouting.renderer.model.table.TableRowRenderBox;

/**
 * Computes the size of the non-breakable areas at the beginning and end
 * of each block-level box.
 *
 * @author Thomas Morgner
 */
public class ComputeBreakabilityStep extends IterateVisualProcessStep
{
  public ComputeBreakabilityStep()
  {
  }

  public void compute(final LogicalPageBox logicalPageBox)
  {
    startProcessing(logicalPageBox);
  }

  protected void processParagraphChilds(final ParagraphRenderBox box)
  {
    // Inline elements count as one line, even if they have more than
    // one line of content. If a user wants to use inline-boxes or tables,
    // then he/she has to live with some limitations.
  }

  protected void finishBlockLevelBox(final RenderBox box)
  {
    if (box instanceof ParagraphRenderBox)
    {
      final ParagraphRenderBox paragraph = (ParagraphRenderBox) box;
      finishParagraph(paragraph);
      return;
    }
    else if (box instanceof TableRenderBox)
    {
      final TableRenderBox table = (TableRenderBox) box;
      finishTable(table);
      return;
    }
    else if (box instanceof TableRowRenderBox)
    {
      final TableRowRenderBox row = (TableRowRenderBox) box;
      finishTableRow(row);
      return;
    }

    // do something ..
    // for now, we ignore most of the stuff, and assume that orphans and
    // widows count for paragraphs and tables, and not for the ordinary stuff.
    final RenderNode firstNode = findNonFinishedVisibleFirst (box);
    if (firstNode instanceof RenderBox)
    {
      final RenderBox firstBox = (RenderBox) firstNode;
      box.setOrphansSize(firstBox.getOrphansSize());
    }
    else if (firstNode != null)
    {
      box.setOrphansSize(firstNode.getHeight());
    }

    final RenderNode lastNode = findNonFinishedVisibleLast(box);
    if (lastNode instanceof RenderBox)
    {
      final RenderBox lastBox = (RenderBox) lastNode;
      box.setWidowsSize(lastBox.getOrphansSize());
    }
    else if (lastNode != null)
    {
      box.setWidowsSize(lastNode.getHeight());
    }
  }

  private RenderNode findNonFinishedVisibleFirst (final RenderBox box)
  {
    RenderNode node = box.getVisibleFirst();
    while (node instanceof FinishedRenderNode)
    {
      node = node.getVisibleNext();
    }
    return node;
  }

  private RenderNode findNonFinishedVisibleLast (final RenderBox box)
  {
    RenderNode node = box.getVisibleLast();
    while (node instanceof FinishedRenderNode)
    {
      node = node.getVisiblePrev();
    }
    return node;
  }

  private void finishTableRow(final TableRowRenderBox box)
  {
    // A table row is different. It behaves as if it is a linebox.
    long orphanSize = 0;
    long widowSize = 0;
    int linecount = 0;

    RenderNode node = box.getVisibleFirst();
    while (node != null)
    {
      if (node instanceof RenderBox == false)
      {
        node = node.getVisibleNext();
        continue;
      }

      final RenderBox cellBox = (RenderBox) node;
      orphanSize = Math.max (cellBox.getOrphansSize(), orphanSize);
      widowSize = Math.max (cellBox.getWidowsSize(), widowSize);
      linecount = Math.max (cellBox.getLineCount(), linecount);
      node = node.getVisibleNext();
    }

    box.setOrphansSize(orphanSize);
    box.setWidowsSize(widowSize);
    box.setLineCount(linecount);
  }

  private void finishTable(final TableRenderBox box)
  {
    // Tables are simple right now. Just grab whatever you get ..
    // ignore non-renderable stuff ..

    RenderNode node = box.getVisibleFirst();
    if (node instanceof RenderBox)
    {
      // This is not very valid now.
      final RenderBox firstBox = (RenderBox) node;
      box.setOrphansSize(firstBox.getOrphansSize());
    }
    else
    {
      box.setOrphansSize(box.getHeight());
    }


    node = box.getVisibleLast();
    if (node instanceof RenderBox)
    {
      final RenderBox lastBox = (RenderBox) node;
      box.setWidowsSize(lastBox.getWidowsSize());
    }
    else
    {
      box.setWidowsSize(box.getHeight());
    }
  }

  private void finishParagraph(final ParagraphRenderBox box)
  {
    int lineCount = 0;
    RenderNode node = box.getVisibleFirst();
    while (node != null)
    {
      // Each node is a line.
      lineCount += 1;
      if (lineCount == box.getOrphans())
      {
        break;
      }
      node = node.getVisibleNext();
    }

    if (node == null)
    {
      box.setOrphansSize(box.getHeight());
    }
    else
    {
      final long nodeY2 = (node.getY() + node.getHeight());
      box.setOrphansSize(nodeY2 - box.getY());
    }

    lineCount = 0;
    node = box.getVisibleLast();
    while (node != null)
    {
      // Each node is a line.
      lineCount += 1;
      if (lineCount == box.getWidows())
      {
        break;
      }
      node = node.getVisiblePrev();
    }

    if (node == null)
    {
      box.setWidowsSize(box.getHeight());
    }
    else
    {
      final long nodeY2 = (node.getY() + node.getHeight());
      final long paragraphY2 = box.getY() + box.getHeight();
      box.setWidowsSize(paragraphY2 - nodeY2);
    }
  }

}
