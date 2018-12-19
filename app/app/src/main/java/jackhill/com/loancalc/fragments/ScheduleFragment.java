package jackhill.com.loancalc.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import jackhill.com.loancalc.adapters.ScheduleAdapter;
import jackhill.com.loancalc.models.Period;

public class ScheduleFragment extends Fragment {

    ArrayList<Period> dates = new ArrayList<>();
    JSONArray arr;

    public ScheduleFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        RecyclerView rv = new RecyclerView(getContext());

        this.init(getArguments().getString("json"));

        rv.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rv.setLayoutManager(mLayoutManager);
        rv.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(new ScheduleAdapter(dates));


        return rv;
    }

    private void init(String json) {
        try {

            arr = new JSONObject(json).getJSONObject("data").getJSONObject("schedule").getJSONArray("monthly_amortization");

            for (int i = 0; i < arr.length(); i++){
                JSONObject obj = arr.getJSONObject(i);
                dates.add(new Period(obj.toString()));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}
