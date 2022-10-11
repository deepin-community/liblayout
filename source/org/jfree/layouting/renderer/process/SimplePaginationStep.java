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
 * $Id: SimplePaginationStep.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.layouting.renderer.process;

import org.jfree.layouting.input.style.keys.box.DisplayRole;
import org.jfree.layouting.input.style.values.CSSConstant;
import org.jfree.layouting.renderer.model.PageAreaRenderBox;
import org.jfree.layouting.renderer.model.ParagraphPoolBox;
import org.jfree.layouting.renderer.model.ParagraphRenderBox;
import org.jfree.layouting.renderer.model.RenderBox;
import org.jfree.layouting.renderer.model.RenderNode;
import org.jfree.layouting.renderer.model.page.LogicalPageBox;
import org.jfree.layouting.renderer.model.table.TableCellRenderBox;
import org.jfree.layouting.renderer.model.table.TableColumnGroupNode;
import org.jfree.layouting.renderer.model.table.TableRenderBox;
import org.jfree.layouting.renderer.model.table.TableSectionRenderBox;

/**
 * Computes the pagination for streaming outputs. The only real purpose of this
 * class is to rearrange the tables so that the header-body-footer order is
 * maintained.
 *
 * This step ignores the pageformat and does not check for pagebreaks or page
 * overflows.
 * <p/>
 * Todo: This class needs some serious attention later.
 *
 * @author Thomas Morgner
 */
public class SimplePaginationStep extends IterateVisualProcessStep
{
  /**
   * The current shift-distance. This can increase, but never decrease.
   */
  private long shift;
  private long stickyMarker;
  private BoxShifter boxShifter;
  private long footerHeight;
  private long headerHeight;

  public SimplePaginationStep()
  {
    boxShifter = new BoxShifter();
  }

  public void performPagebreak(final LogicalPageBox pageBox)
  {
    stickyMarker = pageBox.getChangeTracker();
    shift = 0;

    startRootProcessing(pageBox);
  }

  private void startRootProcessing(final LogicalPageBox pageBox)
  {
    // Note: For now, we limit both the header and footer to a single physical
    // page. This safes me a lot of trouble for now.

    // we have to iterate using a more complex schema here.
    // Step one: layout the header-section. Record that height.
    final PageAreaRenderBox headerArea = pageBox.getHeaderArea();
    headerHeight = headerArea.getHeight();

    // Step two: The footer. For the footer, we have to traverse the whole
    // thing backwards. Nonetheless, we've got the height.
    final PageAreaRenderBox footerArea = pageBox.getFooterArea();
    footerHeight = footerArea.getHeight();

    startProcessing(pageBox);
  }

  protected boolean startInlineLevelBox(final RenderBox box)
  {
    // Skip the contents ..
    return false;
  }

  protected void processParagraphChilds(final ParagraphRenderBox box)
  {
    // Process the direct childs of the paragraph
    // Each direct child represents a line ..
    // Todo: Include orphan and widow stuff ..

    // First: Check the number of lines. (Should have been precomputed)
    // Second: Check whether and where the orphans- and widows-rules apply
    // Third: Shift the lines.
    RenderNode node = box.getVisibleFirst();
    while (node != null)
    {
      // all childs of the linebox container must be inline boxes. They
      // represent the lines in the paragraph. Any other element here is
      // a error that must be reported
      if (node instanceof ParagraphPoolBox == false)
      {
        throw new IllegalStateException("Encountered " + node.getClass());
      }
      final ParagraphPoolBox inlineRenderBox = (ParagraphPoolBox) node;
      if (startLine(inlineRenderBox))
      {
        processBoxChilds(inlineRenderBox);
      }
      finishLine(inlineRenderBox);

      node = node.getVisibleNext();
    }
  }

  private boolean isNodeProcessable(final RenderNode node)
  {
    if (node.getStickyMarker() == stickyMarker)
    {
      // already finished. No need to touch that thing ..
      return false;
    }
    if (node.isIgnorableForRendering())
    {
      return false;
    }
    return true;
  }

  protected void processBlockLevelNode(final RenderNode node)
  {
    if (isNodeProcessable(node) == false)
    {
      return;
    }

    // Apply the current shift.
    node.setY(node.getY() + shift);

    // oh, we have to move the node downwards.
    node.setY(node.getY() + shift);
    // Make sure we do not move that node later on ..
    node.setStickyMarker(stickyMarker);
  }

  protected void processTable(final TableRenderBox table)
  {
    final long originalShift = shift;
    final long height = table.getHeight();

    final long y = table.getY();
    final long y2 = shift + y + height;
    if (table.isIgnorableForRendering())
    {
      return;
    }

    // ordinary shifting is used ..
    table.setY(table.getY() + shift);
    table.setStickyMarker(stickyMarker);

    processTableSection(table, DisplayRole.TABLE_HEADER_GROUP);
    processTableSection(table, DisplayRole.TABLE_ROW_GROUP);
    processTableSection(table, DisplayRole.TABLE_FOOTER_GROUP);

    // Processing all he sections can have an effect on the height of the table
    // Shifting content down increases the height of the table.
    final long finalHeight = table.getHeight();
    final long delta = finalHeight - height;
    if (delta < 0)
    {
      throw new IllegalStateException("A table can/must not shrink!");
    }
  }

  private void processTableSection(final TableRenderBox box,
                                   final CSSConstant role)
  {
    RenderNode rowGroupNode = box.getFirstChild();
    while (rowGroupNode != null)
    {
      if (rowGroupNode instanceof TableSectionRenderBox == false)
      {
        rowGroupNode = rowGroupNode.getNext();
        continue;
      }

      final TableSectionRenderBox sectionBox =
          (TableSectionRenderBox) rowGroupNode;
      if (role.equals
          (sectionBox.getDisplayRole()) == false)
      {
        // not a header ..
        rowGroupNode = rowGroupNode.getNext();
        continue;
      }

      startProcessing(rowGroupNode);
      rowGroupNode = rowGroupNode.getNext();
    }
  }

  protected void processBlockLevelChild(final RenderNode node)
  {
    if (node instanceof TableRenderBox)
    {
      final TableRenderBox table = (TableRenderBox) node;
      processTable(table);
    }
    else
    {
      super.processBlockLevelChild(node);
    }
  }

  protected boolean startBlockLevelBox(final RenderBox box)
  {
    if (isNodeProcessable(box) == false)
    {
      // Not shifted, as this box will not be affected. It is not part of the
      // content window.
      return false;
    }

    final long y = box.getY();
    if (box instanceof TableCellRenderBox)
    {
      // table cells get a special treatment when the row is computed ..
      box.setY(y + shift);
      box.setStickyMarker(stickyMarker);
      return true;
    }

    if (box instanceof TableColumnGroupNode)
    {
      throw new IllegalArgumentException("This is not expected here");
    }

    // At this point, we know that the box is way to large to fit the remaining
    // space. But we know, that content will be processed on that page (by
    // looking at the orphan-size) and that the break occurrs somewhere inside
    // of the children of this box. So shall it be!

    box.setY(box.getY() + shift);
    box.setStickyMarker(stickyMarker);
    return true;
  }

  protected boolean startLine(final ParagraphPoolBox box)
  {
    if (isNodeProcessable(box) == false)
    {
      return false;
    }
    boxShifter.shiftBox(box, shift);
    return false;
  }

  protected void finishLine(final ParagraphPoolBox inlineRenderBox)
  {
  }
}
