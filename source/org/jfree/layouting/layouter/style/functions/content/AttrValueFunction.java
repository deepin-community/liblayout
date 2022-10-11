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
package org.jfree.layouting.layouter.style.functions.content;

import java.net.URL;
import java.util.Date;

import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.input.style.values.CSSFunctionValue;
import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.input.style.values.CSSStringValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.layouter.content.ContentToken;
import org.jfree.layouting.layouter.content.statics.ExternalContentToken;
import org.jfree.layouting.layouter.content.statics.ResourceContentToken;
import org.jfree.layouting.layouter.content.statics.StaticTextToken;
import org.jfree.layouting.layouter.model.LayoutElement;
import org.jfree.layouting.layouter.style.functions.FunctionEvaluationException;
import org.jfree.layouting.layouter.style.functions.FunctionUtilities;
import org.jfree.layouting.layouter.style.values.CSSResourceValue;
import org.jfree.layouting.util.AttributeMap;
import org.jfree.layouting.util.ColorUtil;
import org.pentaho.reporting.libraries.resourceloader.Resource;
import org.pentaho.reporting.libraries.resourceloader.ResourceKey;

/**
 * Creation-Date: 15.04.2006, 18:33:56
 *
 * @author Thomas Morgner
 */
public class AttrValueFunction implements ContentFunction
{
  public AttrValueFunction()
  {
  }

  public ContentToken evaluate(final LayoutProcess layoutProcess,
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


  private ContentToken convertValue(final LayoutProcess layoutProcess,
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
        final CSSNumericValue cssNumericValue =
                FunctionUtilities.parseNumberValue(strVal);
        return new StaticTextToken (cssNumericValue.getCSSText());
      }
      else if ("url".equals(type))
      {
        final CSSResourceValue cssResourceValue =
                FunctionUtilities.loadResource(layoutProcess, strVal);
        final Resource resource = cssResourceValue.getValue();
        return new ResourceContentToken(resource);
      }
      else if ("color".equals(type))
      {
        final CSSValue colorValue = ColorUtil.parseColor(strVal);
        if (colorValue == null)
        {
          throw new FunctionEvaluationException();
        }
        return new StaticTextToken (colorValue.getCSSText());
      }
      else
      {
        // auto-mode. We check for URLs, as this is required for images
        final CSSValue cssValue =
                FunctionUtilities.parseValue(layoutProcess, strVal);
        if (cssValue instanceof CSSResourceValue)
        {
          final CSSResourceValue cssResourceValue =
                  (CSSResourceValue) cssValue;
          final Resource resource = cssResourceValue.getValue();
          return new ResourceContentToken(resource);
        }
        else if (cssValue instanceof CSSStringValue)
        {
          final CSSStringValue sval = (CSSStringValue) cssValue;
          return new StaticTextToken (sval.getValue());
        }
        else
        {
          return new StaticTextToken (cssValue.getCSSText());
        }
      }
    }
    else if (value instanceof URL)
    {
      final CSSResourceValue cssResourceValue =
              FunctionUtilities.loadResource(layoutProcess, value);
      final Resource resource = cssResourceValue.getValue();
      return new ResourceContentToken(resource);
    }
    else if (value instanceof Resource)
    {
      return new ResourceContentToken((Resource) value);
    }
    else if (value instanceof ResourceKey)
    {
      final CSSResourceValue cssResourceValue =
              FunctionUtilities.loadResource(layoutProcess, value);
      final Resource resource = cssResourceValue.getValue();
      return new ResourceContentToken(resource);
    }
    else if (value instanceof Date)
    {
      return new StaticTextToken (String.valueOf(value));
    }
    else if (value instanceof Number)
    {
      return new StaticTextToken (String.valueOf(value));
    }
    else
    {
      return new ExternalContentToken (value);
    }
  }

}
