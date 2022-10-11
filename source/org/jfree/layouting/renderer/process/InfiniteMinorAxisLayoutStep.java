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
 * $Id: InfiniteMinorAxisLayoutStep.java 6489 2008-11-28 14:53:40Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.renderer.process;

import org.jfree.layouting.input.style.keys.text.TextAlign;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.renderer.border.RenderLength;
import org.jfree.layouting.renderer.model.BlockRenderBox;
import org.jfree.layouting.renderer.model.FinishedRenderNode;
import org.jfree.layouting.renderer.model.InlineRenderBox;
import org.jfree.layouting.renderer.model.ParagraphPoolBox;
import org.jfree.layouting.renderer.model.ParagraphRenderBox;
import org.jfree.layouting.renderer.model.PlaceholderRenderNode;
import org.jfree.layouting.renderer.model.RenderBox;
import org.jfree.layouting.renderer.model.RenderNode;
import org.jfree.layouting.renderer.model.RenderableReplacedContent;
import org.jfree.layouting.renderer.model.RenderableText;
import org.jfree.layouting.renderer.model.SpacerRenderNode;
import org.jfree.layouting.renderer.model.StaticBoxLayoutProperties;
import org.jfree.layouting.renderer.model.ComputedLayoutProperties;
import org.jfree.layouting.renderer.model.page.LogicalPageBox;
import org.jfree.layouting.renderer.model.page.PageGrid;
import org.jfree.layouting.renderer.model.table.TableCellRenderBox;
import org.jfree.layouting.renderer.model.table.TableRenderBox;
import org.jfree.layouting.renderer.model.table.cols.TableColumn;
import org.jfree.layouting.renderer.model.table.cols.TableColumnModel;
import org.jfree.layouting.renderer.process.layoutrules.EndSequenceElement;
import org.jfree.layouting.renderer.process.layoutrules.InlineBoxSequenceElement;
import org.jfree.layouting.renderer.process.layoutrules.InlineNodeSequenceElement;
import org.jfree.layouting.renderer.process.layoutrules.InlineSequenceElement;
import org.jfree.layouting.renderer.process.layoutrules.ReplacedContentSequenceElement;
import org.jfree.layouting.renderer.process.layoutrules.SpacerSequenceElement;
import org.jfree.layouting.renderer.process.layoutrules.StartSequenceElement;
import org.jfree.layouting.renderer.process.layoutrules.TextSequenceElement;
import org.pentaho.reporting.libraries.base.util.DebugLog;

/**
 * This process-step computes the effective layout, but it does not take
 * horizontal pagebreaks into account. (It has to deal with vertical breaks, as
 * they affect the text layout.)
 * <p/>
 * This processing step does not ajust anything on the vertical axis. Vertical
 * alignment is handled in a second step.
 *
 * @author Thomas Morgner
 */
public class InfiniteMinorAxisLayoutStep
    extends IterateVisualProcessStep
{
  private ParagraphBreakState breakState;
  private PageGrid pageGrid;
  private RenderBox continuedElement;

  public InfiniteMinorAxisLayoutStep()
  {
  }

  public void compute(final LogicalPageBox root)
  {
    breakState = null;
    pageGrid = root.getPageGrid();
    startProcessing(root);
    pageGrid = null;
    breakState = null;
  }

  /**
   * Continues processing. The renderbox must have a valid x-layout (that is: X,
   * content-X1, content-X2 and Width)
   *
   * @param parent
   * @param box
   */
  public void continueComputation(final PageGrid pageGrid,
                                  final RenderBox box)
  {
    if (box.getContentAreaX2() == 0 || box.getWidth() == 0)
    {
      throw new IllegalStateException("Box must be layouted a bit ..");
    }

    this.pageGrid = pageGrid;
    this.breakState = null;
    this.continuedElement = box;
    startProcessing(box);
    this.continuedElement = null;
    this.pageGrid = null;
    this.breakState = null;
  }

  /**
   * The whole computation is only done for exactly one nesting level of
   * paragraphs. If we encounter an inline-block or inline-table, we handle them
   * as a single element.
   *
   * @param box
   * @return
   */
  protected boolean startBlockLevelBox(final RenderBox box)
  {
    // first, compute the position. The position is global, not relative to a
    // parent or so. Therefore a child has no connection to the parent's
    // effective position, when it is painted.

    if (breakState == null)
    {

      if (box instanceof ParagraphRenderBox)
      {
        computeContentArea(box);

        final ParagraphRenderBox paragraphBox = (ParagraphRenderBox) box;

        if (continuedElement == null)
        {
          final BlockRenderBox lineboxContainer = paragraphBox.getLineboxContainer();
          final boolean unchanged =
              lineboxContainer.getChangeTracker() == paragraphBox.getMinorLayoutAge();
          if (unchanged)
          {
            return false;
          }
        }

        paragraphBox.clearLayout();
        breakState = new ParagraphBreakState(paragraphBox);
      }
      else if (box instanceof TableCellRenderBox)
      {
        // todo: Cannot be done unless the inner paragraphs have been layouted once ..
        computeCellArea((TableCellRenderBox) box);
      }
      else
      {
        computeContentArea(box);
        if (box instanceof TableRenderBox)
        {
          computeCellPositions((TableRenderBox) box);
        }
      }
      return true;
    }


    if (breakState.isSuspended() == false)
    {
      // The break-state exists only while we are inside of an paragraph
      // and suspend can only happen on inline elements.
      // A block-element inside a paragraph cannot be (and if it does, it is
      // a bug)
      throw new IllegalStateException("This cannot be.");
    }

    // this way or another - we are suspended now. So there is no need to look
    // at the children anymore ..
    return false;
  }

  protected void finishBlockLevelBox(final RenderBox box)
  {
    // Todo: maybe it would be very wise if we dont extend the box later on.
    // Heck, lets see whether this causes great pain ...
    verifyContentWidth(box);

    if (breakState != null)
    {
      final Object suspender = breakState.getSuspendItem();
      if (box.getInstanceId() == suspender)
      {
        breakState.setSuspendItem(null);
        return;
      }
      if (suspender != null)
      {
        return;
      }

      if (box instanceof ParagraphRenderBox)
      {
        // finally update the change tracker ..
        final ParagraphRenderBox paraBox = (ParagraphRenderBox) box;
        paraBox.setMinorLayoutAge(paraBox.getLineboxContainer().getChangeTracker());

        breakState = null;
      }
    }

  }


  private void computeCellPositions(final TableRenderBox tableBox)
  {
    // Todo: Compute the effective cell positions ...
    // In a perfect world, we would start to take pagebreaks into account
    // and would try to distribute the table columns in a way, so that they
    // do not cross inner page boundaries.

    // But for the first release, our world is not perfect!
    final TableColumnModel model = tableBox.getColumnModel();
    long position = 0;
    final int columnCount = model.getColumnCount();
    for (int i = 0; i < columnCount; i++)
    {
      final TableColumn column = model.getColumn(i);
      column.setEffectiveCellPosition(position);
      position += column.getEffectiveSize() + model.getBorderSpacing();
    }
  }

  private void computeCellArea(final TableCellRenderBox cellRenderBox)
  {
    // This is slightly different for table cells ...
    final int columnIndex = cellRenderBox.getColumnIndex();
    final TableRenderBox table = cellRenderBox.getTable();
    final TableColumnModel columnModel = table.getColumnModel();
    final TableColumn column = columnModel.getColumn(columnIndex);
    long effectiveSize = column.getEffectiveSize();

    final int colSpan = cellRenderBox.getColSpan();
    if (colSpan > 1)
    {
      for (int i = 1; i < colSpan; i++)
      {
        final TableColumn spannedColumn = columnModel.getColumn(columnIndex + i);
        effectiveSize += spannedColumn.getEffectiveSize();
        effectiveSize += columnModel.getBorderSpacing();
      }
    }

    final long effectiveCellPosition = column.getEffectiveCellPosition();
    cellRenderBox.setX(effectiveCellPosition);
    cellRenderBox.setWidth(effectiveSize);

    final ComputedLayoutProperties blp = cellRenderBox.getComputedLayoutProperties();
    // next, compute the width ...

    long leftPadding = blp.getBorderLeft();
    leftPadding += blp.getPaddingLeft();
    cellRenderBox.setContentAreaX1(effectiveCellPosition + leftPadding);

    long rightPadding = blp.getBorderRight();
    rightPadding += blp.getPaddingRight();
    final long contentWidth = effectiveSize - rightPadding;
    cellRenderBox.setContentAreaX2(effectiveCellPosition + contentWidth);
  }


  /**
   * Computes the effective content area. The content area is the space that can
   * be used by any of the childs of the given box.
   * <p/>
   * InlineBoxes get computed in the alignment processor.
   *
   * @param box the block render box for which we compute the content area
   */
  private void computeContentArea(final RenderBox box)
  {
    if (box == continuedElement)
    {
      return;
    }

    final ComputedLayoutProperties blp = box.getComputedLayoutProperties();
    final long x = computeX(box) + blp.getMarginLeft();
    box.setX(x);
    // next, compute the width ...

    long leftPadding = blp.getBorderLeft();
    leftPadding += blp.getPaddingLeft();
    box.setContentAreaX1(x + leftPadding);

    final RenderLength computedWidth =
        box.getComputedLayoutProperties().getComputedWidth();
    if (computedWidth == RenderLength.AUTO)
    {
      final RenderBox parent = box.getParent();
      if (parent instanceof BlockRenderBox)
      {
        long rightPadding = blp.getMarginRight();
        rightPadding += blp.getBorderRight();
        rightPadding += blp.getPaddingRight();

        final BlockRenderBox blockParent = (BlockRenderBox) parent;
        box.setContentAreaX2(blockParent.getContentAreaX2() - rightPadding);
      }
      else
      {
        // A block level element that sits inside an inline element
        box.setContentAreaX2(x + leftPadding + box.getMinimumChunkWidth());
      }
    }
    else
    {
      final long contentWidth = computedWidth.resolve(0);
      box.setContentAreaX2(x + leftPadding + contentWidth);
    }
  }

  private long computeX(final RenderNode node)
  {
    final RenderBox parent = node.getParent();
    if (parent == null)
    {
      return 0;
    }

    return parent.getContentAreaX1();
  }

  /**
   * Verifies the content width and produces the effective box width.
   *
   * @param box
   */
  private void verifyContentWidth(final RenderBox box)
  {
    final long x = box.getX();
    final long contentEnd = box.getContentAreaX2();
    final ComputedLayoutProperties blp = box.getComputedLayoutProperties();
    final long boxEnd = contentEnd + blp.getBorderRight() + blp.getPaddingRight();
    box.setWidth(boxEnd - x);
  }

  protected boolean startInlineLevelBox(final RenderBox box)
  {
    if (breakState == null)
    {
      // ignore .. should not happen anyway ..
      return true;
    }

    if (breakState.isSuspended())
    {
      return false;
    }

    if (box instanceof InlineRenderBox)
    {
      breakState.add(new StartSequenceElement((InlineRenderBox) box));
      return true;
    }

    computeContentArea(box);

    breakState.add(new InlineBoxSequenceElement(box));
    breakState.setSuspendItem(box.getInstanceId());
    return false;
  }

  protected void finishInlineLevelBox(final RenderBox box)
  {
    if (breakState == null)
    {
      return;
    }
    if (breakState.getSuspendItem() == box.getInstanceId())
    {
      // stop being suspended.
      breakState.setSuspendItem(null);
      return;
    }

    if (box instanceof InlineRenderBox)
    {
      breakState.add(new EndSequenceElement((InlineRenderBox) box));
      return;
    }

    final Object suspender = breakState.getSuspendItem();
    if (box.getInstanceId() == suspender)
    {
      breakState.setSuspendItem(null);
      return;
    }

    if (suspender != null)
    {
      return;
    }

    if (box instanceof ParagraphRenderBox)
    {
      throw new IllegalStateException("This cannot be.");
    }

  }

  protected void processInlineLevelNode(final RenderNode node)
  {
    if (breakState == null || breakState.isSuspended())
    {
      return;
    }

    if (node instanceof FinishedRenderNode)
    {
      final FinishedRenderNode finNode = (FinishedRenderNode) node;
      node.setWidth(finNode.getLayoutedWidth());
    }

    if (node instanceof RenderableText)
    {
      breakState.add(new TextSequenceElement((RenderableText) node));
    }
    else if (node instanceof RenderableReplacedContent)
    {
      breakState.add
          (new ReplacedContentSequenceElement((RenderableReplacedContent) node));
    }

    else if (node instanceof PlaceholderRenderNode)
    {
      breakState.add(new InlineNodeSequenceElement(node));
    }
    else if (node instanceof SpacerRenderNode)
    {
      if (breakState.isContainsContent())
      {
        breakState.add(new SpacerSequenceElement((SpacerRenderNode) node));
      }
    }
    else
    {
      breakState.add(new InlineNodeSequenceElement(node));
    }
  }

  protected void processBlockLevelNode(final RenderNode node)
  {
    // This could be anything, text, or an image.
    final long x = computeX(node);
    node.setX(x);
    if (node instanceof FinishedRenderNode)
    {
      final FinishedRenderNode finNode = (FinishedRenderNode) node;
      node.setWidth(finNode.getLayoutedWidth());
    }
    else
    {
      node.setWidth
          (node.getComputedLayoutProperties().getComputedWidth().resolve(0));
    }
  }

  protected void processParagraphChilds(final ParagraphRenderBox box)
  {
    final BlockRenderBox lineboxContainer = box.getLineboxContainer();
    RenderNode node = lineboxContainer.getVisibleFirst();
    while (node != null)
    {
      // all childs of the linebox container must be inline boxes. They
      // represent the lines in the paragraph. Any other element here is
      // a error that must be reported
      if (node instanceof ParagraphPoolBox == false)
      {
        throw new IllegalStateException("Expected ParagraphPoolBox elements.");
      }
      final InlineRenderBox inlineRenderBox = (InlineRenderBox) node;
      startLine(inlineRenderBox);
      processBoxChilds(inlineRenderBox);
      finishLine(inlineRenderBox);

      node = node.getVisibleNext();
    }

  }

  protected void startLine(final InlineRenderBox inlineRenderBox)
  {
    if (breakState == null)
    {
      return;
    }

    if (breakState.isSuspended())
    {
      return;
    }

    breakState.clear();
    breakState.add(new StartSequenceElement(inlineRenderBox));
  }

  protected void finishLine(final InlineRenderBox inlineRenderBox)
  {
    if (breakState == null || breakState.isSuspended())
    {
      return;
    }

    breakState.add(new EndSequenceElement(inlineRenderBox));

    final ParagraphRenderBox paragraph = breakState.getParagraph();
    final CSSValue lastLineAlignment = paragraph.getLastLineAlignment();
    final CSSValue textAlignment = paragraph.getTextAlignment();
    // Todo: Start the layouting on primary sequence ...

    // This aligns all direct childs. Once that is finished, we have to
    // check, whether possibly existing inner-paragraphs are still valid
    // or whether moving them violated any of the inner-pagebreak constraints.
    final TextAlignmentProcessor processor = create(textAlignment, lastLineAlignment);
    final InlineSequenceElement[] sequence = breakState.getSequence();

    if (sequence.length == 0)
    {
      return;
    }

    final long lineStart = paragraph.getContentAreaX1();
    final long lineEnd = paragraph.getContentAreaX2();
    if (lineEnd - lineStart <= 0)
    {
      final long minimumChunkWidth = paragraph.getMinimumChunkWidth();
      processor.initialize(sequence,
          lineStart, lineStart + minimumChunkWidth, pageGrid);
      DebugLog.log("Auto-Corrected zero-width linebox.");
    }
    else
    {
      processor.initialize(sequence, lineStart, lineEnd, pageGrid);
    }

    while (processor.hasNext())
    {
      final RenderNode linebox = processor.next();
      if (linebox instanceof ParagraphPoolBox == false)
      {
        throw new NullPointerException("Line must not be null");
      }

      paragraph.addGeneratedChild(linebox);
    }
  }

  protected TextAlignmentProcessor create(final CSSValue alignment,
                                          final CSSValue lastLine)
  {
    if (TextAlign.CENTER.equals(alignment))
    {
      return new CenterAlignmentProcessor();
    }
    else if (TextAlign.RIGHT.equals(alignment))
    {
      return new RightAlignmentProcessor();
    }
    return new LeftAlignmentProcessor();
  }

}
