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
 * $Id: PositionalCSSCondition.java 2739 2007-04-02 11:41:22Z taqua $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.input.style.selectors.conditions;

import org.w3c.css.sac.PositionalCondition;

/**
 * Creation-Date: 24.11.2005, 19:51:10
 *
 * @author Thomas Morgner
 */
public class PositionalCSSCondition implements CSSCondition, PositionalCondition
{
  private int position;
  private boolean matchByType;
  private boolean matchByName;

  public PositionalCSSCondition(final int position,
                                final boolean matchByType,
                                final boolean matchByName)
  {
    this.position = position;
    this.matchByType = matchByType;
    this.matchByName = matchByName;
  }

  public boolean isMatch(final Object resolveState)
  {
    // todo: need access to the DOM for that ..
    return false;
  }

  /** An integer indicating the type of <code>Condition</code>. */
  public short getConditionType()
  {
    return SAC_POSITIONAL_CONDITION;
  }

  /**
   * Returns the position in the tree. <p>A negative value means from the end of
   * the child node list. <p>The child node list begins at 0.
   */
  public int getPosition()
  {
    return position;
  }

  /**
   * <code>true</code> if the child node list only shows nodes of the same type
   * of the selector (only elements, only PIS, ...)
   */
  public boolean getTypeNode()
  {
    return matchByType;
  }

  /**
   * <code>true</code> if the node should have the same node type (for element,
   * same namespaceURI and same localName).
   */
  public boolean getType()
  {
    return matchByName;
  }
}
