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
 * $Id: CSSMediaRule.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.input.style;

import java.util.ArrayList;

/**
 * Creation-Date: 23.11.2005, 11:00:04
 *
 * @author Thomas Morgner
 */
public class CSSMediaRule extends CSSDeclarationRule
{
  private ArrayList rules;

  public CSSMediaRule(final StyleSheet parentStyle, final StyleRule parentRule)
  {
    super(parentStyle, parentRule);
    this.rules = new ArrayList();
  }

  public void addRule (final StyleRule rule)
  {
    rules.add(rule);
  }

  public void insertRule (final int index, final StyleRule rule)
  {
    rules.add(index, rule);
  }

  public void deleteRule (final int index)
  {
    rules.remove(index);
  }

  public int getRuleCount ()
  {
    return rules.size();
  }

  public StyleRule getRule (final int index)
  {
    return (StyleRule) rules.get(index);
  }
}
