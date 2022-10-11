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
 * $Id: HyperlinkStyleKeys.java 2739 2007-04-02 11:41:22Z taqua $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.input.style.keys.hyperlinks;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.StyleKeyRegistry;

/**
 * Creation-Date: 24.11.2005, 17:25:02
 *
 * @author Thomas Morgner
 */
public class HyperlinkStyleKeys
{
  public static final StyleKey TARGET_NAME =
          StyleKeyRegistry.getRegistry().createKey
                  ("target-name", false, false, StyleKey.All_ELEMENTS);
  public static final StyleKey TARGET_NEW =
          StyleKeyRegistry.getRegistry().createKey
                  ("target-new", false, false, StyleKey.All_ELEMENTS);
  public static final StyleKey TARGET_POSITION =
          StyleKeyRegistry.getRegistry().createKey
                  ("target-position", false, false, StyleKey.All_ELEMENTS);

  /**
   * todo: implement me
   * This is a libLayout extension to allow a document independent
   * link specification. It is up to the output target to support that.
   *
   * Example style definition:  a { x-href-target: attr("href"); }
   */
  public static final StyleKey HREF_TARGET =
          StyleKeyRegistry.getRegistry().createKey
                  ("-x-liblayout-href-target", true, false, StyleKey.All_ELEMENTS);
  /**
   * todo: implement me
   * This is a libLayout extension to allow a document independent
   * anchor specifications. It is up to the output target to support that.
   *
   * Example style definition:  a { x-href-anchor: attr("name"); }
   */
  public static final StyleKey ANCHOR =
          StyleKeyRegistry.getRegistry().createKey
                  ("-x-liblayout-href-anchor", true, false, StyleKey.All_ELEMENTS);

  private HyperlinkStyleKeys()
  {
  }
}
