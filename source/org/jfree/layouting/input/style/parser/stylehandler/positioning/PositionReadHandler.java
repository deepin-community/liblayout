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
 * $Id: PositionReadHandler.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.input.style.parser.stylehandler.positioning;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.keys.positioning.Position;
import org.jfree.layouting.input.style.parser.CSSValueFactory;
import org.jfree.layouting.input.style.parser.stylehandler.OneOfConstantsReadHandler;
import org.jfree.layouting.input.style.values.CSSFunctionValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.w3c.css.sac.LexicalUnit;

/**
 * Creation-Date: 21.12.2005, 18:32:53
 *
 * @author Thomas Morgner
 */
public class PositionReadHandler extends OneOfConstantsReadHandler
{
  public PositionReadHandler()
  {
    super(false);
    addValue(Position.ABSOLUTE);
    addValue(Position.FIXED);
    addValue(Position.RELATIVE);
    addValue(Position.STATIC);
  }

  public CSSValue createValue(final StyleKey name, final LexicalUnit value)
  {
    final CSSValue result = super.createValue(name, value);
    if (result != null)
    {
      return result;
    }

    // maybe the position is a 'running(..)' function.
    if (CSSValueFactory.isFunctionValue(value))
    {
      final CSSFunctionValue cssFunctionValue = CSSValueFactory.parseFunction(value);
      if (cssFunctionValue != null)
      {
        // we are a bit restrictive for now ..
        if ("running".equals(cssFunctionValue.getFunctionName()))
        {
          return cssFunctionValue;
        }
      }
    }
    return null;
  }
}
