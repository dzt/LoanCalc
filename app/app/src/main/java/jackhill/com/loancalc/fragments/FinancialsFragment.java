package jackhill.com.loancalc.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import jackhill.com.loancalc.R;
import jackhill.com.loancalc.models.Loan;

public class FinancialsFragment extends Fragment {

    private Loan loan;

    public FinancialsFragment() {}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.financials_fragment, container, false);

        loan = new Loan(getArguments().getString("json"));

        TextView monthlyPayment = view.findViewById(R.id.price_detail);
        TextView totalInterest = view.findViewById(R.id.interest_detail);
        TextView end_tv = view.findViewById(R.id.end_tv);

        monthlyPayment.setText(loan.getMonthlyPayment());
        totalInterest.setText(loan.getTotalInterest());
        end_tv.setText(loan.getPayOffDate());

        return view;

    }
}
