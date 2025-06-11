package com.iyuba.talkshow.ui.vip.buyiyubi;

import android.content.Intent;
import androidx.annotation.Nullable;
import android.os.Bundle;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.MenuItem;

import com.iyuba.talkshow.R;
import com.iyuba.talkshow.constant.App;
import com.iyuba.talkshow.databinding.ActivityBuyIyubiBinding;
import com.iyuba.talkshow.ui.base.BaseViewBindingActivity;
import com.iyuba.talkshow.ui.vip.payorder.PayOrderActivity;
import com.umeng.analytics.MobclickAgent;

import java.text.MessageFormat;

import javax.inject.Inject;


public class BuyIyubiActivity extends BaseViewBindingActivity<ActivityBuyIyubiBinding> {
    @Inject
    BuyIyubiAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(binding.buyIyubiToolbar.listToolbar);
        activityComponent().inject(this);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mAdapter.setBuyIyubiCallback(mCallback);
        binding.buyiyubiChargeLv.setAdapter(mAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.buyiyubiChargeLv.setLayoutManager(layoutManager);
        binding.buyiyubiChargeLv.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        MessageFormat.format(getString(R.string.buy_iyubi_description), "");
        MessageFormat.format(getString(R.string.buy_iyubi_description), "");
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    private BuyIyubiAdapter.BuyIyubiCallback mCallback = new BuyIyubiAdapter.BuyIyubiCallback() {

        private static final String SEP = "-";

        @Override
        public void call(String outTradeNo, String subject, String body, int amount, String price, int proType) {
            String description = MessageFormat.format(getString(R.string.buy_iyubi_description), amount);
            body = App.APP_NAME_WEIXIN + SEP + body;
            Intent intent = PayOrderActivity.buildIntent(BuyIyubiActivity.this, description,
                    price, subject, body, amount, proType,PayOrderActivity.Order_iyubi);
            startActivity(intent);
            finish();
        }
    };
}
