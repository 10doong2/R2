package com.doongis.r2.GPS;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.doongis.r2.R;
import com.doongis.r2.ServerConnect.CycleVO;
import com.doongis.r2.ServerConnect.UserVO;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GogoRecordActivity extends AppCompatActivity {

    GogoAdapter gogoAdapter;
    ListView lv_gogo;
    String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gogo_record);

        UserVO userVO = new UserVO();
        user_id = userVO.getUser_id();

        lv_gogo = (ListView) findViewById(R.id.lv_gogo);
        onSelectCycleList();
    }

    public void onSelectCycleList() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
        //String requestURL = "http://192.168.20.119/r2/cycle/selectJSON";
                                    //유저IP주소입력
        String requestURL = "http://"+"    "+"/r2/cycle/selectJSON";
        ArrayList<CycleVO> list = new ArrayList<CycleVO>();

        try {
            URL url = new URL(requestURL);

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setDefaultUseCaches(false);

            BufferedInputStream buf = new BufferedInputStream(httpURLConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(buf, "UTF-8"));

            String line;
            String page = "";

            gogoAdapter = new GogoAdapter();
            lv_gogo.setAdapter(gogoAdapter);

            while ((line = bufferedReader.readLine()) != null) {
                page += line;
            }
            bufferedReader.close();
            JSONArray jsonArray = new JSONArray(page);
            for (int i = 0; i < jsonArray.length(); i++) {
                CycleVO cycleVO = new CycleVO();
                JSONObject json = jsonArray.getJSONObject(i);
                cycleVO.setCycle_total_time(json.getString("cycle_total_time"));
                cycleVO.setCycle_distance(json.getString("cycle_distance"));
                cycleVO.setCycle_speed(json.getString("cycle_speed"));
                list.add(cycleVO);
                gogoAdapter.addItem(cycleVO.getCycle_total_time(), cycleVO.getCycle_distance(), cycleVO.getCycle_speed());
            }
            Log.d("selectTest", list + "");

        } catch (Exception e) {
            Log.d("select error", e.getMessage());
        }
    }
}
