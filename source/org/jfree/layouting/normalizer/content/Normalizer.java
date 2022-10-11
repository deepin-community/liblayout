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
 * $Id: Normalizer.java 2739 2007-04-02 11:41:22Z taqua $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.normalizer.content;

import java.io.IOException;

import org.jfree.layouting.StatefullComponent;
import org.jfree.layouting.input.style.PseudoPage;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.layouter.style.resolver.StyleResolver;
import org.jfree.layouting.renderer.Renderer;
import org.jfree.layouting.util.AttributeMap;

/**
 * The normalizer is the first stage of the content processing. A normalizer
 * is responsible for auto-generating content and for assigning styles to the
 * logical document tree.
 *
 * @author Thomas Morgner
 */
public interface Normalizer extends StatefullComponent
{
  /**
   * Start document is the first call to the normalizer. At this point, all
   * meta-data has been given and the document context is filled correctly.
   *
   * Starting the document also starts a new PageContext.
   *
   * @throws NormalizationException
   * @throws IOException
   */
  public void startDocument ()
          throws NormalizationException, IOException;

  /**
   * Starts a new element. The element uses the given namespace and tagname.
   * The element's attributes are given as collection, each attribute is keyed
   * with a namespace and attributename. The values contained in the attributes
   * are not defined.
   *
   * @param namespace
   * @param tag
   * @param attributes
   * @throws NormalizationException
   * @throws IOException
   */
  public void startElement (String namespace,
                            String tag,
                            AttributeMap attributes)
          throws NormalizationException, IOException;

  /**
   * Adds text content to the current element.
   *
   * @param text
   * @throws NormalizationException
   * @throws IOException
   */
  public void addText (String text)
          throws NormalizationException, IOException;

  /**
   * Ends the current element. The namespace and tagname are given for
   * convienience.
   *
   * @param namespace
   * @param tag
   * @throws NormalizationException
   * @throws IOException
   */
  public void endElement ()
          throws NormalizationException, IOException;

  /**
   * Ends the document. No other events will be fired against this normalizer
   * once this method has been called.
   *
   * @throws NormalizationException
   * @throws IOException
   */
  public void endDocument ()
          throws NormalizationException, IOException;

  public void handlePageBreak(final CSSValue pageName,
                              final PseudoPage[] pseudoPages)
          throws NormalizationException;

  /**
   * Returns the renderer. The renderer is the last step in the processing chain.
   * The ModelBuilder and ContentGenerator steps are considered internal, as
   * they may refeed the normalizer.
   *
   * @return
   */
  public Renderer getRenderer();

//  public StyleResolver getStyleResolver();
}
