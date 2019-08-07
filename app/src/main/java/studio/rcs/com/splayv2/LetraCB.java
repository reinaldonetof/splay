package studio.rcs.com.splayv2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class LetraCB extends Activity {

    Button botaoCB;
    EditText textocopiado;
    Button finalizar;
    ImageView info;
    String[] separated;
    String pasteTxt;
    int a;
    int tamanho;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_letra_cb);

        botaoCB = (Button)findViewById(R.id.botaoletraCB);
        textocopiado = (EditText) findViewById(R.id.TextoCopiado);
        finalizar = (Button) findViewById(R.id.botaCerteza);
        info = (ImageView) findViewById(R.id.infoView);

        botaoCB.setOnClickListener(new View.OnClickListener(){
            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View arg0) {
                a=0;
                android.text.ClipboardManager clipboard = (android.text.ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);

                if (clipboard.getText() == null){
                pasteTxt ="";
                }

                else{
                    pasteTxt = clipboard.getText().toString();
                }
                textocopiado.setText(pasteTxt);

                finalizar.setVisibility(View.VISIBLE);

            }
        });

        finalizar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                //tentando usar a split string
                Editable algo;
                algo = textocopiado.getText();

                String ok = algo.toString();

                separated = ok.split("\n");
                tamanho = separated.length;
                int tam;
                for (tam = 0; tam <tamanho;tam++){
                    if ("\n".equalsIgnoreCase(separated[tam])){
                        separated[tam] =separated[tam++];
                        tam++;
                    }
                }
                if(("".equalsIgnoreCase(separated[0]) || "\n".equalsIgnoreCase(separated[0])) || tam < 2){
                    Toast.makeText(LetraCB.this, "Adicione um texto válido!",Toast.LENGTH_LONG).show();
                    onResume();
                }

                else {
                    String num = Integer.toString(tamanho);
                    passar(num, separated);
                }
            }
        });
    }

    public void onResume(){
        super.onResume();
    }

    private void passar(String tamanho, String[] array){
        Intent passar = new Intent();
        Bundle pct = new Bundle();
        pct.putString("tamanho", tamanho);
        pct.putStringArray("Separado", array);
        passar.putExtras(pct);
        setResult(Activity.RESULT_OK, passar);
        finish();

    }
}
