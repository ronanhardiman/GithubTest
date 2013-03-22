package com.igrs.tivic.phone.Activity;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.igrs.tivic.phone.R;

public class VideoPlayActivity extends BaseActivity implements MediaPlayer.OnErrorListener,
		MediaPlayer.OnCompletionListener{
	
	public static final String TAG = "VideoPlayer";
	private VideoView mVideoView;
	private int mPositionWhenPaused = -1;

	private MediaController mMediaController;
	private ArrayList<String> playUrls;

	Handler startVideoHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if(playUrls.size()>0){// Play Video
				Log.e("affdfs","==========startVideoHandler========="+playUrls.get(0));
				play(playUrls.remove(0));
			}
		}
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.videoview);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		mVideoView = (VideoView) findViewById(R.id.video_view);

		mMediaController = new MediaController(this);
		mVideoView.setMediaController(mMediaController);
		mVideoView.setOnCompletionListener(this);
		
		playUrls = new ArrayList<String>();
		getVideoUrl();
	}

	public void getVideoUrl(){
		final String url_video = getIntent().getStringExtra("video_url");
		Log.e("affdfs","===========url_video========"+url_video);
		if(url_video==null){
			Toast.makeText(this,"video url is null",Toast.LENGTH_LONG).show();
			return;
		}
		if(url_video.contains("web-play.pptv.com")){//pptv

			new Thread(new Runnable() {
				public void run() {
					try {
						InputStream in = new URL(url_video).openStream();
						byte[] b = new byte[1024];
						in.read(b);
						String content = new String(b);
						Log.e("affdfs","==================="+content);
						for(String s : content.split("\n")){
							if(s.startsWith("http")){
								Log.e("affdfs","=========url=========="+s);
								playUrls.add(s.trim());
							}
						}
						startVideoHandler.sendEmptyMessage(0);
					} catch (Exception e) {
						e.printStackTrace();
					} 
				}
			}).start();
		}else{
			play(url_video);
		}
	}
	private void play(String urlStr){
		Uri mUri = Uri.parse(urlStr);
		mVideoView.setVideoURI(mUri);
		mVideoView.start();
	}
	@Override
	public void onPause() {
		mPositionWhenPaused = mVideoView.getCurrentPosition();
		mVideoView.stopPlayback();
		super.onPause();
	}
	@Override
	public void onResume() {
		super.onResume();
		if (mPositionWhenPaused >= 0) {
			mVideoView.seekTo(mPositionWhenPaused);
			mPositionWhenPaused = -1;
		}
	}
	@Override
	public boolean onError(MediaPlayer player, int arg1, int arg2) {
		return false;
	}
	@Override//播放结束时的回调
	public void onCompletion(MediaPlayer mp) {
		if(playUrls.size()>0){
			startVideoHandler.sendEmptyMessage(0);
		}else{
			this.finish();
		}
	}
}
