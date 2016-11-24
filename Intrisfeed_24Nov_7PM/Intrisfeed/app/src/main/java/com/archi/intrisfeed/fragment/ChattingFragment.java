package com.archi.intrisfeed.fragment;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.Toolbar;

import com.archi.intrisfeed.R;
import com.archi.intrisfeed.adapter.ChatUserListAdapter;
import com.archi.intrisfeed.util.Constant;
import com.archi.intrisfeed.util.Util;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.model.QBSession;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBGroupChatManager;
import com.quickblox.chat.QBSystemMessagesManager;
import com.quickblox.chat.exception.QBChatException;
import com.quickblox.chat.listeners.QBSystemMessageListener;
import com.quickblox.chat.model.QBDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.QBSettings;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.request.QBPagedRequestBuilder;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import org.jivesoftware.smack.SmackException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static com.archi.intrisfeed.MainActivity.occupantIdsList;


/**
 * Created by archi_info on 9/24/2016.
 */
public class ChattingFragment extends Fragment {

    //    TextView tvGotoChat;
    public String uNameStr, uPwdStr;
    public static String APP_ID = "50153";
    public static String AUTH_KEY = "r4m6sAXHKOnPWXF";
    public static String AUTH_SECRET = "TJ7wVWdkubbSsf5";
    public static String ACCOUNT_KEY = "76cUn9uZCsxqU8iYpKxt";
//    //    public static final String APP_ID = "47880";
//    public static final String APP_ID = "48912";
//    //    public static final String AUTH_KEY = "hCQpjWq4HcFV5uk";
//    public static final String AUTH_KEY = "4MnSFA7XEX6KPP3";
//    //    public static final String AUTH_SECRET = "NWKJSNhWHCZYgF9";
//    public static final String AUTH_SECRET = "9Q842KO48ZdNsJd";
//    //    public static final String ACCOUNT_KEY = "2xzLtJGk1KXi7Uq8sys3";
//    public static final String ACCOUNT_KEY = "DtUDQqQxbeezkUprEuV7";
    public QBUser userData;
    public ProgressBar pd;
    public ListView listView;
    public ArrayList<QBUser> qbUserList;
    public ArrayList<String> arrayUserName;
    public ImageView ivToolbarAdd;
    public Toolbar toolbar;


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chatting, container, false);


        // set option menu visible on fragment
        setHasOptionsMenu(true);

        occupantIdsList.clear();
        pd = (ProgressBar) rootView.findViewById(R.id.fragement_chatting_progress);
        listView = (ListView) rootView.findViewById(R.id.fragement_chatting_listview);
        QBSettings.getInstance().init(getActivity(), APP_ID, AUTH_KEY, AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);
        qbUserList = new ArrayList<>();
//        toolbar = (Toolbar)rootView.findViewById(R.id.toolbar);
//        ivToolbarAdd = (ImageView)toolbar.findViewById(R.id.iv_toolbar_add);
//        ivToolbarAdd.setVisibility(View.VISIBLE);



        uNameStr = Util.ReadSharePrefrence(getActivity(), Constant.SHRED_PR.KEY_USER_NAME);
        uPwdStr = Util.ReadSharePrefrence(getActivity(), Constant.SHRED_PR.KEY_USER_PASSWORD);
        arrayUserName = new ArrayList<>();
        new GetFriendList().execute();
        final QBUser user = new QBUser();
        user.setEmail(uNameStr);
        user.setPassword(uPwdStr);
        createSession(user);
        return rootView;
    }





    private void createSession(final QBUser users) {
        QBAuth.createSession(users ,new QBEntityCallback<QBSession>(){
            @Override
            public void onSuccess(QBSession qbSession, Bundle bundle) {
                users.setId(qbSession.getUserId());
                loginForQuickBlox(users);
            }

            @Override
            public void onError(QBResponseException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(),"Eror :"+e.toString(),Toast.LENGTH_SHORT).show();
                createSession(users);
            }
        });

//        QBAuth.createSession(users,new QBEntityCallback<QBSession>() {
//            @Override
//            public void onSuccess(QBSession result, Bundle params) {
//                users.setId(result.getUserId());
//                loginForQuickBlox(users);
//            }
//
//            @Override
//            public void onError(QBResponseException e) {
//                e.printStackTrace();
//                Toast.makeText(getActivity(),"Eror :"+e.toString(),Toast.LENGTH_SHORT).show();
//                createSession(users);
//            }
//        });
    }
  /*  @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), TestChat.class);
        startActivity(intent);
    }*/


    private boolean loginForQuickBlox(final QBUser user) {

        QBUsers.signIn(user, new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser users, Bundle params) {
               // Toast.makeText(getActivity(), "Sucess login" + users.getId(), Toast.LENGTH_LONG).show();

                if (!QBChatService.isInitialized()) {
                    QBChatService.init(getActivity());
                    Toast.makeText(getActivity(), "Initialized", Toast.LENGTH_LONG).show();
                    userData = new QBUser();
                    userData.setEmail(uNameStr);
                    userData.setPassword(uPwdStr);
                    userData.setId(user.getId());
                    QBChatService.getInstance().login(userData, new QBEntityCallback() {
                        @Override
                        public void onSuccess(Object o, Bundle bundle) {
                            Toast.makeText(getActivity(), "Chat Logged in", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onError(QBResponseException e) {
                            Toast.makeText(getActivity(), "Chat Error", Toast.LENGTH_LONG).show();
                        }
                    });
                }


                QBPagedRequestBuilder pagedRequestBuilder = new QBPagedRequestBuilder();
                pagedRequestBuilder.setPage(1);
                pagedRequestBuilder.setPerPage(50);
                QBUsers.getUsers(pagedRequestBuilder, new QBEntityCallback<ArrayList<QBUser>>() {
                    @Override
                    public void onSuccess(ArrayList<QBUser> users, Bundle params) {
                        Log.e("USERS ",">>>> "+users);
                        ArrayList<QBUser> arrySelectedUser = new ArrayList<QBUser>();
                        for (int i=0;i<users.size();i++)
                        {
                            for (int k=0;k<arrayUserName.size();k++)
                            {
                                if (users.get(i).getEmail().equals(arrayUserName.get(k)))
                                {
                                    arrySelectedUser.add(users.get(i));

                                }
                            }
                        }
                        ChatUserListAdapter adapter = new ChatUserListAdapter(getActivity(), arrySelectedUser);
                        listView.setAdapter(adapter);
                        pd.setVisibility(View.GONE);
//                        Log.i("TAG", "Users: " + users.toString());
//                        Log.i("TAG", "currentPage: " + params.getInt(Consts.CURR_PAGE));
//                        Log.i("TAG", "perPage: " + params.getInt(Consts.PER_PAGE));
//                        Log.i("TAG", "totalPages: " + params.getInt(Consts.TOTAL_ENTRIES));

                    }
                    @Override
                    public void onError(QBResponseException errors) {
                        Toast.makeText(getActivity(),"exception :"+errors,Toast.LENGTH_SHORT).show();
                    }
                });


//                Intent in = new Intent(getActivity(), TestChat.class);
//                startActivity(in);

//                QBChatService chatService = QBChatService.getInstance();
//                QBPrivateChatManager privateChatManager = chatService.getPrivateChatManager();
//
//
//                QBRequestGetBuilder requestBuilder = new QBRequestGetBuilder();
//                requestBuilder.setLimit(100);
//
//                final QBSystemMessagesManager systemMessagesManager = QBChatService.getInstance().getSystemMessagesManager();
//
//                QBChatService.getChatDialogs(null, requestBuilder, new QBEntityCallback<ArrayList<QBDialog>>() {
//                    @Override
//                    public void onSuccess(ArrayList<QBDialog> dialogs, Bundle args) {
//                        int totalEntries = args.getInt("total_entries");
//                        Log.d("TotalEntry", "" + dialogs.get(0).getDialogId());
//
//
//                        QBChatMessage chatMessage = createChatNotificationForGroupChatCreation(dialogs.get(0));
//
//
//                        chatMessage.setProperty("date_sent", "1000112");
//                        chatMessage.setSenderId(dialogs.get(0).getUserId());
//                        chatMessage.setRecipientId(dialogs.get(0).getOccupants().get(1));
//                        try {
//                            systemMessagesManager.sendSystemMessage(chatMessage);
//                        } catch (SmackException.NotConnectedException e) {
//
//                        } catch (IllegalStateException ee) {
//
//                        }
//                    }
//
//
//                    @Override
//                    public void onError(QBResponseException errors) {
//
//                    }
//                });


//                QBChatService chatService = QBChatService.getInstance();
//                QBPrivateChatManager privateChatManager = chatService.getPrivateChatManager();
//                privateChatManager.createDialog(18803336, new QBEntityCallbackImpl<QBDialog>() {
//                    @Override
//                    public void onSuccess(QBDialog dialog, Bundle args) {
//
//                        Toast.makeText(getActivity(), "Get Data", Toast.LENGTH_LONG).show();
//
//                    }
//                });
            }

            @Override
            public void onError(QBResponseException errors) {
                Toast.makeText(getActivity(), errors.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        return true;
    }

    private class GetFriendList extends AsyncTask<String,String,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
           String userID  = Util.ReadSharePrefrence(getActivity(), Constant.SHRED_PR.KEY_USERID);
           //http://181.224.157.105/~hirepeop/host1/intrisfeed/api/get_friend_list.php?id=26

            String Url = Constant.BASE_URL + "get_friend_list.php?id=" + userID;
            // String Url = "http://181.224.157.105/~hirepeop/host1/intrisfeed/api/get_friend_list.php?id=26";
            return Util.getResponseofGet(Url);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("true"))
                {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String strUserName = jsonObject1.getString("email");
                        arrayUserName.add(strUserName);

                    }
                }
                else
                {
                    Toast.makeText(getActivity(),jsonObject.getString("msg"),Toast.LENGTH_SHORT).show();
                }


            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(),getString(R.string.something_went_wrong),Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {

            /** EDIT **/
            case R.id.action_cart:
                //openEditProfile(); //Open Edit Profile Fragment
//                Toast.makeText(getApplicationContext(),"GROUP CHAT",Toast.LENGTH_LONG).show();
                if(occupantIdsList.size() > 0) {
                    Log.e("OCCUPANT",""+occupantIdsList);
                    // create Group of the User
                    addChatGroup("MOHIT",occupantIdsList);
                }else{
                    Toast.makeText(getActivity(), "Please select user to create group", Toast.LENGTH_SHORT).show();
                }
                return true;



            default:
                return super.onOptionsItemSelected(item);
        }//end switch
    }//end onOptionsItemSelected


    // create User chat group
    public void addChatGroup(String name, ArrayList<Integer> userIds) {
        QBDialog dialog = new QBDialog();
        dialog.setName(name);
        dialog.setType(QBDialogType.GROUP);
        dialog.setOccupantsIds(userIds);

        QBGroupChatManager groupChatManager = QBChatService.getInstance().getGroupChatManager();
        groupChatManager.createDialog(dialog, new QBEntityCallback<QBDialog>() {
            @Override
            public void onSuccess(QBDialog dialog, Bundle args) {

                occupantIdsList.clear();
                Toast.makeText(getActivity(),"SUCCESS",Toast.LENGTH_SHORT).show();
                Log.e("RESPONSE "," "+dialog);
                Log.e("ARGS "," "+args);
                // create Group chat notification
                createChatNotificationForGroupChatCreation(dialog);

                QBSystemMessagesManager systemMessagesManager = QBChatService.getInstance().getSystemMessagesManager();
                Log.e("systemMessagesManager",""+systemMessagesManager);

                for (Integer userID : dialog.getOccupants()) {

                    QBChatMessage chatMessage = createChatNotificationForGroupChatCreation(dialog);
                    Log.e("chatMessage",""+chatMessage);
                    Calendar calander = Calendar.getInstance();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");

                    String time = simpleDateFormat.format(calander.getTime());
//                    long time = DateUtils.getCurrentTime();
                    Log.e("time",""+time);
                    chatMessage.setProperty("date_sent", time + "");
                    Log.e("userID",""+userID);
                    chatMessage.setRecipientId(userID);

                    try {
                        systemMessagesManager.sendSystemMessage(chatMessage);
                    } catch (SmackException.NotConnectedException e) {
                        Log.e("Exception ","EXC "+e);
                    } catch (IllegalStateException ee){
                        Log.e("Exception ","EE "+ee);
                    }
                }


                QBSystemMessageListener systemMessageListener = new QBSystemMessageListener() {
                    @Override
                    public void processMessage(QBChatMessage qbChatMessage) {
                        Log.e("SUCCESS",">> "+qbChatMessage);
                    }

                    @Override
                    public void processError(QBChatException e, QBChatMessage qbChatMessage) {
                        Log.e("SUCCESS",">> "+qbChatMessage);
                    }
                };
                systemMessagesManager.addSystemMessageListener(systemMessageListener);

            }

            @Override
            public void onError(QBResponseException errors) {
                Toast.makeText(getActivity(),"FAIL",Toast.LENGTH_SHORT).show();
            }
        });
    }


    // send notification to the user which are selected in group
    public static QBChatMessage createChatNotificationForGroupChatCreation(QBDialog dialog) {
        String dialogId = String.valueOf(dialog.getDialogId());
        String roomJid = dialog.getRoomJid();
        String occupantsIds = TextUtils.join(",", dialog.getOccupants());
        String dialogName = dialog.getName();
        String dialogTypeCode = String.valueOf(dialog.getType().ordinal());
        Log.e("DATA ",">>>>>>>>>>>>>>>>>>>>>>>>>>>");
        Log.e("dialogID",">> "+dialogId);
        Log.e("roomJid",">> "+roomJid);
        Log.e("occupantsIds",">> "+occupantsIds);
        Log.e("dialogName",">> "+dialogName);
        Log.e("dialogTypeCode",">> "+dialogTypeCode);


        QBChatMessage chatMessage = new QBChatMessage();
        chatMessage.setBody("optional text");

        // Add notification_type=1 to extra params when you created a group chat
        //
        chatMessage.setProperty("notification_type", "1");

        chatMessage.setProperty("_id", dialogId);
        if (!TextUtils.isEmpty(roomJid)) {
            chatMessage.setProperty("room_jid", roomJid);
        }
        chatMessage.setProperty("occupants_ids", occupantsIds);
        if (!TextUtils.isEmpty(dialogName)) {
            chatMessage.setProperty("name", dialogName);
        }
        chatMessage.setProperty("type", dialogTypeCode);



        return chatMessage;
    }


}
