/**
 * 
 */
package fr.beungoud.xbmcremote;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * @author Benoit
 *
 */
public class ConnexionErrorActivity extends Activity {
	
	TextView message;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connexion_error);

        setTheme(android.R.style.Theme_Dialog);

        message = (TextView) findViewById(R.id.ErreurMessage);

        message.setText(getText(R.string.error_connexion_message));

//        
//        button.setOnClickListener(
//        		new View.OnClickListener() {
//					
//					@Override
//					public void onClick(View v) {
//  			
//				    	Intent intent = new Intent();
//				    	intent.putExtra((String) getText(R.string.key_ip_address), ipText.getText().toString());
//		    			intent.putExtra((String) getText(R.string.key_login), loginText.getText().toString());
//		    			intent.putExtra((String) getText(R.string.key_password), passwordText.getText().toString());
//		    			intent.putExtra((String) getText(R.string.key_port), Integer.parseInt(portText.getText().toString()));
//				    	setResult(RESULT_OK, intent);
//				    	finish();
//					}
//				});
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
       }
}
