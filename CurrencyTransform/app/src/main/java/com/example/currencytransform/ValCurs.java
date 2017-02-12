package com.example.currencytransform;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by Александр on 12.02.2017.
 */
@Root
public class ValCurs {

    @Attribute
    private  String Date;
    @Attribute
    private String name;

    @ElementList(inline=true)
    private List<Valute> list;

    public String getDate()
    {
        return Date;
    }

    public String getName()
    {
        return name;
    }

    public List<Valute> getlist()
    {
        return list;
    }

}

