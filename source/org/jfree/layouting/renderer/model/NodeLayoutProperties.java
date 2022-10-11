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
 * $Id: NodeLayoutProperties.java 2739 2007-04-02 11:41:22Z taqua $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.renderer.model;

import java.io.Serializable;

import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.renderer.border.RenderLength;

/**
 * A static properties collection. That one is static; once computed it does
 * not change anymore. It does not (under no thinkable circumstances) depend
 * on the given content. It may depend on static content of the parent.
 *
 * @author Thomas Morgner
 */
public class NodeLayoutProperties implements Serializable, Cloneable
{
  // ComputedMetrics:

  // Fully static properties ...
  private CSSValue alignmentBaseline;
  private CSSValue alignmentAdjust;
  private CSSValue baselineShift;
  private CSSValue verticalAlignment;
  private RenderLength baselineShiftResolved;
  private RenderLength alignmentAdjustResolved;

  private String namespace;
  private String tagName;

  private int majorAxis;
  private int minorAxis;

  public NodeLayoutProperties()
  {
  }

//
//  public boolean isIntrinsic()
//  {
//    return intrinsic;
//  }
//
//  public void setIntrinsic(final boolean intrinsic)
//  {
//    this.intrinsic = intrinsic;
//  }

  public int getMajorAxis()
  {
    return majorAxis;
  }

  public void setMajorAxis(final int majorAxis)
  {
    this.majorAxis = majorAxis;
  }

  public int getMinorAxis()
  {
    return minorAxis;
  }

  public void setMinorAxis(final int minorAxis)
  {
    this.minorAxis = minorAxis;
  }

  public Object clone () throws CloneNotSupportedException
  {
    return super.clone();
  }

  public CSSValue getAlignmentBaseline()
  {
    return alignmentBaseline;
  }

  public void setAlignmentBaseline(final CSSValue alignmentBaseline)
  {
    this.alignmentBaseline = alignmentBaseline;
  }

  public CSSValue getAlignmentAdjust()
  {
    return alignmentAdjust;
  }

  public void setAlignmentAdjust(final CSSValue alignmentAdjust)
  {
    this.alignmentAdjust = alignmentAdjust;
  }

  public CSSValue getBaselineShift()
  {
    return baselineShift;
  }

  public void setBaselineShift(final CSSValue baselineShift)
  {
    this.baselineShift = baselineShift;
  }

  public CSSValue getVerticalAlignment()
  {
    return verticalAlignment;
  }

  public void setVerticalAlignment(final CSSValue verticalAlignment)
  {
    this.verticalAlignment = verticalAlignment;
  }

  public RenderLength getBaselineShiftResolved()
  {
    return baselineShiftResolved;
  }

  public void setBaselineShiftResolved(final RenderLength baselineShiftResolved)
  {
    this.baselineShiftResolved = baselineShiftResolved;
  }

  public RenderLength getAlignmentAdjustResolved()
  {
    return alignmentAdjustResolved;
  }

  public void setAlignmentAdjustResolved(final RenderLength alignmentAdjustResolved)
  {
    this.alignmentAdjustResolved = alignmentAdjustResolved;
  }

  public String getNamespace()
  {
    return namespace;
  }

  public void setNamespace(final String namespace)
  {
    this.namespace = namespace;
  }

  public String getTagName()
  {
    return tagName;
  }

  public void setTagName(final String tagName)
  {
    this.tagName = tagName;
  }
}
