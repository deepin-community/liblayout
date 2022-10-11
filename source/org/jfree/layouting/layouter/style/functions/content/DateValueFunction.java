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
 * $Id: DateValueFunction.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.layouter.style.functions.content;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.jfree.layouting.DocumentContextUtility;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.input.style.values.CSSFunctionValue;
import org.jfree.layouting.input.style.values.CSSStringValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.layouter.content.ContentToken;
import org.jfree.layouting.layouter.content.statics.FormattedContentToken;
import org.jfree.layouting.layouter.i18n.LocalizationContext;
import org.jfree.layouting.layouter.model.LayoutElement;
import org.jfree.layouting.layouter.style.functions.FunctionEvaluationException;

/**
 * Creation-Date: 15.04.2006, 18:33:56
 *
 * @author Thomas Morgner
 */
public class DateValueFunction implements ContentFunction
{
  public DateValueFunction()
  {
  }

  public ContentToken evaluate(final LayoutProcess layoutProcess,
                               final LayoutElement element,
                               final CSSFunctionValue function)
          throws FunctionEvaluationException
  {

    final Date date = DocumentContextUtility.getDate
            (layoutProcess.getDocumentContext());
    final CSSValue[] parameters = function.getParameters();
    final LocalizationContext localizationContext =
            DocumentContextUtility.getLocalizationContext
                    (layoutProcess.getDocumentContext());

    final DateFormat format = getDateFormat
            (parameters, localizationContext,
                    element.getLayoutContext().getLanguage());
    return new FormattedContentToken(date, format, format.format(date));
  }

  private DateFormat getDateFormat(final CSSValue[] parameters,
                                   final LocalizationContext localizationContext,
                                   final Locale locale)
  {
    if (parameters.length < 1)
    {
      return localizationContext.getDateFormat(locale);
    }

    final CSSValue formatValue = parameters[0];
    if (formatValue instanceof CSSStringValue == false)
    {
      return localizationContext.getDateFormat(locale);
    }

    final CSSStringValue sval = (CSSStringValue) formatValue;
    final DateFormat format = localizationContext.getDateFormat
            (sval.getValue(), locale);
    if (format != null)
    {
      return format;
    }
    return localizationContext.getDateFormat(locale);
  }


}
