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
 * $Id: BoxAlignContext.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.renderer.process.valign;

import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.renderer.model.RenderBox;
import org.jfree.layouting.renderer.model.StaticBoxLayoutProperties;
import org.jfree.layouting.renderer.model.ComputedLayoutProperties;
import org.jfree.layouting.renderer.text.ExtendedBaselineInfo;
import org.jfree.layouting.renderer.text.TextUtility;

/**
 * Creation-Date: 13.10.2006, 22:22:10
 *
 * @author Thomas Morgner
 */
public class BoxAlignContext extends AlignContext
{
  private long insetsTop;
  private long insetsBottom;
  private long[] baselines;
  private AlignContext firstChild;
  private AlignContext lastChild;

  public BoxAlignContext(final RenderBox box)
  {
    super(box);

    ExtendedBaselineInfo baselineInfo = box.getBaselineInfo();
    if (baselineInfo == null)
    {
      baselineInfo = box.getNominalBaselineInfo();
    }

    final CSSValue dominantBaselineValue = box.getDominantBaseline();
    setDominantBaseline(TextUtility.translateDominantBaseline
        (dominantBaselineValue, baselineInfo.getDominantBaseline()));

    final ComputedLayoutProperties blp = box.getComputedLayoutProperties();
    insetsTop = blp.getBorderTop() + blp.getPaddingTop();
    insetsBottom = blp.getBorderBottom() + blp.getPaddingBottom();

    baselines = (long[]) baselineInfo.getBaselines().clone();
    for (int i = 1; i < baselines.length; i++)
    {
      baselines[i] += insetsTop;
    }
    baselines[ExtendedBaselineInfo.AFTER_EDGE] =
        baselines[ExtendedBaselineInfo.TEXT_AFTER_EDGE] + insetsBottom;
  }

  public void addChild(final AlignContext context)
  {
    if (lastChild == null)
    {
      firstChild = context;
      lastChild = context;
      return;
    }
    lastChild.setNext(context);
    lastChild = context;
  }

  public AlignContext getFirstChild()
  {
    return firstChild;
  }

  public long getInsetsTop()
  {
    return insetsTop;
  }

  public long getInsetsBottom()
  {
    return insetsBottom;
  }

  public long getBaselineDistance(final int baseline)
  {
    return baselines[baseline] - baselines[getDominantBaseline()];
  }

  public void shift(final long delta)
  {
    for (int i = 0; i < baselines.length; i++)
    {
      baselines[i] += delta;
    }

    AlignContext child = getFirstChild();
    while (child != null)
    {
      child.shift(delta);
      child = child.getNext();
    }
  }

  public long getAfterEdge()
  {
    return this.baselines[ExtendedBaselineInfo.AFTER_EDGE];
  }

  public long getBeforeEdge()
  {
    return this.baselines[ExtendedBaselineInfo.BEFORE_EDGE];
  }

  public void setBeforeEdge(final long offset)
  {
    this.baselines[ExtendedBaselineInfo.BEFORE_EDGE] = offset;
  }

  public void setAfterEdge(final long offset)
  {
    this.baselines[ExtendedBaselineInfo.AFTER_EDGE] = offset;
  }
}
