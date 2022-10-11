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
 * $Id: TextStyleKeys.java 2739 2007-04-02 11:41:22Z taqua $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.layouting.input.style.keys.text;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.StyleKeyRegistry;

/**
 * http://www.w3.org/TR/css3-text/<br/>
 * and</br>
 * http://www.w3.org/TR/2003/CR-css3-text-20030514/
 * <p/>
 * Text needs to be changed, as the Working-draft is more than just incomplete.
 * TextShadow is not supported yet.
 *
 * @see http://www.unicode.org/unicode/reports/tr9/tr9-11.html
 * @author Thomas Morgner
 */
public class TextStyleKeys
{
  /**
   * This property declares whether and how white space inside the element is
   * collapsed. Values have the following meanings:
   * <p/>
   * <ul>
   * <li>collapse
   * <p>This value directs user agents to collapse sequences of white
   * space into a single character (or in some cases, no character).
   * </p>
   * </li>
   * <li>preserve
   * <p>
   * This value prevents user agents from collapsing sequences of white space.
   * Line breaks are preserved.
   * </p>
   * </li>
   * <li>
   * preserve-breaks
   * <p>
   * This value collapses white space as for 'collapse', but preserves line breaks.
   * </p>
   * </li>
   * <li>
   * discard
   * <p>
   * This value directs user agents to discard all white space in the element.
   * </p>
   * </li>
   * </ul>
   *
   * @see http://www.w3.org/TR/css3-text/#white-space-rules
   */
  public static final StyleKey WHITE_SPACE_COLLAPSE =
          StyleKeyRegistry.getRegistry().createKey
                  ("white-space-collapse", false, true, StyleKey.All_ELEMENTS);

  public static final StyleKey WORD_BREAK =
          StyleKeyRegistry.getRegistry().createKey
                  ("word-break", false, true, StyleKey.All_ELEMENTS);

  public static final StyleKey HYPHENATE =
          StyleKeyRegistry.getRegistry().createKey
                  ("hyphenate", false, true, StyleKey.All_ELEMENTS);

  public static final StyleKey TEXT_WRAP =
          StyleKeyRegistry.getRegistry().createKey
                  ("text-wrap", false, true, StyleKey.All_ELEMENTS);

  public static final StyleKey WORD_WRAP =
          StyleKeyRegistry.getRegistry().createKey
                  ("word-wrap", false, true, StyleKey.All_ELEMENTS);

  public static final StyleKey TEXT_ALIGN =
          StyleKeyRegistry.getRegistry().createKey
                  ("text-align", false, true, StyleKey.All_ELEMENTS);

  public static final StyleKey TEXT_ALIGN_LAST =
          StyleKeyRegistry.getRegistry().createKey
                  ("text-align-last", false, true, StyleKey.All_ELEMENTS);

  public static final StyleKey TEXT_JUSTIFY =
          StyleKeyRegistry.getRegistry().createKey
                  ("text-justify", false, true, StyleKey.All_ELEMENTS);

  public static final StyleKey WORD_SPACING =
          StyleKeyRegistry.getRegistry().createKey
                  ("word-spacing", false, true, StyleKey.All_ELEMENTS);
  public static final StyleKey X_MIN_WORD_SPACING =
          StyleKeyRegistry.getRegistry().createKey
                  ("-x-liblayout-min-word-spacing", false, true, StyleKey.All_ELEMENTS);
  public static final StyleKey X_MAX_WORD_SPACING =
          StyleKeyRegistry.getRegistry().createKey
                  ("-x-liblayout-max-word-spacing", false, true, StyleKey.All_ELEMENTS);
  public static final StyleKey X_OPTIMUM_WORD_SPACING =
          StyleKeyRegistry.getRegistry().createKey
                  ("-x-liblayout-optimum-word-spacing", false, true, StyleKey.All_ELEMENTS);

  public static final StyleKey LETTER_SPACING =
          StyleKeyRegistry.getRegistry().createKey
                  ("letter-spacing", false, true, StyleKey.All_ELEMENTS);
  public static final StyleKey X_MIN_LETTER_SPACING =
          StyleKeyRegistry.getRegistry().createKey
                  ("-xliblayout-min-letter-spacing", false, true, StyleKey.All_ELEMENTS);
  public static final StyleKey X_MAX_LETTER_SPACING =
          StyleKeyRegistry.getRegistry().createKey
                  ("-xliblayout-max-letter-spacing", false, true, StyleKey.All_ELEMENTS);
  public static final StyleKey X_OPTIMUM_LETTER_SPACING =
          StyleKeyRegistry.getRegistry().createKey
                  ("-xliblayout-opt-letter-spacing", false, true, StyleKey.All_ELEMENTS);

  /** Arabic script specific */
  public static final StyleKey TEXT_KASHIDA_SPACE =
          StyleKeyRegistry.getRegistry().createKey
                  ("text-kashida-space", false, true, StyleKey.All_ELEMENTS);

  public static final StyleKey DIRECTION =
          StyleKeyRegistry.getRegistry().createKey
                  ("direction", false, true, StyleKey.All_ELEMENTS);

  public static final StyleKey BLOCK_PROGRESSION =
          StyleKeyRegistry.getRegistry().createKey
                  ("block-progression", false, true, StyleKey.All_ELEMENTS);

  public static final StyleKey GLYPH_ORIENTATION_HORIZONTAL =
          StyleKeyRegistry.getRegistry().createKey
                  ("glyph-orientation-horizontal", false, true, StyleKey.All_ELEMENTS);

  public static final StyleKey GLYPH_ORIENTATION_VERTICAL =
          StyleKeyRegistry.getRegistry().createKey
                  ("glyph-orientation-vertical", false, true, StyleKey.All_ELEMENTS);

  public static final StyleKey UNICODE_BIDI =
          StyleKeyRegistry.getRegistry().createKey
                  ("unicode-bidi", false, true, StyleKey.All_ELEMENTS);

  public static final StyleKey TEXT_SCRIPT =
          StyleKeyRegistry.getRegistry().createKey
                  ("text-script", false, true, StyleKey.All_ELEMENTS);

  /** todo: For asian scripts; not yet used. */
  public static final StyleKey TEXT_JUSTIFY_TRIM =
          StyleKeyRegistry.getRegistry().createKey
                  ("text-justify-trim", false, true, StyleKey.All_ELEMENTS);

  public static final StyleKey TEXT_INDENT =
          StyleKeyRegistry.getRegistry().createKey
                  ("text-indent", false, true, StyleKey.All_ELEMENTS);

  public static final StyleKey TEXT_OVERFLOW_MODE =
          StyleKeyRegistry.getRegistry().createKey
                  ("text-overflow-mode", false, true, StyleKey.All_ELEMENTS);

  public static final StyleKey TEXT_OVERFLOW_ELLIPSIS =
          StyleKeyRegistry.getRegistry().createKey
                  ("text-overflow-ellipsis", false, true, StyleKey.All_ELEMENTS);

  public static final StyleKey X_LINE_TEXT_OVERFLOW_ELLIPSIS =
          StyleKeyRegistry.getRegistry().createKey
                  ("-x-liblayout-line-text-overflow-ellipsis", false, true, StyleKey.All_ELEMENTS);
  public static final StyleKey X_BLOCK_TEXT_OVERFLOW_ELLIPSIS =
          StyleKeyRegistry.getRegistry().createKey
                  ("-x-liblayout-block-text-overflow-ellipsis", false, true, StyleKey.All_ELEMENTS);

  /** Asian fonts only */
  public static final StyleKey PUNCTUATION_TRIM =
          StyleKeyRegistry.getRegistry().createKey
                  ("punctuation-trim", false, true, StyleKey.All_ELEMENTS);
  /** Asian fonts only */
  public static final StyleKey TEXT_AUTO_SPACE =
          StyleKeyRegistry.getRegistry().createKey
                  ("text-autospace", false, true, StyleKey.All_ELEMENTS);

  public static final StyleKey KERNING_MODE =
          StyleKeyRegistry.getRegistry().createKey
                  ("kerning-mode", false, true, StyleKey.All_ELEMENTS);
  public static final StyleKey KERNING_PAIR_THRESHOLD =
          StyleKeyRegistry.getRegistry().createKey
                  ("kerning-pair-threshold", false, true, StyleKey.All_ELEMENTS);
  public static final StyleKey TEXT_UNDERLINE_STYLE =
          StyleKeyRegistry.getRegistry().createKey
                  ("text-underline-style", false, true, StyleKey.All_ELEMENTS);
  public static final StyleKey TEXT_LINE_THROUGH_STYLE =
          StyleKeyRegistry.getRegistry().createKey
                  ("text-line-through-style", false, true, StyleKey.All_ELEMENTS);
  public static final StyleKey TEXT_OVERLINE_STYLE =
          StyleKeyRegistry.getRegistry().createKey
                  ("text-overline-style", false, true, StyleKey.All_ELEMENTS);
  public static final StyleKey TEXT_UNDERLINE_WIDTH =
          StyleKeyRegistry.getRegistry().createKey
                  ("text-underline-width", false, true, StyleKey.All_ELEMENTS);
  public static final StyleKey TEXT_LINE_THROUGH_WIDTH =
          StyleKeyRegistry.getRegistry().createKey
                  ("text-line-through-width", false, true, StyleKey.All_ELEMENTS);
  public static final StyleKey TEXT_OVERLINE_WIDTH =
          StyleKeyRegistry.getRegistry().createKey
                  ("text-overline-width", false, true, StyleKey.All_ELEMENTS);
  public static final StyleKey TEXT_UNDERLINE_MODE =
          StyleKeyRegistry.getRegistry().createKey
                  ("text-underline-mode", false, true, StyleKey.All_ELEMENTS);
  public static final StyleKey TEXT_LINE_THROUGH_MODE =
          StyleKeyRegistry.getRegistry().createKey
                  ("text-line-through-mode", false, true, StyleKey.All_ELEMENTS);
  public static final StyleKey TEXT_OVERLINE_MODE =
          StyleKeyRegistry.getRegistry().createKey
                  ("text-overline-mode", false, true, StyleKey.All_ELEMENTS);
  public static final StyleKey TEXT_UNDERLINE_COLOR =
          StyleKeyRegistry.getRegistry().createKey
                  ("text-underline-color", false, true, StyleKey.All_ELEMENTS);
  public static final StyleKey TEXT_LINE_THROUGH_COLOR =
          StyleKeyRegistry.getRegistry().createKey
                  ("text-line-through-color", false, true, StyleKey.All_ELEMENTS);
  public static final StyleKey TEXT_OVERLINE_COLOR =
          StyleKeyRegistry.getRegistry().createKey
                  ("text-overline-color", false, true, StyleKey.All_ELEMENTS);

  public static final StyleKey TEXT_UNDERLINE_POSITION =
          StyleKeyRegistry.getRegistry().createKey
                  ("text-underline-position", false, true, StyleKey.All_ELEMENTS);
  public static final StyleKey TEXT_BLINK =
          StyleKeyRegistry.getRegistry().createKey
                  ("text-blink", false, false, StyleKey.All_ELEMENTS);

  public static final StyleKey LINE_GRID_MODE =
          StyleKeyRegistry.getRegistry().createKey
                  ("line-grid-mode", false, true, StyleKey.All_ELEMENTS);
  public static final StyleKey LINE_GRID_PROGRESSION =
          StyleKeyRegistry.getRegistry().createKey
                  ("line-grid-progression", false, true, StyleKey.All_ELEMENTS);
  public static final StyleKey TEXT_TRANSFORM =
          StyleKeyRegistry.getRegistry().createKey
                  ("text-transform", false, true, StyleKey.All_ELEMENTS);
  public static final StyleKey HANGING_PUNCTUATION =
          StyleKeyRegistry.getRegistry().createKey
                  ("hanging-punctuation", false, true, StyleKey.All_ELEMENTS);
  public static final StyleKey TEXT_COMBINE =
          StyleKeyRegistry.getRegistry().createKey
                  ("text-combine", false, true, StyleKey.All_ELEMENTS);

  public static final StyleKey EXCEL_WRAP_TEXT =
          StyleKeyRegistry.getRegistry().createKey
                  ("-x-liblayout-excel-wrap-text", false, true, StyleKey.All_ELEMENTS);
  public static final StyleKey TEXT_EMPHASIZE_TYPE =
          StyleKeyRegistry.getRegistry().createKey
                  ("-x-liblayout-text-emphasize-type", false, true, StyleKey.All_ELEMENTS);
  public static final StyleKey TEXT_EMPHASIZE_POSITION =
          StyleKeyRegistry.getRegistry().createKey
                  ("-x-liblayout-text-emphasize-position", false, true, StyleKey.All_ELEMENTS);

  private TextStyleKeys()
  {
  }
}
