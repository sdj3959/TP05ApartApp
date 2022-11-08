package com.sdj2022.tp05apartapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ThirdActivity extends AppCompatActivity {

    ArrayList<ThirdRecyclerItem> items = new ArrayList<>();
    RecyclerView recycler;
    ThirdRecyclerAdapter adapter;

    TextView tvNoResult;
    AppCompatButton btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //TODO 업데이트 목록 ---------------------------------------------
        //TODO MainActivity statusBar 색상 흰색으로 변경(+상태아이콘 어둡게)
        //TODO SecondActivity에 FloatingButton 추가 - 기능 : 내가 고른지역만 보여질 수 있도록 지역다중 선택(CheckBox), 오른쪽끝에 스크롤바 추가하기
        //TODO ThirdActivity 지역별 탭으로 이동추가(탭 클릭시 파싱 다시 시작하여 리사이클러뷰 적용)
        //TODO Third 찜 기능 - 클릭시 별표색상이 변경되는 아이콘 리사이클러 아이템에 배치 후 Floating 버튼으로 내가 찜한 청약정보 액티비티 이동
        //TODO MapAc
        //TODO 업데이트 내역 업로드 : 확인 가능한 청약정보 기간연장, 메인화면 버전정보 추가, 지도버튼 오류 현상 수정
        //TODO ---------------------------------------------------------
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        tvNoResult = findViewById(R.id.tv_no_result);
        btn = findViewById(R.id.btn);
        btn.setOnClickListener(v->clickUpButton());

        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("데이터 불러오는 중");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        dialog.setCanceledOnTouchOutside(false);

        dialog.show();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);

        Intent intent = getIntent();
        String region = intent.getStringExtra("region");

        String[] urlRegion = region.split(" "); //index 0 - 지역이름


        TextView tv = findViewById(R.id.tv);
        tv.setText(region);

        recycler = findViewById(R.id.recycler);
        adapter = new ThirdRecyclerAdapter(this, items);
        recycler.setAdapter(adapter);

        recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy>50){
                    btn.setVisibility(View.VISIBLE);
                    dy = 0;
                }
                if(dy<-50){
                    btn.setVisibility(View.INVISIBLE);
                    dy = 0;
                }

            }
        });

        Thread thread = new Thread(){
            @Override
            public void run() {
                super.run();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");

                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date());
                String yM1 = sdf.format(cal.getTime());
                cal.add(Calendar.MONTH, -1); //if문으로 년도 계산 후 1월이면 년도도 빼기?
                String yM2 = sdf.format(cal.getTime());


                String address;

                if(urlRegion[0].equals("전체")) {
                    address = "https://api.odcloud.kr/api/ApplyhomeInfoDetailSvc/v1/getAPTLttotPblancDetail?page=1&perPage=100&returnType=XML&cond%5BRCRIT_PBLANC_DE%3A%3ALTE%5D="
                            +yM1+"-31"
                            +"&cond%5BRCRIT_PBLANC_DE%3A%3AGTE%5D="
                            +yM2+"-01"
                            +"&serviceKey=3N5o9gGDyQiQJjpEf70%2BoI2iCs0ocxvkwGUn%2BmEnyn%2FZh%2F3%2BfF7rlU4sOFs8MooNXbstezb4dAZvOpq3LhxTsw%3D%3D";
                }else {
                    address = "https://api.odcloud.kr/api/ApplyhomeInfoDetailSvc/v1/getAPTLttotPblancDetail?page=1&perPage=100&returnType=XML"
                            +"&cond%5BSUBSCRPT_AREA_CODE_NM%3A%3AEQ%5D="
                            +urlRegion[0]
                            +"&cond%5BRCRIT_PBLANC_DE%3A%3ALTE%5D="
                            +yM1+"-31"
                            +"&cond%5BRCRIT_PBLANC_DE%3A%3AGTE%5D="
                            +yM2+"-01"
                            +"&serviceKey=3N5o9gGDyQiQJjpEf70%2BoI2iCs0ocxvkwGUn%2BmEnyn%2FZh%2F3%2BfF7rlU4sOFs8MooNXbstezb4dAZvOpq3LhxTsw%3D%3D";
                }

                try {
                    URL url = new URL(address);
                    InputStream is = url.openStream();
                    InputStreamReader isr = new InputStreamReader(is);

                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    XmlPullParser xpp = factory.newPullParser();

                    xpp.setInput(isr);

                    int eventType = xpp.getEventType();

                    ThirdRecyclerItem item = null;

                    while(eventType != XmlPullParser.END_DOCUMENT){

                        switch (eventType){
                            case XmlPullParser.START_DOCUMENT:
                                break;

                            case XmlPullParser.START_TAG:

                                if(xpp.getName().equals("currentCount")){
                                    xpp.next();
                                    if(xpp.getText().equals("0")){
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                tvNoResult.setVisibility(View.VISIBLE);
                                            }
                                        });
                                    }
                                }else if(xpp.getName().equals("item")){
                                    item = new ThirdRecyclerItem();
                                }else if(xpp.getName().equals("col")){
                                    String tagName = xpp.getAttributeValue(0);

                                    if(tagName.equals("HOUSE_NM")){
                                        xpp.next();
                                        item.name = xpp.getText();
                                    }else if(tagName.equals("SUBSCRPT_AREA_CODE_NM")){
                                        xpp.next();
                                        item.regName = xpp.getText();
                                    }else if(tagName.equals("HSSPLY_ADRES")){
                                        xpp.next();
                                        item.location = xpp.getText();
                                    }else if(tagName.equals("RCEPT_BGNDE")){
                                        xpp.next();
                                        item.startDate = xpp.getText();
                                    }else if(tagName.equals("RCEPT_ENDDE")){
                                        xpp.next();
                                        item.endDate = xpp.getText();
                                    }else if(tagName.equals("HMPG_ADRES")){
                                        xpp.next();
                                        String s = xpp.getText();
                                        if(s != null)item.address = xpp.getText();
                                        else item.address = "없음";
                                    }else if(tagName.equals("CNSTRCT_ENTRPS_NM")){
                                        xpp.next();
                                        item.company = xpp.getText();
                                    }else if(tagName.equals("MDHS_TELNO")){
                                        xpp.next();
                                        item.tel = xpp.getText();
                                    }else if(tagName.equals("BSNS_MBY_NM")){
                                        xpp.next();
                                        item.bank = xpp.getText();
                                    }else if(tagName.equals("MVN_PREARNGE_YM")){
                                        xpp.next();
                                        item.goInsideDate = xpp.getText();
                                    }
                                }

                                break;

                            case XmlPullParser.TEXT:
                                break;

                            case XmlPullParser.END_TAG:
                                if(xpp.getName().equals("item")){
                                    items.add(item);
                                }
                                break;
                        }//switch
                        eventType = xpp.next();
                    }//while
                    dialog.dismiss();
                    isr.close();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });


            }//run method
        };//Thread
        thread.start();
    }//onCreate method

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = this.getMenuInflater();
        menuInflater.inflate(R.menu.menu_help, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("도움말");
        builder.setMessage("▣ 청약 접수일\n\n  ·빨간색 : 접수 불가\n  ·파란색 : 접수 중\n  ·보라색 : 접수 예정\n\n▣ 입주예정년월\n\n  ·예시) 202205 - 2022년 05월 예정\n\n▣ 자료출처\n\n  ·한국부동산원 청약홈\n  ·문의 : 1644-7445");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        return super.onOptionsItemSelected(item);
    }

    void clickUpButton(){
        recycler.scrollToPosition(0);
        btn.setVisibility(View.INVISIBLE);
    }
}
