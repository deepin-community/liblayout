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
 * $Id: CSSSilblingSelector.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.input.style.selectors;

import java.io.Serializable;

import org.w3c.css.sac.Selector;
import org.w3c.css.sac.SiblingSelector;
import org.w3c.css.sac.SimpleSelector;

/**
 * We do not support DOM node types, we always assume elements here (or evaluate
 * both selectors to see if they match).
 *
 * @author Thomas Morgner
 */
public class CSSSilblingSelector extends AbstractSelector
        implements SiblingSelector
{
  private short nodeType;
  private Selector selector;
  private SimpleSelector silblingSelector;

  public CSSSilblingSelector(final short nodeType,
                             final Selector selector,
                             final SimpleSelector silblingSelector)
  {
    this.nodeType = nodeType;
    this.selector = selector;
    this.silblingSelector = silblingSelector;
  }

  /**
   * The node type to considered in the siblings list. All DOM node types are
   * supported. In order to support the "any" node type, the code ANY_NODE is
   * added to the DOM node types.
   */
  public short getNodeType()
  {
    return nodeType;
  }

  /** Returns the first selector. */
  public Selector getSelector()
  {
    return selector;
  }

  /*
  * Returns the second selector.
  */
  public SimpleSelector getSiblingSelector()
  {
    return silblingSelector;
  }

  /** An integer indicating the type of <code>Selector</code> */
  public short getSelectorType()
  {
    return SAC_DIRECT_ADJACENT_SELECTOR;
  }

  protected SelectorWeight createWeight()
  {
    if (silblingSelector instanceof CSSSelector == false ||
        selector instanceof CSSSelector == false)
    {
      throw new ClassCastException("Invalid selector implementation!");
    }
    final CSSSelector anchestor = (CSSSelector) silblingSelector;
    final CSSSelector simple = (CSSSelector) selector;
    return new SelectorWeight(anchestor.getWeight(), simple.getWeight());
  }
}
