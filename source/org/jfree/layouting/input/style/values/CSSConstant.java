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
 * $Id: CSSConstant.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.input.style.values;

/**
 * Creation-Date: 25.11.2005, 18:22:54
 *
 * @author Thomas Morgner
 */
public final class CSSConstant implements CSSValue
{
  private String constant;

  public CSSConstant(final String constant)
  {
    if (constant == null)
    {
      throw new NullPointerException("Constant must not be null");
    }
    this.constant = constant.toLowerCase();
  }

  public String getCSSText()
  {
    return constant;
  }

  public boolean equals(final Object o)
  {
    if (this == o)
    {
      return true;
    }
    // we excplicitly check all subclasses as well. A constant is always defined
    // by its string value.
    if (o instanceof CSSConstant == false)
    {
      return false;
    }

    final CSSConstant that = (CSSConstant) o;

    if (!constant.equals(that.constant))
    {
      return false;
    }

    return true;
  }

  public int hashCode()
  {
    return constant.hashCode();
  }

  public String toString ()
  {
    return getCSSText();
  }
}
