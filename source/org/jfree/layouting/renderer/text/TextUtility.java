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
 * $Id: TextUtility.java 6489 2008-11-28 14:53:40Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.renderer.text;

import org.jfree.layouting.input.style.keys.line.AlignmentBaseline;
import org.jfree.layouting.input.style.keys.line.DominantBaseline;
import org.jfree.layouting.input.style.values.CSSAutoValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.pentaho.reporting.libraries.fonts.registry.BaselineInfo;
import org.pentaho.reporting.libraries.fonts.registry.FontMetrics;

/**
 * Creation-Date: 12.10.2006, 17:23:30
 *
 * @author Thomas Morgner
 */
public class TextUtility
{
  private TextUtility()
  {
  }


  public static int translateDominantBaseline(final CSSValue baseline, final int defaultValue)
  {
    if (baseline == null || CSSAutoValue.getInstance().equals(baseline))
    {
      return defaultValue;
    }

    if (DominantBaseline.ALPHABETIC.equals(baseline))
    {
      return ExtendedBaselineInfo.ALPHABETHIC;
    }
    if (DominantBaseline.CENTRAL.equals(baseline))
    {
      return ExtendedBaselineInfo.CENTRAL;
    }
    if (DominantBaseline.HANGING.equals(baseline))
    {
      return ExtendedBaselineInfo.HANGING;
    }
    if (DominantBaseline.IDEOGRAPHIC.equals(baseline))
    {
      return ExtendedBaselineInfo.IDEOGRAPHIC;
    }
    if (DominantBaseline.MATHEMATICAL.equals(baseline))
    {
      return ExtendedBaselineInfo.MATHEMATICAL;
    }
    if (DominantBaseline.MIDDLE.equals(baseline))
    {
      return ExtendedBaselineInfo.MIDDLE;
    }
    if (DominantBaseline.TEXT_AFTER_EDGE.equals(baseline))
    {
      return ExtendedBaselineInfo.TEXT_AFTER_EDGE;
    }
    if (DominantBaseline.TEXT_BEFORE_EDGE.equals(baseline))
    {
      return ExtendedBaselineInfo.TEXT_BEFORE_EDGE;
    }
    return defaultValue;
  }

  public static int translateAlignmentBaseline(final CSSValue baseline, final int defaultValue)
  {
    if (baseline == null || CSSAutoValue.getInstance().equals(baseline))
    {
      return defaultValue;
    }

    if (AlignmentBaseline.ALPHABETIC.equals(baseline))
    {
      return ExtendedBaselineInfo.ALPHABETHIC;
    }
    if (AlignmentBaseline.CENTRAL.equals(baseline))
    {
      return ExtendedBaselineInfo.CENTRAL;
    }
    if (AlignmentBaseline.HANGING.equals(baseline))
    {
      return ExtendedBaselineInfo.HANGING;
    }
    if (AlignmentBaseline.IDEOGRAPHIC.equals(baseline))
    {
      return ExtendedBaselineInfo.IDEOGRAPHIC;
    }
    if (AlignmentBaseline.MATHEMATICAL.equals(baseline))
    {
      return ExtendedBaselineInfo.MATHEMATICAL;
    }
    if (AlignmentBaseline.MIDDLE.equals(baseline))
    {
      return ExtendedBaselineInfo.MIDDLE;
    }
    if (AlignmentBaseline.TEXT_AFTER_EDGE.equals(baseline))
    {
      return ExtendedBaselineInfo.TEXT_AFTER_EDGE;
    }
    if (AlignmentBaseline.TEXT_BEFORE_EDGE.equals(baseline))
    {
      return ExtendedBaselineInfo.TEXT_BEFORE_EDGE;
    }
    if (AlignmentBaseline.AFTER_EDGE.equals(baseline))
    {
      return ExtendedBaselineInfo.AFTER_EDGE;
    }
    if (AlignmentBaseline.BEFORE_EDGE.equals(baseline))
    {
      return ExtendedBaselineInfo.BEFORE_EDGE;
    }
    return defaultValue;
  }

  public static int translateBaselines(final int baseline)
  {
    switch (baseline)
    {
      case BaselineInfo.HANGING:
        return ExtendedBaselineInfo.HANGING;
      case BaselineInfo.ALPHABETIC:
        return ExtendedBaselineInfo.ALPHABETHIC;
      case BaselineInfo.CENTRAL:
        return ExtendedBaselineInfo.CENTRAL;
      case BaselineInfo.IDEOGRAPHIC:
        return ExtendedBaselineInfo.IDEOGRAPHIC;
      case BaselineInfo.MATHEMATICAL:
        return ExtendedBaselineInfo.MATHEMATICAL;
      case BaselineInfo.MIDDLE:
        return ExtendedBaselineInfo.MIDDLE;
      default:
        throw new IllegalArgumentException("Invalid baseline");
    }
  }

  public static ExtendedBaselineInfo createBaselineInfo (final int codepoint, final FontMetrics fontMetrics)
  {
    final BaselineInfo baselineInfo = fontMetrics.getBaselines(codepoint, null);
    final int dominantBaseline =
        TextUtility.translateBaselines(baselineInfo.getDominantBaseline());
    final DefaultExtendedBaselineInfo extBaselineInfo =
        new DefaultExtendedBaselineInfo(dominantBaseline);

    final long[] baselines = new long[ExtendedBaselineInfo.BASELINE_COUNT];
    baselines[ExtendedBaselineInfo.ALPHABETHIC] = (baselineInfo.getBaseline(BaselineInfo.ALPHABETIC));
    baselines[ExtendedBaselineInfo.CENTRAL] = (baselineInfo.getBaseline(BaselineInfo.CENTRAL));
    baselines[ExtendedBaselineInfo.HANGING] = (baselineInfo.getBaseline(BaselineInfo.HANGING));
    baselines[ExtendedBaselineInfo.IDEOGRAPHIC] = (baselineInfo.getBaseline(BaselineInfo.IDEOGRAPHIC));
    baselines[ExtendedBaselineInfo.MATHEMATICAL] = (baselineInfo.getBaseline(BaselineInfo.MATHEMATICAL));
    baselines[ExtendedBaselineInfo.MIDDLE] = (baselineInfo.getBaseline(BaselineInfo.MIDDLE));
    baselines[ExtendedBaselineInfo.BEFORE_EDGE] = 0;
    baselines[ExtendedBaselineInfo.TEXT_BEFORE_EDGE] = 0;
    baselines[ExtendedBaselineInfo.TEXT_AFTER_EDGE] = (fontMetrics.getMaxHeight());
    baselines[ExtendedBaselineInfo.AFTER_EDGE] = baselines[ExtendedBaselineInfo.TEXT_AFTER_EDGE];
    extBaselineInfo.setBaselines(baselines);
    return extBaselineInfo;
  }
}
