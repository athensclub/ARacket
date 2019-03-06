package aracket.lang;

/**
 * A class that represent racket keyword
 * 
 * @author Athensclub
 *
 */
public class RacketKeyword extends RacketObject{
    
    private String name;

    public RacketKeyword(String name) {
	this.name = name;
    }

    public boolean equals(RacketKeyword other) {
	return name.equals(other.name);
    }

    @Override
    public boolean equals(Object obj) {
	if (obj instanceof RacketKeyword) {
	    return equals((RacketKeyword) obj);
	}
	return false;
    }

    @Override
    public String toString() {
	return name;
    }
}
