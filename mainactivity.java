import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherActivity extends AppCompatActivity {
    private static final String OPEN_WEATHER_MAP_API = "YOUR_API_KEY_HERE";
    private TextView temperatureTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        temperatureTextView = findViewById(R.id.temperatureTextView);

        // Replace with your own location or use the user's location.
        String city = "Budapest";
        String weatherUrl = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + OPEN_WEATHER_MAP_API;

        new WeatherTask().execute(weatherUrl);
    }

    private class WeatherTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String data;
            try {
                // Connect to the API and fetch data.
                HttpURLConnection connection = (HttpURLConnection) new URL(params[0]).openConnection();
                connection.addRequestProperty("x-api-key", OPEN_WEATHER_MAP_API);

                InputStream is = connection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = br.readLine()) != null) {
                    result.append(line);
                }

                data = result.toString();

                // Close the connection and input stream.
                is.close();
                connection.disconnect();
            } catch (Exception e) {
                data = null;
            }
            return data;
        }

        @Override
        protected void onPostExecute(String data) {
            super.onPostExecute(data);

            if (data != null) {
                try {
                    JSONObject json = new JSONObject(data);
                    JSONObject main = json.getJSONObject("main");
                    double temperature = main.getDouble("temp");

                    // Convert temperature from Kelvin to Celsius and display it.
                    temperature = temperature - 273.15;
                    temperatureTextView.setText("Temperature: " + temperature + "°C");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
