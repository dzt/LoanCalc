package jackhill.com.loancalc;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText price, downPayment;
    private TextView salexTax_tv, interest_tv;
    private SeekBar salesTax, interest;
    private Spinner term;
    private Button calculate_btn;

    private String currentInterest, currentTax = "0.000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.init();

    }

    private void init () {

        price = findViewById(R.id.editText);
        downPayment = findViewById(R.id.editText3);
        salesTax = findViewById(R.id.seekBar);
        interest = findViewById(R.id.seekBar2);
        term = findViewById(R.id.spinner);
        salexTax_tv = findViewById(R.id.textView);
        interest_tv = findViewById(R.id.textView2);
        calculate_btn = findViewById(R.id.button);

        calculate_btn.setOnClickListener(this);

        /* Setup Spinner */
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.terms_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        term.setAdapter(adapter);

        /* Setup Seek-bar's */
        salesTax.setMax(15);
        salesTax.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;
                if (progressChangedValue == 0 || progressChangedValue == 100) {
                    salexTax_tv.setText("Sales Tax: " + progressChangedValue + ".00%");
                } else {
                    salexTax_tv.setText("Sales Tax: " + progressChangedValue + ".25%");
                }
            }

            public void onStartTrackingTouch(SeekBar seekBar) {}

            public void onStopTrackingTouch(SeekBar seekBar) {
                if (progressChangedValue == 0 || progressChangedValue == 100) {
                    salexTax_tv.setText("Sales Tax: " + progressChangedValue + ".00%");
                    currentTax = progressChangedValue + ".000";
                } else {
                    salexTax_tv.setText("Sales Tax: " + progressChangedValue + ".25%");
                    currentTax = progressChangedValue + ".25";
                }
            }
        });

        interest.setMax(50);
        interest.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;
                interest_tv.setText("Interest Rate: " + progressChangedValue + "%");
            }

            public void onStartTrackingTouch(SeekBar seekBar) {}

            public void onStopTrackingTouch(SeekBar seekBar) {
                interest_tv.setText("Interest Rate: " + progressChangedValue + "%");
                currentInterest = progressChangedValue + ".000";
            }
        });


    }

    private void calculate () {

        /* Parse values from UI and send them over to server */
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Fetching Stats....");
        pd.show();

        try {
            String URL = "https://jack-csp.herokuapp.com/fetch";
            JSONObject jsonBody = new JSONObject();

            jsonBody.put("purchase_price", price.getText());
            jsonBody.put("down_payment", downPayment.getText());
            jsonBody.put("interest_rate", currentInterest);
            jsonBody.put("sales_tax_rate", currentTax);
            jsonBody.put("term", term.getSelectedItem().toString().split(" ")[0]);

            JsonObjectRequest jsonOblect = new JsonObjectRequest(Request.Method.POST, URL, jsonBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    Log.d("Response", response.toString());

                    Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("json", response.toString());
                    startActivity(intent);

                    pd.dismiss();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    VolleyLog.e(error.getMessage());
//                    onBackPressed();
                    pd.dismiss();

                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    final Map<String, String> headers = new HashMap<>();
                    return headers;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(jsonOblect);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onClick(View v) {

        Button _b = (Button) findViewById(v.getId());

        switch (v.getId()) {
            case R.id.button:
                this.calculate();
                Log.d("Button Pressed", _b.getText() + "");
                break;
        }

    }

}
