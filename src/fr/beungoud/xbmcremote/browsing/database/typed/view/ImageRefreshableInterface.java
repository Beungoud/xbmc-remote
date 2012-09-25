package fr.beungoud.xbmcremote.browsing.database.typed.view;

import android.graphics.Bitmap;

public interface ImageRefreshableInterface {
	public void setThumbnail(Bitmap bmp, String path);
	public String getThumbPath();
}
