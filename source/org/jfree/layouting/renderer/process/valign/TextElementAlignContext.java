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
 * $Id: TextElementAlignContext.java 2755 2007-04-10 19:27:09Z taqua $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.renderer.process.valign;

import org.jfree.layouting.renderer.model.RenderableText;
import org.jfree.layouting.renderer.text.ExtendedBaselineInfo;

/**
 * Creation-Date: 13.10.2006, 22:26:50
 *
 * @author Thomas Morgner
 */
public class TextElementAlignContext extends AlignContext
{
  private long[] baselines;
  private long baselineShift;

  public TextElementAlignContext(final RenderableText text)
  {
    super(text);
    final ExtendedBaselineInfo baselineInfo = text.getBaselineInfo();
    this.baselines = baselineInfo.getBaselines();
    setDominantBaseline(baselineInfo.getDominantBaseline());
  }

  public long getBaselineDistance(final int baseline)
  {
    return (baselines[baseline] - baselines[getDominantBaseline()]) + baselineShift;
  }

  public void shift(final long delta)
  {
    baselineShift += delta;
  }

  public long getAfterEdge()
  {
    return this.baselines[ExtendedBaselineInfo.AFTER_EDGE] + baselineShift;
  }

  public long getBeforeEdge()
  {
    return this.baselines[ExtendedBaselineInfo.BEFORE_EDGE] + baselineShift;
  }
}
