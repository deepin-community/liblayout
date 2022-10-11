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
 * $Id: DevanagariCounterStyle.java 2739 2007-04-02 11:41:22Z taqua $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.layouting.layouter.counters.numeric;

public class DevanagariCounterStyle extends NumericCounterStyle
{
  public DevanagariCounterStyle ()
  {
    super(10, ".");
    setReplacementChar('0', '\u0966');
    setReplacementChar('1', '\u0967');
    setReplacementChar('2', '\u0968');
    setReplacementChar('3', '\u0969');
    setReplacementChar('4', '\u096A');
    setReplacementChar('5', '\u096b');
    setReplacementChar('6', '\u096c');
    setReplacementChar('7', '\u096d');
    setReplacementChar('8', '\u096e');
    setReplacementChar('9', '\u096f');
  }


}
