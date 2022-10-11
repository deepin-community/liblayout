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
 * $Id: ColumnStyleKeys.java 2739 2007-04-02 11:41:22Z taqua $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.input.style.keys.column;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.StyleKeyRegistry;

/**
 * Creation-Date: 03.12.2005, 20:48:04
 *    * column-count
    * column-width
    * column-min-width
    * column-width-policy

The second group of properties describes the space between columns:

    * column-gap
    * column-rule
    * column-rule-color
    * column-rule-style
    * column-rule-width

The third group consists of one property which make it possible an element to span several columns:

    * column-span
 *
 *
 * @author Thomas Morgner
 */
public class ColumnStyleKeys
{
  public static final StyleKey COLUMN_COUNT =
          StyleKeyRegistry.getRegistry().createKey
                  ("column-count", false, false, StyleKey.BLOCK_ELEMENTS);

  public static final StyleKey COLUMN_SPACE_DISTRIBUTION =
          StyleKeyRegistry.getRegistry().createKey
                  ("column-space-distribution", false, false, StyleKey.BLOCK_ELEMENTS);

  public static final StyleKey COLUMN_WIDTH =
          StyleKeyRegistry.getRegistry().createKey
                  ("column-width", false, false, StyleKey.BLOCK_ELEMENTS);

  public static final StyleKey COLUMN_MIN_WIDTH =
          StyleKeyRegistry.getRegistry().createKey
                  ("column-min-width", false, false, StyleKey.BLOCK_ELEMENTS);

  public static final StyleKey COLUMN_WIDTH_POLICY =
          StyleKeyRegistry.getRegistry().createKey
                  ("column-width-policy", false, false, StyleKey.BLOCK_ELEMENTS);

  public static final StyleKey COLUMN_GAP =
          StyleKeyRegistry.getRegistry().createKey
                  ("column-gap", false, false, StyleKey.BLOCK_ELEMENTS);

  public static final StyleKey COLUMN_RULE_COLOR =
          StyleKeyRegistry.getRegistry().createKey
                  ("column-rule-color", false, false, StyleKey.BLOCK_ELEMENTS);

  public static final StyleKey COLUMN_RULE_STYLE =
          StyleKeyRegistry.getRegistry().createKey
                  ("column-rule-style", false, false, StyleKey.BLOCK_ELEMENTS);

  public static final StyleKey COLUMN_RULE_WIDTH =
          StyleKeyRegistry.getRegistry().createKey
                  ("column-rule-width", false, false, StyleKey.BLOCK_ELEMENTS);

  public static final StyleKey COLUMN_SPAN =
          StyleKeyRegistry.getRegistry().createKey
                  ("column-span", false, false, StyleKey.BLOCK_ELEMENTS);

  /**
   * Defines, whether column contents should be balanced.
   *
   * Another idea stolen from OpenOffice :)
   */
  public static final StyleKey COLUMN_BALANCE =
          StyleKeyRegistry.getRegistry().createKey
                  ("-x-liblayout-column-balance", false, false, StyleKey.BLOCK_ELEMENTS);


  private ColumnStyleKeys()
  {
  }
}
