package com.example.nikitharathnakar.ebaysearch;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;


public class Search extends ActionBarActivity {

    public Context context;
    public static String keywordText;

    public Search() {
        keywordText = "";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        RelativeLayout view = (RelativeLayout)findViewById(R.id.Search);
        view.setBackgroundResource(R.drawable.background_search);
        Spinner spinner = (Spinner) findViewById(R.id.sortOrder);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter;
        adapter = ArrayAdapter.createFromResource(this, R.array.sort_by , android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
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

    public void clear_function(View view) {
        Button b = (Button) view;
        ((EditText)findViewById(R.id.keyword)).setText("");
        ((EditText)findViewById(R.id.MinPrice)).setText("");
        ((EditText)findViewById(R.id.MaxPrice)).setText("");
        ((Spinner)findViewById(R.id.sortOrder)).setSelection(0);
        ((TextView)findViewById(R.id.error_text)).setText("");

    }

    public void search_function(View view){
        if(!validate_fields()){

            String parsedString = "";
            String jsonResponse = "";
            String keyword = ((EditText)findViewById(R.id.keyword)).getText().toString().trim();
            keywordText = keyword;
            String MinPrice = ((EditText)findViewById(R.id.MinPrice)).getText().toString().trim();
            String MaxPrice = ((EditText)findViewById(R.id.MaxPrice)).getText().toString().trim();
            String sortOrder = ((Spinner)findViewById(R.id.sortOrder)).getSelectedItem().toString();
            String sortOrderValue = "";
            if(sortOrder.equals("Best Match"))
                sortOrderValue = "BestMatch";
            if(sortOrder.equals("Price: highest first"))
                sortOrderValue = "CurrentPriceHighest";
            if(sortOrder.equals("Price + Shipping: highest first"))
                sortOrderValue = "PricePlusShippingHighest";
            if(sortOrder.equals("Price + Shipping: lowest first"))
                sortOrderValue = "PricePlusShippingLowest";
            String urlString = "http://uscwebtech2015-env.elasticbeanstalk.com/Search.php?pageNumber=1&Keywords="+keyword+"&sortOrder="+sortOrderValue+"&EntriesPerPage=5";
            if(!MinPrice.isEmpty())
                urlString+="&MinPrice="+MinPrice;
            if(!MaxPrice.isEmpty())
                urlString+="&MaxPrice="+MaxPrice;
            try {


                RequestData task = new RequestData();
                //task.execute(new String[] { urlString});
                task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, urlString,urlString,urlString);
                //new RequestData().execute(urlString,urlString,urlString);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean validate_fields() {
        Boolean errorFlag = false;
        String errorMsg = "";
        EditText keyword = ((EditText)findViewById(R.id.keyword));
        EditText MinPrice = ((EditText)findViewById(R.id.MinPrice));
        EditText MaxPrice = ((EditText)findViewById(R.id.MaxPrice));
        if( keyword.getText().toString().trim().equals(""))
        {
            errorFlag = true;
            errorMsg = "Please enter a keyword";

        }
        else if(!(MaxPrice.getText().toString().trim().equals("")) && !MinPrice.getText().toString().trim().equals("")){
            if((Integer.parseInt(MaxPrice.getText().toString())<(Integer.parseInt(MinPrice.getText().toString())))){
                errorFlag = true;
                errorMsg = "Maximum price cannot be less than minimum price";
            }
        }
        TextView errorText = ((TextView)findViewById(R.id.error_text));
        errorText.setText(errorMsg);
        errorText.setTextColor(Color.RED);
        return errorFlag;
    }

    public class RequestData extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String[] uri){
            Log.e("Doing in Background>>>",uri[0]);
            HttpClient httpclient = new DefaultHttpClient();

            HttpResponse response;
            String responseString = null;
            try {
                response = httpclient.execute(new HttpGet(uri[0]));
                StatusLine statusLine = response.getStatusLine();
                if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    response.getEntity().writeTo(out);
                    responseString = out.toString();

                    out.close();
                } else{
                    //Closes the connection.
                    response.getEntity().getContent().close();
                    throw new IOException(statusLine.getReasonPhrase());
                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //Do anything with response..
            JSONObject jsonObj = null;
            try {
                jsonObj = new JSONObject(result);
                String acknowledge = jsonObj.get("ack").toString();
                if (acknowledge.equals("Success")) {
                    Intent intent = new Intent();
                    intent.setClass(getApplicationContext(), Resultlist.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("jsonResult", result);
                    intent.putExtra("keyword", keywordText);
                    startActivity(intent);
                }
                else{
                    TextView errorText = ((TextView)findViewById(R.id.error_text));
                    errorText.setText("No Results Found");
                    errorText.setTextColor(Color.BLUE);

                }


        } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }
}

