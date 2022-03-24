package com.team5.foodviet.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.team5.foodviet.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.team5.lib.common.NetworkConnection;
import com.team5.lib.common.Show;

public class GioiThieuChungActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    Toolbar toolbar_Gioithieuchung;
    TextView thongbao_soluong;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gioi_thieu_chung);
        getViewId();
        actionToolbar();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        if (mapFragment != null){
            mapFragment.getMapAsync(this);
        }

        if (NetworkConnection.isConnected(this)) {
            Show.changeQuantitiesCart(thongbao_soluong);
        } else {
            Show.Notify(this, getString(R.string.error_network));
            finish();
        }
    }

    private void actionToolbar() {
        setSupportActionBar(toolbar_Gioithieuchung);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar_Gioithieuchung.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getViewId() {
        toolbar_Gioithieuchung = findViewById(R.id.toolbar_Gioithieuchung);
        thongbao_soluong = findViewById(R.id.thongbao_soluong);
    }

    public void openCart(View view) {
        Intent giohang = new Intent(getApplicationContext(), GioHangActivity.class);
        startActivity(giohang);
    }
    @Override
    protected void onStart() {
        super.onStart();
        Show.changeQuantitiesCart(thongbao_soluong);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Show.changeQuantitiesCart(thongbao_soluong);
    }

    public void ToHome(View view) {
        Intent trangchu = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(trangchu);
    }

    public void ToLienHe(View view) {
        Intent lienhe = new Intent(getApplicationContext(), LienHeActivity.class);
        startActivity(lienhe);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        LatLng hanoi = new LatLng(21.03236339687028, 105.85320988395314);
        mMap = googleMap;
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(hanoi,15);

        mMap.addMarker(new MarkerOptions().position(hanoi).title("Marker in Hoàn Kiếm"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(hanoi));
        mMap.moveCamera(cameraUpdate);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

}