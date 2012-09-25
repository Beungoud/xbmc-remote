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

public class SerieSeasonDbBrowseItem extends AbstractDbBrowseItem {
	
	Long idShow;
	
	Long numeroSaison;
	
	String nomSerie;
	
	@Override
	public void buildContextMenu(ContextMenu menu, XbmcRequester requester) {
		
		MenuItem playItem = menu.add(R.string.play);
		Intent intent = new Intent();
		intent.putExtra("WHERECLAUSE", "idShow = " + idShow + " and c12 = " + numeroSaison);
		intent.putExtra("EXEC_MODE", execModeEnum.Play);
		playItem.setIntent(intent);
		playItem.setOnMenuItemClickListener(new playVideoSelectionMenuItemClickListener());
//
		playItem = menu.add(R.string.enqueue);
		Intent intent2 = new Intent();
		intent2.putExtra("EXEC_MODE", execModeEnum.Enqueue);
		intent2.putExtra("WHERECLAUSE", "idShow = " + idShow + " and c12 = " + numeroSaison);
		
		playItem.setIntent(intent2);
		playItem.setOnMenuItemClickListener(new playVideoSelectionMenuItemClickListener());

		playItem = menu.add(R.string.clear_playlist);
		playItem.setOnMenuItemClickListener(new ClearPlaylistMenuItemClickListener());
	}

	public Long getIdShow() {
		return idShow;
	}

	public void setIdShow(Long idShow) {
		this.idShow = idShow;
	}

	public Long getNumeroSaison() {
		return numeroSaison;
	}

	public void setNumeroSaison(Long numeroSaison) {
		this.numeroSaison = numeroSaison;
	}

	public String getNomSerie() {
		return nomSerie;
	}

	public void setNomSerie(String nomSerie) {
		this.nomSerie = nomSerie;
	}

}
