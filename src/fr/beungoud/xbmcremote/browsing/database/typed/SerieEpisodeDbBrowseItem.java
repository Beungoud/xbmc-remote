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
import fr.beungoud.xbmcremote.commands.playMusicSelectionMenuItemClickListener;
import fr.beungoud.xbmcremote.commands.playVideoSelectionMenuItemClickListener;
import fr.beungoud.xbmcremote.commands.playVideoSelectionMenuItemClickListener.execModeEnum;

public class SerieEpisodeDbBrowseItem extends AbstractDbBrowseItem {

	Long idEpisode;
	
	String episodeName;
	
	Integer numeroEpisode;
	
	
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
		intent.putExtra("WHERECLAUSE", "idEpisode = " + idEpisode);
		intent.putExtra("EXEC_MODE", execModeEnum.Play);
		playItem.setIntent(intent);
		playItem.setOnMenuItemClickListener(new playVideoSelectionMenuItemClickListener());
//
		playItem = menu.add(R.string.enqueue);
		Intent intent2 = new Intent();
		intent2.putExtra("EXEC_MODE", execModeEnum.Enqueue);
		intent2.putExtra("WHERECLAUSE", "idEpisode = " + idEpisode);
		
		playItem.setIntent(intent2);
		playItem.setOnMenuItemClickListener(new playVideoSelectionMenuItemClickListener());

		playItem = menu.add(R.string.clear_playlist);
		playItem.setOnMenuItemClickListener(new ClearPlaylistMenuItemClickListener());
	}


	public Long getIdEpisode() {
		return idEpisode;
	}


	public void setIdEpisode(Long idEpisode) {
		this.idEpisode = idEpisode;
	}


	public String getEpisodeName() {
		return episodeName;
	}


	public void setEpisodeName(String episodeName) {
		this.episodeName = episodeName;
	}



	@Override
	public void execCommand(XbmcRequester requester, Context context) {
		Intent intent = new Intent();
		intent.putExtra("WHERECLAUSE", "idEpisode = " + idEpisode);
		intent.putExtra("EXEC_MODE", execModeEnum.Play);
		playVideoSelectionMenuItemClickListener action = new playVideoSelectionMenuItemClickListener();
		action.exec(intent);
	}


	public Integer getNumeroEpisode() {
		return numeroEpisode;
	}

	public void setNumeroEpisode(Integer numeroEpisode) {
		this.numeroEpisode = numeroEpisode;
	}

}
