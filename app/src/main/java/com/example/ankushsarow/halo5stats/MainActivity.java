package com.example.ankushsarow.halo5stats;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private EditText gtInput;
    private TextView invalidText;
    private Button statsButton;
    public final String USER_TAG = "user tag";
    private String userGT;
    private boolean validGT;
    private final String EMPTY_INPUT_MSG = "Please enter a valid Gamertag.";
    private final String GT_NOT_FOUND_MSG = "Gamertag not found, please try again.";
    private final String SERVICE_ERROR_MSG = "Stats not currently retrievable, please try again.";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gtInput = (EditText) findViewById(R.id.gt_input);
        invalidText = (TextView) findViewById(R.id.invalid_gt);
        statsButton = (Button) findViewById(R.id.stats_button);

        /**
         * Handle the user pressing the enter key on input into the EditText field
         * by hiding the keyboard
         */
        gtInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (keyEvent != null && (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager imm = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);

                    return true;
                }
                return false;
            }
        });

        /**
         * Handle the user pressing the stats button - Retrieve the text from gamertag input,
         * if it is invalid, display the invalid text field, otherwise, switch activities
         * to the stats overview activity (done in the AsyncTask below)
         */
        statsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userGT = gtInput.getText().toString().trim();
                if (userGT.isEmpty()) {
                    invalidText.setText(EMPTY_INPUT_MSG);
                    invalidText.setVisibility(View.VISIBLE);
                    return;
                }

                new checkGTTask().execute();

                if (!statsButton.isEnabled()) {
                    statsButton.setEnabled(true);
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * The purpose of this class is to see if the gamertag input in the text field
     * returns a valid response from the Halo API - It will do so by checking the value
     * returned from "ResultCode" - 0 Indicates a success, 1 indicates the GT was not found
     * 2 indicates service failure and 3 indicates service unavailable
     */
    public class checkGTTask extends AsyncTask<Void, Void, String> {
        String urlUserGT = userGT.replaceAll(" ", "%20");
        private final String URL_STRING =
                "https://www.haloapi.com/stats/h5/servicerecords/custom?players=" + urlUserGT;

        @Override
        public void onPreExecute() {
            statsButton.setEnabled(false);
        }

        @Override
        protected String doInBackground(Void... voids) {
            HttpURLConnection httpURLConnection = null;

            try {
                URL url = new URL(URL_STRING);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestProperty("Ocp-Apim-Subscription-Key",
                        "API KEY");
                httpURLConnection.connect();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuffer sb = new StringBuffer();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + '\n');
                }

                if (reader != null) {
                    reader.close();
                }
                return sb.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }
        }

        @Override
        public void onPostExecute(String response) {
            super.onPostExecute(response);
            int resultCode = -1;

            if (response == null) {
                validGT = false;
                resultCode = -1;
            } else {
                //Retrieve the resultCode value
                try {
                    JSONObject jObject = new JSONObject(response);
                    JSONArray jArray = jObject.getJSONArray("Results");
                    JSONObject jObjectCode = jArray.getJSONObject(0);
                    resultCode = jObjectCode.getInt("ResultCode");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            //If a valid GT was entered, start the StatsActivity
            if (resultCode == 0) {
                if (invalidText.getVisibility() == View.VISIBLE) {
                    invalidText.setVisibility(View.INVISIBLE);
                }
                Intent intent = new Intent(MainActivity.this, StatsActivity.class);
                intent.putExtra(USER_TAG, userGT);
                startActivity(intent);
            } else if (resultCode == 1){
                //GT input was not valid
                invalidText.setText(GT_NOT_FOUND_MSG);
                if (invalidText.getVisibility() == View.INVISIBLE) {
                    invalidText.setVisibility(View.VISIBLE);
                }
            } else {
                //If this is reached, the service is down
                invalidText.setText(SERVICE_ERROR_MSG);
                if (invalidText.getVisibility() == View.INVISIBLE) {
                    invalidText.setVisibility(View.VISIBLE);
                }
            }
            statsButton.setEnabled(true);
        }
    }
}
