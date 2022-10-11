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
 * $Id: CambodianCounterStyle.java 2739 2007-04-02 11:41:22Z taqua $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.layouting.layouter.counters.numeric;

public class CambodianCounterStyle extends NumericCounterStyle
{
  public CambodianCounterStyle ()
  {
    super(10, ".");
    setReplacementChar('0', '\u17e0');
    setReplacementChar('1', '\u17e1');
    setReplacementChar('2', '\u17e2');
    setReplacementChar('3', '\u17e3');
    setReplacementChar('4', '\u17e4');
    setReplacementChar('5', '\u17e5');
    setReplacementChar('6', '\u17e6');
    setReplacementChar('7', '\u17e7');
    setReplacementChar('8', '\u17e8');
    setReplacementChar('9', '\u17e9');
  }


}
