package com.team5.foodviet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.team5.foodviet.R;
import com.team5.foodviet.adapter.ChiTietDanhMucAdapter;
import com.team5.lib.RetrofitClient;
import com.team5.lib.common.NetworkConnection;
import com.team5.lib.common.Show;
import com.team5.lib.common.Url;
import com.team5.lib.interfaceRepo.FoodVietMethods;
import com.team5.lib.model.DanhMuc;
import com.team5.lib.model.Mon;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ChiTietDanhMucActivity extends AppCompatActivity {
    Toolbar toolbar_Chitietdanhmuc;
    RecyclerView recycleView_ChiTietDanhMuc;

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    FoodVietMethods appFoodMethods;

    List<Mon.Result> listMonTheoDanhMuc;
    ChiTietDanhMucAdapter chiTietDanhMucAdapter;

    TextView thongbao_soluong;

    LinearLayoutManager linearLayoutManager;
    Handler handler = new Handler();
    boolean isLoading = false;

        int page = 1;
//    int select = 5;
    int madanhmuc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_danh_muc);

        getViewId();
        actionToolbar();
        init();

        if(NetworkConnection.isConnected(this)) {
            getDetailDanhMuc();
                actionLoading();
            Show.changeQuantitiesCart(thongbao_soluong);
        }else{
            Show.Notify(this,getString(R.string.error_network));
            finish();
        }
    }

    private void actionLoading() {
        recycleView_ChiTietDanhMuc.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(!isLoading) {
                    if(linearLayoutManager.findLastCompletelyVisibleItemPosition() == listMonTheoDanhMuc.size() -1) {
                        isLoading = true;
                        loadMore();
                    }
                }
            }
        });
    }

    private void loadMore() {
        handler.post(() -> {
            listMonTheoDanhMuc.add(null);
            chiTietDanhMucAdapter.notifyItemInserted(listMonTheoDanhMuc.size() - 1);
        });
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                listMonTheoDanhMuc.remove(listMonTheoDanhMuc.size() - 1);
                chiTietDanhMucAdapter.notifyItemRemoved(listMonTheoDanhMuc.size());
                page += 1;
                getDetailDanhMuc();
                chiTietDanhMucAdapter.notifyDataSetChanged();
                isLoading = false;
            }
        },1500);
    }

    private void init() {
        listMonTheoDanhMuc = new ArrayList<>();
        appFoodMethods = RetrofitClient.getRetrofit(Url.AppFood_Url).
                create(FoodVietMethods.class);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recycleView_ChiTietDanhMuc.setLayoutManager(layoutManager);
        recycleView_ChiTietDanhMuc.setHasFixedSize(true);
    }

    private void getDetailDanhMuc() {
        DanhMuc.Result danhmucResult = (DanhMuc.Result) getIntent().getSerializableExtra("chitietdanhmuc");
        madanhmuc = danhmucResult.getId();
        compositeDisposable.add(appFoodMethods.POST_MonTheoDanhMuc(madanhmuc)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        mon -> {
                            if (mon.isSuccess()) {
                                listMonTheoDanhMuc = mon.getResult();
                                chiTietDanhMucAdapter = new ChiTietDanhMucAdapter(this, listMonTheoDanhMuc);
                                recycleView_ChiTietDanhMuc.setAdapter(chiTietDanhMucAdapter);
                                toolbar_Chitietdanhmuc.setTitle(danhmucResult.getTendanhmuc());
                            }
                        },
                        throwable -> {
                            Show.Notify(this,getString(R.string.error_server));
                        }
                ));
    }

    private void actionToolbar() {
        setSupportActionBar(toolbar_Chitietdanhmuc);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar_Chitietdanhmuc.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getViewId() {
        toolbar_Chitietdanhmuc = findViewById(R.id.toolbar_Chitietdanhmuc);
        recycleView_ChiTietDanhMuc = findViewById(R.id.recycleView_ChiTietDanhMuc);
        thongbao_soluong = findViewById(R.id.thongbao_soluong);
    }

    public void ToHome(View view) {
        Intent trangchu = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(trangchu);
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

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}