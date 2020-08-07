package com.zeelawahab.i160021_i160099;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class clistAdapter extends RecyclerView.Adapter<clistAdapter.clistVH> {
    private onClickListener monClickListener;
    private ArrayList<String> data;
    private ArrayList<String> data1;

    public clistAdapter(ArrayList<String> data,ArrayList<String> data1, onClickListener onClickListener)
    {
        this.data=data;
        this.data1=data1;
        this.monClickListener=onClickListener;
    }

    @NonNull
    @Override
    public clistVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.clist_item,parent,false);
        return new clistVH(view,monClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull clistVH holder, int position) {
        String title=data.get(position);
        String last=data1.get(position);
        holder.txtTitle.setText(title);
        holder.lastmsg.setText(last);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class clistVH extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imgIcon;
        TextView txtTitle;
        TextView lastmsg;
        onClickListener onClickListener;
        public clistVH(@NonNull View itemView,onClickListener onClickListener) {
            super(itemView);
            imgIcon= itemView.findViewById(R.id.imgIcon);
            txtTitle=itemView.findViewById(R.id.txtTitle);
            lastmsg=itemView.findViewById(R.id.lastmsg);
            this.onClickListener=onClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onClickListener.onClick(getAdapterPosition());
        }
    }

    public interface onClickListener{
        void onClick(int position);

    }
}
