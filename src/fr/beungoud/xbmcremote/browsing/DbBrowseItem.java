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
import fr.beungoud.xbmcremote.commands.EnqueueSelectionFromDBMenuItemClickListener;
import fr.beungoud.xbmcremote.commands.playSelectionFromDBMenuItemClickListener;

/**
 * @author Benoit
 * 
 */
public class DbBrowseItem extends BrowseItem {

	String type;

	String statement;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatement() {
		return statement;
	}

	public void setStatement(String statement) {
		this.statement = statement;
	}

	public void buildContextMenu(ContextMenu menu, XbmcRequester requester) {
		Intent intent = new Intent();
		intent.putExtra("FOLDER", getPath());

		intent.putExtra("MEDIA_TYPE", getPlaylistId());

		intent.putExtra("TYPE", getType());
		intent.putExtra("STATEMENT", getStatement());

		MenuItem playItem = menu.add(R.string.play);
		playItem.setIntent(intent);
		playItem.setOnMenuItemClickListener(new playSelectionFromDBMenuItemClickListener());

		playItem = menu.add(R.string.enqueue);
		playItem.setIntent(intent);
		playItem.setOnMenuItemClickListener(new EnqueueSelectionFromDBMenuItemClickListener());

		playItem = menu.add(R.string.clear_playlist);
		playItem.setOnMenuItemClickListener(new ClearPlaylistMenuItemClickListener());

	}

	@Override
	public void execCommand(XbmcRequester requester, Context context) {

		if (isFolder()) {
			Intent intent = new Intent(context, BrowsingActivity.class);
			intent.putExtra((String) context.getText(R.string.key_browsing_path), getPath());
			intent.putExtra((String) context.getText(R.string.key_media_type), getMediaType());

			intent.putExtra((String) context.getText(R.string.key_browsing_type), (String) context
					.getText(R.string.browsing_type_database));
			context.startActivity(intent);
		} else {
			Intent intent = new Intent();
			intent.putExtra("FOLDER", getPath());

			intent.putExtra("MEDIA_TYPE", getPlaylistId());

			intent.putExtra("TYPE", getType());
			intent.putExtra("STATEMENT", getStatement());
			
			playSelectionFromDBMenuItemClickListener player = new playSelectionFromDBMenuItemClickListener();
			player.exec(intent);
		}
	}
}
