package com.doongis.r2.GPS;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.doongis.r2.R;
import com.doongis.r2.ServerConnect.UserVO;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class Gogosing extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    double earthRadius = 6371;

    private int timer = 0;
    private boolean isBtnClickStart = false;        //  Start 클릭이 먼저 했는지 알아보도록 체크 (그렇지 않으면 핸들러 오류를 발생하도록 )
    private boolean isBtnClickFinish = false;

    // LogCat tag
    private static final String TAG = Gogosing.class.getSimpleName();
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 5000;
    private Location mLastLocation;
    private Location mStartLocation;

    private boolean mRequestingLocationUpdates = false;

    GoogleMap map;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    //위도 경도
    double latitude;
    double longitude;

    //속도
    double speed=0;
    double distance_travelled=0;
    static double total_dist = 0;

    //DB들어갈 것
    String cycle_start_time;
    String cycle_end_time;
    String cycle_total_time;
    String cycle_distance;
    String cycle_speed;
    String user_id;
    String user_name;
    long timeS=0;
    long timeE=0;
    long timeT=0;

    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters

    Button btn_start;

    InfoGPS ig;

    //latlng
    LatLng ex_point;
    LatLng current_point;

    // UI elements
    Handler time_handler;
    Button btn_finish;
    Button btn_reset;
    Button btnAnalysis;

    // 거리 속도 시간
    private TextView tv_distance;
    private TextView tv_avg_speed;
    private TextView tv_timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gogosing);

        UserVO userVO = new UserVO();
        user_id = userVO.getUser_id();
        user_name = userVO.getUser_name();

        checkGps();

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        map = mapFragment.getMap();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        map.setMyLocationEnabled(true);

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this /* FragmentActivity */,
                            this /* OnConnectionFailedListener */)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        mStartLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);

        if (mStartLocation != null) {
            latitude = mStartLocation.getLatitude();
            longitude = mStartLocation.getLongitude();
        }
        ex_point = new LatLng(latitude, longitude);
        //current_point = new LatLng(0, 0);
        btn_start = (Button) this.findViewById(R.id.btn_timer_start);
        btn_finish = (Button) this.findViewById(R.id.btn_timer_finish);
        btn_reset = (Button) this.findViewById(R.id.btn_timer_reset);
        btnAnalysis = (Button) this.findViewById(R.id.btnanalysis);

        //거리 속도 시간
        tv_avg_speed = (TextView) findViewById(R.id.tv_avg_speed);
        tv_distance = (TextView) findViewById(R.id.tv_distance);
        tv_timer = (TextView) findViewById(R.id.tv_timer);

        // BitmapDescriptorFactory 생성하기 위한 소스
        MapsInitializer.initialize(getApplicationContext());

        // First we need to check availability of play services
        if (checkPlayServices()) {
            // Building the GoogleApi client
            buildGoogleApiClient();
            createLocationRequest();
        }

        /*버튼 클릭시 이벤트 - start*/
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.btn_timer_start) {
                    if (isBtnClickStart == true) {;
                        Toast.makeText(getApplicationContext(), "이미 시작되었습니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    timeS = System.currentTimeMillis();
                    SimpleDateFormat sdfS = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    cycle_start_time = sdfS.format(timeS);
                    Toast.makeText(getApplicationContext(), "타이머를 시작합니다.", Toast.LENGTH_SHORT).show();
                    isBtnClickStart = true;

                    time_handler = new Handler() {
                        @Override
                        public void handleMessage(android.os.Message msg) {
                            time_handler.sendEmptyMessageDelayed(0, 1000);   //  1초간격
                            timer++;
                            tv_timer.setText("주행시간 : " + timer + " 초");
                            if (timer % 5 == 0) ;

                            displayLocation();
                            togglePeriodicLocationUpdates();

                        }

                    };
                    time_handler.sendEmptyMessage(0);
                }
            }
        });

        /*버튼 클릭시 이벤트 - finish*/
        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.btn_timer_finish) {
                    if (isBtnClickStart == true) {
                        Toast.makeText(getApplicationContext(), "타이머를 멈춥니다.", Toast.LENGTH_SHORT).show();
                        time_handler.removeMessages(0);

                        timeE = System.currentTimeMillis();
                        SimpleDateFormat sdfE = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        cycle_end_time = sdfE.format(timeE);

                        SimpleDateFormat sdfT = new SimpleDateFormat("HH:mm:ss");
                        timeT=timeE-timeS-32400000;
                        cycle_total_time = sdfT.format(timeT);
                        Log.d("시간계산", timeE +", "+timeS+", "+ timeT);

                        isBtnClickStart = false;
                        isBtnClickFinish = true;
                    } else {
                        Toast.makeText(getApplicationContext(), "타이머가 시작되지 않았습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        /*버튼 클릭시 이벤트 - reset*/
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.btn_timer_reset) {
                    if (isBtnClickStart != true) {
                        mRequestingLocationUpdates = false;
                    } else {
                        time_handler.removeMessages(0);
                    }
                    mRequestingLocationUpdates = false;
                    Toast.makeText(getApplicationContext(), "타이머를 리셋합니다.", Toast.LENGTH_SHORT).show();
                    timer = 0;
                    tv_timer.setText("주행시간 :" + timer + " 초");
                    isBtnClickStart = false;

                    map.clear();
                }
            }
        });

        /*분석페이지로 고고씽*/
        btnAnalysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBtnClickFinish == false) {
                    Toast.makeText(getApplicationContext(), "FINISH 버튼 클릭 후 분석 화면으로 이동할 수 있습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    onStepInsertServer(cycle_start_time, cycle_end_time,cycle_total_time,cycle_distance,cycle_speed,user_id,user_name);
                    Intent intent = new Intent(Gogosing.this, FsActivity.class);
                    intent.putExtra("cycle_start_time", cycle_start_time);
                    intent.putExtra("cycle_end_time", cycle_end_time);
                    intent.putExtra("cycle_total_time", cycle_total_time);
                    intent.putExtra("cycle_distance", cycle_distance);
                    intent.putExtra("cycle_speed", cycle_speed);
                    startActivity(intent);
                }
            }
        });

    }

    private void checkGps() {
        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);

        Toast.makeText(Gogosing.this, String.valueOf(enabled), Toast.LENGTH_SHORT).show();

        // Check if enabled and if not send user to the GSP settings
        // Better solution would be to display a dialog and suggesting to
        // go to the settings
        if (!enabled) {
            showGpsSettingDialog();
        }
    }

    private void showGpsSettingDialog() {
          /*Create a dialog to tell user to enable his GPS settings to pinpoint his or her location*/
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Gogosing.this);
        alertDialog.setTitle("GPS설정"); /*should be on a string values*/

        alertDialog
                .setMessage("GPS가 꺼져있습니다. GPS설정을 하시겠습니까?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

        AlertDialog gpsSettingsDialog = alertDialog.create();
        gpsSettingsDialog.show();
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        checkPlayServices();

        // Resuming the periodic location updates
        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    /**
     * Method to display the location on UI
     */
    private void displayLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();

            //Toast.makeText(getApplicationContext(), latitude + ", " + longitude, Toast.LENGTH_LONG).show();
            LatLng ss = new LatLng(latitude, longitude);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(ss, 18));

            current_point = ss;
            //속도계산
            getLocation(ex_point, current_point);

            if (isBtnClickStart == true) {
                map.addPolyline(new PolylineOptions().color(0xFF65c4a8)
                        .width(15.0f).geodesic(true).add(current_point).add(ex_point));
            }
            ex_point = current_point;
            addMarkerMap(map);
        } else {
            // Toast.makeText(getApplicationContext(), "위치사용불가능", Toast.LENGTH_LONG).show();
            ig = new InfoGPS(Gogosing.this);
            ig.isGetLocation();
        }
    }

    //addMarker
    public void addMarkerMap(GoogleMap map) {
        map.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .title("접속위치")
                .alpha(0.5f)
                .anchor(0.5f, 0.5f)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
    }

    /**
     * Method to toggle periodic location updates
     */
    private void togglePeriodicLocationUpdates() {
        if (!mRequestingLocationUpdates) {
            // Changing the button text
            //Toast.makeText(getApplicationContext(), "측정끝", Toast.LENGTH_LONG).show();
            mRequestingLocationUpdates = true;
            // Starting the location updates
            startLocationUpdates();
            Log.d(TAG, "Periodic location updates started!");
        } else {
            // Changing the button text
            //Toast.makeText(getApplicationContext(), "측정시작", Toast.LENGTH_LONG).show();
            mRequestingLocationUpdates = false;
            // Stopping the location updates
            stopLocationUpdates();
        }
    }

    /**
     * Creating google api client object
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    /**
     * Creating location request object
     */
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    /**
     * Method to verify google play services on the device
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }

    /**
     * Starting the location updates
     */
    protected void startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);

    }

    /**
     * Stopping location updates
     */
    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    /**
     * Google api callback methods
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
    }

    @Override
    public void onConnected(Bundle arg0) {

        // Once connected with google api, get the location
        displayLocation();

        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        // Assign the new location
        mLastLocation = location;
        //Toast.makeText(getApplicationContext(), "Location changed!", Toast.LENGTH_SHORT).show();
        // Displaying the new location on UId
        displayLocation();
    }

    /* 속도 */
    private void getLocation(LatLng location1, LatLng location2) {
        double lat1 = location1.latitude;
        double lon1 = location1.longitude;
        double lat2 = location2.latitude;
        double lon2 = location2.longitude;

        Log.v("The previous Latitude", String.valueOf(lat1));
        Log.v("The previous Longitude", String.valueOf(lon1));

        Log.v("The current Latitude", String.valueOf(lat2));
        Log.v("The current Longitude", String.valueOf(lon2));

            /*get the distance covered from point A to point B*/
        distance_travelled = distanceTravelled(lat1, lon1, lat2, lon2);
            /*set previous latitude and longitude to the last location*/
        DecimalFormat form1 = new DecimalFormat("0.000");
        DecimalFormat form2 = new DecimalFormat("0.000");
        if (timer != 0) {
            speed = distance_travelled / timer;
            tv_avg_speed.setText("속도 : "+form2.format(speed) + " Km/h");
            cycle_speed=String.valueOf(form1.format(speed));
        }
        tv_distance.setText("주행거리 : "+form1.format(distance_travelled) + " Km");
        cycle_distance=String.valueOf(form2.format(distance_travelled));
    }

    /* 거리 계산 공식 */
    private double distanceTravelled(double lat1, double lng1, double lat2, double lng2) {

        if (lat1 != 0 && lat2 != 0) {
            double dLat = Math.toRadians(lat2 - lat1);
            double dLng = Math.toRadians(lng2 - lng1);
            double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                    Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                            Math.sin(dLng / 2) * Math.sin(dLng / 2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            double dist = earthRadius * c;
            total_dist = total_dist + dist;
        }

        return total_dist;

    }
    //데이터 입력
    public void onStepInsertServer(String cycle_start_time, String cycle_end_time, String cycle_total_time, String cycle_distance,String cycle_speed, String user_id, String user_name) {

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
        //String requestURL = "http://192.168.20.119/r2/cycle/insert";
                                     //유저IP주소입력
        String requestURL = "http://"+"        "+"/r2/cycle/insert";

        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(requestURL);

            List<NameValuePair> paramList = new ArrayList<>();
            paramList.add(new BasicNameValuePair("cycle_start_time", cycle_start_time));
            paramList.add(new BasicNameValuePair("cycle_end_time", cycle_end_time));
            paramList.add(new BasicNameValuePair("cycle_total_time", cycle_total_time));
            paramList.add(new BasicNameValuePair("cycle_distance", cycle_distance));
            paramList.add(new BasicNameValuePair("cycle_speed", cycle_speed));
            paramList.add(new BasicNameValuePair("user_id", user_id));
            paramList.add(new BasicNameValuePair("user_name", user_name));

            httpPost.setEntity(new UrlEncodedFormEntity(paramList, HTTP.UTF_8));

            HttpResponse httpResponse = client.execute(httpPost);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("입력됨", cycle_start_time + ", " + cycle_end_time + ", " + cycle_total_time + ", " + cycle_distance + ", " + cycle_speed + ", " + user_id + ", " + user_name);

    }

}