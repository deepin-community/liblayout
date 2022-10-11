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
 * $Id: AttrValueFunction.java 6489 2008-11-28 14:53:40Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.layouter.style.functions.values;

import java.awt.Color;
import java.net.URL;

import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.input.style.values.CSSColorValue;
import org.jfree.layouting.input.style.values.CSSFunctionValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.layouter.model.LayoutElement;
import org.jfree.layouting.layouter.style.functions.FunctionEvaluationException;
import org.jfree.layouting.layouter.style.functions.FunctionUtilities;
import org.jfree.layouting.layouter.style.values.CSSRawValue;
import org.jfree.layouting.layouter.style.values.CSSResourceValue;
import org.jfree.layouting.util.AttributeMap;
import org.jfree.layouting.util.ColorUtil;
import org.pentaho.reporting.libraries.resourceloader.ResourceKey;
import org.pentaho.reporting.libraries.resourceloader.Resource;

/**
 * Creation-Date: 15.04.2006, 18:33:56
 *
 * @author Thomas Morgner
 */
public class AttrValueFunction implements StyleValueFunction
{
  public AttrValueFunction()
  {
  }

  public CSSValue evaluate(final LayoutProcess layoutProcess,
                           final LayoutElement element,
                           final CSSFunctionValue function)
          throws FunctionEvaluationException
  {
    final CSSValue[] params = function.getParameters();
    if (params.length < 2)
    {
      throw new FunctionEvaluationException
              ("The parsed attr() function needs at least two parameters.");
    }
    final String namespace = FunctionUtilities.resolveString
            (layoutProcess, element, params[0]);
    final String name = FunctionUtilities.resolveString
            (layoutProcess, element, params[1]);

    String type = null;
    if (params.length >= 3)
    {
      type = FunctionUtilities.resolveString(layoutProcess, element, params[2]);
    }

    final AttributeMap attributes = element.getLayoutContext().getAttributes();
    if (namespace == null || "".equals(namespace))
    {
      final Object value = attributes.getAttribute
              (element.getLayoutContext().getNamespace(), name);
      return convertValue(layoutProcess, value, type);

    }
    else if ("*".equals(namespace))
    {
      // this is a lot of work. Query all attributes in all namespaces...
      final Object value = attributes.getFirstAttribute(name);
      return convertValue(layoutProcess, value, type);
    }
    else
    {
      // thats easy.
      final Object value = attributes.getAttribute
              (namespace, name);
      return convertValue(layoutProcess, value, type);
    }
  }


  private CSSValue convertValue(final LayoutProcess layoutProcess,
                                final Object value,
                                final String type)
          throws FunctionEvaluationException
  {
    if (value instanceof CSSValue)
    {
      throw new FunctionEvaluationException();
    }

    if (value instanceof String)
    {
      final String strVal = (String) value;
      if ("length".equals(type))
      {
        return FunctionUtilities.parseNumberValue(strVal);
      }
      else if ("url".equals(type))
      {
        return FunctionUtilities.loadResource(layoutProcess, strVal);
      }
      else if ("color".equals(type))
      {
        final CSSValue colorValue = ColorUtil.parseColor(strVal);
        if (colorValue == null)
        {
          throw new FunctionEvaluationException();
        }
        return colorValue;
      }
      else
      {
        // auto-mode. We check for URLs, as this is required for images
        return FunctionUtilities.parseValue(layoutProcess, strVal);
      }
    }
    else if (value instanceof URL)
    {
      return FunctionUtilities.loadResource(layoutProcess, value);
    }
    else if (value instanceof Resource)
    {
      return new CSSResourceValue((Resource) value);
    }
    else if (value instanceof ResourceKey)
    {
      return FunctionUtilities.loadResource(layoutProcess, value);
    }
    else if (value instanceof Number)
    {
      return FunctionUtilities.parseNumberValue(value.toString(), type);
    }
    else if (value instanceof Color)
    {
      final Color color = (Color) value;
      return new CSSColorValue
              (color.getRed(), color.getGreen(),
                      color.getBlue(), color.getAlpha());
    }
    else
    {
      return new CSSRawValue(value);
    }
  }

}
