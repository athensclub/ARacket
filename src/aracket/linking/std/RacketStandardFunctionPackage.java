package aracket.linking.std;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import aracket.core.RacketDictionary;
import aracket.core.RacketInterpreter;
import aracket.core.Rackets;
import aracket.lang.RacketBaseFunction;
import aracket.lang.RacketBoolean;
import aracket.lang.RacketList;
import aracket.lang.RacketNumber;
import aracket.lang.RacketObject;
import aracket.lang.RacketPair;
import aracket.lang.RacketString;
import aracket.linking.RacketFunctionPackage;

/**
 * ARacket standard function package.Contain fundamental racket functions
 * 
 * @author Athensclub
 *
 */
public class RacketStandardFunctionPackage implements RacketFunctionPackage {

    @Override
    public Map<String, RacketBaseFunction> getFunctions(RacketInterpreter interpreter) {
	Map<String, RacketBaseFunction> result = new HashMap<>();

	// MATH_ALGEBRA_CALCULATIONS FUNCTIONS
	result.put("+", new ArithmeticFunction(interpreter, "+", (first, second) -> first.add(second)));
	result.put("-", new ArithmeticFunction(interpreter, "-", (first, second) -> first.subtract(second)));
	result.put("*", new ArithmeticFunction(interpreter, "*", (first, second) -> first.multiply(second)));
	result.put("/", Rackets.createFunction(interpreter, new Function<RacketObject[], RacketObject>() {
	    @Override
	    public RacketObject apply(RacketObject[] args) {
		if (args.length == 0) {
		    throw new IllegalArgumentException(
			    "/ function expected 1 or more argument(s). found: " + args.length);
		}
		if (args.length == 1) {
		    StrictFunction.checkArgType(args[0], RacketNumber.class, "/");
		    return RacketNumber.ONE.divide((RacketNumber) args[0]);
		} else {
		    StrictFunction.checkArgType(args[0], RacketNumber.class, "/");
		    RacketNumber current = (RacketNumber) args[0];
		    for (int i = 1; i < args.length; i++) {
			StrictFunction.checkArgType(args[i], RacketNumber.class, "/");
			current = current.divide((RacketNumber) args[i]);
		    }
		    return current;
		}
	    }
	}));
	result.put("remainder", new StrictFunction(interpreter, "remainder", 2, RacketNumber.class,
		args -> ((RacketNumber) args[0]).remainder((RacketNumber) args[1])));
	// END MATH_ALGEBRA_CALCULATIONS FUNCTIONS

	// NUMBER_COMPARE FUNCTIONS
	result.put("<", new CompareFunction(interpreter, (first, second) -> first.compareTo(second) < 0));
	result.put(">", new CompareFunction(interpreter, (first, second) -> first.compareTo(second) > 0));
	result.put("<=", new CompareFunction(interpreter, (first, second) -> first.compareTo(second) <= 0));
	result.put(">=", new CompareFunction(interpreter, (first, second) -> first.compareTo(second) >= 0));
	// END NUMBER_COMPARE FUNCTIONS

	// NUMBER_BASED FUNCTIONS
	result.put("abs",
		new StrictFunction(interpreter, "abs", 1, RacketNumber.class, args -> ((RacketNumber) args[0]).abs()));
	result.put("expt", new StrictFunction(interpreter, "expt", 2, RacketNumber.class,
		args -> ((RacketNumber) args[0]).pow((RacketNumber) args[1])));
	result.put("sqrt", new StrictFunction(interpreter, "sqrt", 1, RacketNumber.class,
		args -> ((RacketNumber) args[0]).sqrt()));
	result.put("zero?", new StrictFunction(interpreter, "zero?", 1, RacketNumber.class,
		args -> Rackets.from(((RacketNumber) args[0]).equals(RacketNumber.ZERO))));
	result.put("positive?", new StrictFunction(interpreter, "positive?", 1, RacketNumber.class,
		args -> Rackets.from(((RacketNumber) args[0]).compareTo(RacketNumber.ZERO) >= 0)));
	result.put("negative?", new StrictFunction(interpreter, "negative?", 1, RacketNumber.class,
		args -> Rackets.from(((RacketNumber) args[0]).compareTo(RacketNumber.ZERO) < 0)));
	result.put("even?", new StrictFunction(interpreter, "even?", 1, RacketNumber.class,
		args -> Rackets.from(((RacketNumber) args[0]).remainder(RacketNumber.TWO).equals(RacketNumber.ZERO))));
	result.put("odd?", new StrictFunction(interpreter, "odd?", 1, RacketNumber.class,
		args -> Rackets.from(!((RacketNumber) args[0]).remainder(RacketNumber.TWO).equals(RacketNumber.ZERO))));
	// END NUMBER_BASED FUNCTIONS

	// BOOLEAN_BASED FUNCTIONS
	result.put("and", new BooleanFunction(interpreter, "and", RacketBoolean.FALSE));
	result.put("or", new BooleanFunction(interpreter, "or", RacketBoolean.TRUE));
	result.put("not", new StrictFunction(interpreter, "not", 1, RacketBoolean.class,
		args -> ((RacketBoolean) args[0]).not()));
	// END BOOLEAN_BASED FUNCTIONS

	// EQUALITY_CHECK FUNCTIONS
	result.put("eq?", new StrictFunction(interpreter, "eq?", 2, args -> Rackets.from(args[0].equals(args[1]))));
	result.put("=", new CompareFunction(interpreter, (first, second) -> first.equals(second)));
	// END EQUALITY_CHECK FUNCTIONS

	// TYPE_CHECK FUNCTIONS
	result.put("number", new TypeCheckFunction(interpreter, RacketNumber.class));
	result.put("pair?", new TypeCheckFunction(interpreter, RacketPair.class));
	result.put("list?", new TypeCheckFunction(interpreter, RacketList.class));
	result.put("string?", new TypeCheckFunction(interpreter, RacketString.class));
	result.put("boolean?", new TypeCheckFunction(interpreter, RacketBoolean.class));
	result.put("procedure?", new TypeCheckFunction(interpreter, RacketBaseFunction.class));
	// END TYPE_CHECK FUNCTIONS

	// FUNCTION BASED FUNCTIONS
	result.put("compose", Rackets.createFunction(interpreter, args -> {
	    if (args.length == 0) {
		throw new IllegalArgumentException(
			"compose function expected 1 or more argument(s).found: " + args.length);
	    }
	    if (args.length == 1) {
		StrictFunction.checkArgType(args[0], RacketBaseFunction.class, "compose");
		return (RacketBaseFunction) args[0];
	    } else {
		for (RacketObject o : args) {
		    StrictFunction.checkArgType(o, RacketBaseFunction.class, "compose");
		}
		return Rackets.createScopedFunction(interpreter, (scope, fargs) -> {
		    RacketObject obj = ((RacketBaseFunction) args[args.length - 1]).asRacketFunction().invoke(scope,
			    fargs);
		    for (int i = args.length - 2; i >= 0; i--) {
			obj = ((RacketBaseFunction) args[i]).asRacketFunction().invoke(scope, obj);
		    }
		    return obj;
		});
	    }
	}));
	// END FUNCTION BASED FUNCTIONS

	// FUNCTION_LIST BASED FUNCTIONS
	result.put("map", new ListManipulationFunction(interpreter, "map", (scope, list, function) -> list
		.map(obj -> function.invoke(new RacketDictionary(scope, interpreter), obj))));
	result.put("for-each", new ListManipulationFunction(interpreter, "for-each", (scope, list, function) -> {
	    list.forEach(obj -> function.invoke(new RacketDictionary(scope, interpreter), obj));
	    return RacketInterpreter.EVAL_COMMAND;
	}));
	result.put("filter",
		new ListManipulationFunction(interpreter, "filter", (scope, list, function) -> list.filter(obj -> {
		    RacketObject o = function.invoke(new RacketDictionary(scope, interpreter), obj);
		    if (o instanceof RacketBoolean) {
			return ((RacketBoolean) o).value();
		    } else {
			throw new IllegalArgumentException(
				"filter function argument must return a boolean found returned: " + o);
		    }

		})));
	result.put("apply", new ListManipulationFunction(interpreter, "apply",
		(scope, list, function) -> function.invoke(new RacketDictionary(scope, interpreter), list.toArray())));
	// END FUNCTION-LIST BASED FUNCTIONS

	// PAIRS_AND_LIST BASED FUNCTIONS
	result.put("car",
		new StrictFunction(interpreter, "car", 1, RacketPair.class, args -> ((RacketPair) args[0]).car()));
	result.put("cdr",
		new StrictFunction(interpreter, "cdr", 1, RacketPair.class, args -> ((RacketPair) args[0]).cdr()));
	result.put("range", new RangeFunction(interpreter));
	result.put("length", new StrictFunction(interpreter,"length",1,RacketList.class,args->Rackets.from(((RacketList)args[0]).length())));
	result.put("append", Rackets.createFunction(interpreter, args -> {
	    if (args.length > 1) {
		StrictFunction.checkArgType(args[0], RacketList.class, "append");
		RacketList list = (RacketList) args[0];
		for (int i = 1; i < args.length; i++) {
		    StrictFunction.checkArgType(args[i], RacketList.class, "append");
		    list = list.append((RacketList) args[i]);
		}
		return list;
	    } else {
		throw new IllegalArgumentException(
			"append function expected more than 1 argument(s).found: " + args.length);
	    }
	}));
	result.put("reverse", new StrictFunction(interpreter, "reverse", 1, RacketList.class,
		args -> ((RacketList) args[0]).reverse()));
	result.put("list", Rackets.createFunction(interpreter, args -> Rackets.createList(Arrays.asList(args))));
	result.put("cons", new StrictFunction(interpreter, "cons", 2, args -> {
	    if (args[1] instanceof RacketPair) {
		return new RacketList(args[0], ((RacketPair) args[1]).asList());
	    }
	    return new RacketPair(args[0], args[1]);
	}));
	// END PAIRS_AND_LIST BASED FUNCTIONS

	// STRING_BASED FUNCTIONS
	result.put("string-length", new StrictFunction(interpreter, "string-length", 1, RacketString.class,
		args -> Rackets.from(((RacketString) args[0]).length())));
	result.put("string-append", new StrictFunction(interpreter, "string-append", 2, RacketString.class,
		args -> Rackets.from(((RacketString) args[0]).getString() + ((RacketString) args[1]).getString())));
	// END STRING_BASED FUNCTIONS
	return result;
    }

}
