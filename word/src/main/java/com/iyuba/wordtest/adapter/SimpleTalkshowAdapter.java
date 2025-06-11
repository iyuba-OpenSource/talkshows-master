package com.iyuba.wordtest.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iyuba.wordtest.ui.detail.WordDetailActivity;
import com.iyuba.wordtest.databinding.WordtestSimpleWordItemBinding;
import com.iyuba.wordtest.entity.TalkShowWords;
import com.iyuba.wordtest.utils.TextAttr;

import java.util.List;


public class SimpleTalkshowAdapter extends RecyclerView.Adapter<SimpleTalkshowAdapter.ViewHolder> {

    private Context context;

    public SimpleTalkshowAdapter(List<TalkShowWords> words) {
        this.words = words;
    }

    private final List<TalkShowWords> words;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
//        View view ;
        WordtestSimpleWordItemBinding binding = WordtestSimpleWordItemBinding.inflate(LayoutInflater.from(context),viewGroup,false);
//        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.wordtest_simple_word_item, viewGroup, false);
        return new ViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int pos) {
        TalkShowWords rootWord = words.get(pos);
        viewHolder.setItem(rootWord, pos);

        viewHolder.itemView.setOnClickListener(v->{
            WordDetailActivity.start(context, words, pos, words.get(pos).book_id, words.get(pos).unit_id);
        });
    }

    @Override
    public int getItemCount() {
        return words.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {


        private final TextView talkshowword;
        private final TextView talkshowpron;
        private final TextView talkshowdef;


        public ViewHolder(WordtestSimpleWordItemBinding binding) {
            super(binding.getRoot());
            talkshowword = binding.talkshowword;
            talkshowpron = binding.talkshowpron;
            talkshowdef = binding.talkshowdef;
        }


        public void setItem(TalkShowWords rootWord, int position) {
            //取消点击图片跳转的操作
//            talkshowinfoImg.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    WordDetailActivity.start(context, words, getAdapterPosition(), words.get(position).book_id, words.get(position).unit_id, true);
//                }
//            });
            //取消点击item翻页的功能
//            parent.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    isChecked = !isChecked;
//                    setItemVisible();
//                }
//            });

//            parentView.setOnClickListener(v->{
//                WordDetailActivity.start(context, words, getAdapterPosition(), words.get(position).book_id, words.get(position).unit_id, true);
//            });
            talkshowword.setText(rootWord.word);
            if (!TextUtils.isEmpty(rootWord.pron)) {
                if (!rootWord.pron.startsWith("[")){
                    talkshowpron.setText(String.format("[%s]", TextAttr.decode(rootWord.pron)));
                }else {
                    talkshowpron.setText(String.format("%s", TextAttr.decode(rootWord.pron)));
                }
            }else {
                talkshowpron.setText("");
            }
            if (rootWord.wrong == 2){
                talkshowword.setTextColor(Color.parseColor("#FFBD1616"));
            }else if (rootWord.wrong == 1){
                talkshowword.setTextColor(Color.parseColor("#FF8BC34A"));

            }
            talkshowdef.setText(rootWord.def);
//            setItemVisible();
            itemView.setTag(position);
        }
    }
}
