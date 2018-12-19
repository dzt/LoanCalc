package jackhill.com.loancalc.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import jackhill.com.loancalc.R;
import jackhill.com.loancalc.models.Period;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.MyViewHolder> {

    private List<Period> periods;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView period, balance;

        public MyViewHolder(View view) {
            super(view);
            period = (TextView) view.findViewById(R.id.period);
            balance = (TextView) view.findViewById(R.id.balance);
        }
    }


    public ScheduleAdapter(List<Period> periods) {
        this.periods = periods;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Period periodObj = periods.get(position);
        holder.period.setText(periodObj.getPeriod());
        holder.balance.setText("$" + periodObj.getBalance());
    }

    @Override
    public int getItemCount() {
        return periods.size();
    }
}