package com.dream.NiuFaNet.Ui.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView.ScaleType;

import com.bumptech.glide.Glide;
import com.dream.NiuFaNet.CustomView.SmoothImageView;
import com.dream.NiuFaNet.Listener.NoDoubleClickListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;

public class SpaceImageDetailActivity extends Activity {

	private ArrayList<String> mDatas;
	private int mPosition;
	private int mLocationX;
	private int mLocationY;
	private int mWidth;
	private int mHeight;
	SmoothImageView imageView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mLocationX = getIntent().getIntExtra("locationX", 0);
		mLocationY = getIntent().getIntExtra("locationY", 0);
		mWidth = getIntent().getIntExtra("width", 0);
		mHeight = getIntent().getIntExtra("height", 0);
		String picUrl = getIntent().getStringExtra("picUrl");
		imageView = new SmoothImageView(this);
		imageView.setOriginalInfo(mWidth, mHeight, mLocationX, mLocationY);
		imageView.transformIn();
		imageView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
		imageView.setScaleType(ScaleType.FIT_CENTER);
		setContentView(imageView);
		Glide.with(this).load(picUrl).into(imageView);
//		imageView.setImageResource(R.drawable.temp);
		// ScaleAnimation scaleAnimation = new ScaleAnimation(0.5f, 1.0f, 0.5f,
		// 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
		// 0.5f);
		// scaleAnimation.setDuration(300);
		// scaleAnimation.setInterpolator(new AccelerateInterpolator());
		// imageView.startAnimation(scaleAnimation);
		imageView.setOnClickListener(new NoDoubleClickListener() {
			@Override
			public void onNoDoubleClick(View view) {
				imageView.setOnTransformListener(new SmoothImageView.TransformListener() {
					@Override
					public void onTransformComplete(int mode) {
						if (mode == 2) {
							finish();
						}
					}
				});
				imageView.transformOut();
			}
		});

	}

	@Override
	public void onBackPressed() {
		imageView.setOnTransformListener(new SmoothImageView.TransformListener() {
			@Override
			public void onTransformComplete(int mode) {
				if (mode == 2) {
					finish();
				}
			}
		});
		imageView.transformOut();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (isFinishing()) {
			overridePendingTransition(0, 0);
		}
	}

}
