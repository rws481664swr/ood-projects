import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import state_design.InvalidXMLException;
import state_design.XMLParser;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.fail;

public abstract class AbstractXMLParserTest {

  static Map<String, String> results = new HashMap<>();

  {

    Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          OutputStream out = Files.newOutputStream(Paths.get("xmlstuff"));
          results.entrySet()
                  .stream().forEach(e -> {
            try {
              out.write(new StringBuilder()
                      .append(e
                              .getKey())
                      .append("\n")
                      .append(e
                              .getValue())
                      .toString()
                      .getBytes());
            } catch (IOException e1) {
              e1.printStackTrace();
            }
          });
        } catch (Exception e) {
        }
      }
    }));
  }


  /**
   * supplies a parser to the common methods.
   *
   * @return an instance of state_design.XMLParser.
   */
  protected abstract XMLParser getParserInstance();

  protected String expected;
  protected String actual;
  protected XMLParser xmlParser;

  @Before
  public void setUp() {
    expected = "VALUE_UNSET";
    actual = "";
    xmlParser = getParserInstance();
  }

  public final void pass() {
    assertTrue(true);
  }

  public final void pass(String messasge) {
    assertTrue(messasge, true);
  }


  @Test
  public abstract void testAfterClosed();


  /**
   * tests state when input has not been called yet.
   */
  public abstract void testEmpty();

  /**
   * <
   */
  @Test
  public abstract void testOutputJustStartAngle();

  /**
   * <xml>
   */
  @Test
  public abstract void testOutputAtTag();

  /**
   * <xml>hello
   */
  @Test
  public abstract void testOutputOpenTagPlusChars();

  /**
   * <xml>hello <
   */
  @Test
  public abstract void testOutputOpeningSecondTag();

  /**
   * <xml>hello </xml>
   */
  @Test
  public abstract void testOutputClosingBody();

  /**
   * <xml>hello <html
   * <xml>hello <html>
   */
  @Test
  public abstract void testOutputOpeningNestedTag();

  /**
   * <xml>hello <html>asdf
   */
  @Test
  public abstract void testOutputNestedTagChars();

  /**
   * <xml>hello <html>asdf</html>
   */
  @Test
  public abstract void testOutputCloseNestedTag();

  /**
   * <xml>hello <html>asdf</html>test
   */
  @Test
  public abstract void testOutputCharsAfterNestedTagClose();

  /**
   * <xml>hello <html>asdf</html>test</xml
   */
  @Test
  public abstract void testOutputStartingToCloseRoot();

  /**
   * <xml>hello <html>asdf</html>test</xml>
   */
  @Test
  public abstract void testOutputClosedRoot();

  @Test
  public void testInvalidFirstChar() {
    try {
      invalidXMLCatcher(getParserInstance(), '>');
      invalidXMLCatcher(getParserInstance(), '#');
      invalidXMLCatcher(getParserInstance(), 'a');
      assertTrue(true);
    } catch (IllegalStateException e) {
      fail();
    }
  }

  @Test
  public void testNoSpacesInTag() {
    try {
      XMLParser toSend = xmlParser.input('<')
              .input('x')
              .input('m')
              .input('l');
      invalidXMLCatcher(toSend, ' ');
      setUp();
      toSend = xmlParser.input('<');
      invalidXMLCatcher(toSend, ' ');
      setUp();
      toSend = xmlParser.input('<');
      invalidXMLCatcher(toSend, ' ');

      assertTrue(true);
    } catch (IllegalStateException e) {
      fail();
    } catch (InvalidXMLException e) {
      fail();
    }
  }

  @Test
  public void testNoIllegalTagCharacters() {
    try {
      XMLParser toSend = xmlParser.input('<')
              .input('x');
      invalidXMLCatcher(toSend, '9');
      setUp();

      toSend = xmlParser.input('<');
      invalidXMLCatcher(toSend, '-');

      setUp();
      toSend = xmlParser.input('<');
      invalidXMLCatcher(toSend, '1');
      fail();
      assertTrue(true);
    } catch (IllegalStateException e) {

    } catch (InvalidXMLException e) {

    }
  }

  @Test
  public void testSimpleBalancedTag() {
    String test = "<xml> charset </xml>";
    try {
      for (char c : test.toCharArray()) {

        xmlParser.input(c);

      }
    } catch (InvalidXMLException e) {
      fail();
    }
    assertTrue(true);
  }

  @Test
  public void testBalancedTagsWithNestedXML() {
    String test = "<xml chars <div> data</div> </xml>";
    try {
      for (char c : test.toCharArray()) {
        xmlParser.input(c);

      }
      fail();
    } catch (InvalidXMLException e) {
      e.getMessage();
    }
    assertTrue(true);
  }

  @Test
  public void testXMLWithNoInternalData() {
    String s;
    try {
      s = "<xml><div></div></xml>";
      for (char c : s.toCharArray()) {
        xmlParser.input(c);
      }
      assertTrue(true);
    } catch (InvalidXMLException e) {
      fail();
    }
  }

  @Test
  public void testNestedXMLWithNoInternalData() {
    String s;
    try {
      s = "<xml><div>friday</div></xml>";
      for (char c : s.toCharArray()) {
        xmlParser.input(c);
      }
      assertTrue(true);

    } catch (InvalidXMLException e) {
      fail();
    }
  }

  @Test
  public void testFailsWhenClosingOnUnemptyStack() {

    String s;
    StringBuilder sb = new StringBuilder();
    try {
      s = "<xml> <div> hello</xml>";
      for (char c : s.toCharArray()) {
        xmlParser.input(c);
        sb.append(c);
      }
      System.out.println("hello world");

      fail();
    } catch (InvalidXMLException e) {
      e.getMessage();
    }
    assertTrue(true);

    xmlParser = getParserInstance();
    try {
      s = "<xml> hello <div> </xml>";
      sb = new StringBuilder();
      for (char c : s.toCharArray()) {
        xmlParser.input(c);
        sb.append(c);
      }
      fail();
    } catch (InvalidXMLException e) {
      assertTrue(true);
    }
  }

  @Test
  public void testOutOfOrderTagClosings() {
    String[] strs = {"<xml> xml_stuff <div> x <span> </div>",
            "<xml2> xml_stuff <div> <span>x</div></span></xml2>",
            "<xml>  <div> <span>x</div></span></xml>)"};
    for (String s : strs) {
      try {
        xmlParser = getParserInstance();
        for (char c : s.toCharArray()) {
          xmlParser.input(c);
        }
        fail();
      } catch (InvalidXMLException e) {
        assertTrue(true);
      }
    }


  }

  @Test
  public void testAngleBracketsInCharData() {
    String[] strs = {"<xml <hello </xml>", "<xml </xml </xml>"};
    for (String s : strs) {
      try {
        for (char c : s.toCharArray()) {
          xmlParser.input(c);
        }
        fail();

      } catch (InvalidXMLException e) {
        e.getMessage();
      }
    }
    assertTrue(true);
  }

  @Test
  public void failedStatusTest() {
    String test = "<html><head><div></head>"
            + "<p></p><p></p></div></html>";

    StringBuilder sb = new StringBuilder();
    try {
      for (char c : test.toCharArray()) {
        this.xmlParser.input(c);
        sb.append(c);
      }
      fail("should have thrown exception");
    } catch (InvalidXMLException e) {
      System.out.println(e.getMessage());
      System.out.println(sb);
      assertTrue(true);
    }
  }


  private void invalidXMLCatcher(XMLParser parser, char c)
          throws IllegalStateException {
    try {
      parser.input(c);
      throw new IllegalStateException();
    } catch (InvalidXMLException e) {
      e.getMessage();
    }

  }

  protected XMLParser iterateThru(XMLParser x, String s) {
    StringBuilder sb = new StringBuilder();
    try {
      for (char c : s.toCharArray()) {
        x = x.input(c);
        sb.append(c);
      }
    } catch (InvalidXMLException e) {
      fail("Failure: "
              + e.getMessage()
              + "\tGot To "
              + sb
              .append(" out of ")
              .append(s)
              .toString());
    }
    return x;
  }

}