package fr.beungoud.xbmcremote.streaming.service;
import fr.beungoud.xbmcremote.streaming.service.Song;

interface IXBMCMusicStreamer {
        void clearPlaylist();
//        void addSongPlaylist( in String song );
        void setPlaylist( in List<String> songs );
        List<Song> getPlaylist( );
        void playFile( in int position );
        int getCurrentSong();
        
        int getCurrentStatus();        

        void pause();
        void stop();
        void skipForward();
        void skipBack();
}
