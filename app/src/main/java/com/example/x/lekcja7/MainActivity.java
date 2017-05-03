package com.example.x.lekcja7;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private ListView lv;
    private String[] tele;
    private Class[] activities={Tele1Activity.class, Tele2Activity.class, Tele3Activity.class};

    static final private int ALERT_DIALOG_PLAIN = 1;
    static final private int ALERT_DIALOG_BUTTONS = 2;
    static final private int ALERT_DIALOG_LIST = 3;
    static final private int CUSTOM_ALERT_DIALOG = 4;
    private Button btnNewAlertDialog;
    private Button btnNewAlertDialogButton;
    private Button btnNewAlertDialogList;
    private Button btnNewCustomAlertDialog;
    private Button nagrajAudio;
    private Button zatrzymajAudio;
    private Button odtworzNagranie;
    private Button zatrzymajNagranie;

    private MediaPlayer mediaPlayer;
    int music = -1;

    private static final int RECORD_REQUEST_CODE = 101;
    private MediaPlayer m;
    private MediaRecorder myAudioRecorder;
    String mFileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv = (ListView) findViewById(R.id.telephones);

        btnNewAlertDialog = (Button) findViewById(R.id.btnNewAlertDialog);
        btnNewAlertDialogButton = (Button) findViewById(R.id.btnNewAlertDialogButton);
        btnNewAlertDialogList = (Button) findViewById(R.id.btnNewAlertDialogList);
        btnNewCustomAlertDialog = (Button) findViewById(R.id.btnNewCustomAlertDialog);
        nagrajAudio = (Button) findViewById(R.id.nagrajAudio);
        zatrzymajAudio = (Button) findViewById(R.id.zatrzymajAudio);
        zatrzymajAudio.setEnabled(false);
        odtworzNagranie = (Button) findViewById(R.id.odtworzNagranie);
        odtworzNagranie.setEnabled(false);
        zatrzymajNagranie = (Button) findViewById(R.id.zatrzymajNagranie);
        zatrzymajNagranie.setEnabled(false);

        initResources();
        initTelephonesListView();
        initButtonsClick();
    }

    private void initResources(){
        Resources res = getResources();
        tele = res.getStringArray(R.array.telephones);
    }

    private void initTelephonesListView(){
        lv.setAdapter(new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1,tele));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View v, int pos, long id){
                Context context;
                context = getApplicationContext();
                Intent intent = new Intent(context,activities[pos]);
                startActivity(intent);
            }
        });
    }

    private void initButtonsClick() {
        View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btnNewAlertDialog:
                        showDialog(ALERT_DIALOG_PLAIN);
                        break;
                    case R.id.btnNewAlertDialogButton:
                        showDialog(ALERT_DIALOG_BUTTONS);
                        break;
                    case R.id.btnNewAlertDialogList:
                        showDialog(ALERT_DIALOG_LIST);
                        break;
                    case R.id.btnNewCustomAlertDialog:
                        showDialog(CUSTOM_ALERT_DIALOG);
                        break;
                    case R.id.nagrajAudio:
                        try {
                            nagraj();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case R.id.zatrzymajAudio:
                        funkcjazatrzymajAudio();
                        break;
                    case R.id.odtworzNagranie:
                        try {
                            funkcjaodtworzNagranie();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case R.id.zatrzymajNagranie:
                        funkcjazatrzymajNagranie();
                        break;
                    default:
                        break;
                }
            }
        };
        btnNewAlertDialog.setOnClickListener(listener);
        btnNewAlertDialogButton.setOnClickListener(listener);
        btnNewAlertDialogList.setOnClickListener(listener);
        btnNewCustomAlertDialog.setOnClickListener(listener);
        nagrajAudio.setOnClickListener(listener);
        zatrzymajAudio.setOnClickListener(listener);
        odtworzNagranie.setOnClickListener(listener);
        zatrzymajNagranie.setOnClickListener(listener);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case ALERT_DIALOG_PLAIN:
                return createPlainAlertDialog();
            case ALERT_DIALOG_BUTTONS:
                return createAlertDialogWithButtons();
            case ALERT_DIALOG_LIST:
                return createAlertDialogWithList();
            case CUSTOM_ALERT_DIALOG:
                return createCustomAlertDialog();
            default:
                return null;
        }
    }

    private Dialog createPlainAlertDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Prosty dialog");
        dialogBuilder.setMessage("Wiadomość prostego dialogu");
        return dialogBuilder.create();
    }

    private Dialog createAlertDialogWithButtons() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Wyjście");
        dialogBuilder.setMessage("Czy napewno?");
        dialogBuilder.setCancelable(false);
        dialogBuilder.setPositiveButton("Tak", new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                showToast("Wychodzę");
                MainActivity.this.finish();
            }
        });
        dialogBuilder.setNegativeButton("Nie", new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                showToast("Anulowaleś wyjście");
            }
        });
        return dialogBuilder.create();
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    private Dialog createAlertDialogWithList() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final String[] options = {"Pierwsza piosenka", "Druga piosenka", "Trzecia piosenka"};
        dialogBuilder.setTitle("Lista opcji");
        dialogBuilder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {
                showToast("Wybrałeś: " + options[position]);
                switch (position){
                    case 0:
                        music = R.raw.aidu;
                        break;
                    case 1:
                        music = R.raw.aidu1;
                        break;
                    case 2:
                        music = R.raw.aidu2;
                        break;
                }
            }
        });
        return dialogBuilder.create();
    }

    private Dialog createCustomAlertDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        View layout = getCustomDialogLayout();
        dialogBuilder.setView(layout);
        dialogBuilder.setTitle("Custom Dialog");
        return dialogBuilder.create();
    }

    private View getCustomDialogLayout() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(R.layout.wlasny_layout, (ViewGroup)findViewById(R.id.wlasny));
    }

    public void playSound(View view){
        if(mediaPlayer != null) {
            mediaPlayer.release();
        }

        if(music != -1) {
            mediaPlayer = MediaPlayer.create(this, music);
            mediaPlayer.start();
        }
    }

    public void stopSound(View view){
        if(mediaPlayer != null)
            mediaPlayer.stop();
    }

    private Boolean exit = false;

    @Override
    public void onBackPressed() {
        if(exit) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Press Back again to Exit.", Toast.LENGTH_SHORT).show();
            exit = true;
        }
    }

    protected void makeRequest() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                RECORD_REQUEST_CODE);
    }

    public void zapisywanie() {
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/audiorecordtest.3gp";
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myAudioRecorder.setOutputFile(mFileName);
    }

    public void nagraj() throws IOException {
        zatrzymajAudio.setEnabled(true);
        odtworzNagranie.setEnabled(true);
        zatrzymajNagranie.setEnabled(true);
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionCheck2 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED || permissionCheck2 !=
                PackageManager.PERMISSION_GRANTED) {
            makeRequest();
        } else {
            funkcjanajgrajAudio();

        }
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RECORD_REQUEST_CODE: {
                if (grantResults.length == 0
                        || grantResults[0] !=
                        PackageManager.PERMISSION_GRANTED || grantResults[1] !=
                        PackageManager.PERMISSION_GRANTED) {
                } else {
                    try {
                        funkcjanajgrajAudio();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return;
            }
        }
    }

    public void funkcjanajgrajAudio() throws IOException {
        myAudioRecorder = new MediaRecorder();
        zapisywanie();
        myAudioRecorder.prepare();
        myAudioRecorder.start();
    }

    public void funkcjazatrzymajAudio() {
        myAudioRecorder.stop();
        myAudioRecorder.release();
        myAudioRecorder = null;
    }

    public void funkcjaodtworzNagranie() throws IOException {
        m = new MediaPlayer();
        m.setDataSource(mFileName);
        m.prepare();
        m.start();
    }

    public void funkcjazatrzymajNagranie()  {
        m.stop();
        m.release();
        m = null;
    }

}

