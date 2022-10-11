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
 * $Id: ParagraphLineBreakStep.java 6489 2008-11-28 14:53:40Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.renderer.process;

import java.util.Stack;

import org.jfree.layouting.renderer.model.BlockRenderBox;
import org.jfree.layouting.renderer.model.InlineRenderBox;
import org.jfree.layouting.renderer.model.ParagraphRenderBox;
import org.jfree.layouting.renderer.model.RenderBox;
import org.jfree.layouting.renderer.model.RenderNode;
import org.jfree.layouting.renderer.model.RenderableText;
import org.jfree.layouting.renderer.model.page.LogicalPageBox;
import org.pentaho.reporting.libraries.base.util.FastStack;

/**
 * This static computation step performs manual linebreaks on all paragraphs.
 * This transforms the pool-collection into the lines-collection.
 * <p/>
 * For now, we follow a very simple path: A paragraph cannot be validated, if it
 * is not yet closed. The linebreaking, be it the static one here or the dynamic
 * one later, must be redone when the paragraph changes.
 * <p/>
 * Splitting for linebreaks happens only between inline-boxes. BlockBoxes that
 * are contained in inline-boxes (like 'inline-block' elements or
 * 'inline-tables') are considered unbreakable according to the CSS specs.
 * Linebreaking can be suspended in these cases.
 * <p/>
 * As paragraphs itself are block elements, the linebreaks can be done
 * iterative, using a simple stack to store the context of possibly nested
 * paragraphs. The paragraph's pool contains the elements that should be
 * processed, and the line-container will receive the pool's content (contained
 * in an artificial inline element, as the linecontainer is a block-level
 * element).
 * <p/>
 * Change-tracking should take place on the paragraph's pool element instead of
 * the paragraph itself. This way, only structural changes are taken into
 * account.
 *
 * @author Thomas Morgner
 */
public class ParagraphLineBreakStep extends IterateStructuralProcessStep
{
  private static class ParagraphLineBreakState
  {
    private Object suspendItem;
    private BlockRenderBox lines;
    private RenderBox insertationPoint;
    private boolean breakRequested;

    private ParagraphLineBreakState(final ParagraphRenderBox paragraph,
                                   final boolean readOnly)
    {
      if (paragraph == null)
      {
        throw new NullPointerException();
      }
      this.lines = paragraph.getLineboxContainer();
      if (readOnly)
      {
        this.insertationPoint = null;
      }
      else
      {
        this.insertationPoint = (RenderBox)
                paragraph.getPool().deriveFrozen(false);

        // Just make sure that the container is really empty. I hate surprises...
        lines.clear();
        lines.addGeneratedChild(insertationPoint);
      }
    }

    public BlockRenderBox getLines()
    {
      return lines;
    }

    public Object getSuspendItem()
    {
      return suspendItem;
    }

    public void setSuspendItem(final Object suspendItem)
    {
      this.suspendItem = suspendItem;
    }

    public RenderBox getInsertationPoint()
    {
      return insertationPoint;
    }

    public boolean isDirty()
    {
      return insertationPoint != null;
    }

    public void setInsertationPoint(final RenderBox insertationPoint)
    {
      this.insertationPoint = insertationPoint;
    }

    public boolean isBreakRequested()
    {
      return breakRequested;
    }

    public void setBreakRequested(final boolean breakRequested)
    {
      this.breakRequested = breakRequested;
    }
  }

  private FastStack paragraphNesting;
  private ParagraphLineBreakState breakState;

  public ParagraphLineBreakStep()
  {
    paragraphNesting = new FastStack();
  }

  public void compute(final LogicalPageBox root)
  {
    startProcessing(root);
  }

  protected boolean startBlockBox(final BlockRenderBox box)
  {
    if (box instanceof ParagraphRenderBox)
    {
      ParagraphRenderBox paragraphBox = (ParagraphRenderBox) box;
      final boolean unchanged =
              paragraphBox.getPool().getChangeTracker() == paragraphBox.getLineBoxAge();

      if (unchanged)
      {
        final ParagraphRenderBox derivedParagraph = (ParagraphRenderBox) box.derive(true);
        breakState.getInsertationPoint().addGeneratedChild(derivedParagraph);
        final ParagraphLineBreakState item = new ParagraphLineBreakState(derivedParagraph, true);
        paragraphNesting.push(item);
        breakState = item;
        return false;
      }

      if (breakState != null)
      {
        if (breakState.isDirty() == false)
        {
          // OK, should not happen, but you never know. I'm good at hiding
         // bugs in the code ..
          throw new IllegalStateException
                  ("A child cannot be dirty, if the parent is clean");
        }
        // The paragraph is somehow nested in an other paragraph.
        final RenderBox child = (RenderBox) box.deriveFrozen(false);
        breakState.getInsertationPoint().addGeneratedChild(child);
        //breakState.setInsertationPoint(child);
        paragraphBox = (ParagraphRenderBox) child;
      }

      final ParagraphLineBreakState item = new ParagraphLineBreakState(paragraphBox, false);
      paragraphNesting.push(item);
      breakState = item;
    }
    else
    {
      // some other block box .. suspend.
      if (breakState != null)
      {
        if (breakState.isDirty())
        {
          final RenderBox child = (RenderBox) box.deriveFrozen(false);
          breakState.getInsertationPoint().addGeneratedChild(child);
          breakState.setInsertationPoint(child);

          if (breakState.getSuspendItem() == null)
          {
            breakState.setSuspendItem(box.getInstanceId());
          }
        }
      }
    }
    return true;
  }

  protected void finishBlockBox(final BlockRenderBox box)
  {
    if (box instanceof ParagraphRenderBox)
    {
      // do the linebreak jiggle ...
      // This is the first test case whether it is possible to avoid
      // composition-recursion on such computations. I'd prefer to have
      // an iterator pattern here ...

      // finally update the change tracker ..
      final ParagraphRenderBox paraBox = (ParagraphRenderBox) box;
      paraBox.setLineBoxAge(paraBox.getPool().getChangeTracker());

      paragraphNesting.pop();
      if (paragraphNesting.isEmpty())
      {
        breakState = null;
      }
      else
      {
        breakState = (ParagraphLineBreakState) paragraphNesting.peek();
      }

    }
    else
    {
      if (breakState != null)
      {
        if (breakState.isDirty())
        {
          final RenderBox parent = breakState.getInsertationPoint().getParent();
          breakState.setInsertationPoint(parent);

          final Object suspender = breakState.getSuspendItem();
          if (box.getInstanceId() == suspender)
          {
            breakState.setSuspendItem(null);
          }
        }
      }
    }
  }

  protected boolean startInlineBox(final InlineRenderBox box)
  {
    if (breakState == null || breakState.isDirty() == false)
    {
      return true;
    }

    final RenderBox child = (RenderBox) box.deriveFrozen(false);
    breakState.getInsertationPoint().addGeneratedChild(child);
    breakState.setInsertationPoint(child);
    return true;
  }

  protected void finishInlineBox(final InlineRenderBox box)
  {
    if (breakState == null || breakState.isDirty() == false)
    {
      return;
    }

    final RenderBox parent = breakState.getInsertationPoint().getParent();
    breakState.setInsertationPoint(parent);

    if (breakState.isBreakRequested() && box.getNext() != null)
    {
      performBreak();
    }
  }

  protected void startOtherNode(final RenderNode node)
  {
    if (breakState == null || breakState.isDirty() == false)
    {
      return;
    }
    if (breakState.getSuspendItem() != null ||
        node instanceof RenderableText == false)
    {
      final RenderNode child = node.deriveFrozen(false);
      breakState.getInsertationPoint().addGeneratedChild(child);
      return;
    }

    final RenderableText text = (RenderableText) node;
    final RenderNode child = text.deriveFrozen(false);
    breakState.getInsertationPoint().addGeneratedChild(child);

    if (text.isForceLinebreak() == false)
    {
      return;
    }

    // OK, someone requested a manual linebreak.
    // Fill a stack with the current context ..
    // Check if we are at the end of the line
    if (node.getNext() == null)
    {
      boolean endOfLine = true;
      RenderBox parent = node.getParent();
      while (parent != null)
      {
        if (parent instanceof InlineRenderBox == false)
        {
          break;
        }
        if (parent.getNext() != null)
        {
          endOfLine = false;
          break;
        }
        parent = parent.getParent();
      }

      // OK, if we are at the end of the line (for all contexts), so we
      // dont have to perform a break. The text will end anyway ..
      if (endOfLine)
      {
        return;
      }
      else
      {
        // as soon as we are no longer the last element - break!
        // According to the flow rules, that will happen in one of the next
        // finishInlineBox events ..
        breakState.setBreakRequested(true);
        return;
      }
    }

    performBreak();
  }

  private void performBreak()
  {
    // If we come that far, it means, we have a forced linebreak and we
    // are a node in the middle of the tree ..
    final Stack contexts = new Stack();

    // perform a simple split
    // as long as the splitted element is at the end of it's box, it is not
    // needed to split the box at all. Just let it end naturally is enough for
    // them to look good.

    // As the real context (from the break-State) is currently being built,
    // we have to use the original pool to query the 'is-end-of-line' flag.
    RenderBox context = breakState.getInsertationPoint();
    final BlockRenderBox lines = breakState.getLines();
    while (context != lines)
    {
      // save the context ..
      if (context instanceof InlineRenderBox == false)
      {
        throw new IllegalStateException
                ("Confused: I expect InlineBoxes ..");
      }

      final InlineRenderBox inline = (InlineRenderBox) context;
      contexts.push(inline.split(RenderNode.HORIZONTAL_AXIS));
      context = context.getParent();
    }

    // reset to a known state and add all saved contexts ..
    breakState.setInsertationPoint(lines);
    while (contexts.isEmpty() == false)
    {
      final RenderBox box = (RenderBox) contexts.pop();
      breakState.getInsertationPoint().addGeneratedChild(box);
      breakState.setInsertationPoint(box);
    }

    breakState.setBreakRequested(false);
  }

  protected boolean startOtherBox(final RenderBox box)
  {
    if (breakState == null)
    {
      return false;
    }

    if (breakState.isDirty() == false)
    {
      return false;
    }

    // some other block box .. suspend.
    if (breakState.getSuspendItem() == null)
    {
      breakState.setSuspendItem(box.getInstanceId());
    }

    final RenderBox child = (RenderBox) box.deriveFrozen(false);
    breakState.getInsertationPoint().addGeneratedChild(child);
    breakState.setInsertationPoint(child);

    return true;
  }

  protected void finishOtherBox(final RenderBox box)
  {
    if (breakState != null && breakState.isDirty())
    {
      final Object suspender = breakState.getSuspendItem();
      if (box.getInstanceId() == suspender)
      {
        breakState.setSuspendItem(null);
      }

      final RenderBox parent = breakState.getInsertationPoint().getParent();
      breakState.setInsertationPoint(parent);
    }
  }
}
