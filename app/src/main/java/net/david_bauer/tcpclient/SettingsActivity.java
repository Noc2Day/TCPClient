package net.david_bauer.tcpclient;

import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class SettingsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        loadSettings();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop(){
        super.onStop();
        saveSettings();
    }

    protected void saveSettings(View v) {
        saveSettings();
    }
    protected void saveSettings() {
        SharedPreferences settings = getSharedPreferences("SETTINGS", 0);
        SharedPreferences.Editor editor = settings.edit();
        EditText ip = (EditText) findViewById(R.id.editIP);
        EditText port = (EditText) findViewById(R.id.editPort);
        editor.putString("IP", ip.getText().toString());
        editor.putString("PORT", port.getText().toString());
        editor.commit();
    }
    protected void loadSettings() {
        // Restore preferences
        SharedPreferences settings = getSharedPreferences("SETTINGS", 0);
        EditText ip = (EditText) findViewById(R.id.editIP);
        EditText port = (EditText) findViewById(R.id.editPort);
        ip.setText(settings.getString("IP", ""));
        port.setText(settings.getString("PORT", ""));
    }
}
