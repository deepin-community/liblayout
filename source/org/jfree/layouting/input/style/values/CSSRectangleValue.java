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
 * $Id: CSSRectangleValue.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.input.style.values;

/**
 * Creation-Date: 23.11.2005, 12:04:06
 *
 * @author Thomas Morgner
 */
public class CSSRectangleValue implements CSSValue
{
  private CSSRectangleType type;
  private CSSNumericValue top;
  private CSSNumericValue left;
  private CSSNumericValue bottom;
  private CSSNumericValue right;

  public CSSRectangleValue(final CSSRectangleType type,
                           final CSSNumericValue top,
                           final CSSNumericValue right,
                           final CSSNumericValue bottom,
                           final CSSNumericValue left)
  {
    this.type = type;
    this.top = top;
    this.left = left;
    this.bottom = bottom;
    this.right = right;
  }

  public CSSNumericValue getTop()
  {
    return top;
  }

  public CSSNumericValue getLeft()
  {
    return left;
  }

  public CSSNumericValue getBottom()
  {
    return bottom;
  }

  public CSSNumericValue getRight()
  {
    return right;
  }

  public String getCSSText()
  {
    return toString();
  }

  public CSSRectangleType getType()
  {
    return type;
  }

  public String toString ()
  {
    final StringBuffer buffer = new StringBuffer();
    buffer.append(type.getType());
    buffer.append('(');
    buffer.append(top);
    buffer.append(", ");
    buffer.append(left);
    buffer.append(", ");
    buffer.append(bottom);
    buffer.append(", ");
    buffer.append(right);
    buffer.append(')');
    return buffer.toString();
  }
}
