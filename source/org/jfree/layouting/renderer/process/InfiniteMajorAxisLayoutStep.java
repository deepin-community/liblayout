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
 * $Id: InfiniteMajorAxisLayoutStep.java 6489 2008-11-28 14:53:40Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.renderer.process;

import org.jfree.layouting.renderer.border.RenderLength;
import org.jfree.layouting.renderer.model.BlockRenderBox;
import org.jfree.layouting.renderer.model.ComputedLayoutProperties;
import org.jfree.layouting.renderer.model.FinishedRenderNode;
import org.jfree.layouting.renderer.model.InlineRenderBox;
import org.jfree.layouting.renderer.model.ParagraphPoolBox;
import org.jfree.layouting.renderer.model.ParagraphRenderBox;
import org.jfree.layouting.renderer.model.RenderBox;
import org.jfree.layouting.renderer.model.RenderNode;
import org.jfree.layouting.renderer.model.RenderableReplacedContent;
import org.jfree.layouting.renderer.model.RenderableText;
import org.jfree.layouting.renderer.model.SpacerRenderNode;
import org.jfree.layouting.renderer.model.page.LogicalPageBox;
import org.jfree.layouting.renderer.model.table.TableRowRenderBox;
import org.jfree.layouting.renderer.process.valign.BoxAlignContext;
import org.jfree.layouting.renderer.process.valign.InlineBlockAlignContext;
import org.jfree.layouting.renderer.process.valign.NodeAlignContext;
import org.jfree.layouting.renderer.process.valign.ReplacedContentAlignContext;
import org.jfree.layouting.renderer.process.valign.TextElementAlignContext;
import org.jfree.layouting.renderer.process.valign.VerticalAlignmentProcessor;
import org.pentaho.reporting.libraries.base.util.FastStack;

/**
 * This process-step computes the vertical alignment and corrects the
 * y-positions whereever needed.
 * <p/>
 * This will only work, if the minor-axis step has been executed.
 * Executing this class eats 23% of the current layouting time.
 *
 * @author Thomas Morgner
 */
public class InfiniteMajorAxisLayoutStep
    extends IterateVisualProcessStep
{
  public static class ParagraphBreakState
  {
    private Object suspendItem;
    private FastStack contexts;
    private ParagraphRenderBox paragraph;

    public ParagraphBreakState(final ParagraphRenderBox paragraph)
    {
      if (paragraph == null)
      {
        throw new NullPointerException();
      }
      this.paragraph = paragraph;
      this.contexts = new FastStack();
    }

    public ParagraphRenderBox getParagraph()
    {
      return paragraph;
    }

    public Object getSuspendItem()
    {
      return suspendItem;
    }

    public void setSuspendItem(final Object suspendItem)
    {
      this.suspendItem = suspendItem;
    }

    public boolean isSuspended()
    {
      return suspendItem != null;
    }

    public BoxAlignContext getCurrentLine()
    {
      if (contexts.isEmpty())
      {
        return null;
      }
      return (BoxAlignContext) contexts.peek();
    }

    public void openContext (final BoxAlignContext context)
    {
      if (contexts.isEmpty() == false)
      {
        final BoxAlignContext boxAlignContext =
            (BoxAlignContext) contexts.peek();
        boxAlignContext.addChild(context);
      }
      contexts.push (context);
    }

    public BoxAlignContext closeContext()
    {
      return (BoxAlignContext) contexts.pop();
    }
  }


  private ParagraphBreakState breakState;
  private RenderBox continuedElement;

  public InfiniteMajorAxisLayoutStep()
  {
  }

  public void compute(final LogicalPageBox pageBox)
  {
    this.breakState = null;
    this.continuedElement = null;
    startProcessing(pageBox);
    this.breakState = null;
    this.continuedElement = null;
  }

  /**
   * Continues processing. The renderbox must have a valid x-layout (that is: X,
   * content-X1, content-X2 and Width)
   *
   * @param box
   */
  public void continueComputation(final RenderBox box)
  {
    if (box.getContentAreaX2() == 0 || box.getWidth() == 0)
    {
      throw new IllegalStateException("Box must be layouted a bit ..");
    }

    this.breakState = null;
    this.continuedElement = box;
    startProcessing(box);
    this.continuedElement = null;
  }

  protected boolean startBlockLevelBox(final RenderBox box)
  {
    if (box.isIgnorableForRendering())
    {
      return false;
    }

    // first, compute the position. The position is global, not relative to a
    // parent or so. Therefore a child has no connection to the parent's
    // effective position, when it is painted.

    if (box != continuedElement)
    {
      computeYPosition(box);

      // We have an valid y position now
      // we do not recompute the y-position of the continued element yet as we
      // would not have all context-information here (and it had been done in
      // the calling step anyway)
    }

    if (breakState == null)
    {
      if (box instanceof ParagraphRenderBox)
      {
        final ParagraphRenderBox paragraphBox = (ParagraphRenderBox) box;
        // We cant cache that ... the shift operations later would misbehave
        // One way around would be to at least store the layouted offsets
        // (which should be immutable as long as the line did not change its
        // contents) and to reapply them on each run. This is cheaper than
        // having to compute the whole v-align for the whole line.
        breakState = new ParagraphBreakState(paragraphBox);
      }

      return true;
    }

    // No breakstate and not being suspended? Why this?
    if (breakState.isSuspended() == false)
    {
      throw new IllegalStateException("This cannot be.");
    }

    // this way or another - we are suspended now. So there is no need to look
    // at the children anymore ..
    return false;
  }

  private void computeYPosition(final RenderNode node)
  {
    final long marginTop = node.getEffectiveMarginTop();

    // The y-position of a box depends on the parent.
    final RenderBox parent = node.getParent();

    // A table row is something special. Although it is a block box,
    // it layouts its children from left to right
    if (parent instanceof TableRowRenderBox)
    {
      // Node is a table-cell ..
      node.setDirty(true);
      node.setY(parent.getY());
    }
    // If the box's parent is a block box ..
    else if (parent instanceof BlockRenderBox)
    {
      final RenderNode prev = node.getVisiblePrev();
      if (prev != null)
      {
        // we have a silbling. Position yourself directly below your silbling ..
        node.setDirty(true);
        node.setY(marginTop + prev.getY() + prev.getHeight());
      }
      else
      {
        final ComputedLayoutProperties blp = parent.getComputedLayoutProperties();
        final long insetTop = (blp.getBorderTop() + blp.getPaddingTop());

        node.setDirty(true);
        node.setY(marginTop + insetTop + parent.getY());
      }
    }
    // The parent is a inline box.
    else if (parent != null)
    {
      final ComputedLayoutProperties blp = parent.getComputedLayoutProperties();
      final long insetTop = (blp.getBorderTop() + blp.getPaddingTop());

      node.setDirty(true);
      node.setY(marginTop + insetTop + parent.getY());
    }
    else
    {
      // there's no parent ..
      node.setDirty(true);
      node.setY(marginTop);
    }
  }

  protected void finishBlockLevelBox(final RenderBox box)
  {
    // Check the height. Set the height.
    final ComputedLayoutProperties clp = box.getComputedLayoutProperties();
    final RenderLength computedWidth = clp.getComputedWidth();
    final RenderLength preferredHeight = box.getBoxDefinition().getPreferredHeight();
    final long computedHeight =
        preferredHeight.resolve(computedWidth.resolve(0));

    final ComputedLayoutProperties blp = box.getComputedLayoutProperties();
    final long insetBottom = blp.getBorderBottom() + blp.getPaddingBottom();

    final RenderNode lastChildNode = box.getLastChild();
    if (lastChildNode != null)
    {
      // grab the node's y2
      final long childY2 = lastChildNode.getY() + lastChildNode.getHeight() +
          lastChildNode.getEffectiveMarginBottom();
      final long effectiveHeight = (childY2 - box.getY()) + insetBottom;
      final long height = Math.max(effectiveHeight, computedHeight);
      box.setHeight(height);
    }
    else
    {
      final long insetTop = blp.getBorderTop() + blp.getBorderTop();
      box.setHeight(Math.max(computedHeight, insetTop + insetBottom));
    }

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
        paraBox.setMajorLayoutAge(paraBox.getLineboxContainer().getChangeTracker());

        breakState = null;
      }
    }

  }

  protected boolean startInlineLevelBox(final RenderBox box)
  {
    // todo: Inline level boxes may have margins ...
    computeYPosition(box);
    computeBaselineInfo(box);

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
      breakState.openContext(new BoxAlignContext(box));
      return true;
    }

    breakState.getCurrentLine().addChild(new InlineBlockAlignContext(box));
    breakState.setSuspendItem(box.getInstanceId());
    return false;
  }

  private void computeBaselineInfo(final RenderBox box)
  {
    RenderNode node = box.getVisibleFirst();
    while (node != null)
    {
      if (node instanceof RenderableText)
      {
        // grab the baseline info from there ...
        final RenderableText text = (RenderableText) node;
        box.setBaselineInfo(text.getBaselineInfo());
        break;
      }

      node = node.getVisibleNext();
    }

    if (box.getBaselineInfo() == null)
    {
      // If we have no baseline info here, ask the parent. If that one has none
      // either, then we cant do anything about it.
      box.setBaselineInfo(box.getNominalBaselineInfo());
    }
  }


  protected void finishInlineLevelBox(final RenderBox box)
  {
    if (breakState == null)
    {
      return;
    }

    if (box instanceof InlineRenderBox)
    {
      breakState.closeContext();
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
    computeYPosition(node);

    if (breakState == null || breakState.isSuspended())
    {
      return;
    }

    if (node instanceof RenderableText)
    {
      breakState.getCurrentLine().addChild
          (new TextElementAlignContext((RenderableText) node));
    }
    else if (node instanceof RenderableReplacedContent)
    {
      breakState.getCurrentLine().addChild
          (new ReplacedContentAlignContext((RenderableReplacedContent) node));
    }
    else if (node instanceof SpacerRenderNode)
    {
      breakState.getCurrentLine().addChild(new NodeAlignContext(node));
    }
    else
    {
      breakState.getCurrentLine().addChild(new NodeAlignContext(node));
    }
  }

  protected void processBlockLevelNode(final RenderNode node)
  {
    // This could be anything, text, or an image.
    computeYPosition(node);

    if (node instanceof FinishedRenderNode)
    {
      final FinishedRenderNode fnode = (FinishedRenderNode) node;
      node.setHeight(fnode.getLayoutedHeight());
    }
    // Tables can have spacer nodes in weird positions. Actually it is less
    // expensive to filter them here than to kill them earlier.
    // Heck, given infinite time and resources, I will filter them earlier ..
//    throw new IllegalStateException
//        ("Block Level nodes are somewhat illegal: " + node);
//    node.setHeight(node.getNodeLayoutProperties().getMaximumBoxHeight());
  }

  protected void processParagraphChilds(final ParagraphRenderBox box)
  {
    // Process the direct childs of the paragraph
    // Each direct child represents a line ..

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
      startLine(inlineRenderBox);
      processBoxChilds(inlineRenderBox);
      finishLine(inlineRenderBox);

      node = node.getVisibleNext();
    }

  }

  protected void startLine(final ParagraphPoolBox box)
  {
    computeYPosition(box);

    if (breakState == null)
    {
      return;
    }

    if (breakState.isSuspended())
    {
      return;
    }

    breakState.openContext(new BoxAlignContext(box));
  }

  protected void finishLine(final ParagraphPoolBox inlineRenderBox)
  {
    if (breakState == null || breakState.isSuspended())
    {
      return;
    }

    final BoxAlignContext boxAlignContext = breakState.closeContext();


    // This aligns all direct childs. Once that is finished, we have to
    // check, whether possibly existing inner-paragraphs are still valid
    // or whether moving them violated any of the inner-pagebreak constraints.
    final VerticalAlignmentProcessor processor = new VerticalAlignmentProcessor();

    final ComputedLayoutProperties blp = inlineRenderBox.getComputedLayoutProperties();
    final long insetTop = (blp.getBorderTop() + blp.getPaddingTop());

    final long contentAreaY1 = inlineRenderBox.getY() + insetTop;
    final RenderLength lineHeight = inlineRenderBox.getLineHeight();
    final ComputedLayoutProperties clp = inlineRenderBox.getComputedLayoutProperties();
    final RenderLength bcw = clp.getBlockContextWidth();
    processor.align (boxAlignContext, contentAreaY1,
        lineHeight.resolve(bcw.resolve(0)));
  }

  protected void finishOtherBox(final RenderBox box)
  {
    if (breakState != null)
    {
      final Object suspender = breakState.getSuspendItem();
      if (box.getInstanceId() == suspender)
      {
        breakState.setSuspendItem(null);
      }
    }
  }
}
