package sara.com.Utility;

import android.app.Activity;
import android.app.ActivityManager;
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

    public static void ClearData(Activity activity) {
        ((ActivityManager) activity.getSystemService(activity.ACTIVITY_SERVICE))
                .clearApplicationUserData();
        activity.finish();
    }
}
