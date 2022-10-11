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
 * $Id: AbstractOutputProcessor.java 6489 2008-11-28 14:53:40Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.layouting.output;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.LibLayoutBoot;
import org.jfree.layouting.layouter.context.DocumentContext;
import org.jfree.layouting.layouter.feed.DefaultInputFeed;
import org.jfree.layouting.layouter.feed.InputFeed;
import org.jfree.layouting.normalizer.content.ContentNormalizer;
import org.jfree.layouting.normalizer.content.Normalizer;
import org.jfree.layouting.normalizer.displaymodel.FastDisplayModelBuilder;
import org.jfree.layouting.normalizer.displaymodel.ModelBuilder;
import org.jfree.layouting.normalizer.generator.DefaultContentGenerator;
import org.jfree.layouting.output.pageable.LogicalPageKey;
import org.jfree.layouting.renderer.model.page.LogicalPageBox;
import org.jfree.layouting.renderer.model.page.PageGrid;
import org.pentaho.reporting.libraries.base.config.Configuration;

/**
 * This base class configures the layouter for the normal DOM processing. The
 * display model assumes, that all elements can be nested freely, without
 * imposing any limitations at all.
 * <p/>
 * Using this as base process for text-processing document output (like RTF or
 * OpenOffice-Writer) is a sure way to the hell of funny behaviour.
 *
 * @author Thomas Morgner
 */
public abstract class AbstractOutputProcessor implements OutputProcessor
{
  protected static final int PROCESSING_GLOBAL_CONTENT = 0;
  protected static final int PROCESSING_PAGES = 1;
  protected static final int PROCESSING_CONTENT = 2;
  private int processingState;

  private Configuration configuration;
  private List logicalPages;
  private int pageCursor;
  private DocumentContext documentContext;

  public AbstractOutputProcessor(final Configuration configuration)
  {
    if (configuration == null)
    {
      this.configuration = LibLayoutBoot.getInstance().getGlobalConfig();
    }
    else
    {
      this.configuration = configuration;
    }
    this.logicalPages = new ArrayList();
  }

  /**
   * Checks, whether the 'processingFinished' event had been received at least
   * once.
   *
   * @return
   */
  public boolean isPaginationFinished()
  {
    return processingState == PROCESSING_CONTENT;
  }

  /**
   * Notifies the output processor, that the processing has been finished and
   * that the input-feed received the last event.
   */
  public void processingFinished()
  {
    pageCursor = 0;
    if (processingState == PROCESSING_GLOBAL_CONTENT)
    {
      // the global content is complete. fine, lets repaginate ...
      processingGlobalContentFinished();
      processingState = PROCESSING_PAGES;
    }
    else if (processingState == PROCESSING_PAGES)
    {
      // the pagination is complete. So, now we can produce real content.
      processingPagesFinished();
      processingState = PROCESSING_CONTENT;
    }
    else
    {
      processingContentFinished();
    }
    documentContext = null;
  }

  protected void processingContentFinished()
  {

  }

  public DocumentContext getDocumentContext()
  {
    return documentContext;
  }

  protected void processingPagesFinished()
  {
    logicalPages = Collections.unmodifiableList(logicalPages);
  }

  protected void processingGlobalContentFinished()
  {

  }


  public Configuration getConfiguration()
  {
    return configuration;
  }


  public InputFeed createInputFeed(final LayoutProcess layoutProcess)
  {
    return new DefaultInputFeed(layoutProcess);
  }

  /**
   * Returns the content normalizer implementation for this OP. The content
   * normalizer is responsible for resolving the styles and for initiating the
   * DOM building.
   *
   * @return
   */
  public Normalizer createNormalizer(final LayoutProcess layoutProcess)
  {
    return new ContentNormalizer(layoutProcess);
  }

  /**
   * The model builder normalizes the input and builds the Display-Model. The
   * DisplayModel enriches and normalizes the logical document model so that it
   * is better suited for rendering.
   *
   * @return
   */
  public ModelBuilder createModelBuilder(final LayoutProcess layoutProcess)
  {
    //return new DisplayModelBuilder(new PrintContentGenerator(layoutProcess), layoutProcess);
    return new FastDisplayModelBuilder(new DefaultContentGenerator(layoutProcess), layoutProcess);
  }

  public int getLogicalPageCount()
  {
    return logicalPages.size();
  }

  public LogicalPageKey getLogicalPage(final int page)
  {
    if (isPaginationFinished() == false)
    {
      throw new IllegalStateException();
    }

    return (LogicalPageKey) logicalPages.get(page);
  }

  protected LogicalPageKey createLogicalPage(final int width,
                                             final int height)
  {
    final LogicalPageKey key =
        new LogicalPageKey(logicalPages.size(), width, height);
    logicalPages.add(key);
    return key;
  }

  public int getPageCursor()
  {
    return pageCursor;
  }

  public void setPageCursor(final int pageCursor)
  {
    this.pageCursor = pageCursor;
  }

  /**
   * This flag indicates, whether the global content has been computed. Global
   * content consists of global counters (except the pages counter) and derived
   * information like table of contents, the global directory of images or
   * tables etc.
   * <p/>
   * The global state must be computed before paginating can be attempted (if
   * the output target is paginating at all).
   *
   * @return true, if the global state has been computed, false otherwise.
   */
  public boolean isGlobalStateComputed()
  {
    return processingState > PROCESSING_GLOBAL_CONTENT;
  }

  protected int getProcessingState()
  {
    return processingState;
  }

  /**
   * This flag indicates, whether the output processor has collected enough
   * information to start the content generation.
   *
   * @return
   */
  public boolean isContentGeneratable()
  {
    return processingState == PROCESSING_CONTENT;
  }


  public final void processContent(final LogicalPageBox logicalPage)
  {
    if (isGlobalStateComputed() == false)
    {
      return;
    }

    if (isContentGeneratable() == false)
    {
      // This is just an assertation ...
      // Only if pagination is active ..
      final PageGrid pageGrid = logicalPage.getPageGrid();
      final int rowCount = pageGrid.getRowCount();
      final int colCount = pageGrid.getColumnCount();

      final LogicalPageKey key = createLogicalPage(colCount, rowCount);
      final int pageCursor = getPageCursor();
      if (key.getPosition() != pageCursor)
      {
        throw new IllegalStateException("Expected position " + pageCursor + " is not the key's position " + key.getPosition());
      }
      setPageCursor(pageCursor + 1);
      return;
    }

    // thats the real stuff ..
    if (isContentGeneratable())
    {
      final int pageCursor = getPageCursor();
      final LogicalPageKey logicalPageKey = getLogicalPage(pageCursor);
      processPageContent(logicalPageKey, logicalPage);
      setPageCursor(pageCursor + 1);
    }
  }

  protected abstract void processPageContent(final LogicalPageKey logicalPageKey,
                                             final LogicalPageBox logicalPage);

  public void processDocumentMetaData(final DocumentContext documentContext)
  {
    this.documentContext = documentContext;
  }
}
