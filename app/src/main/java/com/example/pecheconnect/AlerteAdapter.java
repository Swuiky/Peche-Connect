package com.example.pecheconnect;

import android.content.Context;
import android.view.*;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

public class AlerteAdapter extends ArrayAdapter<AlerteItem> {

    public AlerteAdapter(Context context, List<AlerteItem> items) {
        super(context, android.R.layout.simple_list_item_2, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(android.R.layout.simple_list_item_2, parent, false);
        }
        AlerteItem item = getItem(position);
        TextView text1 = convertView.findViewById(android.R.id.text1);
        TextView text2 = convertView.findViewById(android.R.id.text2);

        text1.setText("🚨 Casier #" + item.id_casier);
        text2.setText("Détecté le : " + item.date_mesure);
        text1.setTextColor(0xFFE53935); // rouge
        return convertView;
    }
}