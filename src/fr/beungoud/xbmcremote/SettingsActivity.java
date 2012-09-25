/**
 * 
 */
package fr.beungoud.xbmcremote;

import fr.beungoud.util.ConfigurationAccess;
import fr.beungoud.util.ConfigurationDbOpenHelper;
import fr.beungoud.util.Server;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.sax.TextElementListener;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

/**
 * @author Benoit
 * 
 */
public class SettingsActivity extends Activity {

	private EditText ipText;
	private EditText portText;
	private EditText loginText;
	private EditText passwordText;
	private Spinner serverSpinner;
	private CheckBox enableThumbs;
	protected boolean isUpdating = false;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);

		ipText = (EditText) findViewById(R.id.AdresseIp);
		portText = (EditText) findViewById(R.id.Port);
		loginText = (EditText) findViewById(R.id.Login);
		passwordText = (EditText) findViewById(R.id.Password);
		serverSpinner = (Spinner) findViewById(R.id.ServerList);
		enableThumbs = (CheckBox) findViewById(R.id.enableThumbs);

		Button button = (Button) findViewById(R.id.ButtonOK);
		Button helpButton = (Button) findViewById(R.id.helpButton);

		Button addServerButton = (Button) findViewById(R.id.addServerButton);
		Button removeServerButton = (Button) findViewById(R.id.removeServerButton);
		Button renameServerButton = (Button) findViewById(R.id.renameServerButton);
		
		enableThumbs.setChecked(ConfigurationAccess.getInstance().isThumbnailEnabled());
		
		enableThumbs.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

				ConfigurationAccess.getInstance().setThumbnailEnabled(isChecked);
			}
		});
		
		update();

		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent();
				intent.putExtra((String) getText(R.string.key_ip_address), ipText.getText().toString());
				intent.putExtra((String) getText(R.string.key_login), loginText.getText().toString());
				intent.putExtra((String) getText(R.string.key_password), passwordText.getText().toString());
				intent.putExtra((String) getText(R.string.key_port), Integer.parseInt(portText.getText().toString()));
				setResult(RESULT_OK, intent);
				finish();
			}
		});
		final Context context = this;
		helpButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, HelpActivity.class);
				context.startActivity(intent);
			}
		});

		removeServerButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				final Server server = (Server) serverSpinner.getSelectedItem();
				if (server != null) {
					AlertDialog.Builder builder = new AlertDialog.Builder(context);
					builder.setCancelable(true);
					builder.setTitle("Delete current server?");
					TextView tv = new TextView(context);
					tv.setText("Are you sure you wan't to delete server : " + server.getServerName());

					builder.setView(tv);

					builder.setInverseBackgroundForced(false);
					builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							ConfigurationAccess.getInstance().deleteServer(server);
							update();
							dialog.dismiss();
						}
					});
					builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
					AlertDialog alert = builder.create();
					alert.show();
				}
			}
		});

		addServerButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setCancelable(true);
				builder.setTitle("Create new server");
				builder.setInverseBackgroundForced(false);

				TextView tv = new TextView(context);
				tv.setText("Please, enter server name");

				final EditText et = new EditText(context);
				et.setText("ServerName");

				LinearLayout layout = new LinearLayout(context);
				layout.setOrientation(LinearLayout.VERTICAL);

				layout.addView(tv);
				layout.addView(et);

				builder.setView(layout);

				builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();

						Server server = new Server();
						server.setServerName(et.getText().toString());

						ConfigurationAccess.getInstance().addServer(server);

						update();
					}
				});
				builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				AlertDialog alert = builder.create();
				alert.show();
			}
		});

		renameServerButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final Server server = (Server) serverSpinner.getSelectedItem();
				if (server != null) {
					AlertDialog.Builder builder = new AlertDialog.Builder(context);
					builder.setCancelable(true);
					builder.setTitle("Rename server");
					builder.setInverseBackgroundForced(false);

					TextView tv = new TextView(context);
					tv.setText("Please, enter new server name");

					final EditText et = new EditText(context);
					et.setText(server.getServerName());

					LinearLayout layout = new LinearLayout(context);
					layout.setOrientation(LinearLayout.VERTICAL);

					layout.addView(tv);
					layout.addView(et);

					builder.setView(layout);

					builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();

							server.setServerName(et.getText().toString());

							ConfigurationAccess.getInstance().updateServer(server);

							update();
						}

					});
					builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
					AlertDialog alert = builder.create();
					alert.show();
				}
			}
		});
	}

	protected void update() {

		Server[] serverList = ConfigurationAccess.getInstance().getServers();

		ArrayAdapter<Server> adapter = new ArrayAdapter<Server>(this, android.R.layout.simple_spinner_item, serverList);

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		serverSpinner.setAdapter(adapter);

		updateSelection();

		serverSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<? extends Adapter> adapterView, View view, int arg2, long arg3) {
				// Spinner spinner = (Spinner)adapterView;
				// Server server = (Server)((spinner.getSelectedItem()));
				// server.setCurrent(true);
				//				
				// ConfigurationAccess.getInstance().updateServer(server);

				updateSelection();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				updateSelection();
			}
		});

		TextWatcher watcher = new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
					
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				if (!isUpdating)
				{
					commit();
				}
				Log.d("XBMC", "Commit fait");
			}
		};
		
//		ipText.setOnKeyListener(listener);
		ipText.addTextChangedListener(watcher);
//		portText.setOnKeyListener(listener);
		portText.addTextChangedListener(watcher);
//		loginText.setOnKeyListener(listener);
		loginText.addTextChangedListener(watcher);
//		passwordText.setOnKeyListener(listener);
		passwordText.addTextChangedListener(watcher);

	}

	private void commit() {
		Server server = (Server) serverSpinner.getSelectedItem();

		if (server != null) {

			server.setHostname(ipText.getText().toString());
			
			try {
				server.setPort(Integer.parseInt(portText.getText().toString().trim()));
			} catch (NumberFormatException e) {
				server.setPort(8080);
			}
			
			server.setLogin(loginText.getText().toString());
			server.setPassword(passwordText.getText().toString());

			ConfigurationAccess.getInstance().updateServer(server);
		}

	}

	private void updateSelection() {

		Server server = (Server) serverSpinner.getSelectedItem();

		if (server != null) {
			isUpdating = true;

			ipText.setText(server.getHostname());

			loginText.setText(server.getLogin());

			passwordText.setText(server.getPassword());

			portText.setText(Integer.toString(server.getPort()));
			
			isUpdating = false;
		}

	}

	@Override
	protected void onPause() {
		super.onPause();

	}
}
