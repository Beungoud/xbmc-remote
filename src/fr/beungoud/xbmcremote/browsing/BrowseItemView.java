/**
 * 
 */
package fr.beungoud.xbmcremote.browsing;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import fr.beungoud.xbmcremote.R;
import fr.beungoud.xbmcremote.browsing.BrowseItem.MediaType;

/**
 * @author Benoit
 * 
 */
public class BrowseItemView extends RelativeLayout{

	TextView viewTitre;
	TextView viewSousTitre;
	protected ImageView viewImage;
	protected ImageView viewImageBack;

	public BrowseItemView(Context context) {
		super(context);

		LayoutInflater i = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		i.inflate(R.layout.browse_item_new_test, this);

		viewTitre = (TextView) findViewById(R.id.browse_item_title);

		viewImage = (ImageView) findViewById(R.id.browse_item_icon);
		
		viewImageBack = (ImageView) findViewById(R.id.browse_item_back_image);

		viewSousTitre = (TextView) findViewById(R.id.browse_item_subtitle);

	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public MediaType getMediaType() {
		return mediaType;
	}

	public void setMediaType(MediaType mediaType) {
		this.mediaType = mediaType;

		updateImage();
	}
	
	public void setSousTitre(String message)
	{

		if (viewSousTitre != null) {
			viewSousTitre.setText(message);
		}
	}

	public void updateImage() {
		if (viewImage != null) {
			if (isFolder) {
				if (mediaType == MediaType.Music) {
					viewImage.setImageResource(R.drawable.icon_60x60_folder_music);
				}
				if (mediaType == MediaType.Video) {
					viewImage.setImageResource(R.drawable.icon_60x60_folder_video);
				}
				if (mediaType == MediaType.Pictures) {
					viewImage.setImageResource(R.drawable.icon_60x60_folder_images);
				}
			} else {
				if (mediaType == MediaType.Music) {
					viewImage.setImageResource(R.drawable.icon_60x60_filetype_music);
				}
				if (mediaType == MediaType.Video) {
					viewImage.setImageResource(R.drawable.icon_60x60_filetype_video);
				}
				if (mediaType == MediaType.Pictures) {
					viewImage.setImageResource(R.drawable.icon_60x60_filetype_images);
				}

			}
		}
		if (viewImageBack != null)
		{
			viewImageBack.setImageBitmap(null);
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		viewTitre.setText(name);
	}

	public void setFolder(boolean isFolder) {
		this.isFolder = isFolder;
		updateImage();
	}

	public boolean isFolder() {
		return isFolder;
	}

	private boolean isFolder = true;

	private String path;

	private MediaType mediaType = MediaType.Music;

	private String name;

	private BrowseItem sourceItem;

	public BrowseItem getSourceItem() {
		return sourceItem;
	}

	public void setSourceItem(BrowseItem sourceItem) {
		this.sourceItem = sourceItem;
	}
}
