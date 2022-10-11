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
 * $Id: ListStyleTypeReadHandler.java 2739 2007-04-02 11:41:22Z taqua $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.input.style.parser.stylehandler.list;

import org.jfree.layouting.input.style.keys.list.ListStyleTypeAlgorithmic;
import org.jfree.layouting.input.style.keys.list.ListStyleTypeAlphabetic;
import org.jfree.layouting.input.style.keys.list.ListStyleTypeGlyphs;
import org.jfree.layouting.input.style.keys.list.ListStyleTypeNumeric;
import org.jfree.layouting.input.style.keys.list.ListStyleTypeOther;
import org.jfree.layouting.input.style.parser.stylehandler.OneOfConstantsReadHandler;

/**
 * Creation-Date: 01.12.2005, 19:22:31
 *
 * @author Thomas Morgner
 */
public class ListStyleTypeReadHandler extends OneOfConstantsReadHandler
{
  public ListStyleTypeReadHandler()
  {
    super(false);
    addValue(ListStyleTypeOther.ASTERISKS);
    addValue(ListStyleTypeOther.CIRCLED_DECIMAL);
    addValue(ListStyleTypeOther.CIRCLED_LOWER_LATIN);
    addValue(ListStyleTypeOther.CIRCLED_UPPER_LATIN);
    addValue(ListStyleTypeOther.DOTTED_DECIMAL);
    addValue(ListStyleTypeOther.DOUBLE_CIRCLED_DECIMAL);
    addValue(ListStyleTypeOther.FILLED_CIRCLED_DECIMAL);
    addValue(ListStyleTypeOther.FOOTNOTES);
    addValue(ListStyleTypeOther.PARANTHESISED_DECIMAL);
    addValue(ListStyleTypeOther.PARANTHESISED_LOWER_LATIN);

    addGlyphs();
    addNumeric();
    addAlphabetic();
    addAlgorithmic();
  }

  private void addAlgorithmic()
  {
    addValue(ListStyleTypeAlgorithmic.ARMENIAN);
    addValue(ListStyleTypeAlgorithmic.CJK_IDEOGRAPHIC);
    addValue(ListStyleTypeAlgorithmic.ETHIOPIC_NUMERIC);
    addValue(ListStyleTypeAlgorithmic.GEORGIAN);
    addValue(ListStyleTypeAlgorithmic.HEBREW);
    addValue(ListStyleTypeAlgorithmic.JAPANESE_FORMAL);
    addValue(ListStyleTypeAlgorithmic.JAPANESE_INFORMAL);
    addValue(ListStyleTypeAlgorithmic.LOWER_ARMENIAN);
    addValue(ListStyleTypeAlgorithmic.LOWER_ROMAN);
    addValue(ListStyleTypeAlgorithmic.SIMP_CHINESE_FORMAL);
    addValue(ListStyleTypeAlgorithmic.SIMP_CHINESE_INFORMAL);
    addValue(ListStyleTypeAlgorithmic.SYRIAC);
    addValue(ListStyleTypeAlgorithmic.TAMIL);
    addValue(ListStyleTypeAlgorithmic.TRAD_CHINESE_FORMAL);
    addValue(ListStyleTypeAlgorithmic.TRAD_CHINESE_INFORMAL);
    addValue(ListStyleTypeAlgorithmic.UPPER_ARMENIAN);
    addValue(ListStyleTypeAlgorithmic.UPPER_ROMAN);
  }

  private void addAlphabetic()
  {
    addValue(ListStyleTypeAlphabetic.AFAR);
    addValue(ListStyleTypeAlphabetic.AMHARIC);
    addValue(ListStyleTypeAlphabetic.AMHARIC_ABEGEDE);
    addValue(ListStyleTypeAlphabetic.CJK_EARTHLY_BRANCH);
    addValue(ListStyleTypeAlphabetic.CJK_HEAVENLY_STEM);
    addValue(ListStyleTypeAlphabetic.ETHIOPIC);
    addValue(ListStyleTypeAlphabetic.ETHIOPIC_ABEGEDE);
    addValue(ListStyleTypeAlphabetic.ETHIOPIC_ABEGEDE_AM_ET);
    addValue(ListStyleTypeAlphabetic.ETHIOPIC_ABEGEDE_GEZ);
    addValue(ListStyleTypeAlphabetic.ETHIOPIC_ABEGEDE_TI_ER);
    addValue(ListStyleTypeAlphabetic.ETHIOPIC_ABEGEDE_TI_ET);

    addValue(ListStyleTypeAlphabetic.ETHIOPIC_HALEHAME_AA_ER);
    addValue(ListStyleTypeAlphabetic.ETHIOPIC_HALEHAME_AA_ET);
    addValue(ListStyleTypeAlphabetic.ETHIOPIC_HALEHAME_AM_ET);
    addValue(ListStyleTypeAlphabetic.ETHIOPIC_HALEHAME_GEZ);
    addValue(ListStyleTypeAlphabetic.ETHIOPIC_HALEHAME_OM_ET);
    addValue(ListStyleTypeAlphabetic.ETHIOPIC_HALEHAME_SID_ET);
    addValue(ListStyleTypeAlphabetic.ETHIOPIC_HALEHAME_SO_ET);
    addValue(ListStyleTypeAlphabetic.ETHIOPIC_HALEHAME_TI_ER);
    addValue(ListStyleTypeAlphabetic.ETHIOPIC_HALEHAME_TI_ET);
    addValue(ListStyleTypeAlphabetic.ETHIOPIC_HALEHAME_TIG);

    addValue(ListStyleTypeAlphabetic.HANGUL);
    addValue(ListStyleTypeAlphabetic.HANGUL_CONSONANT);
    addValue(ListStyleTypeAlphabetic.HIRAGANA);
    addValue(ListStyleTypeAlphabetic.HIRAGANA_IROHA);
    addValue(ListStyleTypeAlphabetic.KATAKANA);
    addValue(ListStyleTypeAlphabetic.KATAKANA_IROHA);
    addValue(ListStyleTypeAlphabetic.LOWER_ALPHA);
    addValue(ListStyleTypeAlphabetic.LOWER_GREEK);
    addValue(ListStyleTypeAlphabetic.LOWER_LATIN);
    addValue(ListStyleTypeAlphabetic.LOWER_NORWEGIAN);
    addValue(ListStyleTypeAlphabetic.OROMO);
    addValue(ListStyleTypeAlphabetic.SIDAMA);
    addValue(ListStyleTypeAlphabetic.SOMALI);

    addValue(ListStyleTypeAlphabetic.TIGRE);
    addValue(ListStyleTypeAlphabetic.TIGRINYA_ER);
    addValue(ListStyleTypeAlphabetic.TIGRINYA_ER_ABEGEDE);
    addValue(ListStyleTypeAlphabetic.TIGRINYA_ET);
    addValue(ListStyleTypeAlphabetic.TIGRINYA_ET_ABEGEDE);
    addValue(ListStyleTypeAlphabetic.UPPER_ALPHA);
    addValue(ListStyleTypeAlphabetic.UPPER_GREEK);
    addValue(ListStyleTypeAlphabetic.UPPER_LATIN);
    addValue(ListStyleTypeAlphabetic.UPPER_NORWEGIAN);
  }

  private void addNumeric ()
  {
    addValue(ListStyleTypeNumeric.ARABIC_INDIC);
    addValue(ListStyleTypeNumeric.BENGALI);
    addValue(ListStyleTypeNumeric.BINARY);
    addValue(ListStyleTypeNumeric.CAMBODIAN);
    addValue(ListStyleTypeNumeric.DECIMAL);
    addValue(ListStyleTypeNumeric.DECIMAL_LEADING_ZERO);
    addValue(ListStyleTypeNumeric.DEVANAGARI);
    addValue(ListStyleTypeNumeric.GUJARATI);
    addValue(ListStyleTypeNumeric.GURMUKHI);
    addValue(ListStyleTypeNumeric.KANNADA);
    addValue(ListStyleTypeNumeric.KHMER);

    addValue(ListStyleTypeNumeric.LAO);
    addValue(ListStyleTypeNumeric.LOWER_HEXADECIMAL);
    addValue(ListStyleTypeNumeric.MALAYALAM);
    addValue(ListStyleTypeNumeric.MONGOLIAN);
    addValue(ListStyleTypeNumeric.MYANMAR);

    addValue(ListStyleTypeNumeric.OCTAL);
    addValue(ListStyleTypeNumeric.ORIYA);
    addValue(ListStyleTypeNumeric.PERSIAN);
    addValue(ListStyleTypeNumeric.TELUGU);
    addValue(ListStyleTypeNumeric.THAI);
    addValue(ListStyleTypeNumeric.TIBETIAN);

    addValue(ListStyleTypeNumeric.UPPER_HEXADECIMAL);
    addValue(ListStyleTypeNumeric.URDU);
  }

  private void addGlyphs()
  {
    addValue(ListStyleTypeGlyphs.BOX);
    addValue(ListStyleTypeGlyphs.CHECK);
    addValue(ListStyleTypeGlyphs.CIRCLE);
    addValue(ListStyleTypeGlyphs.DIAMOND);
    addValue(ListStyleTypeGlyphs.DISC);
    addValue(ListStyleTypeGlyphs.HYPHEN);
    addValue(ListStyleTypeGlyphs.SQUARE);
  }
}
