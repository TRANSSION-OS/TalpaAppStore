package tran.com.android.tapla.gamecenter.market.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.Rect;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.RemotableViewMethod;
import android.view.TouchDelegate;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import tran.com.android.gc.lib.app.AuroraAlertDialog;

import tran.com.android.talpa.app_core.log.LogPool;
import tran.com.android.tapla.gamecenter.R;
import tran.com.android.tapla.gamecenter.market.util.CustomAnimCallBack;
import tran.com.android.tapla.gamecenter.market.util.SystemUtils;

public class ProgressBtn extends LinearLayout {

	private final String TAG = "ProgressBtn";
	public static final int STATUS_NORMAL = 1;  //正常下载状态
	public static final int STATUS_WAIT_DOWNLOAD = 2; //等待下载状态
	public static final int STATUS_PROGRESSING_DOWNLOAD = 3; //正在下载状态
	public static final int STATUS_WAIT_INSTALL = 4; //等待安装状态
	public static final int STATUS_PROGRESSING_INSTALLING = 5; //安装状态
	public static final int STATUS_FOUCE = 6;
	public static final int STATUS_FOUCE_NORMAL = 7;
	public static final int STATUS_DOWNLOAD_FAIL = 8;
	public static final int STAUTS_DOWNLOAD_CONTINUE = 9;

	private int status = STATUS_NORMAL;

	private float startShow = 0.9f;
	private static int progressTime = 500;

	private FrameLayout progressBtn;
	private ProgressBar progressBar;
	private TextView progressText;

	private int waitingColor = Color.parseColor("#8D8D8D");
	private int redColor = Color.parseColor("#FF535D");

//	private Button btn;
//	private Button btn_backup;
//	private Button progress_btn;
//	private ToCircleView tcv_toProgress;
//	private RoundProgressView round_progress_view;
//	private ImageView iv_progress1;
//	private ImageView iv_progress2;
//	private AuroraGlassView agv1;
//	private AuroraGlassView agv2;
//	private ToCircleView tcv_toRect;
//	private Button fouceBtn;
//	private Button fouceBtn_backup;

	private float textSize;

	private int progress = 0;
	private boolean isRuningStartAnim = false;
	private boolean isRuningEndAnim = false;
	private OnClickListener onButtonClickListener = null;
	private OnClickListener onNormalClickListener = null;
	//private OnAnimListener onBeginAnimListener;

	public ProgressBtn(Context context) {
		super(context);
		initView();
	}

	public ProgressBtn(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}
	
	@Override
	@RemotableViewMethod
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		
//		if (btn != null) {
//			btn.setEnabled(enabled);
//		}
//		if (btn_backup != null) {
//			btn_backup.setEnabled(enabled);
//		}
//		if (fouceBtn != null) {
//			fouceBtn.setEnabled(enabled);
//		}
//		if (fouceBtn_backup != null) {
//			fouceBtn_backup.setEnabled(enabled);
//		}
//		if( progress_btn != null){
//		    progress_btn.setEnabled(enabled);
//		}
	}

	private void initView() {
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View view = inflater.inflate(R.layout.view_progressbtn, this);
		progressBtn = (FrameLayout) view.findViewById(R.id.progress_layout);
		progressBar = (ProgressBar) view.findViewById(R.id.download_progress);
		progressText = (TextView) view.findViewById(R.id.download_text);
//		btn = (Button) view.findViewById(R.id.btn);
//		btn_backup = (Button) view.findViewById(R.id.btn_backup);
//		progress_btn = (Button) view.findViewById(R.id.progress_btn);
//		tcv_toProgress = (ToCircleView) view.findViewById(R.id.tcv_toProgress);
//		round_progress_view = (RoundProgressView) view
//				.findViewById(R.id.round_progress_view);
//		iv_progress1 = (ImageView) view.findViewById(R.id.iv_progress1);
//		iv_progress2 = (ImageView) view.findViewById(R.id.iv_progress2);
//		agv1 = (AuroraGlassView) view.findViewById(R.id.agv1);
//		agv2 = (AuroraGlassView) view.findViewById(R.id.agv2);
//		tcv_toRect = (ToCircleView) view.findViewById(R.id.tcv_toRect);
//		fouceBtn = (Button) view.findViewById(R.id.fouceBtn);
//		fouceBtn_backup = (Button) view.findViewById(R.id.fouceBtn_backup);

		progressBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (!SystemUtils.isDownload(getContext())) {
					final View view = v;

					AuroraAlertDialog mWifiConDialog = new AuroraAlertDialog.Builder(
							getContext(), AuroraAlertDialog.THEME_AMIGO_FULLSCREEN)
							.setTitle(
									getContext().getResources().getString(
											R.string.dialog_prompt))
							.setMessage(
									getContext().getResources().getString(
											R.string.no_wifi_download_message))
							.setNegativeButton(android.R.string.cancel, null)
							.setPositiveButton(android.R.string.ok,
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											
											SharedPreferences sp = PreferenceManager
													.getDefaultSharedPreferences(getContext());
											Editor ed = sp.edit();
											ed.putBoolean("wifi_download_key", false);
											ed.commit();
											if (onButtonClickListener != null) {
												onButtonClickListener
														.onClick(view);
											}

											startBeginAnim();

											if (onNormalClickListener != null) {
												onNormalClickListener
														.onClick(view);
											}
										}

									}).create();
					mWifiConDialog.show();

				} else if (!SystemUtils.hasNetwork()) {
					Toast.makeText(getContext(), getContext()
							.getString(R.string.no_network_download_toast), Toast.LENGTH_SHORT).show();
				} else {
					if (onButtonClickListener != null) {
						onButtonClickListener.onClick(v);
					}

					startBeginAnim();

					if (onNormalClickListener != null) {
						onNormalClickListener.onClick(v);
					}
				}
			}
		});
		
		expandViewTouchDelegate(progressBtn);

		textSize = getResources().getDimension(R.dimen.progressBtnTextSize);
	}
	
	/**
	 * @Title: startBeginAnim
	 * @Description: TODO 进度开始前动画
	 * @param
	 * @return void
	 * @throws
	 */
	public void startBeginAnim() {
//		btn.setVisibility(View.GONE);
//		int width = getResources().getDimensionPixelOffset(R.dimen.app_item_down_btn_width);
//		int height = getResources().getDimensionPixelOffset(R.dimen.app_item_down_btn_height);
//		tcv_toProgress.setAllViewWidthAndHeight(width, height,
//				ToCircleView.TYPE_TO_CIRCLE);
//		tcv_toProgress.setVisibility(View.VISIBLE);
//		isRuningStartAnim = true;
//		tcv_toProgress.startAnim(new CustomAnimCallBack() {
//			@Override
//			public void callBack(float interpolatedTime, Transformation t) {
//				btn_backup.setAlpha(1 - interpolatedTime);
//				btn_backup.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize
//						- textSize * interpolatedTime);
//
//				// if (interpolatedTime >= startShow) {
//				// float alpha = (interpolatedTime - startShow) / (1 -
//				// startShow);
//				// progress_btn.setAlpha(alpha);
//				// progress_btn.setVisibility(View.VISIBLE);
//				// }
//
//				if (interpolatedTime == 1) {
//					isRuningStartAnim = false;
//					setStatus(STATUS_WAIT_DOWNLOAD);
//
//					if (onBeginAnimListener != null) {
//						onBeginAnimListener.onEnd(ProgressBtn.this);
//					}
//
//					// new Handler().postDelayed(new Runnable() {
//					//
//					// @Override
//					// public void run() {
//					// round_progress_view.setProgressAnim(100, 1000);
//					// }
//					// }, 500);
//					// new Handler().postDelayed(new Runnable() {
//					// @Override
//					// public void run() {
//					// startEndAnim();
//					// }
//					// }, 2000);
//				}
//			}
//		});
//		round_progress_view.setProgress(0);
		progressBar.setProgress(0);
	}

	/**
	 * @Title: startEndAnim
	 * @Description: TODO 结束进度后动画，按钮变为focus
	 * @param
	 * @return void
	 * @throws
	 */
	public void startEndAnim() {
		startEndAnim(true);
	}
	
	public void startEndAnim(final boolean fouce) {
//		int width = getResources().getDimensionPixelOffset(R.dimen.app_item_down_btn_width);
//		int height = getResources().getDimensionPixelOffset(R.dimen.app_item_down_btn_height);
//		tcv_toRect.setAllViewWidthAndHeight(width, height,
//				ToCircleView.TYPE_TO_RECT);
//		tcv_toRect.setVisibility(View.VISIBLE);
//		progress_btn.setVisibility(View.GONE);
//		round_progress_view.setVisibility(View.GONE);
//		iv_progress1.clearAnimation();
//		iv_progress1.setVisibility(View.GONE);
//		iv_progress2.clearAnimation();
//		iv_progress2.setVisibility(View.GONE);
//		agv1.stop();
//		agv1.setVisibility(View.GONE);
//		agv2.stop();
//		agv2.setVisibility(View.GONE);
//		fouceBtn_backup.setVisibility(View.VISIBLE);
//		isRuningEndAnim = true;
//		tcv_toRect.startAnim(new CustomAnimCallBack() {
//			@Override
//			public void callBack(float interpolatedTime, Transformation t) {
//				fouceBtn_backup.setAlpha(interpolatedTime);
//				fouceBtn_backup.setTextSize(TypedValue.COMPLEX_UNIT_PX,
//						textSize * interpolatedTime);
//
//				if (interpolatedTime >= startShow) {
//					float alpha = (interpolatedTime - startShow)
//							/ (1 - startShow);
//					fouceBtn.setAlpha(alpha);
//					fouceBtn.setVisibility(View.VISIBLE);
//				}
//
//				if (interpolatedTime == 1) {
//					isRuningEndAnim = false;
//					if (fouce) {
//						setStatus(STATUS_FOUCE);
//					} else {
//						setStatus(STATUS_FOUCE_NORMAL);
//					}
//				}
//			}
//		}, fouce);
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		LogPool.e(TAG,"Download Status:"+status);
		if (this.status == status) {
			return;
		}
		
		isRuningStartAnim = false;
		isRuningEndAnim = false;
//
//		iv_progress1.clearAnimation();
//		iv_progress2.clearAnimation();
//		agv1.stop();
//		agv2.stop();
//		tcv_toProgress.clearAnimation();
//		tcv_toRect.clearAnimation();
//		round_progress_view.clearAnimation();
		
		this.status = status;
		
		switch (status) {
		case STATUS_NORMAL:
//			btn.setVisibility(View.VISIBLE);
//			btn_backup.setVisibility(View.VISIBLE);
//			tcv_toProgress.setVisibility(View.GONE);
//			progress_btn.setVisibility(View.GONE);
//			round_progress_view.setVisibility(View.GONE);
//			iv_progress1.setVisibility(View.GONE);
//			iv_progress2.setVisibility(View.GONE);
//			agv1.setVisibility(View.GONE);
//			agv2.setVisibility(View.GONE);
//			tcv_toRect.setVisibility(View.GONE);
//			fouceBtn.setVisibility(View.GONE);
//			fouceBtn_backup.setVisibility(View.GONE);
			
//			restoreViewTouchDelegate(lastDelegate);
			progressBar.setProgressDrawable(getContext().getDrawable(R.drawable.layer_list_download_btn_df));
			progressText.setTextColor(redColor);
			progressText.setText(getContext().getString(R.string.download_button_download));
			//progressText.setVisibility(View.GONE);
			//expandViewTouchDelegate(progressBtn);
			
//			btn_backup.setAlpha(1);
//			btn_backup.setTextSize(TypedValue.COMPLEX_UNIT_PX, btn.getTextSize());
			break;
		case STATUS_WAIT_DOWNLOAD:
//			btn.setVisibility(View.GONE);
//			btn_backup.setVisibility(View.GONE);
//			tcv_toProgress.setVisibility(View.GONE);
//			progress_btn.setVisibility(View.GONE);
//			round_progress_view.setVisibility(View.GONE);
//			iv_progress1.setVisibility(View.GONE);
//			iv_progress2.setVisibility(View.GONE);
//			agv1.setVisibility(View.VISIBLE);
//			agv2.setVisibility(View.GONE);
//			tcv_toRect.setVisibility(View.GONE);
//			fouceBtn.setVisibility(View.GONE);
//			fouceBtn_backup.setVisibility(View.GONE);
			progressBar.setProgressDrawable(getContext().getDrawable(R.drawable.layer_list_download_btn_waiting));
			if(progressText.getVisibility() == View.GONE){
				progressText.setVisibility(View.VISIBLE);

			}
			progressText.setTextColor(waitingColor);
			progressText.setText(getContext().getString(R.string.download_button_wait));
//			restoreViewTouchDelegate(lastDelegate);
//
//			agv1.start();
			break;
		case STATUS_PROGRESSING_DOWNLOAD:
//			btn.setVisibility(View.GONE);
//			btn_backup.setVisibility(View.GONE);
//			tcv_toProgress.setVisibility(View.GONE);
//			progress_btn.setVisibility(View.VISIBLE);
//			round_progress_view.setVisibility(View.VISIBLE);
//			iv_progress1.setVisibility(View.GONE);
//			iv_progress2.setVisibility(View.GONE);
//			agv1.setVisibility(View.GONE);
//			agv2.setVisibility(View.GONE);
//			tcv_toRect.setVisibility(View.GONE);
//			fouceBtn.setVisibility(View.GONE);
//			fouceBtn_backup.setVisibility(View.GONE);
//
////			restoreViewTouchDelegate(lastDelegate);
//			expandViewTouchDelegate(progress_btn);
			progressBar.setProgressDrawable(getContext().getDrawable(R.drawable.layer_list_download_btn_downloading));
			if(progressText.getVisibility() == View.GONE){
				progressText.setVisibility(View.VISIBLE);
			}
			progressText.setTextColor(Color.WHITE);
			progressText.setText(this.progress+getContext().getString(R.string.download_button_download_percent));
			
			break;
		case STATUS_WAIT_INSTALL:
			progressBar.setProgressDrawable(getContext().getDrawable(R.drawable.layer_list_download_btn_waiting));
			if(progressText.getVisibility() == View.GONE){
				progressText.setVisibility(View.VISIBLE);
			}
			progressText.setTextColor(waitingColor);
			progressText.setText(getContext().getString(R.string.download_button_wait));
//			btn.setVisibility(View.GONE);
//			btn_backup.setVisibility(View.GONE);
//			tcv_toProgress.setVisibility(View.GONE);
//			progress_btn.setVisibility(View.GONE);
//			round_progress_view.setVisibility(View.GONE);
//			iv_progress1.setVisibility(View.GONE);
//			iv_progress2.setVisibility(View.GONE);
//			agv1.setVisibility(View.GONE);
//			agv2.setVisibility(View.VISIBLE);
//			tcv_toRect.setVisibility(View.GONE);
//			fouceBtn.setVisibility(View.GONE);
//			fouceBtn_backup.setVisibility(View.GONE);
//
//			restoreViewTouchDelegate(lastDelegate);
//
//			agv2.start();
			break;
		case STATUS_PROGRESSING_INSTALLING:
            progressBar.setProgressDrawable(getContext().getDrawable(R.drawable.layer_list_download_btn_null));
			if(progressText.getVisibility() == View.GONE){
				progressText.setVisibility(View.VISIBLE);
			}
			progressText.setTextColor(waitingColor);
			progressText.setText(getContext().getString(R.string.download_button_installing));

//			btn.setVisibility(View.GONE);
//			btn_backup.setVisibility(View.GONE);
//			tcv_toProgress.setVisibility(View.GONE);
//			progress_btn.setVisibility(View.GONE);
//			round_progress_view.setVisibility(View.GONE);
//			iv_progress1.setVisibility(View.VISIBLE);
//			iv_progress2.setVisibility(View.VISIBLE);
//			agv1.setVisibility(View.GONE);
//			agv2.setVisibility(View.GONE);
//			tcv_toRect.setVisibility(View.GONE);
//			fouceBtn.setVisibility(View.GONE);
//			fouceBtn_backup.setVisibility(View.GONE);
//
//			restoreViewTouchDelegate(lastDelegate);
//
//			iv_progress1.postInvalidate();
//			iv_progress1.startAnimation(createRotateAnimation(false));
//			iv_progress2.postInvalidate();
//			iv_progress2.startAnimation(createRotateAnimation(true));
			break;
		case STATUS_FOUCE:
			progressBar.setProgressDrawable(getContext().getDrawable(R.drawable.layer_list_download_btn_df));
			progressText.setTextColor(redColor);
			progressText.setText(getContext().getString(R.string.download_button_open));
//			setFouceStyle();
//
//			btn.setVisibility(View.GONE);
//			btn_backup.setVisibility(View.GONE);
//			tcv_toProgress.setVisibility(View.GONE);
//			progress_btn.setVisibility(View.GONE);
//			round_progress_view.setVisibility(View.GONE);
//			iv_progress1.setVisibility(View.GONE);
//			iv_progress2.setVisibility(View.GONE);
//			agv1.setVisibility(View.GONE);
//			agv2.setVisibility(View.GONE);
//			tcv_toRect.setVisibility(View.GONE);
//			fouceBtn.setVisibility(View.VISIBLE);
//			fouceBtn_backup.setVisibility(View.GONE);
//
////			restoreViewTouchDelegate(lastDelegate);
//			expandViewTouchDelegate(fouceBtn);
			
			break;
		case STATUS_FOUCE_NORMAL:
			progressBar.setProgressDrawable(getContext().getDrawable(R.drawable.layer_list_download_btn_df));
			if(progressText.getVisibility() == View.GONE){
				progressText.setVisibility(View.VISIBLE);
			}
			progressText.setTextColor(redColor);
			progressText.setText(getContext().getString(R.string.download_button_install));
			//setFouceNormalStyle();
			
//			btn.setVisibility(View.GONE);
//			btn_backup.setVisibility(View.GONE);
//			tcv_toProgress.setVisibility(View.GONE);
//			progress_btn.setVisibility(View.GONE);
//			round_progress_view.setVisibility(View.GONE);
//			iv_progress1.setVisibility(View.GONE);
//			iv_progress2.setVisibility(View.GONE);
//			agv1.setVisibility(View.GONE);
//			agv2.setVisibility(View.GONE);
//			tcv_toRect.setVisibility(View.GONE);
//			fouceBtn.setVisibility(View.VISIBLE);
//			fouceBtn_backup.setVisibility(View.GONE);
//
////			restoreViewTouchDelegate(lastDelegate);
//			expandViewTouchDelegate(fouceBtn);
			
			break;
			case STATUS_DOWNLOAD_FAIL:
				progressBar.setProgressDrawable(getContext().getDrawable(R.drawable.layer_list_download_btn_df));
				progressText.setTextColor(redColor);
				progressText.setText(getContext().getString(R.string.download_button_try));
				break;
			case STAUTS_DOWNLOAD_CONTINUE:
				progressBar.setProgressDrawable(getContext().getDrawable(R.drawable.layer_list_download_btn_pase));
				if(progressText.getVisibility() == View.VISIBLE){
					progressText.setVisibility(View.GONE);
				}
				progressBar.setProgress(progress);
				break;
		}
	}

	public boolean isRuningStartAnim() {
		return isRuningStartAnim;
	}

	public boolean isRuningEndAnim() {
		return isRuningEndAnim;
	}

	public void setOnButtonClickListener(OnClickListener onButtonClickListener) {
		this.onButtonClickListener = onButtonClickListener;
	}

	public void setOnNormalClickListener(OnClickListener onNormalClickListener) {
		this.onNormalClickListener = onNormalClickListener;
	}

	public void setBtnText(String text) {
		progressBar.setProgressDrawable(getContext().getDrawable(R.drawable.layer_list_download_btn_df));
		if(progressText.getVisibility() == View.GONE){
			progressText.setVisibility(View.VISIBLE);
		}
		progressText.setTextColor(redColor);
		progressText.setText(text);
//		btn.setText(text);
//		btn_backup.setText(text);
//		btn.postInvalidate();
//		btn_backup.postInvalidate();
	}

	public void setFoucesBtnText(String text) {
		progressBar.setProgressDrawable(getContext().getDrawable(R.drawable.layer_list_download_btn_df));
		if(progressText.getVisibility() == View.GONE){
			progressText.setVisibility(View.VISIBLE);
		}
		progressText.setTextColor(redColor);
		progressText.setText(getContext().getString(R.string.download_button_install));
//		fouceBtn.setText(text);
//		fouceBtn_backup.setText(text);
//		fouceBtn.postInvalidate();
//		fouceBtn_backup.postInvalidate();
	}

	public void setOnFoucsClickListener(OnClickListener onFoucsClickListener) {
		progressBtn.setOnClickListener(onFoucsClickListener);
	}

	public void setOnProgressClickListener(
			OnClickListener onProgressClickListener) {
		progressBtn.setOnClickListener(onProgressClickListener);
	}

	public void setProgressBackground(int resid) {
		progressBar.setProgressDrawable(getContext().getDrawable(R.drawable.layer_list_download_btn_downloading));
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int pro) {
		this.progress = pro;
		if (status != STATUS_PROGRESSING_DOWNLOAD) {
			setStatus(STATUS_PROGRESSING_DOWNLOAD);
		}
		post(new Runnable() {
			@Override
			public void run() {
				progressBar.setProgress(progress);
				if(progressText.getVisibility() == View.GONE){
					progressText.setVisibility(View.VISIBLE);
				}
				progressText.setTextColor(Color.WHITE);
				progressText.setText(progress+getContext().getString(R.string.download_button_download_percent));
			}
		});
		//round_progress_view.setProgress(progress);
	}

	public void setProgressAnim(int pr) {
		this.progress = pr;
		if (status != STATUS_PROGRESSING_DOWNLOAD) {
			setStatus(STATUS_PROGRESSING_DOWNLOAD);
		}
		post(new Runnable() {
			@Override
			public void run() {
				progressBar.setProgress(progress);
				if(progressText.getVisibility() == View.GONE){
					progressText.setVisibility(View.VISIBLE);
				}
				progressText.setTextColor(Color.WHITE);
				progressText.setText(progress+getContext().getString(R.string.download_button_download_percent));
			}
		});

		//round_progress_view.setProgressAnim(progress, progressTime);
	}

	public void setOnBeginAnimListener(OnAnimListener onBeginAnimListener) {
//		this.onBeginAnimListener = onBeginAnimListener;
	}
	
	public void setFouceStyle() {
//		fouceBtn.setBackgroundResource(R.drawable.button_focus_selector);
//		fouceBtn.setTextColor(getResources().getColor(R.color.white));
//		fouceBtn_backup.setTextColor(getResources().getColor(R.color.white));
	}
	
	public void setFouceNormalStyle() {
//		fouceBtn.setBackgroundResource(R.drawable.button_default_selector);
//		fouceBtn.setTextColor(getResources().getColorStateList(R.color.normal_btn_text_color));
//		fouceBtn_backup.setTextColor(getResources().getColorStateList(R.color.normal_btn_text_color));
	}

	/**
	 * @Title: createRotateAnimation
	 * @Description: 创建旋转动画
	 * @param @return
	 * @return RotateAnimation
	 * @throws
	 */
	private RotateAnimation createRotateAnimation(boolean reverse) {
		RotateAnimation animation = null;
		if (!reverse) {
			animation = new RotateAnimation(0, 3600, Animation.RELATIVE_TO_SELF,
					0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		} else {
			animation = new RotateAnimation(0, -3600, Animation.RELATIVE_TO_SELF,
					0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		}
		animation.setInterpolator(new LinearInterpolator());
		animation.setFillAfter(true);
		animation.setDuration(10000);
		animation.setStartOffset(0);
		animation.setRepeatCount(1000);
		return animation;
	}
	
	//====================加大按钮点击区域start====================//
	
	private View lastDelegate;
	
	private void expandViewTouchDelegate(final View view) {
		post(new Runnable() {
			@Override
			public void run() {
				Rect bounds = new Rect();
				view.setEnabled(true);
				view.getHitRect(bounds);

		        bounds.top -= 500;
		        bounds.bottom += 500;
		        bounds.left -= 500;
		        bounds.right += 500;

		        TouchDelegate touchDelegate = new TouchDelegate(bounds, view);

		        if (View.class.isInstance(view.getParent())) {
		            ((View) view.getParent()).setTouchDelegate(touchDelegate);
		        }
		        
		        lastDelegate = view;
			}
		});
	}
	
	private void restoreViewTouchDelegate(final View view) {
        post(new Runnable() {
            @Override
            public void run() {
            	if (view != null) {
            		Rect bounds = new Rect();
            		bounds.setEmpty();
            		TouchDelegate touchDelegate = new TouchDelegate(bounds, view);
            		
            		if (View.class.isInstance(view.getParent())) {
            			((View) view.getParent()).setTouchDelegate(touchDelegate);
            		}
            	}
            	
            }
        });
    }
	
	//====================加大按钮点击区域end====================//
	
	public interface OnAnimListener {
		public void onEnd(ProgressBtn view);
	}

}
