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
 * $Id: OneOfAttributeCSSCondition.java 2739 2007-04-02 11:41:22Z taqua $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.input.style.selectors.conditions;

/**
 * Creation-Date: 24.11.2005, 20:13:53
 *
 * @author Thomas Morgner
 */
public class OneOfAttributeCSSCondition extends AttributeCSSCondition
{
  public OneOfAttributeCSSCondition(final String name,
                                    final String namespace,
                                    final boolean specified,
                                    final String value)
  {
    super(name, namespace, specified, value);
  }

  public boolean isMatch(final Object resolveState)
  {
    return false; // todo
  }

  /** An integer indicating the type of <code>Condition</code>. */
  public short getConditionType()
  {
    return SAC_ONE_OF_ATTRIBUTE_CONDITION;
  }
}
