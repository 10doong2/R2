/*
* 자유게시판 페이지 ( Fragment 형식 )
*
*
* */

package com.doongis.r2.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.doongis.r2.R;

public class Main_4 extends Fragment {
    public Main_4() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_4, container, false);
        return rootView;
    }
}
