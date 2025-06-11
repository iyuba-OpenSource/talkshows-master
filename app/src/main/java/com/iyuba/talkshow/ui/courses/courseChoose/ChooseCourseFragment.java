package com.iyuba.talkshow.ui.courses.courseChoose;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.ToastUtils;
import com.iyuba.talkshow.constant.App;
import com.iyuba.talkshow.data.manager.AbilityControlManager;
import com.iyuba.talkshow.data.model.SeriesData;
import com.iyuba.talkshow.databinding.FragmentCoursechoosenewBinding;
import com.iyuba.talkshow.injection.PerFragment;
import com.iyuba.talkshow.ui.base.BaseViewBindingFragmet;
import com.iyuba.talkshow.ui.courses.common.OpenFlag;
import com.iyuba.talkshow.ui.courses.common.TypeHolder;
import com.iyuba.talkshow.ui.courses.coursedetail.CourseDetailActivity;
import com.iyuba.talkshow.ui.lil.view.recyclerView.NoScrollGridLayoutManager;
import com.iyuba.wordtest.ui.WordStepActivity;
import com.iyuba.wordtest.db.WordDataBase;
import com.iyuba.wordtest.entity.BookLevels;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


/**
 * 教材的选书界面
 */
@PerFragment
public class ChooseCourseFragment extends BaseViewBindingFragmet<FragmentCoursechoosenewBinding> implements ChooseCourseMVPView {

    String catId ;
    CourseChooseAdapter adapter  ;
    CourseTypeAdapter courseTypeAdapter;
    List<TypeHolder> list = new ArrayList<>();
    public static final String CATID = "CATID";
    public static final String FLAG = "FLAG";
    List<SeriesData> dataBeans  = new ArrayList<>();
    @Inject
    ChooseCoursePresenter mPresenter ;
    private int flag;

    FragmentCoursechoosenewBinding binding ;

    public static ChooseCourseFragment build(String catId, int flag) {
        ChooseCourseFragment fragment  = new ChooseCourseFragment() ;
        Bundle bundle  = new Bundle( );
        bundle.putString(CATID, catId);
        bundle.putInt(FLAG, flag);
        fragment.setArguments(bundle);
        return fragment ;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentComponent().inject(this);
        mPresenter.attachView(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCoursechoosenewBinding.inflate(getLayoutInflater(),container,false);
        return binding.getRoot();
    }

    public ChooseCourseFragment() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        list = TypeHelper.generateTypeHolders(App.COURSE_TYPE);
        catId  = getArguments().getString(CATID);
        //人教版处理
        if (AbilityControlManager.getInstance().isLimitPep()){
            catId = "320";
        }
        flag  = getArguments().getInt(FLAG);

        //设置列表显示(不可动)
        binding.recycler.setLayoutManager(new NoScrollGridLayoutManager(getActivity(),3,false));
        GridLayoutManager manager = new GridLayoutManager(getActivity(),5);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return 1;
            }
        });
        binding.recyclertype.setLayoutManager(manager);
        adapter = new CourseChooseAdapter(dataBeans,flag);
        courseTypeAdapter = new CourseTypeAdapter(list);
        adapter.setVoaCallback(callback);
        courseTypeAdapter.setCallback(cousreCallback);

        binding.recycler.setAdapter(adapter);
        binding.recyclertype.setAdapter(courseTypeAdapter);

        //获取类型数据(更换成下面一个)
//        mPresenter.chooseCourse(catId);
        mPresenter.choosePrimaryCourse(catId);

        //设置刷新操作
        binding.refreshLayout.setEnableRefresh(true);
        binding.refreshLayout.setEnableLoadMore(false);
        binding.refreshLayout.setRefreshHeader(new ClassicsHeader(getActivity()));
        binding.refreshLayout.setOnRefreshListener(refreshLayout -> {
            if (catId.equals("316")){
                mPresenter.chooseDataByRemote(true,catId);
            }else {
                mPresenter.chooseDataByRemote(false,catId);
            }
        });
    }

    @Override
    public void setCoures(List<SeriesData> beans) {
        //关闭加载
        binding.refreshLayout.finishRefresh();

        adapter.setDataBeans(beans);
    }

    @Override
    public void showToastShort(int resId) {

    }

    @Override
    public void showToastShort(String message) {

    }

    @Override
    public void showToastLong(int resId) {

    }

    @Override
    public void showToastLong(String message) {
        ToastUtils.showShort(message);
    }

    CourseChooseAdapter.CourseCallback callback = new CourseChooseAdapter.CourseCallback() {
        @Override
        public void onCourseClicked(int bookId, String courseTitle) {
            Log.e("ChooseCourseFragment", "onCourseClicked bookId " + bookId + ", courseTitle  " + courseTitle);
            mPresenter.putCourseId(bookId, courseTitle);
            BookLevels bookLevels = WordDataBase.getInstance(getActivity()).getBookLevelDao().getBookLevel(bookId);
            if (null!= bookLevels){
                mPresenter.refreshWords(bookId,bookLevels.version);
            }else {
                mPresenter.refreshWords(bookId,0);
            }
            if (flag == OpenFlag.TO_DETAIL) {
                getActivity().startActivity(CourseDetailActivity.buildIntent(getActivity(), bookId, courseTitle));
            }else if (flag == OpenFlag.TO_WORD){
                startActivity(WordStepActivity.buildIntent(mContext,bookId,courseTitle));
            }else {

            }
        }

        @Override
        public void onCourseLongClicked(int series) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("是否删除此书下的视频？");
            builder.setPositiveButton("是的", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mPresenter.deletCourses(series);
                }
            });
            builder.setCancelable(true);
            builder.show();
        }
    };

    CourseTypeAdapter.Callback cousreCallback = new CourseTypeAdapter.Callback() {
        @Override
        public void onTypeChoose(int id) {
            //将类型数据设置上
            catId = String.valueOf(id);

            //分类查询数据
            if (id == 316){
                mPresenter.chooseJuniorCourse(String.valueOf(id));
            }else {
//                mPresenter.chooseCourse(String.valueOf(id));
                //更换成下面一个
                mPresenter.choosePrimaryCourse(String.valueOf(id));
            }
        }
    };

    @Override
    public void init() {

    }
}
