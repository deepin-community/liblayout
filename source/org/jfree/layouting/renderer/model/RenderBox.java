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
 * $Id: RenderBox.java 6489 2008-11-28 14:53:40Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.renderer.model;

import org.jfree.layouting.input.style.keys.line.LineStyleKeys;
import org.jfree.layouting.input.style.keys.page.PageBreak;
import org.jfree.layouting.input.style.keys.page.PageStyleKeys;
import org.jfree.layouting.input.style.keys.text.TextStyleKeys;
import org.jfree.layouting.input.style.keys.text.WhitespaceCollapse;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.layouter.context.FontSpecification;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.layouter.context.PageContext;
import org.jfree.layouting.layouter.style.CSSValueResolverUtility;
import org.jfree.layouting.output.OutputProcessorMetaData;
import org.jfree.layouting.renderer.border.Border;
import org.jfree.layouting.renderer.text.ExtendedBaselineInfo;
import org.jfree.layouting.renderer.text.TextUtility;
import org.pentaho.reporting.libraries.fonts.registry.FontMetrics;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A render-box corresponds to elements in a DOM tree.
 * <p/>
 * Each box has a size, paddings, margins and borders. Boxes may have one or more childs.
 * <p/>
 * While all nodes may have a position or dimensions, boxes are special, as they can have borders, margins and paddings.
 * Borders, paddings  and margins can have percentages, the margins can additionally be 'auto'.
 * <p/>
 * The StrictInset variables for these properties contain the resolved values, while the box-definition contain the
 * unresolved values. The resolve values are not valid unless the object has been validated to least least 'LAYOUTING'.
 *
 * @author Thomas Morgner
 */
public abstract class RenderBox extends RenderNode
{
  private static final Log logger = LogFactory.getLog(RenderBox.class);
  public static final boolean LOG_PRUNE = false;

  private RenderNode firstChild;
  private RenderNode lastChild;

  private boolean open;
  private PageContext pageContext;
  private BoxLayoutProperties boxLayoutProperties;
  private StaticBoxLayoutProperties staticBoxLayoutProperties;

  private long contentAreaX1;
  private long contentAreaX2;
  private BoxDefinition boxDefinition;

  private int lineCount;
  private ExtendedBaselineInfo baselineInfo;
  private long widowsSize;
  private long orphansSize;

  public RenderBox(final BoxDefinition boxDefinition)
  {
    if (boxDefinition == null)
    {
      throw new NullPointerException("BoxDefinition must not be null");
    }
    this.boxDefinition = boxDefinition;
    this.open = true;
    this.staticBoxLayoutProperties = new StaticBoxLayoutProperties();
  }

  public StaticBoxLayoutProperties getStaticBoxLayoutProperties()
  {
    return staticBoxLayoutProperties;
  }

  public void appyStyle(final LayoutContext context, final OutputProcessorMetaData metaData)
  {
    super.appyStyle(context, metaData);

    staticBoxLayoutProperties.setPreserveSpace
        (WhitespaceCollapse.PRESERVE.equals
            (context.getValue(TextStyleKeys.WHITE_SPACE_COLLAPSE)));
    staticBoxLayoutProperties.setDominantBaseline
        (context.getValue(LineStyleKeys.DOMINANT_BASELINE));

    final FontSpecification fontSpecification = context.getFontSpecification();
    final FontMetrics fontMetrics = metaData.getFontMetrics(fontSpecification);
    staticBoxLayoutProperties.setNominalBaselineInfo
        (TextUtility.createBaselineInfo('x', fontMetrics));

    final CSSValue widowsValue = context.getValue(PageStyleKeys.WIDOWS);
    staticBoxLayoutProperties.setWidows(Math.max(1, (int)
        CSSValueResolverUtility.getNumericValue(widowsValue, 0)));

    final CSSValue orphansValue = context.getValue(PageStyleKeys.ORPHANS);
    staticBoxLayoutProperties.setOrphans(Math.max(1, (int)
        CSSValueResolverUtility.getNumericValue(orphansValue, 0)));

    final CSSValue pageBreak = context.getValue(PageStyleKeys.PAGE_BREAK_INSIDE);
    staticBoxLayoutProperties.setAvoidPagebreakInside
        (PageBreak.AVOID.equals(pageBreak));
  }

  public long getWidowsSize()
  {
    return widowsSize;
  }

  public void setWidowsSize(final long widowsSize)
  {
    this.widowsSize = widowsSize;
  }

  public long getOrphansSize()
  {
    return orphansSize;
  }

  public void setOrphansSize(final long orphansSize)
  {
    this.orphansSize = orphansSize;
  }

  public int getLineCount()
  {
    return lineCount;
  }

  public void setLineCount(final int lineCount)
  {
    this.lineCount = lineCount;
  }

  public int getWidows()
  {
    return staticBoxLayoutProperties.getWidows();
  }

  public int getOrphans()
  {
    return staticBoxLayoutProperties.getOrphans();
  }

  public boolean isAvoidPagebreakInside()
  {
    return staticBoxLayoutProperties.isAvoidPagebreakInside();
  }

  public ExtendedBaselineInfo getBaselineInfo()
  {
    return baselineInfo;
  }

  public void setBaselineInfo(final ExtendedBaselineInfo baselineInfo)
  {
    this.baselineInfo = baselineInfo;
  }

  public ExtendedBaselineInfo getNominalBaselineInfo()
  {
    return staticBoxLayoutProperties.getNominalBaselineInfo();
  }

  public CSSValue getDominantBaseline()
  {
    return staticBoxLayoutProperties.getDominantBaseline();
  }

  public boolean isPreserveSpace()
  {
    return staticBoxLayoutProperties.isPreserveSpace();
  }

  public BoxDefinition getBoxDefinition()
  {
    return boxDefinition;
  }

  public RenderNode getFirstChild()
  {
    return firstChild;
  }

  protected void setFirstChild(final RenderNode firstChild)
  {
    this.firstChild = firstChild;
  }

  public RenderNode getLastChild()
  {
    return lastChild;
  }

  protected void setLastChild(final RenderNode lastChild)
  {
    this.lastChild = lastChild;
  }

  public void addGeneratedChild(final RenderNode child)
  {
    if (child == null)
    {
      throw new NullPointerException
          ("Child to be added must not be null.");
    }

    if (isHibernated())
    {
      throw new IllegalStateException
          ("Check your state management, you've messed with an hibernated element.");
    }

    if (lastChild != null)
    {
      lastChild.setNext(child);
    }

    child.setPrev(lastChild);
    child.setNext(null);
    lastChild = child;

    if (firstChild == null)
    {
      firstChild = child;
    }

    child.setParent(this);

    if (isFrozen())
    {
      child.freeze();
    }
    child.updateChangeTracker();
  }

  public void addChild(final RenderNode child)
  {
    if (child == null)
    {
      throw new NullPointerException
          ("Child to be added must not be null.");
    }

    if (isOpen() == false)
    {
      throw new IllegalStateException
          ("Adding content to an already closed element.");
    }

    if (isHibernated())
    {
      throw new IllegalStateException
          ("Check your state management. You tried to modify a hibernated element.");
    }

    if (lastChild != null)
    {
      lastChild.setNext(child);
    }

    child.setPrev(lastChild);
    child.setNext(null);
    lastChild = child;

    if (firstChild == null)
    {
      firstChild = child;
    }

    child.setParent(this);
    if (isFrozen())
    {
      child.freeze();
    }

    child.updateChangeTracker();
  }

  /**
   * Inserts the given target after the specified node. If the node is null, the target is inserted as first node.
   *
   * @param node
   * @param target
   */
  protected void insertAfter(final RenderNode node, final RenderNode target)
  {
    if (node == null)
    {
      // ok, insert as new first element.
      final RenderNode firstChild = getFirstChild();
      if (firstChild == null)
      {
        setLastChild(target);
        setFirstChild(target);
        target.setParent(this);
        target.setPrev(null);
        target.setNext(null);
        target.updateChangeTracker();
        return;
      }

      // we have a first-child.
      firstChild.setPrev(target);
      setFirstChild(target);
      target.setParent(this);
      target.setPrev(null);
      target.setNext(firstChild);
      target.updateChangeTracker();
      return;
    }

    if (node.getParent() != this)
    {
      throw new IllegalStateException("You made a big boo");
    }

    final RenderNode next = node.getNext();
    node.setNext(target);
    target.setPrev(node);
    target.setParent(this);
    target.setNext(next);
    if (next != null)
    {
      next.setPrev(target);
    }
    else
    {
      setLastChild(target);
    }
    target.updateChangeTracker();
  }

  /**
   * Inserts the given target directly before the the specified node. If the node is null, the element is inserted at
   * the last position.
   *
   * @param node
   * @param target
   */
  protected void insertBefore(final RenderNode node, final RenderNode target)
  {
    if (node == null)
    {
      final RenderNode lastChild = getLastChild();
      if (lastChild == null)
      {
        target.setParent(this);
        target.setPrev(null);
        target.setNext(null);
        setFirstChild(target);
        setLastChild(target);
        target.updateChangeTracker();
        return;
      }

      setLastChild(target);
      target.setParent(this);
      target.setPrev(lastChild);
      target.setNext(null);
      lastChild.setNext(target);
      target.updateChangeTracker();
      return;
    }

    if (node.getParent() != this)
    {
      throw new IllegalStateException("You made a big boo");
    }

    final RenderNode prev = node.getPrev();
    node.setPrev(target);
    target.setNext(node);
    target.setParent(this);
    target.setPrev(prev);
    if (prev != null)
    {
      prev.setNext(target);
    }
    else
    {
      setFirstChild(target);
    }
    target.updateChangeTracker();
  }

  public void replaceChild(final RenderNode old, final RenderNode replacement)
  {
    if (old.getParent() != this)
    {
      throw new IllegalArgumentException("None of my childs.");
    }

    if (old == replacement)
    {
      // nothing to do ...
      return;
    }

    boolean changed = false;
    if (old == firstChild)
    {
      replacement.setNext(firstChild.getNext());
      replacement.setPrev(null);
      firstChild.setNext(null);
      firstChild.setPrev(null);
      firstChild = replacement;
      replacement.setParent(this);
      changed = true;
    }
    if (old == lastChild)
    {
      replacement.setPrev(lastChild.getPrev());
      replacement.setNext(null);
      lastChild.setNext(null);
      lastChild.setPrev(null);
      lastChild = replacement;
      replacement.setParent(this);
      changed = true;
    }

    if (changed)
    {
      old.updateChangeTracker();
      replacement.updateChangeTracker();
      return;
    }

    final RenderNode prev = old.getPrev();
    final RenderNode next = old.getNext();
    replacement.setPrev(prev);
    replacement.setNext(next);

    if (prev != null)
    {
      prev.setNext(replacement);
    }
    if (next != null)
    {
      next.setPrev(replacement);
    }

    replacement.setParent(this);

    old.updateChangeTracker();
    replacement.updateChangeTracker();
  }


  public void replaceChilds(final RenderNode old,
                            final RenderNode[] replacement)
  {
    if (old.getParent() != this)
    {
      throw new IllegalArgumentException("None of my childs.");
    }

    if (replacement.length == 0)
    {
      throw new IndexOutOfBoundsException("Array is empty ..");
    }

    if (old == replacement[0])
    {
      if (replacement.length == 1)
      {
        // nothing to do ...
        return;
      }
      throw new IllegalArgumentException
          ("Thou shall not use the replace method to insert new elements!");
    }

    // first, connect all replacements ...
    RenderNode first = null;
    RenderNode last = null;

    for (int i = 0; i < replacement.length; i++)
    {
      if (last == null)
      {
        last = replacement[i];
        if (last != null)
        {
          first = last;
          first.setParent(this);
        }
        continue;
      }


      final RenderNode node = replacement[i];

      last.setNext(node);
      node.setPrev(last);
      node.setParent(this);
      last = node;
    }

    if (first == null)
    {
      throw new IndexOutOfBoundsException("Array is empty (NullValues stripped)..");
    }

    // next, check if the first replacement .

    if (old == firstChild)
    {
      // inserting a replacement for the first child.
      final RenderNode second = firstChild.getNext();

      last.setNext(second);
      first.setPrev(null);

      if (second != null)
      {
        second.setPrev(last);
      }
      else
      {
        // No second element? So there was only one child.
        lastChild = last;
      }

      firstChild.setNext(null);
      firstChild.setPrev(null);
      firstChild = first;
      old.updateChangeTracker();
      for (int i = 0; i < replacement.length; i++)
      {
        final RenderNode renderNode = replacement[i];
        renderNode.updateChangeTracker();
      }
      return;
    }

    if (old == lastChild)
    {
      first.setPrev(lastChild.getPrev());
      last.setNext(null);
      lastChild.setNext(null);
      lastChild.setPrev(null);
      lastChild = last;
      for (int i = 0; i < replacement.length; i++)
      {
        final RenderNode renderNode = replacement[i];
        renderNode.updateChangeTracker();
      }
      return;
    }

    // Something inbetween ...
    final RenderNode prev = old.getPrev();
    final RenderNode next = old.getNext();
    first.setPrev(prev);
    last.setNext(next);

    if (prev != null)
    {
      prev.setNext(first);
    }
    if (next != null)
    {
      next.setPrev(last);
    }

    for (int i = 0; i < replacement.length; i++)
    {
      final RenderNode renderNode = replacement[i];
      renderNode.updateChangeTracker();
    }
  }

  /**
   * Clones this node. Be aware that cloning can get you into deep trouble, as the relations this node has may no longer
   * be valid.
   *
   * @return
   */
  public Object clone()
  {
    try
    {
      final RenderBox renderBox = (RenderBox) super.clone();
      if (boxLayoutProperties != null)
      {
        renderBox.boxLayoutProperties =
            (BoxLayoutProperties) boxLayoutProperties.clone();
      }
      return renderBox;
    }
    catch (CloneNotSupportedException e)
    {
      throw new IllegalStateException();
    }
  }

  /**
   * Derive creates a disconnected node that shares all the properties of the original node. The derived node will no
   * longer have any parent, silbling, child or any other relationships with other nodes.
   *
   * @return
   */
  public RenderNode derive(final boolean deepDerive)
  {
    final RenderBox box = (RenderBox) super.derive(deepDerive);

    if (deepDerive)
    {
      RenderNode node = firstChild;
      RenderNode currentNode = null;
      while (node != null)
      {
        final RenderNode previous = currentNode;

        currentNode = node.derive(true);
        currentNode.setParent(box);
        if (previous == null)
        {
          box.firstChild = currentNode;
          currentNode.setPrev(null);
        }
        else
        {
          previous.setNext(currentNode);
          currentNode.setPrev(previous);
        }
        node = node.getNext();
      }

      box.lastChild = currentNode;
      if (lastChild != null)
      {
        box.lastChild.setNext(null);
      }
    }
    else
    {
      box.firstChild = null;
      box.lastChild = null;
    }
    return box;
  }


  /**
   * Derive creates a disconnected node that shares all the properties of the original node. The derived node will no
   * longer have any parent, silbling, child or any other relationships with other nodes.
   *
   * @return
   */
  public RenderNode hibernate()
  {
    final RenderBox box = (RenderBox) super.hibernate();

    RenderNode node = firstChild;
    RenderNode currentNode = null;
    while (node != null)
    {
      final RenderNode previous = currentNode;

      currentNode = node.hibernate();
      currentNode.setParent(box);
      if (previous == null)
      {
        box.firstChild = currentNode;
        currentNode.setPrev(null);
      }
      else
      {
        previous.setNext(currentNode);
        currentNode.setPrev(previous);
      }
      node = node.getNext();
    }

    box.lastChild = currentNode;
    if (lastChild != null)
    {
      box.lastChild.setNext(null);
    }
    return box;
  }


  /**
   * Derive creates a disconnected node that shares all the properties of the original node. The derived node will no
   * longer have any parent, silbling, child or any other relationships with other nodes.
   *
   * @return
   */
  public RenderNode deriveFrozen(final boolean deepDerive)
  {
    final RenderBox box = (RenderBox) super.deriveFrozen(deepDerive);
    if (deepDerive)
    {
      RenderNode node = firstChild;
      RenderNode currentNode = null;
      while (node != null)
      {
        final RenderNode previous = currentNode;

        currentNode = node.deriveFrozen(true);
        currentNode.setParent(box);
        if (previous == null)
        {
          box.firstChild = currentNode;
          currentNode.setPrev(null);
        }
        else
        {
          previous.setNext(currentNode);
          currentNode.setPrev(previous);
        }
        node = node.getNext();
      }

      box.lastChild = currentNode;
      if (lastChild != null)
      {
        box.lastChild.setNext(null);
      }
    }
    else
    {
      box.firstChild = null;
      box.lastChild = null;
    }
    return box;
  }

  public void addChilds(final RenderNode[] nodes)
  {
    for (int i = 0; i < nodes.length; i++)
    {
      addChild(nodes[i]);
    }
  }

  public RenderNode findNodeById(final Object instanceId)
  {
    if (instanceId == getInstanceId())
    {
      return this;
    }

    RenderNode child = getLastChild();
    while (child != null)
    {
      final RenderNode nodeById = child.findNodeById(instanceId);
      if (nodeById != null)
      {
        return nodeById;
      }
      child = child.getPrev();
    }
    return null;
  }

  public boolean isAppendable()
  {
    return isOpen();
  }

  public RenderBox getInsertationPoint()
  {
    if (isAppendable() == false)
    {
      throw new IllegalStateException("Already closed");
    }

    final RenderNode lastChild = getLastChild();
    if (lastChild instanceof RenderBox)
    {
      final RenderBox lcBox = (RenderBox) lastChild;
      if (lcBox.isAppendable())
      {
        return lcBox.getInsertationPoint();
      }
    }
    return this;
  }

  public Border getBorder()
  {
    return boxDefinition.getBorder();
  }

  /**
   * Removes all children.
   */
  public void clear()
  {
    RenderNode child = getFirstChild();
    while (child != null)
    {
      final RenderNode nextChild = child.getNext();
      if (child != getFirstChild())
      {
        child.getPrev().setNext(null);
      }
      child.setPrev(null);
      child.setParent(null);
      child = nextChild;
    }
    setFirstChild(null);
    setLastChild(null);
    updateChangeTracker();
  }

  public RenderNode getVisibleFirst()
  {
    RenderNode firstChild = getFirstChild();
    while (firstChild != null)
    {
      if (firstChild.isIgnorableForRendering() == false)
      {
        return firstChild;
      }
      firstChild = firstChild.getNext();
    }
    return null;
  }

  public RenderNode getVisibleLast()
  {
    RenderNode lastChild = getLastChild();
    while (lastChild != null)
    {
      if (lastChild.isIgnorableForRendering() == false)
      {
        return lastChild;
      }
      lastChild = lastChild.getPrev();
    }
    return null;
  }

  private RenderNode getFirstNonEmpty()
  {
    RenderNode firstChild = getFirstChild();
    while (firstChild != null)
    {
      if (firstChild.isEmpty() == false)
      {
        return firstChild;
      }
      firstChild = firstChild.getNext();
    }
    return null;
  }

  public boolean isEmpty()
  {
    if (getBoxDefinition().isEmpty() == false)
    {
      return false;
    }

    final RenderNode node = getFirstNonEmpty();
    if (node != null)
    {
      return false;
    }
    // Ok, the childs were not able to tell us some truth ..
    // lets try something else.
    return true;
  }

  public boolean isDiscardable()
  {
    if (getBoxDefinition().isEmpty() == false)
    {
      return false;
    }

    RenderNode node = getFirstChild();
    while (node != null)
    {
      if (node.isDiscardable() == false)
      {
        return false;
      }
      node = node.getNext();
    }
    return true;
  }

  public void close()
  {
    if (isOpen() == false)
    {
      throw new IllegalStateException("Double close..");
    }

    if (isHibernated())
    {
      throw new IllegalStateException
          ("Check your state management. You tried to mess with an hibernated element.");
    }

    this.open = false;
    if (isDiscardable())
    {
      if (getParent() != null)
      {
        if (LOG_PRUNE)
        {
          logger.debug("Pruning: " + this);
        }
        getParent().remove(this);
      }
    }
    else
    {
      RenderNode lastChild = getLastChild();
      while (lastChild != null)
      {
        if (lastChild.isDiscardable())
        {
          if (LOG_PRUNE)
          {
            logger.debug("Pruning: " + lastChild);
          }
          remove(lastChild);
          lastChild = getLastChild();
        }
        else
        {
          break;
        }
      }
    }
  }

  public void remove(final RenderNode child)
  {
    final RenderBox parent = child.getParent();
    if (parent != this)
    {
      throw new IllegalArgumentException("None of my childs");
    }

    child.setParent(null);

    final RenderNode prev = child.getPrev();
    final RenderNode next = child.getNext();

    if (prev != null)
    {
      prev.setNext(next);
    }

    if (next != null)
    {
      next.setPrev(prev);
    }

    if (firstChild == child)
    {
      firstChild = next;
    }
    if (lastChild == child)
    {
      lastChild = prev;
    }
    child.updateChangeTracker();
    this.updateChangeTracker();
  }

  public boolean isOpen()
  {
    return open;
  }

//  public void setOpen(final boolean open)
//  {
//    this.open = open;
//  }

  public PageContext getPageContext()
  {
    if (pageContext != null)
    {
      return pageContext;
    }
    return super.getPageContext();
  }

  public void setPageContext(final PageContext pageContext)
  {
    this.pageContext = pageContext;
  }

  public void freeze()
  {
    if (isFrozen())
    {
      return;
    }

    super.freeze();
    RenderNode node = getFirstChild();
    while (node != null)
    {
      node.freeze();
      node = node.getNext();
    }
  }

  public BoxLayoutProperties getBoxLayoutProperties()
  {
    if (boxLayoutProperties == null)
    {
      this.boxLayoutProperties = new BoxLayoutProperties();
    }
    return boxLayoutProperties;
  }

  public long getContentAreaX1()
  {
    return contentAreaX1;
  }

  public void setContentAreaX1(final long contentAreaX1)
  {
    this.contentAreaX1 = contentAreaX1;
  }

  public long getContentAreaX2()
  {
    return contentAreaX2;
  }

  public void setContentAreaX2(final long contentAreaX2)
  {
    if (contentAreaX2 < 0)
    {
      throw new IllegalStateException("Failure here: " + contentAreaX2);
    }
    this.contentAreaX2 = contentAreaX2;
  }

  /**
   * Performs a simple split. This box will be altered to form the left/top side of the split, and a derived empty box
   * will be returned, which makes up the right/bottom side.
   * <p/>
   * A split will only happen on inline-boxes during the line-break-step. In the ordinary layouting, splitting is not
   * necesary.
   *
   * @param axis
   * @return
   */
  public RenderBox split(final int axis)
  {
    final RenderBox otherBox = (RenderBox) derive(false);
    final BoxDefinition[] boxDefinitions = boxDefinition.split(axis);
    boxDefinition = boxDefinitions[0];
    otherBox.boxDefinition = boxDefinitions[1];
    return otherBox;
  }

  public long getEffectiveMarginTop()
  {
    return getBoxLayoutProperties().getEffectiveMarginTop();
  }

  public long getEffectiveMarginBottom()
  {
    return getBoxLayoutProperties().getEffectiveMarginBottom();
  }
}
