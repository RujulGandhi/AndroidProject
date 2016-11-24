package com.archi.intrisfeed.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.archi.intrisfeed.R;
import com.archi.intrisfeed.adapter.FeedAllAdapter;
import com.archi.intrisfeed.adapter.FeedArticlesAdapter;
import com.archi.intrisfeed.adapter.FeedBlogsAdapter;
import com.archi.intrisfeed.adapter.FeedImageAdapter;
import com.archi.intrisfeed.adapter.FeedVideoAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by archi_info on 11/14/2016.
 */

public class InterisfeedDetails extends Fragment implements View.OnClickListener {
    public String response, category, keyword;
    public HashMap<String, String> hashmap;
    public ArrayList<HashMap<String, String>> imageFeedList;
    private FeedImageAdapter adapterFeedImage;
    public ListView lvAllFeedList;
    private FeedVideoAdapter adapterFeedVideo;
    private FeedBlogsAdapter adapterFeedBlogs;
    private FeedArticlesAdapter adapterFeedArticles;
    private FeedAllAdapter adapterFeedAll;
    private TextView tvBackBottom;


//    http://181.224.157.105/~hirepeop/host1/intrisfeed/api/send_mail_post.php?post_id=26&subject=abcd&description=all
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_interisfeed_details, container, false);

        lvAllFeedList = (ListView)view.findViewById(R.id.fragment_intrisfeed_details_list);
        tvBackBottom = (TextView) view.findViewById(R.id.tvBackBottom);
        tvBackBottom.setOnClickListener(this);


        Bundle extras = getArguments();
        if (extras != null) {
            keyword = extras.getString("keyword");
            category = extras.getString("category");
            response = extras.getString("response");
            imageFeedList = new ArrayList<HashMap<String, String>>();
        }

        try {
            JSONObject object = new JSONObject(response.toString());
            if (object.getString("status").equalsIgnoreCase("true")) {
                Log.e("DATA", "resp " + object.getJSONArray("data"));
                JSONArray arry = object.getJSONArray("data");

                // get AND  SET IMAGE LIST
//                if (keyword.equalsIgnoreCase("Pics")) {
//                    for (int i = 0; i < arry.length(); i++) {
//                        JSONObject obj = arry.getJSONObject(i);
//
//                        hashmap = new HashMap<String, String>();
//                        if (!obj.get("image").toString().equalsIgnoreCase("")) {
//                            Log.e("CATEGORY ", ">> " + obj.getString("category"));
//                            hashmap.put("category", obj.getString("category"));
//                            hashmap.put("image", "" + obj.getString("image"));
//                            hashmap.put("content_link", "" + obj.get("content_link"));
//                            imageFeedList.add(hashmap);
//                        }
//
//                    }
//                    adapterFeedImage = new FeedImageAdapter(getActivity(), imageFeedList, getActivity().getFragmentManager());
//                    lvAllFeedList.setAdapter(adapterFeedImage);
//                    adapterFeedImage.notifyDataSetChanged();
//                }

                // get & set Video List
                if (keyword.equalsIgnoreCase("Videos")) {
                    for (int i = 0; i < arry.length(); i++) {
                        JSONObject obj = arry.getJSONObject(i);

                        hashmap = new HashMap<String, String>();
                        if (!obj.get("video").toString().equalsIgnoreCase("")) {
                            Log.e("CATEGORY ", ">> " + obj.getString("category"));
                            hashmap.put("id", obj.getString("id"));
                            hashmap.put("category", obj.getString("category"));
                            hashmap.put("video", "" + obj.getString("video"));
                            hashmap.put("content_link", "" + obj.get("content_link"));
                            hashmap.put("content_title","" + obj.get("content_title"));
                            imageFeedList.add(hashmap);
                        }

                    }
                    adapterFeedVideo = new FeedVideoAdapter(getActivity(), imageFeedList, getActivity().getFragmentManager());
                    lvAllFeedList.setAdapter(adapterFeedVideo);
                    adapterFeedVideo.notifyDataSetChanged();
                }


                // get & set Document List
                if (keyword.equalsIgnoreCase("Blogs")) {
                    for (int i = 0; i < arry.length(); i++) {
                        JSONObject obj = arry.getJSONObject(i);

                        hashmap = new HashMap<String, String>();
                        if (!obj.get("doc").toString().equalsIgnoreCase("")) {
                            Log.e("CATEGORY ", ">> " + obj.getString("category"));
                            hashmap.put("id", obj.getString("id"));
                            hashmap.put("category", obj.getString("category"));
                            hashmap.put("doc", "" + obj.getString("doc"));
                            hashmap.put("content_link", "" + obj.get("content_link"));
                            hashmap.put("content_title","" + obj.get("content_title"));
                            imageFeedList.add(hashmap);
                        }
                    }
                    adapterFeedBlogs = new FeedBlogsAdapter(getActivity(), imageFeedList, getActivity().getFragmentManager());
                    lvAllFeedList.setAdapter(adapterFeedBlogs);
                    adapterFeedBlogs.notifyDataSetChanged();
                }


                // get & set Articles List
                if (keyword.equalsIgnoreCase("Articles")) {
                    for (int i = 0; i < arry.length(); i++) {
                        JSONObject obj = arry.getJSONObject(i);

                        hashmap = new HashMap<String, String>();
                        if (!obj.get("content_details").toString().equalsIgnoreCase("")) {
                            Log.e("CATEGORY ", ">> " + obj.getString("category"));
                            hashmap.put("id", obj.getString("id"));
                            hashmap.put("category", obj.getString("category"));
                            hashmap.put("content_details", "" + obj.getString("content_details"));
                            hashmap.put("content_link", "" + obj.get("content_link"));
                            hashmap.put("content_title","" + obj.get("content_title"));
                            imageFeedList.add(hashmap);
                        }
                    }
                    adapterFeedArticles = new FeedArticlesAdapter(getActivity(), imageFeedList, getActivity().getFragmentManager());
                    lvAllFeedList.setAdapter(adapterFeedArticles);
                    adapterFeedArticles.notifyDataSetChanged();
                }


                // get & set All List
                if (keyword.equalsIgnoreCase("All")) {
                    for (int i = 0; i < arry.length(); i++) {
                        JSONObject obj = arry.getJSONObject(i);

                        hashmap = new HashMap<String, String>();

                        Log.e("CATEGORY ", ">> " + obj.getString("content_title"));
                        hashmap.put("id", obj.getString("id"));
                        hashmap.put("category", obj.getString("category"));
                        hashmap.put("content_title", obj.getString("content_title"));
                        hashmap.put("image", "" + obj.getString("image"));
                        hashmap.put("doc", "" + obj.getString("doc"));
                        hashmap.put("video", "" + obj.getString("video"));
                        hashmap.put("content_details", "" + obj.getString("content_details"));
                        hashmap.put("content_link", "" + obj.getString("content_link"));
                        imageFeedList.add(hashmap);

                    }
//                        setListViewHeightBasedOnChildren(lvAllFeedList);
                        adapterFeedAll = new FeedAllAdapter(getActivity(), imageFeedList, getActivity().getFragmentManager());
                        lvAllFeedList.setAdapter(adapterFeedAll);
                        adapterFeedAll.notifyDataSetChanged();

//                    Bundle bundle = new Bundle();
//                    bundle.putString("valuesArray", s);
//                    Fragment fragment = new InterisfeedDetails();
//                    fragment.setArguments(bundle);
//                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                    fragmentTransaction.replace(R.id.container_body, fragment);
//                    fragmentTransaction.commit();

                }


            } else {
                Toast.makeText(getActivity(), "" + object.getString("msg"), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return view;
    }

    @Override
    public void onClick(View v) {
        Fragment fragment = new InterisfeedFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_body, fragment);
        fragmentTransaction.commit();
    }
}
