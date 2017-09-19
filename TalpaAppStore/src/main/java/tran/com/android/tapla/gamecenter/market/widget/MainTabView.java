package tran.com.android.tapla.gamecenter.market.widget;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import tran.com.android.tapla.gamecenter.R;
import tran.com.android.tapla.gamecenter.market.activity.module.AppListActivity;


public class MainTabView extends FrameLayout implements OnClickListener {

	private LinearLayout bg;

	private MainTabItemView mtiv_new;
//	private MainTabItemView mtiv_special;
	private MainTabItemView mtiv_ranking;
//	private MainTabItemView mtiv_category;

	private MainTabItemView mtiv_play;
	
	private int height;
	private int bgHeight;
	private int maintabMaxHeight;
	private int maintabMinHeight;
	private int mActionBarHeight;
	private int tempHeight;
	private int scrollY;
	private float mProgress;

	private View lineBottom;

	public MainTabView(Context context) {
		super(context);
		initView();
	}

	public MainTabView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}
	
	public MainTabView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	private void initView() {
		mActionBarHeight = getResources().getDimensionPixelSize(
				R.dimen.aurora_action_bar_search_view_height);

		LayoutInflater inflater = LayoutInflater.from(getContext());
		View view = inflater.inflate(R.layout.view_main_tab, this);

		bg = (LinearLayout) view.findViewById(R.id.bg);
		mtiv_new = (MainTabItemView) view.findViewById(R.id.mtiv_new);
//		mtiv_special = (MainTabItemView) view.findViewById(mtiv_special);
		mtiv_ranking = (MainTabItemView) view.findViewById(R.id.mtiv_ranking);
//		mtiv_category = (MainTabItemView) view.findViewById(mtiv_category);

		mtiv_play = (MainTabItemView) view.findViewById(R.id.mtiv_play);

		lineBottom=  view.findViewById(R.id.line_bottom);


		maintabMaxHeight = getResources().getDimensionPixelOffset(R.dimen.homepage_main_tab_height_max);
		maintabMinHeight = getResources().getDimensionPixelOffset(R.dimen.homepage_main_tab_height_min2);
		tempHeight = getResources().getDimensionPixelOffset(R.dimen.homepage_main_tab_temp_top);
		
		mtiv_new.setOnClickListener(this);
		mtiv_play.setOnClickListener(this);
//		mtiv_special.setOnClickListener(this);
		mtiv_ranking.setOnClickListener(this);
//		mtiv_category.setOnClickListener(this);
	}
	
	/**
	 * @Title: setProgress
	 * @Description: 设置动画进度
	 * @param @param progress
	 * @return void
	 * @throws
	 */
	public void setProgress(float progress, int dy) {

		if(scrollY != dy){
			scrollY = dy;
			setTranslationY(-dy);
		}

		if(progress != mProgress){
			mProgress = progress;
			if(progress == 1){
				bg.setAlpha(progress);
				lineBottom.setVisibility(VISIBLE);
			}else{
				bg.setAlpha(0);
				lineBottom.setVisibility(GONE);
			}
		}

//		if (height == 0) {
//			height = getHeight();
//		}
//		if (bgHeight == 0) {
//			bgHeight = bg.getHeight();
//		}

//		float translationY = (height - maintabMinHeight) * progress;
//		setTranslationY(-translationY);
//
//		int temp = 0;
//		if (progress <= 0.98) {
//			temp = tempHeight;
//		} else {
//			temp = 0;
//		}
//
//		bg.setTranslationY((maintabMaxHeight - maintabMinHeight - temp) * progress);
//
//		android.view.ViewGroup.LayoutParams params = bg.getLayoutParams();
//		params.height = (int) (bgHeight - (maintabMaxHeight - maintabMinHeight - temp) * progress);
//		bg.setLayoutParams(params);
//
		mtiv_play.setFast(0.2f);
		mtiv_new.setFast(0.1f);
//		mtiv_special.setFast(0.2f);
		mtiv_ranking.setFast(0.0f);
//		mtiv_category.setFast(0.0f);

		mtiv_play.setProgress(dy, 0);
		mtiv_new.setProgress(dy, 1);
//		mtiv_special.setProgress(progress);
		mtiv_ranking.setProgress(dy, 2);
//		mtiv_category.setProgress(progress);




//		if( mActionBarHeight <= dy || dy <= bannerViewPagerHeight){
//			if(scrollY != dy){
//				scrollY = dy;
//				progress = scrollY * 1.0f / bannerViewPagerHeight;
//
//				view.setTranslationY(-dy);
//				view.setAlpha(1-progress);
//				int dx = Math.round(bannerWidth * (1-progress));
//				bannerViewPager.getLayoutParams().width = dx;
//				bannerViewPager.requestLayout();
//			}
//		}else{
//			view.setTranslationY(-dy);
//		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.mtiv_new:
			Intent newIntent = new Intent(getContext(),
					AppListActivity.class);
			newIntent.putExtra(AppListActivity.OPEN_TYPE, AppListActivity.TYPE_NEW);
			getContext().startActivity(newIntent);
			break;
		case R.id.mtiv_ranking:
//			Intent rankingIntent = new Intent(getContext(),
//					AppRankingActivity.class);
//			getContext().startActivity(rankingIntent);
			Intent rankingIntent = new Intent(getContext(),
					AppListActivity.class);
			rankingIntent.putExtra(AppListActivity.OPEN_TYPE, AppListActivity.TYPE_RANK);
			getContext().startActivity(rankingIntent);
			break;
		case R.id.mtiv_play:
			Intent playIntent = new Intent(getContext(),
				AppListActivity.class);
			playIntent.putExtra(AppListActivity.OPEN_TYPE, AppListActivity.TYPE_STARTER);
			getContext().startActivity(playIntent);
			break;
//		case mtiv_category:
//			Intent categoryIntent = new Intent(getContext(),
//					CategoryActivity.class);
//			getContext().startActivity(categoryIntent);
//			break;
// 		case mtiv_special:
//			Intent specialIntent = new Intent(getContext(),
//					SpecialActivity.class);
//			getContext().startActivity(specialIntent);
//			break;

		}
	}

}
