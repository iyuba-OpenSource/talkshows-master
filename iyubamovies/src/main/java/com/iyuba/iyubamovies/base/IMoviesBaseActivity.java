package com.iyuba.iyubamovies.base;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public abstract class IMoviesBaseActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initWeight();
        setListener();
    }
    protected abstract int getLayoutId();
    protected abstract void initWeight();
    protected abstract void setListener();
}
