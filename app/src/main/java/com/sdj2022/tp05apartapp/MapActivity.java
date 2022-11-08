package com.sdj2022.tp05apartapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.ViewGroup;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapActivity extends AppCompatActivity {

    MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Toolbar toolbar = findViewById(R.id.toolbar_map);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);


        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Intent intent = getIntent();
                String address = intent.getStringExtra("map");
                String searchAddr = address;

                Geocoder geocoder = new Geocoder(MapActivity.this, Locale.KOREA);

                StringBuffer buffer = new StringBuffer();

                try {
                    List<Address> addrs = geocoder.getFromLocationName(searchAddr, 1);

                    for( Address addr : addrs){
                        double latitude = addr.getLatitude();
                        double longitude = addr.getLongitude();

                        buffer.append(latitude+","+longitude);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String s = buffer.toString();
                String[] s2 = s.split(",");

                double latitude = Double.parseDouble(s2[0]);
                double longitude = Double.parseDouble(s2[1]);

                mapView = new MapView(MapActivity.this);
                ViewGroup mapContainer = findViewById(R.id.map_container);
                mapContainer.addView(mapView);

                mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(latitude, longitude), 3, true);

                MapPOIItem marker = new MapPOIItem();
                marker.setItemName("공급위치");
                marker.setTag(0);
                marker.setMapPoint(MapPoint.mapPointWithGeoCoord(latitude, longitude));
                marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
                marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.

                mapView.addPOIItem(marker);

            }
        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}