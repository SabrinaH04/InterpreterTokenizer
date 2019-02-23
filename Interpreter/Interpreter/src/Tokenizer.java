
import java.util.*;
import java.io.*;
import java.lang.*;

public class Tokenizer {
	
	private Tokenizer() {

	}
	//end token list:
	public static List<String> tokensList = new LinkedList<String>();
	
	//CORE language list
	private static final String[] reservedWords = {"program","begin","end","int","if","then","else","while","loop","read","write"};
	private static final String[] specialSymbols = {";",",","=","!","[","]","&&","||","(",")","+","-","*","!=","==","<",">","<=",">="};
	//CORE restrictions:
	//private static final int INTSIZE = 8;
	//private static final int IDSIZE = 8;
	//extra
	//private static final String[] whiteSpace = {" ","\n","\t","\r"};
	public static int Tcursor = 0; //token cursor
	
	public static void resetCursor() {//resets cursor to beginning
		Tcursor= 0;
	}
	public static String getToken() { //returns current token
		return tokensList.get(Tcursor);
	}
	public static void skipToken() { //moves cursor forward by 1
		Tcursor++;
	}
	public static Boolean checkToken(String token) {//checks that token matches input string
		if(token.equals(tokensList.get(Tcursor))) {
			return (true);
		}else {
			System.out.println("error in checkToken, expected: "+ token + ", got: "+tokensList.get(Tcursor));
			System.exit(1);
			return(false);
		}
		
	}
	public static Boolean isTokenID() { //checks if current token is ID
		String currentToken = getToken();
		return(Character.isUpperCase(currentToken.charAt(0))); //if current token starts with Uppercase, it is ID.
		
	}
	public static void resetTokensList() {
		tokensList.clear();
	}
	//method to read in the CORE program file
	public static boolean searchArray(String[] array1, String item1) {
		//boolean foundItem1 = false;		
		//for(String items : array1) {//for all items in the array
			//if(items==item1) {//if item1 is found
				//foundItem1 = true;
			//}
						
		//}	
		//System.out.println("ITEM1 IS:" +item1+":");
		
		return Arrays.asList(array1).contains(item1);
	}
	public static List<String> tokenToNum(List<String> listOfTokens){
		List<String> tokenNum = new LinkedList<String>();
		
		for(String tokens2 : listOfTokens) {
			String tokens1 = tokens2;
			//System.out.println("NUMTOKEN:"+tokens1+":");
			
			if(tokens1.equals("program")) {
				tokenNum.add("1");
			}else if(tokens1.equals("begin")) {
				tokenNum.add("2");
			}else if(tokens1.equals("end")) {
				tokenNum.add("3");
			}else if(tokens1.equals("int")) {
				tokenNum.add("4");
			}else if(tokens1.equals("if")) {
				tokenNum.add("5");
			}else if(tokens1.equals("then")) {
				tokenNum.add("6");
			}else if(tokens1.equals("else")) {
				tokenNum.add("7");
			}else if(tokens1.equals("while")) {
				tokenNum.add("8");
			}else if(tokens1.equals("loop")) {
				tokenNum.add("9");
			}else if(tokens1.equals("read")) {
				tokenNum.add("10");
			}else if(tokens1.equals("write")) {
				tokenNum.add("11");
			}else if(tokens1.equals(";")) {
				tokenNum.add("12");
			}else if(tokens1.equals(",")) {
				tokenNum.add("13");
			}else if(tokens1.equals("=")) {
				tokenNum.add("14");
			}else if(tokens1.equals("!")) {
				tokenNum.add("15");
			}else if(tokens1.equals("[")) {
				tokenNum.add("16");
			}else if(tokens1.equals("]")) {
				tokenNum.add("17");
			}else if(tokens1.equals("&&")) {
				tokenNum.add("18");
			}else if(tokens1.equals("||")) {
				tokenNum.add("19");
			}else if(tokens1.equals("(")) {
				tokenNum.add("20");
			}else if(tokens1.equals(")")) {
				tokenNum.add("21");
			}else if(tokens1.equals("+")) {
				tokenNum.add("22");
			}else if(tokens1.equals("-")) {
				tokenNum.add("23");
			}else if(tokens1.equals("*")) {
				tokenNum.add("24");
			}else if(tokens1.equals("!=")) {
				tokenNum.add("25");
			}else if(tokens1.equals("==")) {
				tokenNum.add("26");
			}else if(tokens1.equals("<")) {
				tokenNum.add("27");
			}else if(tokens1.equals(">")) {
				tokenNum.add("28");
			}else if(tokens1.equals("<=")) {
				tokenNum.add("29");
			}else if(tokens1.equals(">=")) {
				tokenNum.add("30");
			}else if(Character.isDigit(tokens1.charAt(0))) { //token is number
				tokenNum.add("31");
			}else if(Character.isUpperCase(tokens1.charAt(0))&&(!tokens1.equals("EOF"))) { //token is variable
				tokenNum.add("32");
			}else if(tokens1.equals("EOF")) {
				tokenNum.add("33");
			}else {
				System.out.println("error in tokenNum, got token: "+tokens1);
				System.exit(1);;
			}
		}
		
		
		return tokenNum;
		
	}
	public static List<String> readInProgram(File coreProgram) throws IOException {
		//test files
		//coreProgram = "test program1";
		
		//line in
		String line = null;
		List<String> inputProgram = new LinkedList<String>();
		
		try {
			FileReader reader = new FileReader(coreProgram);
			
			BufferedReader buffer = new BufferedReader(reader);
			line = buffer.readLine();
			while(line!= null) {
				//System.out.println(line);
				inputProgram.add(line);
				line = buffer.readLine();
				
			}
			buffer.close();
			
		} catch (FileNotFoundException e) {
			System.out.println("Cannot open file "+ coreProgram + "\n");
			e.printStackTrace();
		}catch(IOException ex) {
			ex.printStackTrace();
			
		}
		return inputProgram;
		
		
	}
	public static void Tokenizing(List<String> inputProgram) {
		//start of token index
        int x = 0;
        //end of token index
        int y = 0;
       
        char currentChar;
        for(String temp : inputProgram) { //for every line in the file
        	x = 0;
        	y = 0;
        	
        	while(x < temp.length()) { //while start of token is not at end of line
        		currentChar = temp.charAt(x);
        		String currentString = Character.toString(currentChar);
        		y = x+1;
        		if(Character.isLowerCase(currentChar)) { //char is lowercase - must be one of reservedWords
        			while(y<temp.length()&&Character.isLowerCase(temp.charAt(y))) {//finds start char and end char of lowercase term
        				y++;
        			}
        			String possibleRW = temp.substring(x, y); //possible reserved word, in lowercase
        			//String tempYString = Character.toString(temp.charAt(y));
        			//System.out.println("possibleRW is::: " + possibleRW + "\n");
 
        			if(((y==temp.length())
        					||(Character.isWhitespace(temp.charAt(y)))
        					||(searchArray(specialSymbols,Character.toString(temp.charAt(y)))))
        					&&searchArray(reservedWords, possibleRW)) {//if the next char is whitespace, end of line, or special symbol AND possibleRW is a real RW
        				//System.out.println("Found a reserved word! it is: " + possibleRW + "\n");
        				tokensList.add(possibleRW);
        				
        				
        			}else {
        				System.out.println("error in searching reserved words, either whitespace expected or not a reserved word. got: " + possibleRW + " e \n");
        				System.exit(1);
        			}
        			
        			
        		}else if(Character.isUpperCase(currentChar)) { //must be a variable (ABC, X, Z...)
        			while(y<temp.length()&&Character.isUpperCase(temp.charAt(y))) {
        				y++;
        			}
        			String possibleVar = temp.substring(x, y);
        			//System.out.println("PossibleVar is:"+possibleVar+":");
        			if(y<temp.length()&&(Character.isDigit(temp.charAt(y)))){//if y is digit, good, longer variable			
        				while(y<temp.length()&&Character.isDigit(temp.charAt(y))) {//while there are numbers attached to variable
        					y++;
        				}
        			possibleVar = temp.substring(x,y);
        			//if y is at the end, or whitespace, or special symbol, then good, still valid -variables must be 8 chars or less
        			}
        			if(((y==temp.length())
        					||(Character.isWhitespace(temp.charAt(y)))
        					||(searchArray(specialSymbols, Character.toString(temp.charAt(y)))))
        					&&possibleVar.length()<9) { 
        				//System.out.println("Found a variable! it is: " + possibleVar + "\n"); //found variable with no numbers in it
        				tokensList.add(possibleVar);
        			}else {
        				System.out.println("error in tokenizing variables. got: " + possibleVar + "\n");
        				System.exit(1);
        			}
        			
        			
        		}else if(Character.isDigit(currentChar)) { //Must be number
        			while(y<temp.length()&&(Character.isDigit(temp.charAt(y)))) {
        				y++;
        			}
        			String possibleNum = temp.substring(x,y);
        			if(((y==temp.length())
        					||(Character.isWhitespace(temp.charAt(y)))
        					||(searchArray(specialSymbols, Character.toString(temp.charAt(y)))))
        					&&possibleNum.length()<9) { 
        				//System.out.println("Found a number! it is: " + possibleNum + "\n"); //found variable with no numbers in it
        				tokensList.add(possibleNum);
        			}else {
        				System.out.println("error in tokenizing numbers. got: " + possibleNum + "\n");
        				System.exit(1);
        			}
        		
        			
        		}else if(searchArray(specialSymbols, currentString)) {//must be a special symbol
        			String possibleSy = currentString;
        			if((y<temp.length())&&searchArray(specialSymbols, Character.toString(temp.charAt(y)))) { //if the symbol has a symbol following it
        				if(searchArray(specialSymbols, temp.substring(x,y+1))) { //if the 2 symbols next to eachother are in specialSymbols
        					y++;
        					//System.out.println("Special symbol is 2 chars long!");
        					//System.out.println("Symbol:"+temp.substring(x,y));
        					possibleSy = temp.substring(x,y);
        				}
        			}
        			//System.out.println("Found a special symbol! it is: " + possibleSy + "\n"); //don't need to check for whitespace/characters touching it
        			tokensList.add(possibleSy);
        			
        		}
        	
        		x = y; //move cursor forward to y
        		
        		
        		
        	}
        	
        	
        	
        }
        tokensList.add("EOF"); //java consumes EOF automatically, so need to add it on
		
	}



}