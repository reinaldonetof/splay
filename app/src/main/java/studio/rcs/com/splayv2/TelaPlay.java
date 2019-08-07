package studio.rcs.com.splayv2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.provider.DocumentFile;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLDecoder;

import static android.support.constraint.Constraints.TAG;

//import static android.content.ContentValues.TAG;

public class TelaPlay extends Activity {
    TextView letraatual;
    TextView letraapos;
    Button botaoPlay;
    Button botaoInicio;
    Button botaoFim;
    VideoView videoframe;
    int valor = 0;
    int completo = 0;
    int leg = 0;
    private static String texto1;
    String[] texto4;
    String texto2;
    String tempinicial;
    String tempfinal;
    String nova;
    public VideoView videoescolhido;
    String recebe;
    String textopara = "";
    String LOCAL;
    String SDcard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_play);

        if(Build.VERSION.SDK_INT>22) {
            requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
        }
        //
        Intent chegou = getIntent();

        //
        texto1 = chegou.getExtras().getBundle("video").getString("arquivo");
        texto2 = chegou.getExtras().getBundle("letra").getString("tamanho");
        String texto3 = chegou.getExtras().getBundle("video").getString("curPF");
        LOCAL = chegou.getExtras().getBundle("video").getString("local");
        texto4 = chegou.getExtras().getBundle("letra").getStringArray("Separado");

        //
        diretorio(texto3);
        char[] lala;
        StringBuilder formato = new StringBuilder(); //pra concatenar ate formar o formato
        lala = (recebe.toCharArray());
        int x = recebe.length();
        for (int i = x - 3; i < x; i++) {
            formato.append(lala[i]);
        }
        texto1 = texto1.replace(formato.toString(), "srt");

        //
        botaoInicio = findViewById(R.id.botaoinicio);
        botaoPlay = findViewById(R.id.botaoinicioMesmo);
        botaoFim = findViewById(R.id.botaofinal);
        letraatual = findViewById(R.id.letraatual);
        letraapos = findViewById(R.id.letraapos);
        videoescolhido = findViewById(R.id.videoView2);

        Log.i(TAG, texto3);
        Uri videoplay = Uri.parse(texto3);

        letraapos.setText(texto3);

        try {
            // Start the MediaController
            MediaController mediaController = new MediaController(TelaPlay.this);
            mediaController.setAnchorView(videoescolhido);

            //URI para o video
            videoescolhido.setMediaController(mediaController);
            videoescolhido.setVideoURI(videoplay);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        videoescolhido.requestFocus();

        videoescolhido.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer mp) {
            }
        });
    }

    public void onResume() {
        super.onResume();
    }

    @SuppressLint("DefaultLocale") //por advertencia do androidstudio
    public void startar(View view) {
        int teste = videoescolhido.getCurrentPosition();

        tempinicial = String.format("%02d:%02d:%02d,%03d", teste / 3600000, (teste / 60000) % 60, (teste / 1000) % 60, teste % 1000);
        Log.i(TAG, "TESTAR MILISEGUNDOS PARA STR= " + tempinicial);

        botaoInicio.setTextColor(0xaaa);
        botaoInicio.setEnabled(false);
        botaoFim.setTextColor(0xffaa66cc);
        botaoFim.setEnabled(true);
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"}) //por advertencia do androidstudio, defaullocale pelo .format e o settext pelo finished subtitle!
    public void finallegenda(View view) {
        String paratxt = texto4[leg];
        int teste = videoescolhido.getCurrentPosition();
        tempfinal = String.format("%02d:%02d:%02d,%03d", teste / 3600000, (teste / 60000) % 60, (teste / 1000) % 60, teste % 1000);
        leg++;
        if (leg != Integer.valueOf(texto2)) {
            letraatual.setText(texto4[leg]);
            int a = leg + 1;
            if (a != Integer.valueOf(texto2)) {
                letraapos.setText(texto4[a]);
            } else {
                letraapos.setText("");
            }
            botaoFim.setTextColor(0xaaa);
            botaoFim.setEnabled(false);
            botaoInicio.setTextColor(0xffaa66cc);
            botaoInicio.setEnabled(true);
        }

        String textopara2 = String.valueOf(leg) + "\n" + tempinicial + " --> " + tempfinal + "\n" + paratxt + "\n\n";
        textopara = textopara + textopara2;
        Log.i(TAG, textopara2);
        Log.i(TAG, "TEXTOPARA = " + textopara);
        Log.i(TAG, "Quantidade do LEG=" + Integer.toString(leg) + "Tamanho=" + texto2);

        if (leg == Integer.valueOf(texto2)) {
            letraatual.setText("");
            letraatual.setText("Finished Subtitle!");
            SaveTxt(textopara);
            botaoInicio.setVisibility(View.INVISIBLE);
            botaoFim.setVisibility(View.INVISIBLE);

        }
    }


    public void inicio(View view) {

            Toast.makeText(TelaPlay.this,
                    "O vídeo irá iniciar em 3 seg!", Toast.LENGTH_SHORT).show();
            valor++;
            completo = 0;
            videoframe = findViewById(R.id.VideoFrame);
            Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.countdown);
            videoframe.setVideoURI(uri);
            videoframe.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                public void onPrepared(MediaPlayer mframe) {
                    mframe.setVolume(0f, 0f);
                    videoframe.start();
                    letraatual.setText(texto4[leg]);
                    int a = leg + 1;
                    letraapos.setText(texto4[a]);
                    botaoPlay.setVisibility(View.INVISIBLE);
                }
            });
            videoframe.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    videoframe.setVisibility(View.INVISIBLE);
                    botaoInicio.setVisibility(View.VISIBLE);
                    botaoFim.setVisibility(View.VISIBLE);
                    videoescolhido.start();
                    botaoFim.setTextColor(0xaaa);
                    botaoFim.setEnabled(false);
                    valor = 0;
                    completo++;
                }
            });
//        }
    }

    public void diretorio(String caminhopasta) {
        String[] cam = caminhopasta.split("/");
        int last = cam.length;
        String semarquivo = cam[last - 1];
        recebe = semarquivo;
        Log.i(TAG, "LIXO= " + semarquivo);

        nova = caminhopasta.replace(semarquivo, "");
        Log.i(TAG, "Caminho sem o arquivo= " + nova);
    }

    public void SaveTxt(String data) {
        try {

            Uri uriparse = Uri.parse(nova);
            File PASTAARQUIVO = new File(nova,texto1);
            final boolean mkdirs = PASTAARQUIVO.mkdirs();
            Log.v("","" + mkdirs);

            Log.i(TAG, "DIRETORIO =" + PASTAARQUIVO + "NOME DO ARQUIVO =" + texto1);
            Log.i(TAG, "DATA1 = " + data);


            FileOutputStream fos;
            fos = new FileOutputStream(PASTAARQUIVO);
            fos.write(data.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            Log.i(TAG, "ARQUIVO NAO LOCALIZADO!");
            SDcard = data;
            new AlertDialog.Builder(this)
                    .setTitle("ARQUIVO .srt")
                    .setMessage("O arquivo " +
                            "   "  + texto1 + ", será salvo na pasta padrão SPlay/Legendas !")
                    .setPositiveButton("Voltar a Tela Inicial!",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(TelaPlay.this,TelaHome.class);
                                    startActivity(intent);
                                }
                            })
                    .setNegativeButton("Continuar nesta tela!",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                    .show();
            salvarNaPasta();
//            new AlertDialog.Builder(this)
//                    .setTitle("Criar .srt na Memoria externa!")
//                    .setMessage("Tem certeza que deseja criar este arquivo na memória externa?" +
//                            "   "  + texto1)
//                    .setPositiveButton("Sim",
//                            new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
//                                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                                    intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//                                    startActivityForResult(intent, 54);
//                                }
//                            })
//                    .setNegativeButton("Não",
//                            new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    salvarNaPasta();
//                                }
//                            })
//                    .show();

        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, "ERRO NO IO");
        }

    }


    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        if (requestCode == 54) {
            if (resultCode == RESULT_OK) {
                Uri treeUri = resultData.getData();
                Log.i(TAG, "TREEURI = " + treeUri.toString());
                DocumentFile pickedDir = DocumentFile.fromTreeUri(this, treeUri);
                Log.i(TAG, "DOCUMENTFILE = " + pickedDir);
                String arquivo = treeUri.toString();
                String[] qbr;
                String aftcd = URLDecoder.decode(arquivo);
                qbr = aftcd.split("/");
                int lgt = qbr.length;
                Log.i(TAG, "QUEBRA1 = " + qbr[1] + "QUEBRA2 = " + qbr[2] + "QUEBRA3 = " + qbr[3] + "QUEBRA4 = " + qbr[4]);
                if ("tree".equalsIgnoreCase(qbr[3])) {
                    String[] ot;
                    ot = qbr[4].split(":");
                    int lgnth = ot.length;
                    String pt2 = "/storage";
                    for (int a = 0; a < lgnth; a++) {
                        pt2 = pt2.concat("/");
                        pt2 = pt2.concat(ot[a]);
                    }
                    for (int b = 5; b < lgt; b++) {
                        pt2 = pt2.concat("/");
                        pt2 = pt2.concat(qbr[b]);
                    }
                    arquivo = pt2;
                    Log.i(TAG, "PT2 = " + arquivo + texto1);
                }
                try {
                    File pathfile = new File(arquivo);
                    Log.i(TAG,"BOOLEAN PASTA = " + !pathfile.exists());
                    if (!pathfile.exists()){
                        pathfile.mkdir();
                        Log.i(TAG,"Criou Pasta = ");
                    }
                    File newarquivo = new File(pathfile, texto1);
                    final boolean mkdirs = newarquivo.mkdirs();
                    Log.v("", "" + mkdirs);

                    Log.i(TAG, "DIRETORIO =" + newarquivo + "NOME DO ARQUIVO =" + texto1);
                    Log.i(TAG, "DATA1 = " + SDcard);

                    String novadata = SDcard;


                    FileOutputStream fos;
                    fos = new FileOutputStream(newarquivo);
                    fos.write(novadata.getBytes());
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i(TAG, "ARQUIVO NAO LOCALIZADO!");
                }
            }
        }
    }

    public void salvarNaPasta(){
        String SDPATH = Environment.getExternalStorageDirectory().toString();
        Log.i(TAG, "SDPATH = " + SDPATH+"/SPlay/Legendas");
        File pathFile = new File(SDPATH+"/SPlay/" ,"Legendas");
        Log.i(TAG, "1 = TRUE OR FALSE = " + pathFile.exists() + " PASTA = " + pathFile);
        if (!pathFile.exists()){
            pathFile.mkdirs();
            Log.i(TAG, "2 = TRUE OR FALSE = " + pathFile.exists());
        }
        try {

            File newarquivo = new File(pathFile, texto1);
            final boolean mkdirs = newarquivo.createNewFile();
//            final boolean mkdirs = newarquivo.mkdirs();

            Log.v("", "" + mkdirs);

            Log.i(TAG, "DIRETORIO =" + newarquivo + " NOME DO ARQUIVO =" + texto1);
            Log.i(TAG, "DATA1 = " + SDcard);

            String novadata = SDcard;


            FileOutputStream fos;
            fos = new FileOutputStream(newarquivo);
            fos.write(novadata.getBytes());
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, "ARQUIVO NAO LOCALIZADO!");
        }
        Toast.makeText(TelaPlay.this,
                "A legenda será salva na Pasta Padrão /Splay/Legendas",
                Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Granted, Now you can access app without bug.",Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(this,"Permission Denied, App maybe get crashed.", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }
}