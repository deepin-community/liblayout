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
 * $Id: StringSetReadHandler.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.input.style.parser.stylehandler.content;

import java.util.ArrayList;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.keys.content.ContentValues;
import org.jfree.layouting.input.style.keys.list.ListStyleTypeGlyphs;
import org.jfree.layouting.input.style.parser.CSSValueFactory;
import org.jfree.layouting.input.style.parser.stylehandler.OneOfConstantsReadHandler;
import org.jfree.layouting.input.style.values.CSSAttrFunction;
import org.jfree.layouting.input.style.values.CSSConstant;
import org.jfree.layouting.input.style.values.CSSFunctionValue;
import org.jfree.layouting.input.style.values.CSSStringValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.input.style.values.CSSValueList;
import org.w3c.css.sac.LexicalUnit;

/**
 * Creation-Date: 01.12.2005, 18:21:29
 *
 * @author Thomas Morgner
 */
public class StringSetReadHandler extends OneOfConstantsReadHandler
{
  public StringSetReadHandler ()
  {
    super(false);
    addValue(ContentValues.CLOSE_QUOTE);
    addValue(ContentValues.CONTENTS);
    addValue(ContentValues.DOCUMENT_URL);
    addValue(ContentValues.ENDNOTE);
    addValue(ContentValues.FOOTNOTE);
    addValue(ContentValues.LISTITEM);
    addValue(ContentValues.NO_CLOSE_QUOTE);
    addValue(ContentValues.NO_OPEN_QUOTE);
    addValue(ContentValues.NONE);
    addValue(ContentValues.OPEN_QUOTE);
    addValue(ContentValues.SECTIONNOTE);

    addValue(ListStyleTypeGlyphs.BOX);
    addValue(ListStyleTypeGlyphs.CHECK);
    addValue(ListStyleTypeGlyphs.CIRCLE);
    addValue(ListStyleTypeGlyphs.DIAMOND);
    addValue(ListStyleTypeGlyphs.DISC);
    addValue(ListStyleTypeGlyphs.HYPHEN);
    addValue(ListStyleTypeGlyphs.SQUARE);
  }

  public CSSValue createValue(final StyleKey name, LexicalUnit value)
  {

    final ArrayList contents = new ArrayList();
    final ArrayList contentList = new ArrayList();
    while (value != null)
    {
      if (value.getLexicalUnitType() == LexicalUnit.SAC_IDENT)
      {
        final CSSValue o = lookupValue(value);
        if (o == null)
        {
          // parse error ...
          return null;
        }
        contentList.add(o);
      }
      else if (value.getLexicalUnitType() == LexicalUnit.SAC_STRING_VALUE)
      {
        contentList.add(new CSSConstant(value.getStringValue()));
      }
      else if (value.getLexicalUnitType() == LexicalUnit.SAC_URI)
      {
        final CSSStringValue uriValue = CSSValueFactory.createUriValue(value);
        if (uriValue == null)
        {
          return null;
        }
        contentList.add(uriValue);
      }
      else if (value.getLexicalUnitType() == LexicalUnit.SAC_FUNCTION ||
              value.getLexicalUnitType() == LexicalUnit.SAC_COUNTER_FUNCTION ||
              value.getLexicalUnitType() == LexicalUnit.SAC_COUNTERS_FUNCTION)
      {
        final CSSFunctionValue functionValue =
                CSSValueFactory.parseFunction(value);
        if (functionValue == null)
        {
          return null;
        }
        contentList.add(functionValue);
      }
      else if (value.getLexicalUnitType() == LexicalUnit.SAC_ATTR)
      {
        final CSSAttrFunction attrFn = CSSValueFactory.parseAttrFunction(value);
        if (attrFn == null)
        {
          return null;
        }
        contentList.add(attrFn);
      }
      else if (value.getLexicalUnitType() == LexicalUnit.SAC_OPERATOR_COMMA)
      {
        final CSSValue[] values =
                (CSSValue[]) contentList.toArray(
                        new CSSValue[contentList.size()]);
        final CSSValueList sequence = new CSSValueList(values);
        contents.add(sequence);
      }
      value = value.getNextLexicalUnit();
    }

    final CSSValue[] values =
            (CSSValue[]) contentList.toArray(new CSSValue[contentList.size()]);
    final CSSValueList sequence = new CSSValueList(values);
    contents.add(sequence);
    return new CSSValueList(contents);
  }

}
