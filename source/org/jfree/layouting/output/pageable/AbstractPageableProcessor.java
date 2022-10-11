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
 * $Id: AbstractPageableProcessor.java 6489 2008-11-28 14:53:40Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.layouting.output.pageable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.output.AbstractOutputProcessor;
import org.jfree.layouting.renderer.PaginatingRenderer;
import org.jfree.layouting.renderer.PrototypeBuildingRenderer;
import org.jfree.layouting.renderer.Renderer;
import org.jfree.layouting.renderer.model.page.LogicalPageBox;
import org.jfree.layouting.renderer.model.page.PageGrid;
import org.pentaho.reporting.libraries.base.config.Configuration;

/**
 * Creation-Date: 10.11.2006, 20:05:11
 *
 * @author Thomas Morgner
 */
public abstract class AbstractPageableProcessor extends AbstractOutputProcessor
    implements PageableOutputProcessor
{
  private List physicalPages;
  private PrototypeBuildingRenderer prototypeBuilder;

  protected AbstractPageableProcessor(final Configuration configuration)
  {
    super(configuration);
    this.physicalPages = new ArrayList();
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

  public PrototypeBuildingRenderer getPrototypeBuilder()
  {
    return prototypeBuilder;
  }

  protected void processingPagesFinished()
  {
    super.processingPagesFinished();
    physicalPages = Collections.unmodifiableList(physicalPages);
  }

  public int getPhysicalPageCount()
  {
    return physicalPages.size();
  }

  public PhysicalPageKey getPhysicalPage(final int page)
  {
    if (isPaginationFinished() == false)
    {
      throw new IllegalStateException();
    }

    return (PhysicalPageKey) physicalPages.get(page);
  }

  protected LogicalPageKey createLogicalPage(final int width,
                                             final int height)
  {
    final LogicalPageKey key = super.createLogicalPage(width, height);
    for (int h = 0; h < key.getHeight(); h++)
    {
      for (int w = 0; w < key.getWidth(); w++)
      {
        physicalPages.add(key.getPage(w, h));
      }
    }
    return key;
  }

  protected void processPageContent(final LogicalPageKey logicalPageKey,
                                    final LogicalPageBox logicalPage)
  {
    final PageGrid pageGrid = logicalPage.getPageGrid();
    final int rowCount = pageGrid.getRowCount();
    final int colCount = pageGrid.getColumnCount();

    final PageFlowSelector selector = getFlowSelector();
    if (selector != null)
    {
      if (selector.isLogicalPageAccepted(logicalPageKey))
      {
        processLogicalPage(logicalPageKey, logicalPage);
      }

      for (int row = 0; row < rowCount; row++)
      {
        for (int col = 0; col < colCount; col++)
        {
          final PhysicalPageKey pageKey = logicalPageKey.getPage(col, row);
          if (selector.isPhysicalPageAccepted(pageKey))
          {
            processPhysicalPage(pageGrid, logicalPage, row, col, pageKey);
          }
        }
      }
    }
  }

  protected abstract PageFlowSelector getFlowSelector();


  protected abstract void processPhysicalPage(final PageGrid pageGrid,
                                              final LogicalPageBox logicalPage,
                                              final int row,
                                              final int col,
                                              final PhysicalPageKey pageKey);

  protected abstract void processLogicalPage(final LogicalPageKey key,
                                             final LogicalPageBox logicalPage);

}
