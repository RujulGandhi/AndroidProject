package com.archi.intrisfeed.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.archi.intrisfeed.R;
import com.archi.intrisfeed.fragment.TestChat;
import com.archi.intrisfeed.util.Constant;
import com.archi.intrisfeed.util.Util;
import com.quickblox.users.model.QBUser;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.archi.intrisfeed.MainActivity.groupName;
import static com.archi.intrisfeed.MainActivity.occupantIdsList;

/**
 * Created by archi_info on 10/19/2016.
 */

public class ChatUserListAdapter extends BaseAdapter {
    public Context context;
    public ArrayList<QBUser> arrayList;
    public LayoutInflater inflater;
    ImageView ivAddFav;
    CheckBox cbGroupChat;


    public ChatUserListAdapter(Context context, ArrayList<QBUser> users) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.arrayList = users;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.adapter_chat_user, null);
        ImageView ivProfilePic = (ImageView) view.findViewById(R.id.ivProfilePic);
        TextView usernameTv = (TextView) view.findViewById(R.id.adapter_chat_user_usernametv);
        TextView timeTv = (TextView) view.findViewById(R.id.adapter_chat_user_timetv);
        LinearLayout llAddFav = (LinearLayout) view.findViewById(R.id.llAddFavorite);
        ivAddFav = (ImageView) view.findViewById(R.id.ivAddFav);
        cbGroupChat = (CheckBox) view.findViewById(R.id.checkbox_adapter_chat_user);

        llAddFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("FROM ","F "+Util.ReadSharePrefrence(context,Constant.SHRED_PR.KEY_USERID).toString());
                Log.e("TO","T "+arrayList.get(position).getLogin().toString());
                new addUserToFavoriteList(Util.ReadSharePrefrence(context,Constant.SHRED_PR.KEY_USERID).toString(),arrayList.get(position).getLogin().toString(), position).execute();
            }
        });

//        Picasso.with(context).load(arrayList.get(position).get)
        usernameTv.setText(arrayList.get(position).getFullName());
        timeTv.setText(arrayList.get(position).getEmail());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(context, TestChat.class);
                in.putExtra("id", "" + arrayList.get(position).getId());
                in.putExtra("fullname", arrayList.get(position).getFullName());
                in.putExtra("email", arrayList.get(position).getEmail());
                context.startActivity(in);
            }
        });

        cbGroupChat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    occupantIdsList.add(arrayList.get(position).getId());
                    Log.e("LIST ", ">> " + arrayList.get(position).getId());
                    groupName.add(arrayList.get(position).getFullName());
                }else{
                    occupantIdsList.remove(arrayList.get(position).getId());
                }
            }
        });


        return view;
    }


//    http://181.224.157.105/~hirepeop/host1/intrisfeed/api/add_favourite_friend.php?frd_to=25&frd_from=26
    public class addUserToFavoriteList extends AsyncTask<String, String, String> {
        ProgressDialog pd;
    String from_id, to_id;
    int pos;

    public addUserToFavoriteList(String from, String to, int position) {
        this.from_id = from;
        this.to_id = to;
        this.pos = position;
    }

    @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(context);
            pd.setMessage("Loading");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String response = "";

            HashMap<String, String> hashmap = new HashMap<String, String>();
            hashmap.put("frd_to", to_id);
            hashmap.put("frd_from", from_id);
            response = Util.getResponseofPost(Constant.BASE_URL + "add_favourite_friend.php", hashmap);
//                urlString = Constant.BASE_URL + "login.php?email=" + usernamestr + "&password=" + passwordstr;
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
                if (object.getString("status").equalsIgnoreCase("true")) {
//                    JSONObject obj = object.getJSONObject("data");
//                    String id = obj.getString("id");

//                    ivAddFav.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_favourite));
                    Toast.makeText(context, "Friend Added to Favorite Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "" + object.getString("msg"), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
