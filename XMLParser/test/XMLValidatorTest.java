import state_design.XMLParser;
import state_design.XMLValidator;

import static org.junit.Assert.assertEquals;

public class XMLValidatorTest extends AbstractXMLParserTest {


  @Override
  protected XMLParser getParserInstance() {
    return new XMLValidator();
  }

  private static final String VALID = "Status:Valid";
  private static final String INCOMPLETE = "Status:Incomplete";
  private static final String EMPTY = "Status:Empty";


  /**
   * Nothing there.
   */
  @Override
  public void testEmpty() {
    iterateThru(xmlParser, "");
    expected = EMPTY;
    actual = xmlParser.output();
    assertEquals(expected,actual);
  }

  /**
   * <
   */
  @Override
  public void testOutputJustStartAngle() {
    iterateThru(xmlParser, "<");
    expected = INCOMPLETE;
    actual = xmlParser.output();
    assertEquals(expected,actual);
  }

  /**
   * <xml>
   */
  @Override
  public void testOutputAtTag() {
    iterateThru(xmlParser, "<xml>");
    expected = INCOMPLETE;
    actual = xmlParser.output();
    assertEquals(expected,actual);
  }

  /**
   * <xml>hello
   */
  @Override
  public void testOutputOpenTagPlusChars() {
    iterateThru(xmlParser, "<xml>hello");
    expected = INCOMPLETE;
    actual = xmlParser.output();
    assertEquals(expected,actual);
  }

  /**
   * <xml>hello <
   */
  @Override
  public void testOutputOpeningSecondTag() {
    iterateThru(xmlParser, "<xml>hello <");
    expected = INCOMPLETE;
    actual = xmlParser.output();
    assertEquals(expected,actual);
  }

  /**
   * <xml>hello </xml>
   */
  @Override
  public void testOutputClosingBody() {
    iterateThru(xmlParser, "<xml>hello </xml>");
    expected = VALID;
    actual = xmlParser.output();
    assertEquals(expected,actual);
  }

  /**
   * <xml>hello <html>
   */
  @Override
  public void testOutputOpeningNestedTag() {
    iterateThru(xmlParser, "<xml>hello <html>");
    expected = INCOMPLETE;
    actual = xmlParser.output();
    assertEquals(expected,actual);
  }

  /**
   * <xml>hello <html>asdf
   */
  @Override
  public void testOutputNestedTagChars() {
    iterateThru(xmlParser, "<xml>hello <html>asdf");
    expected = INCOMPLETE;
    actual = xmlParser.output();
    assertEquals(expected,actual);
  }

  /**
   * <xml>hello <html>asdf</html>
   */
  @Override
  public void testOutputCloseNestedTag() {
    iterateThru(xmlParser, "<xml>hello <html>asdf</html>");
    expected = INCOMPLETE;
    actual = xmlParser.output();
    assertEquals(expected,actual);
  }

  /**
   * <xml>hello <html>asdf</html>test
   */
  @Override
  public void testOutputCharsAfterNestedTagClose() {
    iterateThru(xmlParser, "<xml>hello <html>asdf</html>test");
    expected = INCOMPLETE;
    actual = xmlParser.output();
    assertEquals(expected,actual);
  }

  /**
   * <xml>hello <html>asdf</html>test</xml
   */
  @Override
  public void testOutputStartingToCloseRoot() {
    iterateThru(xmlParser, "<xml>hello <html>asdf</html>test</xml");
    expected = INCOMPLETE;
    actual = xmlParser.output();
    assertEquals(expected,actual);
  }

  /**
   * <xml>hello <html>asdf</html>test</xml>
   */
  @Override
  public void testOutputClosedRoot() {
    iterateThru(xmlParser, "<xml>hello <html>asdf</html>test</xml>");
    expected = VALID;
    actual = xmlParser.output();
    assertEquals(expected,actual);
  }

  @Override
  public void testAfterClosed() {

  }
}
