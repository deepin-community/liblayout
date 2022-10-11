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
 * $Id: CountersValueFunction.java 2739 2007-04-02 11:41:22Z taqua $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.layouter.style.functions.content;

import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.input.style.values.CSSFunctionValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.input.style.keys.list.ListStyleKeys;
import org.jfree.layouting.layouter.content.ContentToken;
import org.jfree.layouting.layouter.content.computed.CountersToken;
import org.jfree.layouting.layouter.context.DocumentContext;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.layouter.counters.CounterStyle;
import org.jfree.layouting.layouter.counters.CounterStyleFactory;
import org.jfree.layouting.layouter.model.LayoutElement;
import org.jfree.layouting.layouter.style.functions.FunctionEvaluationException;
import org.jfree.layouting.layouter.style.functions.FunctionUtilities;

/**
 * Creation-Date: 16.04.2006, 15:22:11
 *
 * @author Thomas Morgner
 */
public class CountersValueFunction implements ContentFunction
{
  public CountersValueFunction()
  {
  }

  public ContentToken evaluate(final LayoutProcess layoutProcess,
                               final LayoutElement element,
                               final CSSFunctionValue function)
          throws FunctionEvaluationException
  {
    // Accepts one or two parameters ...
    final CSSValue[] params = function.getParameters();
    if (params.length < 2)
    {
      throw new FunctionEvaluationException();
    }
    final String counterName =
            FunctionUtilities.resolveString(layoutProcess, element, params[0]);
    final String separator =
            FunctionUtilities.resolveString(layoutProcess, element, params[1]);

    CounterStyle cstyle = null;
    if (params.length > 2)
    {
      final String styleName =
              FunctionUtilities.resolveString(layoutProcess, element, params[2]);
      cstyle = CounterStyleFactory.getInstance().getCounterStyle(styleName);
    }

    if (cstyle == null)
    {
      final DocumentContext documentContext = layoutProcess.getDocumentContext();
      cstyle = documentContext.getCounterStyle(counterName);
    }

    return new CountersToken(counterName, separator, cstyle);
  }

}
