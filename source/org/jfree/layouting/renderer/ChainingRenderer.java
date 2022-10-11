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
 * $Id: ChainingRenderer.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.renderer;

import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.State;
import org.jfree.layouting.StateException;
import org.jfree.layouting.StatefullComponent;
import org.jfree.layouting.layouter.content.ContentToken;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.layouter.context.PageContext;
import org.jfree.layouting.normalizer.content.NormalizationException;
import org.jfree.layouting.util.ChainingComponent;

/**
 * Creation-Date: 16.06.2006, 14:49:14
 *
 * @author Thomas Morgner
 */
public class ChainingRenderer extends ChainingComponent implements Renderer
{
  private static class ChainingRendererState implements State
  {
    private State rendererState;

    private ChainingRendererState(final State rendererState)
    {
      this.rendererState = rendererState;
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
      final Renderer renderer = (Renderer) rendererState.restore(layoutProcess);
      return new ChainingRenderer(renderer);
    }
  }

  private static final int MTH_START_DOCUMENT = 1;
  private static final int MTH_START_SPECIAL_FLOW = 2;
  private static final int MTH_START_FLOW = 3;
  private static final int MTH_START_TABLE = 4;
  private static final int MTH_START_TABLE_COLGROUP = 5;
  private static final int MTH_START_TABLE_COL = 6;
  private static final int MTH_START_TABLE_SECTION = 7;
  private static final int MTH_START_TABLE_ROW = 8;
  private static final int MTH_START_TABLE_CELL = 9;
  private static final int MTH_START_BLOCK = 10;
  private static final int MTH_START_MARKER = 11;
  private static final int MTH_START_ROOT_INLINE = 12;
  private static final int MTH_START_INLINE = 13;
  private static final int MTH_START_TABLE_CAPTION = 14;

  private static final int MTH_END_TABLE_CAPTION = 18;
  private static final int MTH_END_INLINE = 19;
  private static final int MTH_END_ROOT_INLINE = 20;
  private static final int MTH_END_MARKER = 21;
  private static final int MTH_END_BLOCK = 22;
  private static final int MTH_END_TABLE_CELL = 23;
  private static final int MTH_END_TABLE_ROW = 24;
  private static final int MTH_END_TABLE_SECTION = 25;
  private static final int MTH_END_TABLE_COL = 26;
  private static final int MTH_END_TABLE_COLGROUP = 27;
  private static final int MTH_END_TABLE = 28;
  private static final int MTH_END_FLOW = 29;
  private static final int MTH_END_SPECIAL_FLOW = 30;
  private static final int MTH_END_DOCUMENT = 31;

  private static final int MTH_ADD_CONTENT = 41;
  private static final int MTH_HANDLE_PAGEBREAK = 42;

  private static final int MTH_START_PASSTHROUGH = 100;
  private static final int MTH_ADD_PASSTHROUGH_CONTENT = 101;
  private static final int MTH_END_PASSTHROUGH = 102;

  private Renderer renderer;

  public ChainingRenderer(final Renderer renderer)
  {
    this.renderer = renderer;
  }

  public Renderer getRenderer()
  {
    return renderer;
  }

  public void startedDocument(final PageContext pageContext)
  {
    addCall(new RecordedCall (MTH_START_DOCUMENT, pageContext));
  }

  /**
   * Starts a floating, absolute or static element. This establishes a new
   * normal flow for the element.
   *
   * @param context
   */
  public void startedFlow(final LayoutContext context)
  {
    addCall(new RecordedCall (MTH_START_FLOW, context));
  }

  public void startedTable(final LayoutContext layoutContext)
  {
    addCall(new RecordedCall (MTH_START_TABLE, layoutContext));
  }

  public void startedTableColumnGroup(final LayoutContext context)
          throws NormalizationException
  {
    addCall(new RecordedCall (MTH_START_TABLE_COLGROUP, context));
  }

  public void startedTableColumn(final LayoutContext context)
          throws NormalizationException
  {
    addCall(new RecordedCall (MTH_START_TABLE_COL, context));
  }

  public void startedTableSection(final LayoutContext layoutContext)
  {
    addCall(new RecordedCall (MTH_START_TABLE_SECTION, layoutContext));
  }

  public void startedTableRow(final LayoutContext layoutContext)
  {
    addCall(new RecordedCall (MTH_START_TABLE_ROW, layoutContext));
  }

  public void startedTableCell(final LayoutContext layoutContext)
  {
    addCall(new RecordedCall (MTH_START_TABLE_CELL, layoutContext));
  }

  public void startedBlock(final LayoutContext context)
  {
    addCall(new RecordedCall (MTH_START_BLOCK, context));
  }

  public void startedMarker(final LayoutContext context)
          throws NormalizationException
  {
    addCall(new RecordedCall (MTH_START_MARKER, context));
  }

  public void startedRootInline(final LayoutContext context)
          throws NormalizationException
  {
    addCall(new RecordedCall(MTH_START_ROOT_INLINE, context));
  }

  public void startedInline(final LayoutContext context)
  {
    addCall(new RecordedCall (MTH_START_INLINE, context));
  }

  public void addContent(final LayoutContext context,
                         final ContentToken content)
  {
    addCall(new RecordedCall (MTH_ADD_CONTENT, new Object[]{ context, content}));
  }

  public void finishedInline()
  {
    addCall(new RecordedCall (MTH_END_INLINE, null));
  }

  public void finishedRootInline() throws NormalizationException
  {
    addCall(new RecordedCall(MTH_END_ROOT_INLINE, null));
  }

  public void finishedMarker() throws NormalizationException
  {
    addCall(new RecordedCall(MTH_END_MARKER, null));
  }

  public void finishedBlock()
  {
    addCall(new RecordedCall (MTH_END_BLOCK, null));
  }

  public void finishedTableCell()
  {
    addCall(new RecordedCall (MTH_END_TABLE_CELL, null));
  }

  public void finishedTableRow()
  {
    addCall(new RecordedCall (MTH_END_TABLE_ROW, null));
  }

  public void finishedTableSection()
  {
    addCall(new RecordedCall (MTH_END_TABLE_SECTION, null));
  }

  public void finishedTableColumnGroup() throws NormalizationException
  {
    addCall(new RecordedCall (MTH_END_TABLE_COLGROUP, null));
  }

  public void finishedTableColumn() throws NormalizationException
  {
    addCall(new RecordedCall (MTH_END_TABLE_COL, null));
  }

  public void finishedTable()
  {
    addCall(new RecordedCall (MTH_END_TABLE, null));
  }

  public void finishedFlow()
  {
    addCall(new RecordedCall (MTH_END_FLOW, null));
  }

  public void finishedDocument()
  {
    addCall(new RecordedCall (MTH_END_DOCUMENT, null));
  }

  public void handlePageBreak(final PageContext pageContext)
  {
    addCall(new RecordedCall (MTH_HANDLE_PAGEBREAK, pageContext));
  }

  protected void invoke(final Object target, final int methodId, final Object parameters) throws Exception
  {
    final Renderer renderer = (Renderer) target;
    switch(methodId)
    {
      case MTH_START_DOCUMENT:
      {
        renderer.startedDocument((PageContext) parameters);
        break;
      }
      case MTH_START_FLOW:
      {
        renderer.startedFlow((LayoutContext) parameters);
        break;
      }
      case MTH_START_TABLE:
      {
        renderer.startedTable((LayoutContext) parameters);
        break;
      }
      case MTH_START_TABLE_COL:
      {
        renderer.startedTableColumn((LayoutContext) parameters);
        break;
      }
      case MTH_START_TABLE_COLGROUP:
      {
        renderer.startedTableColumnGroup((LayoutContext) parameters);
        break;
      }
      case MTH_START_TABLE_SECTION:
      {
        renderer.startedTableSection((LayoutContext) parameters);
        break;
      }
      case MTH_START_TABLE_ROW:
      {
        renderer.startedTableRow((LayoutContext) parameters);
        break;
      }
      case MTH_START_TABLE_CELL:
      {
        renderer.startedTableCell((LayoutContext) parameters);
        break;
      }
      case MTH_START_BLOCK:
      {
        renderer.startedBlock((LayoutContext) parameters);
        break;
      }
      case MTH_START_ROOT_INLINE:
      {
        renderer.startedRootInline((LayoutContext) parameters);
        break;
      }
      case MTH_START_MARKER:
      {
        renderer.startedMarker((LayoutContext) parameters);
        break;
      }
      case MTH_START_INLINE:
      {
        renderer.startedInline((LayoutContext) parameters);
        break;
      }
      case MTH_ADD_CONTENT:
      {
        final Object[] parms = (Object[]) parameters;
        renderer.addContent((LayoutContext) parms[0], (ContentToken) parms[1]);
        break;
      }
      case MTH_END_INLINE:
      {
        renderer.finishedInline();
        break;
      }
      case MTH_END_MARKER:
      {
        renderer.finishedMarker();
        break;
      }
      case MTH_END_TABLE_CELL:
      {
        renderer.finishedTableCell();
        break;
      }
      case MTH_END_TABLE_ROW:
      {
        renderer.finishedTableRow();
        break;
      }
      case MTH_END_TABLE_SECTION:
      {
        renderer.finishedTableSection();
        break;
      }
      case MTH_END_TABLE_COLGROUP:
      {
        renderer.finishedTableColumnGroup();
        break;
      }
      case MTH_END_TABLE_COL:
      {
        renderer.finishedTableColumn();
        break;
      }
      case MTH_END_TABLE:
      {
        renderer.finishedTable();
        break;
      }
      case MTH_END_BLOCK:
      {
        renderer.finishedBlock();
        break;
      }
      case MTH_END_ROOT_INLINE:
      {
        renderer.finishedRootInline();
        break;
      }
      case MTH_END_FLOW:
      {
        renderer.finishedFlow();
        break;
      }
      case MTH_END_DOCUMENT:
      {
        renderer.finishedDocument();
        break;
      }
      case MTH_HANDLE_PAGEBREAK:
      {
        renderer.handlePageBreak((PageContext) parameters);
        break;
      }
      case MTH_START_PASSTHROUGH:
      {
        renderer.startedPassThrough((LayoutContext) parameters);
      }
      case MTH_ADD_PASSTHROUGH_CONTENT:
      {
        final Object[] parms = (Object[]) parameters;
        renderer.addPassThroughContent((LayoutContext) parms[0], (ContentToken) parms[1]);
      }
      case MTH_END_PASSTHROUGH:
      {
        renderer.finishedPassThrough();
      }
      case MTH_START_TABLE_CAPTION:
      {
        renderer.startedTableCaption((LayoutContext) parameters);
      }
      case MTH_END_TABLE_CAPTION:
      {
        renderer.finishedTableCaption();
      }
      default:
      {
        throw new IllegalArgumentException("No such method!");
      }
    }
  }

  public void startedPassThrough(final LayoutContext context)
  {
    addCall(new RecordedCall(MTH_START_PASSTHROUGH, context));
  }

  public void addPassThroughContent(final LayoutContext context,
                                    final ContentToken content)
  {
    addCall(new RecordedCall (MTH_ADD_PASSTHROUGH_CONTENT, new Object[]{ context, content}));
  }

  public void finishedPassThrough()
  {
    addCall(new RecordedCall (MTH_END_PASSTHROUGH, null));
  }

  public void startedTableCaption(final LayoutContext context)
      throws NormalizationException
  {
    addCall(new RecordedCall(MTH_START_TABLE_CAPTION, context));
  }

  public void finishedTableCaption() throws NormalizationException
  {
    addCall(new RecordedCall (MTH_END_TABLE_CAPTION, null));
  }

  public State saveState() throws StateException
  {
    return new ChainingRendererState(renderer.saveState());
  }
}
