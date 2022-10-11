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
 * $Id: TextIndentReadHandler.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.input.style.parser.stylehandler.text;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.parser.CSSValueFactory;
import org.jfree.layouting.input.style.parser.CSSValueReadHandler;
import org.jfree.layouting.input.style.values.CSSConstant;
import org.jfree.layouting.input.style.values.CSSNumericType;
import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.input.style.values.CSSValueList;
import org.w3c.css.sac.LexicalUnit;

/**
 * Creation-Date: 02.12.2005, 19:13:31
 *
 * @author Thomas Morgner
 */
public class TextIndentReadHandler implements CSSValueReadHandler
{
  public TextIndentReadHandler()
  {
  }

  public CSSValue createValue(final StyleKey name, LexicalUnit value)
  {

    CSSValue cssvalue = null;
    if (value.getLexicalUnitType() == LexicalUnit.SAC_PERCENTAGE)
    {
      cssvalue = CSSNumericValue.createValue(CSSNumericType.PERCENTAGE, value.getFloatValue());
    }
    else
    {
      cssvalue = CSSValueFactory.createLengthValue(value);
    }

    value = value.getNextLexicalUnit();
    if (value != null)
    {
      if (value.getLexicalUnitType() != LexicalUnit.SAC_IDENT)
      {
        return null;
      }
      if ("hanging".equalsIgnoreCase(value.getStringValue()))
      {
        return new CSSValueList (new CSSValue[]{cssvalue, new CSSConstant("hanging")});
      }
      else
      {
        return null;
      }
    }

    return new CSSValueList (new CSSValue[]{cssvalue});
  }
}
