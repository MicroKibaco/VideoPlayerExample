package com.asiainfo.videoplayer.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.asiainfo.videoplayer.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VideoPlayerActivity extends Activity {

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
        setPlayerEvent();
        initDatas();
    }

    private void setPlayerEvent() {


    }

    /**
     * 描述:加载视频播放源 创建时间:2/4/17/13:44 作者:小木箱 邮箱:yangzy3@asiainfo.com
     */

    private void initDatas() {

        //has leaked window DecorView@d2bdca5[] that was originally added here 修改你SD卡的视频存储路径
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
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
}
