package com.sdj2022.tp05apartapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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

public class SecondActivity extends AppCompatActivity {

    RecyclerView recycler;
    ArrayList<SecondRecyclerItem> items = new ArrayList<>();
    SecondRecyclerAdapter adapter;

    int[] regionCount = new int[17];

    TextView tv;
    int result = 0;

    AppCompatButton btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("데이터 불러오는 중");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        dialog.setCancelable(false);

        dialog.show();


        // 툴바 설정/////////////////////////////////////
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        actionBar.setDisplayShowTitleEnabled(false);
        ///////////////////////////////////////////////

        btn = findViewById(R.id.btn);
        btn.setOnClickListener(v->clickUpButton());

        tv = findViewById(R.id.tv);

        recycler = findViewById(R.id.recycler);
        adapter = new SecondRecyclerAdapter(this, items);
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

                // 날짜 얻어오기
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");

                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date());
                String yM1 = sdf.format(cal.getTime());
                cal.add(Calendar.MONTH, -1); //if문으로 년도 계산 후 1월이면 년도도 빼기?
                String yM2 = sdf.format(cal.getTime());

                String address = "https://api.odcloud.kr/api/ApplyhomeInfoDetailSvc/v1/getAPTLttotPblancDetail?page=1&perPage=100&returnType=XML&cond%5BRCRIT_PBLANC_DE%3A%3ALTE%5D="
                        +yM1+"-31"
                        +"&cond%5BRCRIT_PBLANC_DE%3A%3AGTE%5D="
                        +yM2+"-01"
                        +"&serviceKey=3N5o9gGDyQiQJjpEf70%2BoI2iCs0ocxvkwGUn%2BmEnyn%2FZh%2F3%2BfF7rlU4sOFs8MooNXbstezb4dAZvOpq3LhxTsw%3D%3D";

                try {
                    URL url = new URL(address);

                    InputStream is = url.openStream();
                    InputStreamReader isr = new InputStreamReader(is);

                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    XmlPullParser xpp = factory.newPullParser();

                    xpp.setInput(isr);

                    int eventType = xpp.getEventType();


                    while(eventType != XmlPullParser.END_DOCUMENT){

                        switch (eventType){
                            case XmlPullParser.START_DOCUMENT:
                                break;

                            case XmlPullParser.START_TAG:

                                break;

                            case XmlPullParser.TEXT:

                                String region = xpp.getText();

                                if(region.equals("서울")){
                                    regionCount[0]++;

                                }else if(region.equals("강원")){
                                    regionCount[1]++;

                                }else if(region.equals("대전")){
                                    regionCount[2]++;

                                }else if(region.equals("충남")){
                                    regionCount[3]++;

                                }else if(region.equals("세종")){
                                    regionCount[4]++;

                                }else if(region.equals("충북")){
                                    regionCount[5]++;

                                }else if(region.equals("인천")){
                                    regionCount[6]++;

                                }else if(region.equals("경기")){
                                    regionCount[7]++;

                                }else if(region.equals("광주")){
                                    regionCount[8]++;

                                }else if(region.equals("전남")){
                                    regionCount[9]++;

                                }else if(region.equals("전북")){
                                    regionCount[10]++;

                                }else if(region.equals("부산")){
                                    regionCount[11]++;

                                }else if(region.equals("경남")){
                                    regionCount[12]++;

                                }else if(region.equals("울산")){
                                    regionCount[13]++;

                                }else if(region.equals("제주")){
                                    regionCount[14]++;

                                }else if(region.equals("대구")){
                                    regionCount[15]++;

                                }else if(region.equals("경북")){
                                    regionCount[16]++;

                                }

                                break;

                            case XmlPullParser.END_TAG:
                                if(xpp.getName().equals("data")){
                                    result= 0;
                                    for(int i = 0; i<regionCount.length; i++){
                                        result += regionCount[i];
                                    }
                                    items.add(new SecondRecyclerItem("전체"+" ("+result+"건)"));
                                    items.add(new SecondRecyclerItem("서울"+" ("+regionCount[0]+"건)"));
                                    items.add(new SecondRecyclerItem("강원"+" ("+regionCount[1]+"건)"));
                                    items.add(new SecondRecyclerItem("대전"+" ("+regionCount[2]+"건)"));
                                    items.add(new SecondRecyclerItem("충남"+" ("+regionCount[3]+"건)"));
                                    items.add(new SecondRecyclerItem("세종"+" ("+regionCount[4]+"건)"));
                                    items.add(new SecondRecyclerItem("충북"+" ("+regionCount[5]+"건)"));
                                    items.add(new SecondRecyclerItem("인천"+" ("+regionCount[6]+"건)"));
                                    items.add(new SecondRecyclerItem("경기"+" ("+regionCount[7]+"건)"));
                                    items.add(new SecondRecyclerItem("광주"+" ("+regionCount[8]+"건)"));
                                    items.add(new SecondRecyclerItem("전남"+" ("+regionCount[9]+"건)"));
                                    items.add(new SecondRecyclerItem("전북"+" ("+regionCount[10]+"건)"));
                                    items.add(new SecondRecyclerItem("부산"+" ("+regionCount[11]+"건)"));
                                    items.add(new SecondRecyclerItem("경남"+" ("+regionCount[12]+"건)"));
                                    items.add(new SecondRecyclerItem("울산"+" ("+regionCount[13]+"건)"));
                                    items.add(new SecondRecyclerItem("제주"+" ("+regionCount[14]+"건)"));
                                    items.add(new SecondRecyclerItem("대구"+" ("+regionCount[15]+"건)"));
                                    items.add(new SecondRecyclerItem("경북"+" ("+regionCount[16]+"건)"));
                                }
                                break;
                        }//switch
                        eventType = xpp.next();
                    }//while

                    dialog.dismiss();
                    isr.close();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();

                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일 ");
                            String yearMonth = sdf.format(new Date());

                            tv.setText(yearMonth+"- 총 "+result+"건 검색");
                        }
                    });

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }

            }//run method
        };
        thread.start();

    }//onCreate method

    //툴바 업버튼 설정////////////////////////
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }///////////////////////////////////////


    void clickUpButton(){
        recycler.scrollToPosition(0);
        btn.setVisibility(View.INVISIBLE);
    }
}
