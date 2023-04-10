package com.example.MyBTL.Adapter;

import static androidx.core.app.ActivityCompat.startActivityForResult;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.MyBTL.R;
import com.example.MyBTL.ThemSanPham;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;

public class UploadRecyclerAdaptor extends RecyclerView.Adapter<UploadRecyclerAdaptor.ViewHolder>{

    private ArrayList<Uri> uriArrayList;
    Context context;

    public UploadRecyclerAdaptor(Context context, ArrayList<Uri> uriArrayList) {
        this.uriArrayList = uriArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public UploadRecyclerAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view =inflater.inflate(R.layout.custom_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UploadRecyclerAdaptor.ViewHolder holder, int position) {
        holder.imageView.setImageURI((Uri) uriArrayList.get(position));
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ThemSanPham) context).launch_func(holder.imageView);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uriArrayList.remove(uriArrayList.get(position));
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, getItemCount());
            }
        });
        holder.zoomImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.custom_dialog_zoom);
                TextView textView = dialog.findViewById(R.id.text_dialog);
                PhotoView photoView = dialog.findViewById(R.id.image_view_dialog);
                Button buttonClose = dialog.findViewById(R.id.btnCloseZoom);
                textView.setText("Image " + position);
                photoView.setImageURI(uriArrayList.get(position));
                buttonClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return uriArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageView delete;
        ImageView zoomImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imageView);
            delete = itemView.findViewById(R.id.imageViewDelete);
            zoomImage = itemView.findViewById(R.id.imageViewZoom);
        }
    }

}
