package com.iyuba.talkshow.ui.lil.ui.dubbing.my.child;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iyuba.talkshow.databinding.ItemReleasedBinding;

public class MyNewDubbingShowAdapter extends RecyclerView.Adapter<MyNewDubbingShowAdapter.DubbingHolder> {

    private Context context;

    @NonNull
    @Override
    public DubbingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull DubbingHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class DubbingHolder extends RecyclerView.ViewHolder{

        private ImageView imageView;
        private TextView titleView;
        private TextView agreeView;
        private TextView timeView;
        private ImageView checkView;

        public DubbingHolder(ItemReleasedBinding binding){
            super(binding.getRoot());

            imageView = binding.imageIv;
            titleView = binding.nameTv;
            agreeView = binding.thumbTv;
            timeView = binding.timeTv;
            checkView = binding.deleteIv;
        }
    }
}
