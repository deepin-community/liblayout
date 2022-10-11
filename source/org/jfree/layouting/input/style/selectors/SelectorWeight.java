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
 * $Id: SelectorWeight.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.input.style.selectors;

import java.io.Serializable;

/**
 * Creation-Date: 05.12.2005, 19:39:58
 *
 * @author Thomas Morgner
 */
public final class SelectorWeight implements Comparable, Serializable
{
  private int styleAttribute;
  private int idCount;
  private int attributeCount; // and pseudo-formats!
  private int elementCount;  // and pseudeo-elements

  public SelectorWeight(final int styleAttribute,
                        final int idCount,
                        final int attributeCount, final int elementCount)
  {
    this(null, styleAttribute, idCount, attributeCount, elementCount);
  }

  public SelectorWeight(final SelectorWeight first,
                        final SelectorWeight second)
  {
    this.styleAttribute = first.styleAttribute + second.styleAttribute;
    this.idCount = first.idCount + second.idCount;
    this.attributeCount = first.attributeCount + second.attributeCount;
    this.elementCount = first.elementCount + second.attributeCount;
  }

  public SelectorWeight(final SelectorWeight parent,
                        final int styleAttribute,
                        final int idCount,
                        final int attributeCount,
                        final int elementCount)
  {
    if (parent == null)
    {
      this.styleAttribute = styleAttribute;
      this.idCount = idCount;
      this.attributeCount = attributeCount;
      this.elementCount = elementCount;
    }
    else
    {
      this.styleAttribute = styleAttribute + parent.styleAttribute;
      this.idCount = idCount + parent.idCount;
      this.attributeCount = attributeCount + parent.attributeCount;
      this.elementCount = elementCount + parent.elementCount;
    }
  }

  public boolean equals(final Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (o == null || getClass() != o.getClass())
    {
      return false;
    }

    final SelectorWeight that = (SelectorWeight) o;

    if (attributeCount != that.attributeCount)
    {
      return false;
    }
    if (elementCount != that.elementCount)
    {
      return false;
    }
    if (idCount != that.idCount)
    {
      return false;
    }
    return styleAttribute == that.styleAttribute;

  }

  public int hashCode()
  {
    int result = styleAttribute;
    result = 29 * result + idCount;
    result = 29 * result + attributeCount;
    result = 29 * result + elementCount;
    return result;
  }

  public int compareTo(final Object o)
  {
    final SelectorWeight weight = (SelectorWeight) o;
    if (styleAttribute < weight.styleAttribute)
    {
      return -1;
    }
    if (styleAttribute > weight.styleAttribute)
    {
      return 1;
    }

    if (idCount < weight.idCount)
    {
      return -1;
    }
    if (idCount > weight.idCount)
    {
      return 1;
    }

    if (attributeCount < weight.attributeCount)
    {
      return -1;
    }
    if (attributeCount > weight.attributeCount)
    {
      return 1;
    }

    if (elementCount < weight.elementCount)
    {
      return -1;
    }
    if (elementCount > weight.elementCount)
    {
      return 1;
    }

    return 0;
  }


  public String toString()
  {
    return "SelectorWeight{" +
            "styleAttribute=" + styleAttribute +
            ", idCount=" + idCount +
            ", attributeCount=" + attributeCount +
            ", elementCount=" + elementCount +
        '}';
  }
}
