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
 * $Id: ModelPrinter.java 6489 2008-11-28 14:53:40Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.layouting.renderer;

import org.jfree.layouting.renderer.model.RenderBox;
import org.jfree.layouting.renderer.model.RenderNode;
import org.jfree.layouting.renderer.model.RenderableText;
import org.jfree.layouting.renderer.model.ParagraphRenderBox;
import org.jfree.layouting.renderer.model.table.TableCellRenderBox;
import org.jfree.layouting.renderer.model.table.TableRenderBox;
import org.jfree.layouting.renderer.model.table.TableRowInfoStructure;
import org.jfree.layouting.renderer.model.table.TableRowRenderBox;
import org.jfree.layouting.renderer.model.table.cells.TableCell;
import org.jfree.layouting.renderer.model.table.cols.TableColumn;
import org.jfree.layouting.renderer.model.table.cols.TableColumnModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Creation-Date: Jan 9, 2007, 2:22:59 PM
 *
 * @author Thomas Morgner
 */
public class ModelPrinter
{
  private static final Log logger = LogFactory.getLog (ModelPrinter.class);

  private ModelPrinter()
  {
  }

  public static void printParents (RenderNode node)
  {
    while (node != null)
    {
      final StringBuffer b = new StringBuffer();
      b.append(node.getClass().getName());
      b.append('[');
      b.append(Integer.toHexString(System.identityHashCode(node)));
      b.append(']');
      logger.debug (b);
      node = node.getParent();
    }
  }

  public static void print(final RenderBox box)
  {
    printBox(box, 0);
  }

  public static void printBox(final RenderBox box, final int level)
  {
    StringBuffer b = new StringBuffer();
    for (int i = 0; i < level; i++)
    {
      b.append("   ");
    }
    b.append(box.getClass().getName());
    b.append('[');
    b.append(Integer.toHexString(System.identityHashCode(box)));
    b.append(']');
    b.append("={x=");
    b.append(box.getX());
    b.append(", y=");
    b.append(box.getY());
    b.append(", width=");
    b.append(box.getWidth());
    b.append(", height=");
    b.append(box.getHeight());
    b.append('}');
    logger.debug(b.toString());

    b = new StringBuffer();
    for (int i = 0; i < level; i++)
    {
      b.append("   ");
    }
    b.append("- nodeLayoutProperties=");
    b.append(box.getNodeLayoutProperties());
    logger.debug(b.toString());

    b = new StringBuffer();
    for (int i = 0; i < level; i++)
    {
      b.append("   ");
    }
    b.append("- boxLayoutProperties=");
    b.append(box.getBoxLayoutProperties());
    logger.debug(b.toString());

    if (box instanceof TableRowRenderBox)
    {
      final TableRowRenderBox row = (TableRowRenderBox) box;
      final TableRowInfoStructure rowInfoStructure = row.getRowInfoStructure();

      for (int i = 0; i < rowInfoStructure.getCellCount(); i++)
      {
        final TableCell cell = rowInfoStructure.getCellAt(i);
        logger.debug ("CELL: " + i + " = " + cell.getRowSpan() + ' ' + cell.getColSpan() + ' ' + cell);
      }
    }
    else if (box instanceof TableRenderBox)
    {
      final TableRenderBox table = (TableRenderBox) box;
      final TableColumnModel columnModel = table.getColumnModel();
      for (int i = 0; i < columnModel.getColumnCount(); i++)
      {
        final TableColumn col = columnModel.getColumn(i);
        logger.debug ("COLUMN: EffectiveSize: " + col.getEffectiveSize() +  " Computed Max Width: " + col.getComputedMaximumWidth() + " Computed ChunkSize: " + col.getComputedMinChunkSize());
//        for (int cs = 1; cs < 3; cs++)
//        {
//          Log.debug ("* COLUMN: " + i + "(" + cs + ") " +
//                  col.getPreferredSize(cs) + " " +
//                  col.getMinimumChunkSize(cs));
//        }
//        Log.debug ("COLUMN: " + i + " " +
//                col.getPreferredSize() + " " +
//                col.getMinimumChunkSize());
      }
    }
    else if (box instanceof TableCellRenderBox)
    {
      final TableCellRenderBox cellBox = (TableCellRenderBox) box;
      logger.debug ("CELL: Position: " + cellBox.getColumnIndex());
    }
    else if (box instanceof ParagraphRenderBox)
    {
      final ParagraphRenderBox paraBox = (ParagraphRenderBox) box;
      logger.debug ("-----------------------------------------------------");
      printBox(paraBox.getLineboxContainer(), level + 1);
      logger.debug ("-----------------------------------------------------");
    }

    printChilds(box, level);
  }

  private static void printChilds(final RenderBox box, final int level)
  {
    RenderNode childs = box.getFirstChild();
    while (childs != null)
    {
      if (childs instanceof RenderBox)
      {
        printBox((RenderBox) childs, level + 1);
      }
      else if (childs instanceof RenderableText)
      {
        printText((RenderableText) childs, level + 1);
      }
      else
      {
        printNode(childs, level + 1);
      }
      childs = childs.getNext();
    }
  }

  private static void printNode(final RenderNode node, final int level)
  {
    StringBuffer b = new StringBuffer();
    for (int i = 0; i < level; i++)
    {
      b.append("   ");
    }
    b.append(node.getClass().getName());
    b.append('[');
    b.append(Integer.toHexString(System.identityHashCode(node)));
    b.append(']');
    b.append("={x=");
    b.append(node.getX());
    b.append(", y=");
    b.append(node.getY());
    b.append(", width=");
    b.append(node.getWidth());
    b.append(", height=");
    b.append(node.getHeight());
    b.append('}');
    logger.debug(b.toString());


    b = new StringBuffer();
    for (int i = 0; i < level; i++)
    {
      b.append("   ");
    }
    b.append("- nodeLayoutProperties=");
    b.append(node.getNodeLayoutProperties());
    logger.debug(b.toString());
  }

  private static void printText(final RenderableText text, final int level)
  {
    StringBuffer b = new StringBuffer();
    for (int i = 0; i < level; i++)
    {
      b.append("   ");
    }
    b.append("Text");
    b.append('[');
    b.append(Integer.toHexString(System.identityHashCode(text)));
    b.append(']');
    b.append("={x=");
    b.append(text.getX());
    b.append(", y=");
    b.append(text.getY());
    b.append(", width=");
    b.append(text.getWidth());
    b.append(", height=");
    b.append(text.getHeight());
    b.append(", text='");
    b.append(text.getRawText());
    b.append("'}");
    logger.debug(b.toString());

    b = new StringBuffer();
    for (int i = 0; i < level; i++)
    {
      b.append("   ");
    }
    b.append("- nodeLayoutProperties=");
    b.append(text.getNodeLayoutProperties());
    logger.debug(b.toString());
  }

}
