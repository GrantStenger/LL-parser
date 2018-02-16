
public class Token<T> {
	
	public enum TOKEN_TYPE{
		ADDOP,
		SUBOP,
		MULTOP,
		DIVOP,
		MODOP,
		COP,
		LPAREN,
		RPAREN,
		LBRACKET,
		RBRACKET,
		QUOTES,
		INTEGER,
		DOUBLE,
		STRING,
		IDENTIFIER,
		EQUALS,
		END_TOKEN
	}

	public TOKEN_TYPE type;
	public T data;

	/**
	 * Returns the text representation of the Token.
	 * The format of the Token is <TOKENTYPE:VALUE>.
	 * The colon and VALUE only appear for Integers, Doubles, Strings and Identifiers.
	 */
	public String toString() {
		String rtn = "<" + type;
		if ((type == TOKEN_TYPE.DOUBLE || type == TOKEN_TYPE.INTEGER || type == TOKEN_TYPE.STRING || type == TOKEN_TYPE.IDENTIFIER) && data != null) {
				rtn += ":" + data.toString();
		}
		
		rtn += ">";
		return rtn;
	}
	
}
