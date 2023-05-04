package test;

/**
 * This class represents a checked exception. This is used by state_design.XMLParser
 * implementations to signal invalid XML.
 */

public class InvalidXMLException extends Exception {
  public InvalidXMLException(String message) {
    super(message);
  }
}