package com.michael.app;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.michael.app.web.WebCallback;
import com.michael.app.web.WebClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MapsActivity extends AppCompatActivity
        implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener {
    public final static String TAG = "WEB_CLIENT";
    private GoogleMap map;
    private LatLng cityPosition = new LatLng(59.932821, 30.329003);
    private ArrayList<Event> events = new ArrayList<>();
    private CardView info;
    private boolean isShowCold = true, isShowHot = true, isShowCar = true, isShowFire = true;
    private ImageButton buttonCold, buttonHot, buttonCar, buttonFire;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        info = findViewById(R.id.info);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                info.setVisibility(View.GONE);
            }
        });
        getEventsFromServer();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        initMap();
        initButtons();
        showEventsOnMap();
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (info != null) info.setVisibility(View.GONE);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        updateInfoMarker(marker);
        if (info.getVisibility() == View.GONE) {
            info.setVisibility(View.VISIBLE);
        }
        return false;
    }

    private void initMap() {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(cityPosition, 12));
        map.setOnMarkerClickListener(this);
        map.setOnMapClickListener(this);
    }

    private void initButtons() {
        buttonCold = findViewById(R.id.buttonColdWater);
        buttonHot = findViewById(R.id.buttonHotWater);
        buttonCar = findViewById(R.id.buttonCar);
        buttonFire = findViewById(R.id.buttonFire);
        buttonHot.setOnClickListener(view -> {
            if (view.getAlpha() == 1) {
                isShowHot = false;
                view.setAlpha(0.5f);
            } else {
                isShowHot = true;
                view.setAlpha(1);
            }
            showEventsOnMap();
            if (info != null) info.setVisibility(View.GONE);
        });
        buttonCold.setOnClickListener(view -> {
            if (view.getAlpha() == 1) {
                isShowCold = false;
                view.setAlpha(0.5f);
            } else {
                isShowCold = true;
                view.setAlpha(1);
            }
            showEventsOnMap();
            if (info != null) info.setVisibility(View.GONE);
        });
        buttonCar.setOnClickListener(view -> {
            if (view.getAlpha() == 1) {
                isShowCar = false;
                view.setAlpha(0.5f);
            } else {
                isShowCar = true;
                view.setAlpha(1);
            }
            showEventsOnMap();
            if (info != null) info.setVisibility(View.GONE);
        });
        buttonFire.setOnClickListener(view -> {
            if (view.getAlpha() == 1) {
                isShowFire = false;
                view.setAlpha(0.5f);
            } else {
                isShowFire = true;
                view.setAlpha(1);
            }
            showEventsOnMap();
            if (info != null) info.setVisibility(View.GONE);
        });
    }

    private void setEvents() {
        events.add(new Event(0, 0, "1/1/19 0:08", "Центральный", 59.939523, 30.370717, 104577, 87878));
        events.add(new Event(1, 1, "1/1/19 1:08", "Центральный", 59.925144, 30.357413, 104577, 87878));
        events.add(new Event(2, 2, "1/1/19 0:58", "Центральный", 59.925704, 30.285435, 104577, 87878));
        events.add(new Event(3, 3, "1/1/19 2:00", "Центральный", 59.924113, 30.282849, 104577, 87878));
        events.add(new Event(4, 3, "1/1/19 4:37", "Центральный", 59.913009, 30.349078, 104577, 87878));
    }

    private void showEventsOnMap() {
        map.clear();

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
        BitmapDrawable bitmapDrawable0 = (BitmapDrawable) getResources().getDrawable(R.drawable.marker_0);
        BitmapDrawable bitmapDrawable1 = (BitmapDrawable) getResources().getDrawable(R.drawable.marker_1);
        /* Установка выбранного изображения. */
        Bitmap icon0 = Bitmap.createScaledBitmap(bitmapDrawable0.getBitmap(), iconSize, iconSize, false);
        Bitmap icon1 = Bitmap.createScaledBitmap(bitmapDrawable1.getBitmap(), iconSize, iconSize, false);

        for (Event event: events) {
            if (event.type == 1 && isShowHot || event.type == 2 && isShowCold || event.type == 3 && isShowCar || event.type == 4 && isShowFire) {
                Bitmap icon = event.danger == 1 ? icon1 : icon0;
                map.addMarker(new MarkerOptions()
                        .position(new LatLng(event.lat, event.lon))
                        .icon(BitmapDescriptorFactory.fromBitmap(icon))).setTag(String.valueOf(event.id));
            }
        }
    }

    private Event getEventById(int id) {
        for (Event event: events) {
            if (event.id == id) {
                return event;
            }
        }
        return null;
    }

    private void updateInfoMarker(Marker marker) {
        Event event = getEventById(Integer.parseInt(String.valueOf(marker.getTag())));

        TextView textType, textDistrict, textTime, textDate, textAddress, textBuilding, textLat, textLon;
        textType = findViewById(R.id.textType);
        textDistrict = findViewById(R.id.textDistrict);
        textTime = findViewById(R.id.textTime);
        textDate = findViewById(R.id.textDate);
        textAddress = findViewById(R.id.textAddress);
        textBuilding = findViewById(R.id.textBuilding);
        textLat = findViewById(R.id.textLat);
        textLon = findViewById(R.id.textLon);

        switch (event.type) {
            case 1:
                textType.setText("Проблемы с горячим водоснабжением");
                break;
            case 2:
                textType.setText("Проблемы с холодным водоснабжением");
                break;
            case 3:
                textType.setText("ДТП");
                break;
            case 4:
                textType.setText("Пожар");
                break;
        }

        textDistrict.setText(event.district);
        textTime.setText(event.time.split(" ")[1]);
        textDate.setText(event.time.split(" ")[0]);
        textAddress.setText(String.valueOf(event.idAddress));
        textBuilding.setText(String.valueOf(event.idBuilding));
        textLat.setText(String.valueOf(event.lat));
        textLon.setText(String.valueOf(event.lon));
    }

    /** Метод для тестирования взаимодействия с сервером. */
    private void getEventsFromServer() {
        WebCallback webCallback = new WebCallback() {
            @Override
            public void onSuccess(String body) {
                try {
                    events.clear();
                    JSONArray array = new JSONArray(body);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject data = array.getJSONObject(i);
                        int id = data.getInt("id");
                        int type = data.getJSONObject("category").getInt("id");
                        String time = data.getString("datetime");
                        String district = data.getJSONObject("area").getString("name");
                        double lat = data.getDouble("latitude");
                        double lon = data.getDouble("longitude");
                        int address = data.getInt("eas_address");
                        int building = data.getInt("eas_building");
                        events.add(new Event(id, type, time, district, lat, lon, address, building));
                        // events.get(events.size() - 1).danger = (events.get(events.size() - 1).id % 3 == 0 ? 1 : 0);
                    }
                    showEventsOnMap();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailing(String error) {
                Log.d(TAG, error);
            }
        };

        WebClient webClient = new WebClient();
        webClient.getEvents(webCallback);
    }
}