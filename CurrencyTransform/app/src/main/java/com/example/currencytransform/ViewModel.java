package com.example.currencytransform;
import android.os.Environment;
import android.util.Log;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

interface EventsListener {
    void eventsChanged(String eventName) throws UnsupportedEncodingException;
}

public class ViewModel {

    private List<EventsListener> listeners;

    public ViewModel() {
        listeners = new ArrayList<EventsListener>();
    }

    public void ReadFromFile()
    {
        File sdcard = Environment.getExternalStorageDirectory();

        File file = new File(sdcard,"cbr.xml");

        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(new String(line.getBytes("UTF-8"),"windows-1251"));
                //text.append('\n');
            }
            //Log.e("Error: ", String.valueOf(text));
            br.close();
            Serializer serializer = new Persister();

            ValCurs example = serializer.read(ValCurs.class, text.toString().replaceAll(",","."));
            SetValCurs(example);
            Log.e("Error: ", String.valueOf(example));

        }
        catch (IOException e) {
            //You'll need to add proper error handling here
        } catch (Exception e) {
            Log.e("Error: ", "error");
            e.printStackTrace();
        }
    }

    private ValCurs _valCurs;
    public void SetValCurs(ValCurs valCurs)
    {
        _valCurs=valCurs;
        OnPropertyChanged("ValCurs");
    }
    public ValCurs GetValCurs()
    {
        return _valCurs;
    }


    public void addListener(EventsListener toAdd) {
        listeners.add(toAdd);
    }

    private void OnPropertyChanged(String eventName)
    {
        for(EventsListener el:listeners)
        {
            try {
                el.eventsChanged(eventName);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    public String Calculate(double sum, int startVal, int endVal)
    {
        Valute start = _valCurs.getlist().get(startVal);
        Valute end = _valCurs.getlist().get(endVal);

        double temp1 = sum*start.getValue()/start.getNominal();
        double temp2 = temp1/end.getValue()/end.getNominal();

        return String.valueOf(temp2);
    }

}
