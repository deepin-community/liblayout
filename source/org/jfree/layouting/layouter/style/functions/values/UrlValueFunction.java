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
 * $Id: UrlValueFunction.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.layouter.style.functions.values;

import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.input.style.values.CSSFunctionValue;
import org.jfree.layouting.input.style.values.CSSStringValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.layouter.model.LayoutElement;
import org.jfree.layouting.layouter.style.functions.FunctionEvaluationException;
import org.jfree.layouting.layouter.style.functions.FunctionUtilities;
import org.jfree.layouting.layouter.style.values.CSSResourceValue;

/**
 * Creation-Date: 16.04.2006, 14:14:42
 *
 * @author Thomas Morgner
 */
public class UrlValueFunction implements StyleValueFunction
{
  public UrlValueFunction()
  {
  }

  public CSSValue evaluate(final LayoutProcess layoutProcess,
                           final LayoutElement element,
                           final CSSFunctionValue function)
          throws FunctionEvaluationException
  {
    final CSSValue[] params = function.getParameters();
    if (params.length != 1)
    {
      throw new FunctionEvaluationException();
    }
    final CSSValue value = FunctionUtilities.resolveParameter(layoutProcess, element, params[0]);
    if (value instanceof CSSResourceValue)
    {
      return value;
    }
    if (value instanceof CSSStringValue == false)
    {
      throw new FunctionEvaluationException
              ("Not even remotely an URI: " + value);
    }
    final CSSStringValue strval = (CSSStringValue) value;
    return FunctionUtilities.loadResource(layoutProcess, strval.getValue());
  }
}
