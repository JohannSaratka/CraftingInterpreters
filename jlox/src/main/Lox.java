package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Lox {
	private static final Interpreter interpreter = new Interpreter();
	static boolean hadError = false;
	static boolean hadRuntimeError = false;
	
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
		if (hadError) {
			// EX_DATAERR input data was incorrect in some way
			System.exit(65); 
		}
		if (hadRuntimeError) {
			// EX_SOFTWARE An internal software error has been detected
			System.exit(70); 
		}
		
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
	    List<Stmt> statements = parser.parse();
	    
	    // Stop if there was a syntax error.
	    if (hadError) {
	    	return;
	    }
	    
	    interpreter.interpret(statements);
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

	static void runtimeError(RuntimeError error) {
		System.err.println(error.getMessage() + 
				"\n[line " + error.token.line + "]");
		hadRuntimeError = true;		
	}
}
