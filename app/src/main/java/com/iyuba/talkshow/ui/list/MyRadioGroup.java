package com.iyuba.talkshow.ui.list;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.iyuba.talkshow.data.model.BasicNameValue;

import java.util.List;

/**
 * Created by Administrator on 2016/11/19 0019.
 */

public class MyRadioGroup extends LinearLayout implements View.OnClickListener {
    private OnCheckedCallback callback;

    private Object value;

    public MyRadioGroup(Context context) {
        this(context, null);
    }

    public MyRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setOnCheckedChangeListener() {
        for (int i = 0; i < getChildCount(); i++) {
            RadioGroup group = (RadioGroup) getChildAt(i);
            for (int j = 0; j < group.getChildCount(); j++) {
                group.getChildAt(j).setOnClickListener(this);
            }
        }
    }

    public void setRadioButtons(List<? extends BasicNameValue> list) {
        if (list != null) {
            int index = 0;
            for (int i = 0; i < getChildCount(); i++) {

                RadioGroup group = (RadioGroup) getChildAt(i);
                try {
                    for (int j = 0; (j < group.getChildCount()) && (index < list.size()); j++) {
                        BasicNameValue basicNameValue = list.get(index++);
//                    if (! (group.getChildAt(i) instanceof  RadioButton)){
//                        return;
//                    }
                        RadioButton radioButton = (RadioButton) group.getChildAt(j);
                        radioButton.setText(basicNameValue.getName());
                        radioButton.setTag(basicNameValue);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }
    }

    public Object getCheckedValue() {
        return value;
    }

    public void setChecked(String value) {
        for (int i = 0; i < getChildCount(); i++) {
            RadioGroup group = (RadioGroup) getChildAt(i);
            group.clearCheck();
            for (int j = 0; j < group.getChildCount(); j++) {
//                if (! (group.getChildAt(i) instanceof  RadioButton)){
//                    return;
//                }
                RadioButton radioButton = (RadioButton) group.getChildAt(j);

                BasicNameValue basicNameValue = (BasicNameValue) radioButton.getTag();
                try {
                    if ((value != null) && (basicNameValue != null) && value.equals(basicNameValue.getValue().toString())) {
                        this.value = value;
                        radioButton.setChecked(true);
                        if(callback != null) {
                            callback.setButtonChecked(radioButton);
                            setButtonUnChecked(radioButton);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setOnCheckedCallback(OnCheckedCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onClick(View view) {
        if (callback != null) {
            RadioButton radioButton = (RadioButton) view;
            value = ((BasicNameValue)radioButton.getTag()).getValue();
            callback.onCheckedChanged((RadioButton) view);
            callback.setButtonChecked((RadioButton) view);
            setButtonUnChecked((RadioButton) view);
        }
    }

    public void setButtonUnChecked(RadioButton button) {
        for (int i = 0; i < getChildCount(); i++) {
            boolean flag = true;
            RadioGroup tmpGroup = (RadioGroup) getChildAt(i);
            for(int j = 0; j < tmpGroup.getChildCount(); j++) {
                RadioButton radioButton = (RadioButton) tmpGroup.getChildAt(j);
                if(radioButton == button) {
                    flag = false;
                } else {
                    callback.setButtonUnChecked(radioButton);
                }
            }
            if (flag) {
                tmpGroup.clearCheck();
            }
        }
    }

    interface OnCheckedCallback {
        void onCheckedChanged(RadioButton radioButton);

        void setButtonChecked(RadioButton radioButton);

        void setButtonUnChecked(RadioButton radioButton);
    }
}
