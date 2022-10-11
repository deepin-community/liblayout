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
 * $Id: CompiledSelectorWeight.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.layouting.layouter.style.compiler;

import org.jfree.layouting.input.style.selectors.SelectorWeight;

/**
 * This selector weight is computed over all CSS-Declaration rules
 * of an document. Lower weight rules get executed before all higher
 * ranking rules. The more specific a rule is, the later it gets
 * executed. This allows later rules to override previously defined
 * rules.
 */
public class CompiledSelectorWeight implements Comparable
{
  private int fileOrder;
  private int selectorOrder;
  private SelectorWeight selectorWeight;

  public CompiledSelectorWeight (final SelectorWeight selectorWeight,
                                 final int fileOrder,
                                 final int selectorOrder)
  {
    this.selectorWeight = selectorWeight;
    this.fileOrder = fileOrder;
    this.selectorOrder = selectorOrder;
  }

  /**
   * Compares this object with the specified object for order.  Returns a negative
   * integer, zero, or a positive integer as this object is less than, equal to, or
   * greater than the specified object.<p>
   *
   * @param o the Object to be compared.
   * @return a negative integer, zero, or a positive integer as this object is less than,
   *         equal to, or greater than the specified object.
   *
   * @throws ClassCastException if the specified object's type prevents it from being
   *                            compared to this Object.
   */
  public int compareTo (final Object o)
  {
    final CompiledSelectorWeight csw = (CompiledSelectorWeight) o;

    final int selectorWeightCmp = selectorWeight.compareTo(csw.selectorWeight);
    if (selectorWeightCmp != 0)
    {
      return selectorWeightCmp;
    }
    if (fileOrder < csw.fileOrder)
    {
      return -1;
    }
    if (fileOrder > csw.fileOrder)
    {
      return +1;
    }
    if (selectorOrder < csw.selectorOrder)
    {
      return -1;
    }
    if (selectorOrder > csw.selectorOrder)
    {
      return +1;
    }
    return 0;
  }
}
