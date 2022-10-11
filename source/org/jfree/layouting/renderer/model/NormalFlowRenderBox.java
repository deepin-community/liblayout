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
 * $Id: NormalFlowRenderBox.java 6489 2008-11-28 14:53:40Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.renderer.model;

import java.util.ArrayList;

import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.output.OutputProcessorMetaData;
import org.jfree.layouting.namespace.Namespaces;

/**
 * A box that defines its own normal flow. All absolutly positioned or
 * floating elements define their own normal flow.
 *
 * Each flow contains an invisible place-holder element, which marks the
 * position of that element in the parent's normal-flow.
 *
 * A flow may hold a set of sub-flows. Sub-Flows are derived from floating
 * elements. Absolutely positioned elements are placed on the page context.
 *
 * Normal-flows are derived for each absolutly or staticly positioned element.
 *
 * @author Thomas Morgner
 */
public class NormalFlowRenderBox extends BlockRenderBox
{
  private Object placeHolderId;
  private PlaceholderRenderNode placeHolder;
  private ArrayList subFlows;

  public NormalFlowRenderBox(final BoxDefinition boxDefinition)
  {
    super(boxDefinition);
    placeHolder = new PlaceholderRenderNode();
    placeHolderId = getPlaceHolder().getInstanceId();
    subFlows = new ArrayList();

    // hardcoded for now, content forms lines, which flow from top to bottom
    // and each line flows horizontally (later with support for LTR and RTL)
    final NodeLayoutProperties nodeLayoutProperties = getNodeLayoutProperties();
    nodeLayoutProperties.setNamespace(Namespaces.LIBLAYOUT_NAMESPACE);
    nodeLayoutProperties.setTagName("normal-flow");
  }

  public void appyStyle(final LayoutContext context, final OutputProcessorMetaData metaData)
  {
    super.appyStyle(context, metaData);
    final NodeLayoutProperties nodeLayoutProperties = getNodeLayoutProperties();
    nodeLayoutProperties.setNamespace(Namespaces.LIBLAYOUT_NAMESPACE);
    nodeLayoutProperties.setTagName("normal-flow");
  }

  public PlaceholderRenderNode getPlaceHolder()
  {
    return placeHolder;
  }

  public void addChild(final RenderNode child)
  {
    if (child instanceof BlockRenderBox == false)
    {
      throw new IllegalStateException("This cannot be.");
    }

    super.addChild(child);
  }

  public void addFlow (final NormalFlowRenderBox flow)
  {
    subFlows.add(flow);
  }

  public NormalFlowRenderBox[] getFlows ()
  {
    return (NormalFlowRenderBox[])
            subFlows.toArray(new NormalFlowRenderBox[subFlows.size()]);
  }

  public NormalFlowRenderBox getFlow (final int i)
  {
    return (NormalFlowRenderBox) subFlows.get(i);
  }

  public int getFlowCount()
  {
    return subFlows.size();
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
    final NormalFlowRenderBox renderBox = (NormalFlowRenderBox) super.hibernate();
    renderBox.subFlows = new ArrayList(subFlows.size());
    for (int i = 0; i < subFlows.size(); i++)
    {
      final NormalFlowRenderBox box = (NormalFlowRenderBox) subFlows.get(i);
      renderBox.subFlows.add(box.derive(true));

      box.placeHolder = (PlaceholderRenderNode) findNodeById(box.placeHolderId);
    }
    renderBox.placeHolder = null;
    return renderBox;
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
    final NormalFlowRenderBox renderBox = (NormalFlowRenderBox) super.derive(deepDerive);
    if (deepDerive)
    {
      renderBox.subFlows = new ArrayList(subFlows.size());
      for (int i = 0; i < subFlows.size(); i++)
      {
        final NormalFlowRenderBox box = (NormalFlowRenderBox) subFlows.get(i);
        renderBox.subFlows.add(box.derive(true));

        box.placeHolder = (PlaceholderRenderNode) findNodeById(box.placeHolderId);
      }
      renderBox.placeHolder = null;
    }
    else
    {
      renderBox.placeHolder = (PlaceholderRenderNode) placeHolder.derive(true);
      renderBox.subFlows = new ArrayList();
    }
    return renderBox;
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
    final NormalFlowRenderBox renderBox = (NormalFlowRenderBox) super.deriveFrozen(deepDerive);
    if (deepDerive)
    {
      renderBox.subFlows = new ArrayList(subFlows.size());
      for (int i = 0; i < subFlows.size(); i++)
      {
        final NormalFlowRenderBox box = (NormalFlowRenderBox) subFlows.get(i);
        renderBox.subFlows.add(box.deriveFrozen(true));

        box.placeHolder = (PlaceholderRenderNode) findNodeById(box.placeHolderId);
      }
      renderBox.placeHolder = null;
    }
    else
    {
      renderBox.placeHolder = (PlaceholderRenderNode) placeHolder.deriveFrozen(true);
      renderBox.subFlows = new ArrayList();
    }
    return renderBox;
  }

  public RenderBox getInsertationPoint()
  {
    for (int i = 0; i < subFlows.size(); i++)
    {
      final NormalFlowRenderBox box = (NormalFlowRenderBox) subFlows.get(i);
      if (box.isOpen())
      {
        return box.getInsertationPoint();
      }
    }
    return super.getInsertationPoint();
  }

  public NormalFlowRenderBox getNormalFlow()
  {
    return this;
  }

  public RenderNode findNodeById(final Object instanceId)
  {
    for (int i = 0; i < subFlows.size(); i++)
    {
      final RenderNode node = (RenderNode) subFlows.get(i);
      final RenderNode nodeById = node.findNodeById(instanceId);
      if (nodeById != null)
      {
        return nodeById;
      }
    }
    return super.findNodeById(instanceId);
  }
}
