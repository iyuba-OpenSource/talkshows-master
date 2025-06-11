package com.iyuba.talkshow.ui.series;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iyuba.lib_common.util.LibGlide3Util;
import com.iyuba.talkshow.R;
import com.iyuba.talkshow.TalkShowApplication;
import com.iyuba.talkshow.constant.App;
import com.iyuba.talkshow.data.model.Voa;

import java.util.List;
import java.util.Locale;


public class SeriesSimpleAdapter extends RecyclerView.Adapter<SeriesSimpleAdapter.VoaViewHolder> {

    private List <Voa> voas;
    private VoaCallback mVoaCallback;
//    private VoaListAdapter.VoaCallback mVoaCallback;


//    ItemVoa4seriesBinding voaBinding ;

    interface VoaCallback {
        void onVoaClick(Voa voa);
    }

    @NonNull
    @Override
    public VoaViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater =  LayoutInflater.from(viewGroup.getContext());
//        voaBinding = ItemVoa4seriesBinding.inflate(inflater,viewGroup,false);
        if (App.APP_ID == 280) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_voa4series, viewGroup, false);
            return new VoaViewHolder(view);
        } else {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_voa, viewGroup, false);
            return new VoaViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull VoaViewHolder holder, int position) {
        if (holder==null){
            return;
        }

        VoaViewHolder voaBinding = holder;
        Voa voa = (Voa) voas.get(position);
        if (App.APP_ID == 280) {
            /*Glide.with(holder.itemView.getContext())
                    .load(voa.pic())
                    .signature(new StringSignature(voa.pic()))
                    .bitmapTransform(new RoundedCornersTransformation(holder.itemView.getContext(), 20, 0, RoundedCornersTransformation.CornerType.ALL))
                    .error(R.drawable.pig_default)
                    .into(voaBinding.pic);*/
            LibGlide3Util.loadRoundImg(holder.itemView.getContext(),voa.pic(),R.drawable.pig_default,20,voaBinding.pic);
        } else {
            /*Glide.with(holder.itemView.getContext())
                    .load(voa.pic())
                    .signature(new StringSignature(voa.pic()))
                    .centerCrop()
                    .placeholder(R.drawable.voa_default)
                    .into(voaBinding.pic);*/
            LibGlide3Util.loadImg(holder.itemView.getContext(),voa.pic(),R.drawable.voa_default,voaBinding.pic);
        }
        if (!TextUtils.isEmpty(voa.titleCn())) {
            voaBinding.titleCn.setText(voa.titleCn());
        } else {
            voaBinding.titleCn.setText(voa.descCn());
        }
        try {
            voaBinding.time.setText(voa.createTime().split(" ")[0]);
            voaBinding.time.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            voaBinding.time.setVisibility(View.GONE);
        }
        String readCount = String.valueOf(voa.readCount());
        if (voa.readCount() > 10000) {
            readCount = String.format(Locale.CHINA, "%.2fW", voa.readCount() / 10000.0);
        }
        voaBinding.readCount.setText(readCount);
        if (App.APP_ID == 280) {
            voaBinding.tvCat.setVisibility(View.GONE);
        } else {
            String temp = getCategoryString(voa.category());
            voaBinding.tvCat.setVisibility(View.VISIBLE);
            voaBinding.tvCat.setText(temp);
        }
        voaBinding.voa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.onLeftClicked();
            }
        });

    }

    @Override
    public int getItemCount() {
        return voas == null  ? 0 : voas.size();
    }

    public void setVoas(List<Voa> voas) {
        this.voas = voas;
    }

    public class VoaViewHolder extends RecyclerView.ViewHolder {
        private View voa;
        private ImageView pic;
        private TextView titleCn;

        private LinearLayout bottomLayout;
        private TextView time;
        private TextView readCount;
        private TextView tvCat;

        public VoaViewHolder(View itemView) {
            super(itemView);

            voa = itemView.findViewById(R.id.voa);
            pic = itemView.findViewById(R.id.pic);
            titleCn = itemView.findViewById(R.id.titleCn);

            bottomLayout = itemView.findViewById(R.id.bottomLayout);
            time = itemView.findViewById(R.id.time);
            readCount = itemView.findViewById(R.id.readCount);
            tvCat = itemView.findViewById(R.id.tv_cat);

            //当包名为小猪英语时，取消时间和数量显示
            if (TalkShowApplication.getContext().getPackageName().equals("com.iyuba.talkshow.pappa")){
                bottomLayout.setVisibility(View.GONE);
            }
        }

        void onLeftClicked() {
            if (mVoaCallback != null) {
                try {
                    mVoaCallback.onVoaClick((Voa) voas.get(getAdapterPosition()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String getCategoryString(int category) {
        switch (category) {
            case 301:
                return "电影";
            case 302:
                return "美剧";
            case 304:
                return "体育";
            case 306:
                return "纪录片";
            case 307:
                return "生活";
            case 308:
                return "科教";
            case 309:
                return "动漫";
            case 310:
                return "歌曲";
            case 311:
                return "演讲";
            case 305:
                return "搞笑";
        }
        return "其他";
    }

    public void setVoaCallback(VoaCallback voaCallback) {
        this.mVoaCallback = voaCallback;
    }

    //获取当前数据
    public List<Voa> getVoas(){
        return voas;
    }
}
