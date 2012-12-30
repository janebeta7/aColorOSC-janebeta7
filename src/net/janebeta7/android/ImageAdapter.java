package net.janebeta7.android;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;

import android.util.Log;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

public class ImageAdapter extends BaseAdapter {
	private List<Integer> pixColors;
	private ImageView imgView;

	private Context mContext;
	private List<String> FileList;

	public ImageAdapter(Context c, List<String> fList) {

		mContext = c;
		FileList = fList;
imgView = (ImageView) ((Activity) mContext)
				.findViewById(R.id.PaletteAct);
		imgView.setScaleType(ImageView.ScaleType.FIT_XY);
		setImageGrande(FileList.get(0).toString());
		processColors(FileList.get(0).toString());
		

	}

	public void setImageGrande(String imagen) {
	
		 Bitmap bitmap = BitmapFactory.decodeFile(imagen);
		 imgView.setImageBitmap(bitmap);


				
	}
	public int getCount() {
		// return mThumbIds.length;
		return FileList.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		 return position;
	}

	/*
	 * http://stackoverflow.com/questions/11681686/displaying-images-from-sd-card
	 * -folder-in-gallery-viewandroid
	 */
	// create a new ImageView for each item referenced by the Adapter
	public View getView(int position, View convertView, ViewGroup parent) {

		ImageView imageView = new ImageView(mContext);
		Bitmap bm = BitmapFactory.decodeFile(FileList.get(position).toString());
		imageView.setImageBitmap(bm);
		imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
		imageView.setScaleType(ImageView.ScaleType.FIT_XY);
		imageView.setPadding(0, 0, 0, 0);
		
		
		//mContext.sendData();
		return imageView;
	}

	public void processColors(final String filepath) {
		Bitmap mBitmap = BitmapFactory.decodeFile(filepath);
		Log.d("janebeta7", "filepath" + filepath );
		Log.d("janebeta7", "pic_width" + mBitmap.getWidth() );
		Log.d("janebeta7", "pic_height" + mBitmap.getHeight() );
		int pic_width = mBitmap.getWidth();
		int pic_height = mBitmap.getHeight();
		if (pic_width>0){ //es una imagen buena
				//comprobar que es imagen las que empiezan por puntos dan error
				Log.d("ImageAdapter", "size:" + pic_width + "px x " + pic_height + "px");
		
				int[] pix = new int[pic_width * pic_height];
		
				pixColors = new ArrayList<Integer>();
		
				mBitmap.getPixels(pix, 0, pic_width, 0, 0, pic_width, pic_height);
		
				for (int x = 0; x < pic_width; x++) {
					for (int y = 0; y < pic_height; y++) {
						int index = y * pic_width + x;
						boolean blnFound = pixColors.contains(pix[index]);
						if (!blnFound)
							pixColors.add(new Integer(pix[index]));
					}
				}
		}
	}

	public int getColor() {
		final Random myRandom = new Random();
		return pixColors.get(myRandom.nextInt(pixColors.size()));
	}

	

	


}
