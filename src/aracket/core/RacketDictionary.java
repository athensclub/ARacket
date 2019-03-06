package aracket.core;

import java.util.HashMap;
import java.util.Map;

import a10lib.compiler.Regex;
import aracket.lang.RacketObject;

/**
 * A class containing all variables in a scope
 * 
 * @author Athensclub
 *
 */
public class RacketDictionary {

    private RacketDictionary outerScope;

    private Map<String, RacketObject> variables = new HashMap<>();
    
    private RacketInterpreter racketi;

    /**
     * Create a dictionary scope with the given outer scope
     * 
     * @param outer
     */
    public RacketDictionary(RacketDictionary outer,RacketInterpreter racketi) {
	outerScope = outer;
	this.racketi = racketi;
    }

    /**
     * Define a variable in this scope.Cannot define an already defined variable
     * 
     * @param name
     * @param value
     */
    public void define(String name, RacketObject value) {
	
	if(!name.matches(Regex.RACKET_IDENTIFIER_REGEX)) {
	    throw new IllegalArgumentException("Define variable with non-identifier name: " + name);
	}

	if(racketi.getCommands().hasCommand(name)) {
	    throw new IllegalArgumentException("Re-Define racket default command: " + name);
	}
	
	if (variables.containsKey(name)) {
	    throw new IllegalArgumentException("Define already defined racket variable: " + name);
	}

	variables.put(name, value);

    }

    /**
     * Get the value of the given variable name.Starting from this scope and if
     * doesn't find any with given name,search for that variable until reaches
     * outer-most scope
     * 
     * @param name
     * @return
     */
    public RacketObject get(String name) {

	if (!variables.containsKey(name)) {
	    if (outerScope != null) {
		return outerScope.get(name);
	    } else {
		throw new IllegalArgumentException("Unknown racket variable name: " + name);
	    }
	}

	return variables.get(name);

    }

}
