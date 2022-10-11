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
 * $Id: DisplayRoleResolveHandler.java 2739 2007-04-02 11:41:22Z taqua $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.layouting.layouter.style.resolver.computed.box;

import org.jfree.layouting.input.style.keys.box.DisplayRole;
import org.jfree.layouting.layouter.style.resolver.computed.ConstantsResolveHandler;

public class DisplayRoleResolveHandler extends ConstantsResolveHandler
{
  public DisplayRoleResolveHandler ()
  {
    addNormalizeValue(DisplayRole.BLOCK);
    addNormalizeValue(DisplayRole.COMPACT);
    addNormalizeValue(DisplayRole.INLINE);
    addNormalizeValue(DisplayRole.LIST_ITEM);
    addNormalizeValue(DisplayRole.NONE);
    addNormalizeValue(DisplayRole.RUBY_BASE);
    addNormalizeValue(DisplayRole.RUBY_BASE_GROUP);
    addNormalizeValue(DisplayRole.RUBY_TEXT);
    addNormalizeValue(DisplayRole.RUBY_TEXT_GROUP);
    addNormalizeValue(DisplayRole.RUN_IN);
    addNormalizeValue(DisplayRole.TABLE_CAPTION);
    addNormalizeValue(DisplayRole.TABLE_CELL);
    addNormalizeValue(DisplayRole.TABLE_COLUMN);
    addNormalizeValue(DisplayRole.TABLE_COLUMN_GROUP);
    addNormalizeValue(DisplayRole.TABLE_FOOTER_GROUP);
    addNormalizeValue(DisplayRole.TABLE_HEADER_GROUP);
    addNormalizeValue(DisplayRole.TABLE_ROW);
    addNormalizeValue(DisplayRole.TABLE_ROW_GROUP);

    setFallback(DisplayRole.INLINE);
  }

}
