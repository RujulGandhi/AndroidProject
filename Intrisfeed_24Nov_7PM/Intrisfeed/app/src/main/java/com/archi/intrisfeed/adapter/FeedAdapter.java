package com.archi.intrisfeed.adapter;

import android.app.Dialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.archi.intrisfeed.R;
import com.archi.intrisfeed.util.Constant;
import com.archi.intrisfeed.util.Util;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by archi_info on 10/14/2016.
 */

public class FeedAdapter extends BaseAdapter {
    Context mContext;
    ArrayList<HashMap<String, String>> userFeedList;
    private LayoutInflater inflater = null;
    private String receiverId;
    HashMap<String, String> hashMap;
    FragmentManager fm;
    String message;
    Uri imgeUri, videoUri;
    String postIdStr, commentStr;


    public FeedAdapter(Context mCtx, ArrayList<HashMap<String, String>> list, FragmentManager fm) {
        this.mContext = mCtx;
        this.userFeedList = list;
        this.fm = fm;
        inflater = (LayoutInflater) mContext.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return userFeedList.size();
    }

    @Override
    public Object getItem(int position) {
        return userFeedList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolderItem viewHolder;
        if (view == null) {
            // inflate the layout
            view = inflater.inflate(R.layout.item_feed_list, parent, false);

            // well set up the ViewHolder
            viewHolder = new ViewHolderItem();
            viewHolder.tvTitle = (TextView) view.findViewById(R.id.tvTitleFeed_FeedListAdapter);
            viewHolder.imageViewFeed = (ImageView) view.findViewById(R.id.imageview_FeedListAdapter);
            viewHolder.ivShare = (ImageView) view.findViewById(R.id.ivShare_FeedListAdapter);
            viewHolder.tvComment = (TextView) view.findViewById(R.id.tvComment_FeedList);
//            viewHolder.videoViewFeed = (VideoView) view.findViewById(R.id.videoview_FeedListAdapter);
//            viewHolder.tvComment = (TextView) view.findViewById(R.id.tvComment_SharingFeedList);
//            viewHolder.llComment = (LinearLayout) view.findViewById(R.id.llCommentLayout);
//            viewHolder.etCommentText = (EditText) view.findViewById(R.id.etCommentPostText);
//            viewHolder.btCommentPost = (Button) view.findViewById(R.id.btCommentPost);
            viewHolder.llCommentUser = (LinearLayout) view.findViewById(R.id.llCommentUser);
            // store the holder with the view.
            view.setTag(viewHolder);
        } else {
            // we've just avoided calling findViewById() on resource everytime
            // just use the viewHolder
            viewHolder = (ViewHolderItem) view.getTag();
        }


        hashMap = userFeedList.get(position);
        if (!hashMap.get("image").equalsIgnoreCase("")) {
            Picasso.with(mContext).load(hashMap.get("image")).placeholder(R.drawable.default_img).into(viewHolder.imageViewFeed);
        }

        if (!hashMap.get("video").equalsIgnoreCase("")) {
            String pathVideo = hashMap.get("video");
            Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(pathVideo,
                    MediaStore.Images.Thumbnails.MINI_KIND);
            Picasso.with(mContext).load(hashMap.get("video")).placeholder(R.drawable.ic_video_default).error(R.drawable.ic_video_default).into(viewHolder.imageViewFeed);


        }

        try {
            JSONObject obj = new JSONObject(hashMap.get("comment"));
            Log.e("ARRY ",">> "+obj);
            for(int i = 0; i< obj.length(); i++) {
                View commentLayout = LayoutInflater.from(mContext).inflate(R.layout.sharing_feed_comment, viewHolder.llCommentUser, false);
                TextView tvUserComment = (TextView) commentLayout.findViewById(R.id.tvComment_User);
                TextView tvUserName = (TextView) commentLayout.findViewById(R.id.tvUserNamneComment);
                tvUserComment.setText(obj.get("comment").toString());
                tvUserName.setText(""+obj.get("name").toString());
                if(!tvUserComment.getText().toString().equalsIgnoreCase("")) {
                    viewHolder.llCommentUser.addView(commentLayout);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        viewHolder.ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message = hashMap.get("text");
                Log.e("ImageUri", ">> " + imgeUri);
                Log.e("message", ">> " + message);

                sharePost(message);


            }
        });


        try {
            JSONObject obj = new JSONObject(hashMap.get("comment"));
            Log.e("ARRY ", ">> " + obj);
            for (int i = 0; i < obj.length(); i++) {
                View commentLayout = LayoutInflater.from(mContext).inflate(R.layout.sharing_feed_comment, viewHolder.llCommentUser, false);
                TextView tvUserComment = (TextView) commentLayout.findViewById(R.id.tvComment_User);
                TextView tvUserName = (TextView) commentLayout.findViewById(R.id.tvUserNamneComment);
                tvUserComment.setText(obj.get("comment").toString());
                tvUserName.setText("" + obj.get("name").toString());
                if (!tvUserComment.getText().toString().equalsIgnoreCase("")) {
                    viewHolder.llCommentUser.addView(commentLayout);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        viewHolder.tvTitle.setText(hashMap.get("text"));
        message = viewHolder.tvTitle.getText().toString();

        viewHolder.tvComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    postIdStr = userFeedList.get(position).get("id");
                Log.e("ON CLICK","POST ID "+postIdStr);
                    showCommentPopup(mContext,postIdStr);
            }
        });

        return view;
    }

    static class ViewHolderItem {
        TextView tvTitle, tvComment;
        ImageView imageViewFeed, ivShare;
        LinearLayout llComment;
        EditText etCommentText;
        LinearLayout llCommentUser;

    }

    public void showCommentPopup(final Context ctx, final String postId) {


        // custom dialog
        final Dialog dialog = new Dialog(ctx);
        dialog.setContentView(R.layout.popup_comment_feed);
        dialog.setTitle("Comment Feed");
        dialog.setCancelable(false);
        final EditText etCommentText = (EditText) dialog.findViewById(R.id.etCommentPostTextPopup);

        final Button ok = (Button) dialog.findViewById(R.id.btSubmitCommentPost);
        final Button cancel = (Button) dialog.findViewById(R.id.btCancelPostComment);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = etCommentText.getText().toString();
                if(!comment.equalsIgnoreCase("")) {
                    Log.e("CoMMENT","M "+comment);
                    Log.e("Post ID","M "+postIdStr);
                    new postCommentOnFeed(comment, postId).execute();
//                    dialog.dismiss();
                }else{
                    Toast.makeText(ctx, "Please enter comment to post commnet on feed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    private void sharePost(String message) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, R.string.app_name);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);
        mContext.startActivity(Intent.createChooser(sharingIntent, message));
    }

    public class postCommentOnFeed extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        String id;
        int pos;
        String comment, post_id;

        public postCommentOnFeed(String comment, String postId) {
            this.comment = comment;
            this.post_id = postId;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(mContext);
            pd.setMessage("Loading");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
//           http://web-medico.com/web2/intrisfeed/api/insert_comment_on_post.php?user_id=16&post_id=23&comment=this%20is%20new

            String response = "";
            HashMap<String, String> hashmap = new HashMap<>();
            hashmap.put("user_id", Util.ReadSharePrefrence(mContext, Constant.SHRED_PR.KEY_USERID));
            hashmap.put("post_id", post_id);
            hashmap.put("comment", comment);

            response = Util.getResponseofPost(Constant.BASE_URL + "insert_comment_on_post.php", hashmap);
            Log.e("RESULT", ">>" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
//            pd.dismiss();
            super.onPostExecute(s);
            pd.dismiss();
            Log.d("Response", "" + s);
            try {
                JSONObject object = new JSONObject(s.toString());
                if (object.getString("successful").equalsIgnoreCase("true")) {
                    Toast.makeText(mContext, "" + object.getString("msg"), Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(mContext, "" + object.getString("msg"), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
