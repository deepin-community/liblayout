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
 * $Id: EmptyModelBuilder.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.normalizer.displaymodel;

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
 * Creation-Date: 31.05.2006, 16:26:04
 *
 * @author Thomas Morgner
 */
public class EmptyModelBuilder implements ModelBuilder
{
  private static class EmptyModelBuilderState implements State
  {
    private EmptyModelBuilderState()
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
     * @throws StateException
     */
    public StatefullComponent restore(final LayoutProcess layoutProcess)
            throws StateException
    {
      return new EmptyModelBuilder();
    }
  }

  public EmptyModelBuilder()
  {
  }

  public void startDocument(final PageContext pageContext)
  {

  }

  public void startElement(final LayoutContext layoutContext)
          throws NormalizationException
  {

  }

  public void addContent(final ContentToken content)
          throws NormalizationException
  {

  }

  public void endDocument()
  {

  }

  public void endElement()
  {

  }

  public void handlePageBreak(final PageContext pageContext)
  {

  }

  public State saveState() throws StateException
  {
    return new EmptyModelBuilderState();
  }

  public Renderer getRenderer()
  {
    throw new UnsupportedOperationException();
  }
}
