//package com.iyuba.talkshow.ui.courses.wordChoose.backup;
//
//import android.app.AlertDialog;
//import android.content.DialogInterface;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.recyclerview.widget.GridLayoutManager;
//
//import com.iyuba.lib_user.manager.UserInfoManager;
//import com.iyuba.talkshow.R;
//import com.iyuba.talkshow.TalkShowApplication;
//import com.iyuba.talkshow.constant.App;
//import com.iyuba.talkshow.constant.ConfigData;
//import com.iyuba.talkshow.data.manager.AbilityControlManager;
//import com.iyuba.talkshow.data.model.SeriesData;
//import com.iyuba.talkshow.data.model.Voa;
//import com.iyuba.talkshow.databinding.FragmentCoursechooseBinding;
//import com.iyuba.talkshow.event.RefreshBookEvent;
//import com.iyuba.talkshow.event.SelectBookEvent;
//import com.iyuba.talkshow.injection.PerFragment;
//import com.iyuba.talkshow.ui.base.BaseViewBindingFragmet;
//import com.iyuba.talkshow.ui.courses.common.OpenFlag;
//import com.iyuba.talkshow.ui.courses.common.TypeHolder;
//import com.iyuba.talkshow.ui.courses.coursedetail.CourseDetailActivity;
//import com.iyuba.talkshow.ui.courses.wordChoose.WordChooseAdapter;
//import com.iyuba.talkshow.ui.courses.wordChoose.WordChooseMVPView;
//import com.iyuba.talkshow.ui.courses.wordChoose.WordChoosePresenter;
//import com.iyuba.talkshow.ui.courses.wordChoose.WordTitleAdapter;
//import com.iyuba.talkshow.ui.courses.wordChoose.WordTypeAdapter;
//import com.iyuba.talkshow.ui.courses.wordChoose.WordTypeHelper;
//import com.iyuba.talkshow.ui.courses.wordChoose.bean.Series;
//import com.iyuba.talkshow.ui.lil.view.LoadingNewDialog;
//import com.iyuba.talkshow.ui.lil.view.recyclerView.NoScrollGridLayoutManager;
//import com.iyuba.talkshow.util.NetStateUtil;
//import com.iyuba.talkshow.util.ToastUtil;
//import com.iyuba.wordtest.db.BookLevelDao;
//import com.iyuba.wordtest.db.CategorySeriesDao;
//import com.iyuba.wordtest.db.NewBookLevelDao;
//import com.iyuba.wordtest.db.WordDataBase;
//import com.iyuba.wordtest.entity.BookLevels;
//import com.iyuba.wordtest.entity.CategorySeries;
//import com.iyuba.wordtest.entity.NewBookLevels;
//import com.iyuba.wordtest.manager.WordManager;
//import com.scwang.smartrefresh.layout.api.RefreshLayout;
//import com.scwang.smartrefresh.layout.header.ClassicsHeader;
//import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
//
//import org.greenrobot.eventbus.EventBus;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.HashMap;
//import java.util.List;
//
//import javax.inject.Inject;
//
//
///**
// * 备份文件-之前修改过的选书界面，需要修改回来
// */
//@PerFragment
//public class WordChooseFragment_backup extends BaseViewBindingFragmet<FragmentCoursechooseBinding> implements WordChooseMVPView {
//
//    String catId ;
//    WordChooseAdapter adapter  ;
//    WordTypeAdapter wordTypeAdapter;
//    WordTitleAdapter wordTitleAdapter;
//    List<TypeHolder> listTitle = new ArrayList<>();
//    List<TypeHolder> list = new ArrayList<>();
//    public static final String CATID = "CATID";
//    public static final String FLAG = "FLAG";
//    public  static final String TYPE = "TYPE";
//    List<SeriesData> dataBeans  = new ArrayList<>();
//    @Inject
//    WordChoosePresenter mPresenter ;
//    private int flag;
//    private int type;
//    HashMap<String, List<TypeHolder>> hashXiao = new HashMap();
//    WordDataBase db;
//    CategorySeriesDao categoryDao;
//
//    FragmentCoursechooseBinding binding ;
//
//    public static WordChooseFragment_backup build(String catId, int flag, int type) {
//        WordChooseFragment_backup fragment  = new WordChooseFragment_backup() ;
//        Bundle bundle  = new Bundle( );
//        bundle.putString(CATID, catId);
//        bundle.putInt(FLAG, flag);
//        bundle.putInt(TYPE, type);
//        fragment.setArguments(bundle);
//        return fragment ;
//    }
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        fragmentComponent().inject(this);
//        mPresenter.attachView(this);
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        binding = FragmentCoursechooseBinding.inflate(getLayoutInflater(),container,false);
//        return binding.getRoot();
//    }
//
//    public WordChooseFragment_backup() {
//
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        mPresenter.detachView();
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        db = WordDataBase.getInstance(TalkShowApplication.getContext());
//        categoryDao = db.getCategorySeriesDao();
//
//        //这里是原来获取类型数据的地方
//        getLocalTypeData();
//
//        type = getArguments().getInt(TYPE);
//        catId  = getArguments().getString(CATID);
//        flag  = getArguments().getInt(FLAG);
//
//        //书籍展示
//        binding.recycler.setLayoutManager(new NoScrollGridLayoutManager(mActivity,3,false));
//        adapter = new WordChooseAdapter(dataBeans,flag);
//        adapter.setVoaCallback(callback);
//        binding.recycler.setAdapter(adapter);
//
//        //大类型展示
//        wordTypeAdapter = new WordTypeAdapter(list);
//        wordTypeAdapter.setCallback(cousreCallback);
//        binding.recyclertype.setLayoutManager(new GridLayoutManager(mActivity,5));
//        binding.recyclertype.setAdapter(wordTypeAdapter);
//        if (list == null || list.size() < 1) {
//            binding.recyclertype.setVisibility(View.GONE);
//        }
//
//        //小类型展示
//        wordTitleAdapter = new WordTitleAdapter(listTitle);
//        wordTitleAdapter.setCallback(titleCallback);
//        binding.recyclertitle.setLayoutManager(new GridLayoutManager(mActivity,5));
//        binding.recyclertitle.setAdapter(wordTitleAdapter);
//
//        if (type == OpenFlag.TO_WORD) {
//            wordTitleAdapter.putActiveTitle(mPresenter.getWordClass());
//            wordTypeAdapter.putActiveType(mPresenter.getWordType());
//        } else {
//            wordTitleAdapter.putActiveTitle(mPresenter.getCourseClass());
//            wordTypeAdapter.putActiveType(mPresenter.getCourseType());
//        }
//
//        if (NetStateUtil.isConnected(TalkShowApplication.getContext())) {
//            //更换为下边的方法
////            mPresenter.getSeriesList(catId);
//            //原来使用的是下边这个方法
//            mPresenter.getShowCourseList(catId);
//            setUseMethod(mPresenter.getLessonType());
//        } else {
//            ToastUtil.showToast(mContext, "选择课程需要打开数据网络。");
//            //设置默认的数据
//            mPresenter.chooseCourse(catId);
//        }
//
//
//        //增加下拉刷新内容
//        binding.refreshLayout.setEnableLoadMore(false);
//        binding.refreshLayout.setEnableRefresh(true);
//        binding.refreshLayout.setRefreshHeader(new ClassicsHeader(getActivity()));
//        binding.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
//            @Override
//            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
//                mPresenter.getSeriesList(catId);
//            }
//        });
//    }
//
//    private void setTextArea(String catId) {
//        Log.e("ChooseCoursePresenter", "setTextArea catId " + catId);
//        binding.tvArea.setVisibility(View.VISIBLE);
//        switch (catId) {
//            case "316":
//                binding.tvArea.setText(R.string.area_renjiao);
//                break;
//            case "330":
//                binding.tvArea.setText(R.string.area_waiyan);
//                break;
//            case "331":
//                binding.tvArea.setText(R.string.area_beishi);
//                break;
//            case "332":
//                binding.tvArea.setText(R.string.area_renai);
//                break;
//            case "333":
//                binding.tvArea.setText(R.string.area_jiban);
//                break;
//            case "334":
//                binding.tvArea.setText(R.string.area_yilin);
//                break;
//            case "335":
//                binding.tvArea.setText(R.string.area_lujiao);
//                break;
//            default:
//                binding.tvArea.setVisibility(View.GONE);
//                break;
//        }
//    }
//
//    private class SortBySeries implements Comparator {
//        @Override
//        public int compare(Object o1, Object o2) {
//            SeriesData s1 = (SeriesData) o1;
//            SeriesData s2 = (SeriesData) o2;
//            if (Integer.parseInt(s1.getId()) > Integer.parseInt(s2.getId())) //小的在前面
//                return 1;
//            return -1;
//        }
//    }
//
//    @Override
//    public void setMoreCourse(List<SeriesData> beans) {
//        //关闭加载
//        binding.refreshLayout.finishRefresh();
//
//        if (beans == null || beans.size() < 1) {
//            adapter.setDataBeans(beans);
//            return;
//        }
//        if (((type == OpenFlag.TO_WORD)) && "337".equals(catId)) {
////            List<SeriesData> newBeans = new ArrayList<>();
////            for (SeriesData series : beans) {
////                if ((series != null) && (450 <= Integer.parseInt(series.getId())) && (Integer.parseInt(series.getId()) <= 453)) {
////                    series.setSeriesName(series.getSeriesName().replace("(美音)", ""));
////                    newBeans.add(series);
////                }
////            }
//            Collections.sort(beans, new SortBySeries());
//            adapter.setDataBeans(beans);
//        } else {
//            Collections.sort(beans, new SortBySeries());
//            adapter.setDataBeans(beans);
//        }
//    }
//
//    @Override
//    public void setCoures(List<SeriesData> beans) {
//        //关闭加载
//        binding.refreshLayout.finishRefresh();
//
//        if (beans == null || beans.size() < 1) {
//            mPresenter.chooseCourse(catId);
//            return;
//        }
//
//        if (((type == OpenFlag.TO_WORD)) && "337".equals(catId)) {
////            List<SeriesData> newBeans = new ArrayList<>();
////            for (SeriesData series : beans) {
////                if ((series != null) && (450 <= Integer.parseInt(series.getId())) && (Integer.parseInt(series.getId()) <= 453)) {
////                    series.setSeriesName(series.getSeriesName().replace("(美音)", ""));
////                    newBeans.add(series);
////                }
////            }
//            Collections.sort(beans, new SortBySeries());
//            adapter.setDataBeans(beans);
//        } else {
//            Collections.sort(beans, new SortBySeries());
//            adapter.setDataBeans(beans);
//        }
//    }
//
//    @Override
//    public void setLesson(List<Series> series) {
//        if (series == null || series.size() < 1) {
//            return;
//        }
//        if (mPresenter.getLessonType().equals(ConfigData.default_word_show_type)) {
//            setXiaoLesson(series);
//        } else {
////            setChuLesson(series);
//            setXiaoLesson(series);
//        }
//
//        //展示课程数据
//        /*catId = String.valueOf(series.get(0).SeriesData.get(0).Category);
//        mPresenter.getShowCourseList(catId);*/
//    }
//
//    @Override
//    public void setLessonFail(String showMsg) {
//        //显示默认的类型
////        getLocalTypeData();
//    }
//
//    private void setChuLesson(List<Series> series) {
//        Log.e("ChooseCoursePresenter", "setLesson series.size() " + series.size());
//        categoryDao.deleteCategory(UserInfoManager.getInstance().getUserId());
//        List<TypeHolder> newTitle = new ArrayList<>();
//        for (int i = 0; i < series.size(); i++) {
//            Series ser = series.get(i);
//            if (ser == null) {
//                continue;
//            }
//            for (Series.SeriesDatas seriesData: ser.SeriesData) {
//                if (seriesData == null) {
//                    continue;
//                }
//                if (App.APP_TENCENT_MOOC) {
//                    if (WordTypeHelper.TYPE_OPPO_RENJIAO) {
//                        newTitle.add(new TypeHolder(seriesData.Category,seriesData.SeriesName));
//                    } else {
//                        if (!"人教版".equals(seriesData.SeriesName)) {
//                            newTitle.add(new TypeHolder(seriesData.Category,seriesData.SeriesName));
//                        }
//                    }
//                } else {
//                    if (!"新概念".equals(seriesData.SeriesName)) {
//                        newTitle.add(new TypeHolder(seriesData.Category,seriesData.SeriesName));
//                    }
//                }
//                CategorySeries categorySer = categoryDao.getUidCategory(seriesData.Category, UserInfoManager.getInstance().getUserId());
//                if (categorySer == null) {
//                    categoryDao.saveCategory(new CategorySeries(seriesData.Category, seriesData.SeriesName, seriesData.lessonName, ser.SourceType, seriesData.isVideo, UserInfoManager.getInstance().getUserId()));
//                } else {
//                    categoryDao.updateCategory(new CategorySeries(seriesData.Category, seriesData.SeriesName, seriesData.lessonName, ser.SourceType, seriesData.isVideo, UserInfoManager.getInstance().getUserId()));
//                }
//            }
//        }
//        Log.e("ChooseCoursePresenter", "setLesson newTitle " + newTitle.size());
//        if (newTitle.size() < 3) {
//            listTitle.clear();
//            listTitle.addAll(newTitle);
//            wordTitleAdapter.SetTitleList(listTitle);
//            wordTitleAdapter.notifyDataSetChanged();
//            binding.recyclertype.setVisibility(View.GONE);
//            return;
//        }
//        binding.recyclertype.setVisibility(View.VISIBLE);
//        int middle = newTitle.size()/2;
//        listTitle.clear();
//        for (int i = 0; i < middle; i++) {
//            listTitle.add(newTitle.get(i));
//        }
//        wordTitleAdapter.SetTitleList(listTitle);
//        wordTitleAdapter.notifyDataSetChanged();
//        list.clear();
//        for (int i = middle; i < newTitle.size(); i++) {
//            list.add(newTitle.get(i));
//        }
//        wordTypeAdapter.SetCourseList(list);
//        wordTypeAdapter.notifyDataSetChanged();
//    }
//
//    private void setXiaoLesson(List<Series> series) {
//        categoryDao.deleteCategory(UserInfoManager.getInstance().getUserId());
//        listTitle.clear();
//        list.clear();
//        hashXiao.clear();
//        for (Series ser: series) {
//            if (ser == null) {
//                continue;
//            }
//
//            //人教版审核限制
//            if (AbilityControlManager.getInstance().isLimitPep()&&ser.SourceType.equals("人教版")){
//                continue;
//            }
//
//            for (Series.SeriesDatas seriesData: ser.SeriesData) {
//                if (seriesData == null) {
//                    continue;
//                }
//
//                if (hashXiao.containsKey(ser.SourceType)) {
//                    List<TypeHolder> result = hashXiao.get(ser.SourceType);
//                    result.add(new TypeHolder(seriesData.Category, seriesData.SeriesName));
//                } else {
//                    List<TypeHolder> result = new ArrayList<>();
//                    result.add(new TypeHolder(seriesData.Category, seriesData.SeriesName));
//                    hashXiao.put(ser.SourceType, result);
//                }
//                CategorySeries categorySer = categoryDao.getUidCategory(seriesData.Category, UserInfoManager.getInstance().getUserId());
//                if (categorySer == null) {
//                    categoryDao.saveCategory(new CategorySeries(seriesData.Category, seriesData.SeriesName, seriesData.lessonName, ser.SourceType, seriesData.isVideo, UserInfoManager.getInstance().getUserId()));
//                } else {
//                    categoryDao.updateCategory(new CategorySeries(seriesData.Category, seriesData.SeriesName, seriesData.lessonName, ser.SourceType, seriesData.isVideo, UserInfoManager.getInstance().getUserId()));
//                }
//            }
//        }
//        setXiaoTitle();
//        setXiaoType();
//        wordTitleAdapter.SetTitleList(listTitle);
//        wordTypeAdapter.SetCourseList(list);
//        wordTitleAdapter.notifyDataSetChanged();
//        wordTypeAdapter.notifyDataSetChanged();
//        //设置显示
//        binding.recyclertype.setVisibility(View.VISIBLE);
//        binding.recyclertitle.setVisibility(View.VISIBLE);
//    }
//
//    private void setXiaoTitle() {
//        int index = 0;
//        if (hashXiao == null || hashXiao.size() < 1) {
//            return;
//        }
//
//        //人教版审核限制
//        if (AbilityControlManager.getInstance().isLimitPep()){
//            ++index;
//        }
//
//        if (hashXiao.containsKey("人教版")) {
//            listTitle.add(new TypeHolder(WordTypeHelper.TYPE_PRIMARY_RENJIAO, "人教版"));
//            ++index;
//        }
//        if (hashXiao.containsKey("北师版")) {
//            listTitle.add(new TypeHolder(WordTypeHelper.TYPE_PRIMARY_BEISHI, "北师版"));
//            ++index;
//        }
//        if (hashXiao.containsKey("北京版")) {
//            listTitle.add(new TypeHolder(WordTypeHelper.TYPE_PRIMARY_BEIJING, "北京版"));
//            ++index;
//        }
//        for (String key: hashXiao.keySet()) {
//            Log.e("ChooseCoursePresenter", "setXiaoTitle key " + key);
//            switch (key) {
//                case "人教版":
//                case "北师版":
//                case "北京版":
//                    break;
//                case "新概念":
//                    if (App.APP_TENCENT_MOOC) {
//                        listTitle.add(new TypeHolder(index, key));
//                        ++index;
//                    }
//                    break;
//                default:
//                    listTitle.add(new TypeHolder(index, key));
//                    ++index;
//                    break;
//            }
//        }
//    }
//
//    private void setXiaoType() {
//        if (hashXiao == null || hashXiao.size() < 1) {
//            if (type == OpenFlag.TO_WORD) {
//                list = WordTypeHelper.getXiaoxueType(mPresenter.getWordClass());
//            } else {
//                list = WordTypeHelper.getXiaoxueType(mPresenter.getCourseClass());
//            }
//            return;
//        }
//        list.clear();
//        int index = 0;
//        if (type == OpenFlag.TO_WORD) {
//            index = mPresenter.getWordClass();
//        } else {
//            index = mPresenter.getCourseClass();
//        }
//
//        //人教版审核限制
//        if (AbilityControlManager.getInstance().isLimitPep()&&hashXiao.keySet().size()==3){
//            index+=1;
//        }
//
//        for (String key: hashXiao.keySet()) {
//            for (TypeHolder typeHolder: listTitle) {
//                if ((index == typeHolder.getId()) && key.equals(typeHolder.getValue())) {
//                    list.addAll(hashXiao.get(key));
//                    return;
//                }
//            }
//        }
//    }
//
//    @Override
//    public void showToastShort(int resId) {
//        ToastUtil.showToast(mActivity, getResources().getString(resId));
//    }
//
//    @Override
//    public void showToastShort(String message) {
//        ToastUtil.showToast(mActivity, message);
//    }
//
//    @Override
//    public void showToastLong(int resId) {
//        ToastUtil.showToast(mActivity, getResources().getString(resId));
//    }
//
//    @Override
//    public void showToastLong(String message) {
//        ToastUtil.showToast(mActivity, message);
//    }
//
//    WordChooseAdapter.CourseCallback callback = new WordChooseAdapter.CourseCallback() {
//        @Override
//        public void onCourseClicked(int bookId, int count, int category, String courseTitle) {
//            if (type == OpenFlag.TO_WORD) {
//                mPresenter.putWordId(bookId, courseTitle);
//                mPresenter.putWordCategory(category);
//            } else {
//                mPresenter.putCourseId(bookId, courseTitle);
//                mPresenter.putCourseCategory(category);
//            }
//            Log.e("ChooseCourseFragment", "onCourseClicked bookId " + bookId);
//            BookLevelDao bookLevelDao = WordDataBase.getInstance(TalkShowApplication.getInstance()).getBookLevelDao();
//            int version = 0;
//            if (WordManager.WordDataVersion == 2) {
//                NewBookLevelDao newLevelDao = WordDataBase.getInstance(TalkShowApplication.getInstance()).getNewBookLevelDao();
//                if (bookLevelDao != null) {
//                    NewBookLevels newLevel = newLevelDao.getBookLevel(bookId, String.valueOf(UserInfoManager.getInstance().getUserId()));
//                    if (newLevel == null) {
//                        TalkShowApplication.getSubHandler().post(new Runnable() {
//                            @Override
//                            public void run() {
//                                NewBookLevels levels = new NewBookLevels(bookId, 0, 0, 0, String.valueOf(UserInfoManager.getInstance().getUserId()));
//                                newLevelDao.saveBookLevel(levels);
//                            }
//                        });
//                    } else {
//                        version = newLevel.version;
//                    }
//                }
//            } else
//            if (bookLevelDao != null) {
//                BookLevels bookLevels = bookLevelDao.getBookLevel(bookId);
//                if (bookLevels == null) {
//                    TalkShowApplication.getSubHandler().post(new Runnable() {
//                        @Override
//                        public void run() {
//                            BookLevels levels = new BookLevels(bookId, 0, 0, 0);
//                            bookLevelDao.saveBookLevel(levels);
//                        }
//                    });
//                } else {
//                    version = bookLevels.version;
//                }
//            }
//            if (type == OpenFlag.TO_WORD) {
//                if (version == 0) {
//                    EventBus.getDefault().post(new RefreshBookEvent(bookId,version,true));
//                } else {
//                    EventBus.getDefault().post(new RefreshBookEvent(bookId,version));
//                }
//            } else {
//                List<Voa> result = mPresenter.loadVoasByBookId(bookId);
//                if (result == null || result.size() < count) {
//                    EventBus.getDefault().post(new SelectBookEvent(bookId,version, true));
//                } else {
//                    EventBus.getDefault().post(new SelectBookEvent(bookId,version));
//                }
//            }
//            if (flag == OpenFlag.TO_DETAIL) {
//                if (type == OpenFlag.TO_WORD) {
//                    mActivity.startActivity(CourseDetailActivity.buildIntent(mActivity, bookId, mPresenter.getWordTitle()));
//                } else {
//                    mActivity.startActivity(CourseDetailActivity.buildIntent(mActivity, bookId, mPresenter.getCourseTitle()));
//                }
//            }else if (flag == OpenFlag.TO_WORD){
////                startActivity(WordStepActivity.buildIntent(mContext,bookId,courseTitle));
//                mActivity.finish();
//            } else {
//                mActivity.finish();
//            }
//        }
//
//        @Override
//        public void onCourseLongClicked(int series) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
//            builder.setMessage("是否删除此书下的视频？");
//            builder.setPositiveButton("是的", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    mPresenter.deletCourses(series);
//                }
//            });
//            builder.setCancelable(true);
//            builder.show();
//        }
//    };
//    WordTypeAdapter.Callback cousreCallback = new WordTypeAdapter.Callback() {
//        @Override
//        public void onTypeChoose(int pos, int id) {
//            Log.e("ChooseCoursePresenter", "onTypeChoose pos = " + pos);
//            Log.e("ChooseCoursePresenter", "onTypeChoose id = " + id);
//            if (App.APP_ID == 259) {
//                if (type == OpenFlag.TO_WORD) {
//                    mPresenter.putWordClass(-1);
//                    mPresenter.putWordType(pos);
//                    mPresenter.putWordCategory(id);
//                    wordTitleAdapter.putActiveTitle(-1);
//                    wordTypeAdapter.putActiveType(pos);
//                } else {
//                    mPresenter.putCourseClass(-1);
//                    mPresenter.putCourseType(pos);
//                    mPresenter.putCourseCategory(id);
//                    wordTitleAdapter.putActiveTitle(-1);
//                    wordTypeAdapter.putActiveType(pos);
//                }
//                catId = "" + id;
//
//                //更换为下边的方法
////                mPresenter.getSeriesList(catId);
//                mPresenter.getShowCourseList(catId);
//
//                setTextArea(catId);
//                return;
//            }
//            if (type == OpenFlag.TO_WORD) {
//                mPresenter.putWordType(pos);
////                mPresenter.putWordCategory(id);
//            } else {
//                mPresenter.putCourseType(pos);
////                mPresenter.putCourseCategory(id);
//            }
//            catId = "" + id;
//
//            //更换为下边的方法
////            mPresenter.getSeriesList(catId);
//            mPresenter.getShowCourseList(catId);
//        }
//    };
//    WordTitleAdapter.TitleCallback titleCallback = new WordTitleAdapter.TitleCallback() {
//        @Override
//        public void onTitleChoose(int pos, int id) {
//            if (type == OpenFlag.TO_WORD) {
//                mPresenter.putWordCategory(id);
//                mPresenter.putWordClass(pos);
//                mPresenter.putWordType(-1);
//                wordTitleAdapter.putActiveTitle(pos);
//                wordTypeAdapter.putActiveType(-1);
//            } else {
//                mPresenter.putCourseCategory(id);
//                mPresenter.putCourseClass(pos);
//                mPresenter.putCourseType(-1);
//                wordTitleAdapter.putActiveTitle(pos);
//                wordTypeAdapter.putActiveType(-1);
//            }
//            catId = "" + id;
//
//            //更换为下边的方法
////                mPresenter.getSeriesList(catId);
//            mPresenter.getShowCourseList(catId);
//
//            setTextArea(catId);
//        }
//    };
//
//    @Override
//    public void init() {
//
//    }
//
//    //刷新课程信息
//    public void refreshLessonData(String lessonType){
//        //刷新课程数据的时候，默认获取第一个
//        setUseCatId(lessonType);
//
//        if (NetStateUtil.isConnected(TalkShowApplication.getContext())) {
//            //更换为下边的方法
////            mPresenter.getSeriesList(catId);
//            mPresenter.getShowCourseList(catId);
//
//            setUseMethod(lessonType);
//        } else {
//            ToastUtil.showToast(mContext, "选择课程需要打开数据网络。");
//            mPresenter.chooseCourse(catId);
//        }
//    }
//
//    //设置执行的课程id
//    private void setUseCatId(String lessonType){
//        if (lessonType.equals("primary")){
//            catId = "313";
//        }else if (lessonType.equals("junior")){
//            catId = "316";
//        }
//    }
//
//    //设置需要的方法
//    private void setUseMethod(String lessonType){
//        if (lessonType.equals("primary")){
//            mPresenter.choosePrimaryLessonNew(lessonType);
//        }else if (lessonType.equals("junior")){
//            mPresenter.chooseJuniorLessonNew(lessonType);
//        }
//    }
//
//    //从本地获取类型数据
//    private void getLocalTypeData(){
//        List<CategorySeries> newTitle = categoryDao.getUidCategories(UserInfoManager.getInstance().getUserId());
//        if ((newTitle == null) || (newTitle.size() < 1)) {
//            listTitle = WordTypeHelper.getXiaoxueTitle(WordTypeHelper.TYPE_PRIMARY_BEIJING);
//            if (type == OpenFlag.TO_WORD) {
//                list = WordTypeHelper.getXiaoxueType(mPresenter.getWordClass());
//            } else {
//                list = WordTypeHelper.getXiaoxueType(mPresenter.getCourseClass());
//            }
//        } else {
//            listTitle.clear();
//            list.clear();
//            hashXiao.clear();
//            for (int i = 0; i < newTitle.size(); i++) {
//                CategorySeries category = newTitle.get(i);
//                if (hashXiao.containsKey(category.SourceType)) {
//                    List<TypeHolder> result = hashXiao.get(category.SourceType);
//                    result.add(new TypeHolder(newTitle.get(i).Category, newTitle.get(i).SeriesName));
//                } else {
//                    List<TypeHolder> result = new ArrayList<>();
//                    result.add(new TypeHolder(newTitle.get(i).Category, newTitle.get(i).SeriesName));
//                    hashXiao.put(category.SourceType, result);
//                }
//            }
//            setXiaoTitle();
//            setXiaoType();
//        }
//
//        /*if (list!=null&&list.size()>0){
//            wordTypeAdapter.notifyDataSetChanged();
//            binding.recyclertype.setVisibility(View.VISIBLE);
//        }
//        if (listTitle!=null&&listTitle.size()>0){
//            wordTitleAdapter.notifyDataSetChanged();
//            binding.recyclertitle.setVisibility(View.VISIBLE);
//
//            //展示课程数据
//            catId = String.valueOf(listTitle.get(0).getId());
//            mPresenter.getShowCourseList(catId);
//            binding.recycler.setVisibility(View.VISIBLE);
//        }*/
//    }
//
//    //显示加载弹窗
//    private LoadingNewDialog loadingNewDialog;
//
//    private void startLoading(String showMsg){
//        if (loadingNewDialog==null){
//            loadingNewDialog = new LoadingNewDialog(getActivity());
//            loadingNewDialog.create();
//        }
//        loadingNewDialog.setMsg(showMsg);
//        loadingNewDialog.show();
//    }
//
//    private void stopLoading(){
//        if (loadingNewDialog!=null && loadingNewDialog.isShowing()){
//            loadingNewDialog.cancel();
//        }
//    }
//}
