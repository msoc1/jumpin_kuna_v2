package com.fixed4fun.jumpingkunav2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RankingAdapter extends RecyclerView.Adapter<RankingAdapter.RankingViewHolder> {

    private ArrayList<Score> listOfScores;
    private LayoutInflater mInflater;

    public RankingAdapter(Context context, ArrayList<Score> list) {
        this.mInflater = LayoutInflater.from(context);
        this.listOfScores = list;
    }

    @NonNull
    @Override
    public RankingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.ranking_item_layout, parent, false);
        return new RankingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RankingAdapter.RankingViewHolder holder, int position) {


        Score score = listOfScores.get(position);
        holder.indexRanking.setText(String.valueOf(position + 1));
        holder.pointsRanking.setText(String.valueOf(score.getS()));
        holder.usernameRanking.setText(score.getName());
        long time = score.getT();



//        if (t / 1000 < 60) {
            String toSet = time / 1000 + ":" + time % 1000 + "s";
            holder.timeRanking.setText(toSet);
//        }
//        } else {
//            String toSet = t / 60000 + ":" + (t % 60000) / 1000 + ":" + t % 100 + "s";
//            holder.timeRanking.setText(toSet);
//        }
    }

    @Override
    public int getItemCount() {
        return listOfScores.size();
    }


    public class RankingViewHolder extends RecyclerView.ViewHolder {

        TextView indexRanking;
        TextView usernameRanking;
        TextView pointsRanking;
        TextView timeRanking;

        public RankingViewHolder(@NonNull View itemView) {
            super(itemView);
            indexRanking = itemView.findViewById(R.id.index_ranking);
            usernameRanking = itemView.findViewById(R.id.name_ranking);
            pointsRanking = itemView.findViewById(R.id.points_ranking);
            timeRanking = itemView.findViewById(R.id.time_ranking);
        }
    }

}
