package sara.com.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import sara.com.Utility.SharedUserData;
import sara.com.Utility.StaticAssets;
import sara.com.Utility.UtilityMethod;
import sara.com.eventtussample.R;

/**
 * Created by sara on 6/21/2016.
 */
public class LanguageDialog extends Dialog implements View.OnClickListener {

    Context context;
    private TextView tv_english, tv_arabic;
    private SharedUserData userData;


    public LanguageDialog(Context context) {
        super(context);
        this.context = context;
    }

    private void SetupTools() {

        userData = new SharedUserData(context);
        tv_english = (TextView) findViewById(R.id.tv_english);
        tv_arabic = (TextView) findViewById(R.id.tv_arabic);
        tv_english.setOnClickListener(this);
        tv_arabic.setOnClickListener(this);

        // load current language
        if (userData.GetLanguage() == StaticAssets.English)
            SetupTextColor(tv_english, tv_arabic);
        else
            SetupTextColor(tv_arabic, tv_english);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_laguage);
        SetupTools();
    }

    @Override
    public void onClick(View v) {
        if (v == tv_arabic) {

            SetupTextColor(tv_arabic, tv_english);
            UtilityMethod.SetSettingLanguage(context, StaticAssets.Arabic);

        } else if (v == tv_english) {
            SetupTextColor(tv_english, tv_arabic);
            UtilityMethod.SetSettingLanguage(context, StaticAssets.English);
        }
    }

    private void SetupTextColor(TextView txt_lang1, TextView txt_lang2) {

        // selected language
        txt_lang1.setBackgroundColor(context.getResources().getColor(R.color.app_color));
        txt_lang1.setTextColor(context.getResources().getColor(R.color.white));

        // unselected language
        txt_lang2.setBackgroundColor(context.getResources().getColor(R.color.white));
        txt_lang2.setTextColor(context.getResources().getColor(R.color.app_color));

    }
}
