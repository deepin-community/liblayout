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
 * $Id: FontEmphasizeReadHandler.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.input.style.parser.stylehandler.font;

import java.util.HashMap;
import java.util.Map;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.keys.font.FontStyleKeys;
import org.jfree.layouting.input.style.parser.CSSCompoundValueReadHandler;
import org.jfree.layouting.input.style.values.CSSValue;
import org.w3c.css.sac.LexicalUnit;

/**
 * Creation-Date: 28.11.2005, 17:52:55
 *
 * @author Thomas Morgner
 */
public class FontEmphasizeReadHandler
        implements CSSCompoundValueReadHandler
{
  private FontEmphasizePositionReadHandler positionReadHandler;
  private FontEmphasizeStyleReadHandler styleReadHandler;

  public FontEmphasizeReadHandler()
  {
    positionReadHandler = new FontEmphasizePositionReadHandler();
    styleReadHandler = new FontEmphasizeStyleReadHandler();
  }

  /**
   * Parses the LexicalUnit and returns a map of (StyleKey, CSSValue) pairs.
   *
   * @param unit
   * @return
   */
  public Map createValues(LexicalUnit unit)
  {
    final CSSValue style = styleReadHandler.createValue(null, unit);
    if (style != null)
    {
      unit = unit.getNextLexicalUnit();
    }
    final CSSValue position;
    if (unit != null)
    {
      position = positionReadHandler.createValue(null, unit);
    }
    else
    {
      position = null;
    }
    final Map map = new HashMap();
    if (position != null)
    {
      map.put(FontStyleKeys.FONT_EMPHASIZE_POSITION, position);
    }
    if (style != null)
    {
      map.put(FontStyleKeys.FONT_EMPHASIZE_STYLE, style);
    }
    return map;
  }

  public StyleKey[] getAffectedKeys()
  {
    return new StyleKey[]{
            FontStyleKeys.FONT_EMPHASIZE_POSITION,
            FontStyleKeys.FONT_EMPHASIZE_STYLE
    };
  }
}
