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
 * $Id: VerticalAlignReadHandler.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.input.style.parser.stylehandler.line;

import java.util.HashMap;
import java.util.Map;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.keys.line.AlignmentBaseline;
import org.jfree.layouting.input.style.keys.line.BaselineShift;
import org.jfree.layouting.input.style.keys.line.LineStyleKeys;
import org.jfree.layouting.input.style.keys.line.VerticalAlign;
import org.jfree.layouting.input.style.parser.CSSCompoundValueReadHandler;
import org.jfree.layouting.input.style.parser.CSSValueFactory;
import org.jfree.layouting.input.style.parser.stylehandler.OneOfConstantsReadHandler;
import org.jfree.layouting.input.style.values.CSSAutoValue;
import org.jfree.layouting.input.style.values.CSSNumericType;
import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.w3c.css.sac.LexicalUnit;

/**
 * Warning: This *is* a compound property, but one if its values depend on
 * the element structure and it changes its meaning if used in Tables.
 *
 * @author Thomas Morgner
 */
public class VerticalAlignReadHandler extends OneOfConstantsReadHandler
  implements CSSCompoundValueReadHandler
{
  public VerticalAlignReadHandler()
  {
    super(true);
    addValue(VerticalAlign.BASELINE);
    addValue(VerticalAlign.BOTTOM);
    addValue(VerticalAlign.CENTRAL);
    addValue(VerticalAlign.MIDDLE);
    addValue(VerticalAlign.SUB);
    addValue(VerticalAlign.SUPER);
    addValue(VerticalAlign.TEXT_BOTTOM);
    addValue(VerticalAlign.TEXT_TOP);
    addValue(VerticalAlign.USE_SCRIPT);
    addValue(VerticalAlign.TOP);
  }

  protected CSSValue lookupValue(final LexicalUnit value)
  {
    final CSSValue constant = super.lookupValue(value);
    if (constant != null)
    {
      return constant;
    }
    else if (value.getLexicalUnitType() == LexicalUnit.SAC_PERCENTAGE)
    {
      return CSSNumericValue.createValue(CSSNumericType.PERCENTAGE,
              value.getFloatValue());
    }

    return CSSValueFactory.createLengthValue(value);
  }

  /**
   * Parses the LexicalUnit and returns a map of (StyleKey, CSSValue) pairs.
   *
   * @param unit
   * @return
   */
  public Map createValues(final LexicalUnit unit)
  {
    final CSSValue value = lookupValue(unit);
    final HashMap map = new HashMap();
    map.put(LineStyleKeys.VERTICAL_ALIGN, value);
    map.put(LineStyleKeys.ALIGNMENT_ADJUST, CSSAutoValue.getInstance());
    map.put(LineStyleKeys.DOMINANT_BASELINE, CSSAutoValue.getInstance());

    if (CSSAutoValue.getInstance().equals(value))
    {
      map.put(LineStyleKeys.ALIGNMENT_BASELINE, AlignmentBaseline.BASELINE);
      map.put(LineStyleKeys.BASELINE_SHIFT, BaselineShift.BASELINE);
    }
    else if (VerticalAlign.BASELINE.equals(value))
    {
      map.put(LineStyleKeys.ALIGNMENT_BASELINE, AlignmentBaseline.USE_SCRIPT);
      map.put(LineStyleKeys.BASELINE_SHIFT, BaselineShift.BASELINE);
    }
    else if (VerticalAlign.BOTTOM.equals(value))
    {
      map.put(LineStyleKeys.ALIGNMENT_BASELINE, AlignmentBaseline.AFTER_EDGE);
      map.put(LineStyleKeys.BASELINE_SHIFT, BaselineShift.BASELINE);
    }
    else if (VerticalAlign.CENTRAL.equals(value))
    {
      map.put(LineStyleKeys.ALIGNMENT_BASELINE, AlignmentBaseline.CENTRAL);
      map.put(LineStyleKeys.BASELINE_SHIFT, BaselineShift.BASELINE);
    }
    else if (VerticalAlign.MIDDLE.equals(value))
    {
      map.put(LineStyleKeys.ALIGNMENT_BASELINE, AlignmentBaseline.MIDDLE);
      map.put(LineStyleKeys.BASELINE_SHIFT, BaselineShift.BASELINE);
    }
    else if (VerticalAlign.SUB.equals(value))
    {
      map.put(LineStyleKeys.ALIGNMENT_BASELINE, AlignmentBaseline.USE_SCRIPT);
      map.put(LineStyleKeys.BASELINE_SHIFT, BaselineShift.SUB);
    }
    else if (VerticalAlign.SUPER.equals(value))
    {
      map.put(LineStyleKeys.ALIGNMENT_BASELINE, AlignmentBaseline.USE_SCRIPT);
      map.put(LineStyleKeys.BASELINE_SHIFT, BaselineShift.SUPER);
    }
    else if (VerticalAlign.TEXT_BOTTOM.equals(value))
    {
      map.put(LineStyleKeys.ALIGNMENT_BASELINE, AlignmentBaseline.TEXT_AFTER_EDGE);
      map.put(LineStyleKeys.BASELINE_SHIFT, BaselineShift.BASELINE);
    }
    else if (VerticalAlign.TEXT_TOP.equals(value))
    {
      map.put(LineStyleKeys.ALIGNMENT_BASELINE, AlignmentBaseline.TEXT_BEFORE_EDGE);
      map.put(LineStyleKeys.BASELINE_SHIFT, BaselineShift.BASELINE);
    }
    else if (VerticalAlign.TOP.equals(value))
    {
      map.put(LineStyleKeys.ALIGNMENT_BASELINE, AlignmentBaseline.BEFORE_EDGE);
      map.put(LineStyleKeys.BASELINE_SHIFT, BaselineShift.BASELINE);
    }
    else if (VerticalAlign.USE_SCRIPT.equals(value))
    {
      map.put(LineStyleKeys.ALIGNMENT_BASELINE, AlignmentBaseline.USE_SCRIPT);
      map.put(LineStyleKeys.BASELINE_SHIFT, CSSAutoValue.getInstance());
    }
    else
    {
      // Todo: handle the case when valign is a length or percentage
      map.put(LineStyleKeys.ALIGNMENT_BASELINE, AlignmentBaseline.BASELINE);
      map.put(LineStyleKeys.BASELINE_SHIFT, BaselineShift.BASELINE);
      map.put(LineStyleKeys.ALIGNMENT_ADJUST, value);
    }
    return map;
  }

  public StyleKey[] getAffectedKeys()
  {
    return new StyleKey[] {
        LineStyleKeys.VERTICAL_ALIGN,
        LineStyleKeys.ALIGNMENT_BASELINE,
        LineStyleKeys.DOMINANT_BASELINE,
        LineStyleKeys.ALIGNMENT_ADJUST,
        LineStyleKeys.BASELINE_SHIFT
    };
  }
}
