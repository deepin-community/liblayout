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
 * $Id: CSSRootNodeSelector.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.input.style.selectors;

import java.io.Serializable;

import org.w3c.css.sac.SimpleSelector;

/**
 * Creation-Date: 30.11.2005, 16:38:41
 *
 * @author Thomas Morgner
 */
public class CSSRootNodeSelector extends AbstractSelector implements SimpleSelector
{
  public CSSRootNodeSelector()
  {
  }

  protected SelectorWeight createWeight()
  {
    return new SelectorWeight(0, 0, 0, 1);
  }

  /** An integer indicating the type of <code>Selector</code> */
  public short getSelectorType()
  {
    return SAC_ROOT_NODE_SELECTOR;
  }
}
