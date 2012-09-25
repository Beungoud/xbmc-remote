package fr.beungoud.xbmcremote.streaming;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import fr.beungoud.xbmcremote.browsing.BrowseItemView;
import fr.beungoud.xbmcremote.streaming.service.Song;

public class PlaylistAdapter extends BaseAdapter {

	List<Song> playlist;
	private int currentSong;

	public PlaylistAdapter() {
		playlist = new ArrayList<Song>();
	}

	public void setPlaylist(List<Song> playlist) {
		this.playlist = playlist;
		this.notifyDataSetChanged();
		this.notifyDataSetInvalidated();
	}

	@Override
	public int getCount() {
		return playlist.size();
	}

	@Override
	public Object getItem(int position) {
		return playlist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void setNowPlaying(int currentSong) {
		this.currentSong = currentSong;
		this.notifyDataSetInvalidated();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		PlaylistItemView TV = (PlaylistItemView) convertView;
		if (TV == null) {
			TV = new PlaylistItemView(parent.getContext());
		}
		Song song = playlist.get(position);
		TV.setTitle(song.getSongName());
		TV.setSubtitle(song.getPath());
		if (currentSong == position) {
			TV.setNowPlaying(true);
		} else {
			TV.setNowPlaying(false);
		}
		return TV;
	}

}
