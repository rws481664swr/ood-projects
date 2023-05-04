import state_design.InvalidXMLException;
import state_design.XMLInfoLogger;
import state_design.XMLParser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class XMLInfoLoggerTest extends AbstractXMLParserTest {


  @Override
  public XMLParser getParserInstance() {
    return new XMLInfoLogger();
  }

  @Override
  public void testEmpty() {
    iterateThru(xmlParser, "");
    expected = "";
    actual = xmlParser.output();
    assertEquals(expected, actual);
  }

  /**
   * <
   */
  @Override
  public void testOutputJustStartAngle() {
    iterateThru(xmlParser, "<");
    expected = "";
    actual = xmlParser.output();
    assertEquals(expected, actual);
  }

  /**
   * <xml>
   */
  @Override
  public void testOutputAtTag() {
    iterateThru(xmlParser, "<xml>");
    expected = "Started:xml\n";
    actual = xmlParser.output();
    assertEquals(expected, actual);
  }

  /**
   * <xml>hello
   */
  @Override
  public void testOutputOpenTagPlusChars() {
    iterateThru(xmlParser, "<xml>hello");
    expected = "Started:xml\n"
            + "Characters:hello\n";
    actual = xmlParser.output();

    assertEquals(expected, actual);
  }

  /**
   * <xml>hello <
   */
  @Override
  public void testOutputOpeningSecondTag() {
    iterateThru(xmlParser, "<xml>hello <");
    expected = "Started:xml\n"
            + "Characters:hello \n";
    actual = xmlParser.output();

    assertEquals(expected, actual);
  }

  /**
   * <xml>hello </xml>
   */
  @Override
  public void testOutputClosingBody() {
    iterateThru(xmlParser, "<xml>hello </xml>");
    expected = "Started:xml\n"
            + "Characters:hello \n"
            + "Ended:xml\n";
    actual = xmlParser.output();
    assertEquals(expected, actual);
  }

  /**
   * <xml>hello <html>
   */
  @Override
  public void testOutputOpeningNestedTag() {
    iterateThru(xmlParser, "<xml>hello <html>");
    expected = "Started:xml\n"
            + "Characters:hello \n"
            + "Started:html\n";
    actual = xmlParser.output();


    assertEquals(expected, actual);
  }

  /**
   * <xml>hello <html>asdf
   */
  @Override
  public void testOutputNestedTagChars() {
    iterateThru(xmlParser, "<xml>hello <html>asdf");
    expected = "Started:xml\n"
            + "Characters:hello \n"
            + "Started:html\n"
            + "Characters:asdf\n";
    actual = xmlParser.output();

    assertEquals(expected, actual);
  }

  /**
   * <xml>hello <html>asdf</html>
   */
  @Override
  public void testOutputCloseNestedTag() {
    iterateThru(xmlParser, "<xml>hello <html>asdf</html>");
    expected = "Started:xml\n"
            + "Characters:hello \n"
            + "Started:html\n"
            + "Characters:asdf\n"
            + "Ended:html\n";
    actual = xmlParser.output();
    assertEquals(expected, actual);
  }

  /**
   * <xml>hello <html>asdf</html>test
   */
  @Override
  public void testOutputCharsAfterNestedTagClose() {
    iterateThru(xmlParser, "<xml>hello <html>asdf</html>test");
    expected = "Started:xml\n"
            + "Characters:hello \n"
            + "Started:html\n"
            + "Characters:asdf\n"
            + "Ended:html\n"
            + "Characters:test\n";
    actual = xmlParser.output();
    assertEquals(expected, actual);
  }

  /**
   * <xml>hello <html>asdf</html>test</xml
   */
  @Override
  public void testOutputStartingToCloseRoot() {
    iterateThru(xmlParser, "<xml>hello <html>asdf"
            + "</html>test</xml");
    expected = "Started:xml\n"
            + "Characters:hello \n"
            + "Started:html\n"
            + "Characters:asdf\n"
            + "Ended:html\n"
            + "Characters:test\n";
    actual = xmlParser.output();
    assertEquals(expected, actual);
  }

  /**
   * <xml>hello <html>asdf</html>test</xml>
   */
  @Override
  public void testOutputClosedRoot() {
    iterateThru(xmlParser, "<xml>hello <html>asdf</html>"
            + "test</xml>");
    expected = "Started:xml\n"
            + "Characters:hello \n"
            + "Started:html\n"
            + "Characters:asdf\n"
            + "Ended:html\n"
            + "Characters:test\n"
            + "Ended:xml\n";
    actual = xmlParser.output();
    assertEquals(expected, actual);
  }

  @Override
  public void testAfterClosed() {
    String test = "<xml>text</xml>c";
    try {
      for (char c : test.toCharArray()) {
        xmlParser.input(c);
      }
      fail();
    } catch (InvalidXMLException e) {
      assertTrue(true);
    }
  }
}
