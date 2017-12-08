package com.konye.lande;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by KONYE on 10/9/2017.
 */

public class ApplicationsAdapter extends ArrayAdapter<AppVerClass> {
    ArrayList<AppVerClass> appVerClasses;
    public ApplicationsAdapter(Context context, int resources, ArrayList<AppVerClass> appVerClasses){
        super(context, resources, appVerClasses);
        this.appVerClasses = appVerClasses;
    }

    public View getView(int position, View convertView, ViewGroup container){
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.application_list_view_style,null);
        }
        AppVerClass appVerClass = getItem(position);
        TextView nameTextView = (TextView) convertView.findViewById(R.id.ind_org_textView);
        ImageView checkVerificationView = (ImageView) convertView.findViewById(R.id.check_imgView);
        Button viewStatusButton = (Button) convertView.findViewById(R.id.view_status_button);
        nameTextView.setText(appVerClass.getNameTextView());
        checkVerificationView.setImageResource(appVerClass.getVerificationStatus());
        viewStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do what you want here
            }
        });

        return convertView;
    }

}
