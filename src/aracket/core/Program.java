package aracket.core;

import java.util.Scanner;

import a10lib.compiler.AParsingException;
import a10lib.compiler.Regex;
import a10lib.util.Strings;
import aracket.lang.RacketBoolean;

/**
 * A class containing main method for using with pure program
 * 
 * @author Athensclub
 *
 */
public class Program {

    public static void main(String[] args) throws AParsingException {
	RacketInterpreter racketi = new RacketInterpreter();
	Scanner input = new Scanner(System.in);
	while (true) {
	    String str = input.nextLine();
	    if (!str.equalsIgnoreCase("exit")) {
		try {
		    racketi.evaluate(str);
		} catch (Exception e) {
		    e.printStackTrace();//System.err.println("Exception: " + e.getMessage());
		}
	    } else {
		System.exit(0);
	    }
	}
    }

}
