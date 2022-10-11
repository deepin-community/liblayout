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
 * $Id: FastDisplayModelBuilder.java 6489 2008-11-28 14:53:40Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.layouting.normalizer.displaymodel;

import java.io.IOException;

import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.State;
import org.jfree.layouting.StateException;
import org.jfree.layouting.StatefullComponent;
import org.jfree.layouting.input.style.keys.box.BoxStyleKeys;
import org.jfree.layouting.input.style.keys.box.DisplayModel;
import org.jfree.layouting.input.style.keys.box.DisplayRole;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.layouter.content.ContentToken;
import org.jfree.layouting.layouter.context.ContextId;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.layouter.context.PageContext;
import org.jfree.layouting.layouter.style.resolver.StyleResolver;
import org.jfree.layouting.normalizer.content.NormalizationException;
import org.jfree.layouting.normalizer.generator.ContentGenerator;
import org.jfree.layouting.renderer.Renderer;
import org.jfree.layouting.util.IntList;
import org.pentaho.reporting.libraries.base.util.FastStack;

/**
 * Creation-Date: Jan 5, 2007, 3:01:09 PM
 *
 * @author Thomas Morgner
 */
public class FastDisplayModelBuilder implements ModelBuilder
{
  private static class FastDisplayModelBuilderState implements State
  {
    private FlowContext[] contexts;
    private int suspendCounter;
    private State contentGeneratorState;

    private FastDisplayModelBuilderState(final FastDisplayModelBuilder builder)
        throws StateException
    {
      this.contentGeneratorState = builder.contentGenerator.saveState();
      this.suspendCounter = builder.suspendCounter;
      this.contexts = new FlowContext[builder.flowContexts.size()];
      try
      {
        for (int i = 0; i < contexts.length; i++)
        {
          final FlowContext flowContext = (FlowContext) builder.flowContexts.get(i);
          contexts[i] = (FlowContext) flowContext.clone();

        }
      }
      catch (CloneNotSupportedException cne)
      {
        throw new StateException("Cloning failed.", cne);
      }
    }

    /**
     * Creates a restored instance of the saved component.
     * <p/>
     * By using this factory-like approach, we gain independence from having to
     * know the actual implementation. This makes things a lot easier.
     *
     * @param layoutProcess the layout process that controls it all
     * @return the saved state
     * @throws org.jfree.layouting.StateException
     *
     */
    public StatefullComponent restore(final LayoutProcess layoutProcess)
        throws StateException
    {
      try
      {
        final FastDisplayModelBuilder fmb = new FastDisplayModelBuilder();
        fmb.layoutProcess = layoutProcess;
        fmb.flowContexts = new FastStack();
        fmb.suspendCounter = suspendCounter;
        for (int i = 0; i < contexts.length; i++)
        {
          final FlowContext context = contexts[i];
          fmb.flowContexts.push(context.clone());
        }
        fmb.contentGenerator = (ContentGenerator) contentGeneratorState.restore(layoutProcess);
        return fmb;
      }
      catch (CloneNotSupportedException e)
      {
        throw new StateException("Clone failed", e);
      }
    }
  }

  public static final int TYPE_BLOCK = 1;
  public static final int TYPE_INLINE = 2;
  public static final int TYPE_MARKER = 3;
  public static final int TYPE_RUBY = 4;
  public static final int TYPE_TABLE = 5;
  public static final int TYPE_TABLE_CELL = 6;
  public static final int TYPE_TABLE_ROW = 7;
  public static final int TYPE_TABLE_SECTION = 8;
  public static final int TYPE_TABLE_COLGROUP = 9;
  public static final int TYPE_TABLE_COL = 10;
  public static final int TYPE_TABLE_CAPTION = 11;
  public static final int TYPE_IGNORED = 12;
  public static final int TYPE_PARAGRAPH = 13;
  public static final int TYPE_FLOW = 14;
  public static final int FLAG_AUTOGENERATED = 0x01000000;

  public static final int MODEL_BLOCK_INSIDE = 1;
  public static final int MODEL_INLINE_INSIDE = 2;
  public static final int MODEL_TABLE = 3;
  public static final int MODEL_TABLE_SECTION = 4;
  public static final int MODEL_TABLE_ROW = 5;
  public static final int MODEL_TABLE_COLGROUP = 6;
  public static final int MODEL_TABLE_COL = 7;

  private LayoutProcess layoutProcess;
  private FastStack flowContexts;
  private ContentGenerator contentGenerator;
  private int suspendCounter;

  public FastDisplayModelBuilder()
  {
  }

  public FastDisplayModelBuilder(final ContentGenerator contentGenerator,
                                 final LayoutProcess layoutProcess)
  {
    if (layoutProcess == null)
    {
      throw new NullPointerException();
    }
    if (contentGenerator == null)
    {
      throw new NullPointerException();
    }
    this.contentGenerator = contentGenerator;
    this.layoutProcess = layoutProcess;
    this.flowContexts = new FastStack();
  }

  public void startDocument(final PageContext pageContext)
      throws NormalizationException
  {
    contentGenerator.startedDocument(pageContext);
  }


  public void startElement(final LayoutContext layoutContext)
      throws NormalizationException, IOException
  {
    // Make sure that we have a root-context
    if (flowContexts.isEmpty())
    {
      // open up a new flow-context ..
      final FlowContext fc = new FlowContext();
      flowContexts.push(fc);
      fc.addElement(MODEL_BLOCK_INSIDE, FLAG_AUTOGENERATED | TYPE_FLOW, layoutContext);
      contentGenerator.startedFlow(layoutContext);
    }

    final CSSValue displayRole = layoutContext.getValue(BoxStyleKeys.DISPLAY_ROLE);
    if (displayRole == DisplayRole.NONE || suspendCounter > 0)
    {
      // do something with that.
      suspendCounter += 1;
      contentGenerator.startedPassThrough(layoutContext);
      return;
    }

    if (displayRole == DisplayRole.TABLE_CAPTION)
    {
      addTableCaption(layoutContext);
      return;
    }
    else if (displayRole == DisplayRole.TABLE_COLUMN)
    {
      addTableColumn(layoutContext);
      return;
    }
    else if (displayRole == DisplayRole.TABLE_COLUMN_GROUP)
    {
      addTableColumnGroup(layoutContext);
      return;
    }
    else if (displayRole == DisplayRole.TABLE_FOOTER_GROUP ||
        displayRole == DisplayRole.TABLE_HEADER_GROUP ||
        displayRole == DisplayRole.TABLE_ROW_GROUP)
    {
      addTableSection(layoutContext);
      return;
    }
    else if (displayRole == DisplayRole.TABLE_ROW)
    {
      addTableRow(layoutContext);
      return;
    }
    else if (displayRole == DisplayRole.TABLE_CELL)
    {
      addTableCell(layoutContext);
      return;
    }

    final FlowContext fc = (FlowContext) flowContexts.peek();
    final int currentDisplayModel = fc.getActiveDisplayModel();
    switch (currentDisplayModel)
    {
      case MODEL_BLOCK_INSIDE:
      {
        //closeSuspended();
        addToBlockLevelBox(layoutContext);
        return;
      }
      case MODEL_INLINE_INSIDE:
      {
        addToInlineLevelBox(layoutContext);
        return;
      }
      case MODEL_TABLE:
      {
        addToTableLevelBox(layoutContext);
        return;
      }
      case MODEL_TABLE_SECTION:
      {
        // Currently, we're in a table-body element, and expect rows to follow.
        // Stupid user gave us something else instead, so we have to autogenerate
        // a row and a cell..
        // Autogenerate the table-row ..
        final StyleResolver styleResolver = layoutProcess.getStyleResolver();
        final ContextId id = new ContextId(ContextId.SOURCE_DISPLAY_MODEL, -1, 0);
        final LayoutContext rowContext =
            styleResolver.createAnonymousContext(id, layoutContext);
        fc.addElement(MODEL_TABLE_ROW, FLAG_AUTOGENERATED | TYPE_TABLE_ROW, rowContext);
        contentGenerator.startedTableRow(rowContext);

        final LayoutContext cellContext =
            styleResolver.createAnonymousContext(id, rowContext);
        fc.addElement(MODEL_BLOCK_INSIDE, FLAG_AUTOGENERATED | TYPE_TABLE_CELL, cellContext);
        contentGenerator.startedTableCell(cellContext);
        addToBlockLevelBox(layoutContext);
        return;
      }
      case MODEL_TABLE_ROW:
      {
        final StyleResolver styleResolver = layoutProcess.getStyleResolver();
        final ContextId id = new ContextId(ContextId.SOURCE_DISPLAY_MODEL, -1, 0);

        final LayoutContext cellContext =
            styleResolver.createAnonymousContext(id, layoutContext);
        fc.addElement(MODEL_BLOCK_INSIDE, FLAG_AUTOGENERATED | TYPE_TABLE_CELL, cellContext);
        contentGenerator.startedTableCell(cellContext);
        addToBlockLevelBox(layoutContext);
        return;
      }
      case MODEL_TABLE_COLGROUP:
      case MODEL_TABLE_COL:
      {
        // This is clearly stupid. We handle that as pass-through content as
        // columns and colgroups are not displayable at all.
        suspendCounter += 1;
        contentGenerator.startedPassThrough(layoutContext);
        return;
      }

      default:
      {
        throw new NormalizationException("Unexpected type: " +
            currentDisplayModel + " for display-role " + displayRole);
      }
    }
  }
//
//  private void closeSuspended() throws NormalizationException
//  {
//    final FlowContext fc = (FlowContext) flowContexts.peek();
//    int state = fc.getCurrentState();
//    while (state == FlowContext.STATE_SUSPEND)
//    {
//      callFinish(fc.getCurrentDisplayRole());
//      fc.close();
//      state = fc.getCurrentState();
//    }
//  }

  private void addTableCaption(final LayoutContext context)
      throws NormalizationException
  {
    final FlowContext fc = (FlowContext) flowContexts.peek();
    // check if the parent is a table.
    final int currentDisplayModel = fc.getCurrentDisplayModel();
    if (currentDisplayModel != MODEL_TABLE)
    {
      // Autogenerate the table ..
      final StyleResolver styleResolver = layoutProcess.getStyleResolver();
      final ContextId id = new ContextId(ContextId.SOURCE_DISPLAY_MODEL, -1, 0);

      final LayoutContext tableContext =
          styleResolver.createAnonymousContext(id, context);
      fc.addElement(MODEL_TABLE, FLAG_AUTOGENERATED | TYPE_TABLE, tableContext);
      contentGenerator.startedTable(tableContext);
    }

    fc.addElement(MODEL_BLOCK_INSIDE, TYPE_TABLE_CAPTION, context);
    contentGenerator.startedTableCaption(context);
  }

  private void addTableColumnGroup(final LayoutContext context)
      throws NormalizationException
  {
    final FlowContext fc = (FlowContext) flowContexts.peek();
    // check if the parent is a table.
    final int currentDisplayModel = fc.getCurrentDisplayModel();
    if (currentDisplayModel != MODEL_TABLE)
    {
      // Autogenerate the table ..
      final StyleResolver styleResolver = layoutProcess.getStyleResolver();
      final ContextId id = new ContextId(ContextId.SOURCE_DISPLAY_MODEL, -1, 0);

      final LayoutContext tableContext =
          styleResolver.createAnonymousContext(id, context);
      fc.addElement(MODEL_TABLE, FLAG_AUTOGENERATED | TYPE_TABLE, tableContext);
      contentGenerator.startedTable(tableContext);
    }

    fc.addElement(MODEL_TABLE_COLGROUP, TYPE_TABLE_COLGROUP, context);
    contentGenerator.startedTableColumnGroup(context);
  }

  private void addTableColumn(final LayoutContext context)
      throws NormalizationException
  {
    final FlowContext fc = (FlowContext) flowContexts.peek();
    // check if the parent is a table.
    final int currentDisplayModel = fc.getCurrentDisplayModel();
    if (currentDisplayModel != MODEL_TABLE &&
        currentDisplayModel != MODEL_TABLE_COLGROUP)
    {
      // Autogenerate the table ..
      final StyleResolver styleResolver = layoutProcess.getStyleResolver();
      final ContextId id = new ContextId(ContextId.SOURCE_DISPLAY_MODEL, -1, 0);

      suspendNonBlockElements();
      final LayoutContext tableContext =
          styleResolver.createAnonymousContext(id, context);
      fc.addElement(MODEL_TABLE, FLAG_AUTOGENERATED | TYPE_TABLE, tableContext);
      contentGenerator.startedTable(tableContext);
    }

    fc.addElement(MODEL_TABLE_COL, TYPE_TABLE_COL, context);
    contentGenerator.startedTableColumn(context);
  }

  private void addTableSection(final LayoutContext context)
      throws NormalizationException
  {
    final FlowContext fc = (FlowContext) flowContexts.peek();
    // check if the parent is a table.
    final int currentDisplayModel = fc.getCurrentDisplayModel();
    if (currentDisplayModel != MODEL_TABLE)
    {
      // Autogenerate the table ..
      final StyleResolver styleResolver = layoutProcess.getStyleResolver();
      final ContextId id = new ContextId(ContextId.SOURCE_DISPLAY_MODEL, -1, 0);

      // the table is a block level element here, so we have to close any open
      // paragraphs before proceeding ..
      suspendNonBlockElements();

      final LayoutContext tableContext =
          styleResolver.createAnonymousContext(id, context);
      fc.addElement(MODEL_TABLE, FLAG_AUTOGENERATED | TYPE_TABLE, tableContext);
      contentGenerator.startedTable(tableContext);
    }

    fc.addElement(MODEL_TABLE_SECTION, TYPE_TABLE_SECTION, context);
    contentGenerator.startedTableSection(context);
  }

  private void addTableRow(final LayoutContext context)
      throws NormalizationException
  {
    final FlowContext fc = (FlowContext) flowContexts.peek();
    // check if the parent is a table.
    final int currentDisplayModel = fc.getCurrentDisplayModel();
    if (currentDisplayModel != MODEL_TABLE_SECTION)
    {
      final StyleResolver styleResolver = layoutProcess.getStyleResolver();
      final ContextId id = new ContextId(ContextId.SOURCE_DISPLAY_MODEL, -1, 0);

      if (currentDisplayModel != MODEL_TABLE)
      {
        // there is not even a table .. generate it
        // Autogenerate the table ..
        final LayoutContext tableContext =
            styleResolver.createAnonymousContext(id, context);

        suspendNonBlockElements();
        fc.addElement(MODEL_TABLE, FLAG_AUTOGENERATED | TYPE_TABLE, tableContext);
        contentGenerator.startedTable(tableContext);
      }

      // OK, finally add the table section ..
      final LayoutContext sectionContext =
          styleResolver.createAnonymousContext(id, context);
      fc.addElement(MODEL_TABLE_SECTION, FLAG_AUTOGENERATED | TYPE_TABLE_SECTION, sectionContext);
      contentGenerator.startedTableSection(sectionContext);
    }

    fc.addElement(MODEL_TABLE_ROW, TYPE_TABLE_ROW, context);
    contentGenerator.startedTableRow(context);
  }

  private void addTableCell(final LayoutContext context)
      throws NormalizationException
  {
    final FlowContext fc = (FlowContext) flowContexts.peek();
    // check if the parent is a table.
    int currentDisplayModel = fc.getCurrentDisplayModel();
    if (currentDisplayModel != MODEL_TABLE_ROW)
    {
      final StyleResolver styleResolver = layoutProcess.getStyleResolver();
      final ContextId id = new ContextId(ContextId.SOURCE_DISPLAY_MODEL, -1, 0);

      if (currentDisplayModel != MODEL_TABLE &&
          currentDisplayModel != MODEL_TABLE_SECTION)
      {
        // generate the table ..
        suspendNonBlockElements();

        final LayoutContext tableContext =
            styleResolver.createAnonymousContext(id, context);
        fc.addElement(MODEL_TABLE, FLAG_AUTOGENERATED | TYPE_TABLE, tableContext);
        contentGenerator.startedTable(tableContext);
        currentDisplayModel = MODEL_TABLE;
      }

      if (currentDisplayModel != MODEL_TABLE_SECTION)
      {
        // OK, generate the table section ..
        final LayoutContext sectionContext =
            styleResolver.createAnonymousContext(id, context);
        fc.addElement(MODEL_TABLE_SECTION, FLAG_AUTOGENERATED | TYPE_TABLE_SECTION, sectionContext);
        contentGenerator.startedTableSection(sectionContext);
      }

      // Autogenerate the table-row ..
      final LayoutContext rowContext =
          styleResolver.createAnonymousContext(id, context);
      fc.addElement(MODEL_TABLE_ROW, FLAG_AUTOGENERATED | TYPE_TABLE_ROW, rowContext);
      contentGenerator.startedTableRow(rowContext);
    }

    fc.addElement(MODEL_BLOCK_INSIDE, TYPE_TABLE_CELL, context);
    contentGenerator.startedTableCell(context);
  }

  private void addToBlockLevelBox(final LayoutContext context)
      throws NormalizationException
  {
    final FlowContext fc = (FlowContext) flowContexts.peek();
    final CSSValue displayRole = context.getValue(BoxStyleKeys.DISPLAY_ROLE);
    if (DisplayRole.INLINE.equals(displayRole))
    {
      // special treatment for a inline box that is added to a block-parent
      // Generate the paragraph box ..
      if (reactivateSuspendedInlines(fc) == false)
      {
        // todo: check for possibly suspended inlines and reactivate them

        final StyleResolver styleResolver = layoutProcess.getStyleResolver();
        final ContextId id = new ContextId(ContextId.SOURCE_DISPLAY_MODEL, -1, 0);
        final LayoutContext paragraphContext =
            styleResolver.createAnonymousContext(id, context);
        fc.addElement(MODEL_INLINE_INSIDE, FLAG_AUTOGENERATED | TYPE_PARAGRAPH, paragraphContext);
        contentGenerator.startedRootInline(paragraphContext);
      }

      // now add the element ..
      final CSSValue displayModel = context.getValue(BoxStyleKeys.DISPLAY_MODEL);
      if (DisplayModel.TABLE.equals(displayModel))
      {
        fc.addElement(MODEL_TABLE, TYPE_INLINE, context);
        contentGenerator.startedTable(context);
      }
      else if (DisplayModel.INLINE_INSIDE.equals(displayModel))
      {
        fc.addElement(MODEL_INLINE_INSIDE, TYPE_INLINE, context);
        contentGenerator.startedInline(context);
      }
      else
      {
        fc.addElement(MODEL_BLOCK_INSIDE, TYPE_BLOCK, context);
        contentGenerator.startedBlock(context);
      }
      return;
    }

    // Todo: need to check for run-in and compact as well..
    // Everything else is considered a block-level element and treated as such..
    final CSSValue displayModel = context.getValue(BoxStyleKeys.DISPLAY_MODEL);
    if (DisplayModel.TABLE.equals(displayModel))
    {
      fc.addElement(MODEL_TABLE, TYPE_TABLE, context);
      contentGenerator.startedTable(context);
    }
    else if (DisplayModel.INLINE_INSIDE.equals(displayModel))
    {
      // A block level element that declares that all its direct childs should
      // be treated as inline-level elements. We create a paragraph box for that
      // one.
      fc.addElement(MODEL_INLINE_INSIDE, TYPE_BLOCK, context);
      contentGenerator.startedBlock(context);

      final StyleResolver styleResolver = layoutProcess.getStyleResolver();
      final ContextId id = new ContextId(ContextId.SOURCE_DISPLAY_MODEL, -1, 0);
      final LayoutContext paragraphContext =
          styleResolver.createAnonymousContext(id, context);
      fc.addElement(MODEL_INLINE_INSIDE,
          FLAG_AUTOGENERATED | TYPE_PARAGRAPH, paragraphContext);
      contentGenerator.startedRootInline(paragraphContext);
    }
    else
    {
      fc.addElement(MODEL_BLOCK_INSIDE, TYPE_BLOCK, context);
      contentGenerator.startedBlock(context);
    }
  }

  private void addToInlineLevelBox(final LayoutContext context)
      throws NormalizationException
  {
    final FlowContext fc = (FlowContext) flowContexts.peek();
    final CSSValue displayRole = context.getValue(BoxStyleKeys.DISPLAY_ROLE);
    if (DisplayRole.INLINE.equals(displayRole))
    {
      // The current linebox is suspended and the boxes are added as if they
      // were direct childs of the current block-context.
      // now add the element ..
      final CSSValue displayModel = context.getValue(BoxStyleKeys.DISPLAY_MODEL);
      if (DisplayModel.TABLE.equals(displayModel))
      {
        fc.addElement(MODEL_TABLE, TYPE_TABLE, context);
        contentGenerator.startedTable(context);
      }
      else if (DisplayModel.INLINE_INSIDE.equals(displayModel))
      {
        fc.addElement(MODEL_INLINE_INSIDE, TYPE_INLINE, context);
        contentGenerator.startedInline(context);
      }
      else
      {
        fc.addElement(MODEL_BLOCK_INSIDE, TYPE_BLOCK, context);
        contentGenerator.startedBlock(context);
      }
      return;
    }

    // we have to suspend the lineboxes to get access to the block-level context.
    // then follow the ordinary block-level procedure ..
    suspendNonBlockElements();
    addToBlockLevelBox(context);
  }

  private void suspendNonBlockElements()
      throws NormalizationException
  {
    final FlowContext fc = (FlowContext) flowContexts.peek();
    int model = fc.getCurrentDisplayModel();
    while (model != MODEL_BLOCK_INSIDE)
    {
      final int currentDisplayRole = fc.getCurrentDisplayRole();
      callFinish(currentDisplayRole);
      fc.suspend();
      model = fc.getCurrentDisplayModel();
    }
  }

  private void addToTableLevelBox(final LayoutContext context)
      throws NormalizationException
  {
    final StyleResolver styleResolver = layoutProcess.getStyleResolver();
    final ContextId id = new ContextId(ContextId.SOURCE_DISPLAY_MODEL, -1, 0);

    // This is some non-table content that has been added to a table.
    // This generally means that someone messed up the layout, we have to
    // generate a default table ...
    final FlowContext fc = (FlowContext) flowContexts.peek();

    final LayoutContext sectionContext =
        styleResolver.createAnonymousContext(id, context);
    fc.addElement(MODEL_TABLE_SECTION, FLAG_AUTOGENERATED | TYPE_TABLE_SECTION, sectionContext);
    contentGenerator.startedTableSection(sectionContext);

    final LayoutContext rowContext =
        styleResolver.createAnonymousContext(id, sectionContext);
    fc.addElement(MODEL_TABLE_ROW, FLAG_AUTOGENERATED | TYPE_TABLE_ROW, rowContext);
    contentGenerator.startedTableRow(rowContext);

    final LayoutContext cellContext =
        styleResolver.createAnonymousContext(id, rowContext);
    fc.addElement(MODEL_BLOCK_INSIDE, FLAG_AUTOGENERATED | TYPE_TABLE_CELL, cellContext);
    contentGenerator.startedTableCell(cellContext);

    addToBlockLevelBox(context);
  }

  public void addContent(final ContentToken content)
      throws NormalizationException
  {
    final FlowContext fc = (FlowContext) flowContexts.peek();
    final LayoutContext layoutContext = fc.getCurrentLayoutContext();

    if (suspendCounter > 0)
    {
      contentGenerator.addPassThroughContent(layoutContext, content);
      return;
    }

    final int currentDisplayModel = fc.getActiveDisplayModel();
    if (currentDisplayModel == MODEL_BLOCK_INSIDE)
    {
      // todo: check for suspended inlines and reactivate them if needed.
      if (reactivateSuspendedInlines(fc) == false)
      {
        // generate a inline box ..
        final StyleResolver styleResolver = layoutProcess.getStyleResolver();
        final ContextId id = new ContextId(ContextId.SOURCE_DISPLAY_MODEL, -1, 0);
        final LayoutContext paragraphContext =
            styleResolver.createAnonymousContext(id, layoutContext);
        fc.addElement(MODEL_INLINE_INSIDE, FLAG_AUTOGENERATED | TYPE_PARAGRAPH, paragraphContext);
        contentGenerator.startedRootInline(paragraphContext);
        contentGenerator.addContent(paragraphContext, content);
      }
      else
      {
        contentGenerator.addContent(layoutContext, content);
      }
      return;
    }

    if (currentDisplayModel == MODEL_INLINE_INSIDE)
    {
      reactivateSuspendedInlines(fc);
      contentGenerator.addContent(layoutContext, content);
    }
    else
    {
      // content that has been added inside a table or any of the table
      // elements is ignored - ok, we add it anyway, but whether it is
      // rendered is not up to us.
      // (Maybe we should not forward the content?)
      contentGenerator.addContent(layoutContext, content);
    }
  }

  private boolean reactivateSuspendedInlines(final FlowContext fc)
      throws NormalizationException
  {
    // If there are suspended inlines, reactivate them ..
    if (fc.getCurrentState() != FlowContext.STATE_SUSPEND)
    {
      return false;
    }

    final IntList roles = new IntList(5);
    final IntList models = new IntList(5);
    final FastStack contexts = new FastStack();
    while (fc.getCurrentState() == FlowContext.STATE_SUSPEND)
    {
      contexts.push(fc.getCurrentLayoutContext());
      roles.push(fc.getCurrentDisplayRole());
      models.push(fc.getCurrentDisplayModel());
      fc.close();
    }

    while (contexts.isEmpty() == false)
    {
      final LayoutContext lc = (LayoutContext) contexts.pop();
      final int role = roles.pop();
      final int model = models.pop();
      final int cleanRole = role & 0xFFFFFF;
      if (cleanRole == TYPE_INLINE)
      {
        contentGenerator.startedInline(lc);
        fc.addElement(model, role, lc);
      }
      else if (cleanRole == TYPE_PARAGRAPH)
      {
        contentGenerator.startedRootInline(lc);
        fc.addElement(model, role, lc);
      }
      else
      {
        throw new IllegalStateException("Unexpected role: " + cleanRole);
      }
    }

    return true;
  }

  public void endElement() throws NormalizationException
  {
    if (suspendCounter > 0)
    {
      contentGenerator.finishedPassThrough();
      suspendCounter -= 1;
      return;
    }

    // close all suspended elements and the first non suspendend element
    FlowContext context = (FlowContext) flowContexts.peek();

    int role;
    do
    {
      role = context.getCurrentDisplayRole();
      if (context.getCurrentState() == FlowContext.STATE_OPEN)
      {
        callFinish(role);
      }
      else
      {
        final LayoutContext lc = context.getCurrentLayoutContext();
      }
      context.close();
      if (role == TYPE_FLOW)
      {
        context = (FlowContext) flowContexts.peek();
      }
    }
    while ((role & FLAG_AUTOGENERATED) == FLAG_AUTOGENERATED &&
        context.isEmpty() == false);

  }

  public void endDocument() throws NormalizationException
  {
    endElement();
    final FlowContext context = (FlowContext) flowContexts.pop();
    contentGenerator.finishedDocument();
  }


  private void callFinish(final int type) throws NormalizationException
  {
    switch ((type & 0xffffff))
    {
      case TYPE_BLOCK:
      {
        contentGenerator.finishedBlock();
        break;
      }
      case TYPE_FLOW:
      {
        contentGenerator.finishedFlow();
        break;
      }
      case TYPE_TABLE:
      {
        contentGenerator.finishedTable();
        break;
      }
      case TYPE_TABLE_CAPTION:
      {
        contentGenerator.finishedTableCaption();
        break;
      }
      case TYPE_TABLE_CELL:
      {
        contentGenerator.finishedTableCell();
        break;
      }
      case TYPE_TABLE_COL:
      {
        contentGenerator.finishedTableColumn();
        break;
      }
      case TYPE_TABLE_COLGROUP:
      {
        contentGenerator.finishedTableColumnGroup();
        break;
      }
      case TYPE_TABLE_ROW:
      {
        contentGenerator.finishedTableRow();
        break;
      }
      case TYPE_TABLE_SECTION:
      {
        contentGenerator.finishedTableSection();
        break;
      }
      case TYPE_INLINE:
      {
        contentGenerator.finishedInline();
        break;
      }
      case TYPE_MARKER:
      {
        contentGenerator.finishedMarker();
        break;
      }
      case TYPE_PARAGRAPH:
      {
        contentGenerator.finishedRootInline();
        break;
      }
      default:
      {
        throw new IllegalStateException();
      }
    }
  }

  public void handlePageBreak(final PageContext pageContext)
      throws NormalizationException
  {
    contentGenerator.handlePageBreak(pageContext);
  }

  public Renderer getRenderer()
  {
    return contentGenerator.getRenderer();
  }

  public State saveState() throws StateException
  {
    return new FastDisplayModelBuilderState(this);
  }
}
