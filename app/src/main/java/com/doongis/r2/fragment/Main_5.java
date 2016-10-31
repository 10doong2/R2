/*
* 마이페이지  페이지 ( Fragment 형식 )
*
*
* */

package com.doongis.r2.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.doongis.r2.FIT.FitRecordActivity;
import com.doongis.r2.GPS.GogoRecordActivity;
import com.doongis.r2.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;

import org.json.JSONObject;

import java.net.URL;

public class Main_5 extends Fragment {

    /* A reference to the Firebase */
    private Firebase mFirebaseRef;

    /* Data from the authenticated user */
    public AuthData mAuthData;

    /* Listener for Firebase session changes */
    private Firebase.AuthStateListener mAuthStateListener;

    private ImageView imgView;
    private TextView MypageName;
    CallbackManager callbackManager;

    String name1;
    String facebook_id;
    URL file_path;
    Bitmap bm;

    Button btnmy_cycle_record;
    Button btnmy_step_record;

    public Main_5() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_5, container, false);


        btnmy_cycle_record = (Button) rootView.findViewById(R.id.btnmy_cycle_record);
        btnmy_step_record = (Button) rootView.findViewById(R.id.btn_step_record);

        btnmy_cycle_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), GogoRecordActivity.class);
                startActivity(intent);
            }
        });
        btnmy_step_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FitRecordActivity.class);
                startActivity(intent);
            }
        });



        //안드로이드 UI접근 예외처리 -> android.os.NetworkOnMainThreadException
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());

        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

            @Override
            public void onCompleted(JSONObject objects, GraphResponse response) {
               // Log.d("페이스북결과", response.toString());

                try {
                    name1 = objects.getString("name");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Bundle parameters = new Bundle();
        //Log.d("페이스북아이디", "{"+facebook_id);
        parameters.putString("fields", "id, name");
        request.setParameters(parameters);
        request.executeAsync();

        MypageName = (TextView) rootView.findViewById(R.id.MypageName);
        imgView = (ImageView) rootView.findViewById(R.id.imgView);

       /* Create the Firebase ref that is used for all authentication with Firebase */
        mFirebaseRef = new Firebase(getResources().getString(R.string.firebase_url));

        /* Setup the progress dialog that is displayed later when authenticating with Firebase */
        mAuthStateListener = new Firebase.AuthStateListener() {
            @Override
            public void onAuthStateChanged(AuthData authData) {
                setAuthenticatedUser(authData);
            }
        };

        mFirebaseRef.addAuthStateListener(mAuthStateListener);

        return rootView;
    }

    //로그인 프로필 가져올 부분
    private void setAuthenticatedUser(AuthData authData) {

        if (authData != null) {
            String name = null;
            if (authData.getProvider().equals("facebook")
                    || authData.getProvider().equals("google")
                    || authData.getProvider().equals("twitter")) {
                name = (String) authData.getProviderData().get("displayName");
                facebook_id = (String) authData.getProviderData().get("id");
                Log.d("authData_success", name + facebook_id + authData.getProvider());
            } else {
                Log.d("authData_error", "Invalid provider: " + authData.getProvider());
            }
            if (name != null) {

                MypageName.setText(name);
                    try {
                        file_path = new URL("https://graph.facebook.com/" + facebook_id + "/picture?type=large");
                        bm = BitmapFactory.decodeStream(file_path.openConnection().getInputStream());  //비트맵 형태 저장
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    imgView.setImageBitmap(bm);
            }
            }

        this.mAuthData = authData;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode,data);
    }

}
