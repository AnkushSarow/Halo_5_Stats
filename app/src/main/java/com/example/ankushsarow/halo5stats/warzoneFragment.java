package com.example.ankushsarow.halo5stats;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
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
import java.util.HashMap;

/**
 * Warzone fragment for the warzone tab - This fragment will display the user's warzone stats
 */
public class WarzoneFragment extends Fragment {
    private String userGT;
    private final String USER_TAG = "user tag";
    private String playtime;
    private final String TOTAL_KILLS = "total_kills";
    private final String TOTAL_DEATHS = "total_deaths";
    private final String TOTAL_ASSISTS = "total_assists";
    private final String TOTAL_WINS = "total_wins";
    private final String TOTAL_LOSSES = "total_losses";
    private final String TOTAL_GAMES = "total_games";

    private HashMap<String, Integer> warzoneData;

    private TextView killsText;
    private TextView deathsText;
    private TextView assistsText;
    private TextView playtimeText;
    private TextView winsText;
    private TextView lossesText;
    private TextView gamesPlayedText;
    private TextView wlRatioText;
    private TextView kdRatioText;
    private ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userGT = getArguments().getString(USER_TAG);
        warzoneData = new HashMap<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_warzone, container, false);

        killsText = (TextView) view.findViewById(R.id.total_kills_value_W);
        deathsText = (TextView) view.findViewById(R.id.total_deaths_value_W);
        assistsText = (TextView) view.findViewById(R.id.total_assists_value_W);
        kdRatioText = (TextView) view.findViewById(R.id.kd_ratio_value_W);
        playtimeText = (TextView) view.findViewById(R.id.total_playtime_value_W);
        gamesPlayedText = (TextView) view.findViewById(R.id.total_games_value_W);
        winsText = (TextView) view.findViewById(R.id.total_wins_value_W);
        lossesText = (TextView) view.findViewById(R.id.total_losses_value_W);
        wlRatioText = (TextView) view.findViewById(R.id.wl_ratio_value_W);
        progressBar = (ProgressBar) view.findViewById(R.id.pBar_W);
        new LoadWarzoneData().execute();

        return view;
    }

    private void loadData() {
        killsText.setText(String.valueOf(warzoneData.get(TOTAL_KILLS)));
        deathsText.setText(String.valueOf(warzoneData.get(TOTAL_DEATHS)));
        assistsText.setText(String.valueOf(warzoneData.get(TOTAL_ASSISTS)));

        if (warzoneData.get(TOTAL_DEATHS) == 0) {
            kdRatioText.setText(String.valueOf((double) warzoneData.get(TOTAL_KILLS)));
        } else {
            kdRatioText.setText(String.format("%.2f", ((double) warzoneData.get(TOTAL_KILLS) /
                    warzoneData.get(TOTAL_DEATHS))));
        }

        gamesPlayedText.setText(String.valueOf(warzoneData.get(TOTAL_GAMES)));
        winsText.setText(String.valueOf(warzoneData.get(TOTAL_WINS)));
        lossesText.setText(String.valueOf(warzoneData.get(TOTAL_LOSSES)));

        if (warzoneData.get(TOTAL_LOSSES) == 0) {
            wlRatioText.setText(String.valueOf((double) warzoneData.get(TOTAL_WINS)));
        } else {
            wlRatioText.setText(String.format("%.2f", ((double) warzoneData.get(TOTAL_WINS) /
                    warzoneData.get(TOTAL_LOSSES))));
        }
    }
    /**
     * Retrieve the stats needed for the WarzoneFragment from the Halo API
     */
    private class LoadWarzoneData extends AsyncTask<Void, Void, HashMap<String, Integer>> {
        private String urlUserGT = userGT.replaceAll(" ", "%20");
        private final String URL_STRING =
                "https://www.haloapi.com/stats/h5/servicerecords/warzone?players=" + urlUserGT;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected HashMap<String, Integer> doInBackground(Void... voids) {
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
                try {
                    JSONObject jObject = new JSONObject(sb.toString());
                    JSONArray jArray = jObject.getJSONArray("Results");
                    JSONObject resultsNode = jArray.getJSONObject(0);
                    JSONObject resultNode = resultsNode.getJSONObject("Result");
                    JSONObject warzoneStatsNode = resultNode.getJSONObject("WarzoneStat");

                    warzoneData.put(TOTAL_KILLS, warzoneStatsNode.getInt("TotalSpartanKills"));
                    warzoneData.put(TOTAL_DEATHS, warzoneStatsNode.getInt("TotalDeaths"));
                    warzoneData.put(TOTAL_WINS, warzoneStatsNode.getInt("TotalGamesWon"));
                    warzoneData.put(TOTAL_LOSSES, warzoneStatsNode.getInt("TotalGamesLost"));
                    warzoneData.put(TOTAL_ASSISTS, warzoneStatsNode.getInt("TotalAssists"));
                    warzoneData.put(TOTAL_GAMES, warzoneStatsNode.getInt("TotalGamesCompleted"));
                    playtime = warzoneStatsNode.getString("TotalTimePlayed");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return warzoneData;
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
        protected void onPostExecute(HashMap<String, Integer> warzoneData) {
            super.onPostExecute(warzoneData);
            progressBar.setVisibility(View.INVISIBLE);
            loadData();
        }
    }
}

