package com.team5.foodviet.adapter;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.team5.lib.NavigationForm;
import com.team5.foodviet.R;

public class NavigationAdapter extends ArrayAdapter<NavigationForm> {

    Activity context;
    int resource;

    public NavigationAdapter(@NonNull Context context, int resource) {
        super(context, resource);

        this.context = (Activity) context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = this.context.getLayoutInflater();
        View customView = layoutInflater.inflate(this.resource,null);

        ImageView hinhminhhoa = customView.findViewById(R.id.hinhminhhoa);
        TextView noidung = customView.findViewById(R.id.noidung);

        NavigationForm nav = getItem(position);

        hinhminhhoa.setImageResource(nav.getHinhminhhoa());
        noidung.setText(nav.getNoidung());

        return customView;
    }
}
