package fr.beungoud.xbmcremote.browsing.database.typed;

import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.MenuItem;
import fr.beungoud.xbmcremote.R;
import fr.beungoud.xbmcremote.XbmcRequester;
import fr.beungoud.xbmcremote.browsing.BrowsingFactory;
import fr.beungoud.xbmcremote.commands.ClearPlaylistMenuItemClickListener;
import fr.beungoud.xbmcremote.commands.DirectPlayMenuItemClickListener;
import fr.beungoud.xbmcremote.commands.EnqueueSelectionMenuItemClickListener;
import fr.beungoud.xbmcremote.commands.playMusicSelectionMenuItemClickListener;
import fr.beungoud.xbmcremote.commands.playMusicSelectionMenuItemClickListener.execModeEnum;

public class SongDbBrowseItem extends AbstractDbBrowseItem {

	String path;
	
	Long idSong;
	
	@Override
	public void buildContextMenu(ContextMenu menu, XbmcRequester requester) {
		
		
		
//		intent.putExtra("FOLDER", getFilePath());
//
//		intent.putExtra("MEDIA_TYPE", getPlaylistId());
//
//		intent.putExtra("TYPE", getType());
//
		MenuItem playItem = menu.add(R.string.play);
		Intent intent = new Intent();
		intent.putExtra("WHERECLAUSE", "idSong = " + idSong);
		intent.putExtra("EXEC_MODE", execModeEnum.Play);
		playItem.setIntent(intent);
		playItem.setOnMenuItemClickListener(new playMusicSelectionMenuItemClickListener());
//
		playItem = menu.add(R.string.enqueue);
		Intent intent2 = new Intent();
		intent2.putExtra("EXEC_MODE", execModeEnum.Enqueue);
		intent2.putExtra("WHERECLAUSE", "idSong = " + idSong);
		
		playItem.setIntent(intent2);
		playItem.setOnMenuItemClickListener(new playMusicSelectionMenuItemClickListener());

		playItem = menu.add(R.string.clear_playlist);
		playItem.setOnMenuItemClickListener(new ClearPlaylistMenuItemClickListener());
	}

	@Override
	public void execCommand(XbmcRequester requester, Context context) {
		Intent intent = new Intent();
		intent.putExtra("FOLDER", getPath() );
		intent.putExtra("MEDIA_TYPE", getPlaylistId());
		
		DirectPlayMenuItemClickListener action = new DirectPlayMenuItemClickListener();
		action.exec(intent);
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Long getIdSong() {
		return idSong;
	}

	public void setIdSong(Long idSong) {
		this.idSong = idSong;
	}

}
