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
 * $Id: ParagraphRenderBox.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.renderer.model;

import org.jfree.layouting.input.style.keys.text.TextAlign;
import org.jfree.layouting.input.style.keys.text.TextStyleKeys;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.output.OutputProcessorMetaData;

/**
 * This articifial renderbox is the connection between block-contexts and the
 * sequences of consectual inline-boxes of that block.
 * <p/>
 * This renderbox generates lineboxes whenever needed.
 * <p/>
 * When asked for layout-sizes or when doing the layouting, it first checks its
 * validity and performs splits on all forced pagebreaks. At the end, there is
 * no inner element's edge with an activated clear-property.
 * <p/>
 * After that, it generates temporary lineboxes for all of its childs. When
 * *re*-computing the layout, these lineboxes get invalidated and merged back
 * into the paragraph.
 *
 * @author Thomas Morgner
 */
public class ParagraphRenderBox extends BlockRenderBox
{
  private static class LineBoxRenderBox extends BlockRenderBox
  {
    private LineBoxRenderBox(final BoxDefinition boxDefinition)
    {
      super(boxDefinition);
    }
  }

  private ParagraphPoolBox pool;
  private LineBoxRenderBox lineboxContainer;
  private CSSValue textAlignment;
  private CSSValue lastLineAlignment;
  private long lineBoxAge;
  private long minorLayoutAge;
  private long majorLayoutAge;

  public ParagraphRenderBox(final BoxDefinition boxDefinition)
  {
    super(boxDefinition);

    pool = new ParagraphPoolBox(EmptyBoxDefinition.getInstance());
    pool.setParent(this);

    // yet another helper box. Level 2
    lineboxContainer = new LineBoxRenderBox(EmptyBoxDefinition.getInstance());
    lineboxContainer.setParent(this);
    // level 3 means: Add all lineboxes to the paragraph
    // This gets auto-generated ..
  }

  public void appyStyle(final LayoutContext context, final OutputProcessorMetaData metaData)
  {
    super.appyStyle(context, metaData);
    final CSSValue alignVal = context.getValue(TextStyleKeys.TEXT_ALIGN);
    final CSSValue alignLastVal = context.getValue(TextStyleKeys.TEXT_ALIGN_LAST);
    this.textAlignment = createAlignment(alignVal);
    if (textAlignment == TextAlign.JUSTIFY)
    {
      this.lastLineAlignment = createAlignment(alignLastVal);
    }
    else
    {
      this.lastLineAlignment = textAlignment;
    }

    pool.appyStyle(context, metaData);
    lineboxContainer.appyStyle(context, metaData);
  }

  /**
   * Derive creates a disconnected node that shares all the properties of the
   * original node. The derived node will no longer have any parent, silbling,
   * child or any other relationships with other nodes.
   *
   * @return
   */
  public RenderNode derive(final boolean deepDerive)
  {
    final ParagraphRenderBox box = (ParagraphRenderBox) super.derive(deepDerive);
    box.pool = (ParagraphPoolBox) pool.derive(deepDerive);
    box.pool.setParent(box);

    box.lineboxContainer = (LineBoxRenderBox) lineboxContainer.derive(deepDerive);
    box.lineboxContainer.setParent(box);
    if (!deepDerive)
    {
      box.lineBoxAge = 0;
    }
    return box;
  }

  /**
   * Derive creates a disconnected node that shares all the properties of the
   * original node. The derived node will no longer have any parent, silbling,
   * child or any other relationships with other nodes.
   *
   * @return
   */
  public RenderNode hibernate()
  {
    final ParagraphRenderBox box = (ParagraphRenderBox) super.derive(false);
    box.setHibernated(true);
    box.pool = (ParagraphPoolBox) pool.hibernate();
    box.pool.setParent(box);
    box.lineboxContainer = (LineBoxRenderBox) lineboxContainer.hibernate();
    box.lineBoxAge = 0;
    return box;
  }

  private CSSValue createAlignment(final CSSValue value)
  {
    if (TextAlign.LEFT.equals(value) ||
        TextAlign.START.equals(value))
    {
      return TextAlign.LEFT;
    }
    if (TextAlign.RIGHT.equals(value) ||
        TextAlign.END.equals(value))
    {
      return TextAlign.RIGHT;
    }
    if (TextAlign.CENTER.equals(value))
    {
      return TextAlign.CENTER;
    }
    if (TextAlign.JUSTIFY.equals(value))
    {
      return TextAlign.JUSTIFY;
    }
    return TextAlign.LEFT;
  }


  public final void addChild(final RenderNode child)
  {
    pool.addChild(child);
  }

  protected void addDirectly(final RenderNode child)
  {
    if (child instanceof ParagraphPoolBox)
    {
      final ParagraphPoolBox poolBox = (ParagraphPoolBox) child;
      poolBox.trim();
    }
    super.addGeneratedChild(child);
  }

  /**
   * Removes all children.
   */
  public final void clear()
  {
    pool.clear();
    lineboxContainer.clear();
    super.clear();
    lineBoxAge = 0;
  }

  public final void clearLayout()
  {
    super.clear();
    lineBoxAge = 0;
  }

  public RenderBox getInsertationPoint()
  {
    return pool.getInsertationPoint();
  }

  public boolean isAppendable()
  {
    return pool.isAppendable();
  }

  public RenderNode findNodeById(final Object instanceId)
  {
    return super.findNodeById(instanceId);
  }

  public boolean isEmpty()
  {
    return pool.isEmpty();
  }

  public boolean isDiscardable()
  {
    return pool.isDiscardable();
  }

  public CSSValue getLastLineAlignment()
  {
    return lastLineAlignment;
  }

  public CSSValue getTextAlignment()
  {
    return textAlignment;
  }

  public BlockRenderBox getLineboxContainer()
  {
    return lineboxContainer;
  }

  public InlineRenderBox getPool()
  {
    return pool;
  }

  public long getLineBoxAge()
  {
    return lineBoxAge;
  }

  public void setLineBoxAge(final long lineBoxAge)
  {
    this.lineBoxAge = lineBoxAge;
  }

  public long getMinorLayoutAge()
  {
    return minorLayoutAge;
  }

  public void setMinorLayoutAge(final long minorLayoutAge)
  {
    this.minorLayoutAge = minorLayoutAge;
  }

  public long getMajorLayoutAge()
  {
    return majorLayoutAge;
  }

  public void setMajorLayoutAge(final long majorLayoutAge)
  {
    this.majorLayoutAge = majorLayoutAge;
  }

  /**
   * The public-id for the paragraph is the pool-box.
   *
   * @return
   */
  public Object getInstanceId()
  {
    return pool.getInstanceId();
  }
}
