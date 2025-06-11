package com.iyuba.talkshow.ui.vip.buyiyubi;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.iyuba.talkshow.R;
import com.iyuba.talkshow.databinding.ItemBuyIyubiBinding;
import com.iyuba.talkshow.injection.ActivityContext;
import com.iyuba.talkshow.ui.vip.buyvip.PriceUtils;
import com.iyuba.talkshow.ui.vip.payorder.alipay.AliPayUtil;

import java.text.MessageFormat;

import javax.inject.Inject;


public class BuyIyubiAdapter extends RecyclerView.Adapter<BuyIyubiAdapter.BuyIyubiViewHolder> {
    private static final String SUBJECT = "爱语币";

    private static final int IYUBI_TYPE = 1;
    private String[] mPrices;
    private int[] mAmounts;
    private int[] mImageIds;

    ItemBuyIyubiBinding binding ;

    private BuyIyubiCallback mCallback;

    @Inject
    public BuyIyubiAdapter(@ActivityContext Context context) {
        TypedArray ta = context.getResources().obtainTypedArray(R.array.iyubi_images);
        int length = ta.length();
        mImageIds = new int[length];
        for (int i = 0; i < length; i++)
            mImageIds[i] = ta.getResourceId(i, 0);
        ta.recycle();
        mPrices = context.getResources().getStringArray(R.array.iyubi_prices);
        mAmounts = context.getResources().getIntArray(R.array.iyubi_amounts);
    }

    public void setBuyIyubiCallback(BuyIyubiCallback cb) {
        this.mCallback = cb;
    }


    @Override
    public BuyIyubiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_buy_iyubi, parent, false);
        binding = ItemBuyIyubiBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new BuyIyubiViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(BuyIyubiViewHolder holder, int position) {
        holder.priceIv.setImageResource(mImageIds[position]);
        holder.priceTv.setText(MessageFormat.format(
                holder.itemView.getContext().getString(R.string.price), mPrices[position]));
        holder.priceBtn.setOnClickListener(v -> holder.onBtnClick());
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mPrices.length;
    }

    class BuyIyubiViewHolder extends RecyclerView.ViewHolder {
        ImageView priceIv;
        TextView priceTv;
        Button priceBtn;
        public BuyIyubiViewHolder(ItemBuyIyubiBinding itemView) {
            super(itemView.getRoot());
            priceIv = itemView.priceIv;
            priceTv = itemView.priceTv;
            priceBtn = itemView.priceBtn;
        }

        void onBtnClick() {
            int position = getPosition();
            final String outTradeNo = AliPayUtil.getOutTradeNo();
            final String body = MessageFormat.format(
                    itemView.getContext().getString(R.string.charge), mPrices[position], mAmounts[position]);
            if (mCallback != null)
                mCallback.call(outTradeNo, SUBJECT, body, mAmounts[position], mPrices[position], IYUBI_TYPE);
        }
    }

    public interface BuyIyubiCallback {
        void call(String outTradeNo, String subject, String body, int amount, String price, int proType);
    }

}
