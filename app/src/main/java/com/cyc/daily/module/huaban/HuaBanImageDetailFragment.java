package com.cyc.daily.module.huaban;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.cyc.mvp.base.BaseFragment;

/**
 * Created by cyc on 2016/12/9 0009.
 */

public class HuaBanImageDetailFragment extends BaseFragment<HuaBanImageDetailFDelegate> {

    public static HuaBanImageDetailFragment newInstance(Bundle args) {
        HuaBanImageDetailFragment fragment = new HuaBanImageDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected HuaBanImageDetailFDelegate createDelegate(Fragment fragment) {
        return new HuaBanImageDetailFDelegate(fragment);
    }
}
