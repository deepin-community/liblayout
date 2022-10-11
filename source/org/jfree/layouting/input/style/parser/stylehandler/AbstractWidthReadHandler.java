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
 * $Id: AbstractWidthReadHandler.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.input.style.parser.stylehandler;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.parser.CSSValueFactory;
import org.jfree.layouting.input.style.parser.CSSValueReadHandler;
import org.jfree.layouting.input.style.values.CSSAutoValue;
import org.jfree.layouting.input.style.values.CSSNumericType;
import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.w3c.css.sac.LexicalUnit;

/**
 * Creation-Date: 27.11.2005, 21:16:17
 *
 * @author Thomas Morgner
 */
public abstract class AbstractWidthReadHandler implements CSSValueReadHandler
{
  private boolean allowPercentages;
  private boolean allowAuto;

  protected AbstractWidthReadHandler(final boolean allowPercentages,
                                     final boolean allowAuto)
  {
    this.allowPercentages = allowPercentages;
    this.allowAuto = allowAuto;
  }

  public boolean isAllowPercentages()
  {
    return allowPercentages;
  }

  public boolean isAllowAuto()
  {
    return allowAuto;
  }

  public CSSValue createValue(final StyleKey name, final LexicalUnit value)
  {
    return parseWidth(value);
  }

  protected CSSValue parseWidth(final LexicalUnit value)
  {
    if (allowPercentages &&
            value.getLexicalUnitType() == LexicalUnit.SAC_PERCENTAGE)
    {
      return CSSNumericValue.createValue(CSSNumericType.PERCENTAGE,
              value.getFloatValue());
    }
    else if (allowAuto &&
            value.getLexicalUnitType() == LexicalUnit.SAC_IDENT)
    {
      if ("auto".equalsIgnoreCase(value.getStringValue()))
      {
        return CSSAutoValue.getInstance();
      }
      return null;
    }
    else
    {
      return CSSValueFactory.createLengthValue(value);
    }
  }
}
