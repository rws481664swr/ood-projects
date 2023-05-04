package test;

import java.util.Deque;
import java.util.LinkedList;

import static test.AbstractXMLParser.ReadState.CHAR;
import static test.AbstractXMLParser.ReadState.OPEN;

public abstract class AbstractXMLParser implements XMLParser {
  private String root = "-";
  private ReadState currentState;
  protected boolean hasStarted;
  protected boolean finished;
  private StringBuilder stringStateBuilder;

  protected final Deque<String> tagStack;

  protected AbstractXMLParser() {
    this.hasStarted = false;
    this.finished = false;
    currentState = ReadState.INIT;
    tagStack = new LinkedList<>();
    stringStateBuilder = new StringBuilder();
  }

  protected enum ReadState {
    INIT,
    CHAR,
    OPEN,
    CLOSE,
    FINAL;
  }

  protected void read(char c) throws InvalidXMLException {
    switch (c) {
      case '<':
        currentState = readOpen();
        break;
      case '>':
        currentState = readClose();
        break;
      case '/':
        currentState = readSlash();
        break;
      default:
        currentState = readChar(c);
        break;
    }
  }

  protected ReadState getState() {
    return currentState;
  }

  protected ReadState readChar(char c) throws InvalidXMLException {
    switch (currentState) {
      case INIT:
        if (c == ' ') {
          stringStateBuilder.append(c);
          return ReadState.INIT;
        } else {
          throw new InvalidXMLException("");
        }
      case OPEN:
      case CLOSE:
        if (isLegalTagChar(c)) {
          if (stringStateBuilder.length() == 1) {
            if (isNumber(c) || isLegalTagSymbol(c)) {
              throw new InvalidXMLException("Cannot start tag with " + c);
            }
          }
          stringStateBuilder.append(c);
          return currentState.equals(OPEN) ? OPEN : ReadState.CLOSE;
        } else {
          throw new InvalidXMLException("ILLEGAL CHARACTER FOR TAG");
        }
      case CHAR:
        if (isLegalChar(c)) {
          stringStateBuilder.append(c);
          return CHAR;
        } else {
          throw new InvalidXMLException("Illegal XML character.");
        }
    }
    throw new IllegalStateException("should not have reached");
  }

  private boolean isNumber(char c) {
    for (char a = '0'; a <= '9'; a++) {
      if (a == c) {
        return true;
      }
    }
    return false;
  }

  private boolean isLegalTagSymbol(char c) {
    char[] smybs = {':', '_', '-'};
    for (char a : smybs) {
      if (a == c) {
        return true;
      }
    }
    return false;
  }

  private boolean isLegalTagChar(char c) {
    for (char a = 'a'; a <= 'z'; a++) {
      if (a == c) {
        return true;
      }
    }

    for (char a = 'A'; a <= 'Z'; a++) {

      if (a == c) {
        return true;
      }
    }

    if (isNumber(c) || isLegalTagSymbol(c)) {
      return true;
    }

    return false;
  }

  private boolean isLegalChar(char c) {
    return true;
  }

  protected ReadState readSlash() throws InvalidXMLException {
    switch (currentState) {
      case INIT:
        throw new InvalidXMLException("Cannot read before tag start.");
      case OPEN:
        if (stringStateBuilder.length() == 1) {
          stringStateBuilder.append('/');
          return ReadState.CLOSE;
        } else {
          throw new InvalidXMLException("Cannot read / mid tag.");
        }
      case CLOSE:
        throw new InvalidXMLException("already read /");
      case CHAR:
        stringStateBuilder.append('/');
        return CHAR;
      case FINAL:
        throw new InvalidXMLException("Cannot read past close root tag");
    }
    throw new IllegalStateException("should be unreachable");

  }

  protected ReadState readOpen() throws InvalidXMLException {
    switch (currentState) {
      case INIT:
        stringStateBuilder.append('<');
        return OPEN;
      case OPEN:
      case CLOSE:
        throw new InvalidXMLException("cannot read < in a tag");
      case CHAR:
        stringStateBuilder.append('<');
        return OPEN;
      case FINAL:
        throw new InvalidXMLException("Cannot append to xml after closed root.");
    }
    throw new IllegalStateException("should be unreachable");
  }

  protected ReadState readClose() throws InvalidXMLException {
    switch (currentState) {
      case INIT:
        throw new InvalidXMLException("cannot read > at start");
      case OPEN:
        stringStateBuilder.append('>');
        this.tagStack.push(stringStateBuilder.toString().substring(0,stringStateBuilder.toString().length()-1));
      case CLOSE:
        break;
      case CHAR:
        break;
      case FINAL:
        break;
    }
    throw new IllegalStateException("should be unreachable");
  }
}
