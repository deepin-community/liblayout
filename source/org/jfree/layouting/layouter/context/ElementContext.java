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
 * $Id: ElementContext.java 2739 2007-04-02 11:41:22Z taqua $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.layouter.context;

/**
 * Creation-Date: 01.12.2005, 23:14:36
 *
 * @author Thomas Morgner
 */
public interface ElementContext
{
  /**
   * Checks, whether a counter is defined in this or one of the parent contexts.
   * A counter gets defined by the 'counter-reset' property.
   *
   * @param counterName the counter name.
   * @return true, if the counter is defined, false otherwise.
   */
  public boolean isCounterDefined(String counterName);

  /**
   * Returns the value for the given counter. If no counter exists under that
   * name, this method returns 0. This method always returns the current value
   * and ignores all page-policy definitions. Enforcing the page policy is up
   * to the page context.
   *
   * @param counterName
   * @return the value for the given counter.
   */
  public int getCounterValue(String counterName);

  /**
   * Reseting an counter creates a new Counter-Instance. Counters from parent
   * elements are not affected and remain unchanged. All further operations
   * issued by all sub-elements will now work with this counter.
   *
   * @param name
   * @param value
   */
  public void resetCounter(String name, int value);

  /**
   * Increments the counter with the given name. If no counter is known under
   * that name, the root node will create one.
   *
   * @param name
   * @param value
   */
  public void incrementCounter(String name, int value);

  /**
   * Sets a named string.
   *
   * @param name
   * @param value
   * @param define
   */
  public void setString(String name, String value, boolean define);

  /**
   * Retrieves the value for a given string. The value returned always
   * represents the *actual* value, ignoring any possibly defined page-policies.
   *
   * @param name
   * @return
   */
  public String getString(String name);
}
