/*
* 탈래?  페이지 ( Fragment 형식 )
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

import com.doongis.r2.R;
import com.doongis.r2.activity.ChatActivity;
import com.doongis.r2.drawing.BoardListActivity;


public class Main_2 extends Fragment {
//    private BackPressCloseHandler backPressCloseHandler;
public Main_2() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_2, container, false);
        final Button btnCreatePlayGroup = (Button) rootView.findViewById(R.id.btnCreatePlayGroup);

        final Button btnCreatePlayRoom = (Button) rootView.findViewById(R.id.btnCreatePlayRoom);

        btnCreatePlayRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), BoardListActivity.class);
                startActivity(intent);
            }
        });

        btnCreatePlayGroup.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), ChatActivity.class);
                startActivity(intent);
            }
        });
        return rootView;
    }




}
