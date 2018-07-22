package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Lox {

	public static void main(String[] args) throws IOException {
		if (args.length > 1) {
			System.out.println("Usage: jlox [script]");
			System.exit(64); // command was used incorrectly
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
		}
		
	}
	
	private static void run(String source) {
		// TODO Auto-generated method stub
		
	}

}
