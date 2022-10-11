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
 * $Id: ExtendedBaselineInfo.java 2739 2007-04-02 11:41:22Z taqua $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.renderer.text;

/**
 * Creation-Date: 24.07.2006, 17:38:23
 *
 * @author Thomas Morgner
 */
public interface ExtendedBaselineInfo
{
  public static final int BASELINE_COUNT = 10;

  public static final int BEFORE_EDGE = 0;
  public static final int TEXT_BEFORE_EDGE = 1;
  public static final int HANGING = 2;
  public static final int CENTRAL = 3;
  public static final int MIDDLE = 4;
  public static final int MATHEMATICAL = 5;
  public static final int ALPHABETHIC = 6;
  public static final int IDEOGRAPHIC = 7;
  public static final int TEXT_AFTER_EDGE = 8;
  public static final int AFTER_EDGE = 9;

  public int getDominantBaseline();

  public long[] getBaselines();
  public long getBaseline (int baseline);
  public ExtendedBaselineInfo shift(long s);

}
