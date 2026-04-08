package com.example.pecheconnect;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class HistoriqueAdapter extends RecyclerView.Adapter<HistoriqueAdapter.ViewHolder> {

    private List<SessionData> historiqueList;

    public HistoriqueAdapter(List<SessionData> list) { this.historiqueList = list; }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_historique, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SessionData item = historiqueList.get(position);

        // Formatage Date : "2026-02-02T10:15:00" -> "02/02/2026 10:15"
        String rawDate = item.date_fin;
        if (rawDate != null && rawDate.contains("T")) {
            String datePart = rawDate.split("T")[0];
            String timePart = rawDate.split("T")[1].substring(0, 5);
            String[] p = datePart.split("-");
            holder.txtDate.setText(p[2] + "/" + p[1] + "/" + p[0] + " " + timePart);
        } else {
            holder.txtDate.setText(rawDate);
        }

        holder.txtNb.setText(String.valueOf(item.total_peche));
        holder.txtType.setText(item.total_peche > 0 ? "Mise à jour automatique" : "Relevage effectué");
    }

    @Override
    public int getItemCount() { return historiqueList.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtDate, txtNb, txtType;
        public ViewHolder(View v) {
            super(v);
            txtDate = v.findViewById(R.id.txt_date_heure);
            txtNb = v.findViewById(R.id.txt_nb_item);
            txtType = v.findViewById(R.id.txt_type_event);
        }
    }
}