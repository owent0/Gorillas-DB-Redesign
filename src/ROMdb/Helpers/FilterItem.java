package ROMdb.Helpers;

/**
 * Created by Derek Gaffney on 4/2/2017.
 * Describes a FilterItem ( a piece of criteria used to filter a results set,
 * which will consist of the name of the filter (Ex: Age),
 * and value of the filter (Ex: 21).
 */
public class FilterItem
{
    private String value;
    private String name;

    /**
     * Filter item constructor.
     * @param value The value of the filter.
     * @param name The name of the filter to filter by.
     */
    public FilterItem(String value, String name)
    {
        this.value = value;
        this.name = name;
    }

    /**
     * Retrieves the value.
     * @return The value.
     */
    public String getValue()
    {
        return value;
    }

    /**
     * Sets the value.
     * @param value The new value to set.
     */
    public void setValue(String value)
    {
        this.value = value;
    }

    /**
     * Retrieves the filter name.
     * @return The name of the filter.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the name of the filter type.
     * @param name The new name of the filter.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * The override of the toString method to display name and value.
     * @return The string representation of this object consisting of name and value.
     */
    public String toString()
    {
        return this.name + ": " + this.value;
    }
}
