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
 * $Id: ColumnRuleReadHandler.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.input.style.parser.stylehandler.column;

import java.util.HashMap;
import java.util.Map;

import org.jfree.layouting.input.style.keys.column.ColumnStyleKeys;
import org.jfree.layouting.input.style.parser.stylehandler.AbstractCompoundValueReadHandler;
import org.jfree.layouting.input.style.values.CSSInheritValue;
import org.w3c.css.sac.LexicalUnit;

/**
 * Creation-Date: 03.12.2005, 21:56:57
 *
 * @author Thomas Morgner
 */
public class ColumnRuleReadHandler extends AbstractCompoundValueReadHandler
{
  public ColumnRuleReadHandler()
  {
    addHandler(ColumnStyleKeys.COLUMN_RULE_COLOR, new ColumnRuleColorReadHandler());
    addHandler(ColumnStyleKeys.COLUMN_RULE_STYLE, new ColumnRuleStyleReadHandler());
    addHandler(ColumnStyleKeys.COLUMN_RULE_WIDTH, new ColumnRuleWidthReadHandler());
  }

  /**
   * Parses the LexicalUnit and returns a map of (StyleKey, CSSValue) pairs.
   *
   * @param unit
   * @return
   */
  public Map createValues(final LexicalUnit unit)
  {
    if (unit.getLexicalUnitType() == LexicalUnit.SAC_INHERIT)
    {
      final Map map = new HashMap();
      map.put(ColumnStyleKeys.COLUMN_RULE_COLOR, CSSInheritValue.getInstance());
      map.put(ColumnStyleKeys.COLUMN_RULE_STYLE, CSSInheritValue.getInstance());
      map.put(ColumnStyleKeys.COLUMN_RULE_WIDTH, CSSInheritValue.getInstance());
      return map;
    }
    return super.createValues(unit);
  }
}
