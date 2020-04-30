package com.example.irrigation2;

import android.os.AsyncTask;

public class DownloadJsonAsyncTask extends AsyncTask<String,Void, String> {
    private int cont=0;

    @Override
    protected String doInBackground(String... params) {

        return (""+cont);
    }
}
