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
 * $Id: ExtensionStyleKeys.java 2739 2007-04-02 11:41:22Z taqua $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.input.style.keys.jfreereport;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.StyleKeyRegistry;

/**
 * All kind of StyleKeys needed for compatiblity with the old display model
 * of JFreeReport. (I dont like the idea of having these keys here.)
 *
 * @author Thomas Morgner
 */
public class ExtensionStyleKeys
{
  public static final StyleKey POSITION_X =
          StyleKeyRegistry.getRegistry().createKey
          ("-x-jfreereport-x", true, false, StyleKey.DOM_ELEMENTS);

  public static final StyleKey POSITION_Y =
          StyleKeyRegistry.getRegistry().createKey
          ("-x-jfreereport-y", true, true, StyleKey.DOM_ELEMENTS);


  private ExtensionStyleKeys()
  {
  }

}
