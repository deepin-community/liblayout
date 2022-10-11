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
 * $Id: ListStyleTypeAlgorithmic.java 2739 2007-04-02 11:41:22Z taqua $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.input.style.keys.list;

import org.jfree.layouting.input.style.values.CSSConstant;

/**
 * Creation-Date: 01.12.2005, 18:42:51
 *
 * @author Thomas Morgner
 */
public class ListStyleTypeAlgorithmic
{
  // armenian | cjk-ideographic | ethiopic-numeric | georgian |
  // hebrew | japanese-formal | japanese-informal |   lower-armenian |
  // lower-roman | simp-chinese-formal | simp-chinese-informal |
  // syriac | tamil | trad-chinese-formal | trad-chinese-informal |
  // upper-armenian | upper-roman

  public static final CSSConstant ARMENIAN =
          new CSSConstant("armenian");
  public static final CSSConstant CJK_IDEOGRAPHIC =
          new CSSConstant("cjk-ideographic");
  public static final CSSConstant ETHIOPIC_NUMERIC =
          new CSSConstant("ethiopic-numeric");
  public static final CSSConstant GEORGIAN =
          new CSSConstant("georgian");
  public static final CSSConstant HEBREW =
          new CSSConstant("hebrew");
  public static final CSSConstant JAPANESE_FORMAL =
          new CSSConstant("japanese-formal");
  public static final CSSConstant JAPANESE_INFORMAL =
          new CSSConstant("japanese-informal");
  public static final CSSConstant LOWER_ARMENIAN =
          new CSSConstant("lower-armenian");
  public static final CSSConstant LOWER_ROMAN =
          new CSSConstant("lower-roman");
  public static final CSSConstant SIMP_CHINESE_FORMAL =
          new CSSConstant("simp-chinese-formal");
  public static final CSSConstant SIMP_CHINESE_INFORMAL =
          new CSSConstant("simp-chinese-informal");
  public static final CSSConstant TRAD_CHINESE_FORMAL =
          new CSSConstant("trad-chinese-formal");
  public static final CSSConstant TRAD_CHINESE_INFORMAL =
          new CSSConstant("trad-chinese-informal");
  public static final CSSConstant UPPER_ARMENIAN =
          new CSSConstant("upper-armenian");
  public static final CSSConstant UPPER_ROMAN =
          new CSSConstant("upper-roman");
  public static final CSSConstant SYRIAC =
          new CSSConstant("syriac");
  public static final CSSConstant TAMIL =
          new CSSConstant("tamil");

  private ListStyleTypeAlgorithmic()
  {
  }
}
