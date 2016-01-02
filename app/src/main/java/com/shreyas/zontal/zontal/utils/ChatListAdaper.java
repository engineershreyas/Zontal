package com.shreyas.zontal.zontal.utils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.shreyas.zontal.zontal.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by shreyashirday on 1/2/16.
 */
public class ChatListAdaper extends BaseAdapter {

    private final String LOG_TAG = "ChatListAdapter";

    private Context ctx;
    private List<ParseObject> objects;
    private ParseUser currentUser;
    private ViewHolder v;


    public ChatListAdaper(Context context, List<ParseObject> chats) {
        super();
        ctx = context;
        objects = chats;
        currentUser = ParseUser.getCurrentUser();
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public ParseObject getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {



        if(convertView == null){
            convertView = LayoutInflater.from(ctx).inflate(R.layout.chat_header_item,null,false);

            v = new ViewHolder();

            CircularImageView iv = (CircularImageView)convertView.findViewById(R.id.user_image);
            TextView unl = (TextView)convertView.findViewById(R.id.user_name);
            TextView tpl = (TextView)convertView.findViewById(R.id.preview_msg);

            v.imageView = iv;
            v.usernameLabel = unl;
            v.textPreviewLabel = tpl;

            convertView.setTag(v);

        }
        else{
            v = (ViewHolder)convertView.getTag();
        }


        ParseObject chat = getItem(position);


        String userID = currentUser.getObjectId();

        boolean isUserA = ChatFactory.isUserA(chat, userID);

        final String otherUser = isUserA ? chat.getString("userB") : chat.getString("userA");
        String otherUserID = isUserA ? chat.getString("userBid") : chat.getString("userAid");

        ParseQuery<ParseUser> userParseQuery = ParseUser.getQuery();

        userParseQuery.whereEqualTo("objectId", otherUserID);

        userParseQuery.getFirstInBackground(new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser object, ParseException e) {

                if (e != null) {

                    ParseFile file = object.getParseFile("profileImage");

                    Picasso.with(ctx).load(file.getUrl()).into(v.imageView);

                }
                else{
                    Log.e(LOG_TAG,e.getMessage());
                }

            }
        });

        v.usernameLabel.setText(otherUser);

        ParseQuery<ParseObject> msgQuery = ParseQuery.getQuery("Message");

        msgQuery.whereEqualTo("chatID",chat.getString("chatID"));

        msgQuery.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if(e != null){
                    v.textPreviewLabel.setText(object.getString("text"));
                }
                else{
                    Log.e(LOG_TAG,e.getMessage());
                }
            }
        });

        return convertView;
    }


    static class ViewHolder {

        CircularImageView imageView;
        TextView usernameLabel;
        TextView textPreviewLabel;

    }

}
