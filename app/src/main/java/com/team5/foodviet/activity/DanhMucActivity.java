package com.team5.foodviet.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.team5.foodviet.R;
import com.team5.foodviet.adapter.DanhMucAdapter;
import com.team5.lib.RetrofitClient;
import com.team5.lib.common.NetworkConnection;
import com.team5.lib.common.Show;
import com.team5.lib.common.Url;
import com.team5.lib.interfaceRepo.FoodVietMethods;
import com.team5.lib.model.DanhMuc;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DanhMucActivity extends AppCompatActivity {
    Toolbar toolbar_Danhmuc;
    RecyclerView recycleView_DanhMuc;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    FoodVietMethods appFoodMethods;
    List<DanhMuc.Result> listDanhMucResult;
    DanhMucAdapter danhMucAdapter;
    TextView thongbao_soluong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_muc);

        getViewId();
        actionToolbar();
        init();

        if(NetworkConnection.isConnected(this)) {
            Show.Notify(this,"Internet Connected");
            getDanhMuc();
            Show.changeQuantitiesCart(thongbao_soluong);
        }else{
            Show.Notify(this,getString(R.string.error_network));
            finish();
        }
    }

    private void actionToolbar() {
        setSupportActionBar(toolbar_Danhmuc);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar_Danhmuc.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void init() {
        listDanhMucResult = new ArrayList<>();
        appFoodMethods = RetrofitClient.getRetrofit(Url.AppFood_Url).create(FoodVietMethods.class);
        //set layout 2 cột
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,2);
        recycleView_DanhMuc.setLayoutManager(layoutManager);
        recycleView_DanhMuc.setHasFixedSize(true);
    }

    private void getDanhMuc() {
        compositeDisposable.add(appFoodMethods.GET_DanhMuc()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        danhMuc -> {
                            if(danhMuc.isSuccess()) {
                                listDanhMucResult = danhMuc.getResult();
                                danhMucAdapter = new DanhMucAdapter(this,listDanhMucResult);
                                recycleView_DanhMuc.setAdapter(danhMucAdapter);
                            }
                        },
                        throwable -> {
                            Show.Notify(this,getString(R.string.error_server));
                        }
                ));
    }

    public void ToHome(View view) {
        Intent trangchu = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(trangchu);
    }

    public void openCart(View view) {
        Intent giohang = new Intent(getApplicationContext(), GioHangActivity.class);
        startActivity(giohang);
    }

    private void getViewId() {
        toolbar_Danhmuc = findViewById(R.id.toolbar_Danhmuc);
        recycleView_DanhMuc = findViewById(R.id.recycleView_DanhMuc);
        thongbao_soluong = findViewById(R.id.thongbao_soluong);
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


    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}