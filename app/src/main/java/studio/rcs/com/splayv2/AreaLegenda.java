package studio.rcs.com.splayv2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AreaLegenda extends Activity {

    Button legCB;
    Button legNet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_legenda);

        legCB = (Button) findViewById(R.id.butLegCB);
        legNet = (Button) findViewById(R.id.butLegNet);

    }

    public void clickLegCB(View view){
        Intent letraCB = new Intent(this, LetraCB.class);
        startActivityForResult(letraCB, 23);

    }

    public void clickLegNet(View view){
        Intent letraNet = new Intent(this, LetraNet.class);
        startActivity(letraNet);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 23) {
            if(resultCode == Activity.RESULT_OK){
                setResult(Activity.RESULT_OK,data);
                finish();
            }
        }
    }//onActivityResult
}
