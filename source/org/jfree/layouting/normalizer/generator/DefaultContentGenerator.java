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
 * $Id: DefaultContentGenerator.java 6489 2008-11-28 14:53:40Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.normalizer.generator;

import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.State;
import org.jfree.layouting.StateException;
import org.jfree.layouting.StatefullComponent;
import org.jfree.layouting.layouter.content.ContentToken;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.layouter.context.PageContext;
import org.jfree.layouting.normalizer.content.NormalizationException;
import org.jfree.layouting.renderer.Renderer;

/**
 * The default content generator produces CSS-compliant content structures (also
 * known as DOM-Trees). This model explicitly allows block elements to contain
 * other block elements and to do all kinds of weird mixing of elements.
 * <p/>
 * The content generator already has all knowledge to compute the manual
 * pagebreaks. So this class could already fire pagebreaks. The next element in
 * the layouting chain will be the Layouter itself. The renderer computes the
 * element positions,knows about the page sizes (and whether we have pages
 * anyway) and more important - keeps track of the current page and the page
 * fill state.
 * <p/>
 * The content generator is conceptionally a part of the normalizer step, and
 * thererfore has access to the raw display model. The renderer receives a more
 * generic view with no object hierarchies at all - if that whould be needed, it
 * has to be rebuilt. (This simplifies serialization, so that most remote calls
 * are now self-contained and do not supply a possibly contradictionary context
 * (in terms of object-identity)).
 *
 * @author Thomas Morgner
 */
public class DefaultContentGenerator implements ContentGenerator
{
  private static class DefaultContentGeneratorState implements State
  {
    private State rendererState;

    private DefaultContentGeneratorState()
    {
    }

    public State getRendererState()
    {
      return rendererState;
    }

    public void setRendererState(final State rendererState)
    {
      this.rendererState = rendererState;
    }

    /**
     * Creates a restored instance of the saved component.
     * <p/>
     * By using this factory-like approach, we gain independence from having to
     * know the actual implementation. This makes things a lot easier.
     *
     * @param layoutProcess the layout process that controls it all
     * @return the saved state
     * @throws StateException
     */
    public StatefullComponent restore(final LayoutProcess layoutProcess)
            throws StateException
    {
      return new DefaultContentGenerator
              ((Renderer) rendererState.restore(layoutProcess));
    }
  }

  private Renderer renderer;

  public DefaultContentGenerator(final LayoutProcess layoutProcess)
  {
    if (layoutProcess == null)
    {
      throw new NullPointerException();
    }
    this.renderer = layoutProcess.getOutputProcessor().createRenderer(layoutProcess);
  }

  protected DefaultContentGenerator(final Renderer renderer)
  {
    this.renderer = renderer;
  }

  /**
   * Receives the information, that the document processing has been started.
   * This is fired only once.
   */
  public void startedDocument(final PageContext pageContext)
  {
    renderer.startedDocument(pageContext);
  }

  public void startedFlow(final LayoutContext element)
          throws NormalizationException
  {
    renderer.startedFlow(element);
  }

  public void startedTable(final LayoutContext element)
          throws NormalizationException
  {
    renderer.startedTable(element);
  }

  public void startedTableColumnGroup(final LayoutContext element)
          throws NormalizationException
  {
    renderer.startedTableColumnGroup(element);
  }

  public void startedTableColumn(final LayoutContext element)
          throws NormalizationException
  {
    renderer.startedTableColumn(element);
  }

  public void startedTableSection(final LayoutContext element)
          throws NormalizationException
  {
    renderer.startedTableSection(element);
  }

  public void startedTableRow(final LayoutContext element)
          throws NormalizationException
  {
    renderer.startedTableRow(element);
  }

  public void startedTableCell(final LayoutContext element)
          throws NormalizationException
  {
    renderer.startedTableCell(element);
  }

  public void startedBlock(final LayoutContext element)
          throws NormalizationException
  {
    renderer.startedBlock(element);
  }

  public void startedMarker(final LayoutContext element)
          throws NormalizationException
  {
    renderer.startedMarker(element);
  }

  public void startedRootInline(final LayoutContext element)
          throws NormalizationException
  {
    renderer.startedRootInline(element);
  }

  public void startedInline(final LayoutContext element) throws
          NormalizationException
  {
    renderer.startedInline(element);
  }

  public void addContent(final LayoutContext node, final ContentToken contentToken) throws
          NormalizationException
  {
    renderer.addContent(node, contentToken);
  }

  public void finishedInline() throws NormalizationException
  {
    renderer.finishedInline();
  }

  public void finishedMarker() throws NormalizationException
  {
    renderer.finishedMarker();
  }

  public void finishedRootInline() throws NormalizationException
  {
    renderer.finishedRootInline();
  }

  public void finishedBlock() throws NormalizationException
  {
    renderer.finishedBlock();
  }

  public void finishedTableCell() throws NormalizationException
  {
    renderer.finishedTableCell();
  }

  public void finishedTableRow() throws NormalizationException
  {
    renderer.finishedTableRow();
  }

  public void finishedTableSection() throws NormalizationException
  {
    renderer.finishedTableSection();
  }

  public void finishedTableColumn() throws NormalizationException
  {
    renderer.finishedTableColumn();
  }

  public void finishedTableColumnGroup() throws NormalizationException
  {
    renderer.finishedTableColumnGroup();
  }

  public void finishedTable() throws NormalizationException
  {
    renderer.finishedTable();
  }

  public void finishedFlow() throws NormalizationException
  {
    renderer.finishedFlow();
  }

  /**
   * Receives notification, that a new flow has started. A new flow is started
   * for each flowing or absolutly positioned element.
   *
   * @param box
   */
  public void finishedDocument() throws NormalizationException
  {
    renderer.finishedDocument();
  }

  public void handlePageBreak(final PageContext pageContext)
  {
    renderer.handlePageBreak(pageContext);
  }

  public void startedPassThrough(final LayoutContext element)
      throws NormalizationException
  {
    renderer.startedPassThrough (element);
  }

  public void addPassThroughContent(final LayoutContext node,
                                    final ContentToken token)
      throws NormalizationException
  {
    renderer.addPassThroughContent(node, token);
  }

  public void finishedPassThrough() throws NormalizationException
  {
    renderer.finishedPassThrough();
  }

  public State saveState() throws StateException
  {
    final DefaultContentGeneratorState state = new DefaultContentGeneratorState();
    state.setRendererState(renderer.saveState());
    return state;
  }

  public void startedTableCaption(final LayoutContext context)
      throws NormalizationException
  {
    renderer.startedTableCaption(context);
  }

  public void finishedTableCaption() throws NormalizationException
  {
    renderer.finishedTableCaption();
  }

  public Renderer getRenderer()
  {
    return renderer;
  }
}
