package sara.com.Adapters;

import android.animation.PropertyValuesHolder;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import java.util.ArrayList;

import sara.com.Models.User;
import sara.com.Utility.StaticAssets;
import sara.com.eventtussample.R;


/**
 * Created by sara on 6/18/2016.
 */
public class FollowersAdapter extends ArrayAdapter<User> {

    private Context context;
    private ArrayList<User> users;

    public FollowersAdapter(Context context, int resource, ArrayList<User> users) {
        super(context, resource, users);
        this.context = context;
        this.users = users;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Holder vHolder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.adapter_followers, parent, false);
            vHolder = new Holder();
            vHolder.img_user = (ImageView) convertView.findViewById(R.id.img_adapter_user);
            vHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_adapter_name);
            vHolder.tv_bio = (TextView) convertView.findViewById(R.id.tv_adapter_bio);
            convertView.setTag(vHolder);
        } else {
            vHolder = (Holder) convertView.getTag();
        }

        SetData(vHolder, position);

        return convertView;
    }

    private void SetData(Holder vHolder, int pos) {
        vHolder.tv_name.setText(users.get(pos).getUser_name());
        UrlImageViewHelper.setUrlDrawable
                (vHolder.img_user, users.get(pos).getProfile_img(),
                        R.drawable.user_icon, StaticAssets.CacheTime);
        if (users.get(pos).getBio() != null &&
                users.get(pos).getBio().length() > 0) {
            vHolder.tv_bio.setVisibility(View.VISIBLE);
            vHolder.tv_bio.setText(users.get(pos).getBio());
        } else {
            vHolder.tv_bio.setVisibility(View.GONE);
        }
    }

    public static class Holder {

        public ImageView img_user;
        public TextView tv_name;
        public TextView tv_bio;
    }
}
