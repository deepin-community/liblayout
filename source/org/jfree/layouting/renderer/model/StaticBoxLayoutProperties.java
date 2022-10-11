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
 * $Id: StaticBoxLayoutProperties.java 2755 2007-04-10 19:27:09Z taqua $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.renderer.model;

import java.io.Serializable;

import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.renderer.text.ExtendedBaselineInfo;

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
public class StaticBoxLayoutProperties implements Serializable
{
  private CSSValue dominantBaseline;
  private ExtendedBaselineInfo nominalBaselineInfo;
  private int widows;
  private int orphans;
  private boolean avoidPagebreakInside;
  private boolean preserveSpace;

  public StaticBoxLayoutProperties()
  {
  }

  public CSSValue getDominantBaseline()
  {
    return dominantBaseline;
  }

  public void setDominantBaseline(final CSSValue dominantBaseline)
  {
    this.dominantBaseline = dominantBaseline;
  }

  public ExtendedBaselineInfo getNominalBaselineInfo()
  {
    return nominalBaselineInfo;
  }

  public void setNominalBaselineInfo(final ExtendedBaselineInfo nominalBaselineInfo)
  {
    this.nominalBaselineInfo = nominalBaselineInfo;
  }

  public int getWidows()
  {
    return widows;
  }

  public void setWidows(final int widows)
  {
    this.widows = widows;
  }

  public int getOrphans()
  {
    return orphans;
  }

  public void setOrphans(final int orphans)
  {
    this.orphans = orphans;
  }

  public boolean isAvoidPagebreakInside()
  {
    return avoidPagebreakInside;
  }

  public void setAvoidPagebreakInside(final boolean avoidPagebreakInside)
  {
    this.avoidPagebreakInside = avoidPagebreakInside;
  }

  public boolean isPreserveSpace()
  {
    return preserveSpace;
  }

  public void setPreserveSpace(final boolean preserveSpace)
  {
    this.preserveSpace = preserveSpace;
  }


  public String toString()
  {
    return "StaticBoxLayoutProperties{" +
        "dominantBaseline=" + dominantBaseline +
        ", nominalBaselineInfo=" + nominalBaselineInfo +
        ", widows=" + widows +
        ", orphans=" + orphans +
        ", avoidPagebreakInside=" + avoidPagebreakInside +
        ", preserveSpace=" + preserveSpace +
        '}';
  }
}
