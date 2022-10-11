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
 * $Id: PhysicalPageDrawable.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.layouting.modules.output.graphics;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;

import org.jfree.layouting.renderer.model.page.PhysicalPageBox;
import org.jfree.layouting.util.geom.StrictGeomUtility;

/**
 * Creation-Date: 17.11.2006, 18:00:46
 *
 * @author Thomas Morgner
 */
public class PhysicalPageDrawable implements PageDrawable
{
  private LogicalPageDrawable pageDrawable;
  private PageFormat pageFormat;
  private long globalX;
  private long globalY;

  public PhysicalPageDrawable(final LogicalPageDrawable pageDrawable,
                              final PhysicalPageBox page)
  {
    this.pageDrawable = pageDrawable;
    this.pageFormat = new PageFormat();

    this.globalX = page.getGlobalX();
    this.globalY = page.getGlobalY();

    if (page.getWidth() < page.getHeight())
    {
      final Paper p = new Paper();
      p.setSize(StrictGeomUtility.toExternalValue(page.getWidth()),
          StrictGeomUtility.toExternalValue(page.getHeight()));
      p.setImageableArea(StrictGeomUtility.toExternalValue(page.getImageableX()),
          StrictGeomUtility.toExternalValue(page.getImageableY()),
          StrictGeomUtility.toExternalValue(page.getImageableWidth()),
          StrictGeomUtility.toExternalValue(page.getImageableHeight()));
      this.pageFormat.setPaper(p);
      this.pageFormat.setOrientation(PageFormat.PORTRAIT);
    }
    else
    {
      final Paper p = new Paper();
      p.setSize(StrictGeomUtility.toExternalValue(page.getHeight()),
          StrictGeomUtility.toExternalValue(page.getWidth()));
      p.setImageableArea(StrictGeomUtility.toExternalValue(page.getImageableY()),
          StrictGeomUtility.toExternalValue(page.getImageableX()),
          StrictGeomUtility.toExternalValue(page.getImageableHeight()),
          StrictGeomUtility.toExternalValue(page.getImageableWidth()));
      this.pageFormat.setPaper(p);
      this.pageFormat.setOrientation(PageFormat.LANDSCAPE);
    }
  }

  public PageFormat getPageFormat()
  {
    return pageFormat;
  }

  public Dimension getPreferredSize()
  {
    return new Dimension ((int) pageFormat.getWidth(), (int) pageFormat.getHeight());
  }

  public boolean isPreserveAspectRatio()
  {
    return true;
  }

  /**
   * Draws the object.
   *
   * @param g2   the graphics device.
   * @param area the area inside which the object should be drawn.
   */
  public void draw(final Graphics2D g2, final Rectangle2D area)
  {
    g2.translate(globalX, globalY);
    g2.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

    pageDrawable.draw(g2, new Rectangle2D.Double
        (0, 0, pageFormat.getImageableWidth(), pageFormat.getImageableHeight()));
    g2.dispose();

  }
}
