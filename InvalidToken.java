
public class InvalidToken extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates specific message to be inputed for each specific error.
	 * @param message Inputed message. 
	 */
	public InvalidToken(String message) {
		super(message);
	}
	
}
