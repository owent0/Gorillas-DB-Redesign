package ROMdb.Helpers;

import java.util.ArrayList;

/**
 * Created by derek on 4/4/2017.
 * This class is used to store array lists within it.
 */
public class MapList<T>
{
    private String name;
    private ArrayList<T> list;

    /**
     * The default constructor of MapList.
     * @param name The key value name to retrieve the specific list.
     * @param list The list that is to be set to in this object.
     */
    public MapList(String name, ArrayList<T> list)
    {
        this.name = name;
        this.list = list;
    }

    /**
     * Gets the name pertaining to the list inside of the map.
     * @return The name pertaining to the list in the map.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the name pertaining to the list.
     * @param name The name that should associate with the list.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Gets the list inside of this object.
     * @return The list of the object.
     */
    public ArrayList<T> getList()
    {
        return list;
    }

    /**
     * Sets the list inside of this object.
     * @param list The list to set.
     */
    public void setList(ArrayList<T> list)
    {
        this.list = list;
    }
}
