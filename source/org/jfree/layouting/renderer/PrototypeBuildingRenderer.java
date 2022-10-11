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
 * $Id: PrototypeBuildingRenderer.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.layouting.renderer;

import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.State;
import org.jfree.layouting.StateException;
import org.jfree.layouting.StatefullComponent;
import org.jfree.layouting.layouter.content.ContentToken;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.layouter.context.PageContext;
import org.jfree.layouting.normalizer.content.NormalizationException;

/**
 * This renderer does not build real output. This renderer fills in
 * prototype element definitions and is later used to spit them out
 * when requested. Prototypes are filled in in the first stage of the
 * renderering and are reused in all subsequent stages.
 *
 * This class needs to be implemented later.
 * @see http://www.w3.org/TR/css3-gcpm/#generated
 * @author Thomas Morgner
 */
public class PrototypeBuildingRenderer implements Renderer
{
  private static class PrototypeBuildingRendererState implements State
  {
    private PrototypeBuildingRendererState()
    {

    }

    /**
     * Creates a restored instance of the saved component.
     * <p/>
     * By using this factory-like approach, we gain independence from having to
     * know the actual implementation. This makes things a lot easier.
     *
     * @param layoutProcess the layout process that controls it all
     * @return the saved state
     * @throws org.jfree.layouting.StateException
     *
     */
    public StatefullComponent restore(final LayoutProcess layoutProcess)
        throws StateException
    {
      return new PrototypeBuildingRenderer(layoutProcess);
    }
  }

  private LayoutProcess layoutProcess;

  public PrototypeBuildingRenderer(final LayoutProcess layoutProcess)
  {
    this.layoutProcess = layoutProcess;
  }

  /**
   * Starts the document and initalizes the default page context.
   *
   * @param pageContext
   */
  public void startedDocument(final PageContext pageContext)
  {

  }

  /**
   * Starts a floating, absolute or static element. This establishes a new
   * normal flow for the element.
   *
   * @param context
   */
  public void startedFlow(final LayoutContext context)
      throws NormalizationException
  {

  }

  public void startedTable(final LayoutContext layoutContext)
      throws NormalizationException
  {

  }

  public void startedTableColumnGroup(final LayoutContext context)
      throws NormalizationException
  {

  }

  public void startedTableColumn(final LayoutContext context)
      throws NormalizationException
  {

  }

  public void startedTableSection(final LayoutContext layoutContext)
      throws NormalizationException
  {

  }

  public void startedTableRow(final LayoutContext layoutContext)
      throws NormalizationException
  {

  }

  public void startedTableCell(final LayoutContext layoutContext)
      throws NormalizationException
  {

  }

  public void startedBlock(final LayoutContext context)
      throws NormalizationException
  {

  }

  public void startedMarker(final LayoutContext context)
      throws NormalizationException
  {

  }

  public void startedRootInline(final LayoutContext context)
      throws NormalizationException
  {

  }

  public void startedInline(final LayoutContext context)
      throws NormalizationException
  {

  }

  public void addContent(final LayoutContext context,
                         final ContentToken content)
      throws NormalizationException
  {

  }

  public void finishedInline() throws NormalizationException
  {

  }

  public void finishedRootInline() throws NormalizationException
  {

  }

  public void finishedMarker() throws NormalizationException
  {

  }

  public void finishedBlock() throws NormalizationException
  {

  }

  public void finishedTableCell() throws NormalizationException
  {

  }

  public void finishedTableRow() throws NormalizationException
  {

  }

  public void finishedTableSection() throws NormalizationException
  {

  }

  public void finishedTableColumnGroup() throws NormalizationException
  {

  }

  public void finishedTableColumn() throws NormalizationException
  {

  }

  public void finishedTable() throws NormalizationException
  {

  }

  public void finishedFlow() throws NormalizationException
  {

  }

  public void finishedDocument() throws NormalizationException
  {
    layoutProcess.getOutputProcessor().processingFinished();
  }

  /**
   * A call-back that informs the renderer, that a new page must be started.
   * This closes the old page context and copies all pending content to the new
   * context.
   * <p/>
   * This method is triggered by a call to 'LayoutProcess.pageBreakEncountered'
   *
   * @param pageContext
   */
  public void handlePageBreak(final PageContext pageContext)
  {

  }

  public void startedPassThrough(final LayoutContext context)
  {

  }

  public void addPassThroughContent(final LayoutContext context,
                                    final ContentToken content)
  {

  }

  public void finishedPassThrough()
  {

  }

  public State saveState() throws StateException
  {
    return new PrototypeBuildingRendererState();
  }

  public void startedTableCaption(final LayoutContext context)
      throws NormalizationException
  {

  }

  public void finishedTableCaption() throws NormalizationException
  {

  }
}
