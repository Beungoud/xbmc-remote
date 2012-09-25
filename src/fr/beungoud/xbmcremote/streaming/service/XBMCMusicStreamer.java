package fr.beungoud.xbmcremote.streaming.service;

import java.util.List;

import android.os.RemoteException;

public class XBMCMusicStreamer extends IXBMCMusicStreamer.Stub {
	StreamingService service ;
	
	public XBMCMusicStreamer(StreamingService service)
	{
		this.service = service;
	}

	@Override
	public void clearPlaylist() throws RemoteException {
		service.clearPlayList();
	}

	@Override
	public int getCurrentSong() throws RemoteException {
		return service.getCurrentSong();
	}

	@Override
	public int getCurrentStatus() throws RemoteException {
		return 0;
	}

	@Override
	public java.util.List<Song> getPlaylist() throws RemoteException {
		return service.getPlayList();
	}
	

	@Override
	public void pause() throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void playFile(int position) throws RemoteException {
		service.setCurrentSong(position);
		service.play();
	}

	@Override
	public void setPlaylist(List<String> songs) throws RemoteException {
		service.setPlayList(songs);
	}

	@Override
	public void skipBack() throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void skipForward() throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stop() throws RemoteException {
		service.stop();
	}
}
