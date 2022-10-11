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
 * $Id: TableColumnModelTest.java 6709 2008-12-02 16:41:37Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.renderer.model.table.cols;

import junit.framework.TestCase;

/**
 * Creation-Date: 21.07.2006, 16:45:30
 *
 * @author Thomas Morgner
 */
public class TableColumnModelTest extends TestCase
{
  public TableColumnModelTest(final String string)
  {
    super(string);
  }
//
//  public void testTableColumnModel ()
//  {
//    TableRenderBox table = new TableRenderBox(new EmptyBoxDefinition());
//    TableColumnModel model = new SpearateColumnModel(table);
//
//    final TableColumn column1 = new TableColumn();
//    final TableColumn column2 = new TableColumn();
//
//    TableColumnGroup tg = new TableColumnGroup();
//    tg.addColumn(column1);
//    tg.addColumn(column2);
//    model.addColumnGroup(tg);
//    model.addAutoColumn();
//    model.addAutoColumn();
//
//    model.getColumn(0).setSizes(1, 16000, 16000);
//    model.getColumn(1).setSizes(1, 16000, 16000);
//    model.getColumn(1).setSizes(2, 15000, 15000);
//    model.getColumn(2).setSizes(1, 16000, 16000);
//    model.getColumn(3).setSizes(1, 15000, 15000);
//
//    model.validateSizes();
//    assertEquals(63000, model.getPreferredSize());
//    assertEquals(63000, model.getMinimumChunkSize());
//  }
}
