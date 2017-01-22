package com.cyc.daily.module.huaban.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cyc.daily.R;
import com.cyc.daily.adapter.BaseRecyclerAdapter;
import com.cyc.daily.module.huaban.bean.PinsMainEntity;
import com.cyc.mvp.util.Util;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;

/**
 * Created by cyc on 2017/1/20 0020.
 */

public class RecyclerHuaBanAdapter extends BaseRecyclerAdapter<PinsMainEntity> {
    protected final String mUrlSmallFormat;//小图地址
    protected final String mUrlGeneralFormat;//普通地址
    protected final String mUrlBigFormat;//大图地址

    public RecyclerHuaBanAdapter(RecyclerView mRecyclerView) {
        super(mRecyclerView);
        this.mUrlSmallFormat = mContext.getResources().getString(R.string.url_image_small);
        this.mUrlGeneralFormat = mContext.getResources().getString(R.string.url_image_general);
        this.mUrlBigFormat = mContext.getResources().getString(R.string.url_image_big);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_huaban_item, parent, false);
        ViewHolderGeneral holder = new ViewHolderGeneral(view);//使用子类初始化ViewHolder
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //        Logger.d(life);
        final PinsMainEntity bean = mList.get(position);

        //注释的是 动态修改image高度
//        LayoutParams lp = holder.img_card_image.getLayoutParams();
//        lp.height = height[mAdapterPosition];
//        holder.img_card_image.setLayoutParams(lp);

        //父类强制转换成子类 因为这个holder本来就是子类初始化的 所以可以强转
        ViewHolderGeneral viewHolder = (ViewHolderGeneral) holder;//强制类型转换 转成内部的ViewHolder

        onBindData(viewHolder, bean);
        onBindListener(viewHolder, bean,position);//初始化点击事件
    }

    private void onBindData(final ViewHolderGeneral holder, PinsMainEntity bean) {
        //检查图片信息
        if (checkInfoContext(bean)) {
            holder.ll_title_info.setVisibility(View.VISIBLE);

            String title = bean.getRaw_text();//图片的文字描述
            int like = bean.getLike_count();//被喜欢数量
            int gather = bean.getRepin_count();//被转采的数量
            if (!TextUtils.isEmpty(title)) {
                holder.tv_card_title.setVisibility(View.VISIBLE);
                holder.tv_card_title.setText(title);
            } else {
                holder.tv_card_title.setVisibility(GONE);
            }
            holder.tv_card_like.setText(" " + like);
            holder.tv_card_gather.setText(" " + gather);
        } else {
            holder.ll_title_info.setVisibility(GONE);
        }

//      String url_img = mUrlFormat + bean.getFile().getKey()+"_fw320sf";
        String url_img = String.format(mUrlGeneralFormat, bean.getFile().getKey());

//      String mImageUrl = "http://img.hb.aicdn.com/1d16a79ac7cffbec844eb48e7e714c9f8c0afffc7f997-ZZCJsm";

        if (Util.checkIsGif(bean.getFile().getType())) {
            holder.ibtn_card_gif.setVisibility(View.VISIBLE);
        } else {
            holder.ibtn_card_gif.setVisibility(INVISIBLE);
        }

        float ratio = Util.getAspectRatio(bean.getFile().getWidth(), bean.getFile().getHeight());
        //Drawable dProgressImage = CompatUtils.getTintListDrawable(mContext, R.drawable.ic_toys_black_48dp, R.color.refresh_progress_1);
        Glide.with(mContext).load(url_img).centerCrop().crossFade().into(holder.img_card_image);
        bean.setLink(url_img);
    }

    /**
     * 检查三项信息 任何一项不为空 都返回true
     *
     * @param bean
     * @return
     */
    private boolean checkInfoContext(PinsMainEntity bean) {

        String title = bean.getRaw_text();//图片的文字描述
        int like = bean.getLike_count();//被喜欢数量
        int gather = bean.getRepin_count();//被转采的数量

        if (!TextUtils.isEmpty(title)) {
            return true;
        } else if (like > 0 || gather > 0) {
            return true;
        }
        return false;
    }

    private void onBindListener(final ViewHolderGeneral viewHolder, final PinsMainEntity itemBean, final int position) {
        /*viewHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClickListener(viewHolder.mView,itemBean,position);
            }
        });*/
        viewHolder.img_card_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener instanceof  OnHuaBanOnAdapterListener){
                    ((OnHuaBanOnAdapterListener)mListener).onItemImageClickListener(viewHolder.img_card_image,itemBean,position);
                }
            }
        });
    }

    public interface OnHuaBanOnAdapterListener extends OnAdapterListener{
        void onItemImageClickListener(View view,Object itemBean,int position);
    }

    public static class ViewHolderGeneral extends RecyclerView.ViewHolder {
        //这个CardView采用两层操作
        public final View mView;

        public final FrameLayout rl_image;//第一层 包含图片和播放按钮
        public final ImageView img_card_image;
        public final ImageButton ibtn_card_gif;

        public final LinearLayout ll_title_info;//第二层 包含描述 图片信息
        public final TextView tv_card_title;//第二层 描述title

        public final LinearLayout ll_info;//第二层的子类 包含图片被采集和喜爱的信息
        public final TextView tv_card_gather;
        public final TextView tv_card_like;

        public ViewHolderGeneral(View view) {
            super(view);
            mView = view;
            rl_image = (FrameLayout) view.findViewById(R.id.framelayout_image);
            img_card_image = (ImageView) view.findViewById(R.id.img_card_image);//主图
            ibtn_card_gif = (ImageButton) view.findViewById(R.id.ibtn_card_gif);//播放按钮

            ll_title_info = (LinearLayout) view.findViewById(R.id.linearlayout_title_info);//图片所有文字信息
            tv_card_title = (TextView) view.findViewById(R.id.tv_card_title);//描述的title

            ll_info = (LinearLayout) view.findViewById(R.id.linearlayout_info);//文字子类
            tv_card_gather = (TextView) view.findViewById(R.id.tv_card_gather);
            tv_card_like = (TextView) view.findViewById(R.id.tv_card_like);
        }

    }
}
