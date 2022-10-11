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
 * $Id: RenderableTextFactory.java 2739 2007-04-02 11:41:22Z taqua $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.renderer.text;

import org.jfree.layouting.StatefullComponent;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.renderer.model.RenderNode;

/**
 * Problem: Text may span more than one chunk, and text may influence the break
 * behaviour of the next chunk.
 * <p/>
 * Possible solution: TextFactory does not return the complete text. It returns
 * the text up to the last whitespace encountered and returns the text chunk
 * only if either finishText has been called or some more text comes in. The
 * ugly sideffect: Text may result in more than one renderable text chunk
 * returned.
 * <p/>
 * If we return lines (broken by an LineBreak-occurence) we can safe us a lot of
 * trouble later.
 *
 * @author Thomas Morgner
 */
public interface RenderableTextFactory extends StatefullComponent
{
  /**
   * The text is given as CodePoints.
   *
   * @param text
   * @return
   */
  public RenderNode[] createText(final int[] text,
                                 final int offset,
                                 final int length,
                                 final LayoutContext layoutContext);

  public RenderNode[] finishText();

  public void startText();
}
