package com.iyuba.talkshow.ui.list;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.iyuba.lib_common.util.LibResUtil;
import com.iyuba.talkshow.R;
import com.iyuba.talkshow.data.model.Category;
import com.iyuba.talkshow.data.model.Level;
import com.iyuba.talkshow.data.model.Voa;
import com.iyuba.talkshow.databinding.ItemEmptyViewBinding;
import com.iyuba.talkshow.databinding.ItemVoaBinding;
import com.iyuba.talkshow.databinding.LayoutListSelectedBinding;
import com.iyuba.talkshow.databinding.SelectorBinding;
import com.iyuba.talkshow.ui.lil.view.recyclerView.NoScrollGridLayoutManager;
import com.iyuba.talkshow.ui.list.show.ListCategoryAdapter;
import com.iyuba.talkshow.ui.list.show.ListLevelAdapter;
import com.youdao.sdk.nativeads.NativeResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import javax.inject.Inject;


/**
 * Created by Administrator on 2016/11/16 0016.
 */

public class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static ListAdapter.ITEM_TYPE ITEM_TYPE;
    private List mVoaList;
    private List<Category> categoryList;
    private List<Level> levelList;

    private VoaCallback mVoaCallback;
    private SelectorCallBack mSelectorCallBack;

    private int categoryValue;
    private String levelValue;
    private int mItemCount;

    private final static int SELECTOR_NUM = 1;
    public final static int EMPTY_NUM = 1;
    public final static int SELECTOR_POSITION = 0;

    public List getData() {
        return mVoaList;
    }

    private enum ITEM_TYPE {
        SELECTOR, VOA, EMPTY
    }

    @Inject
    public ListAdapter() {
        this.mVoaList = new ArrayList<>();
        this.categoryList = Category.getCategories();
        this.levelList = Level.getLevels();
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == ITEM_TYPE.SELECTOR.ordinal()) {
//            SelectorBinding bindingSelector = SelectorBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
//            return new SelectorViewHolder(bindingSelector);
            LayoutListSelectedBinding bindingSelector = LayoutListSelectedBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
            return new NewSelectedViewHolder(bindingSelector);
        } else if (viewType == ITEM_TYPE.VOA.ordinal()) {
            ItemVoaBinding bindingVoa = ItemVoaBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
            return new VoaViewHolder(bindingVoa);
        } else {
            ItemEmptyViewBinding bindingEmpty = ItemEmptyViewBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
            return new EmptyViewHolder(bindingEmpty);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        /*if (holder instanceof SelectorViewHolder) {
            SelectorViewHolder viewHolder = (SelectorViewHolder) holder;
            viewHolder.setGroup();
        }*/
        if (holder instanceof NewSelectedViewHolder){
            NewSelectedViewHolder viewHolder = (NewSelectedViewHolder) holder;
            viewHolder.setGroup();
        }else if (holder instanceof VoaViewHolder) {
            VoaViewHolder voaHolder = (VoaViewHolder) holder;

            if (mVoaList.get(position - SELECTOR_NUM) instanceof NativeResponse) {
                voaHolder.setClick();
                NativeResponse nativeResponse = (NativeResponse) mVoaList.get(position - SELECTOR_NUM);
                voaHolder.setAdItem(nativeResponse);
            }else {
                Voa voa = (Voa) mVoaList.get(position - SELECTOR_NUM);
                voaHolder.setVoaItem(voa);
            }



        } else {
            EmptyViewHolder emptyHolder = (EmptyViewHolder) holder;
            emptyHolder.setItem();
        }
    }

    @Override
    public int getItemCount() {
        return mItemCount;
    }

    @Override
    public int getItemViewType(int position) {
        int type;
        if (position == SELECTOR_POSITION) {
            type = ITEM_TYPE.SELECTOR.ordinal();
        } else if (position <= mVoaList.size()) {
            type = ITEM_TYPE.VOA.ordinal();
        } else {
            type = ITEM_TYPE.EMPTY.ordinal();
        }
        return type;
    }

    public void updateItemCount() {
        mItemCount = mVoaList.size() + SELECTOR_NUM;
    }

    public void setVoaList(List<Voa> mVoaList) {
        this.mVoaList = mVoaList;
        mItemCount = mVoaList.size() + SELECTOR_NUM;
    }

    public void setEmptyVoaList() {
        mVoaList = Collections.emptyList();
        mItemCount = SELECTOR_NUM + 1;
    }

    public void addVoaList(List<Voa> voas) {
        this.mVoaList.addAll(voas);
        mItemCount = mVoaList.size();
    }

    public void setCategoryValue(int categoryValue) {
        this.categoryValue = categoryValue;
    }

    public void setLevelValue(String levelValue) {
        this.levelValue = levelValue;
    }

    public void setVoaCallback(VoaCallback mVoaCallback) {
        this.mVoaCallback = mVoaCallback;
    }

    public void setSelectorCallBack(SelectorCallBack mSelectorCallBack) {
        this.mSelectorCallBack = mSelectorCallBack;
    }

    public String getLevelValue() {
        return levelValue;
    }

    public int getCategoryValue() {
        return categoryValue;
    }

    public int getSpanSize(int position) {
        return (position == ListAdapter.SELECTOR_POSITION
                || position == (mVoaList.size() + ListAdapter.EMPTY_NUM)) ? 2 : 1;
    }

    /*class SelectorViewHolder extends RecyclerView.ViewHolder {

        private MyRadioGroup categoryGroup;
        private MyRadioGroup levelGroup;

        SelectorViewHolder(SelectorBinding binding){
            super(binding.getRoot());
            categoryGroup = binding.categoryGroup;
            levelGroup = binding.levelGroup;
        }

        void setGroup() {
            categoryGroup.setRadioButtons(categoryList);
            categoryGroup.setOnCheckedCallback(callback);

            categoryGroup.setChecked(String.valueOf(categoryValue));

            levelGroup.setRadioButtons(levelList);
            levelGroup.setOnCheckedCallback(callback);
            levelGroup.setChecked(levelValue);

            levelGroup.setOnCheckedChangeListener();
            categoryGroup.setOnCheckedChangeListener();
        }

        MyRadioGroup.OnCheckedCallback callback = new MyRadioGroup.OnCheckedCallback() {
            @Override
            public void onCheckedChanged(RadioButton radioButton) {
                getCategoryAndLevel();
                mSelectorCallBack.onSelectorClicked(categoryValue, levelValue);
            }

            @Override
            public void setButtonChecked(RadioButton radioButton) {
                radioButton.setTextColor(radioButton.getResources().getColor(R.color.WHITE));
                radioButton.setBackgroundResource(R.drawable.green_corner);
            }

            @Override
            public void setButtonUnChecked(RadioButton radioButton) {
                radioButton.setTextColor(radioButton.getResources().getColor(R.color.colorPrimary));
                radioButton.setBackgroundResource(R.drawable.transparent_corner);
            }
        };

        public void getCategoryAndLevel() {
            categoryValue = Integer.parseInt( categoryGroup.getCheckedValue().toString());
            levelValue = (String)  levelGroup.getCheckedValue();
        }
    }*/

    //新的菜单内容操作
    class NewSelectedViewHolder extends RecyclerView.ViewHolder{
        private RecyclerView categoryView;
        private RecyclerView levelView;

        public NewSelectedViewHolder(LayoutListSelectedBinding binding){
            super(binding.getRoot());

            categoryView = binding.categoryView;
            levelView = binding.levelView;
        }

        void setGroup(){
            //大类型
            ListCategoryAdapter categoryAdapter = new ListCategoryAdapter(LibResUtil.getInstance().getContext(), categoryList);
            NoScrollGridLayoutManager categoryManager = new NoScrollGridLayoutManager(LibResUtil.getInstance().getContext(), 4,false);
            categoryView.setLayoutManager(categoryManager);
            categoryView.setAdapter(categoryAdapter);
            categoryAdapter.setOnItemMenuClickListener(new ListCategoryAdapter.OnItemMenuClickListener() {
                @Override
                public void onClick(int position, Category category) {
                        categoryValue = (int) category.getValue();
                        mSelectorCallBack.onSelectorClicked(categoryValue,levelValue);
                }
            });

            //小类型
            ListLevelAdapter levelAdapter = new ListLevelAdapter(LibResUtil.getInstance().getContext(), levelList);
            NoScrollGridLayoutManager levelManager = new NoScrollGridLayoutManager(LibResUtil.getInstance().getContext(), 4,false);
            levelView.setLayoutManager(levelManager);
            levelView.setAdapter(levelAdapter);
            levelAdapter.setOnItemMenuClickListener(new ListLevelAdapter.OnItemMenuClickListener() {
                @Override
                public void onClick(int position, Level level) {
                    levelValue = (String) level.getValue();
                    mSelectorCallBack.onSelectorClicked(categoryValue,levelValue);
                }
            });

            //判断当前选中的大类型
            int categoryIndex = 0;
            for (int i = 0; i < categoryList.size(); i++) {
                int index = (int) categoryList.get(i).getValue();
                if (index == categoryValue){
                    categoryIndex = i;
                    break;
                }
            }
            categoryAdapter.setSelect(categoryIndex);

            //判断当前选中的小类型
            int levelIndex = 0;
            for (int i = 0; i < levelList.size(); i++) {
                String index = (String) levelList.get(i).getValue();
                if (index.equals(levelValue)){
                    levelIndex = i;
                    break;
                }
            }
            levelAdapter.setSelect(levelIndex);
        }
    }

    class VoaViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout voa;
        private ImageView pic;
        private TextView readCount;
        private TextView time;
        private TextView titleCn;

        public VoaViewHolder(ItemVoaBinding binding) {
            super(binding.getRoot());
            voa = binding.voa ;
            pic = binding.pic;
            readCount = binding.readCount ;
            time = binding.time ;
            titleCn = binding.titleCn;
        }

        public void setClick(){
            voa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onLeftClicked();
                }
            });
        }
        void onLeftClicked() {
            if (mVoaCallback != null) {
                try {
                    mVoaCallback.onVoaClicked((Voa) mVoaList.get(getAdapterPosition() - SELECTOR_NUM));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        public void setVoaItem(Voa voa){
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
            String readCountString = String.valueOf(voa.readCount());
            if (voa.readCount() > 10000) {
                readCountString = String.format(Locale.CHINA, "%.1fW", voa.readCount() / 10000.0);
            }
            readCount.setText(readCountString);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onLeftClicked();
                }
            });


        }

        public void setAdItem(NativeResponse nativeResponse) {
            Glide.with(itemView.getContext())
                    .load(nativeResponse.getMainImageUrl())
                    .centerCrop()
                    .placeholder(R.drawable.voa_default)
                    .into(pic);
            readCount.setText(new Random().nextInt(9999) + "");
            time.setText("推广");
            titleCn.setText(nativeResponse.getTitle());
            nativeResponse.recordImpression(itemView);
            itemView.setOnClickListener(view -> nativeResponse.handleClick(view));
            return;
        }
    }

    class EmptyViewHolder extends RecyclerView.ViewHolder {

        private ImageView emptyImage;
        private TextView emptyText;

        public EmptyViewHolder(ItemEmptyViewBinding binding) {
            super(binding.getRoot());
            emptyImage = binding.emptyImage;
            emptyText = binding.emptyText;
        }

        public void setItem() {
            emptyImage.setImageResource(R.drawable.has_no_video);
            emptyText.setVisibility(View.GONE);
        }
    }

    interface VoaCallback {
        void onVoaClicked(Voa voa);
    }

    interface SelectorCallBack {
        void onSelectorClicked(int category, String level);
    }
}
