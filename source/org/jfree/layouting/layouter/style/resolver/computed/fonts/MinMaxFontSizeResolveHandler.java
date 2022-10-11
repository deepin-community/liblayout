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
 * $Id: MinMaxFontSizeResolveHandler.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.layouter.style.resolver.computed.fonts;

import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.LibLayoutBoot;
import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.keys.font.FontSizeConstant;
import org.jfree.layouting.input.style.keys.font.FontStyleKeys;
import org.jfree.layouting.input.style.keys.font.RelativeFontSize;
import org.jfree.layouting.input.style.values.CSSConstant;
import org.jfree.layouting.input.style.values.CSSNumericType;
import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.layouter.model.LayoutElement;
import org.jfree.layouting.layouter.style.resolver.computed.ConstantsResolveHandler;

// todo apply built in defaults instead ...
/**
 * Creation-Date: 21.12.2005, 12:47:43
 *
 * @author Thomas Morgner
 */
public class MinMaxFontSizeResolveHandler extends ConstantsResolveHandler
{
  private static final String SIZE_FACTOR_PREFIX =
          "org.jfree.layouting.defaults.FontSizeFactor.";
  private double fontSize;
  private CSSNumericValue[] predefinedSizes;
  private double[] predefinedScalingFactors;

  public MinMaxFontSizeResolveHandler()
  {
    fontSize = parseDouble("org.jfree.layouting.defaults.FontSize", 12);
    predefinedSizes = new CSSNumericValue[7];
    predefinedSizes[0] = computePredefinedSize(FontSizeConstant.XX_SMALL);
    predefinedSizes[1] = computePredefinedSize(FontSizeConstant.X_SMALL);
    predefinedSizes[2] = computePredefinedSize(FontSizeConstant.SMALL);
    predefinedSizes[3] = computePredefinedSize(FontSizeConstant.MEDIUM);
    predefinedSizes[4] = computePredefinedSize(FontSizeConstant.LARGE);
    predefinedSizes[5] = computePredefinedSize(FontSizeConstant.X_LARGE);
    predefinedSizes[6] = computePredefinedSize(FontSizeConstant.XX_LARGE);

    predefinedScalingFactors = new double[7];
    predefinedScalingFactors[0] = computePredefinedScalingFactor(FontSizeConstant.XX_SMALL);
    predefinedScalingFactors[1] = computePredefinedScalingFactor(FontSizeConstant.X_SMALL);
    predefinedScalingFactors[2] = computePredefinedScalingFactor(FontSizeConstant.SMALL);
    predefinedScalingFactors[3] = computePredefinedScalingFactor(FontSizeConstant.MEDIUM);
    predefinedScalingFactors[4] = computePredefinedScalingFactor(FontSizeConstant.LARGE);
    predefinedScalingFactors[5] = computePredefinedScalingFactor(FontSizeConstant.X_LARGE);
    predefinedScalingFactors[6] = computePredefinedScalingFactor(FontSizeConstant.XX_LARGE);

    addValue(FontSizeConstant.XX_SMALL, predefinedSizes[0]);
    addValue(FontSizeConstant.X_SMALL, predefinedSizes[1]);
    addValue(FontSizeConstant.SMALL, predefinedSizes[2]);
    addValue(FontSizeConstant.MEDIUM, predefinedSizes[3]);
    addValue(FontSizeConstant.LARGE, predefinedSizes[4]);
    addValue(FontSizeConstant.X_LARGE, predefinedSizes[5]);
    addValue(FontSizeConstant.XX_LARGE, predefinedSizes[6]);
  }

  private CSSNumericValue computePredefinedSize(final CSSConstant c)
  {
    final String key = SIZE_FACTOR_PREFIX + c.getCSSText();
    final double scaling = parseDouble(key, 100);
    return CSSNumericValue.createValue(CSSNumericType.PT, fontSize * scaling / 100d);
  }

  private double computePredefinedScalingFactor(final CSSConstant c)
  {
    final String key = SIZE_FACTOR_PREFIX + c.getCSSText();
    return parseDouble(key, 100);
  }

  private double parseDouble(final String configKey, final double defaultValue)
  {
    final String value = LibLayoutBoot.getInstance().getGlobalConfig()
            .getConfigProperty(configKey);
    if (value == null)
    {
      return defaultValue;
    }
    try
    {
      return Double.parseDouble(value);
    }
    catch (NumberFormatException nfe)
    {
      return defaultValue;
    }
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
            FontStyleKeys.FONT_SIZE
    };
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
    if (value instanceof CSSConstant == false)
    {
      // fine, we're done here ...
      return ;
    }
    final CSSConstant constant = (CSSConstant) value;
    final double parentFontSize =
            currentNode.getLayoutContext().getFontSpecification().getFontSize();
    if (RelativeFontSize.LARGER.equals(value))
    {
      final double scaleFactor = getScaleLargerFactor(parentFontSize);
      layoutContext.setValue(key, CSSNumericValue.createValue(CSSNumericType.PERCENTAGE, scaleFactor));
    }
    else if (RelativeFontSize.SMALLER.equals(value))
    {
      final double scaleFactor = getScaleSmallerFactor(parentFontSize);
      layoutContext.setValue(key, CSSNumericValue.createValue(CSSNumericType.PERCENTAGE, scaleFactor));
    }

    final CSSValue resolvedValue = lookupValue(constant);
    if (resolvedValue != null)
    {
      layoutContext.setValue(key, resolvedValue);
      return;
    }
    if (key.equals(FontStyleKeys.MAX_FONT_SIZE))
    {
      // there is no upper limit if the value is invalid
      layoutContext.setValue(key, CSSNumericValue.createValue(CSSNumericType.PT, Short.MAX_VALUE));
    }
    else
    {
      // there is no lower limit if the value is invalid
      layoutContext.setValue(key, CSSNumericValue.createValue(CSSNumericType.PT, 0));
    }
  }

  public double getScaleLargerFactor(final double parentSize)
  {
    for (int i = 0; i < predefinedSizes.length; i++)
    {
      final CSSNumericValue size = predefinedSizes[i];
      if (parentSize < size.getValue())
      {
        return predefinedScalingFactors[i];
      }
    }
    return predefinedScalingFactors[6];
  }

  public double getScaleSmallerFactor(final double parentSize)
  {
    for (int i = predefinedSizes.length; i >= 0; i--)
    {
      final CSSNumericValue size = predefinedSizes[i];
      if (parentSize > size.getValue())
      {
        return predefinedScalingFactors[i];
      }
    }
    return predefinedScalingFactors[0];
  }

}
