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
 * $Id: ContentGenerator.java 2739 2007-04-02 11:41:22Z taqua $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.layouting.normalizer.generator;

import org.jfree.layouting.StatefullComponent;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.layouter.context.PageContext;
import org.jfree.layouting.layouter.content.ContentToken;
import org.jfree.layouting.normalizer.content.NormalizationException;
import org.jfree.layouting.renderer.Renderer;

/**
 * The content generator is the third stage content processor.
 * This part is responsible to process the generated display model
 * and to prepare it for rendering it to the output medium.
 * <p/>
 * In the simplest case, the content generator will be able to pass the
 * display model without any changes. In the more complicated cases, the
 * display model needs to be flattened (as text processors cannot contain
 * paragraphs inside other paragraphs, for instance) before it can be rendered.
 */
public interface ContentGenerator extends StatefullComponent
{
  /**
   * Receives the information, that the document processing has been started.
   * This is fired only once.
   *
   * @param pageContext the page context for the default page.
   */
  public void startedDocument(final PageContext pageContext)
          throws NormalizationException;

  public void startedPassThrough(final LayoutContext element)
          throws NormalizationException;

  public void addPassThroughContent(final LayoutContext node,
                                    final ContentToken token)
      throws NormalizationException;

  public void finishedPassThrough() throws NormalizationException;

  public void startedFlow(final LayoutContext element)
          throws NormalizationException;

  public void startedBlock(final LayoutContext element)
          throws NormalizationException;

  public void startedRootInline(final LayoutContext element)
          throws NormalizationException;

  public void startedTable (final LayoutContext element)
          throws NormalizationException;

  public void startedTableColumnGroup(final LayoutContext element)
          throws NormalizationException;

  public void startedTableColumn(final LayoutContext element)
          throws NormalizationException;

  public void startedTableSection (final LayoutContext element)
          throws NormalizationException;

  public void startedTableRow (final LayoutContext element)
          throws NormalizationException;

  public void startedTableCell (final LayoutContext element)
          throws NormalizationException;

  public void startedMarker(final LayoutContext element)
          throws NormalizationException;

  public void startedInline(final LayoutContext element)
          throws NormalizationException;

  public void addContent(final LayoutContext node, ContentToken contentToken)
          throws NormalizationException;

  public void finishedInline()
          throws NormalizationException;

  public void finishedMarker()
          throws NormalizationException;

  public void finishedTableCell()
          throws NormalizationException;

  public void finishedTableRow ()
          throws NormalizationException;

  public void finishedTableSection()
          throws NormalizationException;

  public void finishedTableColumn()
          throws NormalizationException;

  public void finishedTableColumnGroup()
          throws NormalizationException;

  public void finishedTable()
          throws NormalizationException;

  public void finishedRootInline()
          throws NormalizationException;

  public void finishedBlock ()
          throws NormalizationException;

  public void finishedFlow ()
          throws NormalizationException;

  /**
   * Receives notification, that a new flow has started. A new flow is started
   * for each flowing or absolutly positioned element.
   *
   * @param box
   */
  public void finishedDocument()
          throws NormalizationException;

  /**
   * This event handler is triggered by 'LayoutProcess.pageBreakEncountered()'.
   *
   * @param pageContext
   */
  public void handlePageBreak(final PageContext pageContext);

  public Renderer getRenderer();

  public void startedTableCaption(final LayoutContext context)
      throws NormalizationException;

  public void finishedTableCaption() throws NormalizationException;
}
