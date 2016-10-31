package com.doongis.r2.FIT;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.doongis.r2.R;
import com.doongis.r2.ServerConnect.StepVO;

import java.util.ArrayList;

public class FitAdapter extends BaseAdapter {
    Context context;
    ArrayList<StepVO> arrayList = new ArrayList<StepVO>();

    public FitAdapter(){}

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.fit_item, parent, false);
        }

        TextView fit_total_time = (TextView) convertView.findViewById(R.id.fit_total_time);
        TextView fit_goal = (TextView) convertView.findViewById(R.id.fit_goal);
        TextView fit_step = (TextView) convertView.findViewById(R.id.fit_step);

        StepVO stepVO = arrayList.get(position);
        fit_total_time.setText(stepVO.getStep_total_time());
        fit_goal.setText(stepVO.getStep_goal());
        fit_step.setText(stepVO.getStep_step());

        return convertView;
    }
    public void addItem(String total_time, String goal, String step) {
        StepVO stepVO = new StepVO();
        stepVO.setStep_total_time(total_time);
        stepVO.setStep_goal(goal);
        stepVO.setStep_step(step);

        arrayList.add(stepVO);
    }
}
