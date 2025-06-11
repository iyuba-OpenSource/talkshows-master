package com.iyuba.talkshow.ui.lil.ui.dubbing.my.release;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.iyuba.lib_common.model.remote.bean.Dubbing_publish_release;
import com.iyuba.talkshow.databinding.ItemReleasedBinding;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyNewReleaseAdapter extends RecyclerView.Adapter<MyNewReleaseAdapter.ReleaseHolder> {

    private Context context;
    private List<Dubbing_publish_release.Data> list;

    //保存选中的数据
    private Map<Integer,Dubbing_publish_release.Data> selectedMap;

    public MyNewReleaseAdapter(Context context, List<Dubbing_publish_release.Data> list){
        this.context = context;
        this.list = list;

        this.selectedMap = new HashMap<>();
    }

    @NonNull
    @Override
    public ReleaseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemReleasedBinding binding = ItemReleasedBinding.inflate(LayoutInflater.from(context),parent,false);
        return new ReleaseHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ReleaseHolder holder, int position) {
        if (holder==null){
            return;
        }

        Dubbing_publish_release.Data showBean = list.get(position);
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    class ReleaseHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView titleView;
        private TextView agreeView;
        private TextView timeView;
        private ImageView checkView;

        public ReleaseHolder(ItemReleasedBinding binding){
            super(binding.getRoot());

            imageView = binding.imageIv;
            titleView = binding.nameTv;
            agreeView = binding.thumbTv;
            timeView = binding.timeTv;
            checkView = binding.deleteIv;
        }
    }
}
