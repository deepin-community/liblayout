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
 * $Id: Clear.java 2739 2007-04-02 11:41:22Z taqua $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.input.style.keys.box;

import org.jfree.layouting.input.style.values.CSSConstant;

/**
 * Creation-Date: 28.11.2005, 15:55:25
 *
 * @author Thomas Morgner
 */
public class Clear
{
  public static final CSSConstant NONE = new CSSConstant("none");
  public static final CSSConstant LEFT = new CSSConstant("left");
  public static final CSSConstant RIGHT = new CSSConstant("right");
  public static final CSSConstant TOP = new CSSConstant("top");
  public static final CSSConstant BOTTOM = new CSSConstant("bottom");
  public static final CSSConstant INSIDE = new CSSConstant("inside");
  public static final CSSConstant OUTSIDE = new CSSConstant("outside");
  public static final CSSConstant START = new CSSConstant("start");
  public static final CSSConstant END = new CSSConstant("end");
  public static final CSSConstant BOTH = new CSSConstant("both");

  private Clear()
  {
  }
}
