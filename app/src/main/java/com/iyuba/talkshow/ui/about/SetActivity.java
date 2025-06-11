package com.iyuba.talkshow.ui.about;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.iyuba.talkshow.R;
import com.iyuba.talkshow.data.local.PreferencesHelper;
import com.iyuba.talkshow.databinding.ActivitySetBinding;
import com.iyuba.talkshow.ui.base.BaseViewBindingActivity;
import com.jaeger.library.StatusBarUtil;

import javax.inject.Inject;


/**
 * @author yq QQ:1032006226
 * @name talkshow
 * @class name：com.iyuba.talkshow.ui.about
 * @class describe
 * @time 2018/12/21 17:09
 * @change
 * @chang time
 * @class describe
 */
public class SetActivity extends BaseViewBindingActivity<ActivitySetBinding> {

    public static final String USE_XUNFEI = "use_xunfei";

    @Inject
    public PreferencesHelper mHelper ;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setColor(this, ResourcesCompat.getColor(getResources(), R.color.status_bar_video, getTheme()));

        activityComponent().inject(this);
        setSupportActionBar(binding.aboutToolbar.listToolbar);

        setRadioButtonState();
    }

    private void setRadioButtonState() {
        if(mHelper.loadBoolean(USE_XUNFEI,false)){
            binding.useOther.setChecked(true);
        }else {
            binding.useOther.setChecked(false);
        }
    }

//    @OnClick(R.id.use_other)
    public void onViewClicked() {
        if(mHelper.loadBoolean(USE_XUNFEI,false)){
            binding.useOther.setChecked(false);
            mHelper.putBoolean(USE_XUNFEI,false);
        }else {
            binding.useOther.setChecked(true);
            mHelper.putBoolean(USE_XUNFEI,true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return false;
        }
    }
}
