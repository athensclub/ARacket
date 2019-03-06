package aracket.core.command;

import a10lib.compiler.syntax.Block;
import a10lib.compiler.syntax.Statement;
import aracket.core.RacketDictionary;
import aracket.core.RacketInterpreter;
import aracket.lang.RacketObject;

/**
 * A class represent racket command "let"
 * 
 * @author Athensclub
 *
 */
public class LetCommand extends RacketCommand {

    public LetCommand(RacketInterpreter interpreter) {
	super("let", interpreter);
    }

    @Override
    public RacketObject evaluate(Block command, RacketDictionary scope) {
	if (command.getSubStatement().size() == 3) {
	    if (command.getSubStatement().get(1) instanceof Block) {
		return getInterpreter().evaluate(command.getSubStatement().get(2), defineLet((Block)command.getSubStatement().get(1), scope));		
	    } else {
		throw new IllegalArgumentException(
			"let first argument expected let block.found: " + command.getSubStatement().get(1));
	    }
	} else {
	    throw new IllegalArgumentException(
		    "let command expected 2 arguments. found: " + (command.getSubStatement().size() - 1));
	}
    }

    /**
     * Define a inner dictionary from a let block
     * 
     * @param letBlock
     * @param outer
     * @return
     */
    private RacketDictionary defineLet(Block letBlock, RacketDictionary outer) {
	RacketDictionary result = new RacketDictionary(outer, getInterpreter());
	for(Statement stm : letBlock.getSubStatement()) {
	    if(stm instanceof Block) {
		Block definition = (Block) stm;
		if(definition.getSubStatement().size() == 2) {
		    Statement name = definition.getSubStatement().get(0);
		    if(!(name instanceof Block)) {
			result.define(name.getStatement().getFirst().getString(), getInterpreter().evaluate(definition.getSubStatement().get(1), outer));
		    }else {
			throw new IllegalArgumentException("let block definition block first argument expected a name found: " + name);
		    }
		}else {
		    throw new IllegalArgumentException("let block definition block expected 2 arguments. found: " + definition.getSubStatement().size());
		}
	    }else {
		throw new IllegalArgumentException("let block definition expected a block. found: " + stm);
	    }
	}
	return result;
    }

}
