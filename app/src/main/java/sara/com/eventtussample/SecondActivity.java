package sara.com.eventtussample;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
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
    private CircleImageView img_user_profile;
    private ListView lstV_followers;
    private FollowersAdapter adapter;
    private SwipeRefreshLayout swipe_refresh;
    private ArrayList<User> users;
    private SharedUserData userData;
    private ProgressBar progress_download;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menus, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        SetupTools();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_setting) {

            UtilityMethod.DisplayLanguageDialog(this);
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    private void SetupTools() {

        userData = new SharedUserData(this);
        tv_user_name = (TextView) findViewById(R.id.tv_user_name);
        img_user_profile = (CircleImageView) findViewById(R.id.img_user_profile);
        lstV_followers = (ListView) findViewById(R.id.lstV_followers);
        swipe_refresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        progress_download = (ProgressBar) findViewById(R.id.custom_progress_follower);

        // change refresh color
        swipe_refresh.setColorSchemeColors
                (ContextCompat.getColor(this, R.color.app_color));

        swipe_refresh.setOnRefreshListener(this);
        lstV_followers.setOnItemClickListener(this);
        tv_user_name.setText(userData.getUserName());

        // load img from url
        UrlImageViewHelper.setUrlDrawable
                (img_user_profile, userData.getProfileImg(),
                        R.drawable.user_icon, StaticAssets.CacheTime);


        // check internet to get data from server or database direct
        if (UtilityMethod.HaveNetworkConnection(this))
            new GetFollowers().execute();
        else
            SetFollowerData();

    }

    public void SetFollowerData() {

        swipe_refresh.setVisibility(View.VISIBLE);
        progress_download.setVisibility(View.GONE);
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

    private void MoveToUserInfo(int position) {

        Intent intent = new Intent(SecondActivity.this, FollowerInfoActivity.class);
        intent.putExtra("user", users.get(position));
        startActivity(intent);

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
}
