package com.iyuba.talkshow.injection.component;

import com.iyuba.talkshow.injection.PerActivity;
import com.iyuba.talkshow.injection.module.ActivityModule;
import com.iyuba.talkshow.ui.about.AboutActivity;
import com.iyuba.talkshow.ui.about.SendBookActivity;
import com.iyuba.talkshow.ui.about.SetActivity;
import com.iyuba.talkshow.ui.courses.wordChoose.WordChooseActivity;
import com.iyuba.talkshow.ui.courses.coursedetail.CourseDetailActivity;
import com.iyuba.talkshow.ui.deletlesson.LessonDeleteActivity;
import com.iyuba.talkshow.ui.detail.DetailActivity;
import com.iyuba.talkshow.ui.detail.ranking.watch.WatchDubbingActivity;
import com.iyuba.talkshow.ui.devsearch.DevSearchActivity;
import com.iyuba.talkshow.ui.feedback.FeedbackActivity;
import com.iyuba.talkshow.ui.list.ListActivity;
import com.iyuba.talkshow.ui.main.MainActivity;
import com.iyuba.talkshow.ui.rank.RankActivity;
import com.iyuba.talkshow.ui.rank.dubbing.DubbingListActivity;
import com.iyuba.talkshow.ui.search.SearchActivity;
import com.iyuba.talkshow.ui.series.SeriesActivity;
import com.iyuba.talkshow.ui.sign.SignActivity;
import com.iyuba.talkshow.ui.user.collect.CollectionActivity;
import com.iyuba.talkshow.ui.user.detail.ShowUserInfoActivity;
import com.iyuba.talkshow.ui.user.download.DownloadActivity;
import com.iyuba.talkshow.ui.user.edit.EditUserInfoActivity;
import com.iyuba.talkshow.ui.user.edit.ImproveUserActivity;
import com.iyuba.talkshow.ui.user.image.UploadImageActivity;
import com.iyuba.talkshow.ui.user.login.ChangeNameActivity;
import com.iyuba.talkshow.ui.user.me.CalendarActivity;
import com.iyuba.talkshow.ui.user.register.email.RegisterActivity;
import com.iyuba.talkshow.ui.user.register.phone.RegisterByPhoneActivity;
import com.iyuba.talkshow.ui.user.register.submit.RegisterSubmitActivity;
import com.iyuba.talkshow.ui.vip.buyiyubi.BuyIyubiActivity;
import com.iyuba.talkshow.ui.vip.buyvip.NewVipCenterActivity;
import com.iyuba.talkshow.ui.vip.payorder.PayOrderActivity;
import com.iyuba.talkshow.ui.wallet.WalletHistoryActivity;
import com.iyuba.talkshow.ui.welcome.WelcomeActivity;
import com.iyuba.talkshow.ui.words.WordNoteActivity;

import dagger.Subcomponent;

/**
 * This component inject dependencies to all Activities across the application
 */
@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity mainActivity);

    void inject(SearchActivity activity);

    void inject(DubbingListActivity dubbingListActivity);

    void inject(SendBookActivity sendBookActivityActivity);

    void inject(ListActivity listActivity);

    void inject(RankActivity rankActivity);

    void inject(SeriesActivity seriesActivity);

    void inject(DetailActivity detailActivity);

    /*void inject(DubbingActivity dubbingActivity);*/

    void inject(DevSearchActivity devSearchActivity);

    void inject(SetActivity setActivity);

    /*void inject(PreviewActivity previewActivity);*/

    void inject(WatchDubbingActivity watchDubbingActivity);

    /*void inject(LoginActivity loginActivity);*/
    void inject(ChangeNameActivity userNameActivity);

    /*void inject(MeActivity meActivity);*/

    void inject(CalendarActivity calendarActivity);

    void inject(WordNoteActivity wordNoteActivity);

    void inject(RegisterActivity registerActivity);

    void inject(RegisterByPhoneActivity registerByPhoneActivity);

    void inject(RegisterSubmitActivity registerSubmitActivity);

    void inject(PayOrderActivity payOrderActivity);

    void inject(WelcomeActivity welcomeActivity);

    void inject(AboutActivity aboutActivity);

    void inject(CourseDetailActivity aboutActivity);

    void inject(FeedbackActivity feedbackActivity);

    void inject(ShowUserInfoActivity showUserInfoActivity);

    void inject(EditUserInfoActivity editUserInfoActivity);
    void inject(ImproveUserActivity improveUserActivity);

    void inject(UploadImageActivity uploadImageActivity);

    void inject(CollectionActivity collectionActivity);

    void inject(DownloadActivity downloadActivity);

    void inject(BuyIyubiActivity buyIyubiActivity);

    void inject(SignActivity signActivity);

    void inject(WordChooseActivity wordChooseActivity);

    /*void inject(WXPayEntryActivity wxPayEntryActivity);*/

    void inject(NewVipCenterActivity newVipCenterActivity);

    void inject(LessonDeleteActivity mainActivity);

    FragmentComponent fragmentComponent();

    DialogComponent dialogComponent();

    //选书界面
     void inject(com.iyuba.talkshow.ui.courses.courseChoose.CourseChooseActivity courseChooseActivity);
    //混合登录界面(秒验、小程序、账号登录)
//    void inject(FixLoginActivity fixLoginActivity);
    //钱包历史记录界面
    void inject(WalletHistoryActivity walletHistoryActivity);
}
