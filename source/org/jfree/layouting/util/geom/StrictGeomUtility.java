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
 * $Id: StrictGeomUtility.java 6489 2008-11-28 14:53:40Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.util.geom;

import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;

import org.pentaho.reporting.libraries.base.util.FloatDimension;

/**
 * This class is the heart of the alternative geometrics toolkit. It performs
 * the neccessary conversions from and to the AWT classes to the Strict-classes.
 *
 * @author Thomas Morgner
 */
public class StrictGeomUtility
{
  /**
   * This is the correction factor used to convert points into 'Micro-Points'.
   */
  private static final double CORRECTION_FACTOR = 1000.0d;

  /**
   * Hidden, non usable constructor.
   */
  private StrictGeomUtility ()
  {
  }

  /**
   * Creates a StrictDimension from the given AWT sizes.
   *
   * @param w the width in points (1/72th inch).
   * @param h the height in points (1/72th inch).
   * @return the created dimension object.
   */
  public static StrictDimension createDimension (final double w, final double h)
  {
    return new StrictDimension((long) (w * CORRECTION_FACTOR),
            (long) (h * CORRECTION_FACTOR));
  }

  /**
   * Creates a StrictPoint from the given AWT coordinates.
   *
   * @param x the x coordinate in points (1/72th inch).
   * @param y the y coordinate in points (1/72th inch).
   * @return the created point object.
   */
  public static StrictPoint createPoint (final double x, final double y)
  {
    return new StrictPoint((long) (x * CORRECTION_FACTOR),
            (long) (y * CORRECTION_FACTOR));
  }

  /**
   * Creates a StrictBounds object from the given AWT sizes.
   *
   * @param x the x coordinate in points (1/72th inch).
   * @param y the y coordinate in points (1/72th inch).
   * @param width the width in points (1/72th inch).
   * @param height the height in points (1/72th inch).
   * @return the created dimension object.
   */
  public static StrictBounds createBounds (final double x, final double y,
                                           final double width, final double height)
  {
    return new StrictBounds((long) (x * CORRECTION_FACTOR),
            (long) (y * CORRECTION_FACTOR),
            (long) (width * CORRECTION_FACTOR),
            (long) (height * CORRECTION_FACTOR));
  }

  /**
   * Creates an AWT-Dimension2D object from the given strict sizes.
   *
   * @param width the width in micro points.
   * @param height the height in micro points.
   * @return the created dimension object.
   */
  public static Dimension2D createAWTDimension
          (final long width, final long height)
  {
    return new FloatDimension
            ((float) (width / CORRECTION_FACTOR), (float) (height / CORRECTION_FACTOR));
  }

  /**
   * Creates an AWT rectangle object from the given strict sizes.
   *
   * @param x the x coordinate in micro points.
   * @param y the y coordinate in micro points.
   * @param width the width in micro points.
   * @param height the height in micro points.
   * @return the created dimension object.
   */
  public static Rectangle2D createAWTRectangle
          (final long x, final long y, final long width, final long height)
  {
    return new Rectangle2D.Double
            (x / CORRECTION_FACTOR, y / CORRECTION_FACTOR,
                    width / CORRECTION_FACTOR, height / CORRECTION_FACTOR);
  }

  /**
   * Converts the given AWT value into a strict value.
   *
   * @param value the AWT point value.
   * @return the internal micro point value.
   */
  public static long toInternalValue (final double value)
  {
    return (long) (value * CORRECTION_FACTOR);
  }

  /**
   * Converts the given micro point value into an AWT value.
   *
   * @param value the micro point point value.
   * @return the AWT point value.
   */
  public static double toExternalValue (final long value)
  {
    return (value / CORRECTION_FACTOR);
  }

  public static long multiply (final long x, final long y)
  {
    return (long) ((x * y) / CORRECTION_FACTOR);
  }
}
