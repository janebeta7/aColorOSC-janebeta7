package net.janebeta7.android;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
	private ImageView imgView;

	private Context mContext;

	public ImageAdapter(Context c) {

		mContext = c;
		imgView = (ImageView) ((Activity) mContext)
				.findViewById(R.id.PaletteAct);
		imgView.setScaleType(ImageView.ScaleType.FIT_XY);
		// http://stackoverflow.com/questions/3897176/findviewbyid-in-a-subclassed-surfaceview-throwing-runtimeexception
	}

	public int getCount() {
		return mThumbIds.length;
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}
	
	
	
	// create a new ImageView for each item referenced by the Adapter
	public View getView(final int position, View convertView, ViewGroup parent) {

		ImageView imageView;
		if (convertView == null) { // if it's not recycled, initialize some
									// attributes
			imageView = new ImageView(mContext);
			imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
			// imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			imageView.setPadding(0, 0, 0, 0);
		} else {
			imageView = (ImageView) convertView;
		}
		imageView.setImageResource(mThumbIds[position]);
		imageView.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Log.d("onClick", "position [" + position + "]");
				imgView.setImageResource(mThumbIds[position]); //display image screen
			
				final AssetManager mgr = mContext.getAssets();
				displayFiles(mgr,"set");
				
				BitmapFactory.Options bfoOptions = new BitmapFactory.Options(); 
				bfoOptions.inScaled = false; 
				
				Bitmap mBitmap = BitmapFactory.decodeResource(
						mContext.getResources(), mThumbIds[position]);
				
				
				
				int pic_width = mBitmap.getWidth();
				int pic_height = mBitmap.getHeight();
				Log.d("ImageAdapter", "size:" + pic_width + "px x "
						+ pic_height + "px");

				int[] pix = new int[pic_width * pic_height];

				List<Integer> pixColors = new ArrayList<Integer>();

				mBitmap.getPixels(pix, 0, pic_width, 0, 0, pic_width,
						pic_height);

				for (int x = 0; x < pic_width; x++) {
					for (int y = 0; y < pic_height; y++) {
						int index = y * pic_width + x;
						boolean blnFound = pixColors.contains(pix[index]);
						if (!blnFound)
							pixColors.add(new Integer(pix[index]));
					}
				}
				Log.d("ImageAdapter", "numColors:" + pixColors.size());
				/*
				 * Color colorr = new Color(); for (int n = 0; n < numColors;
				 * n++) { int r = colorr.red(pixColors[n]); int g =
				 * colorr.green(pixColors[n]); //public static int green (int
				 * color) int b = colorr.blue(pixColors[n]); Log.d("Color", "r:"
				 * + r); Log.d("Color", "g:" + g); Log.d("Color", "b:" + b); }
				 */

			}

		}); //imageView.setOnClickListener
		//mContext.sendData();
		return imageView;
	}
	private  void displayFiles (AssetManager mgr, String path) {
	    try {
	    	//Log.d("ImageAdapter", "display files");
	        String list[] = mgr.list(path);
	        if (list != null)
	            for (int i=0; i<list.length; ++i)
	                {
	            		
	            	//Log.d("ImageAdapter", path +"/"+ list[i]);
	                    
	                    displayFiles(mgr, path + "/" + list[i]);
	                }
	    } catch (IOException e) {
	    	Log.d("ImageAdapter", "can't list" + path);
	       
	    }

	}
	// references to our images
	private Integer[] mThumbIds = { R.drawable.a_blanco, R.drawable.rojo,
			R.drawable.deep_skyblues, R.drawable.gold_on_black_1_2,
			R.drawable.harmony_in_my_head, R.drawable.negro, R.drawable.mrdm,
			R.drawable.a_blanco, R.drawable.rojo, R.drawable.deep_skyblues,
			R.drawable.gold_on_black_1_2, R.drawable.harmony_in_my_head,
			R.drawable.negro, R.drawable.mrdm, R.drawable.a_blanco,
			R.drawable.rojo, R.drawable.deep_skyblues,
			R.drawable.gold_on_black_1_2, R.drawable.harmony_in_my_head,
			R.drawable.negro, R.drawable.mrdm };

}
