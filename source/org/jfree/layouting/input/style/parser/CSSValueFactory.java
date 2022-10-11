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
 * $Id: CSSValueFactory.java 6489 2008-11-28 14:53:40Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.input.style.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.jfree.layouting.LibLayoutBoot;
import org.jfree.layouting.input.style.CSSDeclarationRule;
import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.StyleKeyRegistry;
import org.jfree.layouting.input.style.values.CSSAttrFunction;
import org.jfree.layouting.input.style.values.CSSCompoundAttrFunction;
import org.jfree.layouting.input.style.values.CSSConstant;
import org.jfree.layouting.input.style.values.CSSFunctionValue;
import org.jfree.layouting.input.style.values.CSSInheritValue;
import org.jfree.layouting.input.style.values.CSSNumericType;
import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.input.style.values.CSSStringType;
import org.jfree.layouting.input.style.values.CSSStringValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.w3c.css.sac.LexicalUnit;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import org.pentaho.reporting.libraries.base.config.Configuration;
import org.pentaho.reporting.libraries.base.util.ObjectUtilities;

/**
 * Creation-Date: 25.11.2005, 17:43:38
 *
 * @author Thomas Morgner
 */
public class CSSValueFactory
{
  private static final Log logger = LogFactory.getLog(CSSValueFactory.class);
  public static final String SIMPLE_PREFIX = "org.jfree.layouting.parser.handlers.";
  public static final String COMPOUND_PREFIX = "org.jfree.layouting.parser.compoundhandlers.";

  private HashMap handlers;
  private HashMap compoundHandlers;
  private StyleKeyRegistry registry;

  public CSSValueFactory(final StyleKeyRegistry registry)
  {
    if (registry == null)
    {
      throw new NullPointerException();
    }
    this.registry = registry;
    this.handlers = new HashMap();
    this.compoundHandlers = new HashMap();
    this.registerDefaults();
  }


  public void registerDefaults()
  {
    final Configuration config = LibLayoutBoot.getInstance().getGlobalConfig();
    final Iterator sit = config.findPropertyKeys(SIMPLE_PREFIX);
    while (sit.hasNext())
    {
      final String key = (String) sit.next();
      final String name = key.substring(SIMPLE_PREFIX.length()).toLowerCase();
      final String c = config.getConfigProperty(key);
      final Object module =
              ObjectUtilities.loadAndInstantiate(c, CSSValueFactory.class, CSSValueReadHandler.class);
      if (module instanceof CSSValueReadHandler)
      {
        handlers.put(name, module);
      }
      else
      {
        logger.warn("Invalid module implementation: " + c);
      }
    }

    final Iterator cit = config.findPropertyKeys(COMPOUND_PREFIX);
    while (cit.hasNext())
    {
      final String key = (String) cit.next();
      final String name = key.substring(COMPOUND_PREFIX.length()).toLowerCase();
      final String c = config.getConfigProperty(key);
      final Object module =
              ObjectUtilities.loadAndInstantiate(c, CSSValueFactory.class, CSSCompoundValueReadHandler.class);
      if (module instanceof CSSCompoundValueReadHandler)
      {
        compoundHandlers.put(name, module);
      }
    }
  }


  private CSSValue createValue(final StyleKey key, final LexicalUnit value)
  {
    final CSSValueReadHandler module =
            (CSSValueReadHandler) handlers.get(key.getName());
    if (module == null)
    {
      //  || module instanceof CSSCompoundValueReadHandler
      // Compund handler are more important than simple handlers ..
      return null;
    }

    return module.createValue(key, value);
  }

  public static CSSAttrFunction parseAttrFunction(final LexicalUnit unit)
  {
    if (unit.getLexicalUnitType() != LexicalUnit.SAC_ATTR)
    {
      return null;
    }

    final String attrName = unit.getStringValue().trim();
    final String[] name = StyleSheetParserUtil.parseNamespaceIdent(attrName);
    return new CSSAttrFunction(name[0], name[1]);
  }

  public static boolean isFunctionValue(final LexicalUnit unit)
  {
    if (unit.getLexicalUnitType() != LexicalUnit.SAC_FUNCTION &&
            unit.getLexicalUnitType() != LexicalUnit.SAC_COUNTER_FUNCTION &&
            unit.getLexicalUnitType() != LexicalUnit.SAC_COUNTERS_FUNCTION &&
            unit.getLexicalUnitType() != LexicalUnit.SAC_RGBCOLOR &&
            unit.getLexicalUnitType() != LexicalUnit.SAC_RECT_FUNCTION &&
            unit.getLexicalUnitType() != LexicalUnit.SAC_FUNCTION)
    {
      return false;
    }
    return true;
  }

  private static CSSAttrFunction parseComplexAttrFn(final LexicalUnit parameters)
  {
    if (parameters == null)
    {
      return null;
    }

    final String attrName = parameters.getStringValue().trim();
    final String[] name = StyleSheetParserUtil.parseNamespaceIdent(attrName);

    final LexicalUnit afterComma = parseComma(parameters);
    if (afterComma == null)
    {
      return new CSSAttrFunction(name[0], name[1]);
    }

    final String attrType = parseAttributeType(afterComma);
    if (attrType == null)
    {
      return new CSSAttrFunction(name[0], name[1]);
    }
    else
    {
      return new CSSAttrFunction(name[0], name[1], attrType);
    }
  }

  public static CSSFunctionValue parseFunction(final LexicalUnit unit)
  {
    if (isFunctionValue(unit) == false)
    {
      return null;
    }
    LexicalUnit parameters = unit.getParameters();
    final String functionName = unit.getFunctionName();
    if (parameters == null)
    {
      // no-parameter function include the date() function...
      return new CSSFunctionValue(functionName, new CSSValue[0]);
    }
    if ("attr".equalsIgnoreCase(functionName))
    {
      return parseComplexAttrFn(unit.getParameters());
    }


    final ArrayList contentList = new ArrayList();
    while (parameters != null)
    {
      if (parameters.getLexicalUnitType() == LexicalUnit.SAC_IDENT)
      {
        contentList.add(new CSSConstant(parameters.getStringValue()));
      }
      else if (parameters.getLexicalUnitType() == LexicalUnit.SAC_STRING_VALUE)
      {
        contentList.add(new CSSStringValue(CSSStringType.STRING,
                parameters.getStringValue()));
      }
      else if (CSSValueFactory.isNumericValue(parameters))
      {
        final CSSNumericValue numericValue =
                CSSValueFactory.createNumericValue(parameters);
        if (numericValue == null)
        {
          return null;
        }
        contentList.add(numericValue);
      }
      else if (CSSValueFactory.isLengthValue(parameters))
      {
        final CSSNumericValue lengthValue =
                CSSValueFactory.createLengthValue(parameters);
        if (lengthValue == null)
        {
          return null;
        }
        contentList.add(lengthValue);
      }
      else if (parameters.getLexicalUnitType() == LexicalUnit.SAC_ATTR)
      {
        final CSSAttrFunction attrFn =
                CSSValueFactory.parseAttrFunction(parameters);
        if (attrFn == null)
        {
          return null;
        }
        contentList.add(attrFn);
      }
      else if (parameters.getLexicalUnitType() == LexicalUnit.SAC_URI)
      {
        final CSSStringValue uriValue = CSSValueFactory.createUriValue(
                parameters);
        if (uriValue == null)
        {
          return null;
        }
        contentList.add(uriValue);
      }
      else if (isFunctionValue(parameters))
      {
        final CSSFunctionValue functionValue = parseFunction(parameters);
        if (functionValue == null)
        {
          return null;
        }
        contentList.add(functionValue);
      }
      else
      {
        // parse error: Something we do not understand ...
        return null;
      }
      parameters = CSSValueFactory.parseComma(parameters);
    }
    final CSSValue[] paramVals = (CSSValue[])
            contentList.toArray(new CSSValue[contentList.size()]);
    return new CSSFunctionValue(functionName, paramVals);
  }


  private static String parseAttributeType(final LexicalUnit unit)
  {
    if (unit == null)
    {
      return null;
    }
    if (unit.getLexicalUnitType() == LexicalUnit.SAC_IDENT)
    {
      return unit.getStringValue();
    }
    return null;
  }

  private void setCompundInheritValue(final String name,
                                      final CSSDeclarationRule rule,
                                      final boolean important)
  {
    final CSSCompoundValueReadHandler handler =
            (CSSCompoundValueReadHandler) compoundHandlers.get(name);
    if (handler == null)
    {
      logger.warn("Got no key for inherited value: " + name);
      return;
    }

    final StyleKey[] keys = handler.getAffectedKeys();
    for (int i = 0; i < keys.length; i++)
    {
      final StyleKey key = keys[i];
      rule.setPropertyValue(key, CSSInheritValue.getInstance());
      rule.setImportant(key, important);
    }
  }

  private void setCompundAttrValue(final String name,
                                   final CSSAttrFunction attr,
                                   final CSSDeclarationRule rule,
                                   final boolean important)
  {


    final CSSCompoundValueReadHandler handler =
            (CSSCompoundValueReadHandler) compoundHandlers.get(name);
    if (handler == null)
    {
      logger.warn("Got no key for compound attr function: " + name);
      return;
    }

    final StyleKey[] keys = handler.getAffectedKeys();
    for (int i = 0; i < keys.length; i++)
    {
      final StyleKey key = keys[i];
      final CSSCompoundAttrFunction cattr = new CSSCompoundAttrFunction
              (name, attr.getNamespace(), attr.getName(), attr.getType());
      rule.setPropertyValue(key, cattr);
      rule.setImportant(key, important);
    }
  }


  public void parseValue(final CSSDeclarationRule rule,
                         final String name,
                         final LexicalUnit value,
                         final boolean important)
          throws CSSParserFactoryException
  {
    if (rule == null)
    {
      throw new NullPointerException("Rule given is null.");
    }

    final String normalizedName = name.toLowerCase();
    final StyleKey key = registry.findKeyByName(normalizedName);
    if (value.getLexicalUnitType() == LexicalUnit.SAC_INHERIT)
    {
      if (key == null)
      {
        setCompundInheritValue(normalizedName, rule, important);
        return;
      }
      rule.setPropertyValue(key, CSSInheritValue.getInstance());
      rule.setImportant(key, important);
      return;
    }

    if (value.getLexicalUnitType() == LexicalUnit.SAC_ATTR)
    {
      final CSSAttrFunction attrFn = parseAttrFunction(value);
      // ATTR function.
      if (attrFn != null)
      {
        if (key == null)
        {
          // Log.warn("Got no key for attribute-function " + normalizedName);
          setCompundAttrValue(normalizedName, attrFn, rule, important);
          return;
        }
        rule.setPropertyValue(key, attrFn);
        rule.setImportant(key, important);
      }
      return;
    }
    else if (isFunctionValue(value) && "attr".equals(value.getFunctionName()))
    {
      // ATTR function (extended version).
      if (key == null)
      {
        logger.warn("Got no key for attribute-function " + normalizedName);
        return;
      }
      final CSSAttrFunction attrFn = parseComplexAttrFn(value.getParameters());
      if (attrFn != null)
      {
        rule.setPropertyValue(key, attrFn);
        rule.setImportant(key, important);
      }
      return;
    }

    if (key != null)
    {
      final CSSValue cssValue = createValue(key, value);
      if (cssValue != null)
      {
        rule.setPropertyValue(key, cssValue);
        rule.setImportant(key, important);
        //Log.debug ("Got value " + key.getName() + " = " + cssValue + "(" + cssValue.getClass() + ") - (important = " + important + ")");
        return;
      }
    }

    final CSSCompoundValueReadHandler module =
            (CSSCompoundValueReadHandler) compoundHandlers.get(normalizedName);
    if (module == null)
    {
      if (key == null)
      {
        logger.info("Unknown style-key: Neither compound handlers nor single-value handers are registered for " + normalizedName);
        return;
      }

      logger.warn("Unparsable value: Got no valid result for " + normalizedName + " (" + value + ')');
      return; // ignore this rule ..
    }
    final Map map = module.createValues(value);
    if (map == null)
    {
      return;
    }
    final Iterator iterator = map.entrySet().iterator();
    while (iterator.hasNext())
    {
      final Map.Entry entry = (Map.Entry) iterator.next();
      final StyleKey entryKey = (StyleKey) entry.getKey();
      final CSSValue mapCssValue = (CSSValue) entry.getValue();

      rule.setPropertyValue(entryKey, mapCssValue);
      rule.setImportant(entryKey, important);
      //Log.debug ("Got value " + entryKey.getName() + " = " + mapCssValue + "(" + mapCssValue.getClass() + ") - (important = " + important + ")");
    }
  }

  public static CSSStringValue createUriValue(final LexicalUnit value)
  {
    if (value.getLexicalUnitType() != LexicalUnit.SAC_URI)
    {
      return null;
    }

    final String uri = value.getStringValue();
    return new CSSStringValue(CSSStringType.URI, uri);
  }

  public static boolean isNumericValue(final LexicalUnit value)
  {
    if (value.getLexicalUnitType() == LexicalUnit.SAC_INTEGER)
    {
      return true;
    }
    else if (value.getLexicalUnitType() == LexicalUnit.SAC_REAL)
    {
      return true;
    }
    return false;
  }


  public static CSSNumericValue createNumericValue(final LexicalUnit value)
  {
    if (value.getLexicalUnitType() == LexicalUnit.SAC_INTEGER)
    {
      return CSSNumericValue.createValue(CSSNumericType.NUMBER,
              value.getIntegerValue());
    }
    if (value.getLexicalUnitType() == LexicalUnit.SAC_REAL)
    {
      return CSSNumericValue.createValue(CSSNumericType.NUMBER,
              value.getFloatValue());
    }
    return null;
  }

  public static boolean isLengthValue(final LexicalUnit value)
  {
    if (value.getLexicalUnitType() == LexicalUnit.SAC_EM)
    {
      return true;
    }
    else if (value.getLexicalUnitType() == LexicalUnit.SAC_EX)
    {
      return true;
    }
    else if (value.getLexicalUnitType() == LexicalUnit.SAC_PIXEL)
    {
      return true;
    }
    else if (value.getLexicalUnitType() == LexicalUnit.SAC_INCH)
    {
      return true;
    }
    else if (value.getLexicalUnitType() == LexicalUnit.SAC_CENTIMETER)
    {
      return true;
    }
    else if (value.getLexicalUnitType() == LexicalUnit.SAC_MILLIMETER)
    {
      return true;
    }
    else if (value.getLexicalUnitType() == LexicalUnit.SAC_PICA)
    {
      return true;
    }
    else if (value.getLexicalUnitType() == LexicalUnit.SAC_POINT)
    {
      return true;
    }
    return false;
  }


  public static CSSNumericValue createLengthValue(final LexicalUnit value)
  {
    if (value.getLexicalUnitType() == LexicalUnit.SAC_INTEGER)
    {
      if (value.getFloatValue() != 0)
      {
        return null;
      }
      return CSSNumericValue.createValue(CSSNumericType.PT, 0);
    }
    if (value.getLexicalUnitType() == LexicalUnit.SAC_EM)
    {
      return CSSNumericValue.createValue(CSSNumericType.EM, value.getFloatValue());
    }
    else if (value.getLexicalUnitType() == LexicalUnit.SAC_EX)
    {
      return CSSNumericValue.createValue(CSSNumericType.EX,
              value.getFloatValue());
    }
    else if (value.getLexicalUnitType() == LexicalUnit.SAC_PIXEL)
    {
      return CSSNumericValue.createValue(CSSNumericType.PX,
              value.getFloatValue());
    }
    else if (value.getLexicalUnitType() == LexicalUnit.SAC_INCH)
    {
      return CSSNumericValue.createValue(CSSNumericType.INCH,
              value.getFloatValue());
    }
    else if (value.getLexicalUnitType() == LexicalUnit.SAC_CENTIMETER)
    {
      return CSSNumericValue.createValue(CSSNumericType.CM,
              value.getFloatValue());
    }
    else if (value.getLexicalUnitType() == LexicalUnit.SAC_MILLIMETER)
    {
      return CSSNumericValue.createValue(CSSNumericType.MM,
              value.getFloatValue());
    }
    else if (value.getLexicalUnitType() == LexicalUnit.SAC_PICA)
    {
      return CSSNumericValue.createValue(CSSNumericType.PC,
              value.getFloatValue());
    }
    else if (value.getLexicalUnitType() == LexicalUnit.SAC_POINT)
    {
      return CSSNumericValue.createValue(CSSNumericType.PT,
              value.getFloatValue());
    }
    return null;
  }

  public static LexicalUnit parseComma(final LexicalUnit value)
  {
    if (value == null)
    {
      return null;
    }

    final LexicalUnit maybeComma = value.getNextLexicalUnit();
    if (maybeComma == null)
    {
      return null;
    }
    if (maybeComma.getLexicalUnitType() == LexicalUnit.SAC_OPERATOR_COMMA)
    {
      return maybeComma.getNextLexicalUnit();
    }
    return null;
  }

}
