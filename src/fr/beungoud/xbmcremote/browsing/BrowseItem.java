/**
 * 
 */
package fr.beungoud.xbmcremote.browsing;

import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.MenuItem;
import fr.beungoud.util.Utils;
import fr.beungoud.xbmcremote.Constants;
import fr.beungoud.xbmcremote.R;
import fr.beungoud.xbmcremote.XbmcRequester;
import fr.beungoud.xbmcremote.commands.ClearPlaylistMenuItemClickListener;
import fr.beungoud.xbmcremote.commands.EnqueueSelectionMenuItemClickListener;
import fr.beungoud.xbmcremote.commands.StreamSelectionMenuItemClickListener;
import fr.beungoud.xbmcremote.commands.playSelectionMenuItemClickListener;

/**
 * Element de la liste lors du browse
 * 
 * @author Benoit
 * 
 */
public abstract class BrowseItem {
	/**
	 * The path's hash for thumbnail retrieving.
	 */
	private String hash;

	public enum MediaType {
		// Folder,
		Music, Video, Pictures,
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;

//		String mediaType = "Video";
//		switch (getMediaType()) {
//		case Music:
//			mediaType = "Music";
//			break;
//		case Picture:
//			mediaType = "Pictures";
//		case Video:
//			mediaType = "Video";
//		default:
//			break;
//		}
//
//		String hash = Utils.Hash(path);
//
//		setThumbPath("special://masterprofile/Thumbnails/" + mediaType + "/" + hash.substring(0, 1) + "/" + hash
//				+ ".tbn");

	}

	public MediaType getMediaType() {
		return mediaType;
	}

	public void setMediaType(MediaType mediaType) {
		this.mediaType = mediaType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setFolder(boolean isFolder) {
		this.isFolder = isFolder;
	}

	public boolean isFolder() {
		return isFolder;
	}

	private boolean isFolder;

	private String path;

	private MediaType mediaType;

	private String name;

	private String comment;

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getPlaylistId() {
		int playListId = -1;
		if (mediaType == MediaType.Music) {
			playListId = Constants.ID_PLAYLIST_MUSIC;
		} else if (mediaType == MediaType.Pictures) {
			playListId = -1;
		} else if (mediaType == MediaType.Video) {
			playListId = Constants.ID_PLAYLIST_VIDEO;
		}
		return playListId;
	}

	public abstract void buildContextMenu(ContextMenu menu, XbmcRequester requester);

	public abstract void execCommand(XbmcRequester requester, Context context);

	/**
	 * @param thumbPath
	 *            the hash to set
	 */
	public void setThumbPath(String thumbPath) {
		this.hash = thumbPath;
	}

	/**
	 * @param hash
	 *            the hash to set
	 */
	public void setHash(String mediaType, String hash) {
		setThumbPath("special://masterprofile/Thumbnails/" + mediaType + "/" + hash.substring(0, 1) + "/" + hash
				+ ".tbn");

	}

	/**
	 * @return the hash
	 */
	public String getThumbPath() {
		return hash;
	}

}
