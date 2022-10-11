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
 * $Id: BackgroundRepeatReadHandler.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.input.style.parser.stylehandler.border;

import java.util.ArrayList;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.keys.border.BackgroundRepeat;
import org.jfree.layouting.input.style.parser.CSSValueFactory;
import org.jfree.layouting.input.style.parser.CSSValueReadHandler;
import org.jfree.layouting.input.style.values.CSSConstant;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.input.style.values.CSSValueList;
import org.jfree.layouting.input.style.values.CSSValuePair;
import org.w3c.css.sac.LexicalUnit;

/**
 * Creation-Date: 27.11.2005, 18:36:29
 *
 * @author Thomas Morgner
 */
public class BackgroundRepeatReadHandler implements CSSValueReadHandler
{
  public BackgroundRepeatReadHandler()
  {
  }

  public CSSValue createValue(final StyleKey name, LexicalUnit value)
  {
    final ArrayList values = new ArrayList();

    while (value != null)
    {
      if (value.getLexicalUnitType() != LexicalUnit.SAC_IDENT)
      {
        return null;
      }

      final CSSConstant horizontal;
      final CSSConstant vertical;

      final String horizontalString = value.getStringValue();
      if ("repeat-x".equalsIgnoreCase(horizontalString))
      {
        horizontal = BackgroundRepeat.REPEAT;
        vertical = BackgroundRepeat.NOREPEAT;
      }
      else if ("repeat-y".equalsIgnoreCase(value.getStringValue()))
      {
        horizontal = BackgroundRepeat.NOREPEAT;
        vertical = BackgroundRepeat.REPEAT;
      }
      else
      {
        horizontal = translateRepeat(horizontalString);
        if (horizontal == null)
        {
          return null;
        }

        value = value.getNextLexicalUnit();
        if (value == null)
        {
          vertical = horizontal;
        }
        else if (value.getLexicalUnitType() != LexicalUnit.SAC_IDENT)
        {
          return null;
        }
        else
        {
          vertical = translateRepeat(value.getStringValue());
          if (vertical == null)
          {
            return null;
          }
        }
      }

      values.add(new CSSValuePair(horizontal, vertical));
      value = CSSValueFactory.parseComma(value);
    }

    return new CSSValueList(values);
  }

  private CSSConstant translateRepeat(final String value)
  {
    if ("repeat".equalsIgnoreCase(value))
    {
      return BackgroundRepeat.REPEAT;
    }
    if ("no-repeat".equalsIgnoreCase(value))
    {
      return BackgroundRepeat.NOREPEAT;
    }
    if ("space".equalsIgnoreCase(value))
    {
      return BackgroundRepeat.SPACE;
    }
    return null;
  }
}
