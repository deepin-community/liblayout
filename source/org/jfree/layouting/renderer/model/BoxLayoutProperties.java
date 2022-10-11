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
 * $Id: BoxLayoutProperties.java 2739 2007-04-02 11:41:22Z taqua $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.renderer.model;

import java.io.Serializable;

/**
 * A static properties collection. That one is static; once computed it does
 * not change anymore. It does not (under no thinkable circumstances) depend
 * on the given content. It may depend on static content of the parent.
 *
 * A box typically has two sets of margins. The first set is the declared
 * margin set - it simply expresses the user's definitions. The second set
 * is the effective margin set, it is based on the context of the element in
 * the document tree and denotes the distance between the nodes edge and any
 * oposite edge.
 *
 * @author Thomas Morgner
 */
public class BoxLayoutProperties
    implements Serializable, Cloneable
{
  private boolean infiniteMarginTop;
  private boolean infiniteMarginBottom;
  private long effectiveMarginTop;
  private long effectiveMarginBottom;

  private long marginOpenState;
  private long marginCloseState;
  private long marginState;

//  private int dominantBaseline;

  public BoxLayoutProperties()
  {
  }

  public long getEffectiveMarginTop()
  {
    return effectiveMarginTop;
  }

  public void setEffectiveMarginTop(final long effectiveMarginTop)
  {
    this.effectiveMarginTop = effectiveMarginTop;
  }

  public long getEffectiveMarginBottom()
  {
    return effectiveMarginBottom;
  }

  public void setEffectiveMarginBottom(final long effectiveMarginBottom)
  {
    this.effectiveMarginBottom = effectiveMarginBottom;
  }

  public String toString()
  {
    return "BoxLayoutProperties{" +
            "effectiveMarginTop=" + effectiveMarginTop +
            ", effectiveMarginBottom=" + effectiveMarginBottom +
            '}';
  }

  public long getMarginState()
  {
    return marginState;
  }

  public void setMarginState(final long marginState)
  {
    this.marginState = marginState;
  }

  public long getMarginOpenState()
  {
    return marginOpenState;
  }

  public void setMarginOpenState(final long marginOpenState)
  {
    this.marginOpenState = marginOpenState;
  }

  public long getMarginCloseState()
  {
    return marginCloseState;
  }

  public void setMarginCloseState(final long marginCloseState)
  {
    this.marginCloseState = marginCloseState;
  }

  public boolean isInfiniteMarginTop()
  {
    return infiniteMarginTop;
  }

  public void setInfiniteMarginTop(final boolean infiniteMarginTop)
  {
    this.infiniteMarginTop = infiniteMarginTop;
  }

  public boolean isInfiniteMarginBottom()
  {
    return infiniteMarginBottom;
  }

  public void setInfiniteMarginBottom(final boolean infiniteMarginBottom)
  {
    this.infiniteMarginBottom = infiniteMarginBottom;
  }

  public Object clone() throws CloneNotSupportedException
  {
    return super.clone();
  }
}
