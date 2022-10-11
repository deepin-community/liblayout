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
 * $Id: BorderStyle.java 2739 2007-04-02 11:41:22Z taqua $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.layouting.input.style.keys.border;

import org.jfree.layouting.input.style.values.CSSConstant;

/**
 * Creation-Date: 30.10.2005, 19:37:35
 *
 * @author Thomas Morgner
 */
public class BorderStyle
{
  public static final CSSConstant NONE = new CSSConstant("none");
  public static final CSSConstant HIDDEN = new CSSConstant("hidden");
  public static final CSSConstant DOTTED = new CSSConstant("dotted");
  public static final CSSConstant DASHED = new CSSConstant("dashed");
  public static final CSSConstant SOLID = new CSSConstant("solid");
  public static final CSSConstant DOUBLE = new CSSConstant("double");
  public static final CSSConstant DOT_DASH = new CSSConstant("dot-dash");
  public static final CSSConstant DOT_DOT_DASH = new CSSConstant("dot-dot-dash");
  public static final CSSConstant WAVE = new CSSConstant("wave");
  public static final CSSConstant GROOVE = new CSSConstant("groove");
  public static final CSSConstant RIDGE = new CSSConstant("ridge");
  public static final CSSConstant INSET = new CSSConstant("inset");
  public static final CSSConstant OUTSET = new CSSConstant("outset");

  private BorderStyle()
  {
  }
}
