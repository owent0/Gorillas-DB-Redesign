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

    public FilterItem(String value, String name)
    {
        this.value = value;
        this.name = name;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String toString()
    {
        return this.name + ": " + this.value;
    }
}
