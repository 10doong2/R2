package com.doongis.r2.FIT;

import android.content.Intent;
import android.content.IntentSender;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.doongis.r2.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.Value;
import com.google.android.gms.fitness.request.DataSourcesRequest;
import com.google.android.gms.fitness.request.OnDataPointListener;
import com.google.android.gms.fitness.request.SensorRequest;
import com.google.android.gms.fitness.result.DataSourcesResult;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.TimeUnit;


public class FitActivity extends AppCompatActivity implements OnDataPointListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private boolean ISBTNEDITGOAL = false;
    private boolean ISBTNSTART = false;
    private boolean ISBTNPAUSE = false;

    private static final int REQUEST_OAUTH = 1;
    private static final String AUTH_PENDING = "auth_state_pending";
    private boolean authInProgress = false;
    private GoogleApiClient mApiClient;

    TextView goal1;
    TextView step;
    EditText editGoal;
    Button add;
    Button start;

    Button stop;
    Button history;

    String btn_startTime;
    String btn_endTime;
    String btn_totalTime;

    long TimeE=0;
    long TimeN=0;
    long TimeT=0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fit);

        goal1 = (TextView) findViewById(R.id.goal1);
        editGoal = (EditText) findViewById(R.id.editGoal);
        step = (TextView) findViewById(R.id.step);
        add = (Button) findViewById(R.id.add);

        start = (Button) findViewById(R.id.start);
        stop = (Button) findViewById(R.id.stop);
        history = (Button) findViewById(R.id.history);


        if (savedInstanceState != null) {
            authInProgress = savedInstanceState.getBoolean(AUTH_PENDING);
        }

        mApiClient = new GoogleApiClient.Builder(this)
                .addApi(Fitness.SENSORS_API)
                .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE))
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //스타트 버튼
                if(ISBTNEDITGOAL==true){
                    startStep();
                    TimeN = System.currentTimeMillis();
                    SimpleDateFormat sdfN = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    btn_startTime = sdfN.format(TimeN);
                    ISBTNSTART = true;
                }else{
                    Toast.makeText(getApplicationContext(), "목표 설정을 먼저 해주세요!", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Pause 버튼
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ISBTNSTART == true) {
                    onStop();
                    final FitChart fitChart = (FitChart) findViewById(R.id.fitChart);
                    fitChart.setMinValue(0f);
                    fitChart.setMaxValue(100f);
                    Resources resources = getResources();
                    Collection<FitChartValue> values = new ArrayList<>();
                    values.add(new FitChartValue(-100f, resources.getColor(R.color.chart_value_5)));
                    fitChart.setValues(values);
                    TimeE = System.currentTimeMillis();
                    SimpleDateFormat sdfE = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    btn_endTime = sdfE.format(TimeE);

                    ISBTNPAUSE = true;
                }else{
                    Toast.makeText(getApplicationContext(), "START버튼을 먼저 클릭해주세요!", Toast.LENGTH_LONG).show();
                }
            }
        });

        add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (editGoal != null) {
                    goal1.setText("Your goal is " + editGoal.getText() + " steps.");
                    ISBTNEDITGOAL = true;
                }
                else{
                    Toast.makeText(getApplicationContext(), "목표를 먼저 설정 해주세요!", Toast.LENGTH_LONG).show();
                }
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ISBTNPAUSE == true) {
                    TimeT = TimeE - TimeN-32400000;
                    SimpleDateFormat sdfT = new SimpleDateFormat("HH:mm:ss");
                    btn_totalTime = sdfT.format(TimeT);

                    String stepG = String.valueOf(step.getText());
                    String goalG = String.valueOf(editGoal.getText());
                    if(stepG==""){
                        stepG="0";
                    }
                    Log.d("intent실행", btn_startTime + btn_endTime + btn_totalTime+"stepG"+stepG+"goal"+goalG);

                    Intent intent = new Intent(FitActivity.this, FitHistoryActivity.class);
                    intent.putExtra("step", step.getText().toString());
                    intent.putExtra("goal", editGoal.getText().toString());
                    intent.putExtra("btn_startTime", btn_startTime);
                    intent.putExtra("btn_endTime", btn_endTime);
                    intent.putExtra("btn_totalTime", btn_totalTime);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(), "pause버튼을 먼저 클릭해주세요!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }


    private void startStep() {
        mApiClient.connect();
        final FitChart fitChart = (FitChart) findViewById(R.id.fitChart);
        fitChart.setMinValue(0f);
        fitChart.setMaxValue(100f);

        Resources resources = getResources();
        Collection<FitChartValue> values = new ArrayList<>();

        values.add(new FitChartValue(25f, resources.getColor(R.color.chart_value_1)));
        values.add(new FitChartValue(25f, resources.getColor(R.color.chart_value_2)));
        values.add(new FitChartValue(25f, resources.getColor(R.color.chart_value_3)));
        values.add(new FitChartValue(25f, resources.getColor(R.color.chart_value_4)));
        fitChart.setValues(values);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mApiClient.connect();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_OAUTH) {
            authInProgress = false;
            if (resultCode == RESULT_OK) {
                if (!mApiClient.isConnecting() && !mApiClient.isConnected()) {
                    mApiClient.connect();
                }
            } else if (resultCode == RESULT_CANCELED) {
                Log.e("GoogleFit", "RESULT_CANCELED");
            }
        } else {
            Log.e("GoogleFit", "requestCode NOT request_oauth");
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        DataSourcesRequest dataSourceRequest = new DataSourcesRequest.Builder()
                .setDataTypes(DataType.TYPE_STEP_COUNT_CUMULATIVE)
                .setDataSourceTypes(DataSource.TYPE_RAW)
                .build();

        ResultCallback<DataSourcesResult> dataSourcesResultCallback = new ResultCallback<DataSourcesResult>() {
            @Override
            public void onResult(DataSourcesResult dataSourcesResult) {
                for (DataSource dataSource : dataSourcesResult.getDataSources()) {
                    if (DataType.TYPE_STEP_COUNT_CUMULATIVE.equals(dataSource.getDataType())) {
                        registerFitnessDataListener(dataSource, DataType.TYPE_STEP_COUNT_CUMULATIVE);
                    }
                }
            }
        };

        Fitness.SensorsApi.findDataSources(mApiClient, dataSourceRequest)
                .setResultCallback(dataSourcesResultCallback);
    }

    private void registerFitnessDataListener(DataSource dataSource, DataType dataType) {
        SensorRequest request = new SensorRequest.Builder()
                .setDataSource(dataSource)
                .setDataType(dataType)
                .setSamplingRate(3, TimeUnit.SECONDS)
                .build();

        Fitness.SensorsApi.add(mApiClient, request, this)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        if (status.isSuccess()) {
                            Log.e("GoogleFit", "SensorApi successfully added");
                        }
                    }
                });
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (!authInProgress) {
            try {
                authInProgress = true;
                connectionResult.startResolutionForResult(FitActivity.this, REQUEST_OAUTH);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("GoogleFit", "authInProgress");
        }
    }

    @Override
    public void onDataPoint(final DataPoint dataPoint) {
        for (final Field field : dataPoint.getDataType().getFields()) {
            final Value value = dataPoint.getValue(field);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    step.setText(value + "");
                    Log.d("value", "step : " + value);
                }
            });
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(AUTH_PENDING, authInProgress);
    }

    //Stop 메소드
    @Override
    protected void onStop() {
        super.onStop();

        Fitness.SensorsApi.remove(mApiClient, this)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        if (status.isSuccess()) {
                            mApiClient.disconnect();
                        }
                    }
                });
    }

}