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
 * $Id: ListOfValuesReadHandler.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.input.style.parser.stylehandler;

import java.util.ArrayList;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.parser.CSSValueFactory;
import org.jfree.layouting.input.style.parser.CSSValueReadHandler;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.input.style.values.CSSValueList;
import org.w3c.css.sac.LexicalUnit;

/**
 * Creation-Date: 26.11.2005, 19:16:43
 *
 * @author Thomas Morgner
 */
public abstract class ListOfValuesReadHandler implements CSSValueReadHandler
{
  private int maxCount;
  private boolean distinctValues;

  protected ListOfValuesReadHandler()
  {
    maxCount = Integer.MAX_VALUE;
    distinctValues = false;
  }

  protected ListOfValuesReadHandler(final int maxCount, final boolean distinct)
  {
    this.maxCount = maxCount;
    this.distinctValues = distinct;
  }

  public boolean isDistinctValues()
  {
    return distinctValues;
  }

  public int getMaxCount()
  {
    return maxCount;
  }

  public CSSValue createValue(final StyleKey name, LexicalUnit value)
  {
    final ArrayList list = new ArrayList();
    int count = 0;
    while (value != null && count < maxCount)
    {
      final CSSValue pvalue = parseValue(value);
      if (pvalue == null)
      {
        return null;
      }
      if (distinctValues == false ||
          list.contains(pvalue) == false)
      {
        list.add(pvalue);
      }
      value = CSSValueFactory.parseComma(value);
      count += 1;
    }

    return new CSSValueList(list);
  }

  protected abstract CSSValue parseValue(final LexicalUnit value);
}
