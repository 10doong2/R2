package com.doongis.r2.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.doongis.r2.R;
import com.doongis.r2.ServerConnect.ArticleVO;

import java.net.URL;
import java.util.ArrayList;

public class TestAdapter extends BaseAdapter {

    URL file_path;
    Bitmap bm;
    Context context;
    ArrayList<ArticleVO> arrayList = new ArrayList<ArticleVO>();

    public TestAdapter() {
    }
/*

    public TestAdapter(Context context, ArrayList<ArticleVO> arrayList,int fragment_layout) {
        this.context = context;
        this.arrayList = arrayList;
        this.fragment_layout = fragment_layout;
        inf =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
*/

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.main_1_item_article, parent, false);
        }
        TextView tv_name = (TextView) convertView.findViewById(R.id.tv_name);
        TextView tv_date = (TextView) convertView.findViewById(R.id.content_date);
        TextView tv_content = (TextView) convertView.findViewById(R.id.tv_content);
        ImageView userImage = (ImageView) convertView.findViewById(R.id.userImage);
        ImageView content_Image = (ImageView) convertView.findViewById(R.id.content_Image);

        ArticleVO articleVO = arrayList.get(position);
        tv_name.setText(articleVO.getUser_name() + "");
        tv_date.setText(articleVO.getArticle_date() + "");
        tv_content.setText(articleVO.getArticle_title() + "\n" + articleVO.getArticle_content() + "");

        //안드로이드 UI접근 예외처리 -> android.os.NetworkOnMainThreadException
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());

        try {
            file_path = new URL("https://graph.facebook.com/" + articleVO.getUser_id() + "/picture?type=small");
            bm = BitmapFactory.decodeStream(file_path.openConnection().getInputStream());  //비트맵 형태 저장
            Log.d("페이스북연결", "bm실행됨" + file_path);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("페이스북연결", "bm실행안됨" + articleVO.getUser_id());
        }
        userImage.setImageBitmap(bm);

        return convertView;
    }

    public void addItem(String article_title, String article_date, String article_content, String user_id, String user_name) {
        ArticleVO articleVO = new ArticleVO();
        articleVO.setArticle_title(article_title);
        articleVO.setArticle_date(article_date);
        articleVO.setArticle_content(article_content);
        //articleVO.setArticle_url(article_url);
        articleVO.setUser_id(user_id);
        articleVO.setUser_name(user_name);

        arrayList.add(articleVO);
    }
}
