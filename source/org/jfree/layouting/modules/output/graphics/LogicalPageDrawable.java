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
 * $Id: LogicalPageDrawable.java 6489 2008-11-28 14:53:40Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.layouting.modules.output.graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;

import org.jfree.layouting.input.style.keys.color.ColorStyleKeys;
import org.jfree.layouting.input.style.values.CSSColorValue;
import org.jfree.layouting.layouter.context.FontSpecification;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.output.pageable.BorderShapeFactory;
import org.jfree.layouting.renderer.model.ParagraphPoolBox;
import org.jfree.layouting.renderer.model.ParagraphRenderBox;
import org.jfree.layouting.renderer.model.RenderBox;
import org.jfree.layouting.renderer.model.RenderNode;
import org.jfree.layouting.renderer.model.RenderableReplacedContent;
import org.jfree.layouting.renderer.model.RenderableText;
import org.jfree.layouting.renderer.model.page.LogicalPageBox;
import org.jfree.layouting.renderer.model.table.TableCellRenderBox;
import org.jfree.layouting.renderer.model.table.TableRenderBox;
import org.jfree.layouting.renderer.model.table.TableRowRenderBox;
import org.jfree.layouting.renderer.model.table.TableSectionRenderBox;
import org.jfree.layouting.renderer.text.Glyph;
import org.jfree.layouting.util.geom.StrictGeomUtility;
import org.pentaho.reporting.libraries.resourceloader.factory.drawable.DrawableWrapper;

/**
 * The page drawable is the content provider for the Graphics2DOutputTarget.
 * This component is responsible for rendering the current page to a Graphics2D
 * object.
 *
 * @author Thomas Morgner
 */
public class LogicalPageDrawable implements PageDrawable
{
  private static final boolean OUTLINE_MODE = false;
  private LogicalPageBox rootBox;
  private PageFormat pageFormat;
  private double width;
  private double height;

  public LogicalPageDrawable(final LogicalPageBox rootBox)
  {
    this.rootBox = rootBox;
    this.width = rootBox.getPageWidth() / 1000f;
    this.height = rootBox.getPageHeight() / 1000f;

    final Paper paper = new Paper();
    paper.setImageableArea(0,0, width, height);

    this.pageFormat = new PageFormat();
    this.pageFormat.setPaper(paper);

    // print();
  }

  public PageFormat getPageFormat()
  {
    return (PageFormat) pageFormat.clone();
  }

  /**
   * Returns the preferred size of the drawable. If the drawable is aspect ratio
   * aware, these bounds should be used to compute the preferred aspect ratio
   * for this drawable.
   *
   * @return the preferred size.
   */
  public Dimension getPreferredSize()
  {
    return new Dimension((int) width, (int) height);
  }

  /**
   * Returns true, if this drawable will preserve an aspect ratio during the
   * drawing.
   *
   * @return true, if an aspect ratio is preserved, false otherwise.
   */
  public boolean isPreserveAspectRatio()
  {
    return true;
  }

  /**
   * Draws the object.
   *
   * @param g2   the graphics device.
   * @param area the area inside which the object should be drawn.
   */
  public void draw(final Graphics2D g2, final Rectangle2D area)
  {
    g2.setPaint(Color.white);
    g2.fill(area);

//    final Rectangle2D.Double headerBounds = new Rectangle2D.Double
//            (rootBox.getX() / 1000f, rootBox.getY() / 1000f,
//                    rootBox.getWidth() / 1000f, rootBox.getHeight() / 1000f);
//    g2.setPaint(Color.black);
//    g2.draw(headerBounds);
    drawBox(g2, rootBox.getHeaderArea(), 0);
    drawBox(g2, rootBox, 0);
    drawBox(g2, rootBox.getFooterArea(), 0);

//    drawDebugBox(g2, rootBox.getHeaderArea());
//    drawDebugBox(g2, rootBox.getFooterArea());
  }

  private void drawDebugBox (final Graphics2D g2, final RenderBox box)
  {
    if (box instanceof TableCellRenderBox)
    {
      g2.setPaint(Color.yellow);
    }
    else if (box instanceof TableRowRenderBox)
    {
      g2.setPaint(Color.green);
    }
    else if (box instanceof TableSectionRenderBox)
    {
      g2.setPaint(Color.red);
    }
    else if (box instanceof TableRenderBox)
    {
      g2.setPaint(Color.blue);
    }
    else if (box instanceof ParagraphRenderBox)
    {
      g2.setPaint(Color.magenta);
    }
    else if (box instanceof ParagraphPoolBox)
    {
      g2.setPaint(Color.orange);
    }
    else
    {
      g2.setPaint(Color.lightGray);
    }
    final int x = (int) (box.getX() / 1000);
    final int y = (int) (box.getY() / 1000);
    final int w = (int) (box.getWidth() / 1000);
    final int h = (int) (box.getHeight() / 1000);
    g2.drawRect(x, y, w, h);
  }

  public void drawBox(final Graphics2D g2, final RenderBox box, final int level)
  {
    if (OUTLINE_MODE)
    {
      drawDebugBox(g2, box);
    }
    else
    {
      final BorderShapeFactory borderShapeFactory = new BorderShapeFactory(box);
      borderShapeFactory.generateBorder(g2);
    }


    RenderNode childs = box.getFirstChild();
    while (childs != null)
    {
      if (childs instanceof RenderBox)
      {
        drawBox(g2, (RenderBox) childs, level + 1);
      }
      else if (childs instanceof RenderableText)
      {
        drawText(g2, (RenderableText) childs);
      }
      else if (childs instanceof RenderableReplacedContent)
      {
        drawReplacedContent(g2, (RenderableReplacedContent) childs);
      }
      childs = childs.getNext();
    }
  }

  private void drawReplacedContent(final Graphics2D g2,
                                   final RenderableReplacedContent content)
  {
    final Object o = content.getRawObject();
    if (o instanceof Image)
    {
      final int x = (int) StrictGeomUtility.toExternalValue(content.getX());
      final int y = (int) StrictGeomUtility.toExternalValue(content.getY());
      final int width = (int) StrictGeomUtility.toExternalValue(content.getWidth());
      final int height = (int) StrictGeomUtility.toExternalValue(content.getHeight());
      g2.drawImage((Image) o, x, y, width, height, null);
    }
    else if (o instanceof DrawableWrapper)
    {
      final double x = (int) StrictGeomUtility.toExternalValue(content.getX());
      final double y = (int) StrictGeomUtility.toExternalValue(content.getY());
      final double width = (int) StrictGeomUtility.toExternalValue(content.getWidth());
      final double height = (int) StrictGeomUtility.toExternalValue(content.getHeight());
      final DrawableWrapper d = (DrawableWrapper) o;
      final Graphics2D g2Clone = (Graphics2D) g2.create();
      d.draw(g2Clone, new Rectangle2D.Double(x,y,width, height));
      g2Clone.dispose();
    }
  }

  private void drawText(final Graphics2D g2,
                        final RenderableText renderableText)
  {
    final Glyph[] gs = renderableText.getGlyphs();
    final long posX = renderableText.getX();
    final long posY = renderableText.getY();
    long runningPos = posX;

    final LayoutContext layoutContext = renderableText.getLayoutContext();
    final FontSpecification fontSpecification =
            layoutContext.getFontSpecification();

    int style = Font.PLAIN;
    if (fontSpecification.getFontWeight() > 400)
    {
      style |= Font.BOLD;
    }
    if (fontSpecification.isItalic() || fontSpecification.isOblique())
    {
      style |= Font.ITALIC;
    }

    final CSSColorValue cssColor = (CSSColorValue)
            layoutContext.getValue(ColorStyleKeys.COLOR);

    g2.setColor(cssColor);
    g2.setFont(new Font(fontSpecification.getFontFamily(), style, (int) fontSpecification.getFontSize()));

    final int length = renderableText.getOffset() + renderableText.getLength();
    for (int i = renderableText.getOffset(); i < length; i++)
    {
      final Glyph g = gs[i];
      g2.drawString(LogicalPageDrawable.glpyhToString(g), runningPos / 1000f, (posY + g.getBaseLine()) / 1000f);
      runningPos += g.getWidth();
    }
  }

  public static String glpyhToString(final Glyph g)
  {
    final StringBuffer b = new StringBuffer();
    b.append((char) (0xffff & g.getCodepoint()));
    final int[] extraCPs = g.getExtraChars();
    for (int i = 0; i < extraCPs.length; i++)
    {
      b.append(", ");
      final int extraCP = extraCPs[i];
      b.append(extraCP);
    }
    return b.toString();
  }


}
