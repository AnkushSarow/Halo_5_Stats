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
    private final String TOTAL_KILLS = "total kills";
    private final String TOTAL_DEATHS = "total deaths";
    private final String TOTAL_ASSISTS = "total assists";
    private final String TOTAL_WINS = "total wins";
    private final String TOTAL_LOSSES = "total losses";
    private final String TOTAL_TIES = "total ties";
    private final String TOTAL_GAMES = "total games";
    private final String TOTAL_MELEES = "total melees";
    private final String TOTAL_GP = "total gp";
    private final String TOTAL_ASSASSINATIONS = "total assassinations";
    private final String TOTAL_SC = "total sc";
    private final String TOTAL_PW = "total pw";
    private final String TOTAL_GRENADE = "total grenade";


    private HashMap<String, Integer> warzoneData;

    private TextView killsText, deathsText, assistsText, winsText, lossesText, tiesText,
            gamesPlayedText, wlRatioText, kdRatioText, meleesText, gpText, assassText,
            scText, pwText, grenadeText;
    private ProgressBar progressBar;
    private View blank;

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
        blank = view.findViewById(R.id.blank);
        killsText = (TextView) view.findViewById(R.id.total_kills_value_W);
        deathsText = (TextView) view.findViewById(R.id.total_deaths_value_W);
        assistsText = (TextView) view.findViewById(R.id.total_assists_value_W);
        kdRatioText = (TextView) view.findViewById(R.id.kd_ratio_value_W);
        gamesPlayedText = (TextView) view.findViewById(R.id.total_games_value_W);
        winsText = (TextView) view.findViewById(R.id.total_wins_value_W);
        lossesText = (TextView) view.findViewById(R.id.total_losses_value_W);
        tiesText = (TextView) view.findViewById(R.id.total_ties_value_W);
        wlRatioText = (TextView) view.findViewById(R.id.wl_ratio_value_W);
        meleesText = (TextView) view.findViewById(R.id.melee_kills_value);
        gpText = (TextView) view.findViewById(R.id.ground_pounds_value);
        assassText = (TextView) view.findViewById(R.id.assassinations_value);
        scText = (TextView) view.findViewById(R.id.spartan_charges_value);
        pwText = (TextView) view.findViewById(R.id.power_weapon_value);
        grenadeText = (TextView) view.findViewById(R.id.grenade_kills_value);
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
        tiesText.setText(String.valueOf(warzoneData.get(TOTAL_TIES)));

        if (warzoneData.get(TOTAL_LOSSES) == 0) {
            wlRatioText.setText(String.valueOf((double) warzoneData.get(TOTAL_WINS)));
        } else {
            wlRatioText.setText(String.format("%.2f", ((double) warzoneData.get(TOTAL_WINS) /
                    warzoneData.get(TOTAL_LOSSES))));
        }

        meleesText.setText(String.valueOf(warzoneData.get(TOTAL_MELEES)));
        gpText.setText(String.valueOf(warzoneData.get(TOTAL_GP)));
        scText.setText(String.valueOf(warzoneData.get(TOTAL_SC)));
        pwText.setText(String.valueOf(warzoneData.get(TOTAL_PW)));
        grenadeText.setText(String.valueOf(warzoneData.get(TOTAL_GRENADE)));
        assassText.setText(String.valueOf(warzoneData.get(TOTAL_ASSASSINATIONS)));
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
            blank.setVisibility(View.VISIBLE);
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

                    warzoneData.put(TOTAL_KILLS, warzoneStatsNode.getInt("TotalKills"));
                    warzoneData.put(TOTAL_DEATHS, warzoneStatsNode.getInt("TotalDeaths"));
                    warzoneData.put(TOTAL_WINS, warzoneStatsNode.getInt("TotalGamesWon"));
                    warzoneData.put(TOTAL_LOSSES, warzoneStatsNode.getInt("TotalGamesLost"));
                    warzoneData.put(TOTAL_ASSISTS, warzoneStatsNode.getInt("TotalAssists"));
                    warzoneData.put(TOTAL_GAMES, warzoneStatsNode.getInt("TotalGamesCompleted"));
                    warzoneData.put(TOTAL_TIES, warzoneStatsNode.getInt("TotalGamesTied"));
                    warzoneData.put(TOTAL_MELEES, warzoneStatsNode.getInt("TotalMeleeKills"));
                    warzoneData.put(TOTAL_ASSASSINATIONS, warzoneStatsNode.
                            getInt("TotalAssassinations"));
                    warzoneData.put(TOTAL_SC, warzoneStatsNode.getInt("TotalShoulderBashKills"));
                    warzoneData.put(TOTAL_PW, warzoneStatsNode.getInt("TotalPowerWeaponKills"));
                    warzoneData.put(TOTAL_GRENADE, warzoneStatsNode.getInt("TotalGrenadeKills"));
                    warzoneData.put(TOTAL_GP, warzoneStatsNode.getInt("TotalGroundPoundKills"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }
            return warzoneData;
        }

        @Override
        protected void onPostExecute(HashMap<String, Integer> warzoneData) {
            super.onPostExecute(warzoneData);
            progressBar.setVisibility(View.INVISIBLE);
            blank.setVisibility(View.INVISIBLE);
            loadData();
        }
    }
}

