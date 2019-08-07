package studio.rcs.com.splayv2;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private static final int REQUEST_CODE = 6384; // onActivityResult request
    // code

    int numero;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setFinishOnTouchOutside(false);

        Intent numvideo = getIntent();
        Bundle pacote =numvideo.getExtras();

        numero = pacote.getInt("numero");

        if (numero == 1 ){
            showChooser();
        }

        else {
            setContentView(R.layout.tela_home);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    private void showChooser() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("video/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        InputStream stream = null;
        String localizado = "EXTERNAL";
        if (requestCode == 1 && resultCode == RESULT_OK) {
            try {
                stream = getContentResolver().openInputStream(data.getData());
                Uri uri = data.getData();
                Log.i(TAG, "Uri = " + uri.toString());
                String arquivo = getPath(this,uri);
                Log.i(TAG, "String Caminho = " + arquivo);
                if (arquivo == null) {
                    arquivo = uri.toString();
                    String[] qbr;
                    String aftcd = URLDecoder.decode(arquivo);
                    qbr = aftcd.split("/");
                    int lgt = qbr.length;
                    Log.i(TAG, "QUEBRA1 = " + qbr[1] + "QUEBRA2 = " + qbr[2] + "QUEBRA3 = " + qbr[3] + "QUEBRA4 = " + qbr[4]);
                    if ("document".equalsIgnoreCase(qbr[3])) {
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
                        Log.i(TAG, "PT2 = " + pt2);
                        localizado = "INTERNO";
                    }
                }
                Bundle pct = new Bundle();
                pct.putString("curPF",arquivo);
                pct.putString("local", localizado);
                final Intent it2 = new Intent();
                String[] quebrar;
                String afterdecode = URLDecoder.decode(arquivo);
                quebrar = afterdecode.split("/");
                String ultimo = quebrar[quebrar.length - 1];
                pct.putString("arquivo",ultimo);
                it2.putExtras(pct);
                new AlertDialog.Builder(this)
                        .setTitle("Arquivo Selecionado")
                        .setMessage("Tem certeza que deseja este arquivo?" +
                                "   "  + ultimo)
                        .setPositiveButton("Sim",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        setResult(Activity.RESULT_OK,it2);
                                        finish();
                                    }
                                })
                        .setNegativeButton("Não",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        showChooser();
                                    }
                                })
                        .show();
                Toast.makeText(MainActivity.this,
                        "Arquivo selecionado: " + uri, Toast.LENGTH_LONG).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            finally {
                if (stream != null)
                    try {
                        stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }

        }
    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @author paulburke
     */
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}
