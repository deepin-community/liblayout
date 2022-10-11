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
 * $Id: State.java 2739 2007-04-02 11:41:22Z taqua $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting;

import java.io.Serializable;

/**
 * As it is said. Its a state. A state object is created by a StatefullComponent
 * and is used to save and restore the state of that component. The state object
 * must be immutable - and the restore methods must make sure that the state
 * object's contents are not modified in any way.
 *
 * @author Thomas Morgner
 */
public interface State extends Serializable
{
  /**
   * Creates a restored instance of the saved component.
   *
   * By using this factory-like approach, we gain independence from having to
   * know the actual implementation. This makes things a lot easier.
   *
   * @param layoutProcess the layout process that controls it all
   * @return the saved state
   * @throws StateException
   */
  public StatefullComponent restore (LayoutProcess layoutProcess) throws StateException;
}
