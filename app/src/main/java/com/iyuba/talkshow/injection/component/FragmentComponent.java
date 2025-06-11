package com.iyuba.talkshow.injection.component;

import com.iyuba.talkshow.injection.PerFragment;
import com.iyuba.talkshow.newce.WordstepFragment;
import com.iyuba.talkshow.newview.EvalCorrectPage;
import com.iyuba.talkshow.ui.detail.comment.CommentFragment;
import com.iyuba.talkshow.ui.detail.ranking.RankingFragment;
import com.iyuba.talkshow.ui.detail.recommend.RecommendFragment;
import com.iyuba.talkshow.ui.lil.ui.dubbing.DubbingNewFragment;
import com.iyuba.talkshow.ui.lil.ui.dubbing.preview.PreviewNewFragment;
import com.iyuba.talkshow.ui.main.drawer.SlidingMenuFragment;
import com.iyuba.talkshow.ui.main.home.HomeFragment;
import com.iyuba.talkshow.ui.user.download.DownloadFragment;
import com.iyuba.talkshow.ui.user.me.dubbing.draft.DraftFragment;
import com.iyuba.talkshow.ui.user.me.dubbing.released.ReleasedFragment;
import com.iyuba.talkshow.ui.user.me.dubbing.unreleased.UnreleasedFragment;
import com.iyuba.talkshow.ui.courses.wordChoose.WordChooseFragment;

import dagger.Subcomponent;

@PerFragment
@Subcomponent
public interface FragmentComponent {
    void inject(CommentFragment commentFragment);
    void inject(WordChooseFragment commentFragment);

    void inject(DownloadFragment downloadFragment);

    void inject(RankingFragment rankingFragment);

    void inject(RecommendFragment recommendFragment);

    void inject(SlidingMenuFragment slidingMenuFragment);

    void inject(DraftFragment draftFragment);

    void inject(ReleasedFragment releasedFragment);

    void inject(UnreleasedFragment unreleasedFragment);

    void inject(HomeFragment homeFragment);

    //增加纠音界面
    void inject(EvalCorrectPage evalCorrectPage);
    //增加单词界面
    void inject(WordstepFragment wordstepFragment);
    //增加新的课程选择界面
    void inject(com.iyuba.talkshow.ui.courses.courseChoose.ChooseCourseFragment chooseCourseFragment);

    //增加新的配音界面
    void inject(DubbingNewFragment fragment);
    //增加新的预览界面
    void inject(PreviewNewFragment fragment);
}
