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
 * $Id: LeftAlignmentProcessor.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.renderer.process;

import org.jfree.layouting.renderer.model.RenderBox;
import org.jfree.layouting.renderer.model.RenderNode;
import org.jfree.layouting.renderer.process.layoutrules.EndSequenceElement;
import org.jfree.layouting.renderer.process.layoutrules.InlineNodeSequenceElement;
import org.jfree.layouting.renderer.process.layoutrules.InlineSequenceElement;
import org.jfree.layouting.renderer.process.layoutrules.StartSequenceElement;
import org.jfree.layouting.renderer.process.layoutrules.TextSequenceElement;

/**
 * Performs the left-alignment computations.
 * <p/>
 * The inf-min-step creates the initial sequence of elements. The alignment
 * processor now iterates over the sequence and produces the layouted line.
 * <p/>
 * Elements can be split, splitting is a local operation and does not copy the
 * children. Text splitting may produce a totally different text (see: TeX
 * hyphenation system).
 * <p/>
 * The process is iterative and continues unless all elements have been
 * consumed.
 *
 * @author Thomas Morgner
 */
public class LeftAlignmentProcessor extends AbstractAlignmentProcessor
{
  private long position;
  private int pageSegment;

  public LeftAlignmentProcessor()
  {
  }

  public int getPageSegment()
  {
    return pageSegment;
  }

  public void setPageSegment(final int pageSegment)
  {
    this.pageSegment = pageSegment;
  }

  protected long getPosition()
  {
    return position;
  }

  protected void setPosition(final long position)
  {
    this.position = position;
  }

  protected void addPosition(final long width)
  {
    this.position += width;
  }

  public RenderNode next()
  {
    position = getStartOfLine();
    pageSegment = 0;

    final RenderNode retval = super.next();

    position = 0;
    pageSegment = 0;

    return retval;
  }

  /**
   * Handle the next input chunk.
   *
   * @param start the start index
   * @param count the number of elements in the sequence
   * @return true, if processing should be finished, false if more elements are
   *         needed for the line.
   */
  protected int handleElement(final int start, final int count)
  {
    final InlineSequenceElement[] sequenceElements = getSequenceElements();
    final long[] elementDimensions = getElementDimensions();
    final long[] elementPositions = getElementPositions();

    long width = 0;
    final int endIndex = start + count;

    // In the given range, there should be only one content element.
    InlineSequenceElement contentElement = null;
    int contentIndex = start;
    for (int i = start; i < endIndex; i++)
    {
      final InlineSequenceElement element = sequenceElements[i];
      width += element.getMaximumWidth();
      if (element instanceof StartSequenceElement ||
          element instanceof EndSequenceElement)
      {
        continue;
      }

      contentElement = element;
      contentIndex = i;
    }

    final long nextPosition = getPosition() + width;

    final long pagebreak = getPageBreak(getPageSegment());
    // Do we cross a page boundary?
    if (nextPosition > pagebreak)
    {
      // todo: deal with the case where we have inner pagebreaks.
      // check whether the current break is an inner or outer break.
      // On inner break: Move element to new segment and continue processing

      // On outer break: Stop processing

      // Dont write through to the stored position; but prepare if
      // we have to fallback ..
      long position = getPosition();
      for (int i = start; i < endIndex; i++)
      {
        final InlineSequenceElement element = sequenceElements[i];
        elementPositions[i] = position;
        final long elementWidth = element.getMaximumWidth();
        elementDimensions[i] = elementWidth;
        position += elementWidth;
      }

      // Yes, we cross a pagebreak. Stop working on it - we bail out here.

      if (contentElement instanceof TextSequenceElement)
      {
        // the element may be splittable. Test, and if so, give a hint to the
        // outside world ..
        setSkipIndex(endIndex);
        setBreakableIndex(contentIndex);
        return (start);
      }
      else
      {
        // This is the first element and it still does not fit. How evil.
        if (start == 0)
        {
          if (contentElement instanceof InlineNodeSequenceElement)
          {
            final RenderNode node = contentElement.getNode();
            if (node instanceof RenderBox)
            {
              // OK, limit the size of the box to the maximum line width and
              // revalidate it.
              final RenderBox box = (RenderBox) node;
              final long maxWidth = (getEndOfLine() - getPosition());
              computeInlineBlock(box, getPosition(), maxWidth);

              elementDimensions[endIndex - 1] = node.getWidth();
            }
          }
          setSkipIndex(endIndex);
        }
        return(start);
      }

    }

    // No, it is an ordinary advance ..
    // Check, whether we hit an item-sequence element
    if (contentElement instanceof InlineNodeSequenceElement == false)
    {
      for (int i = start; i < endIndex; i++)
      {
        final InlineSequenceElement element = sequenceElements[i];
        elementPositions[i] = getPosition();
        final long elementWidth = element.getMaximumWidth();
        elementDimensions[i] = elementWidth;
        addPosition(elementWidth);
      }
      return endIndex;
    }

    // Handle the ItemSequence element.


    // This is a bit more complicated. So we encountered an inline-block
    // element here. That means, the element will try to occuppy its
    // maximum-content-width.
//    Log.debug("Advance block at index " + contentIndex);
//    final long ceWidth = contentElement.getMinimumWidth();
//    final long extraSpace = contentElement.getMaximumWidth();
//    Log.debug("Advance block: Min " + ceWidth);
//    Log.debug("Advance block: Max " + extraSpace);

    final long itemElementWidth = contentElement.getMaximumWidth();

    final RenderNode node = contentElement.getNode();
    if (node instanceof RenderBox)
    {
      final RenderBox box = (RenderBox) node;
      computeInlineBlock(box, getPosition(), itemElementWidth);
    }
    else
    {
      node.setX(getPosition());
      node.setWidth(itemElementWidth);
    }

    final long preferredEndingPos = getPosition() + itemElementWidth;
    if (preferredEndingPos > getEndOfLine())
    {
      // We would eat the whole space up to the end of the line and more
      // So lets move that element to the next line instead...

      // But: We could easily end in an endless loop here. So check whether
      // the element is the first in the line
      if (start == 0)
      {
        // As it is guaranteed, that each chunk contains at least one item,
        // checking for start == 0 is safe enough ..
        return endIndex;
      }

      return start;
    }

    for (int i = start; i < contentIndex; i++)
    {
      final InlineSequenceElement element = sequenceElements[i];
      final long elementWidth = element.getMaximumWidth();
      elementPositions[i] = getPosition();
      elementDimensions[i] = elementWidth;
      addPosition(elementWidth);
    }

    elementPositions[contentIndex] = getPosition();
    elementDimensions[contentIndex] = itemElementWidth;
    setPosition(preferredEndingPos);

    for (int i = contentIndex + 1; i < endIndex; i++)
    {
      final InlineSequenceElement element = sequenceElements[i];
      final long elementWidth = element.getMaximumWidth();
      elementPositions[i] = getPosition();
      elementDimensions[i] = elementWidth;
      addPosition(elementWidth);
    }

    return endIndex;
  }

  protected int handleLayout(final int start,
                             final int count,
                             final int contentIndex,
                             final long usedWidth)
  {
    // not used ..
    return 0;
  }
}
