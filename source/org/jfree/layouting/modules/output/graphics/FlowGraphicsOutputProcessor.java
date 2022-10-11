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
 * $Id: FlowGraphicsOutputProcessor.java 6489 2008-11-28 14:53:40Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.layouting.modules.output.graphics;

import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.output.AbstractOutputProcessor;
import org.jfree.layouting.output.OutputProcessorMetaData;
import org.jfree.layouting.output.pageable.LogicalPageKey;
import org.jfree.layouting.renderer.PaginatingRenderer;
import org.jfree.layouting.renderer.PrototypeBuildingRenderer;
import org.jfree.layouting.renderer.Renderer;
import org.jfree.layouting.renderer.model.page.LogicalPageBox;
import org.pentaho.reporting.libraries.base.config.Configuration;
import org.pentaho.reporting.libraries.fonts.registry.DefaultFontStorage;
import org.pentaho.reporting.libraries.fonts.awt.AWTFontRegistry;

/**
 * Creation-Date: 02.01.2006, 19:55:14
 *
 * @author Thomas Morgner
 */
public class FlowGraphicsOutputProcessor extends AbstractOutputProcessor
{
  private OutputProcessorMetaData metaData;
  private GraphicsContentInterceptor interceptor;
  private PrototypeBuildingRenderer prototypeBuilder;

  public FlowGraphicsOutputProcessor(final Configuration configuration)
  {
    super(configuration);
    final DefaultFontStorage fontStorage = new DefaultFontStorage(new AWTFontRegistry());

    // Todo:
    metaData = new GraphicsOutputProcessorMetaData(fontStorage, true);
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

  protected void processPageContent(final LogicalPageKey logicalPageKey,
                                    final LogicalPageBox logicalPage)
  {
    if (interceptor != null)
    {
      final LogicalPageDrawable page = new LogicalPageDrawable(logicalPage);
      interceptor.processLogicalPage(logicalPageKey, page);
    }
  }

  public PrototypeBuildingRenderer getPrototypeBuilder()
  {
    return prototypeBuilder;
  }

  public Renderer createRenderer(final LayoutProcess layoutProcess)
  {
    if (isGlobalStateComputed() == false)
    {
      prototypeBuilder = new PrototypeBuildingRenderer(layoutProcess);
      return prototypeBuilder;
    }
    else
    {
      //return new PrintingRenderer (new PaginatingRenderer(layoutProcess));
      return new PaginatingRenderer(layoutProcess);
    }
  }


}
