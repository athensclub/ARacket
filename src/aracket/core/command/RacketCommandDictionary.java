package aracket.core.command;

import java.util.HashMap;
import java.util.Map;

import a10lib.compiler.syntax.Block;
import a10lib.compiler.syntax.Statement;
import aracket.core.RacketDictionary;
import aracket.core.RacketInterpreter;
import aracket.lang.RacketObject;

/**
 * A dictionary for all racket command
 * 
 * @author Athensclub
 *
 */
public class RacketCommandDictionary {

    private Map<String, RacketCommand> commands = new HashMap<>();

    public RacketCommandDictionary(RacketInterpreter interpreter) {
	addCommand(new DefineCommand(interpreter));
	addCommand(new LambdaCommand(interpreter));
	addCommand(new IfCommand(interpreter));
	addCommand(new LetCommand(interpreter));
    }

    /**
     * Add given command to the dictionary
     * 
     * @param command
     */
    public void addCommand(RacketCommand command) {
	if(!commands.containsKey(command.getName())) {
	    commands.put(command.getName(), command);
	}else {
	    throw new IllegalArgumentException("Add command that already exist: " + command.getName());
	}
    }

    /**
     * Check if block begin matches with any of command in this dictionary
     * 
     * @param blockBegin
     */
    public boolean hasCommand(Statement blockBegin) {
	return hasCommand(blockBegin.getStatement().getFirst().getString());
    }

    /**
     * return whether this command this dictionary has command with the given name
     * or not
     * 
     * @param name
     * @return
     */
    public boolean hasCommand(String name) {
	return commands.containsKey(name);
    }

    /**
     * Get the command in this dictionary that has the given name
     * 
     * @param name
     * @return
     */
    public RacketCommand getCommand(Statement blockBegin) {
	return commands.get(blockBegin.getStatement().getFirst().getString());
    }

    /**
     * Evaluate the given block by using command in this dictionary in the given
     * scope
     * 
     * @param command
     */
    public RacketObject evaluateCommand(Block command, RacketDictionary scope) {
	return getCommand(command.getSubStatement().getFirst()).evaluate(command, scope);
    }

}
