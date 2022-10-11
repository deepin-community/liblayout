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
 * $Id: ListStyleTypeGlyphs.java 2739 2007-04-02 11:41:22Z taqua $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.input.style.keys.list;

import org.jfree.layouting.input.style.values.CSSConstant;

/**
 * Creation-Date: 01.12.2005, 18:40:19
 *
 * @author Thomas Morgner
 */
public class ListStyleTypeGlyphs
{
   // box | check | circle | diamond | disc | hyphen | square
  public static final CSSConstant BOX = new CSSConstant("box");
  public static final CSSConstant CHECK = new CSSConstant("check");
  public static final CSSConstant CIRCLE = new CSSConstant("circle");
  public static final CSSConstant DIAMOND = new CSSConstant("diamon");
  public static final CSSConstant DISC = new CSSConstant("disc");
  public static final CSSConstant HYPHEN = new CSSConstant("hyphen");
  public static final CSSConstant SQUARE = new CSSConstant("square");

  private ListStyleTypeGlyphs()
  {
  }
}
