package com.example.currencytransform;

import android.os.AsyncTask;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements EventsListener
{
    private static String file_url = "http://www.cbr.ru/scripts/XML_daily.asp";

    private ViewModel _thisViewModel;

    private EditText editSummText;
    private NumberPicker startCurrency;
    private NumberPicker endCurrency;
    private Button clickButton;
    private TextView resultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        InitilizeElements();
        InitilizeViewModel();
    }

    private void InitilizeViewModel()
    {
        new DownloadFileFromURL().execute(file_url);
        _thisViewModel=new ViewModel();
        _thisViewModel.addListener(this);
        _thisViewModel.ReadFromFile();
    }

    private void InitilizeElements()
    {
        editSummText =(EditText) findViewById(R.id.editSummText);

        startCurrency =(NumberPicker) findViewById(R.id.startCurrency);
        startCurrency.setMinValue(0);
        endCurrency =(NumberPicker) findViewById(R.id.endCurrency);
        endCurrency.setMinValue(0);
        clickButton=(Button) findViewById(R.id.clickButton);
        clickButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(!editSummText.getText().toString().equals("")){
                resultText.setText(_thisViewModel.Calculate(Double.parseDouble(editSummText.getText().toString()),startCurrency.getValue(),endCurrency.getValue()));
                }else
                {
                    resultText.setText("Не указана сумма");
                }
            }
        });
        resultText=(TextView)findViewById(R.id.resultText);
    }




    @Override
    public void eventsChanged(String eventName) {
        if(eventName.equals("ValCurs"))
        {
            ValCurs temp = _thisViewModel.GetValCurs();
            String[] names2 = new String[temp.getlist().size()];
            for(int i=0;i<temp.getlist().size();i++)
            {
                //String result;
                //result = new String(temp.getlist().get(i).getName().getBytes(), Charset.forName("windows-1251"));
                names2[i]=temp.getlist().get(i).getName();
            }
            startCurrency.setMaxValue(names2.length-1);
            startCurrency.setDisplayedValues(names2);
            endCurrency.setMaxValue(names2.length-1);
            endCurrency.setDisplayedValues(names2);
        }
    }


    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... f_url) {
            try {
                //set the download URL, a url that points to a file on the internet
                //this is the file to be downloaded
                URL url = new URL(f_url[0]);

                //create the new connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();


                //and connect!
                urlConnection.connect();
                //set the path where we want to save the file
                //in this case, going to save it on the root directory of the
                //sd card.
                File SDCardRoot = Environment.getExternalStorageDirectory();
                //create a new file, specifying the path, and the filename
                //which we want to save the file as.
                File file = new File(SDCardRoot,"cbr.xml");

                //this will be used to write the downloaded data into the file we created
                FileOutputStream fileOutput = new FileOutputStream(file);

                //this will be used in reading the data from the internet
                InputStream inputStream = urlConnection.getInputStream();

                //create a buffer...
                byte[] buffer = new byte[1024];
                int bufferLength = 0; //used to store a temporary size of the buffer

                //now, read through the input buffer and write the contents to the file
                while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
                    //add the data in the buffer to the file in the file output stream (the file on the sd card
                    fileOutput.write(buffer, 0, bufferLength);

                }
                //close the output stream when done
                fileOutput.close();
                //catch some possible errors...
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String file_url) {

        }

    }
}
