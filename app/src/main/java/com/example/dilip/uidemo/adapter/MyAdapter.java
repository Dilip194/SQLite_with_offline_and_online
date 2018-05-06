package com.example.dilip.uidemo.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dilip.uidemo.R;
import com.example.dilip.uidemo.utils.ItemModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyAdapterViewHolder> {

    Context context;
    List<ItemModel> list;
    boolean networkStatus;

    public MyAdapter(Context context, List<ItemModel> list, boolean status) {
        this.context = context;
        this.list = list;
        this.networkStatus = status;
    }

    public void reset(){
        list.clear();
        notifyDataSetChanged();
    }

    public void addData(ItemModel dataModel){
        list.add(dataModel);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_content, parent, false);
        return new MyAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapterViewHolder holder, int position) {
        ItemModel model = list.get(position);
        holder.setViewHolder(model);
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public class MyAdapterViewHolder extends RecyclerView.ViewHolder {

        ImageView mImage;
        TextView mSpecialization;
        TextView mDescription;

        public MyAdapterViewHolder(View itemView) {
            super(itemView);

            mImage = itemView.findViewById(R.id.image);
            mSpecialization = itemView.findViewById(R.id.sepcialization);
            mDescription = itemView.findViewById(R.id.description);
        }

        public void setViewHolder(ItemModel model) {

            if (networkStatus) {
                Picasso.with(context).load(model.getImage_path()).error(R.drawable.ic_launcher_background).into(mImage);
            } else {
                mImage.setImageBitmap(model.getImage());
            }
            mSpecialization.setText(model.getSpecialization());
            mDescription.setText(model.getDescriptions());
        }
    }
}
