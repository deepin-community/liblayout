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
 * $Id: ListOfConstantsResolveHandler.java 6489 2008-11-28 14:53:40Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.layouter.style.resolver.computed;

import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.values.CSSConstant;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.input.style.values.CSSValueList;
import org.jfree.layouting.layouter.model.LayoutElement;
import org.pentaho.reporting.libraries.base.util.DebugLog;

/**
 * Creation-Date: 14.12.2005, 23:08:14
 *
 * @author Thomas Morgner
 */
public abstract class ListOfConstantsResolveHandler extends ConstantsResolveHandler
{
  public ListOfConstantsResolveHandler()
  {
  }

  /**
   * Resolves a single property.
   *
   * @param currentNode
   * @param style
   */
  public void resolve(final LayoutProcess process,
                      final LayoutElement currentNode,
                      final StyleKey key)
  {
    final CSSValue value = currentNode.getLayoutContext().getValue(key);
    if (value == null)
    {
      return;
    }
    if (value instanceof CSSValueList == false)
    {
      return;
    }

    final CSSValueList list = (CSSValueList) value;
    final int length = list.getLength();
    if (length == 0)
    {
      return;
    }

    for (int i = 0; i < length; i++)
    {
      final CSSValue item = list.getItem(i);
      if (item instanceof CSSConstant == false)
      {
        resolveInvalidItem(process, currentNode, key, i);
      }
      else
      {
        resolveItem(process, currentNode, key, i, (CSSConstant) item);
      }
    }
  }

  protected void resolveInvalidItem (final LayoutProcess process,
                                     final LayoutElement currentNode,
                                     final StyleKey key,
                                     final int index)
  {
    DebugLog.log("Encountered invalid item in Style " + key + " at index " + index);
  }

  protected abstract boolean resolveItem(final LayoutProcess process,
                                         LayoutElement currentNode,
                                         StyleKey key,
                                         int index,
                                         CSSConstant item);
}
