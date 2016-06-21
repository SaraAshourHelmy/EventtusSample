package sara.com.Utility;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import sara.com.eventtussample.R;

/**
 * Created by sara on 6/17/2016.
 */
public class UtilityMethod {

    public static void SetToast(Context context, String msg) {

        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static boolean HaveNetworkConnection(Context context) {

        boolean HaveConnectedWifi = false;
        boolean HaveConnectedMobile = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    HaveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    HaveConnectedMobile = true;
        }
        if (!(HaveConnectedWifi || HaveConnectedMobile))
            SetToast(context, context.getString(R.string.no_internet));
        return HaveConnectedWifi || HaveConnectedMobile;
    }

    public static void SetLog(String tag, String msg) {
        Log.e(tag, msg);
    }

    public static void CreateDB(Context context) {

        InputStream is = context.getResources().openRawResource(
                R.raw.eventtus_db);
        SQLiteDatabase db = context.openOrCreateDatabase("test.sqlite"
                , context.MODE_PRIVATE, null);
        try {
            byte[] b = new byte[is.available()];
            is.read(b);
            FileOutputStream fos = new FileOutputStream("/data/data/" +
                    context.getPackageName() + "/databases/eventtus_db.sqlite");
            fos.write(b);
            fos.flush();
            fos.close();

            SharedUserData userData = new SharedUserData(context);
            userData.SetFirst();

        } catch (Exception e) {
            Log.e("create DB error :", "" + e);
        }
    }

    public static SQLiteDatabase ReadDatabase(Context con) {

        if (con == null) {
            return null;
        } else {
            SQLiteDatabase db = con.openOrCreateDatabase(StaticAssets.DbName
                    , con.MODE_PRIVATE, null);


            return db;
        }

    }

    public static void CloseDatabase(SQLiteDatabase db) {

        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    public static void clearApplicationData(Context context)
    {
        File cache = context.getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));Log.i("TAG", "**************** File /data/data/APP_PACKAGE/" + s + " DELETED *******************");
                }
            }
        }
    }

    public static boolean deleteDir(File dir)
    {
        if (dir != null && dir.isDirectory()) {
        String[] children = dir.list();
        for (int i = 0; i < children.length; i++) {
            boolean success = deleteDir(new File(dir, children[i]));
            if (!success) {
                return false;
            }
        }
    }
        return dir.delete();
    }
}
