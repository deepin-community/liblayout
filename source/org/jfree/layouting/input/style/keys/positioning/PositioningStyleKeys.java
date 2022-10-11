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
 * $Id: PositioningStyleKeys.java 2739 2007-04-02 11:41:22Z taqua $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.input.style.keys.positioning;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.StyleKeyRegistry;

/**
 * Creation-Date: 08.12.2005, 14:50:40
 *
 * @author Thomas Morgner
 */
public class PositioningStyleKeys
{
  /** Width and height are defined in the Box-module. */

  public static final StyleKey TOP =
          StyleKeyRegistry.getRegistry().createKey
                  ("top", false, false, StyleKey.All_ELEMENTS);

  public static final StyleKey LEFT =
          StyleKeyRegistry.getRegistry().createKey
                  ("left", false, false, StyleKey.All_ELEMENTS);

  public static final StyleKey BOTTOM =
          StyleKeyRegistry.getRegistry().createKey
                  ("bottom", false, false, StyleKey.All_ELEMENTS);

  public static final StyleKey RIGHT =
          StyleKeyRegistry.getRegistry().createKey
                  ("right", false, false, StyleKey.All_ELEMENTS);

  public static final StyleKey POSITION =
          StyleKeyRegistry.getRegistry().createKey
                  ("position", false, false, StyleKey.All_ELEMENTS);

  public static final StyleKey Z_INDEX =
          StyleKeyRegistry.getRegistry().createKey
                  ("z-index", false, false, StyleKey.All_ELEMENTS);


  private PositioningStyleKeys()
  {
  }
}
