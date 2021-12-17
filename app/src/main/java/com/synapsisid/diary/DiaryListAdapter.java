package com.synapsisid.diary;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.synapsisid.diary.database.DiaryTable;
import com.synapsisid.diary.databinding.DiaryListLayoutBinding;

import java.util.ArrayList;
import java.util.List;

public class DiaryListAdapter extends RecyclerView.Adapter<DiaryListAdapter.CustomVH> {

    private List<DiaryTable> listData = new ArrayList<>();
    private Context context;

    public DiaryListAdapter(Context context) {
        this.context = context;

    }

    public class CustomVH extends RecyclerView.ViewHolder {
        public ImageView iv;
        public TextView tvTitle;
        public CardView card;
        public CustomVH(DiaryListLayoutBinding binding){
            super(binding.getRoot());
            iv =  binding.iv;
            tvTitle = binding.tvTitle;
            card = binding.cardDiary;
        }


    }

    public void setNewData(List<DiaryTable> newData){
        listData.clear();
        listData.addAll(newData);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DiaryListAdapter.CustomVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomVH(DiaryListLayoutBinding.inflate(LayoutInflater.from(context),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull DiaryListAdapter.CustomVH holder, int position) {

        holder.tvTitle.setText(listData.get(position).getJudul());
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentDiary = new Intent(context,DiaryActivity.class);
                intentDiary.putExtra(DiaryActivity.DIARY_ID_KEY,listData.get(holder.getAdapterPosition()).getId());
                context.startActivity(intentDiary);

            }
        });

        if(position%2 == 0){
            Glide.with(context).load("https://a.cdn-hotels.com/gdcs/production181/d401/8351bef0-ef7f-4979-b020-21621c88cdc0.jpg?impolicy=fcrop&w=800&h=533&q=medium")
                    .into(holder.iv);
        }else if(position%3 == 0){
            Glide.with(context).load("https://img.theculturetrip.com/x/smart/wp-content/uploads/2019/04/greece--anastasios71-shutterstock-e1455616908301.jpg")
                    .into(holder.iv);
        }else{
            Glide.with(context).load("https://www.state.gov/wp-content/uploads/2018/11/Norway-2560x1328.jpg")
                    .into(holder.iv);
        }
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }


}
