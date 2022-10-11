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
 * $Id: PageAreaType.java 2739 2007-04-02 11:41:22Z taqua $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.input.style;

/**
 * Creation-Date: 15.06.2006, 16:12:31
 *
 * @author Thomas Morgner
 */
public class PageAreaType
{
  public static final PageAreaType TOP = new PageAreaType(0, "top");
  public static final PageAreaType TOP_LEFT_CORNER = new PageAreaType(1, "top-left-corner");
  public static final PageAreaType TOP_LEFT = new PageAreaType(2, "top-left");
  public static final PageAreaType TOP_CENTER = new PageAreaType(3, "top-center");
  public static final PageAreaType TOP_RIGHT = new PageAreaType(4, "top-right");
  public static final PageAreaType TOP_RIGHT_CORNER = new PageAreaType(5, "top-right-corner");
  public static final PageAreaType LEFT_TOP = new PageAreaType(6, "left-top");
  public static final PageAreaType LEFT_MIDDLE = new PageAreaType(7, "left-middle");
  public static final PageAreaType LEFT_BOTTOM = new PageAreaType(8, "left-bottom");
  public static final PageAreaType RIGHT_TOP = new PageAreaType(9, "right-top");
  public static final PageAreaType RIGHT_MIDDLE = new PageAreaType(10, "right-middle");
  public static final PageAreaType RIGHT_BOTTOM = new PageAreaType(11, "right-bottom");
  public static final PageAreaType BOTTOM = new PageAreaType(12, "bottom");
  public static final PageAreaType BOTTOM_LEFT_CORNER = new PageAreaType(13, "bottom-left-corner");
  public static final PageAreaType BOTTOM_LEFT = new PageAreaType(14, "bottom-left");
  public static final PageAreaType BOTTOM_CENTER = new PageAreaType(15, "bottom-center");
  public static final PageAreaType BOTTOM_RIGHT = new PageAreaType(16, "bottom-right");
  public static final PageAreaType BOTTOM_RIGHT_CORNER = new PageAreaType(17,"bottom-right-corner");
  public static final PageAreaType CONTENT = new PageAreaType(18, "content");

  private final String myName; // for debug only
  private int index;

  private PageAreaType(final int index, final String name)
  {
    this.index = index;
    this.myName = name;
  }

  public int getIndex()
  {
    return index;
  }

  public static int getMaxIndex ()
  {
    return 19;
  }

  public static PageAreaType[] getPageAreas()
  {
    return new PageAreaType[]{ TOP,
            TOP_LEFT_CORNER ,TOP_LEFT ,TOP_CENTER, TOP_RIGHT,
            TOP_RIGHT_CORNER, LEFT_TOP, LEFT_MIDDLE, LEFT_BOTTOM,
            RIGHT_TOP, RIGHT_MIDDLE, RIGHT_BOTTOM, BOTTOM,
            BOTTOM_LEFT_CORNER, BOTTOM_LEFT, BOTTOM_CENTER,
            BOTTOM_RIGHT, BOTTOM_RIGHT_CORNER, CONTENT };
  }

  public String toString()
  {
    return myName;
  }

  public String getName()
  {
    return myName;
  }
}
