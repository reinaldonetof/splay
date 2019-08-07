package studio.rcs.com.splayv2;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import static android.content.ContentValues.TAG;


public class TelaHome extends AppCompatActivity {

    int num = 0;
    ImageView videocheck;
    ImageView legcheck;
    Button butvideo;
    Button butleg;
    Button butstart;
    TextView textovideo;
    String nomedovideo;
    String urivideo;
    int cont = 0;
    Boolean simvid = false;
    Boolean simleg = false;
    Intent videointent;
    Intent letraintent;
    Bundle pacotemain;
    Bundle letra;
    ImageButton butInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_home);

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                //  Initialize SharedPreferences
                SharedPreferences getPrefs = PreferenceManager
                        .getDefaultSharedPreferences(getBaseContext());

                //  Create a new boolean and preference and set it to true
                boolean isFirstStart = getPrefs.getBoolean("firstStart", true);

                //  If the activity has never started before...
                if (isFirstStart) {

                    //  Launch app intro
                    final Intent i = new Intent(TelaHome.this, IntroMain.class);

                    runOnUiThread(new Runnable() {
                        @Override public void run() {
                            startActivity(i);
                        }
                    });

                    //  Make a new preferences editor
                    SharedPreferences.Editor e = getPrefs.edit();

                    //  Edit preference to make it false because we don't want this to run again
                    e.putBoolean("firstStart", false);

                    //  Apply changes
                    e.apply();
                }
            }
        });

        // Start the thread
        t.start();

        SharedPreferences sp = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        if (!sp.getBoolean("first", false)) {
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("first", true);
            editor.apply();
            Intent intent = new Intent(this, IntroMain.class); // Call the AppIntro java class
            startActivity(intent);
        }

        if(Build.VERSION.SDK_INT>22){
            requestPermissions(new String[] {"android.permission.WRITE_EXTERNAL_STORAGE"}, 2);
        }

        butstart = findViewById(R.id.butstart);
        butvideo = findViewById(R.id.butvideo);
        butleg = findViewById(R.id.butleg);
        videocheck = findViewById(R.id.imgvideocheck);
        legcheck = findViewById(R.id.imglegcheck);
        textovideo = findViewById(R.id.textovideo);
        textovideo.setMovementMethod(new ScrollingMovementMethod());
        butInfo = findViewById(R.id.ButINFO);
        butInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                botaoinfo();
            }
        });

        butstart.setVisibility(View.INVISIBLE);
        videocheck.setImageResource(R.mipmap.ic_not_checked);
        textovideo.setText("");


        butvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchMainActivity();
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();

        if (pacotemain != null) {
            nomedovideo = pacotemain.getString("arquivo");
            urivideo = pacotemain.getString("curPF");
            String formato = "";
            String mp4 = "mp4"; String avi = "avi"; String wmv = "wmv"; String gpp = "3gp";

            char[] lala;
            if (nomedovideo != null) {
                lala = (nomedovideo.toCharArray());
                int x = nomedovideo.length();
                for (int i = x - 3; i < x; i++) {
                    formato = (formato + lala[i]);
                }
                Log.i(TAG,"Tipo do formato=" + formato);
                if (mp4.equalsIgnoreCase(formato) || wmv.equalsIgnoreCase(formato) || avi.equalsIgnoreCase(formato) || gpp.equalsIgnoreCase(formato)){
                    textovideo.setText(nomedovideo);
                    cont++;
                    videocheck.setImageResource(R.mipmap.ic_checked);
                    simvid = true;
                }
                else{
                    textovideo.setText(nomedovideo);
                    cont=0;
                    videocheck.setImageResource(R.mipmap.ic_not_checked);
                    simvid = false;
                    Toast.makeText(TelaHome.this,
                            "Video selecionado não aceito!", Toast.LENGTH_SHORT).show();
                }
                Log.i(TAG,"Valor do cont="+ String.valueOf(cont));
            }
        }

        if (letra != null){
            legcheck.setImageResource(R.mipmap.ic_checked);
            simleg = true;
        }
        else{
            legcheck.setImageResource(R.mipmap.ic_not_checked);
            simleg = false;
        }

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                abrirplay(simvid,simleg, pacotemain, letra);
            }
        }, 1000);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                videointent = data;
                pacotemain = videointent.getExtras();
            }
        }

        if (requestCode == 7) {
            if(resultCode == Activity.RESULT_OK){
                letraintent = data;
                letra = letraintent.getExtras();
            }
        }
    }//onActivityResult


    private void launchMainActivity() {
        simvid = false;
        Intent video = new Intent(this, MainActivity.class);
        num = 1;

        Bundle bundle = new Bundle();
        bundle.putInt("numero", num);
        video.putExtras(bundle);
        startActivityForResult(video, 1);
    }


    public void clickButLeg(View view){
            tentar();
    }

    private void tentar (){
        simleg = false;
        Intent legenda = new Intent(TelaHome.this, AreaLegenda.class);
        startActivityForResult(legenda,7);

    }

    private void abrirplay(Boolean yeap, Boolean legap, final Bundle videolocal, final Bundle letra){
        if (yeap == true && legap == true){
            butstart.setVisibility(View.VISIBLE);
            butstart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent ultima = new Intent(TelaHome.this, TelaPlay.class);
                    ultima.putExtra("video",videolocal);
                    ultima.putExtra("letra",letra);
                    startActivity(ultima);
                }
            });
        }
    }

     public void botaoinfo() {
                 AlertDialog.Builder alertdialog = new AlertDialog.Builder(TelaHome.this);
                         alertdialog
                         .setTitle("Tutorial")
                         .setMessage("Você deseja ir para o tutorial, aprender o passo a passo?")
                         .setPositiveButton("Sim",
                                 new DialogInterface.OnClickListener() {
                                     @Override
                                     public void onClick(DialogInterface dialog, int which) {
                                         Intent intent = new Intent(TelaHome.this, IntroMain.class);
                                         startActivity(intent);
                                     }
                                 })
                         .setNegativeButton("Não",
                                 new DialogInterface.OnClickListener() {
                                     @Override
                                     public void onClick(DialogInterface dialog, int which) {
                                         dialog.dismiss();
                                     }
                                 })
                         .create();
                 alertdialog.show();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 2: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(TelaHome.this, "Permission Granted, Now you can access app without bug.",Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(this,"Permission Denied, App maybe get crashed.", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }
}