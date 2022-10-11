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
 * $Id: NumericCounterStyle.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.layouting.layouter.counters.numeric;

import java.util.HashSet;

import org.jfree.layouting.layouter.counters.CounterStyle;

public abstract class NumericCounterStyle implements CounterStyle
{
  private static final class ReplacementDefinition
  {
    private char original;
    private char replacement;

    private ReplacementDefinition (final char original, final char replacement)
    {
      this.original = original;
      this.replacement = replacement;
    }

    public char getOriginal ()
    {
      return original;
    }

    public char getReplacement ()
    {
      return replacement;
    }

    public boolean equals (final Object o)
    {
      if (this == o)
      {
        return true;
      }
      if (o == null || getClass() != o.getClass())
      {
        return false;
      }

      final ReplacementDefinition that = (ReplacementDefinition) o;

      if (original != that.original)
      {
        return false;
      }

      return true;
    }

    public int hashCode ()
    {
      return (int) original;
    }
  }

  private HashSet replacements;
  private int base;
  private transient ReplacementDefinition[] cachedDefinitions;
  private String suffix;

  protected NumericCounterStyle (final int base, final String suffix)
  {
    this.base = base;
    this.suffix = suffix;
    this.replacements = new HashSet();
  }

  public final void setReplacementChar (final char org, final char other)
  {
    this.replacements.add (new ReplacementDefinition(org, other));
    this.cachedDefinitions = null;
  }

  public final String getCounterValue (final int index)
  {
    if (cachedDefinitions == null)
    {
      cachedDefinitions = (ReplacementDefinition[])
            replacements.toArray(new ReplacementDefinition[replacements.size()]);
    }

    String numeric = Integer.toString(index, base);

    for (int i = 0; i < cachedDefinitions.length; i++)
    {
      final ReplacementDefinition def = cachedDefinitions[i];
      numeric = numeric.replace(def.getOriginal(), def.getReplacement());
    }
    return numeric;
  }

  public String getSuffix()
  {
    return suffix;
  }
}
