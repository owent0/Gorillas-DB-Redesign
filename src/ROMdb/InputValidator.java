package ROMdb;

/**
 * Created by Derek Gaffney on 3/21/2017.
 */
public class InputValidator
{
    public static void checkPatternMatch(String inputString, InputType it) throws InputFormatException
    {
        if(!inputString.matches(it.getPattern()))
        {
            throw new InputFormatException("InputString '" + inputString + "' does not match pattern: " + it.getPatternName());
        }
    }

    public static void checkPatternDoesNotMatch(String inputString, InputType it) throws InputFormatException
    {
        if(inputString.matches(it.getPattern()))
        {
            throw new InputFormatException("InputString '" + inputString + "' matches pattern: " + it.getPatternName());
        }
    }
}
