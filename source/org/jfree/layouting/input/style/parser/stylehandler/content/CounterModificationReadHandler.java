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
 * $Id: CounterModificationReadHandler.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.input.style.parser.stylehandler.content;

import java.util.ArrayList;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.parser.CSSValueFactory;
import org.jfree.layouting.input.style.parser.CSSValueReadHandler;
import org.jfree.layouting.input.style.values.CSSConstant;
import org.jfree.layouting.input.style.values.CSSNumericType;
import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.input.style.values.CSSValueList;
import org.jfree.layouting.input.style.values.CSSValuePair;
import org.w3c.css.sac.LexicalUnit;

/**
 * Handles both the counter-increment and the counter-reset
 *
 * @author Thomas Morgner
 */
public class CounterModificationReadHandler implements CSSValueReadHandler
{
  public static final CSSNumericValue ZERO =
          CSSNumericValue.createValue(CSSNumericType.NUMBER, 0);

  public CounterModificationReadHandler()
  {
  }

  public CSSValue createValue(final StyleKey name, LexicalUnit value)
  {
    if (value.getLexicalUnitType() != LexicalUnit.SAC_IDENT)
    {
      return null;
    }
    final String mayBeNone = value.getStringValue();
    if ("none".equalsIgnoreCase(mayBeNone))
    {
      return new CSSConstant("none");
    }

    final ArrayList counterSpecs = new ArrayList();
    while (value != null)
    {
      if (value.getLexicalUnitType() != LexicalUnit.SAC_IDENT)
      {
        return null;
      }
      final String identifier = value.getStringValue();
      value = value.getNextLexicalUnit();
      CSSValue counterValue = ZERO;
      if (value != null)
      {
        if (value.getLexicalUnitType() == LexicalUnit.SAC_INTEGER)
        {
          counterValue = CSSNumericValue.createValue
                  (CSSNumericType.NUMBER, value.getIntegerValue());
          value = value.getNextLexicalUnit();
        }
        else if (value.getLexicalUnitType() == LexicalUnit.SAC_ATTR)
        {
          counterValue = CSSValueFactory.parseAttrFunction(value);
          value = value.getNextLexicalUnit();
        }
        else if (CSSValueFactory.isFunctionValue(value))
        {
          counterValue = CSSValueFactory.parseFunction(value);
          value = value.getNextLexicalUnit();
        }
      }
      counterSpecs.add(new CSSValuePair
              (new CSSConstant(identifier), counterValue));
    }

    return new CSSValueList(counterSpecs);
  }
}
