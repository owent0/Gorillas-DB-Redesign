package ROMdb.Helpers;

/**
 * Created by derek on 5/7/2017.
 *
 * Used to store valcodes that have both an id, and a value
 * ex: build valcode: id = 38, value = build1
 */
public class ComboItem
{
    private String id;
    private String value;

    public ComboItem(String id, String value)
    {
        this.id = id;
        this.value = value;
    }

    public String getId()
    {
        return this.id;
    }

    public String getValue()
    {
        return this.value;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString()
    {
        return value;
    }
}
