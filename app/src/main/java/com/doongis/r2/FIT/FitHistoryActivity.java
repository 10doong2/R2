package com.doongis.r2.FIT;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.doongis.r2.R;
import com.doongis.r2.ServerConnect.UserVO;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataDeleteRequest;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.request.DataUpdateRequest;
import com.google.android.gms.fitness.result.DailyTotalResult;
import com.google.android.gms.fitness.result.DataReadResult;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FitHistoryActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private Button mButtonStepSave;
    private Button mButtonViewToday;

    TextView fit_start_time;
    TextView fit_end_time;
    TextView fit_total_time;
    TextView fit_field_data;

    String user_id, user_name;

    String step, goal, btn_startTime, btn_endTime, btn_totalTime;

    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fit_history);

        UserVO userVO = new UserVO();
        user_id = userVO.getUser_id();
        user_name = userVO.getUser_name();

        Intent intent = getIntent();
        step = intent.getStringExtra("step");
        goal = intent.getStringExtra("goal");
        btn_startTime = intent.getStringExtra("btn_startTime");
        btn_endTime = intent.getStringExtra("btn_endTime");
        btn_totalTime = intent.getStringExtra("btn_totalTime");

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Fitness.HISTORY_API)
                .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE))
                .addConnectionCallbacks(this)
                .enableAutoManage(this, 0, this)
                .build();

        fit_start_time = (TextView) findViewById(R.id.fit_start_time);
        fit_end_time = (TextView) findViewById(R.id.fit_end_time);
        fit_total_time = (TextView) findViewById(R.id.fit_total_time);
        fit_field_data = (TextView) findViewById(R.id.fit_field_data);

        mButtonViewToday = (Button) findViewById(R.id.btn_view_today);
        mButtonStepSave = (Button) findViewById(R.id.btn_step_save);

        /*오늘의 결과*/
        mButtonViewToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ViewTodaysStepCountTask().execute();
                Toast.makeText(getApplicationContext(), "오늘의 Step!", Toast.LENGTH_LONG).show();
            }
        });

        mButtonStepSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onStepInsertServer(btn_startTime, btn_endTime, btn_totalTime, goal, step, user_id, user_name);
                Toast.makeText(getApplicationContext(), "저장되었습니다^.^", Toast.LENGTH_LONG).show();
                Log.d("step저장", "저장합니당.ㅇ");
            }
        });
    }


    public void onConnected(@Nullable Bundle bundle) {
        Log.e("HistoryAPI", "onConnected");
    }


    private void displayStepDataForToday() {
        DailyTotalResult result = Fitness.HistoryApi.readDailyTotal(mGoogleApiClient, DataType.TYPE_STEP_COUNT_DELTA).await(1, TimeUnit.MINUTES);
        showDataSet(result.getTotal());

    }

    private void displayLastWeeksData() {
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.WEEK_OF_YEAR, -1);
        long startTime = cal.getTimeInMillis();


        java.text.DateFormat dateFormat = DateFormat.getDateInstance();
        Log.e("History", "Range Start: " + dateFormat.format(startTime));
        Log.e("History", "Range End: " + dateFormat.format(endTime));


        DataReadRequest readRequest = new DataReadRequest.Builder()
                .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
                .bucketByTime(1, TimeUnit.DAYS)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .build();


        DataReadResult dataReadResult = Fitness.HistoryApi.readData(mGoogleApiClient, readRequest).await(1, TimeUnit.MINUTES);


        if (dataReadResult.getBuckets().size() > 0) {
            Log.e("History", "Number of buckets: " + dataReadResult.getBuckets().size());
            for (Bucket bucket : dataReadResult.getBuckets()) {
                List<DataSet> dataSets = bucket.getDataSets();
                for (DataSet dataSet : dataSets) {
                    showDataSet(dataSet);
                }
            }
        }
        else if (dataReadResult.getDataSets().size() > 0) {
            Log.e("History", "Number of returned DataSets: " + dataReadResult.getDataSets().size());
            for (DataSet dataSet : dataReadResult.getDataSets()) {
                showDataSet(dataSet);
            }
        }
    }

    private void showDataSet(DataSet dataSet) {
        Log.e("History", "Data returned for Data type: " + dataSet.getDataType().getName());
        for (DataPoint dp : dataSet.getDataPoints()) {
            Log.e("History", "Data point:");
            Log.e("History", "\tType: " + dp.getDataType().getName());
            for (Field field : dp.getDataType().getFields()) {
                Log.e("History", "\tField: " + field.getName() +
                        " Value: " + dp.getValue(field));
            }
        }
    }


    private void addStepDataToGoogleFit() {
        DataSource dataSource = new DataSource.Builder()
                .setAppPackageName(this)
                .setDataType(DataType.TYPE_STEP_COUNT_DELTA)
                .setName("Step Count")
                .setType(DataSource.TYPE_RAW)
                .build();

        int stepCountDelta = 1000000;
        DataSet dataSet = DataSet.create(dataSource);
        DataPoint point = dataSet.createDataPoint();
        point.getValue(Field.FIELD_STEPS).setInt(stepCountDelta);
        dataSet.add(point);

        Status status = Fitness.HistoryApi.insertData(mGoogleApiClient, dataSet).await(1, TimeUnit.MINUTES);

        if (!status.isSuccess()) {
            Log.e("History", "Problem with inserting data: " + status.getStatusMessage());
        } else {
            Log.e("History", "data inserted");
        }
    }


    private void updateStepDataOnGoogleFit() {
        DataSource dataSource = new DataSource.Builder()
                .setAppPackageName(this)
                .setDataType(DataType.TYPE_STEP_COUNT_DELTA)
                .setName("Step Count")
                .setType(DataSource.TYPE_RAW)
                .build();

        int stepCountDelta = 2000000;
        DataSet dataSet = DataSet.create(dataSource);
        DataPoint point = dataSet.createDataPoint();
        point.getValue(Field.FIELD_STEPS).setInt(stepCountDelta);
        dataSet.add(point);

        DataUpdateRequest updateRequest = new DataUpdateRequest.Builder().setDataSet(dataSet).build();
        Fitness.HistoryApi.updateData(mGoogleApiClient, updateRequest).await(1, TimeUnit.MINUTES);
    }


    private void deleteStepDataOnGoogleFit() {
        DataDeleteRequest request = new DataDeleteRequest.Builder()
                .addDataType(DataType.TYPE_STEP_COUNT_DELTA)
                .build();
        Fitness.HistoryApi.deleteData(mGoogleApiClient, request).await(1, TimeUnit.MINUTES);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e("HistoryAPI", "onConnectionSuspended");
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("HistoryAPI", "onConnectionFailed");
    }


    private class ViewTodaysStepCountTask extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... params) {
            displayStepDataForToday();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            fit_start_time.setText(btn_startTime);
            fit_end_time.setText(btn_endTime);
            fit_total_time.setText(btn_totalTime);
            fit_field_data.setText(step + " / " + goal);
        }
    }

    private class AddStepsToGoogleFitTask extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... params) {
            addStepDataToGoogleFit();
            displayLastWeeksData();
            return null;
        }
    }

    private class UpdateStepsOnGoogleFitTask extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... params) {
            updateStepDataOnGoogleFit();
            displayLastWeeksData();
            return null;
        }
    }

    private class DeleteYesterdaysStepsTask extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... params) {
            deleteStepDataOnGoogleFit();
            displayLastWeeksData();
            return null;
        }
    }

    //데이터 입력
    public void onStepInsertServer(String step_start_time, String step_end_time, String step_total_time, String step_goal,String step_step, String user_id, String user_name) {

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());

        //String requestURL = "http://192.168.20.119/r2/step/insert";
        //ip주소 입력
        String requestURL = "http://"+"    "+"/r2/step/insert";

        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(requestURL);

            List<NameValuePair> paramList = new ArrayList<>();
            paramList.add(new BasicNameValuePair("step_start_time", step_start_time));
            paramList.add(new BasicNameValuePair("step_end_time", step_end_time));
            paramList.add(new BasicNameValuePair("step_total_time", step_total_time));
            paramList.add(new BasicNameValuePair("step_goal", step_goal));
            paramList.add(new BasicNameValuePair("step_step", step_step));
            paramList.add(new BasicNameValuePair("user_id", user_id));
            paramList.add(new BasicNameValuePair("user_name", user_name));

            httpPost.setEntity(new UrlEncodedFormEntity(paramList, HTTP.UTF_8));

            HttpResponse httpResponse = client.execute(httpPost);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("입력됨", step_start_time + ", " + step_end_time + ", " + step_total_time + ", " + step_goal + ", " + step_step + ", " + user_id + ", " + user_name);

    }
}