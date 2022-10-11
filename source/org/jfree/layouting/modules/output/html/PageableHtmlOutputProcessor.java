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
 * $Id: PageableHtmlOutputProcessor.java 6489 2008-11-28 14:53:40Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.layouting.modules.output.html;

import org.jfree.layouting.output.OutputProcessorMetaData;
import org.jfree.layouting.output.pageable.AbstractPageableProcessor;
import org.jfree.layouting.output.pageable.AllPageFlowSelector;
import org.jfree.layouting.output.pageable.LogicalPageKey;
import org.jfree.layouting.output.pageable.PageFlowSelector;
import org.jfree.layouting.output.pageable.PhysicalPageKey;
import org.jfree.layouting.renderer.model.page.LogicalPageBox;
import org.jfree.layouting.renderer.model.page.PageGrid;
import org.pentaho.reporting.libraries.repository.DefaultNameGenerator;
import org.pentaho.reporting.libraries.repository.NameGenerator;
import org.pentaho.reporting.libraries.repository.ContentLocation;
import org.pentaho.reporting.libraries.repository.dummy.DummyRepository;
import org.pentaho.reporting.libraries.fonts.registry.DefaultFontStorage;
import org.pentaho.reporting.libraries.fonts.registry.FontStorage;
import org.pentaho.reporting.libraries.fonts.registry.FontRegistry;
import org.pentaho.reporting.libraries.fonts.awt.AWTFontRegistry;
import org.pentaho.reporting.libraries.base.config.Configuration;

/**
 * Creation-Date: 12.11.2006, 14:11:28
 *
 * @author Thomas Morgner
 */
public class PageableHtmlOutputProcessor extends AbstractPageableProcessor
  implements HtmlOutputProcessor
{
  private HtmlOutputProcessorMetaData metaData;
  private PageFlowSelector flowSelector;
  private HtmlPrinter printer;

  public PageableHtmlOutputProcessor(final Configuration configuration)
  {
    super(configuration);
    this.flowSelector = new AllPageFlowSelector(true);

    final FontRegistry fontRegistry = new AWTFontRegistry();
    final FontStorage fontStorage = new DefaultFontStorage(fontRegistry);
    this.metaData = new HtmlOutputProcessorMetaData
        (fontStorage, HtmlOutputProcessorMetaData.PAGINATION_FULL);


    final ContentLocation contentLocation = new DummyRepository().getRoot();
    final NameGenerator contentNameGenerator = new DefaultNameGenerator(contentLocation);
    final ContentLocation dataLocation = new DummyRepository().getRoot();
    final NameGenerator dataNameGenerator = new DefaultNameGenerator(dataLocation);

    this.printer = new HtmlPrinter();
    this.printer.setContentWriter(contentLocation, contentNameGenerator);
    this.printer.setDataWriter(dataLocation, dataNameGenerator);
  }

  public HtmlPrinter getPrinter()
  {
    return printer;
  }

  public void setPrinter(final HtmlPrinter printer)
  {
    this.printer = printer;
  }

  protected void processPhysicalPage(final PageGrid pageGrid,
                                     final LogicalPageBox logicalPage,
                                     final int row,
                                     final int col,
                                     final PhysicalPageKey pageKey)
  {

  }

  protected void processLogicalPage(final LogicalPageKey key,
                                    final LogicalPageBox logicalPage)
  {
    try
    {
      printer.generate(logicalPage, getDocumentContext());
    }
    catch (Exception e)
    {
      throw new RuntimeException("Failed to generate content.", e);
    }
  }

  public PageFlowSelector getFlowSelector()
  {
    return flowSelector;
  }

  public void setFlowSelector(final PageFlowSelector flowSelector)
  {
    this.flowSelector = flowSelector;
  }

  public OutputProcessorMetaData getMetaData()
  {
    return metaData;
  }

}
