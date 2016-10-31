/*
* 볼래?  페이지 ( Fragment 형식 )
*
*
* */

package com.doongis.r2.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.doongis.r2.R;
import com.doongis.r2.ServerConnect.ArticleVO;
import com.doongis.r2.adapter.TestAdapter;
import com.doongis.r2.model.main_1_Write;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Main_1 extends Fragment {

    private Button btn_main_1_gotoWrite;
    RecyclerView rv;


    ListView testitems;
    TestAdapter testAdapter;

    public Main_1() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_1, container, false);

        testitems = (ListView)view.findViewById(R.id.testitems);

        Button btn_main_1_gotoWrite = (Button) view.findViewById(R.id.btn_main_1_gotoWrite);
        btn_main_1_gotoWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), main_1_Write.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        onSelectArticleList();
        super.onResume();
    }

    public void onSelectArticleList(){
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());

        //String requestURL = "http://192.168.20.119/r2/article/selectJSON";
                                     //유저IP주소입력
        String requestURL = "http://"+"  "+"/r2/article/selectJSON";
        ArrayList<ArticleVO> list = new ArrayList<ArticleVO>();

        try {

            URL url = new URL(requestURL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setDefaultUseCaches(false);

            BufferedInputStream buf = new BufferedInputStream(httpURLConnection.getInputStream());

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(buf, "UTF-8"));

            String line;
            String page="";

            testAdapter = new TestAdapter();
            testitems.setAdapter(testAdapter);

            while((line = bufferedReader.readLine())!=null ){
                page +=line;
            }
            bufferedReader.close();
            JSONArray jsonArray = new JSONArray(page);
            for(int i=0; i< jsonArray.length();i++){
                ArticleVO articleVO = new ArticleVO();
                JSONObject json = jsonArray.getJSONObject(i);
                articleVO.setArticle_no(json.getInt("article_no"));
                articleVO.setArticle_title(json.getString("article_title"));
                articleVO.setArticle_date(json.getString("article_date"));
                articleVO.setArticle_content(json.getString("article_content"));
                articleVO.setArticle_heart(json.getInt("article_heart"));
                articleVO.setArticle_url(json.getString("article_url"));
                articleVO.setUser_id(json.getString("user_id"));
                articleVO.setUser_name(json.getString("user_name"));
                list.add(articleVO);
                testAdapter.addItem(articleVO.getArticle_title(), articleVO.getArticle_date(),articleVO.getArticle_content(),articleVO.getUser_id(), articleVO.getUser_name());
            }
            Log.d("selectTest",list+"");

        } catch (Exception e) {
            Log.d("select error", e.getMessage());
        }
    }
}
