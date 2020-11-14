package com.michael.app;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private GoogleMap map;
    private LatLng cityPosition = new LatLng(59.932821, 30.329003);
    private ArrayList<Event> events = new ArrayList<>();
    private CardView info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        setEvents();
        info = findViewById(R.id.info);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                info.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        initMap();
        showEventsOnMap();
    }

    private void initMap() {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(cityPosition, 12));
        map.setOnMarkerClickListener(this);
    }

    private void setEvents() {
        events.add(new Event(0, 0, "1/1/19 0:08", "Центральный", 59.939523, 30.370717, 104577, 87878));
        events.add(new Event(1, 0, "1/1/19 1:08", "Центральный", 59.925144, 30.357413, 104577, 87878));
        events.add(new Event(2, 0, "1/1/19 0:58", "Центральный", 59.925704, 30.285435, 104577, 87878));
        events.add(new Event(3, 0, "1/1/19 2:00", "Центральный", 59.924113, 30.282849, 104577, 87878));
        events.add(new Event(4, 0, "1/1/19 4:37", "Центральный", 59.913009, 30.349078, 104577, 87878));
    }

    private void showEventsOnMap() {
        /* Получение ширины и высоты экрана. */
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int weightDisplay = size.x;
        int heightDisplay = size.y;
        /* Коэфициент уменьшения маркера относительно экрана. */
        float coefficient = 0.016f;
        /* Сумма ширины и высоты экрана. */
        int sumSize = weightDisplay + heightDisplay;
        /* Установка размера иконки (ширина = высота). */
        int iconSize = (int) (coefficient * sumSize);
        /* Ресурс иконки маркера. */
        BitmapDrawable bitmapDrawable = (BitmapDrawable) getResources().getDrawable(R.drawable.marker);
        /* Установка выбранного изображения. */
        Bitmap icon = Bitmap.createScaledBitmap(bitmapDrawable.getBitmap(), iconSize, iconSize, false);

        for (Event event: events) {
            map.addMarker(new MarkerOptions()
                    .position(new LatLng(event.lat, event.lon))
                    .icon(BitmapDescriptorFactory.fromBitmap(icon))).setTag(String.valueOf(event.id));
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        updateInfoMarker(marker);
        if (info.getVisibility() == View.GONE) {
            info.setVisibility(View.VISIBLE);
        }
        return false;
    }

    private void updateInfoMarker(Marker marker) {

    }
}