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
 * $Id: DefaultExtendedBaselineInfo.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.renderer.text;

/**
 * Creation-Date: 24.07.2006, 17:35:25
 *
 * @author Thomas Morgner
 */
public class DefaultExtendedBaselineInfo implements ExtendedBaselineInfo
{
  private long[] baselines;
  private int dominantBaseline;

  public DefaultExtendedBaselineInfo(final int dominantBaseline)
  {
    this.baselines = new long[ExtendedBaselineInfo.BASELINE_COUNT];
    this.dominantBaseline = dominantBaseline;
  }

  public int getDominantBaseline()
  {
    return dominantBaseline;
  }

  public long[] getBaselines()
  {
    return baselines;
  }

  public void setBaselines(final long[] baselines)
  {
    if (baselines.length != ExtendedBaselineInfo.BASELINE_COUNT)
    {
      throw new IllegalArgumentException();
    }
    System.arraycopy(baselines, 0, this.baselines, 0, ExtendedBaselineInfo.BASELINE_COUNT);
  }

  public long getBaseline(final int baseline)
  {
    return baselines[baseline];
  }

  public ExtendedBaselineInfo shift(final long s)
  {
    final DefaultExtendedBaselineInfo info =
        new DefaultExtendedBaselineInfo(dominantBaseline);
    info.baselines = (long[]) baselines.clone();
    for (int i = 0; i < baselines.length; i++)
    {
      info.baselines[i] += s;
    }
    return info;
  }
}
