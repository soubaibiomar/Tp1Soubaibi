
package ma.emsi.soubaibi.tp1.llm;

public class RequeteException extends Exception {

    public RequeteException(String message) {
        super(message);
    }

    public RequeteException(String message, String requeteJson) {
        super(message + "\nRequete JSON :\n" + requeteJson);
    }

    public RequeteException(String message, Throwable cause) {
        super(message, cause);
    }
}
