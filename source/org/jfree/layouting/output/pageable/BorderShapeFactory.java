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
 * $Id: BorderShapeFactory.java 2755 2007-04-10 19:27:09Z taqua $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.output.pageable;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import org.jfree.layouting.input.style.keys.border.BorderStyle;
import org.jfree.layouting.input.style.values.CSSColorValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.renderer.border.Border;
import org.jfree.layouting.renderer.border.BorderCorner;
import org.jfree.layouting.renderer.border.BorderEdge;
import org.jfree.layouting.renderer.model.ComputedLayoutProperties;
import org.jfree.layouting.renderer.model.RenderBox;
import org.jfree.layouting.util.geom.StrictGeomUtility;
import org.jfree.layouting.util.geom.StrictInsets;

/**
 * This class needs attention. Maybe it is wise to split it into several
 * border implementations to reduce the complexity of the whole process.
 *
 * @author Thomas Morgner
 */
public class BorderShapeFactory
{
  private static final int CORNER_RIGHT_TOP = 0;
  private static final int CORNER_TOP_RIGHT = 1;
  private static final int CORNER_TOP_LEFT = 2;
  private static final int CORNER_LEFT_TOP = 3;
  private static final int CORNER_LEFT_BOTTOM = 4;
  private static final int CORNER_BOTTOM_LEFT = 5;
  private static final int CORNER_BOTTOM_RIGHT = 6;
  private static final int CORNER_RIGHT_BOTTOM = 7;

  private static final byte[][] CORNER_FACTORS = new byte[][]{
      {+1, +1}, // RIGHT_TOP
      {+1, +1}, // TOP_RIGHT
      {-1, +1}, // TOP_LEFT
      {-1, +1}, // LEFT_TOP
      {+1, -1}, // LEFT_BOTTOM
      {+1, -1}, // BOTTOM_LEFT
      {-1, -1}, // BOTTOM_RIGHT
      {-1, -1}, // RIGHT_BOTTOM
  };


  public static class BorderDrawOperation
  {
    private Color color;
    private BasicStroke stroke;
    private Shape shape;

    public BorderDrawOperation(final Shape shape,
                               final Color color,
                               final BasicStroke stroke)
    {
      this.shape = shape;
      this.color = color;
      this.stroke = stroke;
    }

    public void draw(final Graphics2D g2)
    {
      if (shape == null)
      {
        return;
      }
      if (stroke.getLineWidth() == 0)
      {
        return;
      }
      if (color.getAlpha() == 0)
      {
        return;
      }

      g2.setStroke(stroke);
      g2.setColor(color);
      g2.draw(shape);
    }

    public void fill(final Graphics2D g2)
    {
      if (shape == null)
      {
        return;
      }
      if (stroke.getLineWidth() == 0)
      {
        return;
      }
      if (color.getAlpha() == 0)
      {
        return;
      }

      g2.setStroke(stroke);
      g2.setColor(color);
      g2.fill(shape);
    }
  }

  private Color color;
  private BasicStroke stroke;
  private double height;
  private double width;
  private double x;
  private double y;
  private Border border;
  private StrictInsets borderSizes;
  private ArrayList drawOps;
  private ArrayList fillOps;
  private CSSColorValue backgroundColor;

  public BorderShapeFactory(final RenderBox box)
  {
    drawOps = new ArrayList();
    fillOps = new ArrayList();

    border = box.getBorder();
    final ComputedLayoutProperties layoutProperties = box.getComputedLayoutProperties();
    final StrictInsets bWidths = new StrictInsets
        (layoutProperties.getBorderTop(), layoutProperties.getBorderLeft(),
            layoutProperties.getBorderBottom(), layoutProperties.getBorderRight());

    x = StrictGeomUtility.toExternalValue
        (box.getX() + (bWidths.getLeft() / 2));
    y = StrictGeomUtility.toExternalValue
        (box.getY() + (bWidths.getTop() / 2));
    width = StrictGeomUtility.toExternalValue
        (box.getWidth() - (bWidths.getLeft() + bWidths.getRight()) / 2);
    height = StrictGeomUtility.toExternalValue
        (box.getHeight() - (bWidths.getTop() + bWidths.getBottom()) / 2);
    borderSizes = bWidths;

    // todo: Change this to the real background ..
    backgroundColor = box.getBoxDefinition().getBackgroundColor();
  }

  private Arc2D generateCorner(final int type,
                               final double cornerX,
                               final double cornerY,
                               final BorderCorner corner,
                               final boolean fillShape)
  {
    if (corner.getHeight().getValue() == 0 || corner.getWidth().getValue() == 0)
    {
      return null;
    }
    final byte[] cornerFactors = CORNER_FACTORS[type];

    final double widthTopLeft =
        StrictGeomUtility.toExternalValue(corner.getWidth().getValue());
    final double heightTopLeft =
        StrictGeomUtility.toExternalValue(corner.getHeight().getValue());

    final int open;
    if (fillShape)
    {
      open = Arc2D.PIE;
    }
    else
    {
      open = Arc2D.OPEN;
    }

    return new Arc2D.Double
        (cornerX + widthTopLeft * cornerFactors[0],
            cornerY + heightTopLeft * cornerFactors[1],
            widthTopLeft, heightTopLeft,
            Math.PI * type / 4.0, Math.PI / 4.0, open);
  }

  private BasicStroke createStroke(final BorderEdge edge, final long internalWidth)
  {
    final float effectiveWidth = (float) StrictGeomUtility.toExternalValue(internalWidth);
    // todo: depending on the stroke type, we need other strokes instead.


    if (BorderStyle.DASHED.equals(edge.getBorderStyle()))
    {
      return new BasicStroke(effectiveWidth, BasicStroke.CAP_SQUARE,
          BasicStroke.JOIN_MITER,
          10.0f, new float[]
          {6 * effectiveWidth, 6 * effectiveWidth}, 0.0f);
    }
    if (BorderStyle.DOTTED.equals(edge.getBorderStyle()))
    {
      return new BasicStroke(effectiveWidth, BasicStroke.CAP_SQUARE,
          BasicStroke.JOIN_MITER,
          5.0f, new float[]{0.0f, 2 * effectiveWidth}, 0.0f);
    }
    if (BorderStyle.DOT_DASH.equals(edge.getBorderStyle()))
    {
      return new BasicStroke(effectiveWidth, BasicStroke.CAP_SQUARE,
          BasicStroke.JOIN_MITER,
          10.0f, new float[]
          {0, 2 * effectiveWidth, 6 * effectiveWidth, 2 * effectiveWidth}, 0.0f);
    }
    if (BorderStyle.DOT_DOT_DASH.equals(edge.getBorderStyle()))
    {
      return new BasicStroke(effectiveWidth, BasicStroke.CAP_SQUARE,
          BasicStroke.JOIN_MITER,
          10.0f, new float[]{0, 2 * effectiveWidth,
          0, 2 * effectiveWidth,
          6 * effectiveWidth, 2 * effectiveWidth}, 0.0f);
    }
    return new BasicStroke(effectiveWidth);
  }

  public void generateBorder(final Graphics2D g2)
  {
    generateTopEdge();
    generateLeftEdge();
    generateBottomEdge();
    generateRightEdge();

    for (int i = 0; i < drawOps.size(); i++)
    {
      final BorderDrawOperation operation = (BorderDrawOperation) drawOps.get(i);
      operation.draw(g2);
    }

    for (int i = 0; i < fillOps.size(); i++)
    {
      final BorderDrawOperation operation = (BorderDrawOperation) fillOps.get(i);
      operation.fill(g2);
    }

    if (backgroundColor == null || backgroundColor.getAlpha() == 0)
    {
      return;
    }

    // now we need some geometry stuff (yeah, I'm lazy!)
    final Area globalArea = new Area(new Rectangle2D.Double(x, y, width, height));

    // for each corner:
    //
    // create the inverse area and substract that from
    // the global area.
    for (int i = 0; i < fillOps.size(); i++)
    {
      final BorderDrawOperation operation = (BorderDrawOperation) fillOps.get(i);
      final Shape shape = operation.shape;
      if (shape == null)
      {
        continue;
      }

      final Area cornerArea = new Area(shape.getBounds2D());
      cornerArea.subtract(new Area(shape));
      globalArea.subtract(cornerArea);
    }
    // oh, yeah, I know, there are faster ways than that.

    //Log.debug ("Drawing " + globalArea.getBounds() + " with " + backgroundColor);
    g2.setColor(backgroundColor);
    g2.fill(globalArea);
  }

  private void draw(final Shape s)
  {
    if (s == null)
    {
      return;
    }
    drawOps.add(new BorderDrawOperation(s, color, stroke));
  }

  private void fill(final Shape s)
  {
    if (s == null)
    {
      return;
    }
    fillOps.add(new BorderDrawOperation(s, color, stroke));
  }

  private void generateRightEdge()
  {
    final BorderEdge rightEdge = border.getRight();
    color = rightEdge.getColor();
    stroke = createStroke(rightEdge, borderSizes.getRight());

    final BorderCorner firstCorner = border.getBottomRight();
    final BorderCorner secondCorner = border.getTopRight();

    draw(generateCorner(CORNER_RIGHT_BOTTOM, x + width, y + height, firstCorner, false));
    draw(generateCorner(CORNER_RIGHT_TOP, x + width, y, secondCorner, false));
    fill(generateCorner(CORNER_RIGHT_BOTTOM, x + width, y + height, firstCorner, true));
    fill(generateCorner(CORNER_RIGHT_TOP, x + width, y, secondCorner, true));

    draw(new Line2D.Double
        (x + width - firstCorner.getWidth().getValue(),
            y + height - firstCorner.getHeight().getValue(),
            x + width - secondCorner.getWidth().getValue(),
            y + secondCorner.getHeight().getValue()));

  }

  private void generateBottomEdge()
  {
    final BorderEdge bottomEdge = border.getBottom();
    final BorderCorner firstCorner = border.getBottomLeft();
    final BorderCorner secondCorner = border.getBottomRight();

    color = bottomEdge.getColor();
    stroke = createStroke(bottomEdge, borderSizes.getBottom());

    draw(generateCorner(CORNER_BOTTOM_LEFT, x, y + height, firstCorner, false));
    draw(generateCorner(CORNER_BOTTOM_RIGHT, x + width, y + height, secondCorner, false));
    fill(generateCorner(CORNER_BOTTOM_LEFT, x, y + height, firstCorner, true));
    fill(generateCorner(CORNER_BOTTOM_RIGHT, x + width, y + height, secondCorner, true));

    draw(new Line2D.Double
        (x + firstCorner.getWidth().getValue(),
            y + height - firstCorner.getHeight().getValue(),
            x + width - secondCorner.getWidth().getValue(),
            y + height - secondCorner.getHeight().getValue()));

  }

  private void generateLeftEdge()
  {
    final BorderEdge leftEdge = border.getLeft();

    final BorderCorner firstCorner = border.getTopLeft();
    final BorderCorner secondCorner = border.getBottomLeft();

    stroke = createStroke(leftEdge, borderSizes.getLeft());
    color = leftEdge.getColor();

    draw(generateCorner
        (CORNER_LEFT_TOP, x, y, firstCorner, false));
    draw(generateCorner
        (CORNER_LEFT_BOTTOM, x, y + height, secondCorner, false));
    fill(generateCorner
        (CORNER_LEFT_TOP, x, y, firstCorner, true));
    fill(generateCorner
        (CORNER_LEFT_BOTTOM, x, y + height, secondCorner, true));

    final double firstWidth =
        StrictGeomUtility.toInternalValue(firstCorner.getWidth().getValue());
    final double firstHeight =
        StrictGeomUtility.toInternalValue(firstCorner.getHeight().getValue());
    final double secondWidth =
        StrictGeomUtility.toInternalValue(secondCorner.getWidth().getValue());
    final double secondHeight =
        StrictGeomUtility.toInternalValue(secondCorner.getHeight().getValue());
    draw(new Line2D.Double
        (x + firstWidth,
            y + firstHeight,
            x + secondWidth,
            y + height - secondHeight));
  }

  private boolean isSimpleStyle (final CSSValue value)
  {
    if (BorderStyle.GROOVE.equals(value))
    {
      return false;
    }
    else if (BorderStyle.RIDGE.equals(value))
    {
      return false;
    }
    return true;
  }

  private void generateTopEdge()
  {
    final BorderEdge topEdge = border.getTop();
    stroke = createStroke(topEdge, borderSizes.getTop());
    color = topEdge.getColor();

    final BorderCorner firstCorner = border.getTopLeft();
    final BorderCorner secondCorner = border.getBottomLeft();

    draw(generateCorner
        (CORNER_TOP_RIGHT, x + width, y, border.getTopRight(), false));
    draw(generateCorner
        (CORNER_TOP_LEFT, x, y, border.getTopLeft(), false));

    final double firstWidth =
        StrictGeomUtility.toInternalValue(firstCorner.getWidth().getValue());
    final double firstHeight =
        StrictGeomUtility.toInternalValue(firstCorner.getHeight().getValue());
    final double secondWidth =
        StrictGeomUtility.toInternalValue(secondCorner.getWidth().getValue());
    final double secondHeight =
        StrictGeomUtility.toInternalValue(secondCorner.getHeight().getValue());
    draw(new Line2D.Double
        (x + firstWidth, y + firstHeight,
            x + width - secondWidth, y + secondHeight));

    fill(generateCorner
        (CORNER_TOP_RIGHT, x + width, y, border.getTopRight(), true));
    fill(generateCorner
        (CORNER_TOP_LEFT, x, y, border.getTopLeft(), true));
  }


}
