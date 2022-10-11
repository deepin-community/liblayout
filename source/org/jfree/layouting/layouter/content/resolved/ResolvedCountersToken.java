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
 * $Id: ResolvedCountersToken.java 2739 2007-04-02 11:41:22Z taqua $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.layouting.layouter.content.resolved;

import org.jfree.layouting.layouter.content.computed.CountersToken;
import org.jfree.layouting.layouter.content.type.TextType;
import org.jfree.layouting.layouter.counters.CounterStyle;

/**
 * Creation-Date: 12.06.2006, 14:38:29
 *
 * @author Thomas Morgner
 */
public class ResolvedCountersToken implements ResolvedToken, TextType
{
  private CountersToken parent;
  private int[] counterValues;

  public ResolvedCountersToken(final CountersToken parent,
                               final int[] counterValues)
  {
    this.parent = parent;
    this.counterValues = counterValues;
  }

  public CountersToken getParent()
  {
    return parent;
  }

  public String getText()
  {
    final CountersToken counterToken = getParent();
    final CounterStyle style = counterToken.getStyle();
    final String separator = counterToken.getSeparator();
    final StringBuffer buffer = new StringBuffer();

    for (int i = 0; i < counterValues.length; i++)
    {
      if (i != 0)
      {
        buffer.append(separator);
      }
      final int value = counterValues[i];
      buffer.append(style.getCounterValue(value));
    }

    return buffer.toString();
  }

  public int[] getCounterValue()
  {
    return (int[]) counterValues.clone();
  }
}
