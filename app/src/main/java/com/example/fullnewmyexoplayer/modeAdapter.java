package com.example.fullnewmyexoplayer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class modeAdapter extends RecyclerView.Adapter<modeAdapter.CustomView> {
    private Context context;
    private List<model> values;
    private OnItemClickListener listener;


    public modeAdapter(Context context, List<model> values) {
        this.context = context;
        this.values = values;
    }

    @NonNull
    @Override
    public CustomView onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reyclcer, parent, false);
        return new CustomView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomView holder, int position) {
        final model item=values.get(position);
        holder.txtTitle.setText(item.getTitle());
        holder.imgDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null) {
                    listener.onDownloadClick(item);
                }
            }
        });

        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.onModelClick(item);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
            return values.size();
    }

    public class CustomView extends RecyclerView.ViewHolder {

        private View rootView;
        private TextView txtTitle;
        private ImageView imgDownload;
        public CustomView(@NonNull View itemView) {
            super(itemView);
            rootView=itemView;
            txtTitle=itemView.findViewById(R.id.txtTitle);
            imgDownload=itemView.findViewById(R.id.imgDownload);

        }
    }

    public interface OnItemClickListener{

        void onDownloadClick(model item);
        void onModelClick(model item);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener=listener;
    }
}
