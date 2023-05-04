package test;

public class XMLValidator extends AbstractXMLParser {




  public XMLValidator() {

  }

  @Override
  protected ReadState readChar(char c) {
    return null;
  }

  @Override
  protected ReadState readSlash() {
    switch (getState()){

    }
    return null;
  }

  @Override
  protected ReadState readOpen() {
    return null;
  }

  @Override
  protected ReadState readClose() {
    return null;
  }


  @Override
  public XMLParser input(char c) throws InvalidXMLException {
    hasStarted = true;
    read(c);

    return this;
  }

  @Override
  public String output() {
    if (!hasStarted) {
      return "Status:Empty";
    } else if (finished) {
      return "Status:Valid";
    } else {
      return "Status:Incomplete";
    }
  }


}
