package aracket.core.command;

import java.util.Iterator;
import java.util.LinkedList;

import a10lib.compiler.Regex;
import a10lib.compiler.syntax.Block;
import a10lib.compiler.syntax.Statement;
import aracket.core.RacketDictionary;
import aracket.core.RacketInterpreter;
import aracket.lang.RacketDefaultFunction;
import aracket.lang.RacketObject;

public class DefineCommand extends RacketCommand {

    public DefineCommand(RacketInterpreter racketi) {
	super("define", racketi);
    }

    @Override
    public RacketObject evaluate(Block command, RacketDictionary scope) {
	if (command.getSubStatement().size() == 3) {
	    Statement firstArg = command.getSubStatement().get(1);
	    if (!(firstArg instanceof Block)) {
		String name = firstArg.getStatement().getFirst().getString();
		if (name.matches(Regex.RACKET_IDENTIFIER_REGEX)) {
		    RacketObject value = getInterpreter().evaluate(command.getSubStatement().get(2), scope);
		    scope.define(name, value);
		    return RacketInterpreter.EVAL_COMMAND;
		} else {
		    throw new IllegalArgumentException("Define variable with invalid name: " + name);
		}
	    } else {
		Block functionBlock = (Block) firstArg;
		Iterator<Statement> fstm = functionBlock.getSubStatement().iterator();
		if (fstm.hasNext()) {
		    Statement namest = fstm.next();
		    if (!(namest instanceof Block)) {
			String name = namest.getStatement().getFirst().getString();
			if (name.matches(Regex.RACKET_IDENTIFIER_REGEX)) {
			    scope.define(name, new RacketDefaultFunction(getInterpreter(), command.getSubStatement().get(2), getArgNames(fstm))); 
			    return RacketInterpreter.EVAL_COMMAND;
			} else {
			    throw new IllegalArgumentException("Define function with invalid name: " + name);
			}
		    } else {
			throw new IllegalArgumentException("function define with name as block: " + namest);
		    }
		} else {
		    throw new IllegalArgumentException("function define without name: " + functionBlock);
		}
	    }
	} else {
	    throw new IllegalArgumentException("Using define command with invalid argument count");
	}
    }
    
    private static LinkedList<String> getArgNames(Iterator<Statement> it) {
	LinkedList<String> result = new LinkedList<>();
	while(it.hasNext()) {
	    Statement stm = it.next();
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
