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
 * $Id: PrintingRenderer.java 6489 2008-11-28 14:53:40Z tmorgner $
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
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

/**
 * Creation-Date: 17.07.2006, 17:43:21
 *
 * @author Thomas Morgner
 */
public class PrintingRenderer implements Renderer
{
  private static final Log logger = LogFactory.getLog(PrintingRenderer.class);
  private static class PrintingRendererState implements State
  {
    private State parentState;

    private PrintingRendererState(final Renderer parent) throws StateException
    {
      this.parentState = parent.saveState();
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
      return new PrintingRenderer((Renderer) parentState.restore(layoutProcess));
    }
  }

  private Renderer parent;

  public PrintingRenderer(final Renderer parent)
  {
    this.parent = parent;
  }

  /**
   * Starts the document and initalizes the default page context.
   *
   * @param pageContext
   */
  public void startedDocument(final PageContext pageContext)
  {
    logger.debug ("<document>");
    parent.startedDocument(pageContext);
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
    logger.debug ("<flow " +
        "tag='" + context.getTagName() + "' namespace='" + context.getNamespace() + "'>");
    parent.startedFlow(context);
  }

  public void startedTable(final LayoutContext context)
          throws NormalizationException
  {
    logger.debug ("<table " +
        "tag='" + context.getTagName() + "' namespace='" + context.getNamespace() + "'>");
    parent.startedTable(context);
  }

  public void startedTableSection(final LayoutContext layoutContext)
          throws NormalizationException
  {
    logger.debug ("<table-section " +
        "tag='" + layoutContext.getTagName() + "' namespace='" + layoutContext.getNamespace() + "'>");
    parent.startedTableSection(layoutContext);
  }

  public void startedTableRow(final LayoutContext layoutContext)
          throws NormalizationException
  {
    logger.debug ("<table-row " +
        "tag='" + layoutContext.getTagName() + "' namespace='" + layoutContext.getNamespace() + "'>");
    parent.startedTableRow(layoutContext);
  }

  public void startedTableCell(final LayoutContext layoutContext)
          throws NormalizationException
  {
    logger.debug ("<table-cell " +
        "tag='" + layoutContext.getTagName() + "' namespace='" + layoutContext.getNamespace() + "'>");
    parent.startedTableCell(layoutContext);
  }

  public void startedBlock(final LayoutContext context)
          throws NormalizationException
  {
    logger.debug ("<block " +
        "tag='" + context.getTagName() + "' namespace='" + context.getNamespace() + "'>");
    parent.startedBlock(context);
  }

  public void startedMarker(final LayoutContext context)
          throws NormalizationException
  {
    logger.debug ("<marker " +
        "tag='" + context.getTagName() + "' namespace='" + context.getNamespace() + "'>");
    parent.startedMarker(context);
  }

  public void startedRootInline(final LayoutContext context)
          throws NormalizationException
  {
    logger.debug ("<paragraph " +
        "tag='" + context.getTagName() + "' namespace='" + context.getNamespace() + "'>");
    parent.startedRootInline(context);
  }

  public void startedInline(final LayoutContext context)
          throws NormalizationException
  {
    logger.debug ("<inline " +
        "tag='" + context.getTagName() + "' namespace='" + context.getNamespace() + "'>");
    parent.startedInline(context);
  }

  public void addContent(final LayoutContext context,
                         final ContentToken content)
          throws NormalizationException
  {
    logger.debug ("<content>" + content + "</content>");
    parent.addContent(context, content);
  }

  public void finishedInline() throws NormalizationException
  {
    logger.debug ("</inline>");
    parent.finishedInline();
  }

  public void finishedRootInline() throws NormalizationException
  {
    logger.debug ("</paragraph>");
    parent.finishedRootInline();
  }

  public void finishedMarker() throws NormalizationException
  {
    logger.debug ("</marker>");
    parent.finishedMarker();
  }

  public void finishedBlock() throws NormalizationException
  {
    logger.debug ("</block>");
    parent.finishedBlock();
  }

  public void finishedTableCell() throws NormalizationException
  {
    logger.debug ("</table-cell>");
    parent.finishedTableCell();
  }

  public void finishedTableRow() throws NormalizationException
  {
    logger.debug ("</table-row>");
    parent.finishedTableRow();
  }

  public void finishedTableSection() throws NormalizationException
  {
    logger.debug ("</table-section>");
    parent.finishedTableSection();
  }

  public void finishedTable() throws NormalizationException
  {
    logger.debug ("</table>");
    parent.finishedTable();
  }

  public void finishedFlow() throws NormalizationException
  {
    logger.debug ("</flow>");
    parent.finishedFlow();
  }

  public void finishedDocument() throws NormalizationException
  {
    logger.debug ("</document>");
    parent.finishedDocument();
  }

  public void startedTableColumnGroup(final LayoutContext context)
          throws NormalizationException
  {
    logger.debug ("<table-column-group>");
    parent.startedTableColumnGroup(context);
  }

  public void startedTableColumn(final LayoutContext context)
          throws NormalizationException
  {
    logger.debug ("<table-column>");
    parent.startedTableColumn(context);
  }

  public void finishedTableColumnGroup() throws NormalizationException
  {
    logger.debug ("</table-column-group>");
    parent.finishedTableColumnGroup();
  }

  public void finishedTableColumn() throws NormalizationException
  {
    logger.debug ("</table-column>");
    parent.finishedTableColumn();
  }

  /**
   * A call-back that informs the renderer, that a new page must be started.
   * This closes the old page context and copies all pending content to the new
   * context.
   *
   * @param pageContext
   */
  public void handlePageBreak(final PageContext pageContext)
  {
    logger.debug ("<!-- PAGEBREAK ENCOUNTERED -->");
    parent.handlePageBreak(pageContext);
  }

  public void startedPassThrough(final LayoutContext context)
      throws NormalizationException
  {
    logger.debug ("<pass-through>");
    parent.startedPassThrough(context);
  }

  public void addPassThroughContent(final LayoutContext context,
                                    final ContentToken content)
      throws NormalizationException
  {
    logger.debug ("<pass-through-content>" + content + "</pass-through-content>");
    parent.addPassThroughContent(context, content);
  }

  public void finishedPassThrough() throws NormalizationException
  {
    logger.debug ("</pass-through>");
    parent.finishedPassThrough();
  }

  public State saveState() throws StateException
  {
    return new PrintingRendererState(parent);
  }

  public void startedTableCaption(final LayoutContext context)
      throws NormalizationException
  {
    logger.debug ("<table-caption>");
    parent.startedTableCaption(context);
  }

  public void finishedTableCaption() throws NormalizationException
  {
    logger.debug ("</table-caption>");
    parent.finishedTableCaption();
  }
}
