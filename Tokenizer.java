import java.util.ArrayList;

public class Tokenizer {

	private ArrayList<Token<?>> tokenArray;

	/**
	 * Constructs the Tokenizer with an array for future tokens.
	 */
	public Tokenizer() {
		tokenArray = new ArrayList<Token<?>>();
	}

	/**
	 * Loads strings onto end of Tokenizer
	 * @param str The String containing the line of text typed onto the console.
	 * @throws InvalidToken An exception customized for specific instances (e.g. Int too large or unclosed quotes)
	 */
	public void load(String str) throws InvalidToken{

		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) >= '0' && str.charAt(i) <= '9'){
				try {
					i = createInteger(str, i);
				} catch (InvalidToken e) {
					tokenArray.clear();
					throw e;
				}
			}
			else if (str.charAt(i) == '\"'){
				try {
					i = createQuote(str, i);
				} catch (InvalidToken e) {
					tokenArray.clear();
					throw e;
				}
			}
			else if (str.charAt(i) == '+'){
				if (i == str.length() - 1 || str.charAt(i+1) == ' ') {
					try {
						i = createAddop(str, i);
					} catch (InvalidToken e) {
						tokenArray.clear();
						throw e;
					}
				}
				else {
					tokenArray.clear();
					throw new InvalidToken("Invalid Token: No space after AddOp");
				}
			}
			else if (str.charAt(i) == '-'){
				if (i == str.length() - 1 || str.charAt(i+1) == ' ') {
					try {
						i = createSubop(str, i);
					} catch (InvalidToken e) {
						tokenArray.clear();
						throw e;
					}
				}
				else {
					tokenArray.clear();
					throw new InvalidToken("Invalid Token: No space after SubOp");
				}
			}
			else if (str.charAt(i) == '*'){
				if (i == str.length() - 1 || str.charAt(i+1) == ' ') {
					try {
						i = createMultop(str, i);
					} catch (InvalidToken e) {
						tokenArray.clear();
						throw e;
					}
				}
				else {
					tokenArray.clear();
					throw new InvalidToken("Invalid Token: No space after MultOp");
				}
			}
			else if (str.charAt(i) == '/'){
				if (i == str.length() - 1 || str.charAt(i+1) == ' ') {
					try {
						i = createDivop(str, i);
					} catch (InvalidToken e) {
						tokenArray.clear();
						throw e;
					}
				}
				else {
					tokenArray.clear();
					throw new InvalidToken("Invalid Token: No space after DivOp");
				}
			}
			else if (str.charAt(i) == '%'){
				if (i == str.length() - 1 || str.charAt(i+1) == ' ') {
					try {
						i = createModop(str, i);
					} catch (InvalidToken e) {
						tokenArray.clear();
						throw e;
					}
				}
				else {
					tokenArray.clear();
					throw new InvalidToken("Invalid Token: No space after ModOp");
				}
			}
			else if (str.charAt(i) == ','){
				if (i == str.length() - 1 || str.charAt(i+1) == ' ') {
					try {
						i = createCop(str, i);
					} catch (InvalidToken e) {
						tokenArray.clear();
						throw e;
					}
				}
				else {
					tokenArray.clear();
					throw new InvalidToken("Invalid Token: No space after COp");
				}
			}
			else if (str.charAt(i) == '('){
				try {
					i = createLParen(str, i);
				} catch (InvalidToken e) {
					tokenArray.clear();
					throw e;
				}
			}
			else if (str.charAt(i) == ')'){
				if (i == str.length() - 1 || str.charAt(i+1) == ' ') {
					try {
						i = createRParen(str, i);
					} catch (InvalidToken e) {
						tokenArray.clear();
						throw e;
					}
				}
				else {
					tokenArray.clear();
					throw new InvalidToken("Invalid Token: No space after RParen");
				}
			}
			else if (str.charAt(i) == '['){
				if (i == str.length() - 1 || str.charAt(i+1) == ' ') {
					try {
						i = createLBracket(str, i);
					} catch (InvalidToken e) {
						tokenArray.clear();
						throw e;
					}
				}
			}
			else if (str.charAt(i) == ']'){
				if (i == str.length() - 1 || str.charAt(i+1) == ' ') {
					try {
						i = createRBracket(str, i);
					} catch (InvalidToken e) {
						tokenArray.clear();
						throw e;
					}
				}
				else {
					tokenArray.clear();
					throw new InvalidToken("Invalid Token: No space after RBracket");
				}
			}
			else if ((str.charAt(i) >= 'A' && str.charAt(i) <= 'Z') || (str.charAt(i) >= 'a' && str.charAt(i) <= 'z')){
				try {
					i = createIdentifier(str, i);
				} catch (InvalidToken e) {
					tokenArray.clear();
					throw e;
				}
			}
			else if (str.charAt(i) == '='){
				if (i == str.length() - 1 || str.charAt(i+1) == ' ') {
					try {
						i = createEq(str, i);
					} catch (InvalidToken e) {
						tokenArray.clear();
						throw e;
					}
				}
				else {
					tokenArray.clear();
					throw new InvalidToken("Invalid Token: No space after SubOp");
				}
			}
			else if (str.charAt(i) >= ' '){
				
			}
			else {
				tokenArray.clear();
				throw new InvalidToken("Invalid Token: Unknown Symbol");
			}

		}

	}

	private int createInteger(String str, int pos) throws InvalidToken {

		String token2b = "";

		boolean stillInt = true;
		while (stillInt && pos < str.length()) {
			if (str.charAt(pos) >= '0' && str.charAt(pos) <= '9') {
				token2b += str.charAt(pos);
				pos += 1;
			}
			else {
				if (str.charAt(pos) == ' ') {
					stillInt = false;
				}
				else if (str.charAt(pos) == ',') {
					pos -= 1;
					stillInt = false;
				}
				else if (str.charAt(pos) == '.') {
					pos = createDouble(str, pos, token2b);
					return pos;
				}
				else {
					tokenArray.clear();
					throw new InvalidToken("Invalid Integer");
				}
			}
		}

		try {
			Token<Integer> token = new Token<Integer>();
			token.data = Integer.parseInt(token2b);
			token.type = Token.TOKEN_TYPE.INTEGER;
			tokenArray.add(token);
		}
		catch(Exception e){
			tokenArray.clear();
			throw new InvalidToken("Integer Too Big: " + token2b);
		}

		return pos;

	}

	private int createDouble(String str, int pos, String token2b) throws InvalidToken {

		token2b += ".";

		boolean stillDoub = true;
		pos += 1;
		while (stillDoub && pos < str.length()) {
			if (str.charAt(pos) >= '0' && str.charAt(pos) <= '9') {
				token2b += str.charAt(pos);
				pos += 1;
			}
			else if (str.charAt(pos) == ' ') {
				stillDoub = false;
			}
			else {
				tokenArray.clear();
				throw new InvalidToken("Invalid Double");
			}
		}


		Token<Double> token = new Token<Double>();
		token.data = Double.parseDouble(token2b);
		token.type = Token.TOKEN_TYPE.DOUBLE;
		tokenArray.add(token);
		return pos;

	}

	private int createQuote(String str, int pos) throws InvalidToken{

		String token2b = "";

		boolean inQuote = true;
		pos += 1;
		if (pos == str.length()) {
			tokenArray.clear();
			throw new InvalidToken("Unclosed Quote");
		}
		else {
			while (inQuote && pos < str.length()) {
				if (str.charAt(pos) == '\"') {
					inQuote = false;
				}
				else if (pos == str.length() - 1) {
					tokenArray.clear();
					throw new InvalidToken("Unclosed Quote");
				}
				else {
					token2b += str.charAt(pos);
					pos += 1;
				}
			}
		}


		Token<String> token = new Token<String>();
		token.data = token2b;
		token.type = Token.TOKEN_TYPE.STRING;
		tokenArray.add(token);
		return pos;

	}

	private int createAddop(String str, int pos) throws InvalidToken{

		Token<String> token = new Token<String>();
		token.data = "+";
		token.type = Token.TOKEN_TYPE.ADDOP;
		tokenArray.add(token);
		return pos;
	}

	private int createSubop(String str, int pos) throws InvalidToken{

		Token<String> token = new Token<String>();
		token.data = "-";
		token.type = Token.TOKEN_TYPE.SUBOP;
		tokenArray.add(token);
		return pos;

	}

	private int createMultop(String str, int pos) throws InvalidToken{

		Token<String> token = new Token<String>();
		token.data = "*";
		token.type = Token.TOKEN_TYPE.MULTOP;
		tokenArray.add(token);
		return pos;
	}

	private int createDivop(String str, int pos) throws InvalidToken{

		Token<String> token = new Token<String>();
		token.data = "/";
		token.type = Token.TOKEN_TYPE.DIVOP;
		tokenArray.add(token);
		return pos;

	}

	private int createModop(String str, int pos) throws InvalidToken{

		Token<String> token = new Token<String>();
		token.data = "%";
		token.type = Token.TOKEN_TYPE.MODOP;
		tokenArray.add(token);
		return pos;

	}
	
	private int createEq(String str, int pos) throws InvalidToken{

		Token<String> token = new Token<String>();
		token.data = "=";
		token.type = Token.TOKEN_TYPE.EQUALS;
		tokenArray.add(token);
		return pos;

	}

	private int createCop(String str, int pos) throws InvalidToken{

		Token<String> token = new Token<String>();
		token.data = ",";
		token.type = Token.TOKEN_TYPE.COP;
		tokenArray.add(token);
		return pos;

	}

	private int createLParen(String str, int pos) throws InvalidToken{

		Token<String> token = new Token<String>();
		token.data = "(";
		token.type = Token.TOKEN_TYPE.LPAREN;
		tokenArray.add(token);

		return pos;

	}

	private int createRParen(String str, int pos) throws InvalidToken{

		Token<String> token = new Token<String>();
		token.data = ")";
		token.type = Token.TOKEN_TYPE.RPAREN;
		tokenArray.add(token);
		return pos;

	}

	private int createLBracket(String str, int pos) throws InvalidToken{

		Token<String> token = new Token<String>();
		token.data = "[";
		token.type = Token.TOKEN_TYPE.LBRACKET;
		tokenArray.add(token);
		return pos;

	}

	private int createRBracket(String str, int pos) throws InvalidToken{

		Token<String> token = new Token<String>();
		token.data = "]";
		token.type = Token.TOKEN_TYPE.RBRACKET;
		tokenArray.add(token);
		return pos;

	}

	private int createIdentifier(String str, int pos) throws InvalidToken {

		String token2b = "";

		boolean stillWord = true;
		while (stillWord && pos < str.length()) {
			if ((str.charAt(pos) >= 'A' && str.charAt(pos) <= 'Z') || (str.charAt(pos) >= 'a' && str.charAt(pos) <= 'z') || (str.charAt(pos) >= '0' && str.charAt(pos) <= '9')) {
				token2b += str.charAt(pos);
				pos += 1;
			}
			else {
				if (str.charAt(pos) == ' ') {
					stillWord = false;
				}
				else if (str.charAt(pos) == ',') {
					pos -= 1;
					stillWord = false;
				}
				else {
					tokenArray.clear();
					throw new InvalidToken("Invalid Identifier");
				}
			}
		}

		Token<String> token = new Token<String>();
		token.data = token2b;
		token.type = Token.TOKEN_TYPE.IDENTIFIER;
		tokenArray.add(token);
		return pos;

	}
	
	/**
	 * Returns next token in the array (the first one loaded into the tokenizer since last clearing).
	 * Removes token from array after returning it. 
	 * @return Next token in the Array
	 */
	public Token<?> nextToken() {

		Token<?> nextToken = new Token<>();
		nextToken = tokenArray.get(0);
		tokenArray.remove(0);
		return nextToken;

	}

	/**
	 * Returns an ArrayList of all the tokens currently in Tokenizer's array.
	 * Clears token array. 
	 * @return All tokens that were just in tokenArray
	 */
	public ArrayList<Token<?>> allTokens()  {
		ArrayList<Token<?>> rtn = new ArrayList<Token<?>>();
		rtn.addAll(tokenArray);
		tokenArray.clear();
		return rtn; 
	}

}
