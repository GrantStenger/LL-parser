import java.util.ArrayList;
import java.util.Scanner;

/**
 * Creates a new Tokenizer object, creates a scanner, and performs
 * listening, loading of text, and returning of newly created Tokens.
 * Then, a Parser is created which evaluates the content loaded into Tokenizer. 
 * The result of this evaluation is then printed, and the user is prompted for another line of input.
 * 
 * @author Home
 */
public class Main {
	
	public static void main( String[] args )  {
		
		Tokenizer tokenizer = new Tokenizer();
		Parser parser = new Parser();

		Scanner scanner = new Scanner(System.in);
		System.out.println("Input: ");
		String line = "";

		while (scanner.hasNextLine())
		{
			line = scanner.nextLine();
			try {

				tokenizer.load(line);

				ArrayList<Token<?>> tokens = tokenizer.allTokens();
				Token<?> result = parser.Parse(tokens);
				System.out.println(result.data);
				System.out.println();
			}
			catch( InvalidToken e ) {
				System.err.println( e.getMessage() ) ;
			}
			catch( ParseError e ) {
				System.err.println( e.getMessage() );
			}
			catch( IDNotFoundException e ) {
				System.err.println( e.getMessage() );
			}
		}
		
		scanner.close();

	}
}

