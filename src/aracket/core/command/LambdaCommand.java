package aracket.core.command;

import java.util.LinkedList;

import a10lib.compiler.Regex;
import a10lib.compiler.syntax.Block;
import a10lib.compiler.syntax.Statement;
import aracket.core.RacketDictionary;
import aracket.core.RacketInterpreter;
import aracket.lang.RacketDefaultFunction;
import aracket.lang.RacketObject;

/**
 * A command responsible for creating a lambda begin
 * 
 * @author Athensclub
 *
 */
public class LambdaCommand extends RacketCommand {

    public LambdaCommand(RacketInterpreter interpreter) {
	super("lambda", interpreter);
    }

    @Override
    public RacketObject evaluate(Block command, RacketDictionary scope) {
	if (command.getSubStatement().size() == 3) {
	    Statement argstm = command.getSubStatement().get(1);
	    if (argstm instanceof Block) {
		return new RacketDefaultFunction(getInterpreter(), command.getSubStatement().get(2), getArgNames((Block) argstm));
	    } else {
		// TODO
		throw new IllegalArgumentException(
			"Lambda argument expression is not in '(' indentifier... ')' form: " + argstm);
	    }
	} else {
	    throw new IllegalArgumentException("Invalid argument count for lambda expression expected 2 found: "
		    + (command.getSubStatement().size() - 1));
	}
    }

    /**
     * Get a argument name in order from getFirst() to getLast()
     * 
     * @param args
     * @return
     */
    private static LinkedList<String> getArgNames(Block args) {
	LinkedList<String> result = new LinkedList<>();
	for (Statement stm : args.getSubStatement()) {
	    if (!(stm instanceof Block)) {
		String name = stm.getStatement().getFirst().getString();
		if (name.matches(Regex.RACKET_IDENTIFIER_REGEX)) {
		    if (!result.contains(name)) {
			result.add(name);
		    } else {
			throw new IllegalArgumentException("Duplicate variable name: " + name);
		    }
		} else {
		    throw new IllegalArgumentException("Lambda expression must be identifier found: " + name);
		}
	    } else {
		throw new IllegalArgumentException("Lambda expression must be identifier found: " + stm);
	    }
	}
	return result;
    }

}
