package com.example.arif.blood_donation_app_v2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

class CustomAdapter extends BaseAdapter{
    List <InfoClass> stuinfo;
    Context context;
    private LayoutInflater inflater;

    public CustomAdapter(Context c,List l) {
        this.context=c;
        this.stuinfo = l;
    }

    @Override
    public int getCount() {
        return stuinfo.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null){
            inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=inflater.inflate(R.layout.donor_list_layout,viewGroup,false);

        }

        TextView email=view.findViewById(R.id.view_donor_email);
        TextView phone=view.findViewById(R.id.view_donor_phone);
        TextView distance=view.findViewById(R.id.view_donor_distance);
        TextView state = view.findViewById(R.id.view_donor_state);
        TextView info = view.findViewById(R.id.view_donor_info);
        TextView name = view.findViewById(R.id.view_donor_name);
        TextView bloodGroup = view.findViewById(R.id.card_blood_group);
        Button call = view.findViewById(R.id.view_button_call);
        final String phoneNumber = "tel:" + stuinfo.get(i).getPhoneNumber();

        email.setText("Email :"+ stuinfo.get(i).getEmail());
        phone.setText("Phone :"+stuinfo.get(i).getPhoneNumber());
        //tv.setText(new DecimalFormat("##.##").format(i2));
        distance.setText("Distance : "+new DecimalFormat("##.##").format(stuinfo.get(i).getDistance())+"KM");
        state.setText("State :"+stuinfo.get(i).getState());
        info.setText("Donor has "+stuinfo.get(i).getDisease()+" disease , Donates "+stuinfo.get(i).getDonateFrequency()+".");
        name.setText(stuinfo.get(i).getName());
        bloodGroup.setText(stuinfo.get(i).getBloodGroup());

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(phoneNumber));
                context.startActivity(intent);
            }
        });

        return  view;
    }
}
