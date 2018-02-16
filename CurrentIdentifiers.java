import java.util.HashMap;

public class CurrentIdentifiers {

	private HashMap<Token<?>, Token<?>> IDs;
	
	public CurrentIdentifiers() {
		IDs = new HashMap<Token<?>, Token<?>>();
	}
	
	private void addID(Token<?> identifier, Token<?> expression) {
		IDs.put(identifier, expression);
	}
	
	public Token<?> getID(Token<?> identifier) {
		return IDs.get(identifier);
	}
	
}
