package com.iyuba.talkshow.ui.lil.ui.choose.junior;

import android.os.Bundle;
import android.util.Pair;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

import com.iyuba.lib_common.data.StrLibrary;
import com.iyuba.talkshow.constant.ConfigData;

import com.iyuba.lib_common.bean.BookChapterBean;
import com.iyuba.lib_common.data.TypeLibrary;
import com.iyuba.lib_common.event.RefreshDataEvent;
import com.iyuba.lib_common.listener.OnSimpleClickListener;
import com.iyuba.lib_common.model.local.entity.BookEntity_junior;
import com.iyuba.lib_common.ui.BaseViewBindingFragment;
import com.iyuba.lib_common.util.LibStackUtil;
import com.iyuba.lib_common.util.LibToastUtil;
import com.iyuba.sdk.other.NetworkUtil;
import com.iyuba.talkshow.R;
import com.iyuba.talkshow.data.manager.AbilityControlManager;
import com.iyuba.talkshow.databinding.FragmentChooseJuniorBinding;
import com.iyuba.talkshow.ui.lil.dialog.LoadingDialog;
import com.iyuba.talkshow.ui.lil.dialog.bookType.BookTypeDialog;
import com.iyuba.talkshow.ui.lil.service.JuniorBgPlayEvent;
import com.iyuba.talkshow.ui.lil.service.JuniorBgPlaySession;
import com.iyuba.talkshow.ui.lil.ui.choose.junior.manager.JuniorBookChooseManager;
import com.iyuba.talkshow.ui.lil.ui.choose.junior.adapter.JuniorChooseBookAdapter;
import com.iyuba.talkshow.ui.lil.ui.choose.junior.adapter.JuniorChoosePublishAdapter;
import com.iyuba.talkshow.ui.lil.ui.choose.junior.adapter.JuniorChooseTypeAdapter;
import com.iyuba.talkshow.ui.lil.ui.choose.junior.manager.JuniorBookChooseTempManager;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * @title: 中小学选书界面
 * @date: 2023/5/19 11:37
 * @author: liang_mu
 * @email: liang.mu.cn@gmail.com
 * @description:
 */
public class JuniorChooseFragment extends BaseViewBindingFragment<FragmentChooseJuniorBinding> implements JuniorChooseView{

    private JuniorChoosePresenter presenter;

    private JuniorChoosePublishAdapter publishAdapter;
    private JuniorChooseTypeAdapter typeAdapter;
    private JuniorChooseBookAdapter bookAdapter;

    //选中的书籍类型
    private String selectBookTypeData;
    //选中的出版数据
    private String selectPublishData;
    //选中的类型数据
    private String selectTypeData;

    //书籍类型弹窗
    private BookTypeDialog bookTypeDialog;
    //加载弹窗
    private LoadingDialog loadingDialog;

    //当前类型标志(课程或者单词)
    private boolean isWord;

    public static JuniorChooseFragment getInstance(boolean isWord){
        JuniorChooseFragment fragment = new JuniorChooseFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(StrLibrary.word,isWord);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new JuniorChoosePresenter();
        presenter.attachView(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initToolbar();
        initList();

        //判断接口数据进行处理
        if (ConfigData.renVerifyCheck && AbilityControlManager.getInstance().isLimitPep()){
            presenter.getRenVerifyData();
        }
        refreshData(true,selectBookTypeData);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        presenter.detachView();
    }

    private void initToolbar(){
        binding.toolbar.title.setText("选择课程");
        binding.toolbar.leftImage.setBackgroundResource(R.drawable.ic_back_white_new);
        binding.toolbar.leftImage.setOnClickListener(v->{
            LibStackUtil.getInstance().finishCur();
        });
        binding.toolbar.rightImage.setBackgroundResource(R.drawable.ic_menu_white_new);
        binding.toolbar.rightImage.setOnClickListener(v->{
            showBookTypeDialog();
        });

        //暂时关闭书籍类型选择
//        binding.toolbar.rightLayout.setVisibility(View.INVISIBLE);
    }

    private void initList(){
        isWord = getArguments().getBoolean(StrLibrary.word,false);
        if (isWord){
            selectBookTypeData = JuniorBookChooseTempManager.getInstance().getBookType();
            selectPublishData = JuniorBookChooseTempManager.getInstance().getBookBigType();
            selectTypeData = JuniorBookChooseTempManager.getInstance().getBookSmallTypeId();
        }else {
            selectBookTypeData = JuniorBookChooseManager.getInstance().getBookType();
            selectPublishData = JuniorBookChooseManager.getInstance().getBookBigType();
            selectTypeData = JuniorBookChooseManager.getInstance().getBookSmallTypeId();
        }

        binding.refreshLayout.setEnableRefresh(true);
        binding.refreshLayout.setEnableLoadMore(false);
        binding.refreshLayout.setRefreshHeader(new ClassicsHeader(getActivity()));
        binding.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (!NetworkUtil.isConnected(getActivity())){
                    binding.refreshLayout.finishRefresh(false);
                    LibToastUtil.showToast(getActivity(),"请链接网络后重试");
                    return;
                }

                presenter.getJuniorNetBookData(selectBookTypeData, selectTypeData);
            }
        });

        //出版社类型
        publishAdapter = new JuniorChoosePublishAdapter(getActivity(),new ArrayList<>());
        GridLayoutManager publishManager = new GridLayoutManager(getActivity(),5);
        binding.bigTypeView.setLayoutManager(publishManager);
        binding.bigTypeView.setAdapter(publishAdapter);
        publishAdapter.setListener(new JuniorChoosePublishAdapter.onPublishClickListener() {
            @Override
            public void onClick(int position, Pair<String, List<Pair<String, String>>> pair) {
                //设置选中位置
                publishAdapter.refreshSelectIndex(position);
                //设置选中数据
                selectPublishData = pair.first;

                //刷新小类型
                int curTypeIndex = getCurTypeDataIndex(selectTypeData,pair.second);
                typeAdapter.refreshDataAndIndex(pair.second,curTypeIndex);

                //刷新书籍
                selectTypeData = pair.second.get(curTypeIndex).first;
                presenter.getBookData(selectTypeData);
            }
        });

        //小类型
        typeAdapter = new JuniorChooseTypeAdapter(getActivity(),new ArrayList<>());
        GridLayoutManager typeManager = new GridLayoutManager(getActivity(),5);
        binding.smallTypeView.setLayoutManager(typeManager);
        binding.smallTypeView.setAdapter(typeAdapter);
        typeAdapter.setListener(new JuniorChooseTypeAdapter.onTypeClickListener() {
            @Override
            public void onClick(int position, Pair<String, String> pair) {
                //设置选中位置
                typeAdapter.refreshSelectIndex(position);
                //设置选中数据
                selectTypeData = pair.first;

                //刷新书籍
                presenter.getBookData(pair.first);
            }
        });

        //书籍类型
        bookAdapter = new JuniorChooseBookAdapter(getActivity(),new ArrayList<>());
        GridLayoutManager bookManager = new GridLayoutManager(getActivity(),3);
        binding.showView.setLayoutManager(bookManager);
        binding.showView.setAdapter(bookAdapter);
        bookAdapter.setListener(new OnSimpleClickListener<BookEntity_junior>() {
            @Override
            public void onClick(BookEntity_junior entity) {
                //判断当前选中的和已经显示的有什么区别
                if (JuniorBgPlaySession.getInstance().getCurData()!=null){
                    BookChapterBean playBean = JuniorBgPlaySession.getInstance().getCurData();
                    if (!playBean.getTypes().equals(selectBookTypeData)
                            ||!playBean.getBookId().equals(entity.bookId)){
                        EventBus.getDefault().post(new JuniorBgPlayEvent(JuniorBgPlayEvent.event_audio_pause));
                        EventBus.getDefault().post(new JuniorBgPlayEvent(JuniorBgPlayEvent.event_control_pause));
                        EventBus.getDefault().post(new JuniorBgPlayEvent(JuniorBgPlayEvent.event_control_hide));
                    }
                }

                //保存数据
                if (isWord){
                    JuniorBookChooseTempManager.getInstance().setBookType(selectBookTypeData);
                    JuniorBookChooseTempManager.getInstance().setBookBigType(selectPublishData);
                    JuniorBookChooseTempManager.getInstance().setBookSmallTypeId(selectTypeData);
                    JuniorBookChooseTempManager.getInstance().setBookId(entity.bookId);
                    JuniorBookChooseTempManager.getInstance().setBookName(entity.seriesName);
                }else {
                    JuniorBookChooseManager.getInstance().setBookType(selectBookTypeData);
                    JuniorBookChooseManager.getInstance().setBookBigType(selectPublishData);
                    JuniorBookChooseManager.getInstance().setBookSmallTypeId(selectTypeData);
                    JuniorBookChooseManager.getInstance().setBookId(entity.bookId);
                    JuniorBookChooseManager.getInstance().setBookName(entity.seriesName);
                }
                //回调刷新
                EventBus.getDefault().post(new RefreshDataEvent(TypeLibrary.RefreshDataType.junior));
                //回退界面
                LibStackUtil.getInstance().finishCur();
            }
        });
    }

    //刷新数据
    private void refreshData(boolean isFirst,String bookType){
        if (isFirst){
            startLoading();
        }
        selectBookTypeData = bookType;

        if (selectBookTypeData.equals(TypeLibrary.BookType.junior_primary)){
            binding.toolbar.title.setText("小学英语");
        }else if (selectBookTypeData.equals(TypeLibrary.BookType.junior_middle)){
            binding.toolbar.title.setText("初中英语");
        }

        presenter.getTypeData(bookType);
    }

    @Override
    public void showTypeData(List<Pair<String, List<Pair<String, String>>>> list) {
        stopLoading();

        //筛选当前的数据，去除新概念内容
        list = filterPublishType(list);

        //默认关闭刷新操作
        binding.refreshLayout.setEnableRefresh(false);

        if (list!=null){
            //解析数据，并且进行判断
            if (list.size()>0){
                //显示刷新操作
                binding.refreshLayout.setEnableRefresh(true);
                //刷新出版数据
                int curPublishIndex = getCurPublishDataIndex(selectPublishData, list);
                publishAdapter.refreshDataAndIndex(list,curPublishIndex);
                //刷新类型数据
                int curTypeIndex = getCurTypeDataIndex(selectTypeData, list.get(curPublishIndex).second);
                typeAdapter.refreshDataAndIndex(list.get(curPublishIndex).second,curTypeIndex);
                //刷新书籍数据
                selectTypeData = typeAdapter.getCurSelectData().first;
                presenter.getBookData(selectTypeData);
            }else {
                publishAdapter.refreshDataAndIndex(new ArrayList<>(),0);
                LibToastUtil.showToast(getActivity(),"暂无类型数据~");
            }
        }else {
            LibToastUtil.showToast(getActivity(),"获取类型数据失败~");
        }
    }

    @Override
    public void showBookData(List<BookEntity_junior> list) {
        stopLoading();

        if (list!=null){
            binding.refreshLayout.finishRefresh(true);

            if (list.size()>0){
                bookAdapter.refreshData(list);
            }else {
                LibToastUtil.showToast(getActivity(),"暂无书籍数据～");
            }
        }else {
            binding.refreshLayout.finishRefresh(false);
            LibToastUtil.showToast(getActivity(),"获取书籍数据失败～");
        }
    }

    @Override
    public void refreshBookData() {
        binding.refreshLayout.autoRefresh();
    }

    @Override
    public void showPepVerifyData(boolean isFirst) {
        refreshData(isFirst,selectBookTypeData);
    }

    /****************辅助功能**************/
    //筛选当前出版社的数据（去除新概念内容）
    private List<Pair<String, List<Pair<String, String>>>> filterPublishType(List<Pair<String, List<Pair<String, String>>>> list){
        List<Pair<String, List<Pair<String, String>>>> newList = new ArrayList<>();
        if (list!=null&&list.size()>0){
            for (int i = 0; i < list.size(); i++) {
                Pair<String,List<Pair<String, String>>> pair = list.get(i);
                /*if (pair.first.contains("新概念")){
                    continue;
                }*/

                //根据接口判断
                if (AbilityControlManager.getInstance().isLimitPep()&&pair.first.contains("人教版")){
                    continue;
                }

                newList.add(pair);
            }
        }
        return newList;
    }

    //获取当前出版类型数据的位置
    private int getCurPublishDataIndex(String text,List<Pair<String,List<Pair<String,String>>>> list){
        int selectIndex = 0;

        if (list!=null&&list.size()>0){
            for (int i = 0; i < list.size(); i++) {
                String data = list.get(i).first;
                if (text.equals(data)){
                    selectIndex = i;
                }
            }
        }
        return selectIndex;
    }

    //获取当前类型数据的位置
    private int getCurTypeDataIndex(String id,List<Pair<String,String>> list){
        int selectIndex = 0;

        if (list!=null&&list.size()>0){
            for (int i = 0; i < list.size(); i++) {
                String dataId = list.get(i).first;

                if (id.equals(dataId)){
                    selectIndex = i;
                }
            }
        }
        return selectIndex;
    }

    //书籍类型弹窗
    private void showBookTypeDialog(){
        selectPublishData = "";
        selectTypeData = "";

        if (bookTypeDialog==null){
            bookTypeDialog = new BookTypeDialog(getActivity());
            bookTypeDialog.create();

            List<Pair<String,String>> list = new ArrayList<>();
            list.add(new Pair<>(TypeLibrary.BookType.junior_primary,"小学"));
            list.add(new Pair<>(TypeLibrary.BookType.junior_middle,"初中"));

            bookTypeDialog.setTitle("选择书籍类型");
            bookTypeDialog.setData(selectBookTypeData,list);
            bookTypeDialog.setListener(new OnSimpleClickListener<Pair<String, String>>() {
                @Override
                public void onClick(Pair<String, String> pair) {
                    selectBookTypeData = pair.first;
                    refreshData(true,pair.first);
                }
            });
        }else {
            bookTypeDialog.setType(selectBookTypeData);
        }
        bookTypeDialog.show();
    }

    //开启加载弹窗
    private void startLoading(){
        if (loadingDialog==null){
            loadingDialog = new LoadingDialog(getActivity());
            loadingDialog.create();
        }
        loadingDialog.setMessage("正在加载书籍数据～");
        loadingDialog.show();
    }

    //关闭加载弹窗
    private void stopLoading(){
        if (loadingDialog!=null&&loadingDialog.isShowing()){
            loadingDialog.dismiss();
        }
    }

    //获取数据
    private void getNetBookData(){
        if (!NetworkUtil.isConnected(getActivity())){
            binding.refreshLayout.finishRefresh(false);
            LibToastUtil.showToast(getActivity(),"请链接网络后重试");
            return;
        }

        presenter.getJuniorNetBookData(selectBookTypeData, selectTypeData);
    }
}
