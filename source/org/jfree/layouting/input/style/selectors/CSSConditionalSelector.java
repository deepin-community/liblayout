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
 * $Id: CSSConditionalSelector.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.input.style.selectors;

import org.w3c.css.sac.AttributeCondition;
import org.w3c.css.sac.CombinatorCondition;
import org.w3c.css.sac.Condition;
import org.w3c.css.sac.ConditionalSelector;
import org.w3c.css.sac.SimpleSelector;

/**
 * Creation-Date: 30.11.2005, 16:43:45
 *
 * @author Thomas Morgner
 */
public class CSSConditionalSelector extends AbstractSelector implements ConditionalSelector
{
  private static final int ID_CONDITION = 0;
  private static final int ATTR_CONDITION = 1;
  private static final int OTHER_CONDITION = 2;

  private Condition condition;
  private SimpleSelector simpleSelector;

  public CSSConditionalSelector(final SimpleSelector simpleSelector,
                                final Condition condition)
  {
    this.simpleSelector = simpleSelector;
    this.condition = condition;
  }

  protected SelectorWeight createWeight()
  {
    final int[] conditions = new int[3];
    countConditions(conditions, condition);
    return new SelectorWeight
            (0, conditions[0], conditions[1], conditions[2] + 1);
  }

  private void countConditions
          (final int[] conditionCounter, final Condition condition)
  {
    if (condition.getConditionType() == Condition.SAC_ID_CONDITION)
    {
      conditionCounter[ID_CONDITION] += 1;
    }
    else if (condition instanceof AttributeCondition)
    {
      conditionCounter[ATTR_CONDITION] += 1;
    }
    else if (condition instanceof CombinatorCondition)
    {
      final CombinatorCondition c = (CombinatorCondition) condition;
      countConditions(conditionCounter, c.getFirstCondition());
      countConditions(conditionCounter, c.getSecondCondition());
    }
    else
    {
      conditionCounter[OTHER_CONDITION] += 1;
    }
  }

  /**
   * Returns the simple selector. <p>The simple selector can't be a
   * <code>ConditionalSelector</code>.</p>
   */
  public SimpleSelector getSimpleSelector()
  {
    return simpleSelector;
  }

  /** Returns the condition to be applied on the simple selector. */
  public Condition getCondition()
  {
    return condition;
  }

  /** An integer indicating the type of <code>Selector</code> */
  public short getSelectorType()
  {
    return SAC_CONDITIONAL_SELECTOR;
  }
}
