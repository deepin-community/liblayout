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
 * $Id: ColorResolveHandler.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.layouter.style.resolver.computed.color;

import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.keys.color.CSSSystemColors;
import org.jfree.layouting.input.style.keys.color.ColorStyleKeys;
import org.jfree.layouting.input.style.keys.color.HtmlColors;
import org.jfree.layouting.input.style.values.CSSColorValue;
import org.jfree.layouting.input.style.values.CSSConstant;
import org.jfree.layouting.input.style.values.CSSFunctionValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.layouter.model.LayoutElement;
import org.jfree.layouting.layouter.style.functions.FunctionEvaluationException;
import org.jfree.layouting.layouter.style.functions.FunctionFactory;
import org.jfree.layouting.layouter.style.functions.values.StyleValueFunction;
import org.jfree.layouting.layouter.style.resolver.ResolveHandler;
import org.jfree.layouting.util.ColorUtil;

/**
 * Creation-Date: 11.12.2005, 23:28:29
 *
 * @author Thomas Morgner
 */
public class ColorResolveHandler implements ResolveHandler
{
  public ColorResolveHandler()
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
    final LayoutContext style = currentNode.getLayoutContext();
    CSSValue value = style.getValue(key);

    if (value instanceof CSSColorValue)
    {
      return;
    }


    // it might as well be a RGB- or HSL- function.
    if (value instanceof CSSFunctionValue)
    {
      final CSSFunctionValue functionValue = (CSSFunctionValue) value;
      final StyleValueFunction function =
              FunctionFactory.getInstance().getStyleFunction
                      (functionValue.getFunctionName());
      if (function == null)
      {
        value = HtmlColors.BLACK;
      }
      else
      {
        try
        {
          value = function.evaluate(process, currentNode, functionValue);
        }
        catch (FunctionEvaluationException e)
        {
          value = HtmlColors.BLACK;
        }
      }

      if (value instanceof CSSColorValue)
      {
        style.setValue(key, value);
        return;
      }
    }


    if (value instanceof CSSConstant == false)
    {
      style.setValue(key, HtmlColors.BLACK);
      return;
    }
    if (CSSSystemColors.CURRENT_COLOR.equals(value))
    {
      style.setValue(key, getCurrentColor(currentNode));
      return;
    }

    final CSSValue c = ColorUtil.parseIdentColor(value.getCSSText());
    if (c != null)
    {
      style.setValue(key, c);
    }
    else
    {
      style.setValue(key, HtmlColors.BLACK);
    }
  }

  protected CSSColorValue getCurrentColor (final LayoutElement currentNode)
  {
    final LayoutElement parent = currentNode.getParent();
    if (parent != null)
    {
      final LayoutContext layoutContext = parent.getLayoutContext();
      final CSSValue value = layoutContext.getValue(ColorStyleKeys.COLOR);
      if (value instanceof CSSColorValue)
      {
        return (CSSColorValue) value;
      }
    }
    return (HtmlColors.BLACK);
  }

}
