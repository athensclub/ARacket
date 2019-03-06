package aracket.core.command;

import a10lib.compiler.syntax.Block;
import aracket.core.RacketDictionary;
import aracket.core.RacketInterpreter;
import aracket.lang.RacketObject;

/**
 * A class representing racket command like define etc...
 * 
 * @author Athensclub
 *
 */
public abstract class RacketCommand extends RacketObject{

    private String name;

    private RacketInterpreter racketi;

    public RacketCommand(String name, RacketInterpreter interpreter) {
	this.name = name;
	racketi = interpreter;
    }

    /**
     * Get the reacket interpreter that is using this command
     * 
     * @return
     */
    public RacketInterpreter getInterpreter() {
	return racketi;
    }

    /**
     * Get the keyword of the block begin used to call this command
     * 
     * @return
     */
    public String getName() {
	return name;
    }

    /**
     * Evaluate the given block representing a command using the given scope
     * 
     * @param command
     */
    public abstract RacketObject evaluate(Block command,RacketDictionary scope);
    
    @Override
    public String toString() {
	return "#java-command(" + name + ")";
    }

}
