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
 * $Id: FormatValueFunction.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.layouter.style.functions.content;

import java.text.Format;
import java.util.Date;

import org.jfree.layouting.DocumentContextUtility;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.input.style.values.CSSFunctionValue;
import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.input.style.values.CSSStringValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.layouter.content.ContentToken;
import org.jfree.layouting.layouter.content.statics.FormattedContentToken;
import org.jfree.layouting.layouter.content.statics.StaticTextToken;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.layouter.i18n.LocalizationContext;
import org.jfree.layouting.layouter.model.LayoutElement;
import org.jfree.layouting.layouter.style.functions.FunctionEvaluationException;
import org.jfree.layouting.layouter.style.functions.FunctionUtilities;
import org.jfree.layouting.layouter.style.values.CSSFormatedValue;
import org.jfree.layouting.layouter.style.values.CSSRawValue;

/**
 * Creation-Date: 16.04.2006, 14:14:42
 *
 * @author Thomas Morgner
 */
public class FormatValueFunction implements ContentFunction
{
  public FormatValueFunction()
  {
  }

  // takes two or three parameters.
  // param1: What shall we format
  // param2: What data do we expect
  // param3: What format string shall we use

  public ContentToken evaluate(final LayoutProcess layoutProcess,
                               final LayoutElement element,
                               final CSSFunctionValue function)
          throws FunctionEvaluationException
  {
    final CSSValue[] params = function.getParameters();
    if (params.length < 2)
    {
      throw new FunctionEvaluationException("Illegal parameter count");
    }
    final CSSValue rawValue = FunctionUtilities.resolveParameter(layoutProcess, element, params[0]);
    final String typeValue = FunctionUtilities.resolveString(layoutProcess, element, params[1]);
    final LocalizationContext localizationContext =
            DocumentContextUtility.getLocalizationContext
                    (layoutProcess.getDocumentContext());
    final LayoutContext layoutContext = element.getLayoutContext();
    if ("date".equals(typeValue))
    {
      if (params.length < 3)
      {
        return getDateValue(rawValue,
                localizationContext.getDateFormat(layoutContext.getLanguage()));
      }
      else
      {
        final String format = FunctionUtilities.resolveString(layoutProcess, element, params[2]);
        return getDateValue(rawValue,
                localizationContext.getDateFormat(format, layoutContext.getLanguage()));
      }
    }
    else if ("time".equals(typeValue))
    {
      if (params.length != 2)
      {
        throw new FunctionEvaluationException();
      }
      return getDateValue(rawValue,
              localizationContext.getTimeFormat(layoutContext.getLanguage()));
    }
    else if ("number".equals(typeValue))
    {
      if (params.length < 3)
      {
        return getNumberValue(rawValue,
                localizationContext.getDateFormat(layoutContext.getLanguage()));
      }
      else
      {
        final String format = FunctionUtilities.resolveString(layoutProcess, element, params[2]);
        return getNumberValue(rawValue,
                localizationContext.getNumberFormat(format, layoutContext.getLanguage()));
      }
    }
    else if ("integer".equals(typeValue))
    {
      if (params.length != 2)
      {
        throw new FunctionEvaluationException();
      }
      return getNumberValue(rawValue,
              localizationContext.getIntegerFormat(layoutContext.getLanguage()));
    }
    throw new FunctionEvaluationException("FormatType not recognized");
  }


  private ContentToken getNumberValue
          (final CSSValue rawValue,
           final Format format)
          throws FunctionEvaluationException
  {


    final double number;
    if (rawValue instanceof CSSStringValue)
    {
      final CSSStringValue strVal = (CSSStringValue) rawValue;
      try
      {
        final CSSNumericValue nval = FunctionUtilities.parseNumberValue(strVal.getValue());
        number = nval.getValue();
      }
      catch(FunctionEvaluationException fee)
      {
        return new StaticTextToken (strVal.getValue());
      }
    }
    else if (rawValue instanceof CSSNumericValue)
    {
      final CSSNumericValue nval = (CSSNumericValue) rawValue;
      number = nval.getValue();
    }
    else
    {
      // Raw-Values should not have been created for number values
      throw new FunctionEvaluationException("Not a numeric value.");
    }

    final Double obj = new Double(number);
    return new FormattedContentToken
            (obj, format, format.format(obj));
  }

  private FormattedContentToken getDateValue
          (final CSSValue rawValue,
           final Format format)
          throws FunctionEvaluationException
  {
    final Date date;
    if (rawValue instanceof CSSRawValue)
    {
      final CSSRawValue cssRawValue = (CSSRawValue) rawValue;
      final Object o = cssRawValue.getValue();
      if (o instanceof Date == false)
      {
        throw new FunctionEvaluationException("Not a date value.");
      }
      date = (Date) o;
    }
    else if (rawValue instanceof CSSFormatedValue)
    {
      final CSSFormatedValue fval = (CSSFormatedValue) rawValue;
      final Object o = fval.getRaw();
      if (o instanceof Date == false)
      {
        throw new FunctionEvaluationException("Not a date value.");
      }
      date = (Date) o;
    }
    else
    {
      throw new FunctionEvaluationException("Not a date value.");
    }

    return new FormattedContentToken
            (date, format, format.format(date));
  }
}
