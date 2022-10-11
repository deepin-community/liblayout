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
 * $Id: LayoutContext.java 2739 2007-04-02 11:41:22Z taqua $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.layouter.context;

import java.util.Locale;
import java.util.Map;

import org.jfree.layouting.util.AttributeMap;

/**
 * This is where the computed style goes into.
 * // todo: Produce the computed counterset!
 *
 * @author Thomas Morgner
 */
public interface LayoutContext extends LayoutStyle
{
  public ContextId getContextId();

  public BackgroundSpecification getBackgroundSpecification();
  public FontSpecification getFontSpecification();
  public ContentSpecification getContentSpecification();
  public ListSpecification getListSpecification();

  public Map getCounters();
  public Map getStrings();

  /**
   * An element can be exactly one pseudo-element type. It is not possible
   * for an element to fullfill two roles, an element is either a 'before'
   * or a 'marker', but can as well be a 'before' of an 'marker' (where
   * the marker element would be the parent).
   *
   * @return
   */
  public String getPseudoElement();

  /**
   * May be null.
   * @return
   */
  public String getNamespace();
  /**
   * May be null.
   * @return
   */
  public String getTagName();
  /**
   * May never be null.
   * @return
   */
  public AttributeMap getAttributes();

  /**
   * Returns the language definition of this layout context. If not set, it
   * defaults to the parent's language. If the root's language is also not
   * defined, then use the system default.
   *
   * @return the defined language, never null.
   */
  public Locale getLanguage();

  public boolean isPseudoElement();

  public LayoutContext derive();
  public LayoutContext detach(Map counters, Map strings);

  public void dispose();
}
