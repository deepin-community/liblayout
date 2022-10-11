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
 * $Id: LogicalPageBox.java 2755 2007-04-10 19:27:09Z taqua $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.renderer.model.page;

import java.util.Arrays;

import org.jfree.layouting.renderer.model.BlockRenderBox;
import org.jfree.layouting.renderer.model.EmptyBoxDefinition;
import org.jfree.layouting.renderer.model.NormalFlowRenderBox;
import org.jfree.layouting.renderer.model.PageAreaRenderBox;
import org.jfree.layouting.renderer.model.RenderBox;
import org.jfree.layouting.renderer.model.RenderNode;
import org.jfree.layouting.renderer.model.NodeLayoutProperties;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.output.OutputProcessorMetaData;
import org.jfree.layouting.namespace.Namespaces;

/**
 * The logical page box does not have a layout at all. It has collection of
 * flows, one for each <b>logical</b> page area.
 * <p/>
 * Each logical page has a header (for the repeating group headers, not to be
 * mistaken as PageHeader!), content and footer area. The footer area contains
 * the foot-notes at the very bottom, the repeatable footer area and finally the
 * flow:bottom area.
 * <p/>
 * The flow- and repeatable areas behave like stacks, and are filled from the
 * content area (so for headers, new content goes after any previous content,
 * and for footers, new content goes before any previous content.)
 * <p/>
 * The footNotes section is always filled in the normal-order, so new content
 * gets added at the bottom of that area.
 * <p/>
 * The logical page also holds the absolutely and static positioned elements.
 * These elements may overlap the repeating headers, but will never overlap the
 * physical page header or footer.
 * <p/>
 * The logical page is also the container for all physical pages. (The sizes of
 * the physical pages influence the available space on the logical pages.)
 * <p/>
 * Layout notes: The logical page, as it is implemented now, consists of three
 * areas. The header and footer areas are plain banded areas and correspond to
 * the @top and @bottom page-areas. These areas are layouted zero-based. The
 * header's y=0 corresponds to the beginning of the page, while the footer's y=0
 * depends on the total height of the footer and must fullfill the following
 * constraint: Page_bottom_edge = (footer_y + footer_height).
 * <p/>
 * The content window is positioned relative to the content-flow and is placed
 * between the header and the footer. The mapping of the content into the header
 * is defined by the pageOffset. The content-window's y=0 corresponds to the
 * normal-flow's y=pageOffset.
 * <p/>
 * Repeatable headers and footers are *not* inserted into the content-flow.
 *
 * @author Thomas Morgner
 */
public class LogicalPageBox extends BlockRenderBox
{
  //private ArrayList subFlows;
  private PageGrid pageGrid;

  private long[] pageWidths;
  private long[] pageHeights;
  private long[] horizontalBreaks;
  private long[] verticalBreaks;
  private long pageWidth;
  private long pageHeight;

  //private long offset;
  private Object contentAreaId;
  private PageAreaRenderBox footerArea;
  private PageAreaRenderBox headerArea;

  private long pageOffset;
  private boolean normalFlowActive;

  public LogicalPageBox(final PageGrid pageGrid)
  {
    super(EmptyBoxDefinition.getInstance());

    if (pageGrid == null)
    {
      throw new NullPointerException("PageGrid must not be null");
    }

    //this.subFlows = new ArrayList();
    final NormalFlowRenderBox contentArea =
        new NormalFlowRenderBox(EmptyBoxDefinition.getInstance());
    this.contentAreaId = contentArea.getInstanceId();
    this.headerArea = new PageAreaRenderBox(EmptyBoxDefinition.getInstance());
    this.headerArea.setParent(this);
    this.footerArea = new PageAreaRenderBox(EmptyBoxDefinition.getInstance());
    this.footerArea.setParent(this);

    updatePageArea(pageGrid);

    addChild(contentArea);

    setMajorAxis(VERTICAL_AXIS);
    setMinorAxis(HORIZONTAL_AXIS);

    final NodeLayoutProperties nodeLayoutProperties = getNodeLayoutProperties();
    nodeLayoutProperties.setNamespace(Namespaces.LIBLAYOUT_NAMESPACE);
    nodeLayoutProperties.setTagName("logical-page");
  }

  public void appyStyle(final LayoutContext context, final OutputProcessorMetaData metaData)
  {
    super.appyStyle(context, metaData);
    final NodeLayoutProperties nodeLayoutProperties = getNodeLayoutProperties();
    nodeLayoutProperties.setNamespace(Namespaces.LIBLAYOUT_NAMESPACE);
    nodeLayoutProperties.setTagName("logical-page");
  }

  public void updatePageArea(final PageGrid pageGrid)
  {
    if (pageGrid == null)
    {
      throw new NullPointerException();
    }

    this.pageGrid = pageGrid;
    this.pageHeights = new long[pageGrid.getColumnCount()];
    this.pageWidths = new long[pageGrid.getRowCount()];
    this.horizontalBreaks = new long[pageGrid.getColumnCount()];
    this.verticalBreaks = new long[pageGrid.getRowCount()];

    Arrays.fill(pageHeights, Long.MAX_VALUE);
    Arrays.fill(pageWidths, Long.MAX_VALUE);

    // todo: This is invalid right now. The page grids content area must be used

    for (int row = 0; row < pageGrid.getRowCount(); row++)
    {
      for (int col = 0; col < pageGrid.getColumnCount(); col++)
      {
        final PhysicalPageBox box = pageGrid.getPage(row, col);
        pageHeights[row] = Math.min(pageHeights[row], box.getImageableHeight());
        pageWidths[col] = Math.min(pageWidths[col], box.getImageableWidth());
      }
    }

    pageHeight = 0;
    for (int i = 0; i < pageHeights.length; i++)
    {
      pageHeight += pageHeights[i];
      verticalBreaks[i] = pageHeight;
    }

    pageWidth = 0;
    for (int i = 0; i < pageWidths.length; i++)
    {
      pageWidth += pageWidths[i];
      horizontalBreaks[i] = pageWidth;
    }
  }

  public NormalFlowRenderBox getContentArea()
  {
    return (NormalFlowRenderBox) findNodeById(contentAreaId);
  }

  public PageAreaRenderBox getFooterArea()
  {
    return footerArea;
  }

  public PageAreaRenderBox getHeaderArea()
  {
    return headerArea;
  }
//
//  public void addAbsoluteFlow(NormalFlowRenderBox flow)
//  {
//    subFlows.add(flow);
//  }
//
//  public NormalFlowRenderBox[] getAbsoluteFlows()
//  {
//    return (NormalFlowRenderBox[])
//        subFlows.toArray(new NormalFlowRenderBox[subFlows.size()]);
//  }
//
//  public NormalFlowRenderBox getAbsoluteFlow(int i)
//  {
//    return (NormalFlowRenderBox) subFlows.get(i);
//  }
//
//  public int getAbsoluteFlowCount()
//  {
//    return subFlows.size();
//  }

  public LogicalPageBox getLogicalPage()
  {
    return this;
  }

  public NormalFlowRenderBox getNormalFlow()
  {
    return getContentArea();
  }

  public PageGrid getPageGrid()
  {
    return pageGrid;
  }

  public RenderBox getInsertationPoint()
  {
    return getContentArea().getInsertationPoint();
  }

  public long[] getPhysicalBreaks(final int axis)
  {
    if (axis == HORIZONTAL_AXIS)
    {
      return (long[]) horizontalBreaks.clone();
    }
    return (long[]) verticalBreaks.clone();
  }

  public boolean isOverflow()
  {
    return false;
  }

  /**
   * Derive creates a disconnected node that shares all the properties of the
   * original node. The derived node will no longer have any parent, silbling,
   * child or any other relationships with other nodes.
   *
   * @return
   */
  public RenderNode deriveFrozen(final boolean deepDerive)
  {
    final LogicalPageBox box = (LogicalPageBox) super.deriveFrozen(deepDerive);
    box.headerArea = (PageAreaRenderBox) headerArea.deriveFrozen(deepDerive);
    box.footerArea = (PageAreaRenderBox) footerArea.deriveFrozen(deepDerive);
//
//    box.subFlows.clear();
//
//    for (int i = 0; i < subFlows.size(); i++)
//    {
//      NormalFlowRenderBox flowRenderBox = (NormalFlowRenderBox) subFlows.get(i);
//      box.subFlows.add(flowRenderBox.deriveFrozen(deepDerive));
//    }

    return box;
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
    final LogicalPageBox box = (LogicalPageBox) super.derive(deepDerive);
    box.headerArea = (PageAreaRenderBox) headerArea.derive(deepDerive);
    box.footerArea = (PageAreaRenderBox) footerArea.derive(deepDerive);
//    box.subFlows.clear();
//
//    for (int i = 0; i < subFlows.size(); i++)
//    {
//      NormalFlowRenderBox flowRenderBox = (NormalFlowRenderBox) subFlows.get(i);
//      box.subFlows.add(flowRenderBox.derive(deepDerive));
//    }

    return box;
  }

  /**
   * Derives an hibernation copy. The resulting object should get stripped of
   * all unnecessary caching information and all objects, which will be
   * regenerated when the layouting restarts. Size does matter here.
   *
   * @return
   */
  public RenderNode hibernate()
  {
    final LogicalPageBox box = (LogicalPageBox) super.hibernate();
    box.headerArea = (PageAreaRenderBox) headerArea.hibernate();
    box.footerArea = (PageAreaRenderBox) footerArea.hibernate();

//    box.subFlows.clear();
//
//    for (int i = 0; i < subFlows.size(); i++)
//    {
//      NormalFlowRenderBox flowRenderBox = (NormalFlowRenderBox) subFlows.get(i);
//      box.subFlows.add(flowRenderBox.hibernate());
//    }

    return box;
  }

  /**
   * Clones this node. Be aware that cloning can get you into deep trouble, as
   * the relations this node has may no longer be valid.
   *
   * @return
   */
  public Object clone()
  {
    try
    {
      final LogicalPageBox o = (LogicalPageBox) super.clone();
      o.pageHeights = (long[]) pageHeights.clone();
      o.pageWidths = (long[]) pageWidths.clone();
      o.pageGrid = (PageGrid) pageGrid.clone();
//      o.subFlows = (ArrayList) subFlows.clone();
      return o;
    }
    catch (CloneNotSupportedException e)
    {
      throw new IllegalStateException("Cloning *must* be supported.");
    }
  }

  public boolean isNormalFlowActive()
  {
    return normalFlowActive;
  }

  public long getPageHeight()
  {
    return pageHeight;
  }

  public long getPageWidth()
  {
    return pageWidth;
  }

  public long getPageOffset()
  {
    return pageOffset;
  }

  public void setPageOffset(final long pageOffset)
  {
    this.pageOffset = pageOffset;
  }

  public void setNormalFlowActive(final boolean normalFlowActive)
  {
    this.normalFlowActive = normalFlowActive;
  }

  public void insertFirst(final RenderNode node)
  {
    insertBefore(getFirstChild(), node);
  }

  public void insertLast(final RenderNode node)
  {
    insertAfter(getLastChild(), node);
  }

  public RenderNode findNodeById(final Object instanceId)
  {
    // quick check
    final RenderNode footerNode = footerArea.findNodeById(instanceId);
    if (footerNode != null)
    {
      return footerNode;
    }

    final RenderNode headerNode = headerArea.findNodeById(instanceId);
    if (headerNode != null)
    {
      return headerNode;
    }
    // then depth-first for ordinary content.
    return super.findNodeById(instanceId);
  }
}
