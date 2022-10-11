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
 * $Id: ImageBackground.java 6489 2008-11-28 14:53:40Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.renderer.background;

import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.renderer.border.RenderLength;
import org.jfree.layouting.util.geom.StrictDimension;
import org.pentaho.reporting.libraries.resourceloader.ResourceKey;

/**
 * A complex background.
 *
 * @author Thomas Morgner
 */
public class ImageBackground extends SimpleBackground
{
  private Object resource;
  private StrictDimension contentSize;
  private ResourceKey source;
  private RenderLength width;
  private RenderLength height;
  private RenderLength x;
  private RenderLength y;
  private CSSValue repeatX; // repeat, no-repeat, space
  private CSSValue repeatY;
  private CSSValue originX; // border, padding, content
  private CSSValue originY;
  private CSSValue clipX;
  private CSSValue clipY;

  public ImageBackground()
  {
    super(null);
  }

  public Object getResource()
  {
    return resource;
  }

  public void setResource(final Object resource)
  {
    this.resource = resource;
  }

  public StrictDimension getContentSize()
  {
    return contentSize;
  }

  public void setContentSize(final StrictDimension contentSize)
  {
    this.contentSize = contentSize;
  }

  public ResourceKey getSource()
  {
    return source;
  }

  public void setSource(final ResourceKey source)
  {
    this.source = source;
  }

  public RenderLength getWidth()
  {
    return width;
  }

  public void setWidth(final RenderLength width)
  {
    this.width = width;
  }

  public RenderLength getHeight()
  {
    return height;
  }

  public void setHeight(final RenderLength height)
  {
    this.height = height;
  }

  public RenderLength getX()
  {
    return x;
  }

  public void setX(final RenderLength x)
  {
    this.x = x;
  }

  public RenderLength getY()
  {
    return y;
  }

  public void setY(final RenderLength y)
  {
    this.y = y;
  }

  public CSSValue getRepeatX()
  {
    return repeatX;
  }

  public void setRepeatX(final CSSValue repeatX)
  {
    this.repeatX = repeatX;
  }

  public CSSValue getRepeatY()
  {
    return repeatY;
  }

  public void setRepeatY(final CSSValue repeatY)
  {
    this.repeatY = repeatY;
  }

  public CSSValue getOriginX()
  {
    return originX;
  }

  public void setOriginX(final CSSValue originX)
  {
    this.originX = originX;
  }

  public CSSValue getOriginY()
  {
    return originY;
  }

  public void setOriginY(final CSSValue originY)
  {
    this.originY = originY;
  }

  public CSSValue getClipX()
  {
    return clipX;
  }

  public void setClipX(final CSSValue clipX)
  {
    this.clipX = clipX;
  }

  public CSSValue getClipY()
  {
    return clipY;
  }

  public void setClipY(final CSSValue clipY)
  {
    this.clipY = clipY;
  }

}
