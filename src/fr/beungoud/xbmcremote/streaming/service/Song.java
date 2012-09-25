package fr.beungoud.xbmcremote.streaming.service;

import android.os.Parcel;
import android.os.Parcelable;

public class Song implements Parcelable {

	String SongName;
	String Artist;
	String Path;
	
	public Song(Parcel in) {
		Path = in.readString();
		SongName = in.readString();
		Artist = in.readString();
	}

	public Song(String path) {
		this.Path = path;

		if (path.lastIndexOf("/") == path.length())
		{
			path = path.substring(0, path.lastIndexOf("/"));
		}
		if (path.lastIndexOf("/") > 0) {
			SongName = path.substring(path.lastIndexOf("/")+1);
		} else {
			SongName = path;
		}

		Artist = "";
	}

	public String getSongName() {
		return SongName;
	}

	public void setSongName(String songName) {
		SongName = songName;
	}

	public String getArtist() {
		return Artist;
	}

	public void setArtist(String artist) {
		Artist = artist;
	}

	public String getPath() {
		return Path;
	}

	public void setPath(String path) {
		Path = path;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(Path);
		dest.writeString(SongName);
		dest.writeString(Artist);
	}
	

    public static final Parcelable.Creator<Song> CREATOR
            = new Parcelable.Creator<Song>() {
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        public Song[] newArray(int size) {
            return new Song[size];
        }
    };
    

}
