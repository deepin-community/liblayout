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
 * $Id: PaginationStep.java 3524 2007-10-16 11:26:31Z tmorgner $
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
 * Computes the pagination. This step checks, whether content crosses an inner
 * or outer page boundary. In that case, the content is shifted downwards to the
 * next page and then marked as sticky (so it wont move any further later; this
 * prevents infinite loops).
 * <p/>
 * This kind of shifting does not apply to inline-elements - they get shifted
 * when their linebox gets shifted.
 *
 * @author Thomas Morgner
 */
public class PaginationStep extends IterateVisualProcessStep
{
  /**
   * The start of the current logical page. Any content that starts before that
   * is considered sicky and therefore not shifted.
   */
  private long pageStartOffset;
  private boolean pageOverflow;

  /**
   * The current shift-distance. This can increase, but never decrease.
   */
  private long shift;
  private long[] physicalBreaks;
  private long headerHeight;
  private long footerHeight;
  private long stickyMarker;
  private BoxShifter boxShifter;

  public PaginationStep()
  {
    boxShifter = new BoxShifter();
  }

  public boolean performPagebreak(final LogicalPageBox pageBox)
  {
    pageStartOffset = 0;
    pageOverflow = false;
    shift = 0;
    physicalBreaks = null;
    headerHeight = 0;
    footerHeight = 0;
    stickyMarker = 0;

    pageStartOffset = pageBox.getPageOffset();
    pageOverflow = false;
    stickyMarker = pageBox.getChangeTracker();
    shift = 0;

    startRootProcessing(pageBox);
    return pageOverflow;
  }

  public long getNextOffset()
  {
    return physicalBreaks[physicalBreaks.length - 1];
  }

  private boolean isCrossingBreak(final RenderNode node)
  {
    int y1Index = -1;
    int y2Index = -1;
    final long nodeY1 = node.getY() + shift;
    final long nodeY2 = nodeY1 + node.getHeight();

    for (int i = 0; i < physicalBreaks.length; i++)
    {
      final long physicalBreak = physicalBreaks[i];
      if (nodeY1 >= physicalBreak)
      {
        y1Index = i;
      }
      if (nodeY2 >= physicalBreak)
      {
        y2Index = i;
      }
    }
    return (y1Index != y2Index);
  }

  private void startRootProcessing(final LogicalPageBox pageBox)
  {
    // Note: For now, we limit both the header and footer to a single physical
    // page. This safes me a lot of trouble for now.

    // we have to iterate using a more complex schema here.
    // Step one: layout the header-section. Record that height.
    final PageAreaRenderBox headerArea = pageBox.getHeaderArea();
    headerHeight = headerArea.getHeight();
    headerArea.setHeight(headerHeight);

    // Step two: The footer. For the footer, we have to traverse the whole
    // thing backwards. Nonetheless, we've got the height.
    final PageAreaRenderBox footerArea = pageBox.getFooterArea();
    footerHeight = footerArea.getHeight();
    footerArea.setHeight(footerHeight);

    // Step three: Perform the breaks. Make sure that at least one
    // line of the normal-flow content can be processed.
    // Reduce the footer and if that's not sufficient the header as well.
    final long[] originalBreaks = pageBox.getPhysicalBreaks(RenderNode.VERTICAL_AXIS);
    physicalBreaks = new long[originalBreaks.length + 1];
    physicalBreaks[0] = pageStartOffset;
    for (int i = 0; i < originalBreaks.length; i++)
    {
      physicalBreaks[i + 1] = pageStartOffset +
          (originalBreaks[i] - headerHeight);
    }

    // This is a bit hacky, isnt it?
    physicalBreaks[physicalBreaks.length - 1] -= footerHeight;

    // assertation
    final long totalPageHeight = physicalBreaks[physicalBreaks.length - 1] - physicalBreaks[0];
    if (totalPageHeight + headerHeight + footerHeight != pageBox.getPageHeight())
    {
      throw new IllegalStateException("Assertation failed: Page height");
    }

    // now consume the usable height and stop if all space is used or the
    // end of the document has been reached. Process at least one line of
    // content.

    // now process all the other content (excluding the header and footer area)
    if (startBlockLevelBox(pageBox))
    {
      processBoxChilds(pageBox);
    }
    finishBlockLevelBox(pageBox);
//    if (pageOverflow)
//    {
//      Log.debug ("Pagination: " + totalPageHeight);
//    }
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
    final long y2 = node.getY() + node.getHeight();
    if (y2 < pageStartOffset)
    {
      // not in range.
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
    // question: Does the node cross a boundary or is is a simple child.
    // this also rejects childs that left the visible area ..
    if (isCrossingBreak(node) == false)
    {
      return;
    }

    // oh, we have to move the node downwards.
    final long nextBreakShiftDistance = getNextBreak(node.getY()) - node.getY();
    node.setY(node.getY() + nextBreakShiftDistance);
    boxShifter.extendHeight(node, nextBreakShiftDistance);
    shift += nextBreakShiftDistance;

    // Make sure we do not move that node later on ..
    node.setStickyMarker(stickyMarker);
  }

  protected void processTable(final TableRenderBox table)
  {
    final long originalShift = shift;
    final long height = table.getHeight();

    final long y = table.getY();
    final long y2 = shift + y + height;
    if (y2 < pageStartOffset)
    {
      // not in range.
      return;
    }
    if (table.isIgnorableForRendering())
    {
      return;
    }


    final long nextBreak = getNextBreak(y + shift);
    final long usableHeight = nextBreak - (y + shift);
    if (usableHeight > 0)
    {
      final long reservedHeight = Math.min(height,
          table.getOrphansSize() + table.getWidowsSize());

      if (table.isAvoidPagebreakInside() ||
          reservedHeight > usableHeight ||
          table.getOrphansSize() > usableHeight)
      {
        final long newShift = nextBreak - y;

        // orphans and widows rules prevent a break.

        if (newShift > 0)
        {
          table.setY(table.getY() + newShift);
          // the table itself shifts, but the parent extends ..
          boxShifter.extendHeight(table.getParent(), newShift - shift);
          shift = newShift;
          table.setStickyMarker(stickyMarker);
        }
        else if (newShift < 0)
        {
          table.setY(table.getY() + shift);
          pageOverflow = true;
        }
        else
        {
          // No shift needed, no processing of the childs needed.
        }
      }
      else
      {
        // ordinary shifting is used ..
        table.setY(table.getY() + shift);
        table.setStickyMarker(stickyMarker);
      }
    }
    else
    {
      // ordinary shifting is used ..
      table.setY(table.getY() + shift);
      table.setStickyMarker(stickyMarker);
    }

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

    if (isOverflow(box))
    {
      pageOverflow = true;
      boxShifter.shiftBox(box, shift);
      return false;
    }

    // Does the full box fit into the current page?
    // Apply the current shift to the box only (non-recursive)
    if (isCrossingBreak(box) == false)
    {
      // It fits into the page. So why bother ..
      boxShifter.shiftBox(box, shift);
      return false;
    }

    if (box instanceof TableColumnGroupNode)
    {
      throw new IllegalArgumentException("This is not expected here");
    }

    // At this point, we know that the node is in fact in range - that
    // means it intersects the current content-window.

    // Break rules: We check for collapsing top-edges. (This is like the margin
    // processing. If we encounter content at this edge, we can check whether
    // that content will fit on the page. If it does, ok, if not - move all
    // boxed from here up to (and including) the content to the bottom.

    final long nextBreak = getNextBreak(y + shift);
    final long usableHeight = nextBreak - (y + shift);
    final long height = box.getHeight();
    final long reservedHeight = Math.min
        (height, box.getOrphansSize() + box.getWidowsSize());

    if (box.isAvoidPagebreakInside() ||
        usableHeight < 0 || reservedHeight > height ||
        box.getOrphansSize() > usableHeight)
    {
      final long newShift = nextBreak - y;

      // orphans and widows rules prevent a break.

      if (newShift > 0)
      {
        boxShifter.shiftBox(box, newShift);
        boxShifter.extendHeight(box.getParent(), newShift - shift);
        shift = newShift;
        box.setStickyMarker(stickyMarker);
        return false;
      }
      else if (newShift < 0)
      {
        boxShifter.shiftBox(box, shift);
        pageOverflow = true;
        return false;
      }
      else
      {
        return false;
      }
    }

    // At this point, we know that the box is way to large to fit the remaining
    // space. But we know, that content will be processed on that page (by
    // looking at the orphan-size) and that the break occurrs somewhere inside
    // of the children of this box. So shall it be!

    box.setY(box.getY() + shift);
    box.setStickyMarker(stickyMarker);
    return true;
  }

  private boolean isOverflow(final RenderNode box)
  {
    final long lastBreak = physicalBreaks[physicalBreaks.length - 1];
    if (box.getY() + shift > lastBreak)
    {
      return true;
    }
    return false;
  }

  private long getNextBreak(final long y)
  {
    for (int i = 0; i < physicalBreaks.length; i++)
    {
      final long physicalBreak = physicalBreaks[i];
      if (y < physicalBreak)
      {
        return physicalBreak;
      }
    }
    // Fall back to the last one, for heavens sake..
    return physicalBreaks[physicalBreaks.length - 1];
  }

  protected boolean startLine(final ParagraphPoolBox box)
  {
    if (isNodeProcessable(box) == false)
    {
      return false;
    }

    if (isOverflow(box))
    {
      boxShifter.shiftBox(box, shift);
      pageOverflow = true;
      return false;
    }

    final long nextBreak = getNextBreak(box.getY() + shift);
    final long usableHeight = nextBreak - (box.getY() + shift);
    if (box.getHeight() < usableHeight)
    {
      boxShifter.shiftBox(box, shift);
      box.setStickyMarker(stickyMarker);
      return false;
    }

    // It gets more complicated. Shift it to the next page. The first node shall
    // be marked as done.
    final long newShift = nextBreak - box.getY();
    if (newShift > 0)
    {
      // we already left the current page.
      boxShifter.shiftBox(box, newShift);
      boxShifter.extendHeight(box, newShift - shift);
      shift = newShift;
      box.setStickyMarker(stickyMarker);
    }
    else if (newShift < 0)
    {
      pageOverflow = true;
      boxShifter.shiftBox(box, shift);
    }
    else
    {
      boxShifter.shiftBox(box, shift);
    }
    return false;
  }

  protected void finishLine(final ParagraphPoolBox inlineRenderBox)
  {
  }
}
