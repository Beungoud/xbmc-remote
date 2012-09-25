package fr.beungoud.xbmcremote.musicstreamer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.apache.http.entity.AbstractHttpEntity;

import android.util.Log;
import fr.beungoud.util.Base64;

public class Base64InputStreamEntity extends AbstractHttpEntity {

	long length;

	InputStream stream;

	BufferedReader bufferedReader = null;

	boolean isStreaming = true;

	public Base64InputStreamEntity(InputStream stream, long length) {
		this.length = length;
		this.stream = stream;
	}

	@Override
	public InputStream getContent() throws IOException, IllegalStateException {
		throw new UnsupportedOperationException("Entity template does not implement getContent()");
	}

	@Override
	public long getContentLength() {
		return length;
	}

	@Override
	public boolean isRepeatable() {
		return false;
	}

	@Override
	public boolean isStreaming() {
		return isStreaming;
	}

	@Override
	public void writeTo(OutputStream outstream) throws IOException {
		if (isStreaming) {
			if (bufferedReader == null) {
				bufferedReader = new BufferedReader(new InputStreamReader(stream));
			}

			String ligne;
			while ((ligne = bufferedReader.readLine()) != null) {
				ligne = ligne.replace("<html>", "");
				ligne = ligne.replace("</html>", "");
				byte[] tabByte = Base64.decode(ligne);

				outstream.write(tabByte);
		
			}
			bufferedReader.close();
			stream.close();
			isStreaming = false;
		}
	}

}
