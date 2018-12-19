package jackhill.com.loancalc.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Loan {

    private String monthlyPayment, loanAmount, totalInterest, payOffDate;
    private JSONArray periods;

    public Loan (String json) {

        try {

            JSONObject loanObject = new JSONObject(json);

            this.monthlyPayment = loanObject.getJSONObject("data").getString("monthlyPayment");
            this.loanAmount = loanObject.getJSONObject("data").getString("loanAmount");
            this.totalInterest = loanObject.getJSONObject("data").getString("totalInterest");
            this.payOffDate = loanObject.getJSONObject("data").getString("payOffDate");

            this.periods = loanObject.getJSONObject("data").getJSONObject("schedule").getJSONArray("monthly_amortization");


        } catch (JSONException e) {

            e.printStackTrace();

        }
    }

    public String getMonthlyPayment() {
        return monthlyPayment;
    }

    public String getLoanAmount() {
        return loanAmount;
    }

    public String getTotalInterest() {
        return totalInterest;
    }

    public String getPayOffDate() {
        return payOffDate;
    }

    public JSONArray getPeriods() {
        return periods;
    }
}
