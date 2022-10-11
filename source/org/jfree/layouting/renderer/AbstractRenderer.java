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
 * $Id: AbstractRenderer.java 6489 2008-11-28 14:53:40Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.renderer;

import java.awt.Dimension;
import java.awt.Image;

import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.State;
import org.jfree.layouting.StateException;
import org.jfree.layouting.LibLayoutBoot;
import org.jfree.layouting.input.style.keys.box.BoxStyleKeys;
import org.jfree.layouting.input.style.keys.line.LineStyleKeys;
import org.jfree.layouting.input.style.keys.positioning.PositioningStyleKeys;
import org.jfree.layouting.input.style.values.CSSFunctionValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.layouter.content.ContentToken;
import org.jfree.layouting.layouter.content.computed.ComputedToken;
import org.jfree.layouting.layouter.content.computed.VariableToken;
import org.jfree.layouting.layouter.content.resolved.ResolvedCounterToken;
import org.jfree.layouting.layouter.content.resolved.ResolvedStringToken;
import org.jfree.layouting.layouter.content.type.GenericType;
import org.jfree.layouting.layouter.content.type.ResourceType;
import org.jfree.layouting.layouter.content.type.TextType;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.layouter.context.PageContext;
import org.jfree.layouting.normalizer.content.NormalizationException;
import org.jfree.layouting.output.OutputProcessor;
import org.jfree.layouting.renderer.border.BorderFactory;
import org.jfree.layouting.renderer.border.RenderLength;
import org.jfree.layouting.renderer.model.BlockRenderBox;
import org.jfree.layouting.renderer.model.BoxDefinition;
import org.jfree.layouting.renderer.model.BoxDefinitionFactory;
import org.jfree.layouting.renderer.model.DefaultBoxDefinitionFactory;
import org.jfree.layouting.renderer.model.InlineRenderBox;
import org.jfree.layouting.renderer.model.MarkerRenderBox;
import org.jfree.layouting.renderer.model.NormalFlowRenderBox;
import org.jfree.layouting.renderer.model.PageAreaRenderBox;
import org.jfree.layouting.renderer.model.ParagraphRenderBox;
import org.jfree.layouting.renderer.model.RenderBox;
import org.jfree.layouting.renderer.model.RenderNode;
import org.jfree.layouting.renderer.model.RenderableReplacedContent;
import org.jfree.layouting.renderer.model.RenderableTextBox;
import org.jfree.layouting.renderer.model.ParagraphPoolBox;
import org.jfree.layouting.renderer.model.page.LogicalPageBox;
import org.jfree.layouting.renderer.model.page.PageGrid;
import org.jfree.layouting.renderer.model.table.TableCellRenderBox;
import org.jfree.layouting.renderer.model.table.TableColumnGroupNode;
import org.jfree.layouting.renderer.model.table.TableColumnNode;
import org.jfree.layouting.renderer.model.table.TableRenderBox;
import org.jfree.layouting.renderer.model.table.TableRowRenderBox;
import org.jfree.layouting.renderer.model.table.TableSectionRenderBox;
import org.jfree.layouting.renderer.page.RenderPageContext;
import org.jfree.layouting.renderer.process.ValidateModelStep;
import org.jfree.layouting.renderer.process.CheckHibernationLayoutStep;
import org.jfree.layouting.renderer.text.DefaultRenderableTextFactory;
import org.jfree.layouting.renderer.text.RenderableTextFactory;
import org.jfree.layouting.util.geom.StrictDimension;
import org.jfree.layouting.util.geom.StrictGeomUtility;
import org.pentaho.reporting.libraries.fonts.encoding.CodePointBuffer;
import org.pentaho.reporting.libraries.fonts.encoding.manual.Utf16LE;
import org.pentaho.reporting.libraries.base.util.FastStack;
import org.pentaho.reporting.libraries.base.util.WaitingImageObserver;
import org.pentaho.reporting.libraries.resourceloader.ResourceKey;
import org.pentaho.reporting.libraries.resourceloader.factory.drawable.DrawableWrapper;

/**
 * Creation-Date: 16.06.2006, 14:10:40
 *
 * @author Thomas Morgner
 */
public abstract class AbstractRenderer implements Renderer
{
  protected abstract static class RendererState implements State
  {
    private BoxDefinitionFactory boxDefinitionFactory;
    private State pageContext;
    private int bufferLength;
    private LogicalPageBox logicalPageBox;

    private ContentStore elementsStore;
    private ContentStore pendingStore;

    // todo:!
    private FlowContext.FlowContextState[] flowContexts;

    protected RendererState(final AbstractRenderer renderer) throws StateException
    {
      this.boxDefinitionFactory = renderer.boxDefinitionFactory;
      if (renderer.pageContext != null)
      {
        this.pageContext = renderer.pageContext.saveState();
      }

      if (renderer.buffer != null)
      {
        this.bufferLength = renderer.buffer.getData().length;
      }

      if (renderer.logicalPageBox != null)
      {
        this.logicalPageBox = (LogicalPageBox)
            renderer.logicalPageBox.hibernate();
        if (LibLayoutBoot.isAsserationEnabled())
        {
          final CheckHibernationLayoutStep step = new CheckHibernationLayoutStep();
          step.startProcessing(this.logicalPageBox);
        }
      }

      try
      {
        this.elementsStore = (ContentStore) renderer.elementsStore.clone();
        this.pendingStore = (ContentStore) renderer.pendingStore.clone();
      }
      catch (CloneNotSupportedException e)
      {
        throw new StateException();
      }

      final FastStack renderFlowContexts = renderer.flowContexts;
      this.flowContexts = new FlowContext.FlowContextState
          [renderFlowContexts.size()];
      for (int i = 0; i < renderFlowContexts.size(); i++)
      {
        final FlowContext context = (FlowContext) renderFlowContexts.get(i);
        flowContexts[i] = context.saveState();
      }
    }


    protected void fill(final AbstractRenderer renderer,
                        final LayoutProcess layoutProcess) throws StateException
    {
      if (bufferLength > 0)
      {
        renderer.buffer = new CodePointBuffer(bufferLength);
      }

      try
      {
        renderer.elementsStore = (ContentStore) this.elementsStore.clone();
        renderer.pendingStore = (ContentStore) this.pendingStore.clone();
      }
      catch (CloneNotSupportedException e)
      {
        throw new StateException();
      }
      renderer.boxDefinitionFactory = this.boxDefinitionFactory;
      if (pageContext != null)
      {
        renderer.pageContext = (RenderPageContext)
            this.pageContext.restore(layoutProcess);
      }
      if (logicalPageBox != null)
      {
        renderer.logicalPageBox = (LogicalPageBox)
            this.logicalPageBox.derive(true);
      }

      renderer.flowContexts = new FastStack();
      for (int i = 0; i < flowContexts.length; i++)
      {
        final FlowContext.FlowContextState state = flowContexts[i];
        final Object currentFlowId = state.getCurrentFlowId();
        final State textFactoryState = state.getTextFactoryState();
        final RenderableTextFactory textFactory =
            (RenderableTextFactory) textFactoryState.restore(layoutProcess);
        final NormalFlowRenderBox box = (NormalFlowRenderBox)
            renderer.logicalPageBox.findNodeById(currentFlowId);
        if (box == null)
        {
          throw new StateException("No Such normal flow.");
        }
        renderer.flowContexts.push(new FlowContext(textFactory, box));
      }
    }
  }

  // from restore
  private LayoutProcess layoutProcess;
  // statefull ..
  private LogicalPageBox logicalPageBox;
  private ContentStore elementsStore;
  private ContentStore pendingStore;
  private FastStack flowContexts;

  // to be recreated
  private CodePointBuffer buffer;
  // Stateless components ..
  private RenderPageContext pageContext;
  private BoxDefinitionFactory boxDefinitionFactory;
  private Object layoutFailureNodeId;
  private int layoutFailureReason;

  protected AbstractRenderer(final LayoutProcess layoutProcess,
                             final boolean init)
  {
    if (layoutProcess == null)
    {
      throw new NullPointerException();
    }

    this.layoutProcess = layoutProcess;
    this.flowContexts = new FastStack();

    if (init)
    {
      this.elementsStore = new ContentStore();
      this.pendingStore = new ContentStore();
      this.boxDefinitionFactory =
          new DefaultBoxDefinitionFactory(new BorderFactory());
    }
  }

  public LogicalPageBox getLogicalPageBox()
  {
    return logicalPageBox;
  }

  public LayoutProcess getLayoutProcess()
  {
    return layoutProcess;
  }

  public RenderPageContext getRenderPageContext()
  {
    return pageContext;
  }

  public void setLayoutFailureReason
      (final int layoutFailureReason, final Object layoutFailureNodeId)
  {
    this.layoutFailureNodeId = layoutFailureNodeId;
    this.layoutFailureReason = layoutFailureReason;
  }

  public void startedDocument(final PageContext pageContext)
  {
    if (pageContext == null)
    {
      throw new NullPointerException();
    }

    final LayoutProcess layoutProcess = getLayoutProcess();
    final OutputProcessor outputProcessor = layoutProcess.getOutputProcessor();
    outputProcessor.processDocumentMetaData(layoutProcess.getDocumentContext());

    this.pageContext = new RenderPageContext(layoutProcess, pageContext);
    // create the initial pagegrid.
    final PageGrid pageGrid =
        this.pageContext.createPageGrid(this.layoutProcess.getOutputMetaData());

    // initialize the logical page. The logical page needs the page grid,
    // as this contains the hints for the physical page sizes.
    logicalPageBox = new LogicalPageBox(pageGrid);
    logicalPageBox.setPageContext(this.pageContext.getPageContext());


  }

  /**
   * @param instanceId can be null if there is no meaningful instanceid.
   * @throws NormalizationException
   */
  protected final void tryValidateOutput(final Object instanceId)
      throws NormalizationException
  {
    if (isValidatable(instanceId))
    {
      try
      {
        validateOutput();
      }
      catch(IllegalStateException e)
      {
        e.printStackTrace();
        //validateOutput();
        ModelPrinter.print(logicalPageBox);
      }
    }
  }

  private boolean isValidatable(final Object instanceId)
  {
    if (layoutFailureReason == ValidateModelStep.BOX_MUST_BE_CLOSED)
    {
      if (instanceId != layoutFailureNodeId)
      {
//        Log.debug("Validation impossible: waiting for close event" +
//            " on node " +
//            logicalPageBox.findNodeById(layoutFailureNodeId));
        return false;
      }

      layoutFailureReason = ValidateModelStep.LAYOUT_OK;
      layoutFailureNodeId = null;
    }
    return true;
  }

  protected abstract void validateOutput() throws NormalizationException;

  protected RenderBox getInsertationPoint()
  {
    final FlowContext context = (FlowContext) flowContexts.peek();
    final NormalFlowRenderBox currentFlow = context.getCurrentFlow();
    if (currentFlow == null)
    {
      throw new IllegalStateException("There is no flow active at the moment.");
    }
    return currentFlow.getInsertationPoint();
  }

  protected boolean isProcessingNormalFlow()
  {
    return this.flowContexts.size() <= 1;
  }

  public void startedFlow(final LayoutContext context)
      throws NormalizationException
  {
    this.pageContext = pageContext.update(context);

    if (logicalPageBox.isNormalFlowActive() == false)
    {
      // this is the first (the normal) flow. A document always starts
      // with a start-document and then a start-flow event.
      logicalPageBox.setNormalFlowActive(true);
      final DefaultRenderableTextFactory textFactory = new DefaultRenderableTextFactory(layoutProcess);
      textFactory.startText();

      final FlowContext flowContext = new FlowContext
          (textFactory, logicalPageBox.getNormalFlow());

      flowContexts.push(flowContext);
    }
    else
    {
      // now check, what flow you are. A flow-top?
      // position: running(header); New headers replace old ones.
      // how to differentiate that (so that style-definitions are not that
      // complicated.

      // For now, we keep it simple. running(header) means go to header
      final CSSValue value = context.getValue(PositioningStyleKeys.POSITION);
      if (value instanceof CSSFunctionValue)
      {
        final CSSFunctionValue fnvalue = (CSSFunctionValue) value;
        final CSSValue[] parameters = fnvalue.getParameters();
        if (parameters.length > 0)
        {
          // Todo: Oh, thats so primitive ...
          final CSSValue targetValue = parameters[0];
          startHeaderFlow(targetValue.getCSSText(), context);
          return;
        }
      }

      // The receiving element would define the content property as
      // 'content: elements(header)'

      // an ordinary flow?
      final BoxDefinition contentRoot =
          boxDefinitionFactory.createBlockBoxDefinition
              (context, layoutProcess.getOutputMetaData());

      final NormalFlowRenderBox newFlow = new NormalFlowRenderBox(contentRoot);
      newFlow.appyStyle(context, layoutProcess.getOutputMetaData());
      newFlow.setPageContext(pageContext.getPageContext());

      final RenderBox currentBox = getInsertationPoint();
      currentBox.addChild(newFlow.getPlaceHolder());
      currentBox.getNormalFlow().addFlow(newFlow);

      final DefaultRenderableTextFactory textFactory =
          new DefaultRenderableTextFactory(layoutProcess);
      textFactory.startText();

      final FlowContext flowContext = new FlowContext
          (textFactory, newFlow);

      flowContexts.push(flowContext);
    }
  }

  private void startHeaderFlow(final String target, final LayoutContext context)
  {
    final BoxDefinition contentRoot =
        boxDefinitionFactory.createBlockBoxDefinition
            (context, layoutProcess.getOutputMetaData());

    final NormalFlowRenderBox newFlow = new NormalFlowRenderBox(contentRoot);
    newFlow.appyStyle(context, layoutProcess.getOutputMetaData());
    newFlow.setPageContext(pageContext.getPageContext());

    if ("header".equals(target))
    {
      final PageAreaRenderBox headerArea = logicalPageBox.getHeaderArea();
      headerArea.clear();
      headerArea.addChild(newFlow);
    }
    else if ("footer".equals(target))
    {
      final PageAreaRenderBox footerArea = logicalPageBox.getFooterArea();
      footerArea.clear();
      footerArea.addChild(newFlow);
    }

    final DefaultRenderableTextFactory textFactory =
        new DefaultRenderableTextFactory(layoutProcess);
    textFactory.startText();

    final FlowContext flowContext = new FlowContext
        (textFactory, newFlow);

    flowContexts.push(flowContext);
  }

  public void startedTable(final LayoutContext context)
      throws NormalizationException
  {
    final RenderableTextFactory textFactory = getCurrentTextFactory();
    getInsertationPoint().addChilds(textFactory.finishText());

    textFactory.startText();

    final BoxDefinition definition =
        boxDefinitionFactory.createBlockBoxDefinition
            (context, layoutProcess.getOutputMetaData());
    this.pageContext = pageContext.update(context);

    final TableRenderBox tableRenderBox =
        new TableRenderBox(definition);
    tableRenderBox.appyStyle(context, layoutProcess.getOutputMetaData());
    tableRenderBox.setPageContext(pageContext.getPageContext());

    getInsertationPoint().addChild(tableRenderBox);

    // tryValidateOutput();

  }

  private RenderableTextFactory getCurrentTextFactory()
  {
    final FlowContext context = (FlowContext) flowContexts.peek();
    return context.getTextFactory();
  }

  public void startedTableColumnGroup(final LayoutContext context)
      throws NormalizationException
  {
    final RenderableTextFactory textFactory = getCurrentTextFactory();
    getInsertationPoint().addChilds(textFactory.finishText());

    textFactory.startText();

    this.pageContext = pageContext.update(context);

    final BoxDefinition definition =
        boxDefinitionFactory.createBlockBoxDefinition
            (context, layoutProcess.getOutputMetaData());
    final TableColumnGroupNode columnGroupNode = new TableColumnGroupNode(definition);
    columnGroupNode.appyStyle(context, layoutProcess.getOutputMetaData());
    getInsertationPoint().addChild(columnGroupNode);

    // tryValidateOutput();

  }

  public void startedTableColumn(final LayoutContext context)
      throws NormalizationException
  {
    final RenderableTextFactory textFactory = getCurrentTextFactory();
    getInsertationPoint().addChilds(textFactory.finishText());

    textFactory.startText();

    this.pageContext = pageContext.update(context);

    final BoxDefinition definition =
        boxDefinitionFactory.createBlockBoxDefinition
            (context, layoutProcess.getOutputMetaData());
    final TableColumnNode columnGroupNode = new TableColumnNode(definition, context);
    getInsertationPoint().addChild(columnGroupNode);

    // tryValidateOutput();

  }

  public void startedTableSection(final LayoutContext context)
      throws NormalizationException
  {
    final RenderableTextFactory textFactory = getCurrentTextFactory();
    getInsertationPoint().addChilds(textFactory.finishText());

    textFactory.startText();

    this.pageContext = pageContext.update(context);

    final BoxDefinition definition =
        boxDefinitionFactory.createBlockBoxDefinition
            (context, layoutProcess.getOutputMetaData());
    final TableSectionRenderBox tableRenderBox =
        new TableSectionRenderBox(definition);
    tableRenderBox.appyStyle(context, layoutProcess.getOutputMetaData());
    tableRenderBox.setPageContext(pageContext.getPageContext());
    getInsertationPoint().addChild(tableRenderBox);

    // tryValidateOutput();

  }

  public void startedTableRow(final LayoutContext context)
      throws NormalizationException
  {
    final RenderableTextFactory textFactory = getCurrentTextFactory();
    getInsertationPoint().addChilds(textFactory.finishText());

    textFactory.startText();

    this.pageContext = pageContext.update(context);

    final BoxDefinition definition =
        boxDefinitionFactory.createBlockBoxDefinition
            (context, layoutProcess.getOutputMetaData());
    final TableRowRenderBox tableRenderBox = new TableRowRenderBox(definition, false);
    tableRenderBox.appyStyle(context, layoutProcess.getOutputMetaData());
    tableRenderBox.setPageContext(pageContext.getPageContext());
    getInsertationPoint().addChild(tableRenderBox);

    // tryValidateOutput();

  }

  public void startedTableCell(final LayoutContext context)
      throws NormalizationException
  {
    final RenderableTextFactory textFactory = getCurrentTextFactory();
    getInsertationPoint().addChilds(textFactory.finishText());

    textFactory.startText();

    this.pageContext = pageContext.update(context);

    final BoxDefinition definition =
        boxDefinitionFactory.createBlockBoxDefinition
            (context, layoutProcess.getOutputMetaData());
    final TableCellRenderBox tableRenderBox =
        new TableCellRenderBox(definition);
    tableRenderBox.setPageContext(pageContext.getPageContext());
    tableRenderBox.appyStyle(context, layoutProcess.getOutputMetaData());

    getInsertationPoint().addChild(tableRenderBox);

    // tryValidateOutput();

  }

  public void startedBlock(final LayoutContext context)
      throws NormalizationException
  {
    final RenderableTextFactory textFactory = getCurrentTextFactory();
    getInsertationPoint().addChilds(textFactory.finishText());

    textFactory.startText();

    this.pageContext = pageContext.update(context);

    final BoxDefinition definition =
        boxDefinitionFactory.createBlockBoxDefinition
            (context, layoutProcess.getOutputMetaData());

    final BlockRenderBox blockBox = new BlockRenderBox(definition);
    blockBox.appyStyle(context, layoutProcess.getOutputMetaData());
    blockBox.setPageContext(pageContext.getPageContext());
    getInsertationPoint().addChild(blockBox);

    // tryValidateOutput(blockBox.getInstanceId());
  }

  public void startedMarker(final LayoutContext context)
      throws NormalizationException
  {
    final RenderableTextFactory textFactory = getCurrentTextFactory();
    getInsertationPoint().addChilds(textFactory.finishText());

    textFactory.startText();

    this.pageContext = pageContext.update(context);

    final BoxDefinition definition =
        boxDefinitionFactory.createInlineBoxDefinition
            (context, layoutProcess.getOutputMetaData());
    final MarkerRenderBox markerBox = new MarkerRenderBox(definition);
    markerBox.appyStyle(context, layoutProcess.getOutputMetaData());
    markerBox.setPageContext(pageContext.getPageContext());
    getInsertationPoint().addChild(markerBox);

    // tryValidateOutput();
  }

  public void startedRootInline(final LayoutContext context)
      throws NormalizationException
  {
    final RenderableTextFactory textFactory = getCurrentTextFactory();
    getInsertationPoint().addChilds(textFactory.finishText());
    textFactory.startText();

    this.pageContext = pageContext.update(context);

    final BoxDefinition definition =
        boxDefinitionFactory.createBlockBoxDefinition
            (context, layoutProcess.getOutputMetaData());
    final ParagraphRenderBox paragraphBox =
        new ParagraphRenderBox(definition);
    paragraphBox.appyStyle(context, layoutProcess.getOutputMetaData());
    paragraphBox.setPageContext(pageContext.getPageContext());

    final RenderBox insertationPoint = getInsertationPoint();
    insertationPoint.addChild(paragraphBox);

    // tryValidateOutput();
  }

  public void startedInline(final LayoutContext context)
      throws NormalizationException
  {
    final RenderableTextFactory textFactory = getCurrentTextFactory();
    getInsertationPoint().addChilds(textFactory.finishText());

    this.pageContext = pageContext.update(context);

    final BoxDefinition definition =
        boxDefinitionFactory.createInlineBoxDefinition
            (context, layoutProcess.getOutputMetaData());
    final InlineRenderBox inlineBox = new InlineRenderBox(definition);
    inlineBox.appyStyle(context, layoutProcess.getOutputMetaData());
    inlineBox.setPageContext(pageContext.getPageContext());

    final RenderBox insertationPoint = getInsertationPoint();
    insertationPoint.addChild(inlineBox);

    // tryValidateOutput();
  }

  public void addContent(final LayoutContext context,
                         final ContentToken content)
      throws NormalizationException
  {
    final RenderableTextFactory textFactory = getCurrentTextFactory();
    if (content instanceof GenericType)
    {
      final GenericType generic = (GenericType) content;
      ResourceKey source = null;
      if (content instanceof ResourceType)
      {
        final ResourceType resourceType = (ResourceType) content;
        source = resourceType.getContent().getSource();
      }

      final Object raw = generic.getRaw();
      if (raw instanceof Image)
      {
        final RenderableReplacedContent replacedContent =
            createImage((Image) raw, source, context);
        if (replacedContent != null)
        {
          replacedContent.appyStyle(context, layoutProcess.getOutputMetaData());

          getInsertationPoint().addChilds(textFactory.finishText());
          getInsertationPoint().addChild(replacedContent);
          tryValidateOutput(null);
          return;
        }
      }
      else if (raw instanceof DrawableWrapper)
      {
        final RenderableReplacedContent replacedContent =
            createDrawable((DrawableWrapper) raw, source, context);
        if (replacedContent != null)
        {
          replacedContent.appyStyle(context, layoutProcess.getOutputMetaData());

          getInsertationPoint().addChilds(textFactory.finishText());
          getInsertationPoint().addChild(replacedContent);
          tryValidateOutput(null);
          return;
        }
      }
    }


    if (content instanceof ResolvedCounterToken)
    {
      final ResolvedCounterToken resolvedToken = (ResolvedCounterToken) content;
      if (isProcessingNormalFlow() == false)
      {
        getInsertationPoint().addChilds(textFactory.finishText());
        try
        {
          final RenderableTextBox token = new RenderableTextBox
              (textFactory.saveState(), resolvedToken, context);
          token.appyStyle(context, getLayoutProcess().getOutputMetaData());
          getInsertationPoint().addChild(token);
          token.close();
          tryValidateOutput(null);
          return;
        }
        catch (StateException se)
        {
          // Should not happen ..
          throw new NormalizationException("State failed.", se);
        }
      }
    }

    if (context instanceof ResolvedStringToken)
    {
      final ResolvedStringToken resolvedToken = (ResolvedStringToken) context;
      final ComputedToken parent = resolvedToken.getParent();
      // todo: The test should be: isProcessingPageFlow()
      if (parent instanceof VariableToken && isProcessingNormalFlow() == false)
      {
        getInsertationPoint().addChilds(textFactory.finishText());
        try
        {
          final RenderableTextBox token = new RenderableTextBox
              (textFactory.saveState(), resolvedToken, context);
          token.appyStyle(context, getLayoutProcess().getOutputMetaData());
          getInsertationPoint().addChild(token);
          token.close();
          tryValidateOutput(null);
          return;
        }
        catch (StateException se)
        {
          // Should not happen ..
          throw new NormalizationException("State failed.", se);
        }
      }
    }

    if (content instanceof TextType)
    {
      final TextType textRaw = (TextType) content;
      final String textStr = textRaw.getText();

      final RenderNode[] text = createText(textStr, context);
      if (text.length == 0)
      {
        return;
      }

      final RenderBox insertationPoint = getInsertationPoint();
      insertationPoint.addChilds(text);
      tryValidateOutput(null);
    }
  }

  private RenderNode[] createText(final String str,
                                  final LayoutContext context)
  {
    if (buffer != null)
    {
      buffer.setCursor(0);
    }
    buffer = Utf16LE.getInstance().decodeString(str, buffer);
    final int[] data = buffer.getBuffer();

    final RenderableTextFactory textFactory = getCurrentTextFactory();
    return textFactory.createText(data, 0, buffer.getLength(), context);
  }

  private RenderableReplacedContent createImage(final Image image,
                                                final ResourceKey source,
                                                final LayoutContext context)
  {
    final WaitingImageObserver wobs = new WaitingImageObserver(image);
    wobs.waitImageLoaded();
    if (wobs.isError())
    {
      return null;
    }

    final CSSValue widthVal = context.getValue(BoxStyleKeys.WIDTH);
    final RenderLength width = DefaultBoxDefinitionFactory.computeWidth
        (widthVal, context, layoutProcess.getOutputMetaData(), true, false);

    final CSSValue heightVal = context.getValue(BoxStyleKeys.HEIGHT);
    final RenderLength height = DefaultBoxDefinitionFactory.computeWidth
        (heightVal, context, layoutProcess.getOutputMetaData(), true, false);
    final StrictDimension dims = StrictGeomUtility.createDimension
        (image.getWidth(null), image.getHeight(null));
    final CSSValue valign =
        context.getValue(LineStyleKeys.VERTICAL_ALIGN);
    return new RenderableReplacedContent(image, source, dims, width, height, valign);
  }

  private RenderableReplacedContent createDrawable(final DrawableWrapper image,
                                                   final ResourceKey source,
                                                   final LayoutContext context)
  {
    final StrictDimension dims = new StrictDimension();

    final Dimension preferredSize = image.getPreferredSize();
    if (preferredSize != null)
    {
      dims.setWidth(StrictGeomUtility.toInternalValue(preferredSize.getWidth()));
      dims.setHeight(StrictGeomUtility.toInternalValue(preferredSize.getHeight()));
    }

    final CSSValue widthVal = context.getValue(BoxStyleKeys.WIDTH);
    final RenderLength width = DefaultBoxDefinitionFactory.computeWidth
        (widthVal, context, layoutProcess.getOutputMetaData(), true, false);

    final CSSValue heightVal = context.getValue(BoxStyleKeys.HEIGHT);
    final RenderLength height = DefaultBoxDefinitionFactory.computeWidth
        (heightVal, context, layoutProcess.getOutputMetaData(), true, false);

    final CSSValue valign =
        context.getValue(LineStyleKeys.VERTICAL_ALIGN);
    return new RenderableReplacedContent(image, source, dims, width, height, valign);
  }

  public void finishedInline() throws NormalizationException
  {
    final RenderBox insertationPoint = getInsertationPoint();
    final RenderableTextFactory textFactory = getCurrentTextFactory();
    final RenderNode[] nodes = textFactory.finishText();
    insertationPoint.addChilds(nodes);
    insertationPoint.close();
    // currentBox = (RenderBox) currentBox.getParent();
    tryValidateOutput(insertationPoint.getInstanceId());
  }

  public void finishedRootInline() throws NormalizationException
  {
    final RenderBox insertationPoint = getInsertationPoint();
    if (insertationPoint instanceof ParagraphPoolBox == false)
    {
      getInsertationPoint();
      throw new IllegalStateException
          ("Assertation: A rootInline must call close on a pool box");
    }

    final RenderableTextFactory textFactory = getCurrentTextFactory();
    final RenderNode[] nodes = textFactory.finishText();
    insertationPoint.addChilds(nodes);
    insertationPoint.close();

    tryValidateOutput(insertationPoint.getInstanceId());
  }

  public void finishedMarker() throws NormalizationException
  {
    final RenderBox insertationPoint = getInsertationPoint();
    final RenderableTextFactory textFactory = getCurrentTextFactory();
    final RenderNode[] nodes = textFactory.finishText();
    insertationPoint.addChilds(nodes);

    insertationPoint.close();
    tryValidateOutput(insertationPoint.getInstanceId());
  }

  public void finishedBlock() throws NormalizationException
  {
    final RenderBox insertationPoint = getInsertationPoint();
    final RenderableTextFactory textFactory = getCurrentTextFactory();
    insertationPoint.addChilds(textFactory.finishText());
    insertationPoint.close();
    tryValidateOutput(insertationPoint.getInstanceId());
  }

  public void finishedTableCell() throws NormalizationException
  {
    final RenderBox insertationPoint = getInsertationPoint();
    final RenderableTextFactory textFactory = getCurrentTextFactory();
    insertationPoint.addChilds(textFactory.finishText());
    insertationPoint.close();

    // A table cell is always inside a table row - and that one must be closed
    // before the layouting can continue ..

    // Update the validation tracker; but do not validate...
    isValidatable(insertationPoint);

  }

  public void finishedTableRow() throws NormalizationException
  {
    final RenderBox insertationPoint = getInsertationPoint();
    final RenderableTextFactory textFactory = getCurrentTextFactory();
    insertationPoint.addChilds(textFactory.finishText());
    insertationPoint.close();
    tryValidateOutput(insertationPoint.getInstanceId());
  }

  public void finishedTableSection() throws NormalizationException
  {
    final RenderBox insertationPoint = getInsertationPoint();
    final RenderableTextFactory textFactory = getCurrentTextFactory();
    insertationPoint.addChilds(textFactory.finishText());
    insertationPoint.close();
    tryValidateOutput(insertationPoint.getInstanceId());
  }

  public void finishedTableColumnGroup() throws NormalizationException
  {
    final RenderBox insertationPoint = getInsertationPoint();
    final RenderableTextFactory textFactory = getCurrentTextFactory();
    insertationPoint.addChilds(textFactory.finishText());
    insertationPoint.close();
    // Table Col-groups have no influence on the layout ..
    // tryValidateOutput();
  }

  public void finishedTableColumn() throws NormalizationException
  {
    final RenderBox insertationPoint = getInsertationPoint();
    final RenderableTextFactory textFactory = getCurrentTextFactory();
    insertationPoint.addChilds(textFactory.finishText());
    // tryValidateOutput();
  }

  public void finishedTable() throws NormalizationException
  {
    final RenderBox insertationPoint = getInsertationPoint();
    final RenderableTextFactory textFactory = getCurrentTextFactory();
    insertationPoint.addChilds(textFactory.finishText());
    insertationPoint.close();

    tryValidateOutput(insertationPoint.getInstanceId());
  }

  public void finishedFlow() throws NormalizationException
  {
    final RenderBox insertationPoint = getInsertationPoint();
    final RenderableTextFactory textFactory = getCurrentTextFactory();
    insertationPoint.addChilds(textFactory.finishText());
    insertationPoint.close();

    flowContexts.pop();

    tryValidateOutput(insertationPoint.getInstanceId());
  }

  public void finishedDocument() throws NormalizationException
  {
    logicalPageBox.close();
    tryValidateOutput(logicalPageBox.getInstanceId());
    //validateOutput();
    // At this point, we should have performed the necessary output.

    // Ok, lets play a little bit
    // todo: This is the end of the document, we should do some smarter things
    // here.
  }

  public RenderPageContext getPageContext()
  {
    return pageContext;
  }

  public void handlePageBreak(final PageContext pageContext)
  {
    if (pageContext == null)
    {
      throw new NullPointerException();
    }

    this.pageContext = this.pageContext.update
        (pageContext, layoutProcess.getOutputProcessor());
    final PageGrid pageGrid =
        this.pageContext.createPageGrid(layoutProcess.getOutputMetaData());

    this.pendingStore = (ContentStore) pendingStore.derive();
    this.elementsStore = (ContentStore) elementsStore.derive();
    this.logicalPageBox.updatePageArea(pageGrid);
  }

  public void startedPassThrough(final LayoutContext context)
  {

  }

  public void addPassThroughContent(final LayoutContext context,
                                    final ContentToken content)
  {

  }

  public void finishedPassThrough()
  {

  }


  public void startedTableCaption(final LayoutContext context)
      throws NormalizationException
  {

  }

  public void finishedTableCaption() throws NormalizationException
  {

  }
}
