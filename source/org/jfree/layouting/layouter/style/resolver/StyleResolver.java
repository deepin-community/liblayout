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
 * $Id: StyleResolver.java 2739 2007-04-02 11:41:22Z taqua $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.layouter.style.resolver;

import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.StatefullComponent;
import org.jfree.layouting.input.style.PageAreaType;
import org.jfree.layouting.input.style.PseudoPage;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.layouter.context.ContextId;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.layouter.context.LayoutStyle;
import org.jfree.layouting.layouter.model.LayoutElement;

/**
 * Creation-Date: 05.12.2005, 18:03:52
 *
 * @author Thomas Morgner
 */
public interface StyleResolver extends StatefullComponent
{
  public StyleResolver deriveInstance();

  /**
   * Resolves the style. This is guaranteed to be called in the order of the
   * document elements traversing the document tree using the
   * 'deepest-node-first' strategy.
   *
   * @param element
   */
  public void resolveStyle (LayoutElement element);

  /**
   * Performs tests, whether there is a pseudo-element definition for the
   * given element. The element itself can be a pseudo-element as well.
   *
   * @param element
   * @param pseudo
   * @return
   */
  public boolean isPseudoElementStyleResolvable (LayoutElement element,
                                                 String pseudo);

  public void initialize(LayoutProcess layoutProcess);

  public LayoutStyle resolvePageStyle
          (CSSValue pageName, PseudoPage[] pseudoPages, PageAreaType pageArea);

  public LayoutContext createAnonymousContext(final ContextId id,
                                              final LayoutContext parent);

  public LayoutStyle getInitialStyle();

}
