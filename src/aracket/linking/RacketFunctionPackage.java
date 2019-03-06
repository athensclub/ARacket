package aracket.linking;

import java.util.Map;

import aracket.core.RacketInterpreter;
import aracket.lang.RacketBaseFunction;

/**
 * An interface to be implement to be able to create a package for racket
 * function which will be loaded by the aracket.When this class is loaded, the
 * default constructor is called to create instance of this package
 * 
 * @author Athensclub
 *
 */
public interface RacketFunctionPackage {

    /**
     * Get all the function that this package has.mapped by name:function
     * (String:RacketFunction)
     * 
     * @return
     */
    public Map<String, RacketBaseFunction> getFunctions(RacketInterpreter interpreter);

}
