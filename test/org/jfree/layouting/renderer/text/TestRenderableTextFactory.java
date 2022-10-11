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
 * $Id: TestRenderableTextFactory.java,v 1.12 2007/04/02 11:41:22 taqua Exp $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.renderer.text;

import org.jfree.layouting.DefaultLayoutProcess;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.LibLayoutBoot;
import org.jfree.layouting.input.style.keys.font.FontStyleKeys;
import org.jfree.layouting.input.style.values.CSSConstant;
import org.jfree.layouting.layouter.context.ContextId;
import org.jfree.layouting.layouter.context.DefaultLayoutContext;
import org.jfree.layouting.layouter.context.FontSpecification;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.modules.output.html.FlowHtmlOutputProcessor;
import org.jfree.layouting.renderer.model.RenderNode;
import org.jfree.layouting.renderer.model.RenderableText;
import org.jfree.layouting.util.AttributeMap;
import org.pentaho.reporting.libraries.fonts.text.whitespace.CollapseWhiteSpaceFilter;
import org.pentaho.reporting.libraries.fonts.text.whitespace.WhiteSpaceFilter;
import org.pentaho.reporting.libraries.fonts.text.breaks.WordBreakProducer;
import org.pentaho.reporting.libraries.fonts.text.breaks.BreakOpportunityProducer;
import org.pentaho.reporting.libraries.fonts.text.font.NoKerningProducer;
import org.pentaho.reporting.libraries.fonts.text.font.KerningProducer;
import org.pentaho.reporting.libraries.fonts.text.font.StaticFontSizeProducer;
import org.pentaho.reporting.libraries.fonts.text.font.FontSizeProducer;
import org.pentaho.reporting.libraries.fonts.text.Spacing;
import org.pentaho.reporting.libraries.fonts.text.StaticSpacingProducer;
import org.pentaho.reporting.libraries.fonts.text.SpacingProducer;

/**
 * Creation-Date: 24.06.2006, 13:21:33
 *
 * @author Thomas Morgner
 */
public class TestRenderableTextFactory extends DefaultRenderableTextFactory
{
  public TestRenderableTextFactory(final LayoutProcess layoutProcess)
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
    return new CollapseWhiteSpaceFilter();
  }

//  public static void main(String[] args)
//  {
//    LibLayoutBoot.getInstance().start();
//
//    DefaultElementContext elementContext = new DefaultElementContext(null);
//    DefaultLayoutContext layoutContext =
//            new DefaultLayoutContext(new ContextId(0, 0,0), "Bah", "buh", null, new AttributeMap());
//
//    TestRenderableTextFactory tr = new TestRenderableTextFactory(null);
//
//
//    int[] text = new int[]{' ', 'W','o', ' ', 'b', ' ', ' ', '\n' };
//    tr.startText();
//    RenderableText[] rts = tr.createText
//            (text, 0, text.length, layoutContext);
//    tr.finishText();
//
//    RenderableText rt = rts[0];
//    Log.debug("RT: " + rt.getLength());
//    printGlyphs(rt);
//
////    RenderNode[] nodes = rt.split(rt.getMinorAxis(), 29000, null);
////    RenderableText text1 = (RenderableText) nodes[0];
////    RenderableText text2 = (RenderableText) nodes[1];
////    Log.debug("RT[0]: " + text1.getLength());
////
////    printGlyphs(text1);
////    Log.debug("RT[1]: " + text2.getLength());
////    printGlyphs(text2);
//  }



  public static void main(String[] args)
  {
    LibLayoutBoot.getInstance().start();
    DefaultLayoutContext layoutContext =
            new DefaultLayoutContext
                    (new ContextId(0, 0,0), "Bah", "buh", null, new AttributeMap());
    layoutContext.getStyle().setValue(FontStyleKeys.FONT_FAMILY, new CSSConstant("helvetica"));
    final FontSpecification fontSpecification = layoutContext.getFontSpecification();
    fontSpecification.setFontFamily("Arial");
    fontSpecification.setFontSize(12);

    final FlowHtmlOutputProcessor out =
        new FlowHtmlOutputProcessor(LibLayoutBoot.getInstance().getGlobalConfig());
    final DefaultLayoutProcess layoutProcess = new DefaultLayoutProcess(out);
    TestRenderableTextFactory tr = new TestRenderableTextFactory(layoutProcess);


//    int[] text = new int[]{ ' ', 'A',' ', 'B',' '};
    int[] text = new int[]{ '1'};

    RenderNode[] rts = tr.createText(text, 0, text.length, layoutContext);
    final RenderNode[] renderNodes = tr.finishText();

    RenderableText rt = (RenderableText) rts[1];
    printGlyphs(rt);
  }

  private static void printGlyphs(final RenderableText text)
  {
    Glyph[] glyphs = text.getGlyphs();
    final int lpos = Math.min(text.getOffset() + text.getLength(), glyphs.length);
    for (int i = text.getOffset(); i < lpos; i++)
    {
      Glyph glyph = glyphs[i];
//      Log.debug("BO: " + glyph + ", BW: " + glyph.getBreakWeight());
    }
  }
}
