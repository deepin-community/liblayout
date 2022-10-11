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
 * $Id: ModelBuilder.java 2739 2007-04-02 11:41:22Z taqua $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.normalizer.displaymodel;

import java.io.IOException;

import org.jfree.layouting.StatefullComponent;
import org.jfree.layouting.layouter.content.ContentToken;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.layouter.context.PageContext;
import org.jfree.layouting.normalizer.content.NormalizationException;
import org.jfree.layouting.renderer.Renderer;

/**
 * The model builder is the second stage in the layout process.
 *
 * The builder receives events from the Normalizer and builds a normalized
 * displayable logical model. Although still working on a logical document model,
 * this model is already bound to the constraints of the underlying renderer.
 *
 * Each generated element has information on which logical page it will be
 * rendered (by looking at the page style name); depending on the output target,
 * it might even know the physical page already (including the page number).
 * (The PageInformation is initiated by the Normalizer.)
 *
 * The ModelBuilder is responsible to manage the moved content.
 *
 * @author Thomas Morgner
 */
public interface ModelBuilder extends StatefullComponent
{
  public void startDocument(final PageContext pageContext)
          throws NormalizationException;

  public void startElement(final LayoutContext layoutContext)
          throws NormalizationException, IOException;

  public void addContent(final ContentToken content)
          throws NormalizationException;


  public void endElement() throws NormalizationException;

  public void endDocument() throws NormalizationException;

  public void handlePageBreak(final PageContext pageContext)
      throws NormalizationException;

  public Renderer getRenderer();
}
