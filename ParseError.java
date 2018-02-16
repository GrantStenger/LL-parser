/**
 * ParseError throws parsing-specific errors.
 * @author Grant Stenger
 */
public class ParseError extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates specific message to be inputed for each specific error.
	 * @param message Inputed message. 
	 */
	public ParseError(String message) {
		
		super(message);
		
	}
	
}
