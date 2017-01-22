package com.cyc.daily.module.huaban;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.cyc.mvp.base.BaseFragment;

/**
 * Created by cyc on 2016/11/3 0003.
 */

public class HuaBanMainFragment extends BaseFragment<HuaBanMainDelegate> {

    public static HuaBanMainFragment newInstance() {
        return newInstance(null);
    }

    public static HuaBanMainFragment newInstance(Bundle args) {
        HuaBanMainFragment fragment = new HuaBanMainFragment();
        if(args!=null){
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    protected HuaBanMainDelegate createDelegate(Fragment fragment) {
        return new HuaBanMainDelegate(fragment);
    }
}
