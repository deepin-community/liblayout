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
 * $Id: TextOverflowEllipsisResolveHandler.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.layouter.style.resolver.computed.text;

import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.keys.text.TextStyleKeys;
import org.jfree.layouting.input.style.values.CSSStringType;
import org.jfree.layouting.input.style.values.CSSStringValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.input.style.values.CSSValueList;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.layouter.model.LayoutElement;
import org.jfree.layouting.layouter.style.resolver.ResolveHandler;

/**
 * Creation-Date: 21.12.2005, 16:48:23
 *
 * @author Thomas Morgner
 */
public class TextOverflowEllipsisResolveHandler implements ResolveHandler
{
  public TextOverflowEllipsisResolveHandler()
  {
  }

  /**
   * This indirectly defines the resolve order. The higher the order, the more
   * dependent is the resolver on other resolvers to be complete.
   *
   * @return
   */
  public StyleKey[] getRequiredStyles()
  {
    return new StyleKey[0];
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
    final LayoutContext layoutContext = currentNode.getLayoutContext();
    final CSSValue value = layoutContext.getValue(key);
    CSSStringValue lineEllipsis = null;
    CSSStringValue blockEllipsis = null;
    if (value instanceof CSSValueList)
    {
      final CSSValueList vlist = (CSSValueList) value;
      if (vlist.getLength() == 2)
      {
        lineEllipsis = filterString(vlist.getItem(0));
        blockEllipsis = filterString(vlist.getItem(1));
      }
      else if (vlist.getLength() == 1)
      {
        lineEllipsis = filterString(vlist.getItem(0));
        blockEllipsis = filterString(vlist.getItem(0));
      }
    }
    if (lineEllipsis == null)
    {
      lineEllipsis = new CSSStringValue(CSSStringType.STRING, "..");
    }
    if (blockEllipsis == null)
    {
      blockEllipsis = new CSSStringValue(CSSStringType.STRING, "..");
    }

    layoutContext.setValue(TextStyleKeys.X_BLOCK_TEXT_OVERFLOW_ELLIPSIS, blockEllipsis);
    layoutContext.setValue(TextStyleKeys.X_LINE_TEXT_OVERFLOW_ELLIPSIS, lineEllipsis);
  }

  private CSSStringValue filterString (final CSSValue value)
  {
    if (value instanceof CSSStringValue == false)
    {
      return null;
    }
    return (CSSStringValue) value;
  }
}
