package com.team5.foodviet.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.team5.foodviet.R;
import com.team5.lib.common.Show;

import java.text.DecimalFormat;

public class SuccessCheckoutActivity extends AppCompatActivity {
    TextView txt_tenkhachhang,txt_email,txt_sodienthoai,txt_ghichu,txt_tongtien;
    DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_checkout);
        getViewId();
        getThongTinKhachHang();
    }

    private void getThongTinKhachHang() {
        txt_tenkhachhang.setText(com.team5.foodviet.activity.ThongTinKhachHangActivity.user_name.getText().toString());
        txt_email.setText(com.team5.foodviet.activity.ThongTinKhachHangActivity.user_email.getText().toString());
        txt_sodienthoai.setText(com.team5.foodviet.activity.ThongTinKhachHangActivity.user_phone.getText().toString());
        txt_ghichu.setText(com.team5.foodviet.activity.ThongTinKhachHangActivity.user_note.getText().toString());
        long thanhtien = Show.countTotalMoney();
        txt_tongtien.setText(decimalFormat.format(thanhtien)+" đ");
        Show.listGiohang.clear();
    }

    private void getViewId() {
        txt_tenkhachhang = findViewById(R.id.txt_tenkhachhang);
        txt_email = findViewById(R.id.txt_email);
        txt_sodienthoai = findViewById(R.id.txt_sodienthoai);
        txt_ghichu = findViewById(R.id.txt_ghichu);
        txt_tongtien = findViewById(R.id.txt_tongtien);
    }

    public void ToHome(View view) {
        Intent trangchu = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(trangchu);
    }
}