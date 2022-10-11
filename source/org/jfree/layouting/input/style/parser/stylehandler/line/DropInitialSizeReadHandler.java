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
 * $Id: DropInitialSizeReadHandler.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.input.style.parser.stylehandler.line;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.parser.CSSValueFactory;
import org.jfree.layouting.input.style.parser.CSSValueReadHandler;
import org.jfree.layouting.input.style.values.CSSAutoValue;
import org.jfree.layouting.input.style.values.CSSNumericType;
import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.w3c.css.sac.LexicalUnit;

/**
 * Creation-Date: 28.11.2005, 18:38:52
 *
 * @author Thomas Morgner
 */
public class DropInitialSizeReadHandler implements CSSValueReadHandler
{
  public DropInitialSizeReadHandler()
  {
  }

  public CSSValue createValue(final StyleKey name, final LexicalUnit value)
  {
    if (value.getLexicalUnitType() == LexicalUnit.SAC_IDENT)
    {
      if ("auto".equalsIgnoreCase(value.getStringValue()))
      {
        return CSSAutoValue.getInstance();
      }
      return null;
    }
    if (value.getLexicalUnitType() == LexicalUnit.SAC_PERCENTAGE)
    {
      return CSSNumericValue.createValue(CSSNumericType.PERCENTAGE, value.getFloatValue());
    }
    if (value.getLexicalUnitType() == LexicalUnit.SAC_INTEGER)
    {
      return CSSNumericValue.createValue(CSSNumericType.NUMBER, value.getIntegerValue());
    }
    return CSSValueFactory.createLengthValue(value);
  }
}
