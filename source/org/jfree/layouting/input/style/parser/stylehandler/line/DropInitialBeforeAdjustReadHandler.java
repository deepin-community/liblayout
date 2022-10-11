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
 * $Id: DropInitialBeforeAdjustReadHandler.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.input.style.parser.stylehandler.line;

import org.jfree.layouting.input.style.keys.line.DropInitialBeforeAdjust;
import org.jfree.layouting.input.style.parser.CSSValueFactory;
import org.jfree.layouting.input.style.parser.stylehandler.OneOfConstantsReadHandler;
import org.jfree.layouting.input.style.values.CSSNumericType;
import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.w3c.css.sac.LexicalUnit;

/**
 * Creation-Date: 28.11.2005, 18:12:27
 *
 * @author Thomas Morgner
 */
public class DropInitialBeforeAdjustReadHandler extends OneOfConstantsReadHandler
{
  public DropInitialBeforeAdjustReadHandler()
  {
    super(true);
    addValue(DropInitialBeforeAdjust.BEFORE_EDGE);
    addValue(DropInitialBeforeAdjust.TEXT_BEFORE_EDGE);
    addValue(DropInitialBeforeAdjust.CENTRAL);
    addValue(DropInitialBeforeAdjust.HANGING);
    addValue(DropInitialBeforeAdjust.MATHEMATICAL);
    addValue(DropInitialBeforeAdjust.MIDDLE);
  }

  protected CSSValue lookupValue(final LexicalUnit value)
  {
    final CSSValue constant = super.lookupValue(value);
    if (constant != null)
    {
      return constant;
    }
    else if (value.getLexicalUnitType() == LexicalUnit.SAC_PERCENTAGE)
    {
      return CSSNumericValue.createValue(CSSNumericType.PERCENTAGE,
              value.getFloatValue());
    }

    return CSSValueFactory.createLengthValue(value);

  }
}
