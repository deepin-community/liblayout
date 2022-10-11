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
 * $Id: CSSCharacterDataSelector.java 2739 2007-04-02 11:41:22Z taqua $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.input.style.selectors;

import org.w3c.css.sac.CharacterDataSelector;

/**
 * Creation-Date: 30.11.2005, 16:04:27
 *
 * @author Thomas Morgner
 */
public class CSSCharacterDataSelector extends AbstractSelector
        implements CharacterDataSelector
{
  private String data;

  public CSSCharacterDataSelector(final String data)
  {
    this.data = data;
  }

  protected SelectorWeight createWeight()
  {
    return new SelectorWeight(0, 0, 0, 1);
  }

  /** Returns the character data. */
  public String getData()
  {
    return data;
  }

  /** An integer indicating the type of <code>Selector</code> */
  public short getSelectorType()
  {
    return SAC_CDATA_SECTION_NODE_SELECTOR;
  }
}
