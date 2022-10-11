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
 * $Id: TextDecorationWidthResolveHandler.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.layouting.layouter.style.resolver.computed.text;

import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.keys.border.BorderWidth;
import org.jfree.layouting.input.style.keys.font.FontStyleKeys;
import org.jfree.layouting.input.style.keys.text.TextDecorationWidth;
import org.jfree.layouting.input.style.values.CSSConstant;
import org.jfree.layouting.input.style.values.CSSNumericType;
import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.layouter.model.LayoutElement;
import org.jfree.layouting.layouter.style.resolver.computed.ConstantsResolveHandler;

public class TextDecorationWidthResolveHandler extends ConstantsResolveHandler
{
  public TextDecorationWidthResolveHandler ()
  {
    addValue(BorderWidth.THIN, CSSNumericValue.createValue(CSSNumericType.PT, 0.5));
    addValue(BorderWidth.MEDIUM, CSSNumericValue.createValue(CSSNumericType.PT, 1));
    addValue(BorderWidth.THICK, CSSNumericValue.createValue(CSSNumericType.PT, 1.5));
    addValue(TextDecorationWidth.DASH, CSSNumericValue.createValue(CSSNumericType.PT, 0.75));
    addValue(TextDecorationWidth.BOLD, CSSNumericValue.createValue(CSSNumericType.PT, 1.25));
    setFallback(CSSNumericValue.ZERO_LENGTH);
  }

  /**
   * This indirectly defines the resolve order. The higher the order, the more dependent
   * is the resolver on other resolvers to be complete.
   *
   * @return the array of required style keys.
   */
  public StyleKey[] getRequiredStyles ()
  {
    return new StyleKey[] {
            FontStyleKeys.FONT_SIZE
    };
  }

  protected CSSValue resolveValue (final LayoutProcess process,
                                   final LayoutElement currentNode,
                                   final StyleKey key)
  {
    final CSSValue value = currentNode.getLayoutContext().getValue(key);
    if (value instanceof CSSConstant)
    {
      return super.resolveValue(process, currentNode, key);
    }
    return value;
  }
}
