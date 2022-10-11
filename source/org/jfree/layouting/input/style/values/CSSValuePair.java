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
 * $Id: CSSValuePair.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.input.style.values;

/**
 * Creation-Date: 15.04.2006, 10:56:53
 *
 * @author Thomas Morgner
 */
public class CSSValuePair implements CSSValue
{
  private CSSValue firstValue;
  private CSSValue secondValue;

  public CSSValuePair(final CSSValue firstValue)
  {
    if (firstValue == null)
    {
      throw new NullPointerException();
    }
    this.firstValue = firstValue;
    this.secondValue = firstValue;
  }

  public CSSValuePair(final CSSValue firstValue, final CSSValue secondValue)
  {
    if (firstValue == null)
    {
      throw new NullPointerException();
    }
    if (secondValue == null)
    {
      throw new NullPointerException();
    }
    this.firstValue = firstValue;
    this.secondValue = secondValue;
  }

  public CSSValue getFirstValue()
  {
    return firstValue;
  }

  public CSSValue getSecondValue()
  {
    return secondValue;
  }

  public String getCSSText()
  {
    return firstValue.getCSSText() + ' ' + secondValue.getCSSText();
  }

  public String toString()
  {
    return getCSSText();
  }
}
