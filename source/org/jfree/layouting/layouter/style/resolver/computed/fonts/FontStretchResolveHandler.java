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
 * $Id: FontStretchResolveHandler.java 6489 2008-11-28 14:53:40Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.layouter.style.resolver.computed.fonts;

import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.keys.font.FontStretch;
import org.jfree.layouting.input.style.keys.font.FontStyleKeys;
import org.jfree.layouting.input.style.values.CSSConstant;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.layouter.model.LayoutElement;
import org.jfree.layouting.layouter.style.resolver.computed.ConstantsResolveHandler;

/**
 * Creation-Date: 18.12.2005, 20:33:42
 *
 * @author Thomas Morgner
 */
public class FontStretchResolveHandler extends ConstantsResolveHandler
{
  public FontStretchResolveHandler()
  {
    addNormalizeValue(FontStretch.CONDENSED);
    addNormalizeValue(FontStretch.EXPANDED);
    addNormalizeValue(FontStretch.EXTRA_CONDENSED);
    addNormalizeValue(FontStretch.EXTRA_EXPANDED);
    addNormalizeValue(FontStretch.NORMAL);
    addNormalizeValue(FontStretch.SEMI_CONDENSED);
    addNormalizeValue(FontStretch.SEMI_EXPANDED);
    addNormalizeValue(FontStretch.ULTRA_CONDENSED);
    addNormalizeValue(FontStretch.ULTRA_EXPANDED);
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
    final CSSConstant result;
    if (FontStretch.WIDER.equals(value))
    {
      // ask the parent ...
      final CSSConstant parentStretch = queryParent(currentNode.getParent());
      result = FontStretch.getByOrder(FontStretch.getOrder(parentStretch) + 1);
    }
    else if (FontStretch.NARROWER.equals(value))
    {
      // ask the parent ...
      final CSSConstant parentStretch = queryParent(currentNode.getParent());
      result = FontStretch.getByOrder(FontStretch.getOrder(parentStretch) - 1);
    }
    else if (value instanceof CSSConstant)
    {
      final CSSConstant stretch = (CSSConstant) lookupValue((CSSConstant) value);
      if (stretch != null)
      {
        result = stretch;
      }
      else
      {
        result = FontStretch.NORMAL;
      }
    }
    else
    {
      result = FontStretch.NORMAL;
    }
    layoutContext.setValue(key, result);
  }

  private CSSConstant queryParent(final LayoutElement parent)
  {
    if (parent == null)
    {
      return FontStretch.NORMAL;
    }
    final CSSValue parentValue =
            parent.getLayoutContext().getValue(FontStyleKeys.FONT_STRETCH);
    if (parentValue == null)
    {
      return FontStretch.NORMAL;
    }
    // normalize ..
    return FontStretch.getByOrder(FontStretch.getOrder(parentValue));
  }
}
