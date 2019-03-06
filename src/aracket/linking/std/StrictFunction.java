package aracket.linking.std;

import java.util.Arrays;
import java.util.function.Function;

import aracket.core.RacketDictionary;
import aracket.core.RacketInterpreter;
import aracket.lang.RacketFunction;
import aracket.lang.RacketObject;

/**
 * A function that accept specific type and length of arguments
 * 
 * @author Athensclub
 *
 */
public class StrictFunction extends RacketFunction {

    private int expectedArgsLength;

    private Class<?>[] expectedType;

    private String functionName;

    private Function<RacketObject[], RacketObject> function;

    public StrictFunction(RacketInterpreter interpreter, String functionName, int expectedArgsLength,
	    Class<?>[] expectedType, Function<RacketObject[], RacketObject> function) {
	super(interpreter);
	if (expectedType.length != expectedArgsLength) {
	    throw new IllegalArgumentException(
		    "Strict Function expected argument types length does not match with expected argument length: "
			    + expectedType.length + "," + expectedArgsLength);
	}
	this.expectedArgsLength = expectedArgsLength;
	this.expectedType = expectedType;
	this.functionName = functionName;
	this.function = function;
    }

    public StrictFunction(RacketInterpreter interpreter, String functionName, int expectedArgsLength,
	    Class<?> expectedType, Function<RacketObject[], RacketObject> function) {
	this(interpreter, functionName, expectedArgsLength, createArgsTypes(expectedArgsLength, expectedType), function);
    }
    
    public StrictFunction(RacketInterpreter interpreter, String functionName, int expectedArgsLength,
	    Function<RacketObject[], RacketObject> function) {
	this(interpreter, functionName, expectedArgsLength, new Class<?>[expectedArgsLength], function);
	expectedType = null;
    }
    
    private static Class<?>[] createArgsTypes(int expectedArgsLength,Class<?> expectedType) {
	Class<?>[] expectedTypes = new Class<?>[expectedArgsLength];
	Arrays.fill(expectedTypes, expectedType);
	return expectedTypes;
    }

    @Override
    public RacketObject invoke(RacketDictionary scope, RacketObject... args) {
	checkArgLength(args, functionName, expectedArgsLength);
	if (expectedType != null) {
	    for (int i = 0; i < args.length; i++) {
		checkArgType(args[i], expectedType[i], functionName);
	    }
	}
	return function.apply(args);
    }

    /**
     * Check if the given object is the given type, throwing exception if found not
     * 
     * @param args
     * @param expectedArgsType
     * @param functionName
     */
    public static void checkArgType(RacketObject arg, Class<?> expectedArgType, String functionName) {
	if (!expectedArgType.isAssignableFrom(arg.getClass())) {
	    throw new IllegalArgumentException(
		    functionName + " expected " + expectedArgType.getSimpleName() + " as argument(s) found: " + arg);
	}
    }

    /**
     * Check the given arguments name to the expected count and throws exception
     * when found unexpected count
     * 
     * @param args
     * @param functionName
     * @param expectedArgCount
     */
    public static void checkArgLength(RacketObject[] args, String functionName, int expectedArgCount) {
	if (args.length != expectedArgCount) {
	    throw new IllegalArgumentException(
		    functionName + " expected " + expectedArgCount + " argument(s) found: " + args.length);
	}
    }

}
