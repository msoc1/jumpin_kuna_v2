package com.fixed4fun.jumpingkunav2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.FriendsViewHolder> {

    private ArrayList<String> listOfFriends;
    private LayoutInflater mInflater;
    private OnItemClickListener mListener;


    public interface OnItemClickListener {
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public FriendsAdapter(Context context, ArrayList<String> friendsList) {
        this.listOfFriends = friendsList;
        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public FriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.friend_item_list, parent, false);
        return new FriendsViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsAdapter.FriendsViewHolder holder, int position) {
        String friend = listOfFriends.get(position);
        holder.friendName.setText(friend);

    }

    @Override
    public int getItemCount() {
        return listOfFriends.size();
    }


    public class FriendsViewHolder extends RecyclerView.ViewHolder {

        TextView friendName;
        TextView friendDelete;

        public FriendsViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            friendName = itemView.findViewById(R.id.friend_name);
            friendDelete = itemView.findViewById(R.id.friend_delete);

            friendDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });


        }
    }
}
