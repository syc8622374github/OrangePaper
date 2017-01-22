package com.cyc.daily.module.huaban;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.cyc.daily.R;
import com.cyc.daily.adapter.HeaderAndFooterRecyclerViewAdapter;
import com.cyc.daily.api.HuaBanApi;
import com.cyc.daily.module.huaban.adapter.RecyclerHuaBanAdapter;
import com.cyc.daily.module.huaban.bean.ListPinsBean;
import com.cyc.daily.module.huaban.bean.PinsMainEntity;
import com.cyc.daily.util.AuthUtils;
import com.cyc.daily.util.Constant;
import com.cyc.daily.util.RecyclerViewUtil;
import com.cyc.daily.widget.LoadingFooter;
import com.cyc.mvp.util.Logger;
import com.cyc.mvp.util.RetrofitUtil;
import com.cyc.mvp.view.AppDelegate;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by cyc on 2016/11/3 0003.
 */
public class HuaBanImageListDelegate extends AppDelegate {

    private RecyclerView recyclerView;
    private int mMaxId = 0;
    private String mKey;
    private RecyclerHuaBanAdapter mAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private LoadingFooter mFooterView;

    protected HuaBanImageListDelegate(Fragment fragment) {
        super(fragment);
        getBundleData(fragment.getArguments());
    }

    private void getBundleData(Bundle arguments) {
        mKey = arguments.getString(Constant.TYPE_KEY);
    }

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_huaban_image_list;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_list);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_widget);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initHuabanData(true);
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        initRecyclerView();
        initHuabanData(false);
    }

    private void initRecyclerView() {
        mAdapter = new RecyclerHuaBanAdapter(recyclerView);
        HeaderAndFooterRecyclerViewAdapter headAdapter = new HeaderAndFooterRecyclerViewAdapter(mAdapter);
        mAdapter.setOnClickItemListener(new RecyclerHuaBanAdapter.OnHuaBanOnAdapterListener() {
            @Override
            public void onItemImageClickListener(View view, Object itemBean, int position) {
                System.out.println("11111111111111111111");
            }

            @Override
            public void onItemClickListener(View view, Object itemBean, int position) {

            }
        });
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(headAdapter);
        if (getFootView() != null) {
            RecyclerViewUtil.addFootView(recyclerView, getFootView());
        }
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());//设置默认动画
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (RecyclerView.SCROLL_STATE_IDLE == newState) {
                    //滑动停止
                    boolean isBottom = staggeredGridLayoutManager
                            .findLastCompletelyVisibleItemPositions(new int[2])[1] >= mAdapter.getItemCount();
                    if (isBottom && !swipeRefreshLayout.isRefreshing()) {
                        getHuaBanDataOnScroll();
                    }
                } else if (RecyclerView.SCROLL_STATE_DRAGGING == newState) {
                    //用户正在滑动
//                    Logger.d("用户正在滑动 position=" + mAdapter.getAdapterPosition());
                } else {
                    //惯性滑动
//                    Logger.d("惯性滑动 position=" + mAdapter.getAdapterPosition());
                }
            }
        });
    }

    protected View getFootView() {
        if (mFooterView == null) {
            mFooterView = new LoadingFooter(fragment.getActivity());
            mFooterView.setState(LoadingFooter.State.Loading);
        }
        return mFooterView;
    }

    @Override
    public View getLoadingTargetView() {
        return recyclerView;
    }

    public void getHuaBanDataOnScroll() {
        RetrofitUtil.createHuaBanApi(context, HuaBanApi.class, HuaBanApi.api)
                .httpsTypeMaxLimitRx(AuthUtils.getAuthorizations(), mKey, mMaxId, Constant.LIMIT)
                .map(new Func1<ListPinsBean, List<PinsMainEntity>>() {
                    @Override
                    public List<PinsMainEntity> call(ListPinsBean listPinsBean) {
                        //取出list对象
                        return listPinsBean.getPins();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<PinsMainEntity>>() {
                    @Override
                    public void onCompleted() {
                        Logger.d();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e);
                        //checkException(e);//检查错误 弹出提示
                    }

                    @Override
                    public void onNext(List<PinsMainEntity> pinsEntities) {
                        Logger.d();
                        mMaxId = getMaxId(pinsEntities);
                        mAdapter.addListNotify(pinsEntities);
                    }
                });
    }

    /**
     * 从返回联网结果中保存max值 用于下次联网的关键
     *
     * @param result
     * @return
     */
    private int getMaxId(List<PinsMainEntity> result) {
        return result.get(result.size() - 1).getPin_id();
    }

    public void initHuabanData(final boolean isRefresh) {
        if (!isRefresh) {
            varyViewHelper.showLoadingView();
            swipeRefreshLayout.setEnabled(false);
        }
        RetrofitUtil.createHuaBanApi(context, HuaBanApi.class, HuaBanApi.api)
                .httpsTypeLimitRx(AuthUtils.getAuthorizations(), mKey, Constant.LIMIT)
                .map(new Func1<ListPinsBean, List<PinsMainEntity>>() {
                    @Override
                    public List<PinsMainEntity> call(ListPinsBean listPinsBean) {
                        return listPinsBean.getPins();
                    }
                })
                .subscribeOn(Schedulers.io())//发布者的运行线程 联网操作属于IO操作
                .observeOn(AndroidSchedulers.mainThread())//订阅者的运行线程 在main线程中才能修改UI
                .subscribe(new Subscriber<List<PinsMainEntity>>() {
                    @Override
                    public void onCompleted() {
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        varyViewHelper.showErrorView();
                        swipeRefreshLayout.setRefreshing(false);
                        System.out.println(e.getMessage());
                    }

                    @Override
                    public void onNext(List<PinsMainEntity> result) {
                        //保存maxId值 后续加载需要
                        mMaxId = getMaxId(result);
                        mAdapter.setListNotify(result);
                        if (!isRefresh) {
                            swipeRefreshLayout.setEnabled(true);
                            if (result.size() > 0) {
                                varyViewHelper.showDataView();
                            } else {
                                varyViewHelper.showEmptyView();
                            }
                        }
                    }
                });
    }

    @Override
    public void onRetryLoad() {
        super.onRetryLoad();
        initHuabanData(false);
    }
}
