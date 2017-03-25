package ROMdb.Helpers;

import ROMdb.Exceptions.InputFormatException;
import ROMdb.Helpers.InputType;

/**
 * Created by Derek Gaffney on 3/21/2017.
 */
public class InputValidator
{
    /**
     * Checks to see if the pattern is a match.
     * @param inputString the string to check the pattern against.
     * @param it the input type.
     * @throws InputFormatException If input is not correct.
     */
    public static void checkPatternMatch(String inputString, InputType it) throws InputFormatException
    {
        if(!inputString.matches(it.getPattern()))
        {
            throw new InputFormatException("InputString '" + inputString + "' does not match pattern: " + it.getPatternName());
        }
    }

    /**
     * Checks to see if the pattern does not match.
     * @param inputString the string to check for validity.
     * @param it the input type.
     * @throws InputFormatException If input is not correct
     */
    public static void checkPatternDoesNotMatch(String inputString, InputType it) throws InputFormatException
    {
        if(inputString.matches(it.getPattern()))
        {
            throw new InputFormatException("InputString '" + inputString + "' matches pattern: " + it.getPatternName());
        }
    }
}
