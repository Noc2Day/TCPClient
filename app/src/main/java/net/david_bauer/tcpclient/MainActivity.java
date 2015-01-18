package net.david_bauer.tcpclient;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.audiofx.BassBoost;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.Telephony;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.protocol.HTTP;
import org.w3c.dom.NameList;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends ActionBarActivity {
    private ActionElementList actionElementList = new ActionElementList();
    private TCPClientClass tcpClientClass;
    private Integer PORT;
    private String IP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        checkForFirstRun();
        loadTasks();
        reloadList();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_clear_log) {
            EditText et = (EditText) findViewById(R.id.textLog);
            et.setText("");
            return true;
        }
        if(id == R.id.action_newtask) {
            LayoutInflater inflater = getLayoutInflater();
            final View v = inflater.inflate(R.layout.dialog_newtask, null);
            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            adb.setTitle("Neue Aktion erstellen");
            adb.setView(v);
            adb.setPositiveButton("Speichern", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    EditText et1 = (EditText) v.findViewById(R.id.addName);
                    EditText et2 = (EditText) v.findViewById(R.id.addTask);
                    if (et1.getText().toString().length() > 0 && et2.getText().toString().length() > 0) {
                        actionElementList.addElement(et1.getText().toString(), et2.getText().toString());
                        saveTasks();
                        reloadList();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), getString(R.string.length_is_null), Toast.LENGTH_SHORT).show();
                    }
                }
            });
            adb.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                }
            });
            adb.show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop(){
        super.onStop();
    }

    public void saveTasks() {
        File file = new File(getDir("data", MODE_PRIVATE), "map");
        ObjectOutputStream outputStream = null;
        try {
            outputStream = new ObjectOutputStream(new FileOutputStream(file));
            outputStream.writeObject(actionElementList);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadTasks() {
        File file = new File(getDir("data", MODE_PRIVATE), "map");
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(file));
            actionElementList = (ActionElementList) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void reloadList() {
        final ActionElementListAdapter adapter = new ActionElementListAdapter(actionElementList);
        final ListView myList = (ListView) findViewById(R.id.listView);
        myList.setAdapter(adapter);
        myList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long arg3) {
                TextView tvId = (TextView) view.findViewById(R.id.text3);
                actionElementList.ready = false;
                actionElementList.removeElement(position);

                reloadList();
                saveTasks();

                return false;
            }

        });
        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            // For Long Duration Toast

            TextView tvId = (TextView) arg1.findViewById(R.id.text2);
                try {
                    if (actionElementList.ready == true)
                    {
                        connect(tvId.getText().toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                actionElementList.ready = true;
            }
        });
        myList.deferNotifyDataSetChanged();
    }

    public void connect(String msg) {
        SharedPreferences settings = getSharedPreferences("SETTINGS", 0);
        Integer port = Integer.parseInt(settings.getString("PORT", ""));
        String ip = settings.getString("IP", "");
        this.IP = ip;
        this.PORT = port;
        if (ip.length() > 0 && port.toString().length() > 0) {
            try {
                tcpClientClass = new TCPClientClass(ip, port);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
            }
            try {
                String answer = tcpClientClass.sendToServer(msg);
                EditText et = (EditText) findViewById(R.id.textLog);

                int callbackType = 0;
                switch (answer.substring(0, 1))
                {
                    case "T":
                        callbackType = 1;
                        break;
                    case "F":
                        callbackType = 2;
                        break;
                }
                answer = DeleteFirst(answer);

                if (callbackType == 1)
                {
                    answer = new String(changeByte(124, answer, 10));
                    et.append(answer + "\n");
                }
                else if (callbackType == 2)
                {
                    String header = "";
                    int headerLength = (byte)answer.charAt(0);
                    int fileStartIndex = Integer.parseInt(answer.substring(4, 4));
                    int fileLengthIndex = Integer.parseInt(answer.substring(8, 4));
                    String filename = answer.substring(16, headerLength - 16);
                    char[] fileC = answer.substring(fileStartIndex, fileLengthIndex).toCharArray();
                    byte[] file = charToByteArray(fileC);

                    FileOutputStream outputStream;

                    try {
                        outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                        outputStream.write(file);
                        outputStream.close();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
            }
            try {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        tcpClientClass.closeSocket();
                    }
                }, 200);

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void checkForFirstRun() {
        SharedPreferences settings = getSharedPreferences("SETTINGS", 0);
        String port = settings.getString("PORT", "");
        String ip = settings.getString("IP", "");
        if (port.length() == 0 && ip.length() == 0) {
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("IP", "127.0.0.1");
            editor.putString("PORT", "80");
            editor.commit();
        }
    }

    public byte[] charToByteArray(char[] src)
    {
        byte[] bArray = new byte[src.length];
        for (int i = 0; i < src.length; i++)
        {
            bArray[i] = (byte)src[i];
        }
        return bArray;
    }

    public String DeleteFirst(String inputString)
    {
        String newString = "";
        char[] c = inputString.toCharArray();
        char[] tChar = c.clone();
        c = new char[tChar.length - 1];

        for (int i = 0; i < c.length; i++)
        {
            c[i] = tChar[i + 1];
            newString = newString + c[i];
        }

        return newString;
    }

    public String changeByte(int targetByte, String targetString, int newByte)
    {
        byte oByte = (byte)targetByte;
        byte nByte = (byte)newByte;

        byte[] aByte = targetString.getBytes();

        for (int i = 0; i < aByte.length; i++)
        {
            if (aByte[i] == oByte) aByte[i] = nByte;
        }
        return new String(aByte);
    }
}
