/*
* 운동기록  페이지 ( Fragment 형식 )
*
*
* */

package com.doongis.r2.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.doongis.r2.FIT.FitActivity;
import com.doongis.r2.GPS.Gogosing;
import com.doongis.r2.R;

public class Main_3 extends Fragment {
    public Main_3() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_3, container, false);
        Button btnGogosing = (Button) view.findViewById(R.id.btngogosing);
        Button btnGosingsing = (Button) view.findViewById(R.id.btngosingsing);

        btnGogosing.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Gogosing.class);
                startActivity(intent);
            }
        });

        btnGosingsing.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FitActivity.class);
                startActivity(intent);
            }
        });


        return view;
    }
}
