package com.team5.foodviet.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;
import com.team5.foodviet.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;

import com.team5.foodviet.adapter.MonNgauNhienAdapter;
import com.team5.foodviet.adapter.NavigationAdapter;
import com.team5.lib.NavigationForm;
import com.team5.lib.RetrofitClient;
import com.team5.lib.common.NetworkConnection;
import com.team5.lib.common.Show;
import com.team5.lib.common.Url;
import com.team5.lib.interfaceRepo.FoodVietMethods;
import com.team5.lib.model.Mon;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar_Home;
    ViewFlipper viewFlipper;
    RecyclerView recycleView_MonNgauNhien;
    NavigationView navigationView;
    ListView listView_NavHome;
    DrawerLayout drawerLayout;
    NavigationAdapter navAdapter;
    TextView thongbao_soluong;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    FoodVietMethods appFoodMethods;
    List<Mon.Result> listMonNgauNhienResult;
    MonNgauNhienAdapter monNgauNhienAdapter;
    boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getViewId();
        actionToolbar();
        init();
        setNav();

        //check network
        if(NetworkConnection.isConnected(this)) {
            Slider();
            getRandomFoods();
            Show.changeQuantitiesCart(thongbao_soluong);
            changePages();
        }else{
            Show.Notify(this,getString(R.string.error_network));
            finish();
        }
    }

    private void setNav() {
        //list tùy chọn nav
        navAdapter = new NavigationAdapter(MainActivity.this,R.layout.item_list_nav);
        listView_NavHome.setAdapter(navAdapter);
        navAdapter.add(new NavigationForm(R.drawable.ic_menu_res,getString(R.string.menu)));
        navAdapter.add(new NavigationForm(R.drawable.ic_info,getString(R.string.introduce)));
        navAdapter.add(new NavigationForm(R.drawable.ic_contact,getString(R.string.contact)));
    }

    private void changePages() {
        listView_NavHome.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                drawerLayout.closeDrawer(GravityCompat.START);
                switch (i) {
                    case 0:
                        Intent danhmuc = new Intent(getApplicationContext(),DanhMucActivity.class);
                        startActivity(danhmuc);
                        break;
                    case 1:
                        Intent gioithieuchung = new Intent(getApplicationContext(), GioiThieuChungActivity.class);
                        startActivity(gioithieuchung);
                        break;
                    case 2:
                        Intent lienhe = new Intent(getApplicationContext(), LienHeActivity.class);
                        startActivity(lienhe);
                        break;
                }
            }
        });
    }

    private void getRandomFoods() {
        compositeDisposable.add(appFoodMethods.GET_MonNgauNhien()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        mon -> {
                            if(mon.isSuccess()) {
                                listMonNgauNhienResult = mon.getResult();
                                monNgauNhienAdapter = new MonNgauNhienAdapter(this,listMonNgauNhienResult);
                                recycleView_MonNgauNhien.setAdapter(monNgauNhienAdapter);
                            }
                        },
                        throwable -> {
                            Show.Notify(this,"Không thể kết nối với Server!");
                        }
                ));
    }

    private void Slider() {
        List<String> slider = new ArrayList<>();
        slider.add(getString(R.string.slide_1));
        slider.add(getString(R.string.slide_2));
        slider.add(getString(R.string.slide_3));
        slider.add(getString(R.string.slide_4));
        for (int i = 0; i< slider.size();i++) {
            ImageView imageView = new ImageView(getApplicationContext());
            Glide.with(getApplicationContext()).load(slider.get(i)).into(imageView);

            //fix imageView vào ViewFlipper
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);
        }
        viewFlipper.setFlipInterval(5000);
        viewFlipper.setAutoStart(true);
        Animation animation_slide_step_1 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slider_step_1);
        Animation animation_slide_step_2 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slider_step_2);
        viewFlipper.setInAnimation(animation_slide_step_1);
        viewFlipper.setOutAnimation(animation_slide_step_2);
    }

    private void actionToolbar() {
        setSupportActionBar(toolbar_Home);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar_Home.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    private void getViewId() {
        toolbar_Home = findViewById(R.id.toolbar_Home);
        viewFlipper = findViewById(R.id.viewFlipper);
        recycleView_MonNgauNhien = findViewById(R.id.recycleView_MonNgauNhien);
        navigationView = findViewById(R.id.navigationView);
        listView_NavHome = findViewById(R.id.listView_NavHome);
        drawerLayout = findViewById(R.id.drawerLayout);
        thongbao_soluong = findViewById(R.id.thongbao_soluong);

    }

    private void init() {
        listMonNgauNhienResult = new ArrayList<>();
        appFoodMethods = RetrofitClient.getRetrofit(Url.AppFood_Url).create(FoodVietMethods.class);

        //set layout 2 cột
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,2);
        recycleView_MonNgauNhien.setLayoutManager(layoutManager);
        recycleView_MonNgauNhien.setHasFixedSize(true);

        if(Show.listGiohang == null) {
            Show.listGiohang = new ArrayList<>();
        }
    }

    public void openCart(View view) {
        Intent giohang = new Intent(getApplicationContext(), GioHangActivity.class);
        startActivity(giohang);
    }

    public void ToHome(View view) {
        Intent trangchu = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(trangchu);
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