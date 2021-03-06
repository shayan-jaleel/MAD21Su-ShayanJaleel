package com.example.numad21su_mohammadsjaleel;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class WebServiceActivity extends AppCompatActivity {

    private static final String TAG = "WebServiceActivity";
    private static final String API_QUERY = "&apikey=4c810120";
    private static final String SEARCH_QUERY = "https://www.omdbapi.com/?s=";
    private static final String DETAILS_QUERY = "https://www.omdbapi.com/?i=";

    private EditText mURLEditText;
    private TextView mTitleTextView;
    private ListView resultsListView;
    private ArrayList<String> resultsStringList;
    private ProgressBar spinner;
    ArrayAdapter<String> stringArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_service);

        mURLEditText = (EditText)findViewById(R.id.URL_editText);
        mTitleTextView = (TextView)findViewById(R.id.result_textview);
        resultsListView = (ListView) findViewById(R.id.result_listview);

        resultsStringList = new ArrayList<>();
        stringArrayAdapter = new ArrayAdapter<>(App.context,
                android.R.layout.simple_list_item_1, resultsStringList);
        resultsListView.setAdapter(stringArrayAdapter);
        spinner = (ProgressBar)findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);

        if(savedInstanceState != null && savedInstanceState.getStringArrayList("ResultsList") != null){
            resultsStringList.addAll(savedInstanceState.getStringArrayList("ResultsList"));
            stringArrayAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
//        TextView textView = findViewById(R.id.text_pressed);
        savedInstanceState.putStringArrayList("ResultsList", resultsStringList);
    }

    public void callWebserviceButtonHandler(View view){
        PingWebServiceTask task = new PingWebServiceTask();
        try {
            String searchQuery = mURLEditText.getText().toString();
            if(searchQuery != null && !searchQuery.isEmpty()){
                String url = NetworkUtil.validInput(SEARCH_QUERY +
                        mURLEditText.getText().toString() + API_QUERY);
                spinner.setVisibility(View.VISIBLE);
                task.execute(url); // This is a security risk.  Don't let your user enter the URL in a real app.
            }
        } catch (NetworkUtil.MyException e) {
            Toast.makeText(getApplication(),e.toString(),Toast.LENGTH_SHORT).show();
        }

    }

    // Google is deprecating Android AsyncTask API in Android 11 and suggesting to use java.util.concurrent
    // But it is still important to learn&manage how it works
    private class PingWebServiceTask extends AsyncTask<String, Integer, JSONObject> {

        @Override
        protected void onProgressUpdate(Integer... values) {
            Log.i(TAG, "Making progress...");
        }

        @Override
        protected JSONObject doInBackground(String... params) {
//            SystemClock.sleep(3000);
            JSONObject jObject = new JSONObject();
            try {

                // Initial website is "https://jsonplaceholder.typicode.com/posts/1"
                URL url = new URL(params[0]);
                // Get String response from the url address
                String resp = NetworkUtil.httpResponse(url);
                //Log.i("resp",resp);

                // JSONArray jArray = new JSONArray(resp);    // Use this if your web service returns an array of objects.  Arrays are in [ ] brackets.
                // Transform String into JSONObject
                jObject = new JSONObject(resp);

                //Log.i("jTitle",jObject.getString("title"));
                //Log.i("jBody",jObject.getString("body"));
                return jObject;

            } catch (MalformedURLException e) {
                Log.e(TAG,"MalformedURLException");
                e.printStackTrace();
            } catch (ProtocolException e) {
                Log.e(TAG,"ProtocolException");
                e.printStackTrace();
            } catch (IOException e) {
                Log.e(TAG,"IOException");
                e.printStackTrace();
            } catch (JSONException e) {
                Log.e(TAG,"JSONException");
                e.printStackTrace();
            }

            return jObject;
        }

        @Override
        protected void onPostExecute(JSONObject jObject) {
            super.onPostExecute(jObject);
            TextView result_view = (TextView)findViewById(R.id.result_textview);
            try {
//                result_view.setText(jObject.getJSONArray("Search").toString());
                JSONArray jsonArray = jObject.getJSONArray("Search");
                resultsStringList.clear();
                for(int i = 0; i < jsonArray.length(); i++){
                    resultsStringList.add(jsonArray.getJSONObject(i).getString("Title"));
                }
                stringArrayAdapter.notifyDataSetChanged();
//                result_view.setText(jObject.getString("Title"));

                spinner.setVisibility(View.GONE);
            } catch (JSONException e) {
                spinner.setVisibility(View.GONE);
//                result_view.setText("Something went wrong!");
            }

        }
    }
}