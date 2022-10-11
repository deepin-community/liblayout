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
 * $Id: CSSPageRule.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.input.style;

import java.util.ArrayList;

/**
 * A page rule contains (among others) page area rules as childs.
 *
 * @author Thomas Morgner
 */
public class CSSPageRule extends CSSDeclarationRule
{
  private ArrayList rules; // the margin rules ...
  private String name;
  private String pseudoPage;

  public CSSPageRule(final StyleSheet parentStyle,
                     final StyleRule parentRule,
                     final String name,
                     final String pseudoPage)
  {
    super(parentStyle, parentRule);
    this.pseudoPage = pseudoPage;
    this.name = name;
    this.rules = new ArrayList();
  }

  public void addRule (final CSSPageAreaRule rule)
  {
    rules.add(rule);
  }

  public void insertRule (final int index, final CSSPageAreaRule rule)
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

  public CSSPageAreaRule getRule (final int index)
  {
    return (CSSPageAreaRule) rules.get(index);
  }

  public String getName()
  {
    return name;
  }

  public String getPseudoPage()
  {
    return pseudoPage;
  }
}
