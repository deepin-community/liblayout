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
 * $Id: ClearAfterReadHandler.java 2739 2007-04-02 11:41:22Z taqua $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.input.style.parser.stylehandler.box;

import org.jfree.layouting.input.style.keys.box.Clear;
import org.jfree.layouting.input.style.parser.stylehandler.OneOfConstantsReadHandler;

/**
 * Creation-Date: 28.11.2005, 15:54:25
 *
 * @author Thomas Morgner
 */
public class ClearAfterReadHandler extends OneOfConstantsReadHandler
{
  public ClearAfterReadHandler()
  {
    super(false);
    addValue(Clear.BOTH);
    addValue(Clear.BOTTOM);
    addValue(Clear.END);
    addValue(Clear.INSIDE);
    addValue(Clear.LEFT);
    addValue(Clear.NONE);
    addValue(Clear.OUTSIDE);
    addValue(Clear.RIGHT);
    addValue(Clear.START);
    addValue(Clear.TOP);
  }
}
