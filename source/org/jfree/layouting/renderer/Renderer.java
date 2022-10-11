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
 * $Id: Renderer.java 2739 2007-04-02 11:41:22Z taqua $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.renderer;

import org.jfree.layouting.StatefullComponent;
import org.jfree.layouting.layouter.content.ContentToken;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.layouter.context.PageContext;
import org.jfree.layouting.normalizer.content.NormalizationException;

/**
 * The renderer is the last step in the predefined processing chain. The
 * renderer computes the layout according to the style rules given in the
 * elements and the inherent limitations of the output targets.
 * <p/>
 * Renderers depend heavily on a correct input model - at this point no model
 * transformation should be done at all (except, maybe, inserting new lineboxes
 * where needed).
 * <p/>
 * Contents for the page or special areas (@footnote, @endnote) are forwarded
 * immediatly.
 *
 * @author Thomas Morgner
 */
public interface Renderer extends StatefullComponent
{
  /**
   * Starts the document and initalizes the default page context.
   *
   * @param pageContext
   */
  public void startedDocument(final PageContext pageContext);

  /**
   * Starts a floating, absolute or static element. This establishes a new
   * normal flow for the element.
   *
   * @param context
   */
  public void startedFlow(final LayoutContext context)
      throws NormalizationException;

  public void startedTable(final LayoutContext layoutContext)
      throws NormalizationException;

  public void startedTableColumnGroup(final LayoutContext context)
      throws NormalizationException;

  public void startedTableColumn(final LayoutContext context)
      throws NormalizationException;

  public void startedTableSection(final LayoutContext layoutContext)
      throws NormalizationException;

  public void startedTableRow(final LayoutContext layoutContext)
      throws NormalizationException;

  public void startedTableCell(final LayoutContext layoutContext)
      throws NormalizationException;

  public void startedBlock(final LayoutContext context)
      throws NormalizationException;

  public void startedMarker(final LayoutContext context)
      throws NormalizationException;

  public void startedRootInline(final LayoutContext context)
      throws NormalizationException;

  public void startedInline(final LayoutContext context)
      throws NormalizationException;

  public void addContent(final LayoutContext context,
                         final ContentToken content)
      throws NormalizationException;

  public void finishedInline()
      throws NormalizationException;

  public void finishedRootInline()
      throws NormalizationException;

  public void finishedMarker()
      throws NormalizationException;

  public void finishedBlock()
      throws NormalizationException;

  public void finishedTableCell()
      throws NormalizationException;

  public void finishedTableRow()
      throws NormalizationException;

  public void finishedTableSection()
      throws NormalizationException;

  public void finishedTableColumnGroup()
      throws NormalizationException;

  public void finishedTableColumn()
      throws NormalizationException;

  public void finishedTable()
      throws NormalizationException;

  public void finishedFlow()
      throws NormalizationException;

  public void finishedDocument()
      throws NormalizationException;

  /**
   * A call-back that informs the renderer, that a new page must be started.
   * This closes the old page context and copies all pending content to the new
   * context.
   * <p/>
   * This method is triggered by a call to 'LayoutProcess.pageBreakEncountered'
   *
   * @param pageContext
   */
  public void handlePageBreak(final PageContext pageContext);

  public void startedPassThrough(final LayoutContext context)
      throws NormalizationException;

  public void addPassThroughContent(final LayoutContext context,
                                    final ContentToken content)
      throws NormalizationException;

  public void finishedPassThrough()
      throws NormalizationException;

  public void startedTableCaption(LayoutContext context)
      throws NormalizationException;

  public void finishedTableCaption()
      throws NormalizationException;
}
