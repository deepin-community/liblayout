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
 * $Id: TargetReadHandler.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.input.style.parser.stylehandler.hyperlinks;

import java.util.HashMap;
import java.util.Map;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.keys.hyperlinks.HyperlinkStyleKeys;
import org.jfree.layouting.input.style.parser.CSSCompoundValueReadHandler;
import org.jfree.layouting.input.style.values.CSSValue;
import org.w3c.css.sac.LexicalUnit;

/**
 * Creation-Date: 28.11.2005, 19:34:19
 *
 * @author Thomas Morgner
 */
public class TargetReadHandler implements CSSCompoundValueReadHandler
{
  private TargetNameReadHandler nameReadHandler;
  private TargetNewReadHandler newReadHandler;
  private TargetPositionReadHandler positionReadHandler;

  public TargetReadHandler()
  {
    nameReadHandler = new TargetNameReadHandler();
    newReadHandler = new TargetNewReadHandler();
    positionReadHandler = new TargetPositionReadHandler();
  }

  /**
   * Parses the LexicalUnit and returns a map of (StyleKey, CSSValue) pairs.
   *
   * @param unit
   * @return
   */
  public Map createValues(LexicalUnit unit)
  {
    final CSSValue nameValue = nameReadHandler.createValue(null, unit);
    if (nameValue != null)
    {
      unit = unit.getNextLexicalUnit();
    }

    CSSValue newValue = null;
    if (unit != null)
    {
      newValue = newReadHandler.createValue(null, unit);
      if (newValue != null)
      {
        unit = unit.getNextLexicalUnit();
      }
    }
    CSSValue positionValue = null;
    if (unit != null)
    {
      positionValue = positionReadHandler.createValue(null, unit);
    }

    final Map map = new HashMap();
    if (nameValue != null)
    {
      map.put (HyperlinkStyleKeys.TARGET_NAME, nameValue);
    }
    if (newValue != null)
    {
      map.put (HyperlinkStyleKeys.TARGET_NEW, newValue);
    }
    if (positionValue != null)
    {
      map.put (HyperlinkStyleKeys.TARGET_POSITION, positionValue);
    }
    return map;
  }

  public StyleKey[] getAffectedKeys()
  {
    return new StyleKey[] {
        HyperlinkStyleKeys.TARGET_NAME,
        HyperlinkStyleKeys.TARGET_NEW,
        HyperlinkStyleKeys.TARGET_POSITION
    };
  }
}
