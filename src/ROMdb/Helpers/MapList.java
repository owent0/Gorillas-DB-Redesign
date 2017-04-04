package ROMdb.Helpers;

import java.util.ArrayList;

/**
 * Created by derek on 4/4/2017.
 */
public class MapList<T>
{
    private String name;
    private ArrayList<T> list;

    public MapList(String name, ArrayList<T> list)
    {
        this.name = name;
        this.list = list;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public ArrayList<T> getList()
    {
        return list;
    }

    public void setList(ArrayList<T> list)
    {
        this.list = list;
    }
}
