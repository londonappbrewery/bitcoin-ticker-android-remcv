package com.londonappbrewery.bitcointicker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity
{
    // Constants:
    private final String BASE_URL = "https://api.coindesk.com/v1/bpi/currentprice/";

    // Member variables:
    private TextView mPriceTextView;
    private String selectedCurrency;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //
        selectedCurrency = null;

        // setup layout variables
        mPriceTextView = findViewById(R.id.priceLabel);
        Spinner spinner = findViewById(R.id.currency_spinner);

        // Create an ArrayAdapter using the String array and a spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.currency_array, R.layout.spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        // Set an OnItemSelected listener on the spinner
        spinner.setOnItemSelectedListener(new OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                Log.d("Bitcoin", "Item selected: " + adapterView.getItemAtPosition(i));
                selectedCurrency = adapterView.getItemAtPosition(i).toString();
                letsDoSomeNetworking();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {
                Log.d("Bitcoin", "From spinner - setOnItemSelectedListener - onNothingSelected() called");
            }
        });
    }

    private void letsDoSomeNetworking()
    {
        AsyncHttpClient client = new AsyncHttpClient();

        String url = BASE_URL + selectedCurrency + ".json";

        client.get(url, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                try
                {
                    String exchangeRate = response.getJSONObject("bpi").getJSONObject(selectedCurrency).getString("rate");
                    mPriceTextView.setText(exchangeRate); 
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response)
            {
                Toast.makeText(MainActivity.this, "Request failed", Toast.LENGTH_SHORT).show();
            }
        });


    }


}
