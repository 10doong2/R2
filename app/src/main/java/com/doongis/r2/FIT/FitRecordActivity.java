package com.doongis.r2.FIT;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.doongis.r2.R;
import com.doongis.r2.ServerConnect.CycleVO;
import com.doongis.r2.ServerConnect.StepVO;
import com.doongis.r2.ServerConnect.UserVO;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class FitRecordActivity extends AppCompatActivity {

    FitAdapter fitAdapter;
    ListView lv_fit;
    String user_id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fit_record);

        UserVO userVO = new UserVO();
        user_id = userVO.getUser_id();

        lv_fit = (ListView) findViewById(R.id.lv_fit);
        onSelectStepList();

    }

    private void onSelectStepList() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
        //String requestURL = "http://192.168.20.119/r2/step/selectJSON";
                                    //ip주소 입력
        String requestURL = "http://"+"   "+"/r2/step/selectJSON";
        ArrayList<StepVO> list = new ArrayList<StepVO>();

        try {
            URL url = new URL(requestURL);

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setDefaultUseCaches(false);

            BufferedInputStream buf = new BufferedInputStream(httpURLConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(buf, "UTF-8"));

            String line;
            String page = "";

            fitAdapter = new FitAdapter();
            lv_fit.setAdapter(fitAdapter);

            while ((line = bufferedReader.readLine()) != null) {
                page += line;
            }
            bufferedReader.close();
            JSONArray jsonArray = new JSONArray(page);
            for (int i = 0; i < jsonArray.length(); i++) {
                StepVO stepVO = new StepVO();
                JSONObject json = jsonArray.getJSONObject(i);
                stepVO.setStep_total_time(json.getString("step_total_time"));
                stepVO.setStep_goal(json.getString("step_goal"));
                stepVO.setStep_step(json.getString("step_step"));
                list.add(stepVO);
                fitAdapter.addItem(stepVO.getStep_total_time(), stepVO.getStep_goal(),stepVO.getStep_step());
            }
            Log.d("selectTest", list + "");

        } catch (Exception e) {
            Log.d("select error", e.getMessage());
        }

    }

}
