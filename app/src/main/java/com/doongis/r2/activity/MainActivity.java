package com.doongis.r2.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.doongis.r2.model.BackPressCloseHandler;
import com.doongis.r2.R;
import com.doongis.r2.adapter.SlidingMenuAdapter;
import com.doongis.r2.fragment.Main_1;
import com.doongis.r2.fragment.Main_2;
import com.doongis.r2.fragment.Main_3;
import com.doongis.r2.fragment.Main_4;
import com.doongis.r2.fragment.Main_5;
import com.doongis.r2.fragment.Main_6;
import com.doongis.r2.model.ItemSlideMenu;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends LoginActivity {

    private List<ItemSlideMenu> listSliding;
    private SlidingMenuAdapter adapter;
    private ListView listViewSliding;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private LinearLayout LL01;

    private BackPressCloseHandler backPressCloseHandler;    //  취소키 이벤트


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        backPressCloseHandler = new BackPressCloseHandler(this);    //  취소키 이벤트

        listViewSliding = (ListView) findViewById(R.id.lv_sliding_menu);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        listSliding = new ArrayList<>();

        LL01 = (LinearLayout) findViewById(R.id.LL01);

        //  슬라이딩 메뉴에 들어갈 리스트 목록들
        listSliding.add(new ItemSlideMenu(R.drawable.logo6, "볼래?"));
        listSliding.add(new ItemSlideMenu(R.drawable.logo2, "탈래?"));
        listSliding.add(new ItemSlideMenu(R.drawable.logo4, "운동기록"));
        listSliding.add(new ItemSlideMenu(R.drawable.loho5, "자유게시판"));
        listSliding.add(new ItemSlideMenu(R.drawable.logo9, "마이페이지"));
        listSliding.add(new ItemSlideMenu(R.drawable.logo3, "QnA"));
        adapter = new SlidingMenuAdapter(this, listSliding);
        listViewSliding.setAdapter(adapter);

        //Display icon to open/ close sliding list
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //  타이틀 설정
        setTitle(listSliding.get(0).getTitle());

        //  슬라이딩 메뉴 내의 아이템 선택
        listViewSliding.setItemChecked(0, true);

        //  슬라이딩 메뉴 닫기
        drawerLayout.closeDrawer(listViewSliding);

        //  MainActivity의 맨 처음 시작 화면은 Main_1 ( 볼래? 페이지 = fragment0 )을 시작으로 힌다.
        replaceFragment(0);

        //  슬라이딩 메뉴 안의 아이템을 클릭하면 다음과 같은 동작을 실행한다.

        listViewSliding.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //  타이틀 설정
                setTitle(listSliding.get(position).getTitle());
                //  아이템 선택
                listViewSliding.setItemChecked(position, true);
                //  fragment 새로고침
                replaceFragment(position);
                //  Menu 닫기
                drawerLayout.closeDrawer(listViewSliding);
            }
        });

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_opened, R.string.drawer_closed){

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }
        };

        drawerLayout.setDrawerListener(actionBarDrawerToggle);
    }

    @Override                      //  스마트폰 자체의 취소버튼 오버라이딩
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
        //  스마트폰 취소버튼 누를 시, 클래스 BackPressCloseHandler 를 불러와서 어플 종료 시키는 방법.
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            logout();
            return true;
        }

        if(actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    //  fragment가 눌려지면 실행할 Method
    private void replaceFragment(int pos) {
        Fragment fragment = null;
        switch (pos) {
            case 0:     //  슬라이드 '볼래?' 클릭
                fragment = new Main_1();
                break;
            case 1:     //  슬라이드 '탈래?' 클릭
                fragment = new Main_2();
                break;
            case 2:     //  슬라이드 '운동기록' 클릭
                fragment = new Main_3();
                break;
            case 3:     //  슬라이드 '자유게시판' 클릭
                fragment = new Main_4();
                break;
            case 4:     //  슬라이드 '마이페이지' 클릭
                fragment = new Main_5();
                break;
            case 5:    //  슬라이드 'QnA' 클릭
                fragment = new Main_6();
                break;

        }

        if(null!=fragment) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.main_content, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
    //로그아웃 바 만들기

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
