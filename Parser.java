import java.util.ArrayList;
import java.util.HashMap;

/**
 * This is the Parser which coupled with Tokenizer creates our LL1 Parser.
 * 
 * @author Grant Stenger
 */
public class Parser {

	private HashMap<String, Token<?>> IDs;
	private HashMap<String, Token<?>> temporaryIDs;
	
	/**
	 * Constructs Parser by creating a HashMap of identifiers and their values
	 */
	public Parser() {
		IDs = new HashMap<String, Token<?>>();
	}
	
	/**
	 * Implements a LL1 Parser by parsing the given ArrayList of tokens and evaluates the expression. 
	 * The logic of evaluating the expression is based off the context free grammar including Expressions, Terms, and Factors.
	 * After evaluating the expression, Parse returns the result to Main.
	 * 
	 * @param tokenArray is the array of tokens created by Tokenizer
	 * @return result is the token representing the evaluation of the tokens in tokenArray
	 * @throws ParseError is a custom error that is thrown when specific errors occur
	 * @throws IDNotFoundException is an error that is thrown when identifiers are used without being declared yet.
	 */
	public Token<?> Parse(ArrayList<Token<?>> tokenArray) throws ParseError, IDNotFoundException {
		
		temporaryIDs = new HashMap<String, Token<?>>();
		temporaryIDs.putAll(IDs);
		
		Token<String> end = new Token<String>();
		end.data = "End";
		end.type = Token.TOKEN_TYPE.END_TOKEN;
		tokenArray.add( 0, end );

		Token<?> result = parseExpr(tokenArray);
		
		if (tokenArray.get(tokenArray.size() - 1).type == Token.TOKEN_TYPE.EQUALS) {
			if (tokenArray.get(tokenArray.size() - 1).type != Token.TOKEN_TYPE.IDENTIFIER) {
				throw new ParseError("Error: Can only change the values of identifiers");
			}
			else {
				parseStatement(tokenArray, result);
			}
		}
		
		if (result.data == "End") {
			throw new ParseError("Parse Error: Nothing entered on line");
		}
		else if (tokenArray.size() > 1) {
			throw new ParseError("Parse Error: Missing operator");
		}
		else {
			IDs = temporaryIDs;
			return result;
		}

	}
	
	private void parseStatement(ArrayList<Token<?>> tokenArray, Token<?> expression) throws ParseError {
		
		tokenArray.remove(tokenArray.size()-1);
		
		if (temporaryIDs.containsKey(tokenArray.get(tokenArray.size() - 1))) {
			temporaryIDs.remove(tokenArray.get(tokenArray.size() - 1));
			temporaryIDs.put((String) tokenArray.get(tokenArray.size() - 1).data, expression);
		}
		else {
			temporaryIDs.put((String) tokenArray.get(tokenArray.size() - 1).data, expression);
		}
		
		tokenArray.remove(tokenArray.size()-1);
		
	}

	private Token<?> parseExpr(ArrayList<Token<?>> tokenArray) throws ParseError, IDNotFoundException {

		Token<?> right = parseTerm(tokenArray);
		
		if (right.type == Token.TOKEN_TYPE.IDENTIFIER) {
			right = temporaryIDs.get(right.data);
		}

		Token<?> operator = tokenArray.get(tokenArray.size()-1);
		
		if (operator.type == Token.TOKEN_TYPE.ADDOP || operator.type == Token.TOKEN_TYPE.SUBOP) {

			tokenArray.remove(tokenArray.size()-1);
			
			Token<?> left = parseExpr(tokenArray);
			
			if (left.type == Token.TOKEN_TYPE.IDENTIFIER) {
				left = temporaryIDs.get(left.data);
			}
			
			if (operator.type == Token.TOKEN_TYPE.ADDOP) {
				if (right == null) {
					throw new ParseError("Parse Error: ADDOP requires two operands");
				}
				else {
					if (left.type == Token.TOKEN_TYPE.STRING || right.type == Token.TOKEN_TYPE.STRING) {
						String resultString = "" + left.data.toString() + right.data.toString();
						Token<String> result = new Token<String>();
						result.data = resultString;
						result.type = Token.TOKEN_TYPE.STRING;
						right = result;
					}
					else if (left.type == Token.TOKEN_TYPE.DOUBLE && right.type == Token.TOKEN_TYPE.DOUBLE) {
						double resultInt = ((double) left.data) + ((double) right.data);
						Token<Double> result = new Token<Double>();
						result.data = resultInt;
						result.type = Token.TOKEN_TYPE.DOUBLE;
						right = result;
					}
					else if (left.type == Token.TOKEN_TYPE.INTEGER && right.type == Token.TOKEN_TYPE.DOUBLE) {
						double resultInt = ((Integer) left.data) + ((double) right.data);
						Token<Double> result = new Token<Double>();
						result.data = resultInt;
						result.type = Token.TOKEN_TYPE.DOUBLE;
						right = result;
					}
					else if (left.type == Token.TOKEN_TYPE.DOUBLE && right.type == Token.TOKEN_TYPE.INTEGER) {
						double resultInt = ((double) left.data) + ((Integer) right.data);
						Token<Double> result = new Token<Double>();
						result.data = resultInt;
						result.type = Token.TOKEN_TYPE.DOUBLE;
						right = result;
					}
					else if (left.type == Token.TOKEN_TYPE.IDENTIFIER && right.type == Token.TOKEN_TYPE.INTEGER) {
						Integer resultInt = ((Integer) temporaryIDs.get(left.data).data) + ((Integer) right.data);
						Token<Integer> result = new Token<Integer>();
						result.data = resultInt;
						result.type = Token.TOKEN_TYPE.INTEGER;
						right = result;
					}
					else if (left.type == Token.TOKEN_TYPE.INTEGER && right.type == Token.TOKEN_TYPE.IDENTIFIER) {
						Integer resultInt = ((Integer) left.data) + ((Integer) temporaryIDs.get(right.data).data);
						Token<Integer> result = new Token<Integer>();
						result.data = resultInt;
						result.type = Token.TOKEN_TYPE.INTEGER;
						right = result;
					}
					else if (left.type == Token.TOKEN_TYPE.IDENTIFIER && right.type == Token.TOKEN_TYPE.IDENTIFIER) {
						Integer resultInt = ((Integer) temporaryIDs.get(left.data).data) + ((Integer) temporaryIDs.get(right.data).data);
						Token<Integer> result = new Token<Integer>();
						result.data = resultInt;
						result.type = Token.TOKEN_TYPE.INTEGER;
						right = result;
					}
					else {
						Integer resultInt = ((Integer) left.data) + ((Integer) right.data);
						Token<Integer> result = new Token<Integer>();
						result.data = resultInt;
						result.type = Token.TOKEN_TYPE.INTEGER;
						right = result;
					}
				}
			}
			else if (operator.type == Token.TOKEN_TYPE.SUBOP) {
				if (right == null) {
					throw new ParseError("Parse Error: SUBOP requires two operands");
				}
				else {
					if (left.type == Token.TOKEN_TYPE.STRING || right.type == Token.TOKEN_TYPE.STRING) {
						throw new ParseError("Parse Error: Can't Subtract Strings");
					}

					else if (left.type == Token.TOKEN_TYPE.DOUBLE && right.type == Token.TOKEN_TYPE.DOUBLE) {
						double resultDouble = ((double) left.data) - ((double) right.data);
						Token<Double> result = new Token<Double>();
						result.data = resultDouble;
						result.type = Token.TOKEN_TYPE.DOUBLE;
						right = result;
					}
					else if (left.type == Token.TOKEN_TYPE.INTEGER && right.type == Token.TOKEN_TYPE.DOUBLE) {
						double resultDouble = ((Integer) left.data) - ((double) right.data);
						Token<Double> result = new Token<Double>();
						result.data = resultDouble;
						result.type = Token.TOKEN_TYPE.DOUBLE;
						right = result;
					}
					else if (left.type == Token.TOKEN_TYPE.DOUBLE && right.type == Token.TOKEN_TYPE.INTEGER) {
						double resultDouble = ((double) left.data) - ((Integer) right.data);
						Token<Double> result = new Token<Double>();
						result.data = resultDouble;
						result.type = Token.TOKEN_TYPE.DOUBLE;
						right = result;
					}
					else if (left.type == Token.TOKEN_TYPE.IDENTIFIER && right.type == Token.TOKEN_TYPE.INTEGER) {
						Integer resultInt = ((Integer) temporaryIDs.get(left.data).data) - ((Integer) right.data);
						Token<Integer> result = new Token<Integer>();
						result.data = resultInt;
						result.type = Token.TOKEN_TYPE.INTEGER;
						right = result;
					}
					else if (left.type == Token.TOKEN_TYPE.INTEGER && right.type == Token.TOKEN_TYPE.IDENTIFIER) {
						Integer resultInt = ((Integer) left.data) - ((Integer) temporaryIDs.get(right.data).data);
						Token<Integer> result = new Token<Integer>();
						result.data = resultInt;
						result.type = Token.TOKEN_TYPE.INTEGER;
						right = result;
					}
					else if (left.type == Token.TOKEN_TYPE.IDENTIFIER && right.type == Token.TOKEN_TYPE.IDENTIFIER) {
						Integer resultInt = ((Integer) temporaryIDs.get(left.data).data) - ((Integer) temporaryIDs.get(right.data).data);
						Token<Integer> result = new Token<Integer>();
						result.data = resultInt;
						result.type = Token.TOKEN_TYPE.INTEGER;
						right = result;
					}
					else {
						Integer resultInt = ((Integer) left.data) - ((Integer) right.data);
						Token<Integer> result = new Token<Integer>();
						result.data = resultInt;
						result.type = Token.TOKEN_TYPE.INTEGER;
						right = result;
					}
				}
			}
		}

		return right;

	}

	private Token<?> parseTerm(ArrayList<Token<?>> tokenArray) throws ParseError, IDNotFoundException {

		Token<?> right = parseFactor(tokenArray);
		
		if (right.type == Token.TOKEN_TYPE.IDENTIFIER) {
			right = temporaryIDs.get(right.data);
		}

		Token<?> operator = tokenArray.get(tokenArray.size()-1);

		if (operator.type == Token.TOKEN_TYPE.MULTOP || operator.type == Token.TOKEN_TYPE.DIVOP || operator.type == Token.TOKEN_TYPE.MODOP) {
			tokenArray.remove(tokenArray.size()-1);
			Token<?> left = parseTerm(tokenArray);
			if (left.type == Token.TOKEN_TYPE.IDENTIFIER) {
				left = temporaryIDs.get(left.data);
			}
			if (operator.type == Token.TOKEN_TYPE.MULTOP) {
				if (right == null) {
					throw new ParseError("Parse Error: MULTOP requires two operands");
				}
				else {
					if (left.type == Token.TOKEN_TYPE.STRING && right.type == Token.TOKEN_TYPE.INTEGER) {
						String resultString = "";
						for (int i = 0; i < ((int) right.data); i++) {
							resultString += (String) left.data;
						}
						Token<String> result = new Token<String>();
						result.data = resultString;
						result.type = Token.TOKEN_TYPE.STRING;
						right = result;
					}
					else if (left.type == Token.TOKEN_TYPE.INTEGER && right.type == Token.TOKEN_TYPE.STRING) {
						String resultString = "";
						for (int i = 0; i < ((int) left.data); i++) {
							resultString += (String) right.data;
						}
						Token<String> result = new Token<String>();
						result.data = resultString;
						result.type = Token.TOKEN_TYPE.STRING;
						right = result;
					}
					else if (left.type == Token.TOKEN_TYPE.INTEGER && right.type == Token.TOKEN_TYPE.INTEGER) {
						Integer resultInt = ((Integer) left.data) * ((Integer) right.data);
						Token<Integer> result = new Token<Integer>();
						result.data = resultInt;
						result.type = Token.TOKEN_TYPE.INTEGER;
						right = result;
					}
					else if (left.type == Token.TOKEN_TYPE.DOUBLE && right.type == Token.TOKEN_TYPE.DOUBLE) {
						double resultDouble = ((double) left.data) * ((double) right.data);
						Token<Double> result = new Token<Double>();
						result.data = resultDouble;
						result.type = Token.TOKEN_TYPE.DOUBLE;
						right = result;
					}
					else if (left.type == Token.TOKEN_TYPE.INTEGER && right.type == Token.TOKEN_TYPE.DOUBLE) {
						double resultDouble = ((Integer) left.data) * ((double) right.data);
						Token<Double> result = new Token<Double>();
						result.data = resultDouble;
						result.type = Token.TOKEN_TYPE.DOUBLE;
						right = result;
					}
					else if (left.type == Token.TOKEN_TYPE.DOUBLE && right.type == Token.TOKEN_TYPE.INTEGER) {
						double resultDouble = ((double) left.data) * ((Integer) right.data);
						Token<Double> result = new Token<Double>();
						result.data = resultDouble;
						result.type = Token.TOKEN_TYPE.DOUBLE;
						right = result;
					}
					else if ((left.type == Token.TOKEN_TYPE.STRING && right.type == Token.TOKEN_TYPE.DOUBLE) || (left.type == Token.TOKEN_TYPE.DOUBLE && right.type == Token.TOKEN_TYPE.STRING)) {
						throw new ParseError("Parse Error: Cannot multiply Strings and Doubles");
					}
					else {
						throw new ParseError("Parse Error: Can't multiply strings like that");
					}
				}
			}
			else if (operator.type == Token.TOKEN_TYPE.DIVOP) {
				if (right == null) {
					throw new ParseError("Parse Error: DIVOP requires two operands");
				}
				else {
					if (left.type == Token.TOKEN_TYPE.STRING || right.type == Token.TOKEN_TYPE.STRING) {
						throw new ParseError("Parse Error: Strings cannot be divided");
					}
					else if (right.data.equals(0)) {
						throw new ParseError("Parse Error: Dividing by zero");
					}
					else if (left.type == Token.TOKEN_TYPE.DOUBLE && right.type == Token.TOKEN_TYPE.DOUBLE) {
						double resultDouble = ((double) left.data) / ((double) right.data);
						Token<Double> result = new Token<Double>();
						result.data = resultDouble;
						result.type = Token.TOKEN_TYPE.DOUBLE;
						right = result;
					}
					else if (left.type == Token.TOKEN_TYPE.INTEGER && right.type == Token.TOKEN_TYPE.DOUBLE) {
						double resultDouble = ((Integer) left.data) / ((double) right.data);
						Token<Double> result = new Token<Double>();
						result.data = resultDouble;
						result.type = Token.TOKEN_TYPE.DOUBLE;
						right = result;
					}
					else if (left.type == Token.TOKEN_TYPE.DOUBLE && right.type == Token.TOKEN_TYPE.INTEGER) {
						double resultDouble = ((double) left.data) / ((Integer) right.data);
						Token<Double> result = new Token<Double>();
						result.data = resultDouble;
						result.type = Token.TOKEN_TYPE.DOUBLE;
						right = result;
					}
					else {
						Integer resultInt = ((Integer) left.data) / ((Integer) right.data);
						Token<Integer> result = new Token<Integer>();
						result.data = resultInt;
						result.type = Token.TOKEN_TYPE.INTEGER;
						right = result;
					}
				}
			}
			else if (operator.type == Token.TOKEN_TYPE.MODOP) {
				if (right == null) {
					throw new ParseError("Parse Error: MODOP requires two operands");
				}
				else {
					if (left.type == Token.TOKEN_TYPE.STRING || right.type == Token.TOKEN_TYPE.STRING) {
						throw new ParseError("Parse Error: Strings don't work with MOD");
					}
					else if (right.data.equals(0)) {
						throw new ParseError("Parse Error: Anything MOD 0 is undefined");
					}
					else if (left.type == Token.TOKEN_TYPE.DOUBLE && right.type == Token.TOKEN_TYPE.DOUBLE) {
						double resultDouble = ((double) left.data) % ((double) right.data);
						Token<Double> result = new Token<Double>();
						result.data = resultDouble;
						result.type = Token.TOKEN_TYPE.DOUBLE;
						right = result;
					}
					else if (left.type == Token.TOKEN_TYPE.INTEGER && right.type == Token.TOKEN_TYPE.DOUBLE) {
						double resultDouble = ((Integer) left.data) % ((double) right.data);
						Token<Double> result = new Token<Double>();
						result.data = resultDouble;
						result.type = Token.TOKEN_TYPE.DOUBLE;
						right = result;
					}
					else if (left.type == Token.TOKEN_TYPE.DOUBLE && right.type == Token.TOKEN_TYPE.INTEGER) {
						double resultDouble = ((double) left.data) % ((Integer) right.data);
						Token<Double> result = new Token<Double>();
						result.data = resultDouble;
						result.type = Token.TOKEN_TYPE.DOUBLE;
						right = result;
					}
					else {
						Integer resultInt = ((Integer) left.data) % ((Integer) right.data);
						Token<Integer> result = new Token<Integer>();
						result.data = resultInt;
						result.type = Token.TOKEN_TYPE.INTEGER;
						right = result;
					}
				}
			}
		}

		return right;

	}

	private Token<?> parseFactor( ArrayList<Token<?>> tokenArray) throws ParseError, IDNotFoundException {

		Token<?> token = tokenArray.get(tokenArray.size()-1);
		
		if (token.type == Token.TOKEN_TYPE.STRING || token.type == Token.TOKEN_TYPE.INTEGER || token.type == Token.TOKEN_TYPE.DOUBLE) {
			tokenArray.remove(tokenArray.size()-1);
		}
		else if (token.type == Token.TOKEN_TYPE.RPAREN) {
			tokenArray.remove(tokenArray.size()-1);
			Token<?> parenVal = parseExpr(tokenArray);
			if (tokenArray.get(tokenArray.size()-1).type == Token.TOKEN_TYPE.LPAREN) {
				tokenArray.remove(tokenArray.size()-1);
				return parenVal;
			}
			else {
				throw new ParseError("Parse Error: Unequal Parenthesis");
			}
		} 
		else if (token.type == Token.TOKEN_TYPE.ADDOP || token.type == Token.TOKEN_TYPE.SUBOP || token.type == Token.TOKEN_TYPE.MULTOP || token.type == Token.TOKEN_TYPE.DIVOP) {
			throw new ParseError("Parse Error: Improper usage of operators");
		}
		else if (token.type == Token.TOKEN_TYPE.IDENTIFIER) {
			tokenArray.remove(tokenArray.size()-1);
			if (temporaryIDs.containsKey(token.data)) {
				
			}
			else {
				throw new IDNotFoundException("Error: ID Not Found");
			}
		}
		else {
			throw new ParseError("Parse Error: Improper Factor");
		}

		return token;

	}

}