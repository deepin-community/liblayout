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
 * $Id: DocumentContext.java 6489 2008-11-28 14:53:40Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.layouter.context;

import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.layouter.counters.CounterStyle;
import org.jfree.layouting.namespace.NamespaceCollection;
import org.pentaho.reporting.libraries.resourceloader.ResourceManager;

/**
 * The document context holds general document-wide data. It does not hold
 * any data, that is expected to change, the document context is meant to be
 * as stateless as possible (after the context has been set up, of course).
 *
 * @author Thomas Morgner
 */
public interface DocumentContext extends DocumentMetaNode
{
  public static final String BASE_RESOURCE_ATTR = "base-resource";
  public static final String DATE_ATTR = "date";
  public static final String LOCALIZATION_ATTR = "localization-context";
  public static final String RESOURCE_MANAGER_ATTR = "resource-manager";
  public static final String STYLE_RESOLVER_ATTR = "style-resolver";
  public static final String STYLE_MATCHER_ATTR = "style-matcher";
  public static final String TITLE_ATTR = "title";
  public static final String STRICT_STYLE_MODE = "strict-style-mode";
  public static final String INITIAL_STYLE = "initial-style";

  /**
   * This method is called once after the input-feed received all the document
   * meta-data.
   */
  public void initialize();

  public void addMetaNode (DocumentMetaNode node);
  public void removeMetaNode (DocumentMetaNode node);
  public DocumentMetaNode getMetaNode (int index);
  public int getMetaNodeCount ();

  /**
   * The namespace collection is not available until initialize() has been called
   * by the input-feed.
   *
   * @return
   */
  public NamespaceCollection getNamespaces();

  /**
   * Defines a global counter style. The style is stored by the counter's name,
   * and if not defined in the counter-property, the counter style is looked up
   * here.
   *
   * This offers a way to define a style for counters at one point, instead of
   * having to copy the style definition for all counter instances.
   *
   * @param counterName
   * @param style
   */
  public void setCounterStyle (String counterName, CounterStyle style);

  /**
   * Looks up a global counter style. If not defined, this returns the default
   * decimal style.
   *
   * @param counterName the name of the counter, for which we search the style.
   * @return the defined style or the decimal style.
   */
  public CounterStyle getCounterStyle (String counterName);

  /**
   * Returns the string policy for this named string. The is either one of the
   * defined PagePolicy constants or null, if no policy is defined, in which
   * case always the current value is used.
   *
   * @param name
   * @return
   */
  public CSSValue getStringPolicy(String name);

  public void setStringPolicy(String name, CSSValue policy);

  /**
   * Returns the counter policy for this counter. The is either one of the
   * defined PagePolicy constants or null, if no policy is defined, in which
   * case always the current value is used.
   *
   * @param name
   * @return
   */
  public CSSValue getCounterPolicy(String name);

  public void setCounterPolicy(String name, CSSValue policy);

  public ResourceManager getResourceManager();

}
