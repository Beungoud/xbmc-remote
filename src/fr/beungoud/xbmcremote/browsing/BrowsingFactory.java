/**
 * 
 */
package fr.beungoud.xbmcremote.browsing;

import android.app.Activity;
import android.content.Intent;
import fr.beungoud.xbmcremote.R;
import fr.beungoud.xbmcremote.XbmcRequester;
import fr.beungoud.xbmcremote.browsing.BrowseItem.MediaType;
import fr.beungoud.xbmcremote.browsing.database.BrowsingThread;
import fr.beungoud.xbmcremote.browsing.database.DatabaseBrowsingThread;
import fr.beungoud.xbmcremote.browsing.database.FileBrowsingThread;
import fr.beungoud.xbmcremote.browsing.database.typed.AlbumBrowsingSource;
import fr.beungoud.xbmcremote.browsing.database.typed.ArtistBrowsingSource;
import fr.beungoud.xbmcremote.browsing.database.typed.BrowsingSource;
import fr.beungoud.xbmcremote.browsing.database.typed.MainMenuBrowsingSource;
import fr.beungoud.xbmcremote.browsing.database.typed.MainMenuDbBrowseItem;
import fr.beungoud.xbmcremote.browsing.database.typed.MovieBrowsingSource;
import fr.beungoud.xbmcremote.browsing.database.typed.SerieEpisodeBrowsingSource;
import fr.beungoud.xbmcremote.browsing.database.typed.SerieSaisonBrowsingSource;
import fr.beungoud.xbmcremote.browsing.database.typed.SerieTitleBrowsingSource;
import fr.beungoud.xbmcremote.browsing.database.typed.SongBrowsingSource;
import fr.beungoud.xbmcremote.browsing.database.typed.TypedBrowsingActivity;

/**
 * @author Benoit
 *
 */
public class BrowsingFactory {
	
	
	public enum IntentSourceEnum {
		// Définit de type de browsing qu'on fait.
		TypeBrowsing, 
		// Défini l'Id Artist
		IdArtist, 
		// Défini l'id d'un album spécifique.
		IdAlbum,
		// Le nom d'un artist
		NomArtist,
		// Le nom d'un album
		NomAlbum,
		// Id de la série
		IdSerie,
		// Nom de la série
		NomSerie,
		// Le numero de la saison.
		NumeroSaison,
	}
	
	
	public enum TypeBrowsingEnum {
		MAIN_MENU,
		MOVIES, ARTIST, ALBUM, SONGS,
		SERIE_TITLE, SERIE_SEASONS, SERIE_EPISODES
	}

	
	public static BrowsingThread getBrowsingThread(String browsingType, String browsePath, BrowsingActivity activity,
			MediaType mediaType, XbmcRequester requester)
	{
		BrowsingThread retour = null;
		if (activity.getText(R.string.browsing_type_database).equals(browsingType))
		{
			retour = new DatabaseBrowsingThread(browsePath, activity, mediaType, requester);
		} else if (activity.getText(R.string.browsing_type_file).equals(browsingType))
		{
			retour = new FileBrowsingThread(browsePath, activity, mediaType, requester);
		}
		return retour;
	}
	
	
	
	
	public static BrowsingSource getBrowsingSource(Intent intent, Activity activity, XbmcRequester requester)
	{
		BrowsingSource retour = null;

		TypeBrowsingEnum typeBrowsing = (TypeBrowsingEnum)intent.getExtras().get(IntentSourceEnum.TypeBrowsing.toString());
		
		if (typeBrowsing==TypeBrowsingEnum.MOVIES)
		{
			retour = new MovieBrowsingSource(intent, activity, requester);
		}
		if (typeBrowsing==TypeBrowsingEnum.MAIN_MENU)
		{
			retour = new MainMenuBrowsingSource(intent, activity, requester);
		}
		
		if (typeBrowsing==TypeBrowsingEnum.ARTIST)
		{
			retour = new ArtistBrowsingSource(intent, activity, requester);
		}
		
		if (typeBrowsing==TypeBrowsingEnum.ALBUM)
		{
			retour = new AlbumBrowsingSource(intent, activity, requester);
		}		
		
		if (typeBrowsing==TypeBrowsingEnum.SONGS)
		{
			retour = new SongBrowsingSource(intent, activity, requester);
		}	
		
		if (typeBrowsing==TypeBrowsingEnum.SERIE_TITLE)
		{
			retour = new SerieTitleBrowsingSource(intent, activity, requester);
		}
		
		if (typeBrowsing==TypeBrowsingEnum.SERIE_SEASONS)
		{
			retour = new SerieSaisonBrowsingSource(intent, activity, requester);
		}
		
		
		if (typeBrowsing==TypeBrowsingEnum.SERIE_EPISODES)
		{
			retour = new SerieEpisodeBrowsingSource(intent, activity, requester);
		}
			
		return retour;
	}
}
