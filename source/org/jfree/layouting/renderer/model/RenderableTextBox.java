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
 * $Id: RenderableTextBox.java 2739 2007-04-02 11:41:22Z taqua $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.layouting.renderer.model;

import org.jfree.layouting.State;
import org.jfree.layouting.layouter.content.resolved.ResolvedToken;
import org.jfree.layouting.layouter.context.LayoutContext;

/**
 * Creation-Date: 29.11.2006, 21:22:31
 *
 * @author Thomas Morgner
 */
public class RenderableTextBox extends InlineRenderBox
{
  private State textFactory;
  private ResolvedToken resolvedToken;
  private LayoutContext layoutContext;
  private String resolvedText;

  public RenderableTextBox(final State textFactoryState,
                           final ResolvedToken resolvedToken,
                           final LayoutContext layoutContext)
  {
    super(EmptyBoxDefinition.getInstance());
    this.layoutContext = layoutContext;
    this.textFactory = textFactoryState;
    this.resolvedToken = resolvedToken;
  }

  public State getTextFactory()
  {
    return textFactory;
  }

  public ResolvedToken getResolvedToken()
  {
    return resolvedToken;
  }

  public LayoutContext getLayoutContext()
  {
    return layoutContext;
  }

  public String getResolvedText()
  {
    return resolvedText;
  }

  public void setResolvedText(final String resolvedText)
  {
    this.resolvedText = resolvedText;
  }

  public boolean isDiscardable()
  {
    // no auto-pruning as we can't tell whether this box will be considered
    // empty ..
    return false;
  }
}
