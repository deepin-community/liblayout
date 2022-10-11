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
 * $Id: DisplayRoleReadHandler.java 2739 2007-04-02 11:41:22Z taqua $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.input.style.parser.stylehandler.box;

import org.jfree.layouting.input.style.keys.box.DisplayRole;
import org.jfree.layouting.input.style.parser.stylehandler.OneOfConstantsReadHandler;

/**
 * Creation-Date: 27.11.2005, 20:46:54
 *
 * @author Thomas Morgner
 */
public class DisplayRoleReadHandler extends OneOfConstantsReadHandler
{
  public DisplayRoleReadHandler()
  {
    super(false);
    addValue(DisplayRole.BLOCK);
    addValue(DisplayRole.COMPACT);
    addValue(DisplayRole.INLINE);
    addValue(DisplayRole.LIST_ITEM);
    addValue(DisplayRole.NONE);
    addValue(DisplayRole.RUBY_BASE);
    addValue(DisplayRole.RUBY_BASE_GROUP);
    addValue(DisplayRole.RUBY_TEXT);
    addValue(DisplayRole.RUBY_TEXT_GROUP);
    addValue(DisplayRole.RUN_IN);
    addValue(DisplayRole.TABLE_CAPTION);
    addValue(DisplayRole.TABLE_CELL);
    addValue(DisplayRole.TABLE_COLUMN);
    addValue(DisplayRole.TABLE_COLUMN_GROUP);
    addValue(DisplayRole.TABLE_FOOTER_GROUP);
    addValue(DisplayRole.TABLE_HEADER_GROUP);
    addValue(DisplayRole.TABLE_ROW);
    addValue(DisplayRole.TABLE_ROW_GROUP);
    addValue(DisplayRole.ABSOLUTE);
  }
}
