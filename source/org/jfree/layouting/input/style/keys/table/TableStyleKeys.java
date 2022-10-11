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
 * $Id: TableStyleKeys.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.input.style.keys.table;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.StyleKeyRegistry;

/**
 * Creation-Date: 23.06.2006, 15:20:03
 *
 * @author Thomas Morgner
 */
public class TableStyleKeys
{
  /**
   * Enumeration: Show, hide, inherit
   */
  public static final StyleKey EMPTY_CELLS =
          StyleKeyRegistry.getRegistry().createKey
                  ("empty-cells", false, true, StyleKey.All_ELEMENTS);

  /**
   * Pair of length; No percentages; Inheritable
   */
  public static final StyleKey BORDER_SPACING =
          StyleKeyRegistry.getRegistry().createKey
                  ("border-spacing", false, true, StyleKey.All_ELEMENTS);

  /**
   * Pair of length; No percentages; Inheritable
   */
  public static final StyleKey BORDER_COLLAPSE =
          StyleKeyRegistry.getRegistry().createKey
                  ("border-collapse", false, true, StyleKey.All_ELEMENTS);

  /**
   * Auto or fixed.
   */
  public static final StyleKey TABLE_LAYOUT =
          StyleKeyRegistry.getRegistry().createKey
                  ("table-layout", false, true, StyleKey.All_ELEMENTS);

  /**
   * top or bottom.
   */
  public static final StyleKey CAPTION_SIDE =
          StyleKeyRegistry.getRegistry().createKey
                  ("caption-side", false, true, StyleKey.All_ELEMENTS);

  public static final StyleKey ROW_SPAN =
          StyleKeyRegistry.getRegistry().createKey
                  ("-x-liblayout-rowspan", false, false, StyleKey.All_ELEMENTS);
  public static final StyleKey COL_SPAN =
          StyleKeyRegistry.getRegistry().createKey
                  ("-x-liblayout-colspan", false, false, StyleKey.All_ELEMENTS);


  private TableStyleKeys()
  {
  }
}
