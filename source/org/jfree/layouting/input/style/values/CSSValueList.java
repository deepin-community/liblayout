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
 * $Id: CSSValueList.java 6489 2008-11-28 14:53:40Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.input.style.values;

import java.util.Collection;

import org.pentaho.reporting.libraries.base.util.ObjectUtilities;


/**
 * Creation-Date: 23.11.2005, 12:37:21
 *
 * @author Thomas Morgner
 */
public final class CSSValueList implements CSSValue, Cloneable
{
  private CSSValue[] values;

  public CSSValueList(final Collection collection)
  {
    final CSSValue[] values = new CSSValue[collection.size()];
    this.values = (CSSValue[]) collection.toArray(values);
  }

  public CSSValueList(final CSSValue[] values)
  {
    this.values = values;
  }

  public int getLength()
  {
    return values.length;
  }

  public CSSValue getItem(final int index)
  {
    return values[index];
  }

  public String getCSSText()
  {
    final StringBuffer b = new StringBuffer();
    for (int i = 0; i < values.length; i++)
    {
      final CSSValue value = values[i];
      if (i > 0)
      {
        b.append(' ');
      }
      b.append(value);
    }
    return b.toString();
  }

  public String toString()
  {
    return getCSSText();
  }

  public boolean contains(final CSSValue value)
  {
    for (int i = 0; i < values.length; i++)
    {
      final CSSValue cssValue = values[i];
      if (ObjectUtilities.equal(cssValue, value))
      {
        return true;
      }
    }
    return false;
  }

  public static CSSValueList createList(final CSSValue value)
  {
    return new CSSValueList( new CSSValue[]{ value });
  }

  public static CSSValueList createDuoList(final CSSValue value)
  {
    return CSSValueList.createDuoList(value, value);
  }

  public static CSSValueList createDuoList(final CSSValue first, final CSSValue second)
  {
    final CSSValue[] values = new CSSValue[2];
    values[0] = first;
    values[1] = second;
    return new CSSValueList(values);
  }

  public static CSSValueList createQuadList(final CSSValue value)
  {
    return CSSValueList.createQuadList(value, value);
  }

  public static CSSValueList createQuadList(final CSSValue first, final CSSValue second)
  {
    return CSSValueList.createQuadList(first, second, first, second);
  }

  public static CSSValueList createQuadList(final CSSValue first, final CSSValue second,
                                            final CSSValue third, final CSSValue fourth)
  {
    final CSSValue[] values = new CSSValue[4];
    values[0] = first;
    values[1] = second;
    values[2] = third;
    values[3] = fourth;
    return new CSSValueList(values);
  }

  public static CSSValueList insertFirst (final CSSValueList list,
                                          final CSSValue value)
  {
    final int length = list.values.length;
    final CSSValue[] newValues = new CSSValue[length + 1];
    newValues[0] = value;
    System.arraycopy(list.values, 0, newValues, 1, length);
    return new CSSValueList(newValues);
  }

  public static CSSValueList insertLast (final CSSValueList list,
                                          final CSSValue value)
  {
    final int length = list.values.length;
    final CSSValue[] newValues = new CSSValue[length + 1];
    System.arraycopy(list.values, 0, newValues, 0, length);
    newValues[length] = value;
    return new CSSValueList(newValues);
  }

  public Object clone()
  {
    try
    {
      final CSSValueList o = (CSSValueList) super.clone();
      o.values = (CSSValue[]) values.clone();
      return o;
    }
    catch (CloneNotSupportedException e)
    {
      throw new IllegalAccessError("Clone cannot be unsupported.");
    }
  }
}
