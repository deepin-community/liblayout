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
 * $Id: FlatStyleResolver.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.layouting.layouter.style.resolver;

import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.State;
import org.jfree.layouting.StateException;
import org.jfree.layouting.input.style.PageAreaType;
import org.jfree.layouting.input.style.PseudoPage;
import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.layouter.context.LayoutStyle;
import org.jfree.layouting.layouter.model.LayoutElement;

/**
 * Not yet used. Needs to be implemented. Its my fast resolver, but that
 * one needs more thinking and more tweaking.
 */
public class FlatStyleResolver extends AbstractStyleResolver
{
  public FlatStyleResolver ()
  {
  }

  public StyleResolver deriveInstance ()
  {
    return this;
  }

  public void initialize (final LayoutProcess layoutProcess)
  {
    super.initialize(layoutProcess);

  }

  protected void resolveOutOfContext(final LayoutElement element)
  {

  }

  public LayoutStyle resolvePageStyle(final CSSValue pageName,
                                                                             final PseudoPage[] pseudoPages,
                                                                             final PageAreaType pageArea)
  {
    return null;
  }

  /**
   * Performs tests, whether there is a pseudo-element definition for the given
   * element. The element itself can be a pseudo-element as well.
   *
   * @param element
   * @param pseudo
   * @return
   */
  public boolean isPseudoElementStyleResolvable(final LayoutElement element,
                                                final String pseudo)
  {
    return false;
  }

  /**
   * Resolves the style. This is guaranteed to be called in the order of the document
   * elements traversing the document tree using the 'deepest-node-first' strategy.
   *
   * @param node
   */
  public void resolveStyle (final LayoutElement node)
  {
    // this is a three stage process
    final LayoutContext layoutContext = node.getLayoutContext();
    final StyleKey[] keys = getKeys();

    // Stage 0: Initialize with the built-in defaults
    // Stage 1a: Add the parent styles (but only the one marked as inheritable).
    final LayoutStyle initialStyle = getInitialStyle();

    // initialize the style ...
    for (int i = 0; i < keys.length; i++)
    {
      final StyleKey key = keys[i];
      layoutContext.setValue(key, initialStyle.getValue(key));
    }

    // Stage 2: Search for all class attributes, and lookup the corresponding
    // style. The sty


  }


  public State saveState() throws StateException
  {
    return null;
  }
}
