package com.team5.lib.common;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.team5.lib.model.GioHang;

import java.util.List;

public class Show {
    public static void Notify(Context context, String notify) {
        Toast.makeText(context, notify, Toast.LENGTH_SHORT).show();
    }

    public static List<GioHang> listGiohang;

    public static int countQuantitiesCart(int Options) {
        int count = 0;
        if (Show.listGiohang != null) {
            for (int i = 0; i < Show.listGiohang.size(); i++) {
                count += Show.listGiohang.get(i).getSoluong();
            }
            switch (Options) {
                case 1:
                    count = count < 999 ? count : (count > 999 ? 1000 : 999);
                    return count;
                case 2:
                    return count;
                default:
                    return 0;
            }
        }
        return 0;
    }

    public static void changeQuantitiesCart(TextView view) {
        int check = Show.countQuantitiesCart(1);
        if (check > 999) {
            view.setText("999+");
        } else if (check <= 0) {
            view.setVisibility(View.GONE);
        } else {
            view.setText(String.valueOf(countQuantitiesCart(1)));
            view.setVisibility(View.VISIBLE);
        }
    }

    public static int countTotalMoney() {
        int totalPrice = 0;
        if (listGiohang.size() > 0) {
            for (int i = 0; i < Show.listGiohang.size(); i++) {
                totalPrice += Show.listGiohang.get(i).getGia() * Show.listGiohang.get(i).getSoluong();
            }
        }
        return totalPrice;
    }
}
