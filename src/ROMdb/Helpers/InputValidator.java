package ROMdb.Helpers;

import ROMdb.Exceptions.InputFormatException;

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
     * Checks to see if the password pattern is a match.
     * @param inputString the string to check the pattern against.
     * @param it the input type.
     * @throws InputFormatException If input is not correct.
     */
    public static void checkPasswordPatternMatch(String inputString, InputType it) throws InputFormatException
    {
        if(!inputString.matches(it.getPattern()))
        {
            throw new InputFormatException("The password entered must be 6-15 characters in length, contain at least one lowercase letter, one uppercase letter, one number and one symbol(#?!@$%^&*-).");
        }
    }

    /**
     * Checks to see if the string is empty
     * @param inputString the string to check
     * @param it the input type
     * @return if the string is empty
     */
    public static boolean checkEmptyString(String inputString, InputType it) {
        if (inputString.matches(it.getPattern()))
        {
            return true;
        }
        return false;
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

    /**
     * Check that the pattern matches. Takes in a double as a parameter and matches
     * it to the pattern passed as InputType.
     * @param inputDouble The double that is to passed as a parameter.
     * @param it The InputType to compare with.
     * @throws InputFormatException If the inputDouble does not match the InputType.
     */
    public static void checkPatternMatch(Double inputDouble, InputType it) throws InputFormatException
    {
        if(!it.getPatternName().equals(InputType.DOUBLE.getPatternName()))
        {
            throw new InputFormatException("InputDouble '" + inputDouble + "' does not match pattern: " + it.getPatternName());
        }
    }

    /**
     * Check that the pattern matches. Takes in an int as a parameter and matches
     * it to the pattern passed as InputType.
     * @param inputInt The int that will be compared to the InputType.
     * @param it The InputType to compare against the passed integer.
     * @throws InputFormatException If the integer does not match the InputType.
     */
    public static void checkPatternMatch(int inputInt, InputType it) throws InputFormatException
    {
        if(!it.getPatternName().equals(InputType.INT.getPatternName()))
        {
            throw new InputFormatException("InputInt '" + inputInt + "' does not match pattern: " + it.getPatternName());
        }
    }

    /**
     * Checks to make sure that val is between 0 and 100 inclusively.
     * @param val The value to check the boundaries of.
     * @throws InputFormatException If val does not fall between 0 and 100 inclusively.
     */
    public static void oneToHundredInclusive(double val) throws InputFormatException
    {
        if(val < 0 || val > 100)
        {
            throw new InputFormatException("Input: " + val + " not in acceptable boundaries [0-100].");
        }
    }

    /**
     * Checks if val is a negative number and throws an exception if it is true.
     * @param val The value that will be checked if it is negative.
     * @throws InputFormatException If the value is a negative number.
     */
    public static void checkNegative(double val) throws InputFormatException
    {
        if(val < 0)
        {
            throw new InputFormatException("Input: " + val + " cannot be negative.");
        }
    }
}
