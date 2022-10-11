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
 * $Id: MarginTest.java,v 1.5 2007/04/02 11:41:22 taqua Exp $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.renderer.text;

import junit.framework.TestCase;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.LibLayoutBoot;
import org.jfree.layouting.layouter.context.ContextId;
import org.jfree.layouting.layouter.context.DefaultElementContext;
import org.jfree.layouting.layouter.context.DefaultLayoutContext;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.renderer.model.RenderNode;
import org.jfree.layouting.renderer.model.RenderableText;
import org.jfree.layouting.util.AttributeMap;
import org.pentaho.reporting.libraries.fonts.text.SpacingProducer;
import org.pentaho.reporting.libraries.fonts.text.Spacing;
import org.pentaho.reporting.libraries.fonts.text.StaticSpacingProducer;
import org.pentaho.reporting.libraries.fonts.text.whitespace.WhiteSpaceFilter;
import org.pentaho.reporting.libraries.fonts.text.whitespace.PreserveWhiteSpaceFilter;
import org.pentaho.reporting.libraries.fonts.text.breaks.BreakOpportunityProducer;
import org.pentaho.reporting.libraries.fonts.text.breaks.WordBreakProducer;
import org.pentaho.reporting.libraries.fonts.text.font.KerningProducer;
import org.pentaho.reporting.libraries.fonts.text.font.StaticFontSizeProducer;
import org.pentaho.reporting.libraries.fonts.text.font.FontSizeProducer;
import org.pentaho.reporting.libraries.fonts.text.font.NoKerningProducer;

/**
 * Creation-Date: 14.07.2006, 14:12:02
 *
 * @author Thomas Morgner
 */
public class MarginTest extends TestCase
{
  private class MarginRenderableTextFactory extends DefaultRenderableTextFactory
  {
    public MarginRenderableTextFactory(final LayoutProcess layoutProcess)
    {
      super(layoutProcess);
    }

    protected SpacingProducer createSpacingProducer(final LayoutContext layoutContext)
    {
      return new StaticSpacingProducer(Spacing.EMPTY_SPACING);
    }

    protected FontSizeProducer createFontSizeProducer(final LayoutContext layoutContext)
    {
      return new StaticFontSizeProducer(5000, 5000, 4000);
    }

    protected KerningProducer createKerningProducer(final LayoutContext layoutContext)
    {
      return new NoKerningProducer();
    }

    protected BreakOpportunityProducer createBreakProducer(final LayoutContext layoutContext)
    {
      return new WordBreakProducer();
    }

    protected WhiteSpaceFilter createWhitespaceFilter(final LayoutContext layoutContext)
    {
      return new PreserveWhiteSpaceFilter();
    }

  }

  public MarginTest()
  {
  }

  public MarginTest(String string)
  {
    super(string);
  }

  protected void setUp() throws Exception
  {
    LibLayoutBoot.getInstance().start();
  }

  public void testMarginCreation()
  {
    DefaultElementContext elementContext = new DefaultElementContext(null);
    DefaultLayoutContext layoutContext = new DefaultLayoutContext
            (new ContextId(0, 0, 0), "Bah", "buh", null, new AttributeMap());

    RenderableTextFactory tr = new MarginRenderableTextFactory(null);


    int[] text = new int[]{' ', 'A', ' ', ' ', 'B', ' '};

    RenderNode[] rts = tr.createText(text, 0, text.length, layoutContext);
    RenderNode[] fts = tr.finishText();

    assertEquals("Seq 1: Length", 2, rts.length);
    assertEquals("Seq 2: Length", 1, fts.length);

    RenderableText rt0 = (RenderableText) rts[0];
    RenderableText rt1 = (RenderableText) rts[1];
    RenderableText ft0 = (RenderableText) fts[0];
    assertEquals("Chunk 1: Length", 1, rt0.getLength());
    assertEquals("Chunk 2: Length", 1, rt1.getLength());
    assertEquals("Chunk 3: Length", 0, ft0.getLength());
  }


  public void testChunkCreation()
  {
    DefaultElementContext elementContext = new DefaultElementContext(null);
    DefaultLayoutContext layoutContext = new DefaultLayoutContext
            (new ContextId(0, 0, 0), "Bah", "buh", null, new AttributeMap());

    RenderableTextFactory tr = new MarginRenderableTextFactory(null);


    int[] text0 = new int[]{'A'};

    RenderNode[] rts0 = tr.createText(text0, 0, text0.length, layoutContext);
    RenderNode[] fts0 = tr.finishText();

    int[] text1 = new int[]{'B'};
    RenderNode[] rts1 = tr.createText(text1, 0, text1.length, layoutContext);
    RenderNode[] fts1 = tr.finishText();

    int[] text2 = new int[]{'C'};
    RenderNode[] rts2 = tr.createText(text2, 0, text2.length, layoutContext);
    RenderNode[] fts2 = tr.finishText();

    assertEquals("Seq 1: Length", 1, rts0.length);
//    assertEquals("Seq 2: Length", 1, fts.length);
//
//    RenderableText rt0 = rts[0];
//    RenderableText rt1 = rts[1];
//    RenderableText ft0 = fts[0];
//    assertEquals("Chunk 1: Length", 1, rt0.getLength());
//    assertEquals("Chunk 2: Length", 1, rt1.getLength());
//    assertEquals("Chunk 3: Length", 0, ft0.getLength());
//
//    assertEquals("Chunk 1: Has Leading", 5000, rt0.getLeadingSpace());
//    assertEquals("Chunk 1: No Trailing", 0, rt0.getTrailingSpace());
//    assertEquals("Chunk 2: Has Leading", 10000, rt1.getLeadingSpace());
//    assertEquals("Chunk 2: No Trailing", 0, rt1.getTrailingSpace());
//    assertEquals("Chunk 3: Has Leading", 5000, ft0.getLeadingSpace());
//    assertEquals("Chunk 3: No Trailing", 0, ft0.getTrailingSpace());
  }
}
