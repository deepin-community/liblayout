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
 * $Id: IdCSSCondition.java 2739 2007-04-02 11:41:22Z taqua $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.input.style.selectors.conditions;

import org.w3c.css.sac.AttributeCondition;

/**
 * Creation-Date: 24.11.2005, 19:54:48
 *
 * @author Thomas Morgner
 */
public class IdCSSCondition implements AttributeCondition, CSSCondition
{
  private String value;

  public IdCSSCondition(final String value)
  {
    this.value = value;
  }

  /** An integer indicating the type of <code>Condition</code>. */
  public short getConditionType()
  {
    return SAC_ID_CONDITION;
  }

  public boolean isMatch(final Object resolveState)
  {
    // todo
    return false;
  }

  /**
   * Returns the <a href="http://www.w3.org/TR/REC-xml-names/#dt-NSName">namespace
   * URI</a> of this attribute condition. <p><code>NULL</code> if : <ul>
   * <li>this attribute condition can match any namespace. <li>this attribute is
   * an id attribute. </ul>
   */
  public String getNamespaceURI()
  {
    return null;
  }

  /**
   * Returns <code>true</code> if the attribute must have an explicit value in
   * the original document, <code>false</code> otherwise.
   */
  public final boolean getSpecified()
  {
    return false;
  }

  public String getValue()
  {
    return value;
  }

  /**
   * Returns the <a href="http://www.w3.org/TR/REC-xml-names/#NT-LocalPart">local
   * part</a> of the <a href="http://www.w3.org/TR/REC-xml-names/#ns-qualnames">qualified
   * name</a> of this attribute. <p><code>NULL</code> if : <ul> <li><p>this
   * attribute condition can match any attribute. <li><p>this attribute is a
   * class attribute. <li><p>this attribute is an id attribute. <li><p>this
   * attribute is a pseudo-class attribute. </ul>
   */
  public String getLocalName()
  {
    return null;
  }
}
