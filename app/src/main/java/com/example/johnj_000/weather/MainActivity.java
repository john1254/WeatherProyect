package com.example.johnj_000.weather;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends Activity {
    EditText city;
    TextView description,EditCity;
    TextView temp;
    Button button;
    String findCity;
    ImageView imgView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditCity = (EditText) findViewById(R.id.cityeditText);
        city = (EditText) findViewById(R.id.cityText);
        description = (TextView) findViewById(R.id.condDescr);
        temp = (TextView) findViewById(R.id.temp);
        button = (Button) findViewById(R.id.button);
        imgView = (ImageView) findViewById(R.id.condIcon);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WeatherTask task = new WeatherTask();
                findCity = EditCity.getText().toString();
                task.execute(findCity);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class WeatherTask extends AsyncTask<String, Void, Void> {
        private static final String TAG = "WeatherTask";
        private String Error = null;
        private ProgressDialog Dialog = new ProgressDialog(MainActivity.this);
        String data = "";

        @override
        protected  Void onPreExecute(){
            Dialog.setMessage("Por favor espere");
            Dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try{
                data = ((new WeatherHttpClient()).getWeatherData(params[0]));
            }catch (Exception ex){
                Error = ex.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid){
            Dialog.dismiss();
            if (Error == null){
                String OutputData = "";
                byte[] imagen;
                Double celsius = 0.0;
                JSONObject jsonResponse;
                try{
                    jsonResponse = new JSONObject(data);
                    OutputData = jsonResponse.optString("name");
                    city.setText(OutputData);

                    OutputData = jsonResponse.getJSONObject("main").optString("temp");
                    celsius = Double.parseDouble(OutputData)-273.15;
                    temp.setText(celsius+" °C");

                    OutputData = jsonResponse.getJSONArray("weather").getJSONObject(0).optString("main")+" , "
                            +jsonResponse.getJSONArray("weather").getJSONObject(0).optString("description");
                    description.setText(OutputData);

                    OutputData = jsonResponse.getJSONArray("weather").getJSONObject(0).optString("icon");
                    imagen = ((new WeatherHttpClient()).getImage(OutputData));
                    if (imagen != null && imagen.length > 0) {
                        Bitmap img = BitmapFactory.decodeByteArray(imagen, 0, imagen.length);
                        imgView.setImageBitmap(img);
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
