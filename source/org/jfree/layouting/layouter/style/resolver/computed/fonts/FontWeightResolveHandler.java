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
 * $Id: FontWeightResolveHandler.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.layouter.style.resolver.computed.fonts;

import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.keys.font.FontStyleKeys;
import org.jfree.layouting.input.style.keys.font.FontWeight;
import org.jfree.layouting.input.style.values.CSSNumericType;
import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.layouter.model.LayoutElement;
import org.jfree.layouting.layouter.style.resolver.ResolveHandler;

/**
 * Creation-Date: 18.12.2005, 20:33:42
 *
 * @author Thomas Morgner
 */
public class FontWeightResolveHandler implements ResolveHandler
{
  public FontWeightResolveHandler()
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
    final int fontWeight;
    if (FontWeight.BOLD.equals(value))
    {
      // ask the parent ...
      fontWeight = 700;
    }
    else if (FontWeight.NORMAL.equals(value))
    {
      // ask the parent ...
      fontWeight = 400;
    }
    else if (FontWeight.BOLDER.equals(value))
    {
      final int parentFontWeight = queryParent(currentNode.getParent());
      fontWeight = Math.max (900, parentFontWeight + 100);
    }
    else if (FontWeight.LIGHTER.equals(value))
    {
      final int parentFontWeight = queryParent(currentNode.getParent());
      fontWeight = Math.min (100, parentFontWeight - 100);
    }
    else if (value instanceof CSSNumericValue)
    {
      final CSSNumericValue nval = (CSSNumericValue) value;
      if (CSSNumericType.NUMBER.equals(nval.getType()) == false)
      {
        // preserve the parent's weight...
        fontWeight = queryParent(currentNode.getParent());
      }
      else
      {
        fontWeight = (int) nval.getValue();
      }
    }
    else
    {
      fontWeight = queryParent(currentNode.getParent());
    }

    layoutContext.setValue(FontStyleKeys.FONT_WEIGHT, CSSNumericValue.createValue(CSSNumericType.NUMBER, fontWeight));
  }

  private int queryParent(final LayoutElement parent)
  {
    if (parent == null)
    {
      return 400; // Normal
    }
    return parent.getLayoutContext().getFontSpecification().getFontWeight();
  }
}
