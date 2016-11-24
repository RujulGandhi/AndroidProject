package com.archi.intrisfeed.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.archi.intrisfeed.R;
import com.archi.intrisfeed.util.Constant;
import com.archi.intrisfeed.util.Util;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by archi_info on 10/29/2016.
 */

public class ChatMsgListAdapter extends BaseAdapter {
    public Context context;
    public ArrayList<HashMap<String, String>> arrayList;
    public HashMap<String, String> hashMap;
    public LayoutInflater inflater;

    public ChatMsgListAdapter(Context context, ArrayList<HashMap<String, String>> msgs) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.arrayList = msgs;
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
    public View getView(int position, View view, ViewGroup parent) {
//        final ViewHolderItem viewHolder;

            // inflate the layout
            view = inflater.inflate(R.layout.adapter_msg_list, parent, false);
            // well set up the ViewHolder
//            viewHolder = new ViewHolderItem();
            TextView receivedMsg = (TextView) view.findViewById(R.id.tvreceivedMsg);
            TextView timeRecive = (TextView) view.findViewById(R.id.adapter_received_time);

             TextView sendMsg = (TextView) view.findViewById(R.id.tvSendMsg);
            TextView timeSend = (TextView) view.findViewById(R.id.adapter_send_time);

            LinearLayout llSender = (LinearLayout) view.findViewById(R.id.llSender);
            LinearLayout llReceiver = (LinearLayout) view.findViewById(R.id.llReceiver);


        hashMap = arrayList.get(position);
        Log.e("ADPTR MSG", "" + hashMap.get("msg"));
        if(Util.ReadSharePrefrence(context, Constant.SHRED_PR.KEY_USER_CHAT_ID).equalsIgnoreCase(hashMap.get("sender_id"))){
            llSender.setVisibility(View.VISIBLE);
            llReceiver.setVisibility(View.GONE);
            sendMsg.setText(hashMap.get("msg"));
            timeSend.setText(hashMap.get("updated_at"));
            Log.e("TIME ",">> "+hashMap.get("updated_at"));
//            timeSend.setText(getTimeFromDateTime(hashMap.get("updated_at")));

        }else{
            llSender.setVisibility(View.GONE);
            llReceiver.setVisibility(View.VISIBLE);
            receivedMsg.setText(hashMap.get("msg"));
            timeRecive.setText(hashMap.get("updated_at"));
//            Log.e("TIME ",">> "+getTimeFromDateTime(hashMap.get("updated_at")));
//            timeRecive.setText(getTimeFromDateTime(hashMap.get("updated_at")));
        }


        return view;
    }

//    private String getTimeFromDateTime(String updated_at){
//        String startTime = "updated_at";
//        StringTokenizer tk = new StringTokenizer(startTime);
//        String date = tk.nextToken();
//        String time = tk.nextToken();
//
//        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
//        SimpleDateFormat sdfs = new SimpleDateFormat("hh:mm a");
//        Date dt;
//        try {
//            dt = sdf.parse(time);
//            System.out.println("Time Display: " + sdfs.format(dt)); // <-- I got result here
//            Log.e("TIME  >>> ",""+sdfs.format(dt));
//        } catch (ParseException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        return startTime;
//    }

//    static class ViewHolderItem {
//        TextView receivedMsg,timeRecive;
//        TextView sendMsg,timeSend;
//        LinearLayout llSender,llReceiver;


}
