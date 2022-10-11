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
 * $Id: HslValueFunction.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.layouter.style.functions.values;

import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.input.style.values.CSSColorValue;
import org.jfree.layouting.input.style.values.CSSFunctionValue;
import org.jfree.layouting.input.style.values.CSSNumericType;
import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.input.style.values.CSSStringValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.layouter.model.LayoutElement;
import org.jfree.layouting.layouter.style.functions.FunctionEvaluationException;
import org.jfree.layouting.layouter.style.functions.FunctionUtilities;
import org.jfree.layouting.util.ColorUtil;

/**
 * Creation-Date: 16.04.2006, 14:14:42
 *
 * @author Thomas Morgner
 */
public class HslValueFunction implements StyleValueFunction
{
  public HslValueFunction()
  {
  }

  protected int validateHueParameter (final CSSValue value) throws FunctionEvaluationException
  {
    final CSSNumericValue nval;
    if (value instanceof CSSStringValue)
    {
      // I shouldn't do this, but ..
      final CSSStringValue strVal = (CSSStringValue) value;
      nval = FunctionUtilities.parseNumberValue(strVal.getValue());
    }
    else if (value instanceof CSSNumericValue == false)
    {
      throw new FunctionEvaluationException("Expected a number");
    }
    else
    {
      nval = (CSSNumericValue) value;
    }
    if (nval.getType().equals(CSSNumericType.NUMBER))
    {
      return (int) (nval.getValue() % 360);
    }
    throw new FunctionEvaluationException("Expected a number, not a length");
  }

  protected float validateOtherParameter (final CSSValue value) throws FunctionEvaluationException
  {
    final CSSNumericValue nval;
    if (value instanceof CSSStringValue)
    {
      // I shouldn't do this, but ..
      final CSSStringValue strVal = (CSSStringValue) value;
      nval = FunctionUtilities.parseNumberValue(strVal.getValue());
    }
    else if (value instanceof CSSNumericValue == false)
    {
      throw new FunctionEvaluationException("Expected a number");
    }
    else
    {
      nval = (CSSNumericValue) value;
    }
    if (nval.getType().equals(CSSNumericType.PERCENTAGE))
    {
      return (float) (nval.getValue() % 100);
    }
    throw new FunctionEvaluationException("Expected a number, not a length");
  }


  public CSSValue evaluate(final LayoutProcess layoutProcess,
                           final LayoutElement element,
                           final CSSFunctionValue function)
          throws FunctionEvaluationException
  {

    final CSSValue[] values = function.getParameters();
    if (values.length == 3)
    {
      final int hueValue = validateHueParameter
          (FunctionUtilities.resolveParameter(layoutProcess, element, values[0]));
      final float saturationValue = validateOtherParameter
          (FunctionUtilities.resolveParameter(layoutProcess, element, values[1]));
      final float lightValue = validateOtherParameter
          (FunctionUtilities.resolveParameter(layoutProcess, element, values[2]));
      final float[] rgb = ColorUtil.hslToRGB
              (hueValue, saturationValue, lightValue);
      return new CSSColorValue(rgb[0], rgb[1], rgb[2]);
    }
    else if (values.length == 4)
    {
      final int hueValue = validateHueParameter
          (FunctionUtilities.resolveParameter(layoutProcess, element, values[0]));
      final float saturationValue = validateOtherParameter
          (FunctionUtilities.resolveParameter(layoutProcess, element, values[1]));
      final float lightValue = validateOtherParameter
          (FunctionUtilities.resolveParameter(layoutProcess, element, values[2]));
      final float alphaValue = validateOtherParameter
          (FunctionUtilities.resolveParameter(layoutProcess, element, values[3]));
      final float[] rgb =
              ColorUtil.hslToRGB(hueValue, saturationValue, lightValue);
      return new CSSColorValue(rgb[0], rgb[1], rgb[2], alphaValue);
    }
    else
    {
      throw new FunctionEvaluationException("Expected either three or four parameters.");
    }
  }
}
