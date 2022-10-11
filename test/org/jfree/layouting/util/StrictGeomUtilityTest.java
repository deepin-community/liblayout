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
 * $Id$
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.layouting.util;

import junit.framework.TestCase;
import org.jfree.layouting.util.geom.StrictGeomUtility;

/**
 * Creation-Date: 01.04.2007, 22:36:24
 *
 * @author Thomas Morgner
 */
public class StrictGeomUtilityTest extends TestCase
{
  public StrictGeomUtilityTest()
  {
  }

  public StrictGeomUtilityTest(String string)
  {
    super(string);
  }

  public void testMultiply ()
  {
    assertEquals ("1 * 1 is 1", 1000, StrictGeomUtility.multiply(1000, 1000));
    assertEquals ("1000 * 1 is 1000", 1000000, StrictGeomUtility.multiply(1000000, 1000));
  }
}
