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
 * $Id: CSSNumericValue.java 2739 2007-04-02 11:41:22Z taqua $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.input.style.values;

import org.jfree.layouting.util.geom.StrictGeomUtility;

/**
 * Creation-Date: 23.11.2005, 11:37:44
 *
 * @author Thomas Morgner
 */
public class CSSNumericValue implements CSSValue
{
  public static final CSSNumericValue ZERO_LENGTH = CSSNumericValue.createValue(CSSNumericType.PT, 0);

  private double value;
  private CSSNumericType type;

  protected CSSNumericValue(final CSSNumericType type, final double value)
  {
    if (type == null)
    {
      throw new NullPointerException();
    }
    this.type = type;
    this.value = value;
  }

  public double getValue()
  {
    return value;
  }

  public CSSNumericType getType()
  {
    return type;
  }

  public String getCSSText()
  {
    final String typeText = type.getType();
    final double value = getValue();
    if (typeText.length() == 0)
    {
      if (Math.floor(value) == value)
      {
        return String.valueOf((long) value);
      }
      return String.valueOf(value);
    }

    if (Math.floor(value) == value)
    {
      return ((long) value) + " " + typeText;
    }
    return value + " " + typeText;
  }

  public String toString()
  {
    return getCSSText();
  }

  public static CSSNumericValue createPtValue(final double value)
  {
    return new CSSNumericValue(CSSNumericType.PT, value);
  }

  public static CSSNumericValue createValue(final CSSNumericType type,
                                            final double value)
  {
    return new CSSNumericValue(type, value);
  }
}
