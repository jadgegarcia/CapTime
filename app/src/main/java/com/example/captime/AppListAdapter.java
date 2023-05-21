package com.example.captime;

import androidx.appcompat.app.AlertDialog;
import android.app.Dialog;
import android.content.pm.ApplicationInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AppListAdapter extends RecyclerView.Adapter<AppListAdapter.ViewHolder> {
    private List<ApplicationInfo> appList;
    private AppItemClickListener itemClickListener;
    private static EditText editText;
    private static AlertDialog dialog;

    private static ApplicationInfo selectedApp;

    public AppListAdapter(List<ApplicationInfo> appList, EditText editText, AlertDialog dialog) {
        this.appList = filterVisibleApps(appList);
        this.editText = editText;
        this.dialog = dialog;
    }

    public AppListAdapter(List<ApplicationInfo> appList, EditText editText) {
        this.appList = filterVisibleApps(appList);
        this.editText = editText;
    }

    private List<ApplicationInfo> filterVisibleApps(List<ApplicationInfo> appList) {
        List<ApplicationInfo> filteredList = new ArrayList<>();

        for (ApplicationInfo appInfo : appList) {
            if (appInfo.icon != 0) {
                // Exclude apps with no launcher icon (background-only apps)
                filteredList.add(appInfo);
            }
        }

        return filteredList;
    }

    private List<ApplicationInfo> filterInstalledApps(List<ApplicationInfo> appList) {
        List<ApplicationInfo> filteredList = new ArrayList<>();

        for (ApplicationInfo appInfo : appList) {
            if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                // Exclude system apps and services
                filteredList.add(appInfo);
            }
        }

        return filteredList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_app, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ApplicationInfo app = appList.get(position);
        holder.bind(app, itemClickListener);
        holder.appName.setText(app.loadLabel(holder.itemView.getContext().getPackageManager()));
        holder.appIcon.setImageDrawable(app.loadIcon(holder.itemView.getContext().getPackageManager()));

        holder.itemView.setOnClickListener(v -> {
            // Set the app name in the EditText
            String appName = app.loadLabel(holder.itemView.getContext().getPackageManager()).toString();
            editText.setText(appName);

            // Close the dialog
            //dialog.dismiss();
        });
    }

    @Override
    public int getItemCount() {
        return appList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView appIcon;
        public TextView appName;

        public ViewHolder(View itemView) {
            super(itemView);
            appIcon = itemView.findViewById(R.id.app_icon);
            appName = itemView.findViewById(R.id.app_name);
        }

        public void bind(ApplicationInfo appInfo, AppItemClickListener itemClickListener) {
            //itemView.setOnClickListener(v -> itemClickListener.onAppItemClick(appInfo));
            // Populate the appIcon and appName views
            // ...
            itemView.setOnClickListener(v -> {
                // Set the app name in the EditText
                String appName = appInfo.loadLabel(itemView.getContext().getPackageManager()).toString();
                editText.setText(appName);
                setSelectedApp(appInfo);
                // Close the dialog
                dialog.dismiss();
            });
        }
    }

    public EditText getEditText() {
        return editText;
    }

    public static void setSelectedApp(ApplicationInfo app) {
        selectedApp = app;
    }

    public ApplicationInfo getSelectedApp() {
        return selectedApp;
    }


    public interface AppItemClickListener {
        void onAppItemClick(ApplicationInfo appInfo);
    }

}



