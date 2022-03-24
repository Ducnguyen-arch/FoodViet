package com.team5.foodviet.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.team5.foodviet.R;
import com.team5.foodviet.adapter.GioHangAdapter;
import com.team5.lib.common.NetworkConnection;
import com.team5.lib.common.Show;
import com.team5.lib.model.EventBus.ActionEvent;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;

;

public class GioHangActivity extends AppCompatActivity {
    Toolbar toolbar_Giohang;
    RecyclerView recycleView_Giohang;
    TextView textview_tongtien,btn_tieptucmua,message_order;
    Button btn_thanhtoan;
    FrameLayout frame_giohangtrong;

    GioHangAdapter gioHangAdapter;
    DecimalFormat decimalFormat = new DecimalFormat("###,###,###");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gio_hang);

        getViewId();
        actionToolbar();
        init();
        if(NetworkConnection.isConnected(this)) {
            getGiohang();
            countTotalMoneyCart();
            countToolBarCart();
        }else{
            Show.Notify(this,getString(R.string.error_network));
            finish();
        }
    }

    private void countTotalMoneyCart() {
        textview_tongtien.setText(decimalFormat.format(Show.countTotalMoney())+" đ");
    }

    private void init() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recycleView_Giohang.setHasFixedSize(true);
        recycleView_Giohang.setLayoutManager(layoutManager);
    }

    private void getGiohang() {
        if(Show.listGiohang.size() == 0) {
            frame_giohangtrong.setVisibility(View.VISIBLE);
        }else{
            frame_giohangtrong.setVisibility(View.INVISIBLE);
            gioHangAdapter = new GioHangAdapter(getApplicationContext(),Show.listGiohang);
            recycleView_Giohang.setAdapter(gioHangAdapter);
        }
    }

    public void countToolBarCart() {
        int show = Show.countQuantitiesCart(2);
        if(show > 0) {
            toolbar_Giohang.setTitle(getString(R.string.cart)+" (" + show +")");
            btn_tieptucmua.setText(getString(R.string.continue_buy));
        } else {
            toolbar_Giohang.setTitle(getString(R.string.cart));
            btn_tieptucmua.setText(getString(R.string.buy));
        }

    }

    private void actionToolbar() {
        setSupportActionBar(toolbar_Giohang);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar_Giohang.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getViewId() {
        toolbar_Giohang = findViewById(R.id.toolbarGiohang);
        recycleView_Giohang = findViewById(R.id.recycleView_Giohang);
        textview_tongtien = findViewById(R.id.textview_tongtien);
        btn_thanhtoan = findViewById(R.id.btn_thanhtoan);
        frame_giohangtrong = findViewById(R.id.frame_giohangtrong);
        btn_tieptucmua = findViewById(R.id.btn_tieptucmua);
        message_order = findViewById(R.id.message_order);
    }

    public void ToHome(View view) {
        Intent trangchu = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(trangchu);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //đăng kí sự kiện
        EventBus.getDefault().register(this);
        getGiohang();
        countToolBarCart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getGiohang();
        countToolBarCart();
        countTotalMoneyCart();

    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    //nhận sự kiện
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void getEvent(ActionEvent event) {
        if(event != null ) {
//            gioHangAdapter.notifyDataSetChanged();
            getGiohang();
            countTotalMoneyCart();
            countToolBarCart();
        }
    }

    public void checkOut(View view) {
        if(Show.listGiohang.size() <= 0) {
            message_order.setVisibility(View.VISIBLE);
            message_order.setText(getString(R.string.empty_cart));
        }else {
            Intent thanhtoan = new Intent(getApplicationContext(), ThongTinKhachHangActivity.class);
            startActivity(thanhtoan);
            message_order.setVisibility(View.GONE);
            message_order.setText(" ");
        }
    }

    public void continueBuy(View view) {
        Intent danhmuc = new Intent(getApplicationContext(),DanhMucActivity.class);
        startActivity(danhmuc);
    }
}