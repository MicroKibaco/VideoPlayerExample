package com.asiainfo.videoplayer.activity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.asiainfo.videoplayer.R;
import com.asiainfo.videoplayer.utils.PixelUtil;
import com.asiainfo.videoplayer.utils.permissionutil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VideoPlayerActivity extends Activity implements SeekBar.OnSeekBarChangeListener, View.OnTouchListener {

    static final int UPDATE_UI = 1;
    //播放控件
    @BindView(R.id.vv_player)
    VideoView mVideoView;

    //播放控制条
    @BindView(R.id.sb_play)
    SeekBar mSeekBarPlay;

    //音量控制条
    @BindView(R.id.sb_volume)
    SeekBar mSbVolume;

    //播放暂停控制按钮
    @BindView(R.id.iv_playControl)
    ImageView mIvPlayControl;

    //当前播放时间进度
    @BindView(R.id.tv_currentTime)
    TextView mTvCurrentTime;

    //总播放时间进度
    @BindView(R.id.tv_totalTime)
    TextView mTvTotalTime;

    @BindView(R.id.ll_playControl)
    LinearLayout mLlPlayControl;

    //屏幕切换按钮
    @BindView(R.id.iv_screenSwitch)
    ImageView mIvScreenSwitch;

    //音量图标
    @BindView(R.id.iv_volume)
    ImageView mIvVolume;

    //音量控制LinearLayout
    @BindView(R.id.ll_volumeControl)
    LinearLayout mLlVolumeControl;

    //包含控制各种view的布局
    @BindView(R.id.ll_control)
    LinearLayout mLlControl;

    //整个布局容器
    @BindView(R.id.rl_video)
    RelativeLayout mRlVideo;

    @BindView(R.id.operation_bg)
    ImageView mOperationBg;

    @BindView(R.id.left_percent)
    ImageView mLeftPercent;

    @BindView(R.id.operation_percent)
    ImageView mOperationPercent;

    @BindView(R.id.progress_layout)
    FrameLayout mProgressLayout;

    //当前的亮度
    private float mBrightness;

    private AudioManager mAudioManager;

    private boolean isFullScreen = false;

    private boolean isAdjust = false;

    private int threshold = 54;

    //当前获取屏幕的宽和屏幕的高
    private int screen_width, screen_heigh;

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == UPDATE_UI) {

                //获取当前视频播放时间
                int currentPosition = mVideoView.getCurrentPosition();

                //获取视频播放的总时间
                int totalDuration = mVideoView.getDuration();

                //格式视频播放时间
                updateTextViewWithTimeOut(mTvCurrentTime, currentPosition);
                updateTextViewWithTimeOut(mTvTotalTime, totalDuration);

                mSeekBarPlay.setMax(totalDuration);
                mSeekBarPlay.setProgress(currentPosition);
                mHandler.sendEmptyMessageDelayed(UPDATE_UI, 500);

            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        ButterKnife.bind(this);
        permissionutil.permission6(this);
        initView();
        initEvent();
        initDatas();
    }


    private void initView() {

        screen_width = getResources().getDisplayMetrics().widthPixels;
        screen_heigh = getResources().getDisplayMetrics().heightPixels;
    }

    private void initEvent() {

        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        mSeekBarPlay.setOnSeekBarChangeListener(this);
        mVideoView.setOnTouchListener(this);

    }

    /***
     * 控制VideoView的手势事件
     */

    private void setVideoViewScale(int width, int height) {

        ViewGroup.LayoutParams lp = mVideoView.getLayoutParams();
        lp.width = width;
        lp.height = height;
        mVideoView.setLayoutParams(lp);


        ViewGroup.LayoutParams lptemp = mRlVideo.getLayoutParams();
        lptemp.width = width;
        lptemp.height = height;
        mRlVideo.setLayoutParams(lptemp);

    }

    /**
     * 描述:加载视频播放源 创建时间:2/4/17/13:44 作者:小木箱 邮箱:yangzy3@asiainfo.com
     */

    private void initDatas() {

        //has leaked window DecorView@d2bdca5[] that was originally added here 修改你SD卡的视频存储路径
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + getResources().getString(R.string.mv_name);

        /***
         * 本地视频播放
         */

        mVideoView.setVideoPath(path);
        mVideoView.start();
        mHandler.sendEmptyMessage(UPDATE_UI);


        /***
         * 网络视频播放
         */
        // mVideoview.setVideoURI(Uri.parse("https://pan.baidu.com/s/1dFEDfTB"));

        /***
         * 使用MediaController控制视屏播放
         */

        MediaController controller = new MediaController(this);

        /***
         * 设置videoView与MediaController建立关联
         */

        controller.setMediaPlayer(mVideoView);

    }

    /***
     * 更新视频播放时间
     */

    private void updateTextViewWithTimeOut(TextView view, int millisecond) {

        int second = millisecond / 1000;
        int hh = second / 3600;
        int mm = second % 3600 / 60;
        int ss = second % 60;
        String str = null;
        if (hh != 0) {

            str = String.format("%02d:%02d:%02d", hh, mm, ss);

        } else {

            str = String.format("%02d:%02d", mm, ss);

        }

        view.setText(str);

    }

    @Override
    protected void onPause() {

        super.onPause();
        mHandler.removeMessages(UPDATE_UI);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @OnClick({R.id.vv_player, R.id.sb_play, R.id.iv_playControl, R.id.tv_currentTime, R.id.tv_totalTime, R.id.ll_playControl, R.id.iv_screenSwitch, R.id.iv_volume, R.id.sb_volume, R.id.ll_volumeControl, R.id.ll_control, R.id.rl_video})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.vv_player:
                break;
            case R.id.sb_play:
                break;
            case R.id.iv_playControl:

                /***
                 * 控制视频的暂停和播放
                 */

                if (mVideoView.isPlaying()) {

                    mIvPlayControl.setImageResource(R.drawable.pause_btn_style);

                    //暂停播放
                    mVideoView.pause();
                    mHandler.removeMessages(UPDATE_UI);

                } else {

                    mIvPlayControl.setImageResource(R.drawable.pause_btn_style);

                    //继续播放
                    mVideoView.start();
                    mHandler.sendEmptyMessage(UPDATE_UI);

                }


                break;

            case R.id.tv_currentTime:
                break;

            case R.id.tv_totalTime:
                break;

            case R.id.ll_playControl:
                break;

            case R.id.iv_screenSwitch:

                /***
                 * 切换屏幕
                 */
                if (isFullScreen) {

                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                } else {

                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

                }

                break;

            case R.id.iv_volume:
                break;

            case R.id.sb_volume:
                break;

            case R.id.ll_volumeControl:
                break;

            case R.id.ll_control:
                break;

            case R.id.rl_video:
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        updateTextViewWithTimeOut(mTvCurrentTime, progress);

    }


    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

        mHandler.removeMessages(UPDATE_UI);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

        int progress = seekBar.getProgress();

        //另视频播放的进度遵循seekBar停止拖动的这一刻的进度
        mVideoView.seekTo(progress);
        mHandler.sendEmptyMessage(UPDATE_UI);

    }
    /***
     * 横竖屏切换
     */


    /***
     * 监听到屏幕方向的改变
     */

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        /***
         * 当屏幕方向为横屏的时候
         */
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

            setVideoViewScale(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mIvVolume.setVisibility(View.VISIBLE);
            mSbVolume.setVisibility(View.VISIBLE);
            isFullScreen = true;
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);


        } else {
            /***
             * 当屏幕为竖屏的时候
             */
            setVideoViewScale(ViewGroup.LayoutParams.MATCH_PARENT, PixelUtil.dp2px(this, 240));
            mIvVolume.setVisibility(View.GONE);
            mSbVolume.setVisibility(View.GONE);
            isFullScreen = false;
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

        }
    }

    /***
     * 调节声音
     */
    private void changeVolume(float detlaY) {

        int max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int current = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int index = (int) (detlaY / screen_heigh + max * 3);
        int volume = Math.max(current + index, 0);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);

        if (mProgressLayout.getVisibility() == View.VISIBLE) {

            mProgressLayout.setVisibility(View.VISIBLE);
        }

        mOperationBg.setImageResource(R.drawable.play);
        ViewGroup.LayoutParams params = mOperationPercent.getLayoutParams();

        params.width = (int) (PixelUtil.dp2px(this, 94) * (float) volume / max);
        mOperationPercent.setLayoutParams(params);
        mSbVolume.setProgress(volume);


    }

    /***
     * 调节亮度
     */
    private void changeBrightness(float detlay) {

        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        mBrightness = attributes.screenBrightness;
        float index = detlay / screen_heigh / 3;
        mBrightness += index;
        if (mBrightness > 1.0f) {
            mBrightness = 1.0f;
        }
        if (mBrightness < 0.01f) {

            mBrightness = 0.01f;

        }
        attributes.screenBrightness = mBrightness;

        if (mProgressLayout.getVisibility() == View.VISIBLE) {

            mProgressLayout.setVisibility(View.VISIBLE);
        }

        mOperationBg.setImageResource(R.drawable.play);
        ViewGroup.LayoutParams params = mOperationPercent.getLayoutParams();

        params.width = (int) (PixelUtil.dp2px(this, 94) * mBrightness);
        mOperationPercent.setLayoutParams(params);
        getWindow().setAttributes(attributes);

    }

    /***
     * 控制videoView的手势事件
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {

        float x = event.getX();
        float y = event.getY();
        float lastX = 0, lastY = 0;

        switch (event.getAction()) {
            /***
             * 手指落下屏幕的那一刻(只会调用一次)
             */
            case MotionEvent.ACTION_DOWN:
                lastX = x;
                lastY = y;
                break;

            /***
             * 手指在屏幕上移动(调用多次)
             */
            case MotionEvent.ACTION_MOVE:
                float detlax = -lastX;
                float detlay = y - lastY;
                float absdelax = Math.abs(detlax);
                float absdetlay = Math.abs(detlay);

                if (absdelax > threshold && absdetlay > threshold) {

                    isAdjust = absdelax < absdetlay;

                } else if (absdelax < threshold && absdetlay > threshold) {


                    isAdjust = true;

                } else if (absdelax > threshold && absdetlay < threshold) {

                    isAdjust = false;
                }
                Log.e("VideoPlayerActivity", "手势是否合法" + isAdjust);

                if (isAdjust) {

                    if (x < screen_width / 2) {

                        /***
                         * 调节亮度
                         */
                        if (detlay > 0) {

                            /***
                             * 降低亮度
                             */
                            Log.e("VideoPlayerActivity", "降低亮度" + detlay);


                        } else {

                            /***
                             * 升高亮度
                             */
                            Log.e("VideoPlayerActivity", "升高亮度" + detlay);

                        }
                        changeBrightness(-detlay);
                    } else {

                        /***
                         * 调大声音
                         */

                        changeVolume(-detlay);

                        if (detlay > 0) {

                            /***
                             * 减小声音
                             */
                            changeVolume(detlay);
                        }

                    }

                }

                lastX = x;
                lastY = y;

                break;
            /***
             * 手指离开屏幕那一刻(只会调用一次)
             */
            case MotionEvent.ACTION_UP:
                break;

        }

        return true;
    }
}
