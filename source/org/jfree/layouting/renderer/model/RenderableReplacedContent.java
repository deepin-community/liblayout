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
 * $Id: RenderableReplacedContent.java 6489 2008-11-28 14:53:40Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.renderer.model;

import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.renderer.border.RenderLength;
import org.jfree.layouting.util.geom.StrictDimension;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.output.OutputProcessorMetaData;
import org.pentaho.reporting.libraries.resourceloader.ResourceKey;

/**
 * This handles all kinds of renderable rectangular content, mostly images and
 * drawables. It is assumed, that the image can be split on any position,
 * although this is avoided as far as possible.
 *
 * Flame me, but 'crop', 'fit' and 'fit-position' will be implemented later.
 * Yes, they are powerfull, but nothing I want to deal with for the initial
 * throw.
 *
 * @see http://www.w3.org/TR/css3-box/#the-fit
 * @author Thomas Morgner
 */
public class RenderableReplacedContent extends RenderNode
{
  private Object rawObject;
  private StrictDimension contentSize;
  private CSSValue verticalAlign;
  private ResourceKey source;
  private RenderLength requestedWidth;
  private RenderLength requestedHeight;

  public RenderableReplacedContent(final Object rawObject,
                                   final ResourceKey source,
                                   final StrictDimension contentSize,
                                   final RenderLength width,
                                   final RenderLength height,
                                   final CSSValue verticalAlign)
  {
    if (rawObject == null)
    {
      throw new NullPointerException("Raw-Object for Replaced content must not be null");
    }
    if (contentSize == null)
    {
      throw new NullPointerException("Content-Size cannot be null");
    }
    if (width == null)
    {
      throw new NullPointerException("Requested width cannot be null");
    }
    if (height == null)
    {
      throw new NullPointerException("Requested height cannot be null");
    }

    this.requestedHeight = height;
    this.requestedWidth = width;
    this.rawObject = rawObject;
    this.source = source;
    this.contentSize = contentSize;
    this.verticalAlign = verticalAlign;
  }

  public void appyStyle(final LayoutContext context, final OutputProcessorMetaData metaData)
  {
    super.appyStyle(context, metaData);
  }

  public Object getRawObject()
  {
    return rawObject;
  }

  public StrictDimension getContentSize()
  {
    return contentSize;
  }

  public ResourceKey getSource()
  {
    return source;
  }

  public RenderLength getRequestedWidth()
  {
    return requestedWidth;
  }

  public RenderLength getRequestedHeight()
  {
    return requestedHeight;
  }

  public CSSValue getVerticalAlign()
  {
    return verticalAlign;
  }
}
