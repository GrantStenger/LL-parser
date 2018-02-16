/**
 * ParseError throws specific, ID-existence-related parsing errors.
 * @author Grant Stenger
 */
public class IDNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates specific message to be inputed for each specific error.
	 * @param message Inputed message. 
	 */
	public IDNotFoundException(String message) {
		
		super(message);
		
	}
	
}