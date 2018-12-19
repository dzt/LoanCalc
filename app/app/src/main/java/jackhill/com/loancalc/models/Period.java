package jackhill.com.loancalc.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Period {

    private String period, principal_paid, interest_paid, balance;

    public Period(String json) {
        try {
            JSONObject obj = new JSONObject(json);
            this.period = obj.getString("period");
            this.principal_paid = obj.getString("principal_paid");
            this.interest_paid = obj.getString("interest_paid");
            this.balance = obj.getString("balance");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getPeriod() {
        return period;
    }

    public String getPrincipal_paid() {
        return principal_paid;
    }

    public String getInterest_paid() {
        return interest_paid;
    }

    public String getBalance() {
        return balance;
    }
}
