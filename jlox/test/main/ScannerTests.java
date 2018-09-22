package main;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import org.junit.Test;

import junit.framework.TestCase;
import main.Scanner;
import main.Token;
import main.TokenType;

public class ScannerTests extends TestCase {
	private ByteArrayOutputStream errorContent = new ByteArrayOutputStream();
	
	@Override
	protected void setUp() throws Exception {
	    System.setErr(new PrintStream(errorContent));
		super.setUp();
	}
	
	@Override
	protected void tearDown() throws Exception {
		System.setErr(System.err);
		super.tearDown();
	}
	
	private boolean hasErrors(){
		return (errorContent.size() != 0);
	}
	private void shouldReturnSingleValidToken( String lexeme, TokenType expectedType){
		Scanner scan = new Scanner(lexeme);
		List<Token> tokenList = scan.scanTokens();
		assertEquals(2, tokenList.size()); // returns expected token and EOF
		assertEquals(expectedType, tokenList.get(0).type);
		assertFalse(hasErrors());
	}
	
	private void invalidTokenShouldThrowError( String lexeme){
		Scanner scan = new Scanner(lexeme);
		scan.scanTokens();		
	    //check if there was any error reported
	    assertTrue( hasErrors());
	}
	
	@Test
	public void testScanTokens_invalidCharacterIsReported() {
		invalidTokenShouldThrowError("@");
	}
	
	@Test
	public void testScanTokens_singleCharacterIsValid() {
		shouldReturnSingleValidToken("(", TokenType.LEFT_PAREN);
		shouldReturnSingleValidToken(")", TokenType.RIGHT_PAREN);
		shouldReturnSingleValidToken("{", TokenType.LEFT_BRACE);
		shouldReturnSingleValidToken("}", TokenType.RIGHT_BRACE);
		shouldReturnSingleValidToken(",", TokenType.COMMA);
		shouldReturnSingleValidToken(".", TokenType.DOT);
		shouldReturnSingleValidToken("-", TokenType.MINUS);
		shouldReturnSingleValidToken("+", TokenType.PLUS);
		shouldReturnSingleValidToken(";", TokenType.SEMICOLON);
		shouldReturnSingleValidToken("*", TokenType.STAR);
		shouldReturnSingleValidToken("/", TokenType.SLASH);
	}
	@Test
	public void testScanTokens_ReservedWordsIsValid() {
		shouldReturnSingleValidToken("and", TokenType.AND);
		shouldReturnSingleValidToken("class", TokenType.CLASS);
		shouldReturnSingleValidToken("else", TokenType.ELSE);
		shouldReturnSingleValidToken("false", TokenType.FALSE);
		shouldReturnSingleValidToken("for", TokenType.FOR);
		shouldReturnSingleValidToken("fun", TokenType.FUN);
		shouldReturnSingleValidToken("if", TokenType.IF);
		shouldReturnSingleValidToken("nil", TokenType.NIL);
		shouldReturnSingleValidToken("or", TokenType.OR);
		shouldReturnSingleValidToken("print", TokenType.PRINT);
		shouldReturnSingleValidToken("return", TokenType.RETURN);
		shouldReturnSingleValidToken("super", TokenType.SUPER);
		shouldReturnSingleValidToken("this", TokenType.THIS);
		shouldReturnSingleValidToken("true", TokenType.TRUE);
		shouldReturnSingleValidToken("var", TokenType.VAR);
		shouldReturnSingleValidToken("while", TokenType.WHILE);
	}
	
	@Test
	public void testScanTokens_OneOrTwoCharacterIsValid() {
		shouldReturnSingleValidToken("!", TokenType.BANG);
		shouldReturnSingleValidToken("!=", TokenType.BANG_EQUAL);
		shouldReturnSingleValidToken("=", TokenType.EQUAL);
		shouldReturnSingleValidToken("==", TokenType.EQUAL_EQUAL);
		shouldReturnSingleValidToken("<", TokenType.LESS);
		shouldReturnSingleValidToken("<=", TokenType.LESS_EQUAL);
		shouldReturnSingleValidToken(">", TokenType.GREATER);
		shouldReturnSingleValidToken(">=", TokenType.GREATER_EQUAL);
	}

	@Test
	public void testScanTokens_GetOnlyTokenAfterAfterNewLine() {    
		shouldReturnSingleValidToken("//only +parse+ token ; after {comment}\n*",TokenType.STAR);
	}
	
	@Test
	public void testScanTokens_MultipleValidLines() {
		String testInput = "// this is\ra\tcomment\n(( )){} // grouping stuff\n!*+-/=<> <= == // operators";
		Scanner scan = new Scanner(testInput);
		List<Token> tokenList = scan.scanTokens();
		
		assertEquals(17, tokenList.size());
		assertFalse(hasErrors());
		assertEquals(3, scan.getLine());
	}
	
	@Test
	public void testScanTokens_MultilineStringLiteralIsValid() {
		String testInput = "\"This is \n a String\"";
		Scanner scan = new Scanner(testInput);
		List<Token> tokenList = scan.scanTokens();

		assertEquals(2, tokenList.size());
		assertEquals(TokenType.STRING, tokenList.get(0).type);
		assertFalse(hasErrors());
		assertEquals(2, scan.getLine());
	}
	
	@Test
	public void testScanTokens_DecimalLiteralIsValid() {
		String testInput = "12.34";
		Scanner scan = new Scanner(testInput);
		List<Token> tokenList = scan.scanTokens();
		Token token = tokenList.get(0);
		
		assertEquals(TokenType.NUMBER, token.type);
		assertEquals(12.34, token.literal);
		assertFalse(hasErrors());
	}
	@Test
	public void testScanTokens_IntegerLiteralIsValid() {
		String testInput = "1234";
		Scanner scan = new Scanner(testInput);
		List<Token> tokenList = scan.scanTokens();
		Token token = tokenList.get(0);
		
		assertEquals(TokenType.NUMBER, token.type);
		assertEquals(1234.0, token.literal);
		assertFalse(hasErrors());
	}
	@Test
	public void testScanTokens_identifierStartingWithUnderscoreOrLetterIsValid() {
		shouldReturnSingleValidToken("_test", TokenType.IDENTIFIER);
		shouldReturnSingleValidToken("test", TokenType.IDENTIFIER);
		shouldReturnSingleValidToken("Test", TokenType.IDENTIFIER);
	}
	
	@Test
	public void testScanTokens_upperCaseReservedWordIsIdentifier() {
		shouldReturnSingleValidToken("And", TokenType.IDENTIFIER);
	}
	
	@Test
	public void testScanTokens_reservedWordWithAdditionalCharactersIsIdentifier() {
		shouldReturnSingleValidToken("orchid", TokenType.IDENTIFIER);
	}
}
