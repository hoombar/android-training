package net.rdyonline.android_training.material.palette;

import net.rdyonline.android_training.R;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.graphics.Palette.PaletteAsyncListener;
import android.support.v7.graphics.Palette.Swatch;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class PaletteActivity extends Activity {

	private Button mFindSwatches;
	private Button mFindOtherSwatches;

	private View mVibrant;
	private View mVibrantDark;
	private View mVibrantLight;
	private View mMuted;
	private View mMutedDark;
	private View mMutedLight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_palette);

		bindViews();
		setListeners();
	}

	private void bindViews() {
		mFindSwatches = (Button) findViewById(R.id.button_find_swatches);
		mFindOtherSwatches = (Button) findViewById(R.id.button_find_second_swatches);
		
		mVibrant = findViewById(R.id.img_vibrant);
		mVibrantDark = findViewById(R.id.img_vibrant_dark);
		mVibrantLight = findViewById(R.id.img_vibrant_light);
		
		mMuted = findViewById(R.id.img_muted);
		mMutedDark = findViewById(R.id.img_muted_dark);
		mMutedLight = findViewById(R.id.img_muted_light);
	}

	private void setListeners() {
		OnClickListener listener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.button_find_swatches:
					loadSwatchesForFirstImage();
					break;
				case R.id.button_find_second_swatches:
					loadSwatchesForSecondImage();
					break;
				}
			}
		};

		mFindSwatches.setOnClickListener(listener);
		mFindOtherSwatches.setOnClickListener(listener);
	}

	private void loadSwatchesForFirstImage() {
		Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.ch1);
		loadPaletteFromBitmap(b);
	}
	
	private void loadSwatchesForSecondImage() {
		Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.ch2);
		loadPaletteFromBitmap(b);
	}
	
	private void loadPaletteFromBitmap(Bitmap b) {
		Palette.generateAsync(b, new PaletteAsyncListener() {

			@Override
			public void onGenerated(Palette palette) {
				setBackground(mVibrant, palette.getVibrantSwatch());
				setBackground(mVibrantDark, palette.getDarkVibrantSwatch());
				setBackground(mVibrantLight, palette.getLightVibrantSwatch());
				
				setBackground(mMuted, palette.getMutedSwatch());
				setBackground(mMutedDark, palette.getDarkMutedSwatch());
				setBackground(mMutedLight, palette.getLightMutedSwatch());
			}
		});
	}
	
	private void setBackground(View view, Swatch swatch) {
		if (swatch == null) {
			view.setBackgroundDrawable(null);
			return;
		}
		view.setBackgroundColor(swatch.getRgb());
	}
	
}
