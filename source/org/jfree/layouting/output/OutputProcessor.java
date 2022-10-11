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
 * $Id: OutputProcessor.java 6489 2008-11-28 14:53:40Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.output;

import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.layouter.feed.InputFeed;
import org.jfree.layouting.layouter.context.DocumentContext;
import org.jfree.layouting.normalizer.content.Normalizer;
import org.jfree.layouting.normalizer.displaymodel.ModelBuilder;
import org.jfree.layouting.output.pageable.LogicalPageKey;
import org.jfree.layouting.renderer.Renderer;
import org.jfree.layouting.renderer.model.page.LogicalPageBox;
import org.pentaho.reporting.libraries.base.config.Configuration;

/**
 * The output processor defines, which processing-step implementation will
 * be used during the layout process.
 *
 * Obviously, what computations are required depends heavily on the desired
 * result.
 *
 * @author Thomas Morgner
 */
public interface OutputProcessor
{
  public OutputProcessorMetaData getMetaData();

  public InputFeed createInputFeed (LayoutProcess layoutProcess);

  /**
   * Returns the content normalizer implementation for this OP. The content
   * normalizer is responsible for resolving the styles and for initiating the
   * display model building.
   *
   * @param layoutProcess the layout process that governs all.
   * @return the created content normalizer.
   */
  public Normalizer createNormalizer(LayoutProcess layoutProcess);

  /**
   * The model builder normalizes the input and builds the Display-Model. The
   * DisplayModel enriches and normalizes the logical document model so that
   * it is better suited for rendering.
   *
   * @param layoutProcess the layout process that governs all.
   * @return the created model builder.
   */
  public ModelBuilder createModelBuilder(LayoutProcess layoutProcess);

  public Renderer createRenderer (LayoutProcess layoutProcess);

  public void processContent (LogicalPageBox logicalPage);

  /**
   * Notifies the output processor, that the processing has been finished and
   * that the input-feed received the last event.
   */
  public void processingFinished();

  /**
   * This flag indicates, whether the global content has been computed. Global
   * content consists of global counters (except the pages counter) and
   * derived information like table of contents, the global directory of images
   * or tables etc.
   *
   * The global state must be computed before paginating can be attempted (if
   * the output target is paginating at all).
   *
   * @return true, if the global state has been computed, false otherwise.
   */
  public boolean isGlobalStateComputed();

  /**
   * This flag indicates, whether the output processor has collected enough
   * information to start the content generation.
   *
   * @return
   */
  public boolean isContentGeneratable();

  public Configuration getConfiguration();

  public int getLogicalPageCount();
  public LogicalPageKey getLogicalPage (int page);
  public void setPageCursor (int cursor);
  public int getPageCursor ();

  /**
   * Checks, whether the 'processingFinished' event had been received at least
   * once.
   *
   * @return
   */
  public boolean isPaginationFinished();

  public void processDocumentMetaData(DocumentContext documentContext);
}
