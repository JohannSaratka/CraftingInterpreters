package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Lox {

	static boolean hadError = false;

	public static void main(String[] args) throws IOException {
		if (args.length > 1) {
			System.out.println("Usage: jlox [script]");
			System.exit(64); // EX_USAGE command was used incorrectly
		} else if (args.length == 1) {
			runFile(args[0]);
		} else {
			runPrompt();
		}
	}	
	
	/** 
	 * Read file from path and execute it 
	 * 
	 * @param path path to lox script file
	 */
	private static void runFile(String path) throws IOException{
		byte[] bytes = Files.readAllBytes(Paths.get(path));
		run(new String(bytes, Charset.defaultCharset()));
		
		// Indicate an error in the exit code
		if (hadError) System.exit(65); // EX_DATAERR input data was incorrect in some way
		
	}

	/**
	 * Interactive command prompt to enter and execute code one line at a time
	 * @throws IOException 
	 */
	private static void runPrompt() throws IOException {
		InputStreamReader input = new InputStreamReader(System.in);
		BufferedReader reader = new BufferedReader(input);
		
		for(;;) {
			System.out.print("> ");
			run(reader.readLine());
			hadError = false;
		}
		
	}
	
	private static void run(String source) {
	    Scanner scanner = new Scanner(source);    
	    List<Token> tokens = scanner.scanTokens();
	    Parser parser = new Parser(tokens);
	    Expr expressions = parser.parse();
	    
	    // Stop if there was a syntax error.
	    if (hadError) {
	    	return;
	    }
	    
	    System.out.println(new AstPrinter().print(expressions));
	}
	
	static void error(int line, String message) {
		report(line, "", message);
	}

	/**
	 * @param line
	 * @param where
	 * @param message
	 * 
	 * TODO Pretty error handling like this:
	 * Error: Unexpected "," in argument list.
	 *     15 | function(first, second,);
	 *                                ^-- Here.
	 */
	private static void report(int line, String where, String message) {
		System.err.println(
				"[line " + line + "] Error" + where + ": " + message);
		hadError  = true;		
	}
	
	static void error(Token token, String message){
		if (token.type == TokenType.EOF){
			report(token.line, " at end", message);
		} else {
			report(token.line, " at '" + token.lexeme + "'", message);
		}
	}
}
