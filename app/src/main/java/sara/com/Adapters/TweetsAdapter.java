package sara.com.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import sara.com.eventtussample.R;

/**
 * Created by sara on 6/21/2016.
 */
public class TweetsAdapter extends ArrayAdapter<String> {

    Context context;
    String[] tweets;

    public TweetsAdapter(Context context, int layout, String[] tweets) {
        super(context, layout, tweets);
        this.context = context;
        this.tweets = tweets;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TweetHolder tweetHolder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.adapter_tweet, parent, false);
            tweetHolder = new TweetHolder();
            tweetHolder.tv_tweet =
                    (TextView) convertView.findViewById(R.id.tv_tweet);
            convertView.setTag(tweetHolder);
        } else {

            tweetHolder = (TweetHolder) convertView.getTag();
        }

        tweetHolder.tv_tweet.setText(tweets[position]);
        return convertView;
    }

    static class TweetHolder {
        public TextView tv_tweet;
    }
}
