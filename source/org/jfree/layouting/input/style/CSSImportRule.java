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
 * $Id: CSSImportRule.java 2739 2007-04-02 11:41:22Z taqua $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.input.style;

import java.net.URL;

/**
 * Creation-Date: 23.11.2005, 11:06:52
 *
 * @author Thomas Morgner
 */
public class CSSImportRule extends StyleRule
{
  private URL href;
  private StyleSheet styleSheet;

  public CSSImportRule(final StyleSheet parentStyle, final StyleRule parentRule)
  {
    super(parentStyle, parentRule);
  }

  public URL getHref()
  {
    return href;
  }

  public void setHref(final URL href)
  {
    this.href = href;
  }

  public StyleSheet getStyleSheet()
  {
    return styleSheet;
  }

  public void setStyleSheet(final StyleSheet styleSheet)
  {
    this.styleSheet = styleSheet;
  }
}
