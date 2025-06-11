package com.iyuba.talkshow.ui.courses.wordChoose;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

import com.iyuba.lib_user.manager.UserInfoManager;
import com.iyuba.talkshow.R;
import com.iyuba.talkshow.TalkShowApplication;
import com.iyuba.talkshow.constant.App;
import com.iyuba.talkshow.constant.ConfigData;
import com.iyuba.talkshow.data.manager.AbilityControlManager;
import com.iyuba.talkshow.data.model.SeriesData;
import com.iyuba.talkshow.data.model.Voa;
import com.iyuba.talkshow.databinding.FragmentCoursechooseBinding;
import com.iyuba.talkshow.event.RefreshBookEvent;
import com.iyuba.talkshow.event.SelectBookEvent;
import com.iyuba.talkshow.injection.PerFragment;
import com.iyuba.talkshow.ui.base.BaseViewBindingFragmet;
import com.iyuba.talkshow.ui.courses.common.OpenFlag;
import com.iyuba.talkshow.ui.courses.common.TypeHolder;
import com.iyuba.talkshow.ui.courses.coursedetail.CourseDetailActivity;
import com.iyuba.talkshow.ui.courses.wordChoose.bean.Series;
import com.iyuba.talkshow.ui.lil.dialog.LoadingDialog;
import com.iyuba.talkshow.ui.lil.util.DateUtil;
import com.iyuba.talkshow.ui.lil.view.LoadingNewDialog;
import com.iyuba.talkshow.util.NetStateUtil;
import com.iyuba.talkshow.util.ToastUtil;
import com.iyuba.wordtest.db.BookLevelDao;
import com.iyuba.wordtest.db.CategorySeriesDao;
import com.iyuba.wordtest.db.NewBookLevelDao;
import com.iyuba.wordtest.db.WordDataBase;
import com.iyuba.wordtest.entity.BookLevels;
import com.iyuba.wordtest.entity.CategorySeries;
import com.iyuba.wordtest.entity.NewBookLevels;
import com.iyuba.wordtest.manager.WordManager;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

/**
 * 单词选书界面
 * <p>
 * 这里不筛选是否有视频数据（即不判断isVideo=0）
 */
@PerFragment
public class WordChooseFragment extends BaseViewBindingFragmet<FragmentCoursechooseBinding> implements WordChooseMVPView {

    //内容显示
    WordChooseAdapter adapter;
    WordTypeAdapter courseTypeAdapter;
    WordTitleAdapter courseTitleAdapter;
    List<TypeHolder> listTitle = new ArrayList<>();
    List<TypeHolder> list = new ArrayList<>();
    List<SeriesData> dataBeans = new ArrayList<>();

    //参数
    String catId;
    public static final String CATID = "CATID";
    public static final String FLAG = "FLAG";
    public static final String TYPE = "TYPE";
    private int flag;
    private int type;

    @Inject
    WordChoosePresenter mPresenter;
    HashMap<String, List<TypeHolder>> hashXiao = new HashMap();
    WordDataBase db;
    CategorySeriesDao categoryDao;

    //布局样式
    FragmentCoursechooseBinding binding;

    public static WordChooseFragment build(String catId, int flag, int type) {
        WordChooseFragment fragment = new WordChooseFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CATID, catId);
        bundle.putInt(FLAG, flag);
        bundle.putInt(TYPE, type);
        fragment.setArguments(bundle);
        return fragment;
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
        binding = FragmentCoursechooseBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = WordDataBase.getInstance(TalkShowApplication.getContext());
        categoryDao = db.getCategorySeriesDao();

        //原来获取数据库数据的操作
//        getLocalTypeData();

        //参数
        type = getArguments().getInt(TYPE);
        catId = getArguments().getString(CATID);
        flag = getArguments().getInt(FLAG);

        //设置书籍内容
        binding.recycler.setLayoutManager(new GridLayoutManager(mActivity, 3));
        adapter = new WordChooseAdapter(dataBeans, flag);
        adapter.setVoaCallback(callback);
        binding.recycler.setAdapter(adapter);

        //设置大类型内容
        courseTypeAdapter = new WordTypeAdapter(list);
        courseTypeAdapter.setCallback(cousreCallback);
        binding.recyclertype.setLayoutManager(new GridLayoutManager(mActivity, 5));
        binding.recyclertype.setAdapter(courseTypeAdapter);
        if (list == null || list.size() < 1) {
            binding.recyclertype.setVisibility(View.GONE);
        }

        //设置小类型内容
        courseTitleAdapter = new WordTitleAdapter(listTitle);
        courseTitleAdapter.setCallback(titleCallback);
        binding.recyclertitle.setLayoutManager(new GridLayoutManager(mActivity, 5));
        binding.recyclertitle.setAdapter(courseTitleAdapter);

        if (type == OpenFlag.TO_WORD) {
            courseTitleAdapter.putActiveTitle(mPresenter.getWordClass());
            courseTypeAdapter.putActiveType(mPresenter.getWordType());
        } else {
            courseTitleAdapter.putActiveTitle(mPresenter.getCourseClass());
            courseTypeAdapter.putActiveType(mPresenter.getCourseType());
        }

        //获取数据
        if (NetStateUtil.isConnected(TalkShowApplication.getContext())) {
            startLoading("正在获取课程类型...");
            //获取类型数据
            setUseMethod(mPresenter.getLessonType());
            //获取书籍数据
//            mPresenter.getBookDataByRemote(catId);
        } else {
            ToastUtil.showToast(mContext, "选择课程需要打开数据网络。");
            mPresenter.getBookDataByDb(catId);
        }
    }

    /*private void setTextArea(String catId) {
        binding.tvArea.setVisibility(View.VISIBLE);
        switch (catId) {
            case "316":
                binding.tvArea.setText(R.string.area_renjiao);
                break;
            case "330":
                binding.tvArea.setText(R.string.area_waiyan);
                break;
            case "331":
                binding.tvArea.setText(R.string.area_beishi);
                break;
            case "332":
                binding.tvArea.setText(R.string.area_renai);
                break;
            case "333":
                binding.tvArea.setText(R.string.area_jiban);
                break;
            case "334":
                binding.tvArea.setText(R.string.area_yilin);
                break;
            case "335":
                binding.tvArea.setText(R.string.area_lujiao);
                break;
            default:
                binding.tvArea.setVisibility(View.GONE);
                break;
        }
    }*/

    private class SortBySeries implements Comparator {
        @Override
        public int compare(Object o1, Object o2) {
            SeriesData s1 = (SeriesData) o1;
            SeriesData s2 = (SeriesData) o2;

            //这里按照书籍的id进行排序，小的在前面，小的在后面
//            if (Integer.parseInt(s1.getId()) > Integer.parseInt(s2.getId())) {
//                return 1;
//            } else {
//                return -1;
//            }

            // TODO: 2024/11/12 补充数据后重新处理下，李涛在中小学英语群里确定使用createTime进行处理，从小到大排序
            long firstTime = DateUtil.toDateLong(s1.getCreateTime(),DateUtil.YMDHMSS);
            long secondTime = DateUtil.toDateLong(s2.getCreateTime(),DateUtil.YMDHMSS);
            if (firstTime > secondTime){
                return 1;
            }else {
                return -1;
            }
        }
    }

    @Override
    public void setMoreCourse(List<SeriesData> beans) {
        if (beans == null || beans.size() < 1) {
            adapter.setDataBeans(beans);
            return;
        }
        if (((type == OpenFlag.TO_WORD)) && "337".equals(catId)) {
            Collections.sort(beans, new SortBySeries());
            adapter.setDataBeans(beans);
        } else {
            Collections.sort(beans, new SortBySeries());
            adapter.setDataBeans(beans);
        }

        binding.recycler.setVisibility(View.VISIBLE);
    }

    @Override
    public void setCoures(List<SeriesData> beans) {
        if (beans == null || beans.size() < 1) {
            mPresenter.getBookDataByDb(catId);
            return;
        }

        if (((type == OpenFlag.TO_WORD)) && "337".equals(catId)) {
            Collections.sort(beans, new SortBySeries());
            adapter.setDataBeans(beans);
        } else {
            Collections.sort(beans, new SortBySeries());
            adapter.setDataBeans(beans);
        }
    }

    @Override
    public void setLesson(List<Series> series) {
        stopLoading();

        if (series == null || series.size() < 1) {
            return;
        }

        /*if (mPresenter.getLessonType().equals(ConfigData.default_word_show_type)) {
            setXiaoLesson(series);
        } else {
            setXiaoLesson(series);
        }*/
        setXiaoLesson(series);

        //刷新书籍数据
        catId = String.valueOf(series.get(0).SeriesData.get(0).Category);
        mPresenter.getBookDataByRemote(catId);
    }

    @Override
    public void setLessonFail(String showMsg) {
        stopLoading();
        //刷新类型数据显示
        getLocalTypeData();
        if (list != null && list.size() > 0) {
            courseTypeAdapter.SetCourseList(list);
            courseTypeAdapter.notifyDataSetChanged();
            binding.recyclertype.setVisibility(View.VISIBLE);
        }
        if (listTitle != null && listTitle.size() > 0) {
            courseTitleAdapter.SetTitleList(listTitle);
            courseTitleAdapter.notifyDataSetChanged();
            binding.recyclertitle.setVisibility(View.VISIBLE);
        }
    }

    /*private void setChuLesson(List<Series> series) {
        categoryDao.deleteCategory(UserInfoManager.getInstance().getUserId());
        List<TypeHolder> newTitle = new ArrayList<>();
        for (int i = 0; i < series.size(); i++) {
            Series ser = series.get(i);
            if (ser == null) {
                continue;
            }
            for (Series.SeriesDatas seriesData: ser.SeriesData) {
                if (seriesData == null) {
                    continue;
                }
                if (App.APP_TENCENT_MOOC) {
                    if (WordTypeHelper.TYPE_OPPO_RENJIAO) {
                        newTitle.add(new TypeHolder(seriesData.Category,seriesData.SeriesName));
                    } else {
                        if (!"人教版".equals(seriesData.SeriesName)) {
                            newTitle.add(new TypeHolder(seriesData.Category,seriesData.SeriesName));
                        }
                    }
                } else {
                    if (!"新概念".equals(seriesData.SeriesName)) {
                        newTitle.add(new TypeHolder(seriesData.Category,seriesData.SeriesName));
                    }
                }
                CategorySeries categorySer = categoryDao.getUidCategory(seriesData.Category, UserInfoManager.getInstance().getUserId());
                if (categorySer == null) {
                    categoryDao.saveCategory(new CategorySeries(seriesData.Category, seriesData.SeriesName, seriesData.lessonName, ser.SourceType, seriesData.isVideo, UserInfoManager.getInstance().getUserId()));
                } else {
                    categoryDao.updateCategory(new CategorySeries(seriesData.Category, seriesData.SeriesName, seriesData.lessonName, ser.SourceType, seriesData.isVideo, UserInfoManager.getInstance().getUserId()));
                }
            }
        }
        Log.e("ChooseCoursePresenter", "setLesson newTitle " + newTitle.size());
        if (newTitle.size() < 3) {
            listTitle.clear();
            listTitle.addAll(newTitle);
            courseTitleAdapter.SetTitleList(listTitle);
            courseTitleAdapter.notifyDataSetChanged();
            binding.recyclertype.setVisibility(View.GONE);
            return;
        }
        binding.recyclertype.setVisibility(View.VISIBLE);
        int middle = newTitle.size()/2;
        listTitle.clear();
        for (int i = 0; i < middle; i++) {
            listTitle.add(newTitle.get(i));
        }
        courseTitleAdapter.SetTitleList(listTitle);
        courseTitleAdapter.notifyDataSetChanged();
        list.clear();
        for (int i = middle; i < newTitle.size(); i++) {
            list.add(newTitle.get(i));
        }
        courseTypeAdapter.SetCourseList(list);
        courseTypeAdapter.notifyDataSetChanged();
    }*/

    private void setXiaoLesson(List<Series> series) {
        categoryDao.deleteCategory(UserInfoManager.getInstance().getUserId());
        listTitle.clear();
        list.clear();
        hashXiao.clear();

        //筛选操作
        for (Series ser : series) {
            if (ser == null) {
                continue;
            }

            //人教版审核限制
            if (AbilityControlManager.getInstance().isLimitPep() && ser.SourceType.equals("人教版")) {
                continue;
            }

            for (Series.SeriesDatas seriesData : ser.SeriesData) {
                if (seriesData == null) {
                    continue;
                }

                if (hashXiao.containsKey(ser.SourceType)) {
                    List<TypeHolder> result = hashXiao.get(ser.SourceType);
                    result.add(new TypeHolder(seriesData.Category, seriesData.SeriesName));
                } else {
                    List<TypeHolder> result = new ArrayList<>();
                    result.add(new TypeHolder(seriesData.Category, seriesData.SeriesName));
                    hashXiao.put(ser.SourceType, result);
                }
                CategorySeries categorySer = categoryDao.getUidCategory(seriesData.Category, UserInfoManager.getInstance().getUserId());
                if (categorySer == null) {
                    categoryDao.saveCategory(new CategorySeries(seriesData.Category, seriesData.SeriesName, seriesData.lessonName, ser.SourceType, seriesData.isVideo, UserInfoManager.getInstance().getUserId()));
                } else {
                    categoryDao.updateCategory(new CategorySeries(seriesData.Category, seriesData.SeriesName, seriesData.lessonName, ser.SourceType, seriesData.isVideo, UserInfoManager.getInstance().getUserId()));
                }
            }
        }

        //筛选数据
        setXiaoTitle();
        setXiaoType();
        //刷新数据显示
        courseTitleAdapter.SetTitleList(listTitle);
        courseTypeAdapter.SetCourseList(list);
        courseTitleAdapter.notifyDataSetChanged();
        courseTypeAdapter.notifyDataSetChanged();
        //显示界面
        binding.recyclertype.setVisibility(View.VISIBLE);
        binding.recyclertitle.setVisibility(View.VISIBLE);
    }

    private void setXiaoTitle() {
        int index = 0;
        if (hashXiao == null || hashXiao.size() < 1) {
            return;
        }

        //人教版审核限制
        if (AbilityControlManager.getInstance().isLimitPep()) {
            ++index;
        }

        if (hashXiao.containsKey("人教版")) {
            listTitle.add(new TypeHolder(WordTypeHelper.TYPE_PRIMARY_RENJIAO, "人教版"));
            ++index;
        }
        if (hashXiao.containsKey("北师版")) {
            listTitle.add(new TypeHolder(WordTypeHelper.TYPE_PRIMARY_BEISHI, "北师版"));
            ++index;
        }
        if (hashXiao.containsKey("北京版")) {
            listTitle.add(new TypeHolder(WordTypeHelper.TYPE_PRIMARY_BEIJING, "北京版"));
            ++index;
        }
        for (String key : hashXiao.keySet()) {
            Log.e("ChooseCoursePresenter", "setXiaoTitle key " + key);
            switch (key) {
                case "人教版":
                case "北师版":
                case "北京版":
                    break;
                case "新概念":
                    if (App.APP_TENCENT_MOOC) {
                        listTitle.add(new TypeHolder(index, key));
                        ++index;
                    }
                    break;
                default:
                    listTitle.add(new TypeHolder(index, key));
                    ++index;
                    break;
            }
        }
    }

    private void setXiaoType() {
        if (hashXiao == null || hashXiao.size() < 1) {
            if (type == OpenFlag.TO_WORD) {
                list = WordTypeHelper.getXiaoxueType(mPresenter.getWordClass());
            } else {
                list = WordTypeHelper.getXiaoxueType(mPresenter.getCourseClass());
            }
            return;
        }
        list.clear();
        int index = 0;
        if (type == OpenFlag.TO_WORD) {
            index = mPresenter.getWordClass();
        } else {
            index = mPresenter.getCourseClass();
        }

        //人教版审核限制
        if (AbilityControlManager.getInstance().isLimitPep() && hashXiao.keySet().size() == 3) {
            index += 1;
        }

        for (String key : hashXiao.keySet()) {
            for (TypeHolder typeHolder : listTitle) {
                if ((index == typeHolder.getId()) && key.equals(typeHolder.getValue())) {
                    list.addAll(hashXiao.get(key));
                    return;
                }
            }
        }
    }

    @Override
    public void showToastShort(int resId) {
        ToastUtil.showToast(mActivity, getResources().getString(resId));
    }

    @Override
    public void showToastShort(String message) {
        ToastUtil.showToast(mActivity, message);
    }

    @Override
    public void showToastLong(int resId) {
        ToastUtil.showToast(mActivity, getResources().getString(resId));
    }

    @Override
    public void showToastLong(String message) {
        ToastUtil.showToast(mActivity, message);
    }

    WordChooseAdapter.CourseCallback callback = new WordChooseAdapter.CourseCallback() {
        @Override
        public void onCourseClicked(int bookId, int count, int category, String courseTitle) {
            if (type == OpenFlag.TO_WORD) {
                mPresenter.putWordId(bookId, courseTitle);
                mPresenter.putWordCategory(category);
            } else {
                mPresenter.putCourseId(bookId, courseTitle);
                mPresenter.putCourseCategory(category);
            }
            BookLevelDao bookLevelDao = WordDataBase.getInstance(TalkShowApplication.getInstance()).getBookLevelDao();
            int version = 0;
            if (WordManager.WordDataVersion == 2) {
                NewBookLevelDao newLevelDao = WordDataBase.getInstance(TalkShowApplication.getInstance()).getNewBookLevelDao();
                if (bookLevelDao != null) {
                    NewBookLevels newLevel = newLevelDao.getBookLevel(bookId, String.valueOf(UserInfoManager.getInstance().getUserId()));
                    if (newLevel == null) {
                        TalkShowApplication.getSubHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                NewBookLevels levels = new NewBookLevels(bookId, 0, 0, 0, String.valueOf(UserInfoManager.getInstance().getUserId()));
                                newLevelDao.saveBookLevel(levels);
                            }
                        });
                    } else {
                        version = newLevel.version;
                    }
                }
            } else if (bookLevelDao != null) {
                BookLevels bookLevels = bookLevelDao.getBookLevel(bookId);
                if (bookLevels == null) {
                    TalkShowApplication.getSubHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            BookLevels levels = new BookLevels(bookId, 0, 0, 0);
                            bookLevelDao.saveBookLevel(levels);
                        }
                    });
                } else {
                    version = bookLevels.version;
                }
            }
            if (type == OpenFlag.TO_WORD) {
                if (version == 0) {
                    EventBus.getDefault().post(new RefreshBookEvent(bookId, version, true));
                } else {
                    EventBus.getDefault().post(new RefreshBookEvent(bookId, version));
                }
            } else {
                List<Voa> result = mPresenter.loadVoasByBookId(bookId);
                if (result == null || result.size() < count) {
                    EventBus.getDefault().post(new SelectBookEvent(bookId, version, true));
                } else {
                    EventBus.getDefault().post(new SelectBookEvent(bookId, version));
                }
            }
            if (flag == OpenFlag.TO_DETAIL) {
                if (type == OpenFlag.TO_WORD) {
                    mActivity.startActivity(CourseDetailActivity.buildIntent(mActivity, bookId, mPresenter.getWordTitle()));
                } else {
                    mActivity.startActivity(CourseDetailActivity.buildIntent(mActivity, bookId, mPresenter.getCourseTitle()));
                }
            } else if (flag == OpenFlag.TO_WORD) {
//                startActivity(WordStepActivity.buildIntent(mContext,bookId,courseTitle));
                mActivity.finish();
            } else {
                mActivity.finish();
            }
        }

        @Override
        public void onCourseLongClicked(int series) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
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

    WordTypeAdapter.Callback cousreCallback = new WordTypeAdapter.Callback() {
        @Override
        public void onTypeChoose(int pos, int id) {
            if (type == OpenFlag.TO_WORD) {
                mPresenter.putWordType(pos);
//                mPresenter.putWordCategory(id);
            } else {
                mPresenter.putCourseType(pos);
//                mPresenter.putCourseCategory(id);
            }

            catId = "" + id;
            mPresenter.getBookDataByRemote(catId);
        }
    };

    WordTitleAdapter.TitleCallback titleCallback = new WordTitleAdapter.TitleCallback() {
        @Override
        public void onTitleChoose(int pos, int id) {
            if (type == OpenFlag.TO_WORD) {
                mPresenter.putWordClass(pos);
            } else {
                mPresenter.putCourseClass(pos);
            }
            setXiaoType();
            courseTypeAdapter.SetCourseList(list);
            courseTypeAdapter.notifyDataSetChanged();
        }
    };

    @Override
    public void init() {

    }

    //刷新课程信息
    public void refreshLessonData(String lessonType) {
        //刷新课程数据的时候，默认获取第一个
        setUseCatId(lessonType);

        if (NetStateUtil.isConnected(TalkShowApplication.getContext())) {
            mPresenter.getBookDataByRemote(catId);
            setUseMethod(lessonType);
        } else {
            ToastUtil.showToast(mContext, "选择课程需要打开数据网络。");
            mPresenter.getBookDataByDb(catId);
        }
    }

    //设置执行的课程id
    private void setUseCatId(String lessonType) {
        if (lessonType.equals("primary")) {
            catId = "313";
        } else if (lessonType.equals("junior")) {
            catId = "316";
        }
    }

    //设置需要的方法
    private void setUseMethod(String lessonType) {
        if (lessonType.equals("primary")) {
            mPresenter.choosePrimaryLessonNew(lessonType);
        } else if (lessonType.equals("junior")) {
            mPresenter.chooseJuniorLessonNew(lessonType);
        }
    }

    //获取本地类型的数据
    private void getLocalTypeData() {
        List<CategorySeries> newTitle = categoryDao.getUidCategories(UserInfoManager.getInstance().getUserId());
        if ((newTitle == null) || (newTitle.size() < 1)) {
            listTitle = WordTypeHelper.getXiaoxueTitle(WordTypeHelper.TYPE_PRIMARY_BEIJING);
            if (type == OpenFlag.TO_WORD) {
                list = WordTypeHelper.getXiaoxueType(mPresenter.getWordClass());
            } else {
                list = WordTypeHelper.getXiaoxueType(mPresenter.getCourseClass());
            }
        } else {
            listTitle.clear();
            list.clear();
            hashXiao.clear();
            for (int i = 0; i < newTitle.size(); i++) {
                CategorySeries category = newTitle.get(i);
                if (hashXiao.containsKey(category.SourceType)) {
                    List<TypeHolder> result = hashXiao.get(category.SourceType);
                    result.add(new TypeHolder(newTitle.get(i).Category, newTitle.get(i).SeriesName));
                } else {
                    List<TypeHolder> result = new ArrayList<>();
                    result.add(new TypeHolder(newTitle.get(i).Category, newTitle.get(i).SeriesName));
                    hashXiao.put(category.SourceType, result);
                }
            }
            setXiaoTitle();
            setXiaoType();
        }
    }

    //加载弹窗
    private LoadingDialog loadingDialog;

    private void startLoading(String showMsg) {
//        if (loadingDialog==null){
//            loadingDialog = new LoadingDialog(getActivity());
//            loadingDialog.create();
//        }
//        loadingDialog.setMessage(showMsg);
//        loadingDialog.show();
    }

    private void stopLoading() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }
}