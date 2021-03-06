package com.archi.intrisfeed.util;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by archi_info on 11/14/2016.
 */

public class DownloadFileFromURL extends AsyncTask<String, String, String> {
    Listner listner;
    File file;
    String filename;

    public DownloadFileFromURL(Listner listner, File wallpaperDirectory, String filename) {
        this.listner = listner;
        file = wallpaperDirectory;
        this.filename = filename;
    }

    /**
     * Before starting background thread
     * Show Progress Bar Dialog
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
//        showDialog(progress_bar_type);
    }

    /**
     * Downloading file in background thread
     */
    @Override
    protected String doInBackground(String... f_url) {
        int count;
        try {
            URL url = new URL(f_url[0]);
            URLConnection conection = url.openConnection();
            conection.connect();
            // this will be useful so that you can show a tipical 0-100% progress bar
            int lenghtOfFile = conection.getContentLength();

            // download the file
            InputStream input = new BufferedInputStream(url.openStream(), 8192);

            // Output stream
            File outputFile = new File(file, filename);
            OutputStream output = new FileOutputStream(outputFile);

            byte data[] = new byte[1024];

            long total = 0;

            while ((count = input.read(data)) != -1) {
                total += count;
                // publishing the progress....
                // After this onProgressUpdate will be called
                publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                // writing data to file
                output.write(data, 0, count);
            }

            // flushing output
            output.flush();

            // closing streams
            output.close();
            input.close();

        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        }

        return null;
    }

    /**
     * Updating progress bar
     */
    protected void onProgressUpdate(String... progress) {
        // setting progress percentage
//        pDialog.setProgress(Integer.parseInt(progress[0]));
    }

    /**
     * After completing background task
     * Dismiss the progress dialog
     **/
    @Override
    protected void onPostExecute(String file_url) {
        // dismiss the dialog after the file was downloaded
//        dismissDialog(progress_bar_type);

        // Displaying downloaded image into image view
        // Reading image path from sdcard
        listner.onSuccess("done");
        // setting downloaded into image view
//        my_image.setImageDrawable(Drawable.createFromPath(imagePath));
    }

}
