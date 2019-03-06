package aracket.lang;

import aracket.core.Rackets;

/**
 * A racket class object containing string literal.Use getString() to get literal value of the string
 * 
 * @author Athensclub
 *
 */
public class RacketString extends RacketObject{

    private String value;
    
    public RacketString(String string) {
	value = string;
    }
    
    /**
     * Return literal value of this string while toString() return the literal inside a ' ' quote;
     * @return
     */
    public String getString() {
	return value;
    }
    
    /**
     * Get the length of this string
     * @return
     */
    public int length() {
	return value.length();
    }
    
    public boolean equals(RacketString other) {
	return value.equals(other.value);
    }
    
    @Override
    public boolean equals(Object other) {
	if(other instanceof String) {
	    return equals(Rackets.from((String)other));
	}else if(other instanceof RacketString) {
	    return equals((RacketString)other);
	}
	return false;
    }
    
    @Override
    public String toString() {
	return "\"" + value + "\"";
    }
    
}
