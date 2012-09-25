/**
 * 
 */
package fr.beungoud.xbmcremote.browsing;

import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.MenuItem;
import fr.beungoud.xbmcremote.Constants;
import fr.beungoud.xbmcremote.R;
import fr.beungoud.xbmcremote.XbmcRequester;
import fr.beungoud.xbmcremote.browsing.BrowseItem.MediaType;
import fr.beungoud.xbmcremote.commands.ClearPlaylistMenuItemClickListener;
import fr.beungoud.xbmcremote.commands.DirectPlayMenuItemClickListener;
import fr.beungoud.xbmcremote.commands.DirectSlideMenuItemClickListener;
import fr.beungoud.xbmcremote.commands.EnqueueSelectionFromDBMenuItemClickListener;
import fr.beungoud.xbmcremote.commands.EnqueueSelectionMenuItemClickListener;
import fr.beungoud.xbmcremote.commands.EnqueueSlideMenuItemClickListener;
import fr.beungoud.xbmcremote.commands.StreamSelectionMenuItemClickListener;
import fr.beungoud.xbmcremote.commands.playSelectionFromDBMenuItemClickListener;
import fr.beungoud.xbmcremote.commands.playSelectionMenuItemClickListener;

/**
 * @author Benoit
 * 
 */
public class FileBrowseItem extends BrowseItem {

	public void buildContextMenu(ContextMenu menu, XbmcRequester requester) {
		Intent intent = new Intent();
		intent.putExtra("FOLDER", getPath());

		intent.putExtra("MEDIA_TYPE", getPlaylistId());
		MenuItem playItem;

		if (getMediaType().equals(MediaType.Pictures)) {
			playItem = menu.add(R.string.slideshow);
			playItem.setIntent(intent);
			playItem.setOnMenuItemClickListener(new DirectSlideMenuItemClickListener());

			playItem = menu.add(R.string.enqueueSlide);
			playItem.setIntent(intent);
			playItem.setOnMenuItemClickListener(new EnqueueSlideMenuItemClickListener());
		} else {
			playItem = menu.add(R.string.play);
			playItem.setIntent(intent);
			playItem.setOnMenuItemClickListener(new playSelectionMenuItemClickListener());

			playItem = menu.add(R.string.enqueue);
			playItem.setIntent(intent);
			playItem.setOnMenuItemClickListener(new EnqueueSelectionMenuItemClickListener());

			playItem = menu.add(R.string.clear_playlist);
			playItem.setOnMenuItemClickListener(new ClearPlaylistMenuItemClickListener());
		}

		// if (getMediaType() == MediaType.Music) {
		// playItem = menu.add(R.string.stream_music);
		// playItem.setIntent(intent);
		// playItem.setOnMenuItemClickListener(new
		// StreamSelectionMenuItemClickListener());
		// }
	}

	@Override
	public void execCommand(XbmcRequester requester, Context context) {

		if (isFolder()) {
			Intent intent = new Intent(context, BrowsingActivity.class);
			intent.putExtra((String) context.getText(R.string.key_browsing_path), getPath());
			intent.putExtra((String) context.getText(R.string.key_media_type), getMediaType());

			intent.putExtra((String) context.getText(R.string.key_browsing_type), (String) context
					.getText(R.string.browsing_type_file));
			context.startActivity(intent);
		} else if (getMediaType() == MediaType.Music) {
			Intent intent = new Intent();
			intent.putExtra("FOLDER", getPath());

			intent.putExtra("MEDIA_TYPE", Constants.ID_PLAYLIST_MUSIC);
			DirectPlayMenuItemClickListener player = new DirectPlayMenuItemClickListener();
			player.exec(intent);

		} else if (getMediaType() == MediaType.Video) {
			Intent intent = new Intent();
			intent.putExtra("FOLDER", getPath());

			intent.putExtra("MEDIA_TYPE", Constants.ID_PLAYLIST_VIDEO);
			DirectPlayMenuItemClickListener player = new DirectPlayMenuItemClickListener();
			player.exec(intent);
		} else if (getMediaType() == MediaType.Pictures) {
			Intent intent = new Intent();
			intent.putExtra("FOLDER", getPath());

			DirectSlideMenuItemClickListener player = new DirectSlideMenuItemClickListener();
			player.exec(intent);

		}
	}

}
