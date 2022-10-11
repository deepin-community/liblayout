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
 * $Id$
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.layouting.test;

import java.util.ArrayList;

import junit.framework.TestCase;
import org.jfree.layouting.ChainingLayoutProcess;
import org.jfree.layouting.DefaultLayoutProcess;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.LayoutProcessState;
import org.jfree.layouting.LibLayoutBoot;
import org.jfree.layouting.StateException;
import org.jfree.layouting.layouter.feed.InputFeedException;
import org.jfree.layouting.modules.output.graphics.GraphicsOutputProcessor;
import org.jfree.layouting.namespace.Namespaces;

/**
 * Creation-Date: Jan 5, 2007, 1:50:05 PM
 *
 * @author Thomas Morgner
 */
public class MixInlineAndBlockTest extends TestCase
{
  private FeedCommand[] document;

  public MixInlineAndBlockTest(String string)
  {
    super(string);
  }

  protected void setUp() throws Exception
  {
    super.setUp();
    LibLayoutBoot.getInstance().start();
    createDocument();
  }

  /**
   * First run: Creating the save state must not result in an exception.
   * Reusing the state is not really valid, as this gives funny results in
   * the output target. (Although it would be technically possible, it would
   * result in invalid table-of-contents etc..)
   *
   * @param lp
   * @throws StateException
   * @throws InputFeedException
   */
  private void executeFirst (LayoutProcess lp)
      throws StateException, InputFeedException
  {
    for (int i = 0; i < document.length; i++)
    {
      FeedCommand command = document[i];
      command.execute(lp);
    }
  }

  /**
   * Second run: The pagination run. Saving all states is mandatory, reusing
   * them later is what we want. (This happens in the third run.)
   *
   * @param lp
   * @throws StateException
   * @throws InputFeedException
   */
  private LayoutProcessState[] executeSecond (LayoutProcess lp)
      throws StateException, InputFeedException
  {
    LayoutProcessState[] states = new LayoutProcessState[document.length];
    for (int i = 0; i < document.length; i++)
    {
      FeedCommand command = document[i];
      states[i] = command.execute(lp);
    }
    return states;
  }



  public void testMixing () throws InputFeedException, StateException
  {
    final GraphicsOutputProcessor gop = new GraphicsOutputProcessor
        (LibLayoutBoot.getInstance().getGlobalConfig());

    // Stage one: Global values ..
    final LayoutProcess dlpPrep1 = new ChainingLayoutProcess(new DefaultLayoutProcess(gop));
    executeFirst(dlpPrep1);

    // Stage two: Pagination
    final LayoutProcess dlpPrep2 =
        new ChainingLayoutProcess(new DefaultLayoutProcess(gop));
    final LayoutProcessState[] layoutProcessStates = executeSecond(dlpPrep2);

      // Stage Three: Content generation ..
    for (int i = 0; i < layoutProcessStates.length; i++)
    {
      final LayoutProcessState state = layoutProcessStates[i];
      executeThird(state.restore(gop), i);
    }
  }

  private void executeThird (LayoutProcess lp, int idx)
      throws StateException, InputFeedException
  {
    for (int i = idx + 1; i < document.length; i++)
    {
      FeedCommand command = document[i];
      command.execute(lp);
    }
  }

  private void createDocument()
  {
    ArrayList documentList = new ArrayList();
    documentList.add(FeedCommand.startDocument());
      documentList.add(FeedCommand.startElement(Namespaces.XHTML_NAMESPACE, "span"));
      documentList.add(FeedCommand.addContent(" ROOT"));
        documentList.add(FeedCommand.startElement(Namespaces.XHTML_NAMESPACE, "span"));
        documentList.add(FeedCommand.addContent(" alsda asadja sawjeop fas aasdl afsldj9pw"));
          documentList.add(FeedCommand.startElement(Namespaces.XHTML_NAMESPACE, "div"));
          documentList.add(FeedCommand.addContent(" bbb b bbbbbbbbb b b bbbbb bb"));
            documentList.add(FeedCommand.startElement(Namespaces.XHTML_NAMESPACE, "span"));
            documentList.add(FeedCommand.addContent("cc ccc ccc c"));
            documentList.add(FeedCommand.endElement(Namespaces.XHTML_NAMESPACE, "span"));
          documentList.add(FeedCommand.addContent("sddddd"));
          documentList.add(FeedCommand.endElement(Namespaces.XHTML_NAMESPACE, "div")); // Close the DIV
        documentList.add(FeedCommand.addContent("eeeee"));
        documentList.add(FeedCommand.endElement(Namespaces.XHTML_NAMESPACE, "span"));
      documentList.add(FeedCommand.addContent("FFF"));
      documentList.add(FeedCommand.endElement(Namespaces.XHTML_NAMESPACE, "span"));
    documentList.add(FeedCommand.endDocument());

    document = (FeedCommand[]) documentList.toArray(new FeedCommand[0]);
  }
}
