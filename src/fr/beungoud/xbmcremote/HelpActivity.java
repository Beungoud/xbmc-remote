/**
 * 
 */
package fr.beungoud.xbmcremote;

import java.util.ResourceBundle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

/**
 * @author Benoit
 *
 */
public class HelpActivity extends Activity {

	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.helpview);

        WebView webView = (WebView)findViewById(R.id.helpHtmlView);
        

        // les définitions de type mime et de l'encodage
        final String mimeType = Messages.getString("help_activity.mime_type"); //$NON-NLS-1$
         final String encoding = Messages.getString("help_activity.encoding"); //$NON-NLS-1$
         WebView objetview;
  
         
        //mon code html 
         String mapage=Messages.getString("help_activity.help_text"); //$NON-NLS-1$
  

         webView.loadData(mapage, mimeType, encoding);
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    	

    }
}
