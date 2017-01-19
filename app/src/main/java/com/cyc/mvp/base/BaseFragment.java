package com.cyc.mvp.base;


import com.cyc.mvp.presenter.FragmentPresenter;
import com.cyc.mvp.view.IDelegate;

/**
 * Created by cyc on 2016/10/16.
 */

public abstract class BaseFragment<T extends IDelegate> extends FragmentPresenter {
}
