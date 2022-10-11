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
 * $Id: DefaultBoxDefinitionFactory.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.renderer.model;

import org.jfree.layouting.input.style.keys.border.BorderStyleKeys;
import org.jfree.layouting.input.style.keys.box.BoxStyleKeys;
import org.jfree.layouting.input.style.keys.color.CSSSystemColors;
import org.jfree.layouting.input.style.values.CSSAutoValue;
import org.jfree.layouting.input.style.values.CSSColorValue;
import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.output.OutputProcessorMetaData;
import org.jfree.layouting.renderer.border.Border;
import org.jfree.layouting.renderer.border.BorderFactory;
import org.jfree.layouting.renderer.border.RenderLength;

/**
 * Creation-Date: 25.06.2006, 15:46:01
 *
 * @author Thomas Morgner
 */
public class DefaultBoxDefinitionFactory implements BoxDefinitionFactory
{
  private BorderFactory borderFactory;

  public DefaultBoxDefinitionFactory(final BorderFactory borderFactory)
  {
    this.borderFactory = borderFactory;
  }


  public BoxDefinition createBlockBoxDefinition(final LayoutContext boxContext,
                                                final OutputProcessorMetaData metaData)
  {
    final Border border = borderFactory.createBorder(boxContext, metaData);
    final DefaultBoxDefinition boxDefinition = new DefaultBoxDefinition();
    boxDefinition.setBorder(border);
    final CSSValue value = boxContext.getValue(BorderStyleKeys.BACKGROUND_COLOR);
    if (value instanceof CSSColorValue)
    {
      boxDefinition.setBackgroundColor((CSSColorValue) value);
    }
    else
    {
      boxDefinition.setBackgroundColor(CSSSystemColors.TRANSPARENT);
    }

    fillHorizontalPadding(boxDefinition, boxContext, metaData);

    boxDefinition.setPreferredWidth(computeWidth
            (boxContext.getValue(BoxStyleKeys.WIDTH), boxContext, metaData, true, true));
    boxDefinition.setMarginLeft(computeWidth
            (boxContext.getValue(BoxStyleKeys.MARGIN_LEFT), boxContext, metaData, true, true));
    boxDefinition.setMarginRight(computeWidth
            (boxContext.getValue(BoxStyleKeys.MARGIN_RIGHT), boxContext, metaData, true, true));

    fillVerticalModel(boxDefinition, boxContext, metaData);

    return boxDefinition;
  }

  public BoxDefinition createInlineBoxDefinition(final LayoutContext boxContext,
                                                 final OutputProcessorMetaData metaData)
  {
    final Border border = borderFactory.createBorder(boxContext, metaData);
    final DefaultBoxDefinition boxDefinition = new DefaultBoxDefinition();
    boxDefinition.setBorder(border);
    final CSSValue value = boxContext.getValue(BorderStyleKeys.BACKGROUND_COLOR);
    if (value instanceof CSSColorValue)
    {
      boxDefinition.setBackgroundColor((CSSColorValue) value);
    }
    else
    {
      boxDefinition.setBackgroundColor(CSSSystemColors.TRANSPARENT);
    }

    fillHorizontalPadding(boxDefinition, boxContext, metaData);

    // inline-elements have no way to define the width. (10.3.1 of CSS2.1)
    // exception: inline-block elements can and will have a width.
    // we move that evaluation into the layouter, that one may or may not
    // use the preferred width ..
    boxDefinition.setPreferredWidth(computeWidth
            (boxContext.getValue(BoxStyleKeys.WIDTH), boxContext, metaData, true, true));

    boxDefinition.setMarginLeft(computeWidth
            (boxContext.getValue(BoxStyleKeys.MARGIN_LEFT), boxContext, metaData, false, true));
    boxDefinition.setMarginRight(computeWidth
            (boxContext.getValue(BoxStyleKeys.MARGIN_RIGHT), boxContext, metaData, false, true));

    // second, the vertical model.
    fillVerticalModel(boxDefinition, boxContext, metaData);

    return boxDefinition;
  }

  private void fillVerticalModel(final DefaultBoxDefinition boxDefinition,
                                 final LayoutContext boxContext,
                                 final OutputProcessorMetaData metaData)
  {
    boxDefinition.setPaddingTop(computeWidth
            (boxContext.getValue(BoxStyleKeys.PADDING_TOP), boxContext, metaData, false, false));
    boxDefinition.setPaddingBottom(computeWidth
            (boxContext.getValue(BoxStyleKeys.PADDING_BOTTOM), boxContext, metaData, false, false));

    boxDefinition.setMarginTop(computeWidth
            (boxContext.getValue(BoxStyleKeys.MARGIN_TOP), boxContext, metaData, false, true));
    boxDefinition.setMarginBottom(computeWidth
            (boxContext.getValue(BoxStyleKeys.MARGIN_BOTTOM), boxContext, metaData, false, true));

    // I dont believe in Voodoo, therefore I dont follow Section 10.6.3 on the
    // height computation. Auto-Values are now generally accepted, if no length
    // has been specified explicitly. In that case we simply compute what ever
    // comes in and do not overflow in any case.
    boxDefinition.setPreferredHeight(computeWidth
            (boxContext.getValue(BoxStyleKeys.HEIGHT), boxContext, metaData, true, true));
  }

  private void fillHorizontalPadding(final DefaultBoxDefinition boxDefinition,
                                     final LayoutContext boxContext,
                                     final OutputProcessorMetaData metaData)
  {
    // first, the horizontal model.
    boxDefinition.setPaddingLeft(computeWidth
            (boxContext.getValue(BoxStyleKeys.PADDING_LEFT),
                    boxContext, metaData, false, false));
    boxDefinition.setPaddingRight(computeWidth
            (boxContext.getValue(BoxStyleKeys.PADDING_RIGHT),
                    boxContext, metaData, false, false));
    boxDefinition.setMaximumWidth(computeWidth
            (boxContext.getValue(BoxStyleKeys.MAX_WIDTH),
                    boxContext, metaData, false, false));
    boxDefinition.setMaximumHeight(computeWidth
            (boxContext.getValue(BoxStyleKeys.MAX_HEIGHT),
                    boxContext, metaData, false, false));
    boxDefinition.setMinimumWidth(computeWidth
            (boxContext.getValue(BoxStyleKeys.MIN_WIDTH),
                    boxContext, metaData, false, false));
    boxDefinition.setMinimumHeight(computeWidth
            (boxContext.getValue(BoxStyleKeys.MIN_HEIGHT),
                    boxContext, metaData, false, false));
  }

  public static RenderLength computeWidth(final CSSValue widthValue,
                                    final LayoutContext boxContext,
                                    final OutputProcessorMetaData metaData,
                                    final boolean allowAuto,
                                    final boolean allowNegativeValues)
  {
    if (allowAuto && CSSAutoValue.getInstance().equals(widthValue))
    {
      return RenderLength.AUTO;
    }
    else if (widthValue instanceof CSSNumericValue == false)
    {
      return RenderLength.EMPTY;
    }
    else
    {
      final CSSNumericValue nval = (CSSNumericValue) widthValue;
      if (nval.getValue() < 0 && allowNegativeValues == true)
      {
        return RenderLength.convertToInternal(widthValue, boxContext, metaData);
      }
      else if (nval.getValue() > 0)
      {
        final RenderLength renderLength = RenderLength.convertToInternal(widthValue, boxContext, metaData);
        if (renderLength.getValue() > 0)
        {
          return renderLength;
        }
      }
      return RenderLength.EMPTY;
    }
  }


}
