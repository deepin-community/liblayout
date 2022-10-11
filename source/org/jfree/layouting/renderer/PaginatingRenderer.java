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
 * $Id: PaginatingRenderer.java 6489 2008-11-28 14:53:40Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.layouting.renderer;

import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.State;
import org.jfree.layouting.StateException;
import org.jfree.layouting.StatefullComponent;
import org.jfree.layouting.input.style.PseudoPage;
import org.jfree.layouting.layouter.context.LayoutStyle;
import org.jfree.layouting.layouter.context.PageContext;
import org.jfree.layouting.normalizer.content.NormalizationException;
import org.jfree.layouting.output.OutputProcessor;
import org.jfree.layouting.renderer.model.page.LogicalPageBox;
import org.jfree.layouting.renderer.process.CleanPaginatedBoxesStep;
import org.jfree.layouting.renderer.process.ComputeBreakabilityStep;
import org.jfree.layouting.renderer.process.ComputeICMMetricsStep;
import org.jfree.layouting.renderer.process.ComputeMarginsStep;
import org.jfree.layouting.renderer.process.ComputeStaticPropertiesStep;
import org.jfree.layouting.renderer.process.ComputeTableICMMetricsStep;
import org.jfree.layouting.renderer.process.FillPhysicalPagesStep;
import org.jfree.layouting.renderer.process.InfiniteMajorAxisLayoutStep;
import org.jfree.layouting.renderer.process.InfiniteMinorAxisLayoutStep;
import org.jfree.layouting.renderer.process.PaginationStep;
import org.jfree.layouting.renderer.process.ParagraphLineBreakStep;
import org.jfree.layouting.renderer.process.TableRowHeightStep;
import org.jfree.layouting.renderer.process.TableValidationStep;
import org.jfree.layouting.renderer.process.UpdateTokensStep;
import org.jfree.layouting.renderer.process.ValidateModelStep;


/**
 * A renderer which builds a streaming page model.
 *
 * @author Thomas Morgner
 */
public class PaginatingRenderer extends AbstractRenderer
{
  protected static class DefaultFlowRendererState extends RendererState
  {
    private ValidateModelStep validateModelStep;
    private TableValidationStep tableValidationStep;
    private ComputeStaticPropertiesStep staticPropertiesStep;
    private ComputeMarginsStep marginsStep;
    private ComputeICMMetricsStep icmMetricsStep;
    private ComputeTableICMMetricsStep tableICMMetricsStep;
    private ParagraphLineBreakStep paragraphLinebreakStep;
    private InfiniteMinorAxisLayoutStep minorAxisLayoutStep;
    private InfiniteMajorAxisLayoutStep majorAxisLayoutStep;
    private TableRowHeightStep tableRowHeightStep;
    private ComputeBreakabilityStep breakabilityStep;
    private PaginationStep paginationStep;
    private FillPhysicalPagesStep fillPhysicalPagesStep;
    private CleanPaginatedBoxesStep cleanPaginatedBoxesStep;
    private UpdateTokensStep updateTokensStep;

    protected DefaultFlowRendererState(final PaginatingRenderer renderer)
        throws StateException
    {
      super(renderer);
      this.validateModelStep = renderer.validateModelStep;
      this.tableValidationStep = renderer.tableValidationStep;
      this.staticPropertiesStep = renderer.staticPropertiesStep;

      this.marginsStep = renderer.marginsStep;
      this.icmMetricsStep = renderer.icmMetricsStep;
      this.tableICMMetricsStep = renderer.tableICMMetricsStep;
      this.paragraphLinebreakStep = renderer.paragraphLinebreakStep;
      this.minorAxisLayoutStep = renderer.minorAxisLayoutStep;
      this.majorAxisLayoutStep = renderer.majorAxisLayoutStep;
      this.tableRowHeightStep = renderer.tableRowHeightStep;
      this.breakabilityStep = renderer.breakabilityStep;
      this.paginationStep = renderer.paginationStep;
      this.fillPhysicalPagesStep = renderer.fillPhysicalPagesStep;
      this.cleanPaginatedBoxesStep = renderer.cleanPaginatedBoxesStep;
      this.updateTokensStep = renderer.updateTokensStep;
    }

    /**
     * Creates a restored instance of the saved component.
     * <p/>
     * By using this factory-like approach, we gain independence from having to
     * know the actual implementation. This makes things a lot easier.
     *
     * @param layoutProcess the layout process that controls it all
     * @return the saved state
     * @throws StateException
     */
    public StatefullComponent restore(final LayoutProcess layoutProcess)
        throws StateException
    {
      final PaginatingRenderer defaultRenderer = new PaginatingRenderer(layoutProcess, false);
      fill(defaultRenderer, layoutProcess);
      defaultRenderer.validateModelStep = this.validateModelStep;
      defaultRenderer.tableValidationStep = this.tableValidationStep;
      defaultRenderer.staticPropertiesStep = this.staticPropertiesStep;
      defaultRenderer.marginsStep = this.marginsStep;
      defaultRenderer.icmMetricsStep = this.icmMetricsStep;
      defaultRenderer.tableICMMetricsStep = this.tableICMMetricsStep;
      defaultRenderer.paragraphLinebreakStep = this.paragraphLinebreakStep;
      defaultRenderer.minorAxisLayoutStep = this.minorAxisLayoutStep;
      defaultRenderer.majorAxisLayoutStep = this.majorAxisLayoutStep;
      defaultRenderer.tableRowHeightStep = this.tableRowHeightStep;
      defaultRenderer.paginationStep = this.paginationStep;
      defaultRenderer.fillPhysicalPagesStep = this.fillPhysicalPagesStep;
      defaultRenderer.cleanPaginatedBoxesStep = this.cleanPaginatedBoxesStep;
      defaultRenderer.breakabilityStep = this.breakabilityStep;
      defaultRenderer.updateTokensStep = this.updateTokensStep;
      return defaultRenderer;
    }
  }


  private ValidateModelStep validateModelStep;
  private TableValidationStep tableValidationStep;
  private ComputeStaticPropertiesStep staticPropertiesStep;
  private ComputeMarginsStep marginsStep;
  private ComputeICMMetricsStep icmMetricsStep;
  private ComputeTableICMMetricsStep tableICMMetricsStep;
  private ParagraphLineBreakStep paragraphLinebreakStep;
  private InfiniteMinorAxisLayoutStep minorAxisLayoutStep;
  private InfiniteMajorAxisLayoutStep majorAxisLayoutStep;
  private TableRowHeightStep tableRowHeightStep;
  private ComputeBreakabilityStep breakabilityStep;
  private PaginationStep paginationStep;
  private FillPhysicalPagesStep fillPhysicalPagesStep;
  private CleanPaginatedBoxesStep cleanPaginatedBoxesStep;
  private UpdateTokensStep updateTokensStep;

  protected PaginatingRenderer(final LayoutProcess layoutProcess, final boolean init)
  {
    super(layoutProcess, init);
    if (init)
    {
      this.validateModelStep = new ValidateModelStep();
      this.staticPropertiesStep = new ComputeStaticPropertiesStep();
      this.tableValidationStep = new TableValidationStep();
      this.marginsStep = new ComputeMarginsStep();
      this.paragraphLinebreakStep = new ParagraphLineBreakStep();
      this.icmMetricsStep = new ComputeICMMetricsStep();
      this.tableICMMetricsStep = new ComputeTableICMMetricsStep();
      this.minorAxisLayoutStep = new InfiniteMinorAxisLayoutStep();
      this.majorAxisLayoutStep = new InfiniteMajorAxisLayoutStep();
      this.tableRowHeightStep = new TableRowHeightStep();
      this.breakabilityStep = new ComputeBreakabilityStep();
      this.paginationStep = new PaginationStep();
      this.fillPhysicalPagesStep = new FillPhysicalPagesStep();
      this.cleanPaginatedBoxesStep = new CleanPaginatedBoxesStep();
      this.updateTokensStep = new UpdateTokensStep();
    }
  }

  public PaginatingRenderer(final LayoutProcess layoutProcess)
  {
    this(layoutProcess, true);
  }

  protected void validateOutput() throws NormalizationException
  {
    final LogicalPageBox logicalPageBox = getLogicalPageBox();
    if (validateModelStep.isLayoutable(logicalPageBox) == false)
    {
//      final RenderNode nodeById = logicalPageBox.findNodeById(validateModelStep.getLayoutFailureNodeId());
//      Log.debug ("Validation impossible: Reason-id: " +
//          validateModelStep.getLayoutFailureResolution() +
//          " on node " +
//          nodeById);

      setLayoutFailureReason(validateModelStep.getLayoutFailureResolution(),
          validateModelStep.getLayoutFailureNodeId());
      return;
    }

    tableValidationStep.validate(logicalPageBox);
    updateTokensStep.compute
        (logicalPageBox, getLayoutProcess(), getPageContext());
    staticPropertiesStep.compute(logicalPageBox);
    marginsStep.compute(logicalPageBox);
    paragraphLinebreakStep.compute(logicalPageBox);
    icmMetricsStep.compute(logicalPageBox);

    boolean repeat = true;
    while (repeat)
    {
      tableICMMetricsStep.compute(logicalPageBox);

      minorAxisLayoutStep.compute(logicalPageBox);
      majorAxisLayoutStep.compute(logicalPageBox);
      tableRowHeightStep.compute(logicalPageBox);
      breakabilityStep.compute(logicalPageBox);

      if (paginationStep.performPagebreak(logicalPageBox) ||
          logicalPageBox.isOpen() == false)
      {
        // Note: This logic might fail if the last commit causes more than one pagebreak.
        // We may have to find a 'content-based' indicator when to shut-down the system.

        // A new page has been started. Recover the page-grid, then restart
        // everything from scratch. (We have to recompute, as the pages may
        // be different now, due to changed margins or page definitions)
        final OutputProcessor outputProcessor =
            getLayoutProcess().getOutputProcessor();
        final long nextOffset = paginationStep.getNextOffset();
        final long pageOffset = logicalPageBox.getPageOffset();
        final LogicalPageBox box = fillPhysicalPagesStep.compute
            (logicalPageBox, pageOffset, nextOffset);
        outputProcessor.processContent(box);

        // Now fire the pagebreak. This goes through all layers and informs all
        // components, that a pagebreak has been encountered and possibly a
        // new page has been set. It does not save the state or perform other
        // expensive operations. However, it updates the 'isPagebreakEncountered'
        // flag, which will be active until the input-feed received a new event.
        repeat = logicalPageBox.isOpen();
        if (repeat)
        {
          firePagebreak();

          logicalPageBox.setPageOffset(nextOffset);
          cleanPaginatedBoxesStep.compute(logicalPageBox);
        }
        else
        {
          outputProcessor.processingFinished();
        }
      }
      else
      {
        repeat = false;
      }
    }
  }

  protected void firePagebreak() throws NormalizationException
  {
    // todo: Compute the current page and the pseudo-pages for this one.
    final PageContext pageContext = getRenderPageContext().getPageContext();
    final LayoutStyle style = pageContext.getStyle();

    // todo: Update the pseudo-pages (left | right, first)
    getLayoutProcess().pageBreakEncountered(null, new PseudoPage[0]);
  }

  public State saveState() throws StateException
  {
    return new DefaultFlowRendererState(this);
  }

}
