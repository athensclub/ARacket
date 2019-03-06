package aracket.core;

import java.awt.BorderLayout;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import a10lib.compiler.Regex;
import a10lib.compiler.provider.KeywordProvider;
import a10lib.compiler.provider.StringProvider;
import a10lib.compiler.provider.WhitespaceProvider;
import a10lib.compiler.syntax.Block;
import a10lib.compiler.syntax.BlockProvider;
import a10lib.compiler.syntax.Parser;
import a10lib.compiler.syntax.Statement;
import a10lib.compiler.syntax.StatementProvider;
import a10lib.compiler.token.StreamTokenizer;
import a10lib.compiler.token.StringTokenizer;
import a10lib.compiler.token.Token;
import a10lib.compiler.token.TokenMatcher;
import a10lib.compiler.token.Tokenizer;
import aracket.core.command.RacketCommandDictionary;
import aracket.core.primitive.RacketPrimitiveDictionary;
import aracket.lang.RacketBaseFunction;
import aracket.lang.RacketFunction;
import aracket.lang.RacketList;
import aracket.lang.RacketObject;
import aracket.linking.RacketFunctionPackage;
import aracket.linking.std.RacketStandardFunctionPackage;

/**
 * A main class used for interpreting racket language.
 * 
 * <p>
 * Note that this language is just a simpler version of racket language and is
 * much smaller language.
 * </p>
 * 
 * <p>
 * If no scope is passed in evaluation methods, the global scope is used as
 * scope
 * </p>
 * <h5>Syntax:</h5>
 * <p>
 * &emsp;<code>(function args...)</code>: invoking function -> args... can be
 * any racket object or an evaluation into racket object and can be any amount
 * of argument;function can be a racket object type function or evaluation into
 * racket object type function
 * </p>
 * <p>
 * &emsp;<code>(define identifier object)</code>: defining an object in that
 * scope to be the given value -> object can be any racket object or evaluation
 * into racket object
 * </p>
 * <p>
 * &emsp;<code>(define (identifier identifier...) body)</code>: Define a
 * function with given name in the first identifier of the block followed by
 * arguments name in the same block and the implementation of function in body.
 * </p>
 * <p>
 * &emsp;<code>(if condition case_true case_false)</code>: evaluate a statement
 * in condition then if condition return #t then evaluate case_true otherwise
 * evaluate case_false ,raise exception is condition does not return boolean
 * </p>
 * <p>
 * &emsp;<code>(let ((name value)...) body)</code>: define all the value in the
 * name-value blocks and evaluate the body.The name-value block can be in any
 * amount
 * </p>
 * <p>
 * &emsp;<code>identifier</code>: print the value of that variable to the
 * console
 * </p>
 * <p>
 * &emsp;<code>(lambda (args...) body)</code>: Create a lambda function that
 * accept argument and return the result of function body -> args... can be
 * identifier and if that name already exist in outer scope, the outer scope
 * variable will be hidden args can also be any amount of identifier,body can be
 * any expression that return racket object and also use scope of that lambda
 * function
 * </p>
 * 
 * @author Athensclub
 *
 */
public class RacketInterpreter {

    /**
     * An object that return from evaluation if its evaluated a command.(no return
     * value in racket)
     */
    public static final RacketObject EVAL_COMMAND = new RacketObject();

    /**
     * Special value representing null
     */
    public static final RacketObject NULL = new RacketObject() {

	@Override
	public boolean equals(Object obj) {
	    return this == obj || obj == EMPTY_LIST || obj == null;
	}

	@Override
	public String toString() {
	    return "null";
	}
    };

    /**
     * Special value representing empty list
     */
    public static final RacketList EMPTY_LIST = new RacketList(null, null) {

	public void forEach(java.util.function.Consumer<RacketObject> consumer) {
	};

	@Override
	public String toString() {
	    return "’()";
	}

	@Override
	public String toStringWithoutQuote() {
	    return "()";
	}

	@Override
	public boolean equals(Object obj) {
	    return obj == this || obj == NULL || obj == null;
	}
    };

    private RacketDictionary globalScope;

    private boolean display = true;

    /**
     * commands like define,etc...
     */
    private RacketCommandDictionary commands;

    private RacketPrimitiveDictionary primitives;

    private StringTokenizer stringTokenizer;

    private StreamTokenizer streamTokenizer;

    private PrintStream out = System.out;

    private Parser parser;

    {
	primitives = new RacketPrimitiveDictionary();
	commands = new RacketCommandDictionary(this);
	globalScope = new RacketDictionary(null, this);
	stringTokenizer = new StringTokenizer("");
	streamTokenizer = new StreamTokenizer(null);
	parser = new Parser(null);
	setupDictionary();
	setupTokenizer(stringTokenizer);
	setupTokenizer(streamTokenizer);
	setupParser(parser);
    }

    /**
     * Prepare the dictionary to be ready for using
     * 
     * @param scope
     */
    private void setupDictionary() {
	importPackage(new RacketStandardFunctionPackage());
	globalScope.define("null", NULL);
	globalScope.define("empty", EMPTY_LIST);
    }

    /**
     * Set up tokenizer to be ready for tokenizing racket
     * 
     * @param tokenizer
     */
    private void setupTokenizer(Tokenizer tokenizer) {
	// string literal visitor
	StringProvider string = new StringProvider();
	tokenizer.addProvider(string);
	// adding whitespaces
	WhitespaceProvider whitespace = new WhitespaceProvider();
	tokenizer.addProvider(whitespace);
	// adding keywords
	KeywordProvider keyword = new KeywordProvider();
	keyword.addKeywords("(", ")");
	tokenizer.addProvider(keyword);
	// a identifier for when there is no brackets
	tokenizer.addProvider(new TokenMatcher() {

	    @Override
	    public boolean matchToken(Tokenizer tokenizer, StringBuilder string) {
		if (tokenizer.eof()) {
		    if (Regex.matches(string, Regex.RACKET_IDENTIFIER_PATTERN)) {
			return true;
		    }
		    Token token = new Token(string.toString());
		    if (primitives.createPrimitive(token) != EVAL_COMMAND) {
			return true;
		    }
		}
		return false;
	    }

	});
    }

    /**
     * Set up parser to be ready for parsing racket
     * 
     * @param parser
     */
    private static void setupParser(Parser parser) {
	parser.addStatementProvider(new StatementProvider() {
	    // anything but ')' '(' keyword
	    @Override
	    public boolean matchStatement(LinkedList<Token> tokens) {
		boolean match = true;
		Token token = null;
		Iterator<Token> it = tokens.iterator();
		while (it.hasNext()) {
		    token = it.next();
		    switch (token.getString()) {
		    case "(":
		    case ")":
			match = false;
			break;
		    }
		}
		return match;
	    }
	});
	parser.addBlockProvider(new BracketBlockProvider());
    }

    /**
     * Set the output stream of this racket interpreter
     * 
     * @param out
     */
    public void setOut(PrintStream out) {
	this.out = out;
    }

    /**
     * Show a block in java swing form
     * 
     * @param block
     */
    public void showBlock(Block block) {
	JFrame frame = new JFrame("ACompiler Block view");
	DefaultMutableTreeNode node = new DefaultMutableTreeNode("Outer_Block");
	block.addTo(node, "_");
	JTree tree = new JTree(node);
	tree.setRootVisible(true);
	frame.add(tree, BorderLayout.CENTER);
	frame.pack();
	frame.setVisible(true);
	frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    /**
     * Evaluate an script and return the result
     * 
     * @param expr
     * @return
     * @throws Exception
     */
    public RacketObject evaluate(String expr) throws Exception {
	stringTokenizer.reset(expr);
	return evaluate(stringTokenizer);
    }

    /**
     * Read from the given file and evaluate it. then return the result of
     * evaluation
     * 
     * @param file
     * @return
     * @throws Exception
     */
    public RacketObject evaluate(File file) throws Exception {
	streamTokenizer.reset(new FileReader(file));
	return evaluate(streamTokenizer);
    }

    /**
     * Read using the given reader and evaluate it.Then return the result of the
     * evaluation
     * 
     * @param reader
     * @return
     * @throws Exception
     */
    public RacketObject evaluate(Reader reader) throws Exception {
	streamTokenizer.reset(reader);
	return evaluate(streamTokenizer);
    }

    /**
     * Read using the given reader and evaluate it.Then return the result of the
     * evaluation
     * 
     * @param reader
     * @return
     * @throws Exception
     */
    public RacketObject evaluate(InputStream reader) throws Exception {
	streamTokenizer.reset(new InputStreamReader(reader));
	return evaluate(streamTokenizer);
    }

    /**
     * Evaluate from given tokenizer
     * 
     * @param tokenizer
     * @throws Exception
     */
    private RacketObject evaluate(Tokenizer tokenizer) throws Exception {
	parser.reset(tokenizer);
	Block global = parser.parse();
	RacketObject result = null;
	for (Statement sub : global.getSubStatement()) {
	    result = evaluate(sub, globalScope);
	    if (display && result != EVAL_COMMAND) {
		out.println(result);
	    }
	}
	return result;
    }

    /**
     * Evaluate a racket statement object in a given scope
     * 
     * @param stm
     * @return
     */
    public RacketObject evaluate(Statement stm, RacketDictionary scope) {
	if (stm instanceof Block) {
	    return evaluate((Block) stm, scope);
	} else {
	    if (stm instanceof RacketFunction.ObjectStatement) {
		return ((RacketFunction.ObjectStatement) stm).value();
	    }
	    String statement = stm.getStatement().getFirst().getString();
	    RacketObject temp;
	    if ((temp = primitives.createPrimitive(stm.getStatement().getFirst())) != EVAL_COMMAND) {
		return temp;
	    }
	    if (commands.hasCommand(stm)) {
		return commands.getCommand(stm);
	    }
	    if (Regex.matches(statement, Regex.RACKET_IDENTIFIER_PATTERN)) {
		return scope.get(statement);
	    } else {
		throw new IllegalArgumentException("Unexpected token: " + statement);
	    }
	}
    }

    /**
     * Evaluate a racket block object in a given scope
     * 
     * @param block
     * @return
     */
    public RacketObject evaluate(Block block, RacketDictionary scope) {

	RacketObject begin = evaluate(block.getSubStatement().getFirst(), scope);
	if (begin instanceof RacketBaseFunction) {
	    // block begin is function
	    List<Statement> substm = block.getSubStatement();
	    List<Statement> args = substm.size() == 1 ? Collections.emptyList() : substm.subList(1, substm.size());
	    return ((RacketBaseFunction) begin).invoke(new RacketDictionary(scope, this),
		    args.toArray(new Statement[args.size()]));
	}
	if (commands.hasCommand(block.getSubStatement().getFirst())) {
	    return commands.evaluateCommand(block, scope);
	}
	throw new IllegalArgumentException("Unexpected block begin: " + begin);
    }

    /**
     * Import the given function package into this interpreter's global scope
     * 
     * @param package_
     */
    public void importPackage(RacketFunctionPackage package_) {
	package_.getFunctions(this).forEach((name, function) -> globalScope.define(name, function));
    }

    /**
     * Get the global scope of dictionary for all of variables for this racket
     * interpreter
     * 
     * @return
     */
    public RacketDictionary getGlobalScope() {
	return globalScope;
    }

    /**
     * Get a dictionary containing all command in racket ie.define etc...
     * 
     * @return
     */
    public RacketCommandDictionary getCommands() {
	return commands;
    }

    /**
     * A block provider class providing block with '(' and ')'
     * 
     * @author Athensclub
     *
     */
    private static class BracketBlockProvider extends BlockProvider {

	@Override
	public boolean matchBeginBlock(Parser parser, LinkedList<Token> statement) {
	    if (statement.isEmpty()) {
		return false;
	    }
	    return statement.getFirst().getString().equals("(");
	}

	@Override
	public boolean matchEndBlock(Parser parser, LinkedList<Token> statement) {
	    if (statement.isEmpty()) {
		return false;
	    }
	    if (parser.getSubBlocks().peek() == null) {
		return statement.getLast().getString().equals(")"); // no blocks going on just hint parser to throw
								    // error
	    }
	    return (statement.getLast().getString().equals(")") && parser.getSubBlocks().peek().getProvider() == this);
	}
	// (define timetwo (lambda(x) (+ x x)))
    }

}
