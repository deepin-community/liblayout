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
 * $Id: ContentSpecification.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.layouting.layouter.context;

import org.jfree.layouting.layouter.content.ContentToken;
import org.jfree.layouting.layouter.content.computed.ContentsToken;

public class ContentSpecification
{
  private static final QuotesPair[] EMPTY_QUOTES = new QuotesPair[0];
  private static final ContentsToken[] EMPTY_CONTENT = new ContentsToken[0];

  private QuotesPair[] quotes;
  private ContentToken[] contents;
  private ContentToken[] strings;
  private ContentToken[] alternateText;
  private boolean allowContentProcessing;
  private boolean inhibitContent;
  private int quotingLevel;
  private String moveTarget;

  public ContentSpecification ()
  {
    quotes = EMPTY_QUOTES;
    contents = EMPTY_CONTENT;
    strings = EMPTY_CONTENT;
    alternateText = EMPTY_CONTENT;
    allowContentProcessing = true;
  }

  public boolean isInhibitContent()
  {
    return inhibitContent;
  }

  public void setInhibitContent(final boolean inhibitContent)
  {
    this.inhibitContent = inhibitContent;
  }

  public QuotesPair[] getQuotes ()
  {
    return (QuotesPair[]) quotes.clone();
  }

  public ContentToken[] getStrings()
  {
    return (ContentToken[]) strings.clone();
  }

  public void setStrings(final ContentToken[] strings)
  {
    this.strings = (ContentToken[]) strings.clone();
  }

  public ContentToken[] getAlternateText()
  {
    return (ContentToken[]) alternateText.clone();
  }

  public void setAlternateText(final ContentToken[] strings)
  {
    this.alternateText = (ContentToken[]) strings.clone();
  }

  public String getOpenQuote (final int level)
  {
    if (level < 0)
    {
      return "";
    }
    if (level >= quotes.length)
    {
      if (quotes.length == 0)
      {
        return "";
      }
      return quotes[quotes.length - 1].getOpenQuote();
    }
    return quotes[level].getOpenQuote();
  }

  public String getCloseQuote (final int level)
  {
    if (level < 0)
    {
      return "";
    }
    if (level >= quotes.length)
    {
      if (quotes.length == 0)
      {
        return "";
      }
      return quotes[quotes.length - 1].getCloseQuote();
    }
    return quotes[level].getCloseQuote();
  }

  public void setQuotes (final QuotesPair[] quotes)
  {
    if (this.quotes == null)
    {
      throw new NullPointerException();
    }
    this.quotes = (QuotesPair[]) quotes.clone();
  }

  public ContentToken[] getContents ()
  {
    return (ContentToken[]) contents.clone();
  }

  public void setContents (final ContentToken[] contents)
  {
    this.contents = (ContentToken[]) contents.clone();
  }


  public boolean isAllowContentProcessing()
  {
    return allowContentProcessing;
  }

  public void setAllowContentProcessing(final boolean allowContentProcessing)
  {
    this.allowContentProcessing = allowContentProcessing;
  }

  public int getQuotingLevel ()
  {
    return quotingLevel;
  }

  public void setQuotingLevel (final int quotingLevel)
  {
    this.quotingLevel = quotingLevel;
  }

  public String getMoveTarget()
  {
    return moveTarget;
  }

  public void setMoveTarget(final String moveTarget)
  {
    this.moveTarget = moveTarget;
  }
}
