import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import state_design.InvalidXMLException;




public class AbstractXMLParser implements XMLParser {
  protected final Deque<String> tagStack;
  protected final List<String> xmlLog;
  private boolean started;
  private boolean finished;

  protected AbstractXMLParser() {
    this.tagStack = new LinkedList<>();
    this.xmlLog = new LinkedList<>();
    this.started = false;
    this.finished = false;
  }


  @Override
  public XMLParser input(char c) throws InvalidXMLException {
    switch (c) {
      case '<':
        break;
      case '>':
        break;
      case '/':
        break;
      default:
        break;
    }
  }

  @Override
  public String output() {
    return null;
  }
}
