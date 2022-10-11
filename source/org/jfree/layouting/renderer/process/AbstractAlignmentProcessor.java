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
 * $Id: AbstractAlignmentProcessor.java 6489 2008-11-28 14:53:40Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.renderer.process;

import java.util.ArrayList;
import java.util.Arrays;

import org.jfree.layouting.renderer.ModelPrinter;
import org.jfree.layouting.renderer.model.ComputedLayoutProperties;
import org.jfree.layouting.renderer.model.InlineRenderBox;
import org.jfree.layouting.renderer.model.RenderBox;
import org.jfree.layouting.renderer.model.RenderNode;
import org.jfree.layouting.renderer.model.page.PageGrid;
import org.jfree.layouting.renderer.model.page.PhysicalPageBox;
import org.jfree.layouting.renderer.process.layoutrules.EndSequenceElement;
import org.jfree.layouting.renderer.process.layoutrules.InlineSequenceElement;
import org.jfree.layouting.renderer.process.layoutrules.StartSequenceElement;
import org.jfree.layouting.util.LongList;
import org.pentaho.reporting.libraries.base.util.FastStack;

/**
 * Todo: The whole horizontal alignment is not suitable for spanned page
 * breaks.
 *
 * @author Thomas Morgner
 */
public abstract class AbstractAlignmentProcessor
    implements TextAlignmentProcessor
{
  private static final int START = 0;
  private static final int CONTENT = 1;
  private static final int END = 2;

  private long startOfLine;
  private long endOfLine;
  private long[] pagebreaks;
  private PageGrid pageGrid;

  private InlineSequenceElement[] sequenceElements;
  private int sequenceFill;

  /**
   * A layouter hint, that indicates a possibly breakable element
   */
  private int breakableIndex;
  /**
   * A layouter hint, that indicates where to continue on unbreakable elements.
   */
  private int skipIndex;

  private long[] elementPositions;
  private long[] elementDimensions;

  public AbstractAlignmentProcessor()
  {
  }

  public long getStartOfLine()
  {
    return startOfLine;
  }

  public PageGrid getPageGrid()
  {
    return pageGrid;
  }

  protected InlineSequenceElement[] getSequenceElements()
  {
    return sequenceElements;
  }

  public long[] getElementPositions()
  {
    return elementPositions;
  }

  public long[] getElementDimensions()
  {
    return elementDimensions;
  }

  public long getEndOfLine()
  {
    return endOfLine;
  }

  protected long getPageBreak(final int pageIndex)
  {
    return pagebreaks[pageIndex];
  }

  public int getBreakableIndex()
  {
    return breakableIndex;
  }

  public void setBreakableIndex(final int breakableIndex)
  {
    this.breakableIndex = breakableIndex;
  }

  public int getSkipIndex()
  {
    return skipIndex;
  }

  public void setSkipIndex(final int skipIndex)
  {
    this.skipIndex = skipIndex;
  }

  public int iterate(final InlineSequenceElement[] elements, final int maxPos)
  {
    breakableIndex = -1;
    skipIndex = -1;
    // The state transitions are as follows:
    // ......From....START...CONTENT...END
    // to...START....-.......X.........X
    // ...CONTENT....-.......X.........X
    // .......END....-.......-.........-
    //
    // Dash signals, that there is no break opportunity,
    // while X means, that it is possible to break the inline flow at that
    // position.

    if (maxPos == 0)
    {
      // nothing to do ..
      return 0;
    }


    int lastElementType = classifyInput(elements[0]);
    int startIndex = 0;
    for (int i = 1; i < maxPos; i++)
    {
      final InlineSequenceElement element = elements[i];
      final int elementType = classifyInput(element);
      if (elementType == END)
      {
        lastElementType = elementType;
        continue;
      }

      if (lastElementType == START)
      {
        lastElementType = elementType;
        continue;
      }

      final int newIndex = handleElement(startIndex, i - startIndex);
      if (newIndex <= startIndex)
      {
        return startIndex;
      }

      startIndex = i;
      lastElementType = elementType;
    }

    return handleElement(startIndex, maxPos - startIndex);
  }


  /**
   * Initializes the alignment process. The start and end parameters specify the
   * line boundaries, and have been precomputed.
   *
   * @param sequence
   * @param start
   * @param end
   * @param breaks
   */
  public void initialize(final InlineSequenceElement[] sequence,
                         final long start,
                         final long end,
                         final PageGrid breaks)
  {
    if (end < start)
    {
      // This is most certainly an error, treat it as such ..
      throw new IllegalArgumentException("Start is <= end; which is stupid!: " + end + ' ' + start);
    }

    this.sequenceElements = sequence;
    this.sequenceFill = sequence.length;
    this.startOfLine = start;
    this.endOfLine = end;
    this.pageGrid = breaks;
    elementPositions = new long[sequenceElements.length];
    elementDimensions = new long[sequenceElements.length];

    // to be computed by the pagegrid ..
    updateBreaks();
  }

  private void updateBreaks()
  {
    final int pageCnt = pageGrid.getColumnCount();
    final LongList pageLongList = new LongList(pageCnt);
    long pos = 0;
    for (int i = 0; i < pageCnt; i++)
    {
      if (pos < startOfLine)
      {
        // skip ..
        continue;
      }
      final PhysicalPageBox page = pageGrid.getPage(0, i);
      pos += page.getImageableWidth();
      if (pos >= endOfLine)
      {
        break;
      }
      pageLongList.add(pos);
    }
    pageLongList.add(endOfLine);

    this.pagebreaks = pageLongList.toArray();
  }

  public boolean hasNext()
  {
    return sequenceFill > 0;
  }

  public RenderNode next()
  {
    Arrays.fill(elementDimensions, 0);
    Arrays.fill(elementPositions, 0);

    int lastPosition = iterate(sequenceElements, sequenceFill);
    if (lastPosition == 0)
    {
      // This could evolve into an infinite loop. Thats evil.
      // We have two choices to prevent that:
      // (1) Try to break the element.
      if (getBreakableIndex() >= 0)
      {
        // Todo: Breaking is not yet implemented ..
      }
      if (getSkipIndex() >= 0)
      {
        // This causes an overflow ..
        lastPosition = getSkipIndex();
      }
      else
      {
        // Skip the complete line. Oh, thats not good, really!
        lastPosition = sequenceFill;
      }
    }

    // now, build the line and update the array ..
    final ArrayList pendingElements = new ArrayList();
    final FastStack contexts = new FastStack();
    RenderBox firstBox = null;
    RenderBox box = null;
    for (int i = 0; i < lastPosition; i++)
    {
      final InlineSequenceElement element = sequenceElements[i];
      if (element instanceof EndSequenceElement)
      {
        contexts.pop();
        final long boxX2 = (elementPositions[i] + elementDimensions[i]);
        //noinspection ConstantConditions
        box.setWidth(boxX2 - box.getX());

        if (contexts.isEmpty())
        {
          box = null;
        }
        else
        {
          final RenderNode node = box;
          box = (RenderBox) contexts.peek();
          box.addGeneratedChild(node);
        }
        continue;
      }

      if (element instanceof StartSequenceElement)
      {
        final RenderNode node = element.getNode();
        box = (RenderBox) node.derive(false);
        box.setX(elementPositions[i]);
        contexts.push(box);
        if (firstBox == null)
        {
          firstBox = box;
        }
        continue;
      }

      if (box == null)
      {
        throw new IllegalStateException("Invalid sequence: " +
            "Cannot have elements before we open the box context.");
      }

      // Content element: Perform a deep-derive, so that we preserve the
      // possibly existing sub-nodes.
      final RenderNode child = element.getNode().derive(true);
      child.setX(elementPositions[i]);
      child.setWidth(elementDimensions[i]);
      if (box.isPreserveSpace() != false)
      {
        box.addGeneratedChild(child);
        continue;
      }

      if (child.isIgnorableForRendering())
      {
        pendingElements.add(child);
      }
      else
      {
        for (int j = 0; j < pendingElements.size(); j++)
        {
          final RenderNode node = (RenderNode) pendingElements.get(j);
          box.addGeneratedChild(node);
        }
        pendingElements.clear();
        box.addGeneratedChild(child);
      }
    }

    // Remove all spacers and other non printable content that might
    // look ugly at the beginning of a new line ..
    for (; lastPosition < sequenceFill; lastPosition++)
    {
      final RenderNode node = sequenceElements[lastPosition].getNode();
      if (node.isDiscardable() == false)
      {
        break;
      }
    }

    // If there are open contexts, then add the split-result to the new line
    // and update the width of the current line
    final int openContexts = contexts.size();
    for (int i = 0; i < openContexts; i++)
    {
      final RenderBox renderBox = (RenderBox) contexts.get(i);
      renderBox.setWidth(getEndOfLine() - box.getX());

      final InlineRenderBox rightBox = (InlineRenderBox)
          renderBox.split(RenderNode.HORIZONTAL_AXIS);
      sequenceElements[i] = new StartSequenceElement(rightBox);
    }

    final int length = sequenceFill - lastPosition;
    System.arraycopy(sequenceElements, lastPosition,
        sequenceElements, openContexts, length);
    sequenceFill = openContexts + length;
    Arrays.fill(sequenceElements, sequenceFill, sequenceElements.length, null);

    return firstBox;
  }

  /**
   * Handle the next input chunk.
   *
   * @param start the start index
   * @param count the number of elements in the sequence
   * @return the processing position. Linebreaks will be inserted, if the
   *         returned value is equal or less the start index.
   */
  protected int handleElement(final int start, final int count)
  {
    final int endIndex = start + count;
    // always look at all elements.

    final InlineSequenceElement[] sequenceElements = getSequenceElements();

    // In the given range, there should be only one content element.
    int contentIndex = start;
    long width = 0;
    for (int i = 0; i < endIndex; i++)
    {
      final InlineSequenceElement element = sequenceElements[i];
      width += element.getMaximumWidth();
      if (element instanceof StartSequenceElement ||
          element instanceof EndSequenceElement)
      {
        continue;
      }

      contentIndex = i;
    }

    final long pagebreak = getPageBreak(0);
    // Do we cross a page boundary?
    if (width > pagebreak)
    {
      return start;
    }
    return handleLayout(start, count, contentIndex, width);
  }

  protected abstract int handleLayout(int start, int count, int contentIndex, long usedWidth);

  private int classifyInput(final InlineSequenceElement element)
  {
    if (element instanceof StartSequenceElement)
    {
      return START;
    }
    else if (element instanceof EndSequenceElement)
    {
      return END;
    }
    else
    {
      return CONTENT;
    }
  }


  protected void computeInlineBlock(final RenderBox box,
                                    final long position,
                                    final long itemElementWidth)
  {
    final ComputedLayoutProperties blp = box.getComputedLayoutProperties();
    box.setX(position + blp.getMarginLeft());
    final long width = itemElementWidth - blp.getMarginLeft() - blp.getMarginRight();
    if (width == 0)
    {
      ModelPrinter.printParents(box);

      throw new IllegalStateException("A box without any width? " +
          Integer.toHexString(System.identityHashCode(box)) + ' ' + box.getClass());
    }
    box.setWidth(width);

    final long leftInsets = blp.getPaddingLeft() + blp.getBorderLeft();
    final long rightInsets = blp.getPaddingRight() + blp.getBorderRight();
    box.setContentAreaX1(box.getX() + leftInsets);
    box.setContentAreaX2(box.getX() + box.getWidth() - rightInsets);

    final InfiniteMinorAxisLayoutStep layoutStep = new InfiniteMinorAxisLayoutStep();
    layoutStep.continueComputation(getPageGrid(), box);
  }

}
