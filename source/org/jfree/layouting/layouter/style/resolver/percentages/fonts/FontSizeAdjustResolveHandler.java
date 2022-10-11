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
 * $Id: FontSizeAdjustResolveHandler.java 6489 2008-11-28 14:53:40Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.layouter.style.resolver.percentages.fonts;

import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.util.geom.StrictGeomUtility;
import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.keys.font.FontStyleKeys;
import org.jfree.layouting.input.style.values.CSSNumericType;
import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.layouter.context.FontSpecification;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.layouter.model.LayoutElement;
import org.jfree.layouting.layouter.style.resolver.ResolveHandler;
import org.pentaho.reporting.libraries.fonts.registry.FontMetrics;

/**
 * Creation-Date: 18.12.2005, 19:46:43
 *
 * @author Thomas Morgner
 */
public class FontSizeAdjustResolveHandler implements ResolveHandler
{
  public FontSizeAdjustResolveHandler()
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
    return new StyleKey[]{
        FontStyleKeys.FONT_SIZE,
        FontStyleKeys.FONT_FAMILY,
        FontStyleKeys.FONT_EFFECT,
        FontStyleKeys.FONT_SMOOTH,
        FontStyleKeys.FONT_STRETCH,
        FontStyleKeys.FONT_VARIANT,
        FontStyleKeys.FONT_WEIGHT,
    };
  }

  public void resolve(final LayoutProcess process,
                      final LayoutElement currentNode,
                      final StyleKey key)
  {
    final LayoutContext layoutContext = currentNode.getLayoutContext();
    final CSSValue value = layoutContext.getValue(key);
    if (value instanceof CSSNumericValue == false)
    {
      return; // do nothing
    }
    final CSSNumericValue nval = (CSSNumericValue) value;
    if (CSSNumericType.NUMBER.equals(nval.getType()) == false)
    {
      return; // syntax error, do nothing
    }
    final LayoutElement parent = currentNode.getParent();
    if (parent == null)
    {
      return; // no parent to resolve against ...
    }

    final double adjustFactor = nval.getValue();
    final FontSpecification fontSpecification =
        currentNode.getLayoutContext().getFontSpecification();
    final FontMetrics fontMetrics =
        process.getOutputMetaData().getFontMetrics(fontSpecification);
    if (fontMetrics == null)
    {
      return; // no font metrics means no valid font...
    }

    final double actualFontXHeight = StrictGeomUtility.toExternalValue(fontMetrics.getXHeight());

    final double fontSize = fontSpecification.getFontSize();
    final double aspectRatio = actualFontXHeight / fontSize;
    final double result = (fontSize * (adjustFactor / aspectRatio));
    fontSpecification.setFontSize(result);
  }
}
