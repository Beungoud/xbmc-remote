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

public class ArtistDbBrowseItem extends AbstractDbBrowseItem {

	int idArtist;
	
	
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
		intent.putExtra("WHERECLAUSE", "idArtist = " + idArtist);
		intent.putExtra("EXEC_MODE", execModeEnum.Play);
		playItem.setIntent(intent);
		playItem.setOnMenuItemClickListener(new playMusicSelectionMenuItemClickListener());
//
		playItem = menu.add(R.string.enqueue);
		Intent intent2 = new Intent();
		intent2.putExtra("EXEC_MODE", execModeEnum.Enqueue);
		intent2.putExtra("WHERECLAUSE", "idArtist = " + idArtist);
		
		playItem.setIntent(intent2);
		playItem.setOnMenuItemClickListener(new playMusicSelectionMenuItemClickListener());

		playItem = menu.add(R.string.clear_playlist);
		playItem.setOnMenuItemClickListener(new ClearPlaylistMenuItemClickListener());
	}

	@Override
	public void backgroundExec() {
		super.backgroundExec();
		try {
			Thread.sleep(500);
			setName(getName() + " ++" + getBuildId());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	public int getIdArtist() {
		return idArtist;
	}

	public void setIdArtist(int idArtist) {
		this.idArtist = idArtist;
	}

}
