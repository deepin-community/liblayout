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
 * $Id: InlineBoxAlignReadHandler.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.input.style.parser.stylehandler.line;

import org.jfree.layouting.input.style.keys.line.InlineBoxAlign;
import org.jfree.layouting.input.style.parser.stylehandler.OneOfConstantsReadHandler;
import org.jfree.layouting.input.style.values.CSSNumericType;
import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.w3c.css.sac.LexicalUnit;

/**
 * Warning: This *is* a compound property, but one if its values depend on
 * the element structure and it changes its meaning if used in Tables.
 *
 * @author Thomas Morgner
 */
public class InlineBoxAlignReadHandler extends OneOfConstantsReadHandler
{
  public InlineBoxAlignReadHandler()
  {
    super(false);
    addValue(InlineBoxAlign.INITIAL);
    addValue(InlineBoxAlign.LAST);
  }

  protected CSSValue lookupValue(final LexicalUnit value)
  {
    final CSSValue constant = super.lookupValue(value);
    if (constant != null)
    {
      return constant;
    }
    else if (value.getLexicalUnitType() == LexicalUnit.SAC_INTEGER)
    {
      return CSSNumericValue.createValue(CSSNumericType.NUMBER,
              value.getIntegerValue());
    }

    return null;

  }
}
