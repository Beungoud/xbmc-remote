/**
 * 
 */
package fr.beungoud.xbmcremote.streaming;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import fr.beungoud.xbmcremote.R;

/**
 * @author Benoit
 * 
 */
public class PlaylistItemView extends LinearLayout {

	TextView viewTitre;
	TextView viewSousTitre;
	ImageView viewImage;

	String title;
	boolean isNowPlaying = false;;
	String subtitle;

	public PlaylistItemView(Context context) {
		super(context);

		LayoutInflater i = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		i.inflate(R.layout.playlist_item, this);

		viewTitre = (TextView) findViewById(R.id.browse_item_title);

		viewImage = (ImageView) findViewById(R.id.browse_item_icon);

		viewSousTitre = (TextView) findViewById(R.id.browse_item_subtitle);

	}

	public void setNowPlaying(boolean isNowPlaying) {
		this.isNowPlaying = isNowPlaying;

		if (isNowPlaying) {
			viewImage.setImageResource(R.drawable.dvd);
		} else {
			viewImage.setImageResource(R.drawable.cd_unknown);
		}
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		viewTitre.setText(title);
		this.title = title;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		viewSousTitre.setText(subtitle);
		this.subtitle = subtitle;
	}

}
