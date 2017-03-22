package ROMdb;

/**
 * Created by Anthony Orio on 3/21/2017.
 */
public enum InputType
{
    INT("\\d+", "Integer"),
    DOUBLE("\\d+(\\.\\d{1,25})?", "Double"),
    ALPHA("^[a-zA-Z]*$", "Alpha"),
    ALPHA_SPACE("^[a-zA-Z\\s]*$", "Alpha Space"),
    ALPHA_NUMERIC("^[a-zA-Z0-9]*$", "Alpha Numeric"),
    ALPHA_NUMERIC_SPACE("^[a-zA-Z0-9\\s]*$", "Alpha Numeric Space"),
    EMPTY_STRING("^$", "Empty String"),
    WHITE_SPACE("^\\s*$", "White Space");

    private String pattern;
    private String patternName;

    InputType(String pattern, String patternName)
    {
        this.pattern = pattern;
        this.patternName = patternName;
    }

    public String getPattern()
    {
        return this.pattern;
    }

    public String getPatternName()
    {
        return this.patternName;
    }
}
