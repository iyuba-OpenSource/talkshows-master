package com.iyuba.talkshow.ui.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.iyuba.lib_common.util.LibGlide3Util;
import com.iyuba.talkshow.R;
import com.iyuba.talkshow.constant.App;
import com.iyuba.talkshow.data.model.Voa;
import com.iyuba.talkshow.databinding.ItemEmptyViewSearchBinding;
import com.iyuba.talkshow.databinding.ItemVoa4seriesBinding;
import com.iyuba.talkshow.databinding.ItemVoaBinding;
import com.youdao.sdk.nativeads.NativeResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import javax.inject.Inject;


/**
 * Created by Administrator on 2016/11/16 0016.
 */

public class SearchListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List mVoaList;

    private VoaCallback mVoaCallback;

    public List getData() {
        return mVoaList;
    }

    public void setVoaCallback(VoaCallback voaCallback) {
        this.mVoaCallback = voaCallback;
    }

    private enum ITEM_TYPE {
        VOA, EMPTY
    }


    @Inject
    public SearchListAdapter() {
        this.mVoaList = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE.VOA.ordinal()) {
            if (App.APP_ID == 280) {
                ItemVoa4seriesBinding voaBinding = ItemVoa4seriesBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
                return new Voa4SeriesHolder(voaBinding);
            } else {
                ItemVoaBinding bindingVoa = com.iyuba.talkshow.databinding.ItemVoaBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
                return new VoaViewHolder(bindingVoa);
            }
        } else {
            ItemEmptyViewSearchBinding bindingEmpty = ItemEmptyViewSearchBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new EmptyViewHolder(bindingEmpty);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof VoaViewHolder) {
            VoaViewHolder voaHolder = (VoaViewHolder) holder;

            if (mVoaList.get(position) instanceof NativeResponse) {
                voaHolder.setItem((NativeResponse) mVoaList.get(position));
            } else {
                voaHolder.setItem((Voa) mVoaList.get(position));
            }
        } else if (holder instanceof Voa4SeriesHolder) {
            Voa4SeriesHolder voaHolder = (Voa4SeriesHolder) holder;
            if (mVoaList.get(position) instanceof NativeResponse) {
                voaHolder.setItem((NativeResponse) mVoaList.get(position));
            } else {
                voaHolder.setItem((Voa) mVoaList.get(position));
            }
        } else {
            EmptyViewHolder emptyHolder = (EmptyViewHolder) holder;
            emptyHolder.setItem();

        }
    }

    boolean isEmpty = false;

    @Override
    public int getItemCount() {
        if (mVoaList.size() == 0) {
            isEmpty = true;
            return 1;
        }
        isEmpty = false;
        return mVoaList.size();
    }

    @Override
    public int getItemViewType(int position) {
        int type;
        if (mVoaList.size() > 0) {
            type = ITEM_TYPE.VOA.ordinal();
        } else {
            type = ITEM_TYPE.EMPTY.ordinal();
        }
        return type;
    }

    public void setEmptyVoaList() {
        mVoaList.clear();
        notifyDataSetChanged();
    }

    public void addVoaList(List<Voa> voas) {
        mVoaList.addAll(voas);
        notifyDataSetChanged();
    }

    public int getSpanSize(int position) {
        return (position == mVoaList.size()) ? 2 : 1;
    }

    class VoaViewHolder extends RecyclerView.ViewHolder {

        ImageView pic;
        TextView time;
        TextView titleCn;
        TextView tvReadCount;

        public VoaViewHolder(ItemVoaBinding itemView) {
            super(itemView.getRoot());
            tvReadCount = itemView.readCount;
            pic = itemView.pic;
            titleCn = itemView.titleCn;
            time = itemView.time ;
        }

        void onLeftClicked(View view) {
            if (mVoaCallback != null) {
                mVoaCallback.onVoaClicked((Voa) mVoaList.get(getAdapterPosition()));
            }
        }

        public void setItem(NativeResponse nativeResponse) {
            Glide.with(itemView.getContext())
                    .load(nativeResponse.getMainImageUrl())
                    .centerCrop()
                    .placeholder(R.drawable.voa_default)
                    .into(pic);

            tvReadCount.setText(new Random().nextInt(9999) + "");
            time.setText("推广");
            titleCn.setText(nativeResponse.getTitle());
            nativeResponse.recordImpression(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    nativeResponse.handleClick(view);
                }
            });
        }

        public void setItem(Voa voa) {
            Glide.with(itemView.getContext())
                    .load(voa.pic())
                    .centerCrop()
                    .placeholder(R.drawable.voa_default)
                    .into(pic);

            if (voa.titleCn() != null) {
                titleCn.setText(voa.titleCn());
            }
            try {
                time.setText(voa.createTime().split(" ")[0]);
                time.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                time.setVisibility(View.GONE);
            }
            String readCount = String.valueOf(voa.readCount());
            if (voa.readCount() > 10000) {
                readCount = String.format(Locale.CHINA, "%.1fW", voa.readCount() / 10000.0);
            }
            tvReadCount.setText(readCount);
            itemView.setOnClickListener(view -> onLeftClicked(view));

        }
    }

    class Voa4SeriesHolder extends RecyclerView.ViewHolder {
        ImageView pic;
        TextView time;
        TextView titleCn;
        TextView tvReadCount;

        public Voa4SeriesHolder(ItemVoa4seriesBinding itemView) {
            super(itemView.getRoot());
            tvReadCount = itemView.readCount;
            pic = itemView.pic;
            titleCn = itemView.titleCn;
            time = itemView.time ;
        }

        void onLeftClicked(View view) {
            if (mVoaCallback != null) {
                mVoaCallback.onVoaClicked((Voa) mVoaList.get(getAdapterPosition()));
            }
        }

        public void setItem(NativeResponse nativeResponse) {
            Glide.with(itemView.getContext())
                    .load(nativeResponse.getMainImageUrl())
                    .centerCrop()
                    .placeholder(R.drawable.pig_default)
                    .into(pic);

            tvReadCount.setText(new Random().nextInt(9999) + "");
            time.setText("推广");
            titleCn.setText(nativeResponse.getTitle());
            nativeResponse.recordImpression(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    nativeResponse.handleClick(view);
                }
            });
        }

        public void setItem(Voa voa) {
            /*Glide.with(itemView.getContext())
                    .load(voa.pic())
                    .signature(new StringSignature(voa.pic()))
                    .bitmapTransform(new RoundedCornersTransformation(itemView.getContext(), 20, 0, RoundedCornersTransformation.CornerType.ALL))
                    .placeholder(R.drawable.pig_default)
                    .into(pic);*/
            LibGlide3Util.loadRoundImg(itemView.getContext(),voa.pic(),R.drawable.pig_default,20,pic);

            if (voa.titleCn() != null) {
                titleCn.setText(voa.titleCn());
            }
            try {
                time.setText(voa.createTime().split(" ")[0]);
                time.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                time.setVisibility(View.GONE);
            }
            String readCount = String.valueOf(voa.readCount());
            if (voa.readCount() > 10000) {
                readCount = String.format(Locale.CHINA, "%.1fW", voa.readCount() / 10000.0);
            }
            tvReadCount.setText(readCount);
            itemView.setOnClickListener(view -> onLeftClicked(view));
        }
    }

    class EmptyViewHolder extends RecyclerView.ViewHolder {
        ImageView emptyImageIv;
        TextView emptyTextTv;

        public EmptyViewHolder(ItemEmptyViewSearchBinding itemView) {
            super(itemView.getRoot());
            emptyImageIv = itemView.emptyImage;
            emptyTextTv = itemView.text;
        }

        public void setItem() {
            if (App.APP_ID == 280) {
                emptyImageIv.setImageResource(R.drawable.pig_no_data);
            } else {
                emptyImageIv.setImageResource(R.drawable.has_no_video);
            }
            emptyTextTv.setVisibility(View.GONE);
        }
    }

    interface VoaCallback {
        void onVoaClicked(Voa voa);
    }
}
