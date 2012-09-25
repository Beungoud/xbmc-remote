package fr.beungoud.xbmcremote.browsing.database.typed;

import android.content.Intent;
import android.view.ContextMenu;
import android.view.MenuItem;
import fr.beungoud.util.Utils;
import fr.beungoud.xbmcremote.R;
import fr.beungoud.xbmcremote.XbmcRequester;
import fr.beungoud.xbmcremote.browsing.BrowsingActivity;
import fr.beungoud.xbmcremote.commands.ClearPlaylistMenuItemClickListener;
import fr.beungoud.xbmcremote.commands.playVideoSelectionMenuItemClickListener;
import fr.beungoud.xbmcremote.commands.playVideoSelectionMenuItemClickListener.execModeEnum;

public class SerieTitleDbBrowseItem extends AbstractDbBrowseItem {

	Long idSerie;

	@Override
	public void buildContextMenu(ContextMenu menu, XbmcRequester requester) {

		MenuItem playItem = menu.add(R.string.play);
		Intent intent = new Intent();
		intent.putExtra("WHERECLAUSE", "idShow = " + idSerie);
		intent.putExtra("EXEC_MODE",
				fr.beungoud.xbmcremote.commands.playVideoSelectionMenuItemClickListener.execModeEnum.Play);
		playItem.setIntent(intent);
		playItem.setOnMenuItemClickListener(new playVideoSelectionMenuItemClickListener());
		//
		playItem = menu.add(R.string.enqueue);
		Intent intent2 = new Intent();
		intent2.putExtra("EXEC_MODE", execModeEnum.Enqueue);
		intent2.putExtra("WHERECLAUSE", "idShow = " + idSerie);

		playItem.setIntent(intent2);
		playItem.setOnMenuItemClickListener(new playVideoSelectionMenuItemClickListener());

		playItem = menu.add(R.string.clear_playlist);
		playItem.setOnMenuItemClickListener(new ClearPlaylistMenuItemClickListener());
	}

	public Long getIdSerie() {
		return idSerie;
	}

	public void setIdSerie(Long idSerie) {
		this.idSerie = idSerie;
	}

	@Override
	public void backgroundExec() {
		String hash = Utils.Hash(getPath());
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			//
		}
		setName(getName() );
		setComment(hash);
//		if (getActivity() instanceof BrowsingActivity) {
//			BrowsingActivity browsingActivity = (BrowsingActivity) getActivity();
//			browsingActivity.getHandler().sendEmptyMessage(BrowsingActivity.INVALIDATE_LIST);
//
//		}

	}

}
