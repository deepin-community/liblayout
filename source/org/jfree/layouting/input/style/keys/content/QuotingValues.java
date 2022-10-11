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
 * $Id: QuotingValues.java 2739 2007-04-02 11:41:22Z taqua $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.input.style.keys.content;

import org.jfree.layouting.input.style.values.CSSStringType;
import org.jfree.layouting.input.style.values.CSSStringValue;

/**
 * This class holds a sample of well-known quoting characters.
 * These values are non-normative and there are no CSS-constants
 * defined for them.
 *
 * @author Thomas Morgner
 */
public class QuotingValues
{

  public static final CSSStringValue QUOTATION_MARK =
          new CSSStringValue(CSSStringType.STRING, "\"");
  public static final CSSStringValue APOSTROPHE =
          new CSSStringValue(CSSStringType.STRING, "\u0027");
  public static final CSSStringValue SINGLE_LEFT_POINTING_ANGLE_QUOTATION_MARK =
          new CSSStringValue(CSSStringType.STRING, "\u2039");
  public static final CSSStringValue SINGLE_RIGHT_POINTING_ANGLE_QUOTATION_MARK =
          new CSSStringValue(CSSStringType.STRING, "\u203A");
  public static final CSSStringValue DOUBLE_LEFT_POINTING_ANGLE_QUOTATION_MARK =
          new CSSStringValue(CSSStringType.STRING, "\u00AB");
  public static final CSSStringValue DOUBLE_RIGHT_POINTING_ANGLE_QUOTATION_MARK =
          new CSSStringValue(CSSStringType.STRING, "\u00BB");
  public static final CSSStringValue SINGLE_LEFT_QUOTATION_MARK =
          new CSSStringValue(CSSStringType.STRING, "\u2018");
  public static final CSSStringValue SINGLE_RIGHT_QUOTATION_MARK =
          new CSSStringValue(CSSStringType.STRING, "\u2019");
  public static final CSSStringValue DOUBLE_LEFT_QUOTATION_MARK =
          new CSSStringValue(CSSStringType.STRING, "\u201C");
  public static final CSSStringValue DOUBLE_RIGHT_QUOTATION_MARK =
          new CSSStringValue(CSSStringType.STRING, "\u201D");
  public static final CSSStringValue DOUBLE_LOW9_QUOTATION_MARK =
          new CSSStringValue(CSSStringType.STRING, "\u201E");

  private QuotingValues()
  {
  }
}
