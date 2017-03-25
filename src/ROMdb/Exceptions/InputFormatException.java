package ROMdb.Exceptions;

/**
 * Created by Anthony Orio on 3/21/2017.
 */
public class InputFormatException extends Exception
{
    /**
     * The exception to throw if the input is invalid.
     * @param message the string to check validity.
     */
    public InputFormatException(String message)
    {
        super(message);
    }
}
