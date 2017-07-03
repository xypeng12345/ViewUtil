package com.xyp.meyki_bear.viewutil.RecyclerViewUtil;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：RecyclerViewUtil
 * 类描述：
 * 创建人：xyp
 * 创建时间：2017/5/31 15:28
 * 修改人：meyki-bear
 * 修改时间：2017/5/31 15:28
 * 修改备注：
 */

public abstract class MyHeaderFooterAdapter<VH extends MyHeaderFooterAdapter.HeaderAndFooterViewHolder>
        extends RecyclerView.Adapter<MyHeaderFooterAdapter.HeaderAndFooterViewHolder> {
    private HeaderAndFooterList mHeaderViews;
    private HeaderAndFooterList mFooterViews;
    private int headerType = 20000;
    private int footerType = 30000;

    private final static int EMPTY_TYPE = 40000; //数据为空时返回这个集合
    private EmptyView emptyView;
    private ShowWhat showWhat = ShowWhat.SHOW_ALL;//默认显示全部

    public void addHeaderView(View headerView) {
        addHeaderView(headerView, -1);
    }

    protected EmptyView getEmptyView() {
        return emptyView;
    }

    public void setEmptyView(View emptyView, ShowWhat emptyShowWhat) {
        if (this.emptyView == null) {
            this.emptyView = new EmptyView();
        }
        this.emptyView.setEmpty(emptyView);
        this.emptyView.setShowWhat(emptyShowWhat);

    }

    protected int getHeaderSize(){
        return mHeaderViews==null?0:mHeaderViews.size();
    }

    /**
     * 清楚空数据布局
     */
    public void clearEmptyView() {
        this.emptyView = null;
    }

    /**
     * 在置顶位置插入一个headerView
     *
     * @param headerView
     * @param index      这个index不能超过插入前headerViews的长度
     */
    public void addHeaderView(View headerView, int index) {
        if (mHeaderViews == null) {
            mHeaderViews = new HeaderAndFooterList();
        }
        //同一个headerView不能重复添加
        for (int i = 0; i < mHeaderViews.size(); i++) {
            if (mHeaderViews.get(i).view == headerView) {
                return;
            }
        }
        HeaderAndFooterBean map = new HeaderAndFooterBean();
        map.itemType = headerType;
        map.view = headerView;
        if (index == -1) {
            mHeaderViews.add(map);
            notifyItemInserted(mHeaderViews.size() - 1);
        } else {
            mHeaderViews.add(index, map);
            notifyItemInserted(index);
        }
        headerType++;
    }

    public void removeHeaderView(View headerView) {
        if (mHeaderViews == null) {
            return;
        }
        for (int i = mHeaderViews.size() - 1; i >= 0; i--) {
            if (mHeaderViews.get(i).view == headerView) {
                mHeaderViews.remove(i);
                notifyItemRemoved(i);
            }
        }
    }

    public void clearHeaderView() {
        if (mHeaderViews == null) {
            return;
        }
        notifyItemRangeRemoved(0, mHeaderViews.size() - 1);
        mHeaderViews.clear();
    }

    public int findHeaderPosition(View view) {
        for (int i = 0; i < mHeaderViews.size(); i++) {
            if (mHeaderViews.get(i).view == view) {
                return i;
            }
        }
        return -1;
    }

    public void addFooterView(View footerView) {
        if (mFooterViews == null) {
            mFooterViews = new HeaderAndFooterList();
        }
        //如果已经添加过了，则不在添加
        for (int i = 0; i < mFooterViews.size(); i++) {
            View view = mFooterViews.get(i).view;
            if (view == footerView) {
                return;
            }
        }
        HeaderAndFooterBean map = new HeaderAndFooterBean();
        map.itemType = footerType;
        map.view = footerView;
        mFooterViews.add(map);
        int headerSize = 0;
        int dataSize = 0;
        int footerSize = 0;
        if (mHeaderViews != null) {
            headerSize = mHeaderViews.size();
        }
        if (mFooterViews != null) {
            footerSize = mFooterViews.size();
        }
        dataSize = getCount();
        footerType++;
        //size从1开始，position从0开始，所以需要减1
        notifyItemInserted((headerSize + footerSize + dataSize + mFooterViews.size()) - 1);
        return;
    }

    public void removeFooterView(View footerView) {
        if (mFooterViews == null) {
            return;
        }
        for (int i = mFooterViews.size() - 1; i >= 0; i--) {
            if (mFooterViews.get(i).view == footerView) {
                mFooterViews.remove(i);
            }
        }
        int headerSize = 0;
        int dataSize = 0;
        int footerSize = 0;
        if (mHeaderViews != null) {
            headerSize = mHeaderViews.size();
        }
        if (mFooterViews != null) {
            footerSize = mFooterViews.size();
        }
        dataSize = getCount();
        footerType++;
        //size从1开始，position从0开始，所以需要减1
        notifyItemRemoved((headerSize + footerSize + dataSize + mFooterViews.size()) - 1);
    }

    public void clearFooterView() {
        if (mFooterViews == null) {
            return;
        }
        mFooterViews.clear();
    }

    @Override
    public int getItemCount() {
        if (isEmpty()) {//数据为空，显示空布局,且根据空布局的设置
            changeFooterAndHeaderShow(emptyView.getShowWhat());
        } else {
            changeFooterAndHeaderShow(showWhat);//数据不为空
        }
        return getHeaderCount() + getCount() + getFooterCount();
    }

    private void changeFooterAndHeaderShow(ShowWhat mEmptyShowWhat) {
        switch (mEmptyShowWhat) {
            case SHOW_ALL:
                setHeardViewsShowWhat(true);
                setFooterViewsShowWhat(true);
                break;
            case SHOW_HEADER:
                setHeardViewsShowWhat(true);
                setFooterViewsShowWhat(false);
                break;
            case SHOW_FOOTER:
                setHeardViewsShowWhat(false);
                setFooterViewsShowWhat(true);
                break;
            case SHOW_NONE:
                setHeardViewsShowWhat(false);
                setFooterViewsShowWhat(false);
                break;
        }
    }

    private void setHeardViewsShowWhat(boolean isShow) {
        if (mHeaderViews != null) {
            mHeaderViews.setShow(isShow);
        }
    }

    private void setFooterViewsShowWhat(boolean isShow) {
        if (mFooterViews != null) {
            mFooterViews.setShow(isShow);
        }
    }

    private int getHeaderCount() {
        return mHeaderViews != null ? mHeaderViews.size() : 0;
    }

    private int getFooterCount() {
        return mFooterViews != null ? mFooterViews.size() : 0;
    }

    protected abstract int getDataCount();

    protected abstract int getType(int position);

    @Override
    final public int getItemViewType(int position) {
        //有头布局且当前显示的Position在头布局的范围内
        if (isHeaderView(position)) {
            return mHeaderViews.get(position).itemType;
        }
        //有尾布局且当前显示的position在尾布局的范围内
        if (isFooterView(position)) {
            position = position - getHeaderCount() - getCount();
            return mFooterViews.get(position).itemType;
        }
        if (isEmpty()) { //不是头不是尾且是空数据，则返回空布局
            return EMPTY_TYPE;
        }
        int pos = getRealPosition(position);
        return getType(pos);
    }

    private boolean isEmpty() {
        return emptyView != null && getDataCount() == 0;
    }

    /**
     * 头尾布局中realPosition只需要处理头布局的影响，尾布局不需要
     *
     * @param position
     * @return
     */
    final protected int getRealPosition(int position) {
        if (mHeaderViews != null && position < getCount() + getHeaderCount()) {
            position = position - (mHeaderViews.size());
        }
        return position;
    }

    private boolean isHeaderView(int position) {
        return mHeaderViews != null && mHeaderViews.getShow() && position < mHeaderViews.size();
    }

    private boolean isFooterView(int position) {
        return mFooterViews != null && mFooterViews.getShow() && position > getCount() + getHeaderCount() - 1;
    }

    /**
     * 如果数据为空且有空布局，则返回1，否则返回0
     *
     * @return
     */
    private int getCount() {
        if (isEmpty()) {
            return 1;
        } else {
            return getDataCount();
        }
    }

    @Override
    final public void onBindViewHolder(MyHeaderFooterAdapter.HeaderAndFooterViewHolder holder, int position, List<Object> payloads) {
        if (isHeaderView(position) || isFooterView(position) || isEmpty()) {
            return;
        }
        final int pos = getRealPosition(position);
        convert((VH) holder, pos, payloads);
    }

    @Override
    final public void onBindViewHolder(MyHeaderFooterAdapter.HeaderAndFooterViewHolder holder, int position) {

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    /**
     * 专门处理瀑布流的RecyclerView的头布局
     *
     * @param holder
     */
    @Override
    public void onViewAttachedToWindow(MyHeaderFooterAdapter.HeaderAndFooterViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (gridLayoutManagerSpanLookUp == null) {
            gridLayoutManagerSpanLookUp = new GridLayoutManagerSpanLookUp();
        }
        //GridLayoutManager的头尾布局判断
        RecyclerView recyclerView = (RecyclerView) holder.itemView.getParent();
        //GridLayoutManager头尾布局判断方法
        RecyclerView.LayoutManager lm = recyclerView.getLayoutManager();
        if (lm instanceof GridLayoutManager) {
            final GridLayoutManager glm = (GridLayoutManager) lm;
//            if (gridLayoutManagerSpanLookUp.getLayoutManager() != glm) {
//                gridLayoutManagerSpanLookUp.setLayoutManager(glm);
            glm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return MyHeaderFooterAdapter.this.getSpanSize(position, glm.getSpanCount());
                }
            });
//            }
        } else if (lm instanceof StaggeredGridLayoutManager) {
            final StaggeredGridLayoutManager sgm = (StaggeredGridLayoutManager) lm;
            //瀑布流StaggeredGridLayoutManager头尾布局的判断方法
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            int layoutPosition = holder.getLayoutPosition();
            StaggeredGridLayoutManager.LayoutParams slp = (StaggeredGridLayoutManager.LayoutParams) lp;
            int spanSize = getSpanSize(layoutPosition, sgm.getSpanCount());
            //瀑布流没有占几行只说，只有是否占满的区别
            slp.setFullSpan(spanSize == sgm.getSpanCount());
        }
    }

    private GridLayoutManagerSpanLookUp gridLayoutManagerSpanLookUp;

    /**
     * 这个回调只在GirdLayoutManager时有精准效果，如果是瀑布流则只能选择是否占满一行
     *
     * @param position
     * @param spanCount
     * @return
     */
    final public int getSpanSize(int position, int spanCount) {
        return isHeaderView(position) || isFooterView(position) || isEmpty() ?
                spanCount :
                getViewHolderSpanSize(getRealPosition(position), spanCount);
    }

    /**
     * 这个ViewHolder占几行显示，这个方法返回值在grid布局中精准有效
     * 而在瀑布流布局中，如果返回值小于行数，则无效，大于等于行数则占满一行显示
     *
     * @return
     */
    protected int getViewHolderSpanSize(int position, int spanCount) {
        return 1;
    }

    private class GridLayoutManagerSpanLookUp extends GridLayoutManager.SpanSizeLookup {
        private GridLayoutManager layoutManager;

        public GridLayoutManager getLayoutManager() {
            return layoutManager;
        }

        public void setLayoutManager(GridLayoutManager layoutManager) {
            this.layoutManager = layoutManager;
        }

        @Override
        public int getSpanSize(int position) {
            return MyHeaderFooterAdapter.this.getSpanSize(position, layoutManager.getSpanCount());
        }
    }

    protected abstract void convert(VH holder, int position, List<Object> payloads);

    @Override
    final public HeaderAndFooterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderViews != null && mHeaderViews.getShow()) {
            for (int i = 0; i < mHeaderViews.size(); i++) {
                if (mHeaderViews.get(i).itemType == viewType) {
                    HeaderAndFooterViewHolder viewHolder = new HeaderAndFooterViewHolder(mHeaderViews.get(i).view);
                    onInitHeaderViewHolder(parent, viewHolder);
                    return viewHolder;
                }
            }
        }
        if (mFooterViews != null && mFooterViews.getShow()) {
            for (int i = 0; i < mFooterViews.size(); i++) {
                if (mFooterViews.get(i).itemType == viewType) {
                    HeaderAndFooterViewHolder viewHolder = new HeaderAndFooterViewHolder(mFooterViews.get(i).view);
                    onInitFooterViewHolder(parent, viewHolder);
                    return viewHolder;
                }
            }
        }
        if (isEmpty()) {
            View empty = emptyView.getEmpty();
            ViewGroup.LayoutParams layoutParams=new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            empty.setLayoutParams(layoutParams);
            HeaderAndFooterViewHolder viewHolder = new HeaderAndFooterViewHolder(empty);
            onInitEmpterViewHolder(parent, viewHolder);
            return viewHolder;
        }
        return onCreateDataViewHolder(parent, viewType);
    }

    protected void onInitHeaderViewHolder(ViewGroup parent, RecyclerView.ViewHolder header) {

    }

    protected void onInitFooterViewHolder(ViewGroup parent, RecyclerView.ViewHolder footer) {

    }

    protected void onInitEmpterViewHolder(ViewGroup parent, RecyclerView.ViewHolder empty) {

    }

    protected abstract VH onCreateDataViewHolder(ViewGroup parent, int viewType);


    private class EmptyView {
        private View empty;
        private ShowWhat showWhat;

        public View getEmpty() {
            return empty;
        }

        public void setEmpty(View empty) {
            this.empty = empty;
        }

        public ShowWhat getShowWhat() {
            return showWhat;
        }

        public void setShowWhat(ShowWhat showWhat) {
            this.showWhat = showWhat;
        }
    }

    public enum ShowWhat {
        SHOW_ALL, SHOW_HEADER, SHOW_FOOTER, SHOW_NONE
    }

    private class HeaderAndFooterBean {
        int itemType;
        View view;
    }

    private class HeaderAndFooterList extends ArrayList<HeaderAndFooterBean> {
        private boolean isShow = true;

        public void setShow(boolean isShow) {
            this.isShow = isShow;
        }

        public boolean getShow() {
            return isShow;
        }

        @Override
        public int size() {
            if (!isShow) {
                return 0;
            }
            return super.size();
        }
    }

    public class HeaderAndFooterViewHolder extends RecyclerView.ViewHolder {

        public HeaderAndFooterViewHolder(View itemView) {
            super(itemView);
        }
    }
}
