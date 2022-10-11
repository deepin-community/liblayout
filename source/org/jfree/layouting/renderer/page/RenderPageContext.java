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
 * $Id: RenderPageContext.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.renderer.page;

import java.util.Map;
import java.util.Iterator;

import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.layouter.context.PageContext;
import org.jfree.layouting.output.OutputProcessorMetaData;
import org.jfree.layouting.output.OutputProcessor;
import org.jfree.layouting.renderer.model.page.DefaultPageGrid;
import org.jfree.layouting.renderer.model.page.PageGrid;
import org.jfree.layouting.renderer.CounterStore;
import org.jfree.layouting.renderer.StringStore;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.StatefullComponent;
import org.jfree.layouting.State;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.StateException;

/**
 * This is a running page context, which contains a list of watched strings
 * and counters. Whenever one of these counters or strings change, a new
 * page context is generated.
 *
 * This page context also contains the special 'page' counter. That counter
 * is maintained by the system, initialized with '1' and is increased by
 * the number of physical pages at every physical split.
 *
 * @author Thomas Morgner
 */
public class RenderPageContext implements StatefullComponent
{
  private static class RenderPageContextState implements State
  {
    private CounterStore counterStore;
    private StringStore stringStore;
    private PageContext pageContext;

    private RenderPageContextState(final RenderPageContext context)
        throws StateException
    {
      try
      {
        this.pageContext = context.pageContext;
        this.counterStore = (CounterStore) context.counterStore.clone();
        this.stringStore = (StringStore) context.stringStore.clone();
      }
      catch (CloneNotSupportedException e)
      {
        throw new StateException("Clone failed.");
      }
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
      try
      {
        final RenderPageContext rpc = new RenderPageContext();
        rpc.pageContext = this.pageContext;
        rpc.counterStore = (CounterStore) this.counterStore.clone();
        rpc.stringStore = (StringStore) this.stringStore.clone();
        rpc.counterStore.add("pages", new Integer
            (layoutProcess.getOutputProcessor().getLogicalPageCount()));

        return rpc;
      }
      catch (CloneNotSupportedException e)
      {
        throw new StateException("Clone failed.");
      }
    }
  }

  private PageContext pageContext;

  private CounterStore counterStore;
  private StringStore stringStore;

  public RenderPageContext(final LayoutProcess layoutProcess,
                           final PageContext pageContext)
  {
    if (pageContext == null)
    {
      throw new NullPointerException();
    }
    if (layoutProcess == null)
    {
      throw new NullPointerException();
    }
    this.pageContext = pageContext;
    this.counterStore = new CounterStore();
    this.stringStore = new StringStore();

    this.counterStore.add("page", new Integer
        (layoutProcess.getOutputProcessor().getPageCursor() + 1));
    this.counterStore.add("pages", new Integer
        (layoutProcess.getOutputProcessor().getLogicalPageCount()));
  }

  protected RenderPageContext()
  {
  }

  public PageContext getPageContext()
  {
    return pageContext;
  }

  public RenderPageContext update(final PageContext pageContext,
                                  final OutputProcessor outputProcessor)
  {
    // this should preserve all recorded trails ..
    final RenderPageContext renderPageContext = new RenderPageContext();
    renderPageContext.pageContext = pageContext;
    renderPageContext.counterStore = (CounterStore) counterStore.derive();
    renderPageContext.stringStore = (StringStore) stringStore.derive();

    renderPageContext.counterStore.add("page", new Integer
        (outputProcessor.getPageCursor() + 1));
    return renderPageContext;
  }

  /**
   * This method should check the layout context for updated counters and strings.
   *
   * @param layoutContext
   * @return
   */
  public RenderPageContext update(final LayoutContext layoutContext)
  {
    final Map counters = layoutContext.getCounters();
    final Map strings = layoutContext.getStrings();

    final Iterator counterIt = counters.entrySet().iterator();
    while (counterIt.hasNext())
    {
      final Map.Entry entry = (Map.Entry) counterIt.next();
      final String name = (String) entry.getKey();
      final Integer value = (Integer) entry.getValue();
      counterStore.add(name, value);
    }

    final Iterator stringsIt = strings.entrySet().iterator();
    while (stringsIt.hasNext())
    {
      final Map.Entry entry = (Map.Entry) stringsIt.next();
      final String name = (String) entry.getKey();
      final String value = (String) entry.getValue();
      stringStore.add(name, value);
    }
    return this;
  }

  public PageGrid createPageGrid(final OutputProcessorMetaData outputMetaData)
  {
    return new DefaultPageGrid(pageContext, outputMetaData);
  }

  public String getString(final String name, final CSSValue pagePolicy)
  {
    return stringStore.get(name, pagePolicy);
  }

  public Integer getCounter(final String name, final CSSValue pagePolicy)
  {
    return counterStore.get(name, pagePolicy);
  }

  public Object clone()
  {
    try
    {
      final RenderPageContext pg =  (RenderPageContext) super.clone();
      pg.counterStore = (CounterStore) counterStore.clone();
      pg.stringStore = (StringStore) stringStore.clone();
      return pg;
    }
    catch(CloneNotSupportedException cne)
    {
      throw new IllegalStateException("Clone must be supported.");
    }
  }

  public State saveState() throws StateException
  {
    return new RenderPageContextState(this);
  }
}
