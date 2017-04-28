package ROMdb.Helpers;

/**
 * Created by Anthony Orio on 3/21/2017.
 */
public enum InputType
{
    /**
     * The patterns to use when verifying input. The name of the pattern
     * is the enumeration name.
     */
    ALPHA("^[a-zA-Z]*$", "Alpha"),
    ALPHA_SPACE("^[a-zA-Z\\s]*$", "Alpha Space"),
    ALPHA_NUMERIC("^[a-zA-Z0-9\\.\\-]*$", "Alpha Numeric"),
    ALPHA_NUMERIC_SPACE("^[a-zA-Z0-9\\s]*$", "Alpha Numeric Space"),
    ALPHA_NUMERIC_PERIOD("^[a-zA-Z0-9.]*$", "Alpha Numeric With Period"),
    DOUBLE("\\d+(\\.\\d{1,25})?", "Double"),
    EMPTY_STRING("^$", "Empty String"),
    INT("\\d+", "Integer"),
    POS_NEG_NUM("[-+]?\\d*\\.?\\d+", "Positive or Negative Number"),
    WHITE_SPACE("^\\s*$", "White Space"),
    COMPLEX_PASSWORD("(?=^.{6,15}$)(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&amp;*()_+}{&quot;:;'?/&gt;.&lt;,])(?!.*\\s).*$", "Complex Password");

    // NOTE: Use DOUBLE pattern type when trying to match any number (with decimal place or not)

    private String pattern;
    private String patternName;

    /**
     * Checks for the validity on the string using the specified pattern.
     * @param pattern The pattern to use.
     * @param patternName The pattern name.
     */
    InputType(String pattern, String patternName)
    {
        this.pattern = pattern;
        this.patternName = patternName;
    }

    /**
     * Gets the pattern.
     * @return The pattern.
     */
    public String getPattern()
    {
        return this.pattern;
    }

    /**
     * Gets the pattern name.
     * @return The pattern name.
     */
    public String getPatternName()
    {
        return this.patternName;
    }
}
