package com.zeelawahab.i160021_i160099;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class msgAdapter extends RecyclerView.Adapter<msgAdapter.clistVH> {
    private onClickListener monClickListener;
    private ArrayList<String> data;

    public msgAdapter(ArrayList<String> data, onClickListener onClickListener)
    {
        this.data=data;
        this.monClickListener=onClickListener;
    }

    @NonNull
    @Override
    public clistVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.msg_item,parent,false);
        return new clistVH(view,monClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull clistVH holder, int position) {
        String title=data.get(position);
        if (title.contains("~")){
            String url=title.substring(7);
            //String url="https://firebasestorage.googleapis.com/v0/b/i160021i160099.appspot.com/o/images%2Fe06c7e30-431b-4003-82d3-c6b3b2661846?alt=media&token=2f08c262-0ef5-4ec9-98c9-4885f6211a9b";
            Picasso.get().load(url).into(holder.imgIcon);
            //Glide.with(getActivity().getApplicationContext()).load(url).into(holder.imgIcon);
            holder.txtTitle.setText("Image");
            Log.d("H",url);
        }
        else{
            holder.txtTitle.setText(title);
        }


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class clistVH extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imgIcon;
        TextView txtTitle;
        onClickListener onClickListener;
        public clistVH(@NonNull View itemView,onClickListener onClickListener) {
            super(itemView);
            imgIcon= itemView.findViewById(R.id.imgIcon);
            txtTitle=itemView.findViewById(R.id.txtTitle);
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
