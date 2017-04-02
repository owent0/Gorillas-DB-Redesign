package ROMdb.Exceptions;

/**
 * Created by Derek Gaffney on 3/21/2017.
 */
public class UnknownDatabaseTypeException extends Exception
{
    /**
     * The exception to throw if an unrecognized DatabaseType object is encountered.
     * @param message the string to check validity.
     */
    public UnknownDatabaseTypeException(String message)
    {
        super("Unrecognized DatabaseType encountered! Local Message: '" + message + "'");
    }

    public UnknownDatabaseTypeException(){ super(); }

}
