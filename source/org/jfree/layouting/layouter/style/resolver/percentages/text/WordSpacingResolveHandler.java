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
 * $Id: WordSpacingResolveHandler.java 6489 2008-11-28 14:53:40Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.layouter.style.resolver.percentages.text;

import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.util.geom.StrictGeomUtility;
import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.keys.font.FontStyleKeys;
import org.jfree.layouting.input.style.keys.text.TextStyleKeys;
import org.jfree.layouting.input.style.values.CSSNumericType;
import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.layouter.context.FontSpecification;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.layouter.model.LayoutElement;
import org.jfree.layouting.layouter.style.CSSValueResolverUtility;
import org.jfree.layouting.layouter.style.resolver.ResolveHandler;
import org.pentaho.reporting.libraries.fonts.registry.FontMetrics;
import org.pentaho.reporting.libraries.base.util.DebugLog;

/**
 * Creation-Date: 21.12.2005, 15:12:04
 *
 * @author Thomas Morgner
 */
public class WordSpacingResolveHandler implements ResolveHandler
{
  public WordSpacingResolveHandler()
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
    return new StyleKey[] {
            FontStyleKeys.FONT_SIZE,
            FontStyleKeys.FONT_FAMILY,
            FontStyleKeys.FONT_EFFECT,
            FontStyleKeys.FONT_SMOOTH,
            FontStyleKeys.FONT_STRETCH,
            FontStyleKeys.FONT_VARIANT,
            FontStyleKeys.FONT_WEIGHT,
    };
  }

  /**
   * Resolves a single property.
   *
   * @param currentNode
   */
  public void resolve(final LayoutProcess process,
                      final LayoutElement currentNode,
                      final StyleKey key)
  {
    // Percentages get resolved against the width of a standard space (0x20)
    // character.
    final LayoutContext layoutContext = currentNode.getLayoutContext();
    final FontSpecification fontSpecification =
            layoutContext.getFontSpecification();
    final FontMetrics fm = process.getOutputMetaData().getFontMetrics(fontSpecification);
    if (fm == null)
    {
      final CSSValue value = layoutContext.getValue(FontStyleKeys.FONT_FAMILY);
      DebugLog.log("FontFamily is " + value + " but has not been set?" + currentNode);
      return;
    }
    final double width = StrictGeomUtility.toExternalValue(fm.getCharWidth(0x20));
    final CSSNumericValue percentageBase =
            CSSNumericValue.createValue(CSSNumericType.PT, width);
    final CSSNumericValue min = CSSValueResolverUtility.getLength
            (resolveValue(layoutContext, TextStyleKeys.X_MIN_WORD_SPACING), percentageBase);
    final CSSNumericValue max = CSSValueResolverUtility.getLength
            (resolveValue(layoutContext, TextStyleKeys.X_MAX_WORD_SPACING), percentageBase);
    final CSSNumericValue opt = CSSValueResolverUtility.getLength
            (resolveValue(layoutContext, TextStyleKeys.X_OPTIMUM_WORD_SPACING), percentageBase);

    layoutContext.setValue(TextStyleKeys.X_MIN_WORD_SPACING, min);
    layoutContext.setValue(TextStyleKeys.X_MAX_WORD_SPACING, max);
    layoutContext.setValue(TextStyleKeys.X_OPTIMUM_WORD_SPACING, opt);
  }

  private CSSNumericValue resolveValue (final LayoutContext style, final StyleKey key)
  {
    final CSSValue value = style.getValue(key);
    if (value instanceof CSSNumericValue == false)
    {
      // this also covers the valid 'normal' property.
      // it simply means, dont add extra space to the already existing spaces
      return CSSNumericValue.ZERO_LENGTH;
    }

    return (CSSNumericValue) value;
  }
}
