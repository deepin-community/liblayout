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
 * $Id: MoveToReadHandler.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.input.style.parser.stylehandler.content;

import org.jfree.layouting.input.style.keys.content.MoveToValues;
import org.jfree.layouting.input.style.parser.stylehandler.OneOfConstantsReadHandler;
import org.jfree.layouting.input.style.values.CSSConstant;
import org.jfree.layouting.input.style.values.CSSValue;
import org.w3c.css.sac.LexicalUnit;

/**
 * Creation-Date: 01.12.2005, 17:53:49
 *
 * @author Thomas Morgner
 */
public class MoveToReadHandler extends OneOfConstantsReadHandler
{
  public MoveToReadHandler()
  {
    super(false);
    addValue(MoveToValues.HERE);
    addValue(MoveToValues.NORMAL);
  }

  protected CSSValue lookupValue(final LexicalUnit value)
  {
    final CSSValue content = super.lookupValue(value);
    if (content != null)
    {
      return content;
    }

    if (value.getLexicalUnitType() != LexicalUnit.SAC_IDENT)
    {
      return null;
    }
    return new CSSConstant(value.getStringValue());
  }
}
