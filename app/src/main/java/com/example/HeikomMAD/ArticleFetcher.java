package com.example.HeikomMAD;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
public class ArticleFetcher extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... urls) {
        try {
            URL url = new URL(urls[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set request method to GET
            connection.setRequestMethod("GET");

            // Get the response code
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read the HTML content
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }

                // Close the reader
                reader.close();

                return stringBuilder.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    // Handle the result in onPostExecute method
    @Override
    protected void onPostExecute(String htmlContent) {
        if (htmlContent != null) {
            // Parse the HTML content using Jsoup
            Document document = Jsoup.parse(htmlContent);

            // Extract the title and image URL using Jsoup methods
            String title = document.title();
            String imageUrl = document.select("meta[property=og:image]").attr("content");

            // Now you can use 'title' and 'imageUrl' in your UI components
        }
    }
}