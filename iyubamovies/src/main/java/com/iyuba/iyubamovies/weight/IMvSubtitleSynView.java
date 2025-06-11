package com.iyuba.iyubamovies.weight;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.iyuba.iyubamovies.model.Subtitle;

import java.util.ArrayList;
import java.util.List;

/**
 * 字幕同步View
 */
public class IMvSubtitleSynView extends ScrollView implements
		TextPageSelectTextCallBack {
	private boolean syncho;
	private Context context;
	private LinearLayout subtitleLayout;
	private SubtitleSum subtitleSum;
	private List<View> subtitleViews;
	private int currParagraph, lastParagraph;
	private TextPageSelectTextCallBack tpstcb;
	private boolean enableSelectText = true;
	private int type;// 0 lrc;1 fen;
	private ThehighlightListener listener;
	private boolean ONLY_SHOW_ENGLISH = true;
	public void setTpstcb(TextPageSelectTextCallBack tpstcb) {
		if (enableSelectText)
			this.tpstcb = tpstcb;
	}

	public IMvSubtitleSynView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initWidget(context);
	}

	public IMvSubtitleSynView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		setVerticalScrollBarEnabled(false);
		initWidget(context);
	}

	public IMvSubtitleSynView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		setVerticalScrollBarEnabled(false);
		initWidget(context);
	}

	private void initWidget(Context context) {
		this.context = context;
		subtitleLayout = new LinearLayout(this.context);
		subtitleLayout.setOrientation(LinearLayout.VERTICAL);
	}

	public void setSubtitleSum(SubtitleSum subtitleSum, int type) {
		this.subtitleSum = subtitleSum;
		this.type = type;
		subtitleLayout.removeAllViews();
		removeAllViews();
		initSubtitleSum();
		currParagraph = lastParagraph = 0;
	}

	public void initSubtitleSum() {
		List<Subtitle> subtitles = null;
		if (type == 1) {
			subtitles = subtitleSum.subtitles;
		} else {
			subtitles = subtitleSum.lrcs;
		}
		if (subtitleSum != null && subtitles.size() != 0) {
			subtitleViews = new ArrayList<View>();
			subtitleViews.clear();
			int size = subtitles.size();
			IMvTextPage tp;
			for (int i = 0; i < size; i++) {
				tp = new IMvTextPage(this.context);
				tp.setBackgroundColor(Color.TRANSPARENT);
				tp.setTextColor(Color.BLACK);
				tp.setTextSize(16);
				if(ONLY_SHOW_ENGLISH)
					tp.setText(subtitles.get(i).english);
				else
					tp.setText(subtitles.get(i).content);
				tp.setTextpageSelectTextCallBack(this);
				final int current = i;
				tp.setOnLongClickListener(new OnLongClickListener() {

					@Override
					public boolean onLongClick(View arg0) {
						// TODO Auto-generated method stub
						tpstcb.selectParagraph(current);
						return false;
					}
				});
				subtitleViews.add(tp);
				subtitleLayout.addView(tp);
			}
		}
		addView(subtitleLayout);
	}

	@Override
	public void selectTextEvent(String selectText) {
		tpstcb.selectTextEvent(selectText);
	}

	/**
	 * 段落高亮跳转
	 *
	 * @param paragraph
	 */
	public void snyParagraph(int paragraph) {
		currParagraph = paragraph;
		handler.sendEmptyMessage(0);
	}

	public void unsnyParagraph() {
		handler.removeMessages(0);
	}
	public void ifShowChinese(boolean show){
		this.ONLY_SHOW_ENGLISH = show;
		updateSubtitleView();
	}
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {
				case 0:
					boolean run = subtitleViews != null
							&& subtitleViews.size() != 0 && currParagraph != 0
							&& (currParagraph - 1 < subtitleViews.size());
					if (run) {
						TextView textView = (TextView) subtitleViews
								.get(lastParagraph);
						textView.setTextColor(Color.BLACK);
						textView = (TextView) subtitleViews.get(currParagraph - 1);
						textView.setTextColor(Color.parseColor("#3bc3fb"));
						if(listener!=null){
							if(subtitleSum!=null&&subtitleSum.subtitles!=null&&subtitleSum.subtitles.size()>0)
								listener.OnHighLightListener(subtitleSum.subtitles.get(currParagraph-1).content);
						}
						lastParagraph = currParagraph - 1;
						int center = textView.getTop() + textView.getHeight() / 2;
						if (syncho) {
							center -= getHeight() / 2;
							if (center > 0) {
								smoothScrollTo(0, center);
							} else {
								smoothScrollTo(0, 0);
							}
						}
					}
					break;
				case 1:
					break;
			}
		}

	};

	public void updateSubtitleView() {
		List<Subtitle> subtitles = null;
		if (type == 1) {
			if(subtitleSum!=null&&subtitleSum.subtitles!=null)
			subtitles = subtitleSum.subtitles;
		} else {
			if(subtitleSum!=null&&subtitleSum.lrcs!=null)
			subtitles = subtitleSum.lrcs;
		}
		if (subtitleSum != null && subtitles.size() != 0
				&& subtitleViews != null && subtitleViews.size() != 0) {
			int size = subtitles.size();
			IMvTextPage tp;
			for (int i = 0; i < size; i++) {
				tp = (IMvTextPage) subtitleViews.get(i);
				if(ONLY_SHOW_ENGLISH)
					tp.setText(subtitles.get(i).english);
				else
					tp.setText(subtitles.get(i).content);
			}
			snyParagraph(currParagraph);
		}
	}
	public void setOnHighlightListener(ThehighlightListener thehighlightListener){

		this.listener = thehighlightListener;

	}
	public int getCurrParagraph() {
		return currParagraph;
	}

	@Override
	public void selectParagraph(int paragraph) {
		tpstcb.selectParagraph(paragraph);
	}

	public void setSyncho(boolean syncho) {
		this.syncho = syncho;
	}

	public void clear() {
		if (subtitleViews != null) {
			subtitleViews.clear();
		}
	}
	public interface ThehighlightListener{
		void OnHighLightListener(String text);
	}
}
