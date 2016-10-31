package com.doongis.r2.GPS;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.doongis.r2.R;
import com.doongis.r2.ServerConnect.CycleVO;

import java.util.ArrayList;

/**
 * Created by TAEEUN on 2016-06-17.
 */
public class GogoAdapter extends BaseAdapter {

    Context context;
    ArrayList<CycleVO> arrayList = new ArrayList<CycleVO>();

    public GogoAdapter(){}

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
            convertView = inflater.inflate(R.layout.gogo_item, parent, false);
        }

        TextView gogo_total_time = (TextView) convertView.findViewById(R.id.gogo_total_time);
        TextView gogo_distance = (TextView) convertView.findViewById(R.id.gogo_distance);
        TextView gogo_speed = (TextView) convertView.findViewById(R.id.gogo_speed);

        CycleVO cycleVO = arrayList.get(position);
        gogo_total_time.setText(cycleVO.getCycle_total_time());
        gogo_distance.setText(cycleVO.getCycle_distance());
        gogo_speed.setText(cycleVO.getCycle_speed());

        return convertView;
    }
    public void addItem(String total_time, String total_distance, String total_speed) {
        CycleVO cycleVO = new CycleVO();
        cycleVO.setCycle_total_time(total_time);
        cycleVO.setCycle_distance(total_distance);
        cycleVO.setCycle_speed(total_speed);

        arrayList.add(cycleVO);
    }
}
