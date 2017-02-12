package com.example.currencytransform;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="Valute")
public class Valute
{
    @Attribute
    private  String ID;
    @Element
    private  int NumCode;
    @Element
    private  String CharCode;
    @Element
    private  int Nominal;
    @Element
    private  String Name;
    @Element
    private  double Value;

    public String getID()
    {
        return ID;
    }

    public int getNumCode()
    {
        return NumCode;
    }

    public String getCharCode()
    {
        return CharCode;
    }

    public int getNominal()
    {
        return Nominal;
    }

    public String getName()
    {
        return Name;
    }

    public double getValue()
    {
        return Value;
    }
}

