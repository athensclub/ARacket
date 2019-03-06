package aracket.lang;

/**
 * A class represent racket symbol
 * 
 * @author Athensclub
 *
 */
public class RacketSymbol extends RacketObject {

    private String name;

    public RacketSymbol(String name) {
	this.name = name;
    }

    public boolean equals(RacketSymbol other) {
	return name.equals(other.name);
    }

    @Override
    public boolean equals(Object obj) {
	if (obj instanceof RacketSymbol) {
	    return equals((RacketSymbol) obj);
	}
	return false;
    }

    @Override
    public String toString() {
	return "’" + name;
    }

}
