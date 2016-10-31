/*
* 볼래?  페이지 의 하위 : 글쓰기 페이지( Activity 형식 )
*
*
* */
package com.doongis.r2.model;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.doongis.r2.R;
import com.doongis.r2.ServerConnect.UserVO;
import com.doongis.r2.activity.LoginActivity;
import com.doongis.r2.activity.MainActivity;
import com.doongis.r2.fragment.Main_1;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class main_1_Write extends FragmentActivity
        implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_CAMERA = 2;

    private Uri mImageCaptureUri;
    private ImageView mPhotoImageView;
    private Button mButton;

    Button registContent;

    private BackPressCloseHandler backPressCloseHandler;

    Spinner spinner;
    EditText mySpinnerValue;

    String url = null;
    EditText article_tit;
    EditText article_con;

    UserVO userVO=new UserVO();
    String user_id =userVO.getUser_id();
    String user_name=userVO.getUser_name();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_1__write);

        // 종목선택
        spinner = (Spinner) findViewById(R.id.spinner);
        mySpinnerValue = (EditText) findViewById(R.id.mySpinnerValue);
        mySpinnerValue.setFocusable(false);
        mySpinnerValue.setClickable(false);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.sport,
                android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        //  갤러리, 카메라 작업
        mButton = (Button) findViewById(R.id.btn_UploadPicture);
        mPhotoImageView = (ImageView) findViewById(R.id.image);

        mButton.setOnClickListener(this);

        article_tit = (EditText) findViewById(R.id.article_tit);
        article_con = (EditText) findViewById(R.id.article_con);

        registContent = (Button) findViewById(R.id.registContent);

        Log.d("유저브이오",userVO.toString());

        registContent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (article_tit == null || article_con == null || url == null) {
                    Toast.makeText(getApplicationContext(), "제목, 내용, 사진을 등록해주세용!", Toast.LENGTH_LONG).show();
                } else {
                    String article_tit1 = article_tit.getText().toString();
                    String article_con1 = article_con.getText().toString();
                    onArticleInsertServer(article_tit1, article_con1, url, user_id, user_name);

                    Intent intent = new Intent(view.getContext(), MainActivity.class);
                    //DB에 넣을것+용량제한(if, else)
                    Toast.makeText(getApplicationContext(), "글이 등록되었습니다.", Toast.LENGTH_LONG).show();
                    startActivity(intent);
                }
            }
        });
    }

    /*
    * 카메라에서 이미지 가져오기
    */
    private void doTakePhotoAction() {
    /*
     * 참고 해볼곳
     * http://2009.hfoss.org/Tutorial:Camera_and_Gallery_Demo
     * http://stackoverflow.com/questions/1050297/how-to-get-the-url-of-the-captured-image
     * http://www.damonkohler.com/2009/02/android-recipes.html
     * http://www.firstclown.us/tag/android/
     */

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // 임시로 사용할 파일의 경로를 생성
        url = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
        mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));

        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
        // 특정기기에서 사진을 저장못하는 문제가 있어 다음을 주석처리 합니다.
        //intent.putExtra("return-data", true);
        startActivityForResult(intent, PICK_FROM_CAMERA);
    }

    /**
     * 앨범에서 이미지 가져오기
     */
    private void doTakeAlbumAction() {
        // 앨범 호출
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case CROP_FROM_CAMERA: {
                //  크롭이 된 이후 이미지를 넘겨받는다.
                //  이미지뷰에 이미지를 보여준다거나 부가적인 작업 이후에
                //  임시파일 삭제
                final Bundle extras = data.getExtras();

                if (extras != null) {
                    Bitmap photo = extras.getParcelable("data");
                    mPhotoImageView.setImageBitmap(photo);
                }
                // 임시 파일 삭제
                File f = new File(mImageCaptureUri.getPath());
                if (f.exists()) {
                    f.delete();
                }
                break;
            }
            case PICK_FROM_ALBUM: {
                // 이후의 처리가 카메라와 같으므로 일단  break없이 진행합니다.
                // 실제 코드에서는 좀더 합리적인 방법을 선택하시기 바랍니다.

                mImageCaptureUri = data.getData();
            }

            case PICK_FROM_CAMERA: {
                // 이미지를 가져온 이후의 리사이즈할 이미지 크기를 결정합니다.
                // 이후에 이미지 크롭 어플리케이션을 호출하게 됩니다.

                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(mImageCaptureUri, "image/*");

                intent.putExtra("outputX", 200);
                intent.putExtra("outputY", 200);
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("scale", true);
                intent.putExtra("return-data", true);
                startActivityForResult(intent, CROP_FROM_CAMERA);

                url=mImageCaptureUri.toString();
                break;
            }
        }

    }

    /* 갤러리, 카메라 작업 */
    @Override
    public void onClick(View v) {
        DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                doTakePhotoAction();
            }
        };

        DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                doTakeAlbumAction();
            }
        };

        DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        };

        new AlertDialog.Builder(this)
                .setTitle("업로드할 이미지 선택")
                .setPositiveButton("사진촬영", cameraListener)
                .setNeutralButton("앨범선택", albumListener)
                .setNegativeButton("취소", cancelListener)
                .show();
    }

    /* 종목선택 */
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        TextView mytext = (TextView) view;
        mySpinnerValue.setText(mytext.getText());
    }

    /* 종목선택 */
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    //로그인 후 데이터 입력
    public void onArticleInsertServer(String article_title, String article_content, String article_url, String user_id, String user_name) {

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
        //String requestURL = "http://192.168.20.119/r2/article/insert";
                                //유저IP주소입력
        String requestURL = "http://"+"    "+"/r2/article/insert";
        long timeM = System.currentTimeMillis();
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String dateNow = sdfNow.format(timeM);

        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(requestURL);

            List<NameValuePair> paramList = new ArrayList<>();
            paramList.add(new BasicNameValuePair("article_title", article_title));
            paramList.add(new BasicNameValuePair("article_date", dateNow));
            paramList.add(new BasicNameValuePair("article_content", article_content));
            paramList.add(new BasicNameValuePair("article_url", article_url));
            paramList.add(new BasicNameValuePair("board_no", "1"));
            paramList.add(new BasicNameValuePair("user_id", user_id));
            paramList.add(new BasicNameValuePair("user_name", user_name));

            httpPost.setEntity(new UrlEncodedFormEntity(paramList, HTTP.UTF_8));

            HttpResponse httpResponse = client.execute(httpPost);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("입력됨", article_title + ", " + article_content + ", "+dateNow+", " + article_url+", " +user_id+", "+user_name);

    }
}