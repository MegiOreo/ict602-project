//// RecentSearchAdapter.java
//
//package com.example.groupprojectict602;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.util.List;
//
//public class RecentSearchAdapter extends RecyclerView.Adapter<RecentSearchAdapter.ViewHolder> {
//
//    private Context context;
//    private List<String> recentSearches;
//
//    public RecentSearchAdapter(Context context, List<String> recentSearches) {
//        this.context = context;
//        this.recentSearches = recentSearches;
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.recent_search_item, parent, false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        String searchQuery = recentSearches.get(position);
//        holder.searchTextView.setText(searchQuery);
//    }
//
//    @Override
//    public int getItemCount() {
//        return recentSearches.size();
//    }
//
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//        TextView searchTextView;
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            searchTextView = itemView.findViewById(R.id.searchTextView);
//        }
//    }
//}
