/**
 * 
 */
package fr.beungoud.xbmcremote.browsing.database.typed;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.MenuItem;
import fr.beungoud.xbmcremote.R;
import fr.beungoud.xbmcremote.XbmcRequester;
import fr.beungoud.xbmcremote.browsing.BrowseItem;
import fr.beungoud.xbmcremote.browsing.BrowsingActivity;
import fr.beungoud.xbmcremote.commands.ClearPlaylistMenuItemClickListener;
import fr.beungoud.xbmcremote.commands.EnqueueSelectionFromDBMenuItemClickListener;
import fr.beungoud.xbmcremote.commands.playSelectionFromDBMenuItemClickListener;

/**
 * @author Benoit
 * 
 */
public abstract class AbstractDbBrowseItem extends BrowseItem {

	private Activity activity;
	
	String type;

	Intent intentAction;
	Long buildId;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void backgroundExec() {

	}

	public void buildContextMenu(ContextMenu menu, XbmcRequester requester) {
		// Intent intent = new Intent();
		// intent.putExtra("FOLDER", getPath());
		//
		// intent.putExtra("MEDIA_TYPE", getPlaylistId());
		//
		// intent.putExtra("TYPE", getType());
		// intent.putExtra("STATEMENT", getStatement());
		//
		// MenuItem playItem = menu.add(R.string.play);
		// playItem.setIntent(intent);
		// playItem.setOnMenuItemClickListener(new
		// playSelectionFromDBMenuItemClickListener());
		//
		// playItem = menu.add(R.string.enqueue);
		// playItem.setIntent(intent);
		// playItem.setOnMenuItemClickListener(new
		// EnqueueSelectionFromDBMenuItemClickListener());
		//
		// playItem = menu.add(R.string.clear_playlist);
		// playItem.setOnMenuItemClickListener(new
		// ClearPlaylistMenuItemClickListener());

	}

	@Override
	public void execCommand(XbmcRequester requester, Context context) {

		context.startActivity(intentAction);

	}

	public Intent getIntentAction() {
		return intentAction;
	}

	public void setIntentAction(Intent intentAction) {
		this.intentAction = intentAction;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((intentAction == null) ? 0 : intentAction.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractDbBrowseItem other = (AbstractDbBrowseItem) obj;
		if (intentAction == null) {
			if (other.intentAction != null)
				return false;
		} else if (!intentAction.equals(other.intentAction))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	public void setBuildId(Long currentId) {
		buildId = currentId;
	}

	/**
	 * @return the buildId
	 */
	public Long getBuildId() {
		return buildId;
	}

}
