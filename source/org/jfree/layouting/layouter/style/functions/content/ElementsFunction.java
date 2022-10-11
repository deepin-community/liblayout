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
 * $Id: ElementsFunction.java 2739 2007-04-02 11:41:22Z taqua $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.layouter.style.functions.content;

import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.input.style.values.CSSFunctionValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.layouter.content.ContentToken;
import org.jfree.layouting.layouter.content.computed.ElementsToken;
import org.jfree.layouting.layouter.model.LayoutElement;
import org.jfree.layouting.layouter.style.functions.FunctionEvaluationException;
import org.jfree.layouting.layouter.style.functions.FunctionUtilities;

/**
 * Creation-Date: 23.10.2006, 17:50:27
 *
 * @author Thomas Morgner
 */
public class ElementsFunction implements ContentFunction
{
  public ElementsFunction()
  {
  }

  public ContentToken evaluate(final LayoutProcess layoutProcess,
                               final LayoutElement element,
                               final CSSFunctionValue function)
      throws FunctionEvaluationException
  {
    final CSSValue[] parameters = function.getParameters();
    if (parameters.length == 0)
    {
      throw new FunctionEvaluationException
          ("One parameter must be given to the 'elements' function.");
    }

    final String name = FunctionUtilities.resolveString
        (layoutProcess, element, parameters[0]);
    return new ElementsToken(name);
  }
}
