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
 * $Id: GraphicsOutputProcessor.java 6489 2008-11-28 14:53:40Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.layouting.modules.output.graphics;

import org.jfree.layouting.output.OutputProcessorMetaData;
import org.jfree.layouting.output.pageable.AbstractPageableProcessor;
import org.jfree.layouting.output.pageable.LogicalPageKey;
import org.jfree.layouting.output.pageable.PageFlowSelector;
import org.jfree.layouting.output.pageable.PhysicalPageKey;
import org.jfree.layouting.renderer.model.page.LogicalPageBox;
import org.jfree.layouting.renderer.model.page.PageGrid;
import org.jfree.layouting.renderer.model.page.PhysicalPageBox;
import org.pentaho.reporting.libraries.base.config.Configuration;
import org.pentaho.reporting.libraries.fonts.registry.DefaultFontStorage;
import org.pentaho.reporting.libraries.fonts.awt.AWTFontRegistry;

/**
 * Creation-Date: 02.01.2006, 19:55:14
 *
 * @author Thomas Morgner
 */
public class GraphicsOutputProcessor extends AbstractPageableProcessor
{
  private OutputProcessorMetaData metaData;
  private GraphicsContentInterceptor interceptor;

  public GraphicsOutputProcessor(final Configuration configuration)
  {
    super(configuration);
    final DefaultFontStorage fontStorage = new DefaultFontStorage(new AWTFontRegistry());
    metaData = new GraphicsOutputProcessorMetaData(fontStorage, false);
  }

  public OutputProcessorMetaData getMetaData()
  {
    return metaData;
  }

  public GraphicsContentInterceptor getInterceptor()
  {
    return interceptor;
  }

  public void setInterceptor(final GraphicsContentInterceptor interceptor)
  {
    this.interceptor = interceptor;
  }

  protected PageFlowSelector getFlowSelector()
  {
    return getInterceptor();
  }

  protected void processPhysicalPage(final PageGrid pageGrid,
                                     final LogicalPageBox logicalPage,
                                     final int row,
                                     final int col,
                                     final PhysicalPageKey pageKey)
  {
    final PhysicalPageBox page = pageGrid.getPage(row, col);
    final LogicalPageDrawable drawable = new LogicalPageDrawable(logicalPage);
    final PhysicalPageDrawable pageDrawable =
        new PhysicalPageDrawable(drawable, page);
    interceptor.processPhysicalPage(pageKey, pageDrawable);
  }

  protected void processLogicalPage (final LogicalPageKey key, final LogicalPageBox logicalPage)
  {
    final LogicalPageDrawable drawable = new LogicalPageDrawable(logicalPage);
    interceptor.processLogicalPage(key, drawable);
  }
}
