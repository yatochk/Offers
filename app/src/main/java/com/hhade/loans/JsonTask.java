package com.hhade.loans;

import android.os.AsyncTask;

import com.hhade.loans.fragments.CardFragment;
import com.hhade.loans.fragments.LoansFragment;
import com.hhade.loans.fragments.OtherFragment;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

class JsonTask extends AsyncTask<Void, Void, String> {

    private String resultJson = "";
    private LoansFragment loansFragment;
    private CardFragment cardsFragment;
    private OtherFragment otherFragment;
    private String urlString;

    JsonTask(LoansFragment loansFragment, CardFragment cardFragment, OtherFragment otherFragment, String urlString) {
        this.loansFragment = loansFragment;
        this.cardsFragment = cardFragment;
        this.otherFragment = otherFragment;
        this.urlString = urlString;
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            URL url = new URL(urlString);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            resultJson = buffer.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultJson;
    }

    @Override
    protected void onPostExecute(String resultJson) {
        super.onPostExecute(resultJson);

        //JsonParser jsonParser = new JsonParser(resultJson);
        JsonParser jsonParser = new JsonParser(urlString);
        loansFragment.initAdapter(jsonParser.getOffers(0), jsonParser.getUrlAllOffers());
        cardsFragment.initAdapter(jsonParser.getOffers(1));
        otherFragment.initAdapter(jsonParser.getOffers(2));
    }
}