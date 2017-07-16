
// File:   MH_Lexer.java
// Date:   October 2012

// Java template file for lexer component of Informatics 2A Assignment 1 (2012).
// Concerns lexical classes and lexer for the language MH (`Micro-Haskell').


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

class MH_Lexer extends GenLexer implements LEX_TOKEN_STREAM {

	static class VarAcceptor extends GenAcceptor implements DFA {

		int nextState (int state, char c) {
			switch (state) {
			case 0: {
				if (CharTypes.isSmall(c)) { 
					return 1;
				} else {
					return 2;
				}
			}
			case 1: { 
				if (CharTypes.isLetter(c) || CharTypes.isDigit(c) || c == '\'') {
					return 1;
				} else {
					return 2; 
				}
			}
			default: {
				return 2 ; // garbage state, declared "dead" below
			}
			}
		}

		public String lexClass() {
			return "VAR";
		}

		public int numberOfStates() {
			return 3;
		}

		boolean accepting(int state) {
			return(state == 1);
		}

		boolean dead(int state) {
			return(state == 2);
		}
	}

	static class NumAcceptor extends GenAcceptor implements DFA {

		@Override
		public String lexClass() {
			return "NUM";
		}

		@Override
		public int numberOfStates() {
			return 4;
		}

		@Override
		int nextState(int state, char c) {
			switch (state) {
			case 0: {
				if (c == '0') {
					return 1;
				} else if (CharTypes.isDigit(c)) {
					return 2;
				} else {
					return 3;
				}
			}

			case 1: {
				return 3;
			}
			case 2: {
				if (CharTypes.isDigit(c)) {
					return 2;
				} else {
					return 3;
				}
			}
			default: {
				return 3;
			}
			}
		}

		@Override
		boolean accepting(int state) {
			return(state == 1 || state == 2);
		}

		@Override
		boolean dead(int state) {
			return(state == 3);
		}
		// add code here
	}

	static class BooleanAcceptor extends GenAcceptor implements DFA {

		@Override
		public String lexClass() {
			return "BOOLEAN";
		}

		@Override
		public int numberOfStates() {
			return 10;
		}

		@Override
		int nextState(int state, char c) {
			switch (state) {
			case 0: {
				switch (c) {
				case 'T': {
					return 1;
				}
				case 'F': {
					return 4;
				}
				default: {
					return 9;
				}
				}
			}
			case 1: {
				if (c == 'r') {
					return 2;
				}
				else {
					return 9;
				}
			}
			case 2: {
				if (c == 'u') {
					return 3;
				} else {
					return 9;
				}
			}
			case 3: {
				if (c == 'e') {
					return 8;
				} else {
					return 9;
				}
			}
			case 4: {
				if (c == 'a') {
					return 5;
				} else {
					return 9;
				}
			}
			case 5: {
				if (c == 'l') {
					return 6;
				} else {
					return 9;
				}
			}
			case 6: {
				if (c == 's') {
					return 7;
				} else {
					return 9;
				}
			}
			case 7: {
				if (c == 'e') {
					return 8;
				} else {
					return 9;
				}
			}
			default: {
				return 9;
			}
			}
		}

		@Override
		boolean accepting(int state) {
			return (state == 8);
		}

		@Override
		boolean dead(int state) {
			return (state == 9);
		}

	}

	static class SymAcceptor extends GenAcceptor implements DFA {

		@Override
		public String lexClass() {
			return "SYM";
		}

		@Override
		public int numberOfStates() {
			return 3;
		}

		@Override
		int nextState(int state, char c) {
			switch (state) {
			case 0: {
				if (CharTypes.isSymbolic(c)) {
					return 1;
				} else {
					return 2;
				}
			}
			case 1: {
				if (CharTypes.isSymbolic(c)) {
					return 1;
				} else {
					return 2;
				}
			}
			default: {
				return 2;
			}
			}
		}

		@Override
		boolean accepting(int state) {
			return (state == 1);
		}

		@Override
		boolean dead(int state) {
			return (state == 2);
		}
		// add code here
	}

	static class WhitespaceAcceptor extends GenAcceptor implements DFA {

		@Override
		public String lexClass() {
			return "";
		}

		@Override
		public int numberOfStates() {
			return 3;
		}

		@Override
		int nextState(int state, char c) {
			switch (state) {
			case 0: {
				if (CharTypes.isWhitespace(c)) {
					return 1;
				} else {
					return 2;
				}
			}
			case 1: {
				if (CharTypes.isWhitespace(c)) {
					return 1;
				} else {
					return 2;
				}
			}
			default: {
				return 2;
			}
			}
		}

		@Override
		boolean accepting(int state) {
			return (state == 1);
		}

		@Override
		boolean dead(int state) {
			return (state == 2);
		}
	}

	static class CommentAcceptor extends GenAcceptor implements DFA {

		@Override
		public String lexClass() {
			return "";
		}

		@Override
		public int numberOfStates() {
			return 6;
		}

		@Override
		int nextState(int state, char c) {
			switch (state) {
			case 0: {
				if (c == '-') {
					return 1;
				} else {
					return 5;
				}
			}
			case 1: {
				if (c == '-') {
					return 2;
				} else {
					return 5;
				}
			}
			case 2: {
				if (c == '-') {
					return 2;
				} else if (!CharTypes.isSymbolic(c) && !CharTypes.isNewline(c)) {
					return 3;
				} else {
					return 5;
				}
			}
			case 3: {
				if (CharTypes.isNewline(c)) {
					return 4;
				} else {
					return 3;
				}
			}
			default: {
				return 5;
			}
			}
		}

		@Override
		boolean accepting(int state) {
			return (state == 4);
		}

		@Override
		boolean dead(int state) {
			return (state == 5);
		}
		// add code here
	}

	static class TokAcceptor extends GenAcceptor implements DFA {

		String tok;
		int tokLen;

		TokAcceptor (String tok) {
			this.tok = tok;
			tokLen = tok.length();
		}

		@Override
		public String lexClass() {
			return tok;
		}

		@Override
		public int numberOfStates() {
			return tokLen+2;
		}

		@Override
		int nextState(int state, char c) {			
			if (state <= tokLen - 1) {
				if (c == tok.charAt(state)) {
					return ++state;
				} else {
					return tokLen + 1;
				}
			} else {
				return tokLen + 1;
			}
		}

		@Override
		boolean accepting(int state) {
			return (state == tokLen);
		}

		@Override
		boolean dead(int state) {
			return (state == tokLen + 1);
		}
	}

    static DFA varAcc = new VarAcceptor();
    static DFA numAcc = new NumAcceptor();
    static DFA booleanAcc = new BooleanAcceptor();
    static DFA symAcc = new SymAcceptor();
    static DFA whitespaceAcc = new WhitespaceAcceptor();
    static DFA commentAcc = new CommentAcceptor();
    static DFA openParAcc = new TokAcceptor(")");
    static DFA closeParAcc = new TokAcceptor("(");
    static DFA semicolonAcc = new TokAcceptor(";");
    static DFA intAcc = new TokAcceptor("Integer");
    static DFA boolAcc = new TokAcceptor("Bool");
    static DFA ifAcc = new TokAcceptor("if");
    static DFA thenAcc = new TokAcceptor("then");
    static DFA elseAcc = new TokAcceptor("else");
    
    static DFA[] MH_acceptors = new DFA[] {
    	ifAcc,
    	thenAcc,
    	elseAcc,
    	intAcc,
    	boolAcc,
    	commentAcc,
    	whitespaceAcc,
    	openParAcc,
    	closeParAcc,
    	semicolonAcc,
    	varAcc,
    	numAcc,
    	booleanAcc, 
    	symAcc };

	MH_Lexer (Reader reader) {
		super(reader,MH_acceptors) ;
	}

}

class MH_DemoLexer {
    public static void main (String[] args) 
	throws LexError, StateOutOfRange, IOException {
	System.out.print ("Lexer> ") ;
	Reader reader = new BufferedReader (new InputStreamReader (System.in)) ;
    MH_Lexer demoLexer = new MH_Lexer (reader) ;
	LexToken currTok = demoLexer.pullProperToken() ;
	while (currTok != null) {
	    System.out.println (currTok.value() + " \t" + 
				currTok.lexClass()) ;
	    currTok = demoLexer.pullProperToken() ;
	} ;
	System.out.println ("END OF INPUT.") ;
    }
}
