package com.archi.intrisfeed.fragment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.archi.intrisfeed.R;
import com.archi.intrisfeed.adapter.ChatMsgListAdapter;
import com.archi.intrisfeed.util.Constant;
import com.archi.intrisfeed.util.Util;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBPrivateChat;
import com.quickblox.chat.QBPrivateChatManager;
import com.quickblox.chat.exception.QBChatException;
import com.quickblox.chat.listeners.QBMessageListener;
import com.quickblox.chat.listeners.QBPrivateChatManagerListener;
import com.quickblox.chat.model.QBDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.QBSettings;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.request.QBRequestGetBuilder;
import com.quickblox.users.model.QBUser;

import org.jivesoftware.smack.SmackException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by archi on 9/29/2016.
 */

public class TestChat extends AppCompatActivity {
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
    public String email, fullname, id;

    TextView userNameTv, emailTv, sendTv;
    EditText messageEdt;
    QBPrivateChatManagerListener privateChatManagerListener;
    QBMessageListener<QBPrivateChat> privateChatMessageListener;
    public String dia;
    public ArrayList<String> arrayDialogId;
    public ArrayList<QBDialog> dialogs;
    public ListView lvChatDetails;
    public ArrayList<HashMap<String, String>> chatArraylist;
    public HashMap<String, String> hashmap;
    ChatMsgListAdapter chatListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_chat);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        lvChatDetails = (ListView) findViewById(R.id.lvChatDetails_TestChat);
        sendTv = (TextView) findViewById(R.id.activity_user_send);
        messageEdt = (EditText) findViewById(R.id.activity_user_message);

        arrayDialogId = new ArrayList<>();
        QBSettings.getInstance().init(getApplicationContext(), APP_ID, AUTH_KEY, AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);

//        try {
//            chatDialogs = QBChatService.getChatDialogs(QBDialogType.PRIVATE, new QBRequestGetBuilder(), new Bundle());
//        } catch (QBResponseException e) {
//            e.printStackTrace();
//        }


        if (getIntent().getExtras() != null) {

            email = getIntent().getExtras().getString("email");
            fullname = getIntent().getExtras().getString("fullname");
            id = getIntent().getExtras().getString("id");
            Util.WriteSharePrefrence(getApplicationContext(), Constant.SHRED_PR.KEY_USER_CHAT_ID, id);

            userNameTv = (TextView) findViewById(R.id.activity_user_name);
            emailTv = (TextView) findViewById(R.id.activity_user_email);

            userNameTv.setText(fullname);
            emailTv.setText(email);

        }

        // clear chat list
        chatArraylist = new ArrayList<HashMap<String, String>>();
        chatArraylist.clear();


        QBChatService chatService = QBChatService.getInstance();
        final QBPrivateChatManager chatMessage = chatService.getPrivateChatManager();
//        QBPrivateChat chat = chatMessage.getChat(12);
//        boolean isLoggedIn = chatService.isLoggedIn();
        QBUser user = chatService.getUser();
        String data = user.getEmail();
        final QBRequestGetBuilder requestBuilder = new QBRequestGetBuilder();
        requestBuilder.setLimit(100);

        QBChatService.getChatDialogs(QBDialogType.PRIVATE, requestBuilder, new QBEntityCallback<ArrayList<QBDialog>>() {
            @Override
            public void onSuccess(ArrayList<QBDialog> dialogs, Bundle args) {
                int totalEntries = args.getInt("total_entries");

//                Log.d("MainData",dialogs.get(0).getLastMessage());
//                dialogs.get(0).getDialogId();
//                dia = dialogs.get(0).getDialogId();
//                Log.d("Dialog_Data", dia);
                for (int i = 0; i < dialogs.size(); i++) {
//                    Log.e("COMPARE", " " + id + " WITH " + dialogs.get(i).getOccupants().get(0));

                    for (int j = 0; j < dialogs.get(i).getOccupants().size(); j++) {

                        if (Integer.parseInt(id) == dialogs.get(i).getOccupants().get(j)) {
                            Toast.makeText(TestChat.this, "GetCHat", Toast.LENGTH_SHORT).show();
                            dia = dialogs.get(i).getDialogId();
                            QBDialog qbDialog = new QBDialog(dia);
//        QBDialog qbDialog = new QBDialog(dia);

                            requestBuilder.setLimit(100);

                            QBChatService.getDialogMessages(qbDialog, requestBuilder, new QBEntityCallback<ArrayList<QBChatMessage>>() {

                                @Override
                                public void onSuccess(ArrayList<QBChatMessage> messages, Bundle args) {

                                    for (int i = 0; i < messages.size(); i++) {
                                        hashmap = new HashMap<String, String>();
                                        hashmap.put("id", "" + messages.get(i).getId());
                                        hashmap.put("msg", "" + messages.get(i).getBody());
                                        hashmap.put("recipient_id", "" + messages.get(i).getRecipientId());
                                        hashmap.put("sender_id", "" + messages.get(i).getSenderId());
                                        hashmap.put("updated_at", "" + messages.get(i).getProperties().get("updated_at"));



                                        Log.e("getMESSAGE", "" + messages.get(i).getBody());
                                        Log.e("MSG ", ">> " + messages);
                                        chatArraylist.add(hashmap);
                                    }

                                    chatListAdapter = new ChatMsgListAdapter(getApplicationContext(), chatArraylist);
                                    lvChatDetails.setAdapter(chatListAdapter);
                                    chatListAdapter.notifyDataSetChanged();
                                    scrollMyListViewToBottom();






                                }

                                @Override
                                public void onError(QBResponseException errors) {
                                    Log.e("ERROR ", ">> " + errors);
                                }
                            });
                            arrayDialogId.add(dialogs.get(i).getDialogId());
                        } else {
                            Log.e("DID", "ELSE>>> " + dialogs.get(i).getDialogId());
                        }


                    }

                }
            }

            @Override
            public void onError(QBResponseException errors) {

            }
        });


        privateChatMessageListener = new QBMessageListener<QBPrivateChat>() {
            @Override
            public void processMessage(QBPrivateChat privateChat, final QBChatMessage chatMessage) {
                Toast.makeText(TestChat.this, "Suceess" + privateChat.getDialogId(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void processError(QBPrivateChat privateChat, QBChatException error, QBChatMessage originMessage) {
                Toast.makeText(TestChat.this, "Error", Toast.LENGTH_SHORT).show();
            }
        };

        privateChatManagerListener = new QBPrivateChatManagerListener() {
            @Override
            public void chatCreated(final QBPrivateChat privateChat, final boolean createdLocally) {
                if (!createdLocally) {
                    privateChat.addMessageListener(privateChatMessageListener);
//                    dialogId = privateChat.getDialogId();
//                    Log.e("DIALOG ID",">>2>> "+dialogId);
                }
            }
        };
        sendTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (messageEdt.length() > 0) {

                    QBChatService.getInstance().getPrivateChatManager().addPrivateChatManagerListener(privateChatManagerListener);
                    Integer opponentId = Integer.parseInt(id);

                    try {
                        QBChatMessage chatMessage1 = new QBChatMessage();
                        chatMessage1.setBody(messageEdt.getText().toString());
                        chatMessage1.setProperty("notification_type", "1");
                        chatMessage1.setProperty("save_to_history", "1"); // Save a message to history

                        // create a message
                        QBPrivateChat privateChat = chatMessage.getChat(opponentId);
                        if (privateChat == null) {
                            privateChat = chatMessage.createChat(opponentId, privateChatMessageListener);
                        }

                        privateChat.sendMessage(chatMessage1);
                        String ChatID = Util.ReadSharePrefrence(TestChat.this,Constant.ChatId);
                        String Tag = "sendChat";
                        Log.e(Tag, "chat massage >> " + chatMessage1);
                        Log.e(Tag, "Chat user Id :"+ ChatID);
                        Log.e(Tag,"Chat sender Id :"+id);
                        hashmap = new HashMap<String, String>();
                        hashmap.put("id", "" + id);
                        hashmap.put("msg", "" +messageEdt.getText().toString());
                        hashmap.put("recipient_id", "");
                        hashmap.put("sender_id", "");
                        hashmap.put("updated_at", "" );
                        chatArraylist.add(hashmap);
//
                        chatListAdapter = new ChatMsgListAdapter(getApplicationContext(), chatArraylist);
                        lvChatDetails.setAdapter(chatListAdapter);
//                        lvChatDetails.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
//                        lvChatDetails.setStackFromBottom(true);
                        chatListAdapter.notifyDataSetChanged();
                        messageEdt.setText("");
                        scrollMyListViewToBottom();

                        // refresh adapter
//                        chatListAdapter.notifyDataSetChanged();

                    } catch (SmackException.NotConnectedException e) {

                    }

//                    File filePhoto = new File("holy_grail.png");
//                    Boolean fileIsPublic = false;
//                    QBContent.uploadFileTask(filePhoto, fileIsPublic, null, new QBEntityCallback<QBFile>() {
//                        @Override
//                        public void onSuccess(QBFile file, Bundle params) {
//
//                            // create a message
//                            QBChatMessage chatMessage = new QBChatMessage();
//                            chatMessage.setProperty("save_to_history", "1"); // Save a message to history
//
//                            // attach a photo
//                            QBAttachment attachment = new QBAttachment("photo");
//                            attachment.setId(file.getId().toString());
//                            chatMessage.addAttachment(attachment);
//
//                            // send a message
//                            // ...
//                        }
//
//                        @Override
//                        public void onError(QBResponseException errors) {
//                            // error
//                        }
//                    });



                }

            }
        });






//        QBDialog qbDialog = new QBDialog("5805f09ea0eb47288300002e");
////        QBDialog qbDialog = new QBDialog(dia);
//
//        requestBuilder.setLimit(100);
//
//        QBChatService.getDialogMessages(qbDialog, requestBuilder, new QBEntityCallback<ArrayList<QBChatMessage>>() {
//
//            @Override
//            public void onSuccess(ArrayList<QBChatMessage> messages, Bundle args) {
//
//                for (int i = 0; i < messages.size(); i++) {
//                    hashmap = new HashMap<String, String>();
//                    hashmap.put("id", "" + messages.get(i).getId());
//                    hashmap.put("msg", "" + messages.get(i).getBody());
//                    hashmap.put("recipient_id", "" + messages.get(i).getRecipientId());
//                    hashmap.put("sender_id", "" + messages.get(i).getSenderId());
//                    hashmap.put("updated_at", "" + messages.get(i).getProperties().get("updated_at"));
//
//                    Log.e("getMESSAGE", "" + messages.get(i).getBody());
//                    Log.e("MSG ", ">> " + messages);
//                    chatArraylist.add(hashmap);
//                }
//
//                chatListAdapter = new ChatMsgListAdapter(getApplicationContext(), chatArraylist);
//                lvChatDetails.setAdapter(chatListAdapter);
//                chatListAdapter.notifyDataSetChanged();
//
//            }
//
//            @Override
//            public void onError(QBResponseException errors) {
//                Log.e("ERROR ", ">> " + errors);
//            }
//        });

//        QBChatService.getInstance().getPrivateChatManager().addPrivateChatManagerListener(privateChatManagerListener);
//
//        Integer opponentId = Integer.parseInt(id);
//
//        try {
//            QBChatMessage chatMessage1 = new QBChatMessage();
//            chatMessage1.setBody("Hi there123!");
//            chatMessage1.setProperty("save_to_history", "1"); // Save a message to history
//
//            QBPrivateChat privateChat = chatMessage.getChat(opponentId);
//            if (privateChat == null) {
//                privateChat = chatMessage.createChat(opponentId, privateChatMessageListener);
//            }
//            privateChat.sendMessage(chatMessage1);
//        } catch (SmackException.NotConnectedException e) {
//
//        }


//        TockenGenerate();
//        CreateSession();
//
//        ChatRegister();


    }

    private void scrollMyListViewToBottom()
    {
        lvChatDetails.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                lvChatDetails.setSelection(chatArraylist.size() - 1);
            }
        });

    }


//    @Override
//    public void processMessage(QBPrivateChat privateChat, final QBChatMessage chatMessage) {
//        for(QBAttachment attachment : chatMessage.getAttachments()){
//            Integer fileId = attachment.getId();
//
//            // download a file
//            QBContent.downloadFileTask(fileId, new QBEntityCallback<InputStream>(){
//                @Override
//                public void onSuccess(InputStream inputStream, Bundle params) {
//                    // process file
//                }
//
//                @Override
//                public void onError(QBResponseException errors) {
//                    // errors
//                }
//            });
//        }
//    }

//    public QBDialog getDialogOfOpponent(int opponentId) {
//        for (QBDialog dialog : chatDialogs) {
//            if (dialog.getOccupants().contains(opponentId)) {
//                return dialog;
//            }
//        }
//        return null;
//    }
}