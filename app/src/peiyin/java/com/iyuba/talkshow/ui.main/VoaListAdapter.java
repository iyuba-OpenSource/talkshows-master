package com.iyuba.talkshow.ui.main;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.iyuba.iyubamovies.model.DataBean;
import com.iyuba.lib_user.manager.UserInfoManager;
import com.iyuba.module.commonvar.CommonVars;
import com.iyuba.talkshow.R;
import com.iyuba.talkshow.constant.App;
import com.iyuba.talkshow.data.DataManager;
import com.iyuba.talkshow.data.manager.AbilityControlManager;
import com.iyuba.talkshow.data.model.AdNativeResponse;
import com.iyuba.talkshow.data.model.CategoryFooter;
import com.iyuba.talkshow.data.model.Header;
import com.iyuba.talkshow.data.model.HomeHeaderImage;
import com.iyuba.talkshow.data.model.LoopItem;
import com.iyuba.talkshow.data.model.RecyclerItem;
import com.iyuba.talkshow.data.model.SeriesData;
import com.iyuba.talkshow.data.model.Voa;
import com.iyuba.talkshow.ui.detail.DetailActivity;
import com.iyuba.talkshow.ui.list.ListActivity;
import com.iyuba.talkshow.ui.user.me.dubbing.MyDubbingActivity;
import com.iyuba.talkshow.util.glide.GlideImageLoader;
import com.youdao.sdk.nativeads.NativeResponse;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/11/10 0010.
 */

public class VoaListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private enum ITEM_TYPE {
        LUNBO, HEADER, PEIYIN, CATEGORY_FOOTER, HEADER_FUNCTION_IMAGE, AD
    }

    private static final int BANNER_POSITION = 0;

    private List<RecyclerItem> mItemList;
    private List<Voa> mVoaList;
    private List<Header> mHeaderList;
    private List<CategoryFooter> mFooterList;
    private RecyclerItem bannerHolder = new RecyclerItem() {
    };
    private HomeHeaderImage homeHeaderImage = new HomeHeaderImage();
    private List<LoopItem> mLoopItemList;
    private List<String> mImageUrls;
    private List<String> mNames;

    private VoaCallback mVoaCallback;
    private LoopCallback mLoopCallback;
    private DataChangeCallback mDataChangeCallback;
    private CourseChooseCallback mCourseCallback;
    private MobCallback mobCallback;

    @Inject
    public VoaListAdapter() {
        this.mItemList = new ArrayList<>();
        this.mImageUrls = new ArrayList<>();
        this.mNames = new ArrayList<>();
        this.mHeaderList = Header.getAllHeaders();
        this.mFooterList = Header.getALlFooters();
        mItemList.add(bannerHolder);
        mItemList.add(homeHeaderImage);
    }

    public VoaListAdapter(List<Voa> mVoaList) {
        this.mVoaList = mVoaList;
        try {
            setRecyclerItemList();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Inject
    DataManager dataManager;

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE.LUNBO.ordinal()) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.loop, parent, false);
            return new LoopViewHolder(view);
        } else if (viewType == ITEM_TYPE.HEADER.ordinal()) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.voa_header, parent, false);
            return new HeaderViewHolder(view);
        } else if (viewType == ITEM_TYPE.CATEGORY_FOOTER.ordinal()) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_category_footer, parent, false);
            return new CategoryFooterViewHolder(view);
        } else if (viewType == ITEM_TYPE.HEADER_FUNCTION_IMAGE.ordinal()) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_header_image, parent, false);
            return new HeaderImageViewHolder(view);
        } else if (viewType == ITEM_TYPE.AD.ordinal()) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_ad, parent, false);
            return new AdHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_voa, parent, false);
            return new VoaViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NotNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof VoaViewHolder) {
            VoaViewHolder peiyinHolder = (VoaViewHolder) holder;
            Voa voa = (Voa) mItemList.get(position);
            peiyinHolder.setItem(voa,position);
          
        } else if (holder instanceof HeaderViewHolder) {
            Header header = (Header) mItemList.get(position);
            HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
            //设置前边图标
            setCategoryDrawable(headerHolder, header.getPic());

//            headerHolder.name.setText(header.getName());
//            headerHolder.more.setText(header.getDesc());
            // TODO: 2022/7/11 部分商店会将"今日推荐"判断为广告，这里进行屏蔽，上边两条是原来的
            if (header.getName().equals("今日推荐")){
                headerHolder.name.setText("精彩视频");
                headerHolder.more.setText("精彩视频不要错过");
            }else {
                headerHolder.name.setText(header.getName());
                headerHolder.more.setText(header.getDesc());
            }
        } else if (holder instanceof HeaderImageViewHolder) {
            HeaderImageViewHolder headerHolder = (HeaderImageViewHolder) holder;
            if (AbilityControlManager.getInstance().isLimitMoc()){
                headerHolder.tvMoc.setVisibility(View.GONE);
            }else {
                headerHolder.tvMoc.setVisibility(View.VISIBLE);
            }
//            HeaderImageViewHolder headViewHolder = (HeaderImageViewHolder) holder;
//            headViewHolder.tvAll.setDrawable(tintDrawable(imageView.drawable, ColorStateList.valueOf(Color.argb(alpha, red, green, blue))))
//            headViewHolder.tvLocal.setImageDrawable(tintDrawable(imageView.drawable, ColorStateList.valueOf(Color.argb(alpha, red, green, blue))))
//            headViewHolder.tvRank.setImageDrawable(tintDrawable(imageView.drawable, ColorStateList.valueOf(Color.argb(alpha, red, green, blue))))
//            headViewHolder.tvSign.setImageDrawable(tintDrawable(imageView.drawable, ColorStateList.valueOf(Color.argb(alpha, red, green, blue))))
        } else if (holder instanceof CategoryFooterViewHolder) {
        } else if (holder instanceof AdHolder) {
            AdHolder adHolder = (AdHolder) holder;
            final NativeResponse nativeResponse = ((AdNativeResponse) mItemList.get(position)).getNativeResponse();
            nativeResponse.recordImpression(holder.itemView);
            holder.itemView.setOnClickListener(nativeResponse::handleClick);
            Glide.with(holder.itemView.getContext()).load(nativeResponse.getMainImageUrl())
                    .placeholder(R.drawable.voa_default)
                    .into(adHolder.imageView);
            adHolder.title.setText(nativeResponse.getTitle());
            adHolder.read.setText(new Random().nextInt(9) + "5270");

        } else if (holder instanceof LoopViewHolder) {
            LoopViewHolder loopViewHolder = (LoopViewHolder) holder;
            loopViewHolder.setBanner();
        }
    }

    private void setCategoryDrawable(HeaderViewHolder holder, int id) {
        Drawable drawable = ContextCompat.getDrawable(holder.itemView.getContext(), id);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        holder.name.setCompoundDrawables(drawable, null, null, null);
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        int type = super.getItemViewType(position);
        if (mItemList.get(position) instanceof Voa) {
            type = ITEM_TYPE.PEIYIN.ordinal();
        } else if (mItemList.get(position) instanceof Header) {
            type = ITEM_TYPE.HEADER.ordinal();
        } else if (mItemList.get(position) instanceof CategoryFooter) {
            type = ITEM_TYPE.CATEGORY_FOOTER.ordinal();
        } else if (mItemList.get(position) instanceof HomeHeaderImage) {
            type = ITEM_TYPE.HEADER_FUNCTION_IMAGE.ordinal();
        } else if (mItemList.get(position) instanceof AdNativeResponse) {
            type = ITEM_TYPE.AD.ordinal();
        } else {
            type = ITEM_TYPE.LUNBO.ordinal();
        }
        return type;
    }

    public void setVoasByCategory(final List<Voa> voas, final CategoryFooter category) {
        int pos = category.getPos() - 4;

        while (mItemList.get(pos) instanceof Voa) {
            mItemList.remove(pos);
        }
//        mItemList.remove(pos);
//        mItemList.remove(pos);
//        mItemList.remove(pos);

        mItemList.add(pos, voas.get(0));
        mItemList.add(pos, voas.get(1));
        mItemList.add(pos, voas.get(2));
        mItemList.add(pos, voas.get(3));

//        for (int i = 0; i < mItemList .size(); i++) {
//            Log.d("com.iyuba.talkshow", mItemList.get(i).getClass().getSimpleName()+"");
//        }
        notifyItemRangeChanged(pos, 4);
    }

    public void setAd(AdNativeResponse nativeResponse) {
//        int pos = Header.startIndex;
        mItemList.add(8, nativeResponse);
        Header.setStartIndex(9);
        notifyDataSetChanged();
    }

    public void removeAd() {
        for (int i = 0; i < mItemList.size(); i++) {
            if (mItemList.get(i) instanceof AdNativeResponse) {
                mItemList.remove(mItemList.get(i));
            }
        }
        notifyDataSetChanged();
    }

    public void setVoas(List<Voa> mVoaList) {
        this.mVoaList = mVoaList;

        try {
            setRecyclerItemList();
        } catch (Exception e) {
            e.printStackTrace();
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
            case 305:
                return "搞笑";
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
            case 313:
                return "新起点";
            case 314:
                return "PEP";
            case 315:
                return "精通";
            case 316:
                return "初中英语";
            case 317:
                return "三起点";
            case 318:
                return "一起点";

        }
        return "其他";
    }

    private void setRecyclerItemList() {
        mItemList.clear();
        mItemList.add(bannerHolder);  //先添加轮播图
        mItemList.add(homeHeaderImage); //四张指示图
        mItemList.add(Header.getNewsHeader());//推荐头部

        if (mVoaList == null) {
            Log.e("com.iyuba.talkshow", "setRecyclerItemList is null? ");
            return;
        } else if (mVoaList.size() < 4) {
            Log.e("com.iyuba.talkshow", "setRecyclerItemList is < 4, the mini number. ");
            return;
        }
        mItemList.add(mVoaList.get(0));
        mItemList.add(mVoaList.get(1));
        mItemList.add(mVoaList.get(2));
        mItemList.add(mVoaList.get(3));

        mItemList.add(Header.getNewsFooter());


        for (int i = 0; i < mHeaderList.size(); i++) {
            mItemList.add(mHeaderList.get(i));
            //如果有专辑在前面显示
            if (mVoaList.size() < ((i+2)*4)) {
                Log.e("com.iyuba.talkshow", "setRecyclerItemList mHeaderList.size() " + mHeaderList.size());
                Log.e("com.iyuba.talkshow", "setRecyclerItemList mVoaList.size() " + mVoaList.size());
                continue;
            }
            if (mVoaList.get((i + 1) * 4 + 3).series() != 0) {
                mItemList.add(mVoaList.get((i + 1) * 4 + 3));
                mItemList.add(mVoaList.get((i + 1) * 4));
                mItemList.add(mVoaList.get((i + 1) * 4 + 1));
                mItemList.add(mVoaList.get((i + 1) * 4 + 2));
            } else {
                mItemList.add(mVoaList.get((i + 1) * 4));
                mItemList.add(mVoaList.get((i + 1) * 4 + 1));
                mItemList.add(mVoaList.get((i + 1) * 4 + 2));
                mItemList.add(mVoaList.get((i + 1) * 4 + 3));
            }

            mItemList.add(mFooterList.get(i));
        }

        Log.e("com.iyuba.talkshow", "mItemList.size() " + mItemList.size());
//        for (int i = 0; i < mItemList.size(); i++) {
//            Timber.tag("com.iyuba.talkshow").d("%s", mItemList.get(i).getClass().getSimpleName());
//        }
    }

    public void setBanner(List<LoopItem> loopItemList) {
        this.mLoopItemList = loopItemList;
        if (loopItemList != null && loopItemList.size() > 0) {
            mImageUrls.clear();
            mNames.clear();
            for (LoopItem item : mLoopItemList) {
                mImageUrls.add(item.pic());
                mNames.add(item.name());
            }
            notifyItemChanged(BANNER_POSITION);
        }
    }

    public List<RecyclerItem> getItemList() {
        return mItemList;
    }

    /**
     * 配音内容栏
     */

    class VoaViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.voa)
        public View voa;

        @BindView(R.id.pic)
        public ImageView pic;

        @BindView(R.id.time)
        public TextView time;

        @BindView(R.id.titleCn)
        public TextView titleCn;

        @BindView(R.id.readCount)
        public TextView tvReadCount;

        @BindView(R.id.tv_cat)
        public TextView tvcat;

        public VoaViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.voa)
        void onLeftClicked() {
//            if (!mAccountManager.checkLogin()) {
//                mobCallback.onMobCheck();
//                return;
//            }
            if (mVoaCallback != null) {
                if (getAdapterPosition()<=6&&getAdapterPosition()>=0){
                    Voa voa = (Voa) mItemList.get(getAdapterPosition());
                    itemView.getContext().startActivity(DetailActivity.buildIntent(itemView.getContext(), voa, true));
                    return;
                }
                try {
                    mVoaCallback.onVoaClick((Voa) mItemList.get(getAdapterPosition()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        public void setItem(Voa voa, int position) {
            Glide.with(itemView.getContext())
                    .load(voa.pic().replace("iyuba.com", CommonVars.domain))
                    .centerCrop()
                    .placeholder(R.drawable.voa_default)
                    .into(pic);
            tvcat.setText(getCategoryString(voa.category()));
            titleCn.setText(voa.titleCn());
            tvcat.setBackgroundColor(pic.getContext().getResources().getColor(R.color.colorPrimary));
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
                readCount = String.format(Locale.CHINA, "%.2fW", voa.readCount() / 10000.0);
            }
            tvReadCount .setText(readCount);
            tvcat.setVisibility(View.VISIBLE);
            if (voa.series() != 0&& position>6) {
                dataManager.getSeriesById(voa.series() + "")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<SeriesData>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(SeriesData bean) {
//                                Glide.with(itemView.getContext())
//                                        .load(bean.getPic().replace("iyuba.com", "iyuba.cn"))
//                                        .centerCrop()
//                                        .placeholder(R.drawable.voa_default)
//                                        .into(pic);
                                tvcat.setText("专辑");
                                titleCn.setText(bean.getSeriesName());
                                tvcat.setBackgroundColor(pic.getContext().getResources().getColor(R.color.orangered));
                            }
                        });

            } else {
                Glide.with(itemView.getContext())
                        .load(voa.pic().replace("iyuba.com", CommonVars.domain))
                        .centerCrop()
                        .placeholder(R.drawable.voa_default)
                        .into(pic);
                tvcat.setText(getCategoryString(voa.category()));
                tvcat.setBackgroundColor(pic.getContext().getResources().getColor(R.color.colorPrimary));
            }
        }
    }

    /**
     * 分类视频底部栏 更多 换一批
     */
    class CategoryFooterViewHolder extends RecyclerView.ViewHolder {

        public CategoryFooterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.tv_more, R.id.img_more})
        void more() {
//            if (!mAccountManager.checkLogin()) {
//                mobCallback.onMobCheck();
//                return;
//            }
            Intent intent = new Intent(itemView.getContext(), ListActivity.class);
            intent.putExtra(ListActivity.VOA_CATEGORY, getAdapterPosition() < 10 ? 0 : getType());
            itemView.getContext().startActivity(intent);
        }

        int getType() {
            String category = ((CategoryFooter) mItemList.get(getAdapterPosition())).getCategory().split(",")[0];
            return Integer.parseInt(category);
        }

        @OnClick({R.id.tv_refresh, R.id.img_refresh})
        void refresh(final View view) {
            final int limit = 4;
//            if (!mAccountManager.checkLogin()) {
//                mobCallback.onMobCheck();
//                return;
//            }
            try {
                CategoryFooter footer = (CategoryFooter) mItemList.get(getLayoutPosition());
                footer.setPos(getLayoutPosition());
                StringBuilder builder = new StringBuilder();
                builder.append(((Voa) mItemList.get(getLayoutPosition() - 1)).voaId());
                builder.append(",");
                builder.append(((Voa) mItemList.get(getLayoutPosition() - 2)).voaId());
                builder.append(",");
                builder.append(((Voa) mItemList.get(getLayoutPosition() - 3)).voaId());
                builder.append(",");
                builder.append(((Voa) mItemList.get(getLayoutPosition() - 4)).voaId());
                mDataChangeCallback.onClick(view, footer, limit, builder.toString());
            } catch (ClassCastException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 打卡等工具栏
     */

    class HeaderImageViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_sign)
        TextView tvSign;
        @BindView(R.id.tv_course)
        TextView tvRank;
        @BindView(R.id.tv_local)
        TextView tvLocal;
        @BindView(R.id.tv_all)
        TextView tvAll;
        @BindView(R.id.tv_word)
        TextView word ;

        //微课
        @BindView(R.id.tv_mooc)
        TextView tvMoc;

        public HeaderImageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            word.setVisibility(View.GONE);
            if (App.APP_CHECK_VIVO) {
                tvRank.setVisibility(View.GONE);
            }else {
                tvRank.setVisibility(View.VISIBLE);
            }
        }

        @OnClick(R.id.tv_sign)
        void sign(View v) {
            dailyBonusOnClickListener.onClick(v);
        }

        @OnClick(R.id.tv_course)
        void course() {
//            CourseChooseActivity.start(itemView.getContext(), OpenFlag.TO_DETAIL);
            mCourseCallback.onClickCourse(1);
        }
        @OnClick(R.id.tv_mooc)
        void mooc() {
            mCourseCallback.onClickCourse(2);
        }

        @OnClick(R.id.tv_word)
        void word() {
//            mWordBookCallback.onWordCallBack();
        }

        @OnClick(R.id.tv_local)
        void local() {
            if (!UserInfoManager.getInstance().isLogin()) {
                mobCallback.onMobCheck();
                return;
            }
            itemView.getContext().startActivity(new Intent(itemView.getContext(), MyDubbingActivity.class));
        }

        @OnClick(R.id.tv_all)
        void all() {
//            if (!mAccountManager.checkLogin()) {
//                mobCallback.onMobCheck();
//                return;
//            }
            Intent intent = new Intent(itemView.getContext(), ListActivity.class);
            intent.putExtra(ListActivity.VOA_CATEGORY, 0);
            itemView.getContext().startActivity(intent);
        }
    }


    /**
     * 推荐的顶部栏
     */
    class HeaderViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.more)
        TextView more;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    /**
     * 顶部的轮播图
     */
    class LoopViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.banner)
        Banner banner;

        public LoopViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


        public void setBanner() {
            //设置banner样式
            List<DataBean> list = new ArrayList<>();
            try {
                if ((mImageUrls != null) && (mNames != null)) {
                    for (int i = 0; i < mItemList.size(); i++) {
                        list.add(new DataBean(mImageUrls.get(i), mNames.get(i), i));
                    }
                }
            } catch (Exception var) {}
//            banner.setAdapter(new ImageTitleNumAdapter(list));
//            banner.setBannerRound(12);
//            banner.setOnBannerListener(new OnBannerListener() {
//                @Override
//                public void OnBannerClick(Object data, int position) {
//                    LoopItem loopItem = mLoopItemList.get(position);
//                    Log.e("banner", "loopItem.id() " + loopItem.id());
//                    mLoopCallback.onLoopClick(loopItem.id());
//                }
//            });
            banner.setBannerStyle(BannerConfig.NUM_INDICATOR_TITLE)
                    //设置图片加载器
                    .setImageLoader(new GlideImageLoader())
                    //设置图片集合
                    .setImages(mImageUrls)
                    //设置banner动画效果
                    .setBannerAnimation(Transformer.Default)
                    //设置标题集合（当banner样式有显示title时）
                    .setBannerTitles(mNames)
                    //设置自动轮播，默认为true
                    .isAutoPlay(true)
                    //设置轮播时间
                    .setDelayTime(5000)
                    //设置指示器位置（当banner模式中有指示器时）
                    .setIndicatorGravity(BannerConfig.RIGHT)
                    .setOnBannerListener(new OnBannerListener() {
                        @Override
                        public void OnBannerClick(int position) {
                            LoopItem loopItem = mLoopItemList.get(position);
                            Log.e("banner", "loopItem.id() " + loopItem.id());
                            mLoopCallback.onLoopClick(loopItem.id());
                        }
                    });
            //banner设置方法全部调用完毕时最后调用
            banner.start();
        }

    }

    /**
     * 广告栏
     */

    class AdHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_ad)
        ImageView imageView;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.read)
        TextView read;

        public AdHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface VoaCallback {
        void onVoaClick(Voa voa);
    }

    public interface LoopCallback {
        void onLoopClick(int voaId);
    }

    public interface DataChangeCallback {
        void onClick(View v, CategoryFooter category, int limit, String ids);
    }

    public interface CourseChooseCallback{
        void onClickCourse(int item);
    }

    public void setVoaCallback(VoaCallback voaCallback) {
        this.mVoaCallback = voaCallback;
    }

    public void setLoopCallback(LoopCallback loopCallback) {
        this.mLoopCallback = loopCallback;
    }

    public void setDataChangeCallback(DataChangeCallback mDataChangeCallback) {
        this.mDataChangeCallback = mDataChangeCallback;
    }

    public void setCourseClickCallback(CourseChooseCallback courseChooseCallback) {
        this.mCourseCallback = courseChooseCallback;
    }

    private View.OnClickListener dailyBonusOnClickListener;

    public void setDailyBonusCallback(View.OnClickListener onClickListener) {
        this.dailyBonusOnClickListener = onClickListener;
    }
    public interface MobCallback {
        void onMobCheck();
    }
    public void setMobCallback(MobCallback voaCallback) {
        mobCallback = voaCallback;
    }
}