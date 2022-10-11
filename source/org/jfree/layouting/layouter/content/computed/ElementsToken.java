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
 * $Id: ElementsToken.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.layouter.content.computed;

/**
 * The elemnts function. This is a lookup to the current pending context.
 * The elements function grabs the last value from the pending context and
 * drops all previous elements. If the pending context is empty, it preserves
 * its content.
 *
 * The elements get removed from the normal flow and get added to the pending
 * flow. Due to the highly volatile nature of that step, no - I repeat - no
 * validation is done to normalize inline and block elements.
 *
 * @author Thomas Morgner
 */
public class ElementsToken extends ComputedToken
{
  private String key;

  public ElementsToken(final String key)
  {
    this.key = key;
  }

  public String getKey()
  {
    return key;
  }
}
