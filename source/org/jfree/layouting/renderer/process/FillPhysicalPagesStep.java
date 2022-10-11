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
 * $Id: FillPhysicalPagesStep.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.renderer.process;

import org.jfree.layouting.renderer.model.PageAreaRenderBox;
import org.jfree.layouting.renderer.model.ParagraphRenderBox;
import org.jfree.layouting.renderer.model.RenderBox;
import org.jfree.layouting.renderer.model.RenderNode;
import org.jfree.layouting.renderer.model.page.LogicalPageBox;

/**
 * This Step copies all content from the logical page into the page-grid. When
 * done, it clears the content and replaces the elements with dummy-nodes. These
 * nodes have a fixed-size (the last known layouted size), and will not be
 * recomputed later.
 * <p/>
 * Adjoining dummy-nodes get unified into a single node, thus simplifying and
 * pruning the document tree.
 *
 * @author Thomas Morgner
 */
public class FillPhysicalPagesStep extends IterateVisualProcessStep
{
  private long contentEnd;
  private long contentStart;

  public FillPhysicalPagesStep()
  {
  }

  public LogicalPageBox compute(final LogicalPageBox pagebox,
                                final long pageStart,
                                final long pageEnd)
  {
    this.contentStart = pagebox.getHeaderArea().getHeight();
    this.contentEnd = (pageEnd - pageStart) + contentStart;

    // This is a simpel strategy.
    // Copy and relocate, then prune. (I whished we could prune first, but
    // this does not work.)
    //
    // For the sake of efficiency, we do *not* create private copies for each
    // phyiscal page. This would be an total overkill.
    final LogicalPageBox derived = (LogicalPageBox) pagebox.derive(true);

    // first, shift the normal-flow content downwards.
    // The start of the logical pagebox might be in the negative range now
    // The header-size has already been taken into account by the pagination
    // step.
    final BoxShifter boxShifter = new BoxShifter();
    boxShifter.shiftBoxUnchecked(derived, -pageStart + contentStart);

    // now remove all the content that will not be visible at all ..
    if (startBlockLevelBox(derived))
    {
      // not processing the header and footer area: they are 'out-of-context' bands
      processBoxChilds(derived);
    }
    finishBlockLevelBox(derived);

    // Then add the header at the top - it starts at (0,0) and thus it is
    // ok to leave it unshifted.

    // finally, move the footer at the bottom (to the page's bottom, please!)
    final PageAreaRenderBox footerArea = derived.getFooterArea();
    final long footerPosition = pagebox.getPageHeight() -
        (footerArea.getY() + footerArea.getHeight());
    final long footerShift = footerPosition - footerArea.getY();
    boxShifter.shiftBoxUnchecked(footerArea, footerShift);

    // the renderer is responsible for painting the page-header and footer ..
    return derived;
  }

  protected void processParagraphChilds(final ParagraphRenderBox box)
  {
    processBoxChilds(box);
  }

  /**
   * Invisible nodes may need special treatment here.
   *
   * @param box
   * @return
   */
  protected boolean startBlockLevelBox(final RenderBox box)
  {
    RenderNode node = box.getFirstChild();
    while (node != null)
    {
      if (node.isIgnorableForRendering())
      {
        node = node.getNext();
      }
      if ((node.getY() + node.getHeight()) <= contentStart)
      {
        final RenderNode next = node.getNext();
        box.remove(node);
        node = next;
      }
      else if (node.getY() >= contentEnd)
      {
        final RenderNode next = node.getNext();
        box.remove(node);
        node = next;
      }
      else
      {
        node = node.getNext();
      }
    }
    return true;
  }
}
