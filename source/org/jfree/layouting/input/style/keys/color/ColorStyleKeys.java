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
 * $Id: ColorStyleKeys.java 2739 2007-04-02 11:41:22Z taqua $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.layouting.input.style.keys.color;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.StyleKeyRegistry;

/**
 * Creation-Date: 30.10.2005, 18:47:30
 *
 * @author Thomas Morgner
 */
public final class ColorStyleKeys
{
  public static final StyleKey COLOR =
          StyleKeyRegistry.getRegistry().createKey
          ("color", false, true, StyleKey.ALWAYS);

  /**
   * Not sure whether we can implement this one. It is a post-processing
   * operation, and may or may not be supported by the output target.
   */
  public static final StyleKey OPACITY =
          StyleKeyRegistry.getRegistry().createKey
          ("opacity", false, false, StyleKey.ALWAYS);

  /**
   * For now, we do not care about color profiles. This might have to do with
   * me being clueless about the topic, but also with the cost vs. usefullness
   * calculation involved.
   */
  public static final StyleKey COLOR_PROFILE =
          StyleKeyRegistry.getRegistry().createKey
          ("color-profile", false, true, StyleKey.ALWAYS);
  public static final StyleKey RENDERING_INTENT =
          StyleKeyRegistry.getRegistry().createKey
          ("rendering-intent", false, true, StyleKey.ALWAYS);

  private ColorStyleKeys() {}
}
