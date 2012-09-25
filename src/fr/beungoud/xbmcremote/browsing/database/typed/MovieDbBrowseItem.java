package fr.beungoud.xbmcremote.browsing.database.typed;

import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.MenuItem;
import fr.beungoud.xbmcremote.R;
import fr.beungoud.xbmcremote.XbmcRequester;
import fr.beungoud.xbmcremote.commands.ClearPlaylistMenuItemClickListener;
import fr.beungoud.xbmcremote.commands.DirectPlayMenuItemClickListener;
import fr.beungoud.xbmcremote.commands.EnqueueSelectionMenuItemClickListener;

public class MovieDbBrowseItem extends AbstractDbBrowseItem {

	String filePath = null;

	public MovieDbBrowseItem() {

	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	@Override
	public void buildContextMenu(ContextMenu menu, XbmcRequester requester) {
		Intent intent = new Intent();
		intent.putExtra("FOLDER", getFilePath());

		intent.putExtra("MEDIA_TYPE", getPlaylistId());

		intent.putExtra("TYPE", getType());

		MenuItem playItem = menu.add(R.string.play);
		playItem.setIntent(intent);
		playItem.setOnMenuItemClickListener(new DirectPlayMenuItemClickListener());

		playItem = menu.add(R.string.enqueue);
		playItem.setIntent(intent);
		playItem.setOnMenuItemClickListener(new EnqueueSelectionMenuItemClickListener());

		playItem = menu.add(R.string.clear_playlist);
		playItem.setOnMenuItemClickListener(new ClearPlaylistMenuItemClickListener());
	}

	@Override
	public void execCommand(XbmcRequester requester, Context context) {
		DirectPlayMenuItemClickListener action = new DirectPlayMenuItemClickListener();

		Intent intent = new Intent();

		intent.putExtra("FOLDER", getFilePath());
		intent.putExtra("MEDIA_TYPE", getPlaylistId());

		action.exec(intent);
	}

}
