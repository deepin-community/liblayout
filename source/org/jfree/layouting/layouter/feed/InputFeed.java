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
 * $Id: InputFeed.java 2739 2007-04-02 11:41:22Z taqua $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.layouter.feed;

import org.jfree.layouting.StatefullComponent;
import org.jfree.layouting.input.style.PseudoPage;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.namespace.NamespaceCollection;
import org.jfree.layouting.normalizer.content.NormalizationException;
import org.jfree.layouting.normalizer.content.Normalizer;

/**
 * The input feed shields the internal processing from users errors. It
 * implements a state maschine, which checks that all documents are well formed
 * and which does not allow users to manipulate the resulting document tree
 * directly.
 * <p/>
 * An input feed collects all data for elements and other nodes and forwards
 * them to the normalizer. The normalizer is the first stage of the content
 * layouting and processing.
 * <p/>
 * Pagination ability is not propagated back to the caller. A caller will not
 * normaly know whether a certain input caused a pagebreak. However, especially
 * in the cases where only one page should be processed, we allow the detection
 * of page breaks using a boolean flag. (Which is also used to detect loops.)
 * <p/>
 * The flag is reset on each call to 'startElement', 'startDocument',
 * 'endElement', 'endDocument' and 'addText'. Attribute modifications have no
 * effect on that flag (as these calls are accumulated into one big supercall
 * before passing them to the normalizer.)
 * <p/>
 * Processing the meta-info also has no effect on the page-break flag, as meta-
 * info is processed before the content is processed.
 *
 * @author Thomas Morgner
 */
public interface InputFeed extends StatefullComponent
{
  /**
   * Starts the document processing. This is the first method to call. After
   * calling this method, the meta-data should be fed into the inputfeed.
   */
  public void startDocument() throws InputFeedException;

  /**
   * Signals, that meta-data follows. Calling this method is only valid directly
   * after startDocument has been called.
   */
  public void startMetaInfo() throws InputFeedException;

  /**
   * Adds document attributes. Document attributes hold object factories and
   * document wide resources which appear only once.
   *
   * @param name
   * @param attr
   */
  public void addDocumentAttribute(String name, Object attr)
          throws InputFeedException;

  /**
   * Starts a new meta-node structure. Meta-Nodes are used to hold content that
   * can appear more than once (like stylesheet declarations).
   * <p/>
   * For now, only stylesheet declarations are defined as meta-node content;
   * more content types will surely arise in the future.
   * <p/>
   * Calling this method is only valid after 'startMetaInfo' has been called.
   */
  public void startMetaNode() throws InputFeedException;

  /**
   * Defines an attribute for the meta-nodes. For each meta node, at least the
   * 'type' attribute (namespace: LibLayout) should be defined.
   *
   * @param name
   * @param attr
   */
  public void setMetaNodeAttribute(String name, Object attr)
          throws InputFeedException;

  public void endMetaNode() throws InputFeedException;

  public void endMetaInfo() throws InputFeedException;

  public void startElement(String namespace, String name)
          throws InputFeedException;

  public void setAttribute(String namespace, String name, Object attr)
          throws InputFeedException;

  public void addContent(String text) throws InputFeedException;

  public void endElement() throws InputFeedException;

  public void endDocument() throws InputFeedException;

  public NamespaceCollection getNamespaceCollection();

  public void handlePageBreakEncountered(final CSSValue pageName,
                                         final PseudoPage[] pseudoPages)
          throws NormalizationException;

  public boolean isPagebreakEncountered();
  public void resetPageBreakFlag();

  /**
   * Warning; This method is needed internally, mess with it from the outside
   * and you will run into trouble. The normalizer is a statefull component and
   * any call to it may mess up the state. From there on, 'Abandon every hope,
   * ye who enter here'.
   *
   * @return
   */
  public Normalizer getCurrentNormalizer();
}
