package xyz.rty813.gfremind;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

public class MainAdapter extends RecyclerView.Adapter{

    private ArrayList<Map<String, String>> list;
    private Context context;

    public MainAdapter(Context context, ArrayList<Map<String, String>> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recyclerview_main, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        MyViewHolder holder = (MyViewHolder) viewHolder;
        holder.getTvContent().setText(list.get(i).get("content"));
        holder.getTvTitle().setText(list.get(i).get("title"));
        holder.getTvTime().setText(list.get(i).get("time"));
        PackageManager pm = context.getPackageManager();
        try {
            Drawable appIcon = pm.getApplicationIcon(list.get(i).get("package"));
            holder.getIvApp().setImageDrawable(appIcon);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        holder.itemView.setTag(i);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;
        private TextView tvContent;
        private ImageView ivApp;
        private TextView tvTime;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvContent = itemView.findViewById(R.id.tvContent);
            ivApp = itemView.findViewById(R.id.ivApp);
        }

        public TextView getTvTitle() {
            return tvTitle;
        }

        public TextView getTvContent() {
            return tvContent;
        }

        public ImageView getIvApp() {
            return ivApp;
        }

        public TextView getTvTime() {
            return tvTime;
        }
    }
}
