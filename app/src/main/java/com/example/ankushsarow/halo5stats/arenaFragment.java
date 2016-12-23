package com.example.ankushsarow.halo5stats;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
 * Arena fragment for the arena tab - This fragment will display the user's Arena stats
 */
public class ArenaFragment extends Fragment {
    private String userGT, userSR;
    private final String USER_TAG = "user tag";
    private final String USER_SR = "user sr";
    private final String HIGHEST_RANK = "highest rank";
    private final String RANK_TIER = "rank tier";
    private final String CSR = "csr";
    private final String TOTAL_KILLS = "total kills";
    private final String TOTAL_DEATHS = "total deaths";
    private final String TOTAL_ASSISTS = "total assists";
    private final String TOTAL_WINS = "total wins";
    private final String TOTAL_LOSSES = "total losses";
    private final String TOTAL_TIES = "total ties";
    private final String TOTAL_GAMES = "total games";
    private final String[] RANK_TAGS = {"Unranked", "Bronze", "Silver", "Gold", "Platinum",
            "Diamond", "Onyx", "Champion"};
    private final String[] WEAPON_STATS = {"TotalWeaponKills", "TotalShotsFired", "TotalShotsLanded",
            "TotalHeadShots"};
    private Long mostUsedWeaponID;
    private HashMap<String, Integer> arenaData;
    private HashMap<Long, String> weaponData;

    private TextView killsText, deathsText, assistsText, rankText, rankTierText, csrText,
        gamesPlayedText, winsText, lossesText, wlRatioText, kdRatioText, tiesText, weaponNameText,
        weaponKillsText, weaponSFText, weaponSLText, weaponHSText, weaponAccText, gtText, srText;
    private View blank;

    private ImageView rankImage, profileImage;
    private ProgressBar progressBar;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userGT = getArguments().getString(USER_TAG);
        userSR = getArguments().getString(USER_SR);
        arenaData = new HashMap<>();
        weaponData = new HashMap<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_arena, container, false);

        blank = view.findViewById(R.id.blank);

        gtText = (TextView) view.findViewById(R.id.spartan_text_value);
        srText = (TextView) view.findViewById(R.id.spartan_rank_value);
        gtText.setText(userGT);
        srText.setText(userSR);

        rankText = (TextView) view.findViewById(R.id.rank_value);
        rankTierText = (TextView) view.findViewById(R.id.rank_tier);
        csrText = (TextView) view.findViewById(R.id.rank_csr);
        rankTierText.setVisibility(View.INVISIBLE);
        csrText.setVisibility(View.INVISIBLE);
        rankImage = (ImageView) view.findViewById(R.id.rankImg);

        killsText = (TextView) view.findViewById(R.id.total_kills_value);
        deathsText = (TextView) view.findViewById(R.id.total_deaths_value);
        assistsText = (TextView) view.findViewById(R.id.total_assists_value);
        kdRatioText = (TextView) view.findViewById(R.id.kd_ratio_value);
        gamesPlayedText = (TextView) view.findViewById(R.id.total_games_value);
        winsText = (TextView) view.findViewById(R.id.total_wins_value);
        lossesText = (TextView) view.findViewById(R.id.total_losses_value);
        tiesText = (TextView) view.findViewById(R.id.total_ties_value);
        wlRatioText = (TextView) view.findViewById(R.id.wl_ratio_value);
        progressBar = (ProgressBar) view.findViewById(R.id.pBar);

        weaponNameText = (TextView) view.findViewById(R.id.top_wep_value);
        weaponKillsText = (TextView) view.findViewById(R.id.top_wep_kills_val);
        weaponSFText = (TextView) view.findViewById(R.id.top_wep_sf_val);
        weaponSLText = (TextView) view.findViewById(R.id.top_wep_sl_val);
        weaponHSText = (TextView) view.findViewById(R.id.top_wep_hs_val);
        weaponAccText = (TextView) view.findViewById(R.id.top_wep_acc_val);

        profileImage = (ImageView) view.findViewById(R.id.profile_image);

        new LoadProfileImage().execute();
        new LoadArenaData().execute();
        new LoadWeaponMetaData().execute();
        return view;
    }

    /**
     * Load the data from the hashmap into the corresponding views
     */
    private void loadData() {
        setRankVals();

        killsText.setText(String.valueOf(arenaData.get(TOTAL_KILLS)));
        deathsText.setText(String.valueOf(arenaData.get(TOTAL_DEATHS)));
        assistsText.setText(String.valueOf(arenaData.get(TOTAL_ASSISTS)));

        if (arenaData.get(TOTAL_DEATHS) == 0) {
            kdRatioText.setText(String.valueOf((double) arenaData.get(TOTAL_KILLS)));
        } else {
            kdRatioText.setText(String.format("%.2f", ((double) arenaData.get(TOTAL_KILLS) /
                    arenaData.get(TOTAL_DEATHS))));
        }

        gamesPlayedText.setText(String.valueOf(arenaData.get(TOTAL_GAMES)));
        winsText.setText(String.valueOf(arenaData.get(TOTAL_WINS)));
        lossesText.setText(String.valueOf(arenaData.get(TOTAL_LOSSES)));
        tiesText.setText(String.valueOf(arenaData.get(TOTAL_TIES)));

        if (arenaData.get(TOTAL_LOSSES) == 0) {
            wlRatioText.setText(String.valueOf((double) arenaData.get(TOTAL_WINS)));
        } else {
            wlRatioText.setText(String.format("%.2f", ((double) arenaData.get(TOTAL_WINS) /
                    arenaData.get(TOTAL_LOSSES))));
        }
    }

    /**
     * Set the value for the highest rank, the CSR, and the Tier
     */
    private void setRankVals() {
        int id = arenaData.get(HIGHEST_RANK);
        switch(id) {
            case 0:
                rankText.setText(RANK_TAGS[0]);
                rankText.setText("Unranked");
                rankTierText.setVisibility(View.VISIBLE);
                rankImage.setImageResource(R.drawable.bronze);
                break;
            case 1:
                rankText.setText(RANK_TAGS[1]);
                rankTierText.setText(String.valueOf(arenaData.get(RANK_TIER)));
                rankTierText.setVisibility(View.VISIBLE);
                rankImage.setImageResource(R.drawable.bronze);
                break;
            case 2:
                rankText.setText(RANK_TAGS[2]);
                rankTierText.setText(String.valueOf(arenaData.get(RANK_TIER)));
                rankTierText.setVisibility(View.VISIBLE);
                rankImage.setImageResource(R.drawable.silver);
                break;
            case 3:
                rankText.setText(RANK_TAGS[3]);
                rankTierText.setText(String.valueOf(arenaData.get(RANK_TIER)));
                rankTierText.setVisibility(View.VISIBLE);
                rankImage.setImageResource(R.drawable.gold);
                break;
            case 4:
                rankText.setText(RANK_TAGS[4]);
                rankTierText.setText(String.valueOf(arenaData.get(RANK_TIER)));
                rankTierText.setVisibility(View.VISIBLE);
                rankImage.setImageResource(R.drawable.platinum);
                break;
            case 5:
                rankText.setText(RANK_TAGS[5]);
                rankTierText.setText(String.valueOf(arenaData.get(RANK_TIER)));
                rankTierText.setVisibility(View.VISIBLE);
                rankImage.setImageResource(R.drawable.diamond);
                break;
            case 6:
                rankText.setText(RANK_TAGS[6]);
                csrText.setText(String.valueOf(arenaData.get(CSR)));
                csrText.setVisibility(View.VISIBLE);
                rankImage.setImageResource(R.drawable.onyx);
                break;
            case 7:
                rankText.setText(RANK_TAGS[7]);
                csrText.setText(String.valueOf(arenaData.get(CSR)));
                csrText.setVisibility(View.VISIBLE);
                rankImage.setImageResource(R.drawable.champion);
                break;
            default:
                break;
        }
    }

    /**
     * Update the weapon data for the top weapon
     */
    private void loadWeaponData() {
        String weaponName = weaponData.get(mostUsedWeaponID);
        weaponNameText.setText(weaponName);
        weaponKillsText.setText(String.valueOf(arenaData.get(WEAPON_STATS[0])));
        weaponSFText.setText(String.valueOf(arenaData.get(WEAPON_STATS[1])));
        weaponSLText.setText(String.valueOf(arenaData.get(WEAPON_STATS[2])));
        weaponHSText.setText(String.valueOf(arenaData.get(WEAPON_STATS[3])));
        String weaponAcc = String.valueOf((int) ((double) arenaData.get(WEAPON_STATS[2]) /
                arenaData.get(WEAPON_STATS[1]) * 100)) + "%";
        weaponAccText.setText(weaponAcc);
    }

    /**
     * Retrieve the stats needed for the ArenaFragment from the Halo API
     */
    private class LoadArenaData extends AsyncTask<Void, Void, HashMap<String, Integer>> {
        private String urlUserGT = userGT.replaceAll(" ", "%20");
        private final String URL_STRING =
                "https://www.haloapi.com/stats/h5/servicerecords/arena?players=" + urlUserGT;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            blank.setVisibility(View.VISIBLE);
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
                    JSONObject arenaStatsNode = resultNode.getJSONObject("ArenaStats");

                    arenaData.put(HIGHEST_RANK, arenaStatsNode.getJSONObject("HighestCsrAttained").
                            getInt("DesignationId"));
                    arenaData.put(RANK_TIER, arenaStatsNode.getJSONObject("HighestCsrAttained").
                            getInt("Tier"));
                    arenaData.put(CSR, arenaStatsNode.getJSONObject("HighestCsrAttained").
                            getInt("Csr"));
                    arenaData.put(TOTAL_KILLS, arenaStatsNode.getInt("TotalSpartanKills"));
                    arenaData.put(TOTAL_DEATHS, arenaStatsNode.getInt("TotalDeaths"));
                    arenaData.put(TOTAL_WINS, arenaStatsNode.getInt("TotalGamesWon"));
                    arenaData.put(TOTAL_LOSSES, arenaStatsNode.getInt("TotalGamesLost"));
                    arenaData.put(TOTAL_ASSISTS, arenaStatsNode.getInt("TotalAssists"));
                    arenaData.put(TOTAL_GAMES, arenaStatsNode.getInt("TotalGamesCompleted"));
                    arenaData.put(TOTAL_TIES, arenaStatsNode.getInt("TotalGamesTied"));


                    mostUsedWeaponID = arenaStatsNode.getJSONObject("WeaponWithMostKills").
                            getJSONObject("WeaponId").getLong("StockId");
                    arenaData.put(WEAPON_STATS[0], arenaStatsNode.getJSONObject("WeaponWithMostKills").
                            getInt("TotalKills"));
                    arenaData.put(WEAPON_STATS[1], arenaStatsNode.getJSONObject("WeaponWithMostKills").
                            getInt("TotalShotsFired"));
                    arenaData.put(WEAPON_STATS[2], arenaStatsNode.getJSONObject("WeaponWithMostKills").
                            getInt("TotalShotsLanded"));
                    arenaData.put(WEAPON_STATS[3], arenaStatsNode.getJSONObject("WeaponWithMostKills").
                            getInt("TotalHeadshots"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
            return arenaData;
        }

        @Override
        protected void onPostExecute(HashMap<String, Integer> arenaData) {
            super.onPostExecute(arenaData);
            progressBar.setVisibility(View.INVISIBLE);
            blank.setVisibility(View.INVISIBLE);
            loadData();
        }
    }

    /**
     * Retrieve the weapon meta data from the Halo API
     */
    private class LoadWeaponMetaData extends AsyncTask<Void, Void, HashMap<Long, String>> {
        private final String URL_STRING =
                "https://www.haloapi.com/metadata/h5/metadata/weapons";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected HashMap<Long, String> doInBackground(Void... voids) {
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
                    JSONArray jArray = new JSONArray(sb.toString());

                    for (int i = 0; i < jArray.length(); i++) {
                        weaponData.put(jArray.getJSONObject(i).getLong("id"),
                                jArray.getJSONObject(i).getString("name"));
                    }
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
            return weaponData;
        }

        @Override
        protected void onPostExecute(HashMap<Long, String> weaponData) {
            super.onPostExecute(weaponData);
            progressBar.setVisibility(View.INVISIBLE);
            loadWeaponData();
        }
    }

    /**
     * Retrieve the users profile image
     */
    private class LoadProfileImage extends AsyncTask<Void, Void, Bitmap> {
        private String urlUserGT = userGT.replaceAll(" ", "%20");
        private final String URL_STRING =
                "https://www.haloapi.com/profile/h5/profiles/" + urlUserGT + "/spartan";
        Bitmap profileImg;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            HttpURLConnection httpURLConnection = null;

            try {
                URL url = new URL(URL_STRING);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestProperty("Ocp-Apim-Subscription-Key",
                        "API KEY");
                httpURLConnection.connect();

                InputStream inputStream = httpURLConnection.getInputStream();
                profileImg = BitmapFactory.decodeStream(inputStream);

                if (inputStream != null) {
                    inputStream.close();
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
            return profileImg;
        }

        @Override
        protected void onPostExecute(Bitmap profileImg) {
            super.onPostExecute(profileImg);
            progressBar.setVisibility(View.INVISIBLE);
            profileImage.setImageBitmap(profileImg);
        }
    }
}
