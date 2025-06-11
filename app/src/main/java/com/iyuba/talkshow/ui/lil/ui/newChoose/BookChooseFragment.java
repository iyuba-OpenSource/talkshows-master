//package com.iyuba.talkshow.ui.lil.ui.newChoose;
//
//import android.os.Bundle;
//import android.view.View;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.recyclerview.widget.GridLayoutManager;
//
//import com.iyuba.lib_common.data.StrLibrary;
//import com.iyuba.lib_common.ui.BaseViewBindingFragment;
//import com.iyuba.lib_common.util.LibToastUtil;
//import com.iyuba.talkshow.R;
//import com.iyuba.talkshow.databinding.FragmentBookChooseBinding;
//import com.iyuba.talkshow.event.RefreshBookEvent;
//import com.iyuba.talkshow.ui.courses.coursedetail.CourseDetailActivity;
//import com.iyuba.talkshow.ui.lil.ui.newChoose.adapter.ChooseBigTypeAdapter;
//import com.iyuba.talkshow.ui.lil.ui.newChoose.adapter.ChooseBookTypeAdapter;
//import com.iyuba.talkshow.ui.lil.ui.newChoose.adapter.ChooseSmallTypeAdapter;
//import com.iyuba.talkshow.ui.lil.ui.newChoose.bean.BookShowBean;
//import com.iyuba.talkshow.ui.lil.ui.newChoose.bean.TypeShowBean;
//import com.iyuba.talkshow.ui.lil.ui.newChoose.listener.OnSelectListener;
//import com.iyuba.talkshow.ui.lil.ui.newChoose.preference.BookChooseHelper;
//import com.iyuba.talkshow.ui.lil.view.recyclerView.NoScrollGridLayoutManager;
//import com.scwang.smartrefresh.layout.api.RefreshLayout;
//import com.scwang.smartrefresh.layout.header.ClassicsHeader;
//import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
//
//import org.greenrobot.eventbus.EventBus;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class BookChooseFragment extends BaseViewBindingFragment<FragmentBookChooseBinding> implements BookChooseView{
//
//    //操作
//    private BookChoosePresenter presenter;
//    //大类型
//    private ChooseBigTypeAdapter bigTypeAdapter;
//    //小类型
//    private ChooseSmallTypeAdapter smallTypeAdapter;
//    //书籍
//    private ChooseBookTypeAdapter bookTypeAdapter;
//
//    //大类型名称
//    private String selectBigName = "人教版";
//    //小类型id
//    private String selectSmallId = "313";
//    //小类型名称
//    private String selectSmallName = "新起点";
//
//    //当前的类型
//    private String showType = BookChooseActivity.tag_lesson;
//
//    public static BookChooseFragment getInstance(String showType){
//        BookChooseFragment chooseFragment = new BookChooseFragment();
//        Bundle bundle = new Bundle();
//        bundle.putString(StrLibrary.type,showType);
//        chooseFragment.setArguments(bundle);
//        return chooseFragment;
//    }
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        showType = getArguments().getString(StrLibrary.type,BookChooseActivity.tag_lesson);
//
//        presenter = new BookChoosePresenter();
//        presenter.attachView(this);
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        showToolbar();
//        initView();
//        loadTypeData();
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//
//        presenter.detachView();
//    }
//
//    private void showToolbar(){
//        binding.toolbar.leftLayout.setVisibility(View.VISIBLE);
//        binding.toolbar.leftImage.setImageResource(R.drawable.back);
//        binding.toolbar.leftLayout.setOnClickListener(v->{
//            getActivity().finish();
//        });
//        binding.toolbar.title.setText("选择书籍");
//    }
//
//    //初始化界面
//    private void initView(){
//        //刷新内容
//        binding.refreshLayout.setEnableRefresh(true);
//        binding.refreshLayout.setEnableLoadMore(false);
//        binding.refreshLayout.setRefreshHeader(new ClassicsHeader(getActivity()));
//        binding.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
//            @Override
//            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
//                presenter.loadBookDataByRemote(selectSmallId);
//            }
//        });
//
//        //大类型
//        bigTypeAdapter = new ChooseBigTypeAdapter(getActivity(),new ArrayList<>());
//        binding.bigTypeView.setLayoutManager(new NoScrollGridLayoutManager(getActivity(),5,false));
//        binding.bigTypeView.setAdapter(bigTypeAdapter);
//        bigTypeAdapter.setOnSelectListener(new OnSelectListener<TypeShowBean>() {
//            @Override
//            public void onSelect(TypeShowBean typeShowBean) {
//                selectBigName = typeShowBean.getType();
//                //刷新小类型数据
//                smallTypeAdapter.refreshDataAndIndex(typeShowBean.getTypeList(),0);
//                //设置默认数据
//                selectSmallId = typeShowBean.getTypeList().get(0).getId();
//                selectSmallName = typeShowBean.getTypeList().get(0).getName();
//                //加载书籍数据
//                loadBookData(selectSmallId);
//            }
//        });
//
//        //小类型
//        smallTypeAdapter = new ChooseSmallTypeAdapter(getActivity(),new ArrayList<>());
//        binding.smallTypeView.setLayoutManager(new NoScrollGridLayoutManager(getActivity(),5,false));
//        binding.smallTypeView.setAdapter(smallTypeAdapter);
//        smallTypeAdapter.setOnSelectListener(new OnSelectListener<TypeShowBean.SmallTypeBean>() {
//            @Override
//            public void onSelect(TypeShowBean.SmallTypeBean smallTypeBean) {
//                selectSmallId = smallTypeBean.getId();
//                selectSmallName = smallTypeBean.getName();
//                //加载书籍数据
//                loadBookData(selectSmallId);
//            }
//        });
//
//        //书籍
//        bookTypeAdapter = new ChooseBookTypeAdapter(getActivity(),new ArrayList<>());
//        binding.bookView.setLayoutManager(new GridLayoutManager(getActivity(),3));
//        binding.bookView.setAdapter(bookTypeAdapter);
//        bookTypeAdapter.setOnSelectListener(new OnSelectListener<BookShowBean>() {
//            @Override
//            public void onSelect(BookShowBean bookShowBean) {
//                if (showType.equals(BookChooseActivity.tag_lesson)){
//                    BookChooseHelper.getInstance().saveCourseId(Integer.parseInt(bookShowBean.getId()));
//                    BookChooseHelper.getInstance().saveCourseTitle(bookShowBean.getName());
//
//                    //跳转
//                    getActivity().startActivity(CourseDetailActivity.buildIntent(getActivity(), Integer.parseInt(bookShowBean.getId()), bookShowBean.getName()));
//                }else {
//                    BookChooseHelper.getInstance().saveWordId(Integer.parseInt(bookShowBean.getId()));
//                    BookChooseHelper.getInstance().saveWordTitle(bookShowBean.getName());
//
//                    //刷新显示
//                    EventBus.getDefault().post(new RefreshBookEvent(Integer.parseInt(bookShowBean.getId()),0,true));
//                }
//
//                //回退
//                getActivity().finish();
//            }
//        });
//    }
//
//    //加载类型数据
//    private void loadTypeData(){
//        //根据类型判断显示
//        if (showType.equals(BookChooseActivity.tag_lesson)){
//            presenter.loadLessonTypeData();
//        }else {
//            presenter.loadWordTypeData();
//        }
//    }
//
//    //加载书籍数据
//    private void loadBookData(String typeId){
//        presenter.loadBookData(typeId);
//    }
//
//    @Override
//    public void showTypeData(List<TypeShowBean> list) {
//        binding.refreshLayout.finishRefresh();
//
//        //保存选中的数据内容
//        TypeShowBean showBean = list.get(0);
//        selectBigName = showBean.getType();
//        selectSmallId = showBean.getTypeList().get(0).getId();
//        selectSmallName = showBean.getTypeList().get(0).getName();
//
//        //展示数据
//        bigTypeAdapter.refreshData(list);
//        //展示小类型数据
//        smallTypeAdapter.refreshData(list.get(0).getTypeList());
//        //加载书籍数据
//        loadBookData(selectSmallId);
//    }
//
//    @Override
//    public void showCourseData(List<BookShowBean> list) {
//        binding.refreshLayout.finishRefresh();
//        //根据类型判断是否显示无视频的内容：课程显示无视频内容，单词不用
//        List<BookShowBean> showList = new ArrayList<>();
//        if (showType.equals(BookChooseActivity.tag_lesson)){
//            for (int i = 0; i < list.size(); i++) {
//                BookShowBean showBean = list.get(i);
//                if (showBean.getHasVideo()!=null && showBean.getHasVideo().equals("0")){
//                    continue;
//                }
//                showList.add(showBean);
//            }
//        }else {
//            showList = list;
//        }
//
//        //展示界面
//        bookTypeAdapter.refreshData(showList);
//    }
//
//    @Override
//    public void showFail(String showMsg) {
//        binding.refreshLayout.finishRefresh();
//        LibToastUtil.showToast(getActivity(),showMsg);
//    }
//
//    @Override
//    public void showError(String showError) {
//        binding.refreshLayout.finishRefresh();
//        LibToastUtil.showToast(getActivity(),showError);
//    }
//}
