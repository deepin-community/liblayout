/**
 * =============================================
 * LibCSS: A free document archive library
 * =============================================
 *
 * Project Info:  http://reporting.pentaho.org/libcss/
 *
 * (C) Copyright 2007,2008, by Pentaho Corporation and Contributors.
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
 * StyleRule.java
 * ------------
 */

package org.jfree.layouting.input.style;

import java.io.Serializable;

/**
 * Creation-Date: 23.11.2005, 10:50:15
 *
 * @author Thomas Morgner
 */
public abstract class StyleRule implements Serializable, Cloneable
{
  private StyleSheet parentStyle;
  private StyleRule parentRule;
  private boolean readOnly;
  private StyleKeyRegistry styleKeyRegistry;

  protected StyleRule(final StyleKeyRegistry styleKeyRegistry)
  {
    if (styleKeyRegistry == null)
    {
      throw new NullPointerException();
    }
    this.styleKeyRegistry = styleKeyRegistry;
  }

  protected StyleRule(final StyleSheet parentStyle,
                      final StyleRule parentRule)
  {
    if (parentStyle == null)
    {
      this.styleKeyRegistry = StyleKeyRegistry.getRegistry();
    }
    else
    {
      this.styleKeyRegistry = parentStyle.getStyleKeyRegistry();
    }
    this.parentStyle = parentStyle;
    this.parentRule = parentRule;
  }

  public StyleKeyRegistry getStyleKeyRegistry()
  {
    return styleKeyRegistry;
  }

  public StyleSheet getParentStyle()
  {
    return parentStyle;
  }

  public StyleRule getParentRule()
  {
    return parentRule;
  }

  protected void setParentStyle(final StyleSheet parentStyle)
  {
    if (parentStyle == null)
    {
      throw new NullPointerException();
    }
    this.parentStyle = parentStyle;
  }

  protected void setParentRule(final StyleRule parentRule)
  {
    this.parentRule = parentRule;
  }

  public Object clone() throws CloneNotSupportedException
  {
    // parent rule and parent style are not cloned.
    return super.clone();
  }

  public final void makeReadOnly()
  {
    readOnly = true;
  }

  public final boolean isReadOnly()
  {
    return readOnly;
  }
}
