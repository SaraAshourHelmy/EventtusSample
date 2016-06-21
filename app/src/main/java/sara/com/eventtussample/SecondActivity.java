package sara.com.eventtussample;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import sara.com.Adapters.FollowersAdapter;
import sara.com.DBModels.UserDB;
import sara.com.Models.User;
import sara.com.Utility.SharedUserData;
import sara.com.Utility.StaticAssets;
import sara.com.Utility.TwitterHelper;
import sara.com.Utility.UtilityMethod;

public class SecondActivity extends AppCompatActivity implements
        SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener {

    private TextView tv_user_name;
    private ImageView img_user_profile;
    private ListView lstV_followers;
    private LinearLayout lnr_user_info;
    private FollowersAdapter adapter;
    private SwipeRefreshLayout swipe_refresh;
    private ArrayList<User> users;
    private SharedUserData userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        SetupTools();

    }

    private void SetupTools() {

        userData = new SharedUserData(this);
        tv_user_name = (TextView) findViewById(R.id.tv_user_name);
        img_user_profile = (ImageView) findViewById(R.id.img_user_profile);
        lstV_followers = (ListView) findViewById(R.id.lstV_followers);
        swipe_refresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        lnr_user_info = (LinearLayout) findViewById(R.id.lnr_user_info);

        swipe_refresh.setOnRefreshListener(this);
        lstV_followers.setOnItemClickListener(this);
        tv_user_name.setText(userData.getUserName());

        // load img from url
        UrlImageViewHelper.setUrlDrawable
                (img_user_profile, userData.getProfileImg(),
                        R.drawable.user_icon, StaticAssets.CacheTime );


        if (UtilityMethod.HaveNetworkConnection(this))
            new GetFollowers().execute();
        else
            SetFollowerData();

    }

    public void SetFollowerData() {

        users = UserDB.GetUsers(SecondActivity.this);
        adapter = new FollowersAdapter(this, R.layout.adapter_followers, users);
        lstV_followers.setAdapter(adapter);
        swipe_refresh.setRefreshing(false);
    }

    @Override
    public void onRefresh() {

        if (UtilityMethod.HaveNetworkConnection(this))
            new GetFollowers().execute();
        else
            swipe_refresh.setRefreshing(false);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        MoveToUserInfo(position);
    }

    class GetFollowers extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {

            TwitterHelper twitterHelper = new TwitterHelper();
            twitterHelper.GetFollowers(SecondActivity.this, userData.getUserId());

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            SetFollowerData();
        }
    }

    private void MoveToUserInfo(int position) {

        Intent intent = new Intent(SecondActivity.this, FollowerInfoActivity.class);
        intent.putExtra("user", users.get(position));
        startActivity(intent);

    }
}
