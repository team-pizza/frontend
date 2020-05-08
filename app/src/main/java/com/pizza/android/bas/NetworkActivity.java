package com.pizza.android.bas;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

// Uses this as a guide: https://developer.android.com/training/basics/network-ops/connecting#java

import com.pizza.android.bas.networking.DownloadCallback;

public class NetworkActivity extends FragmentActivity implements DownloadCallback {

    // Keep a reference to the NetworkFragment, which owns the AsyncTask object
    // that is used to execute network ops.
    private NetworkFragment networkFragment;

    // Boolean telling us whether a download is in progress, so we don't trigger overlapping
    // downloads with consecutive button clicks.
    private boolean downloading = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        networkFragment = NetworkFragment.getInstance(getSupportFragmentManager(), "https://www.google.com");
        // Retain this Fragment across configuration changes in the host Activity.
        networkFragment.setRetainInstance(true);
    }


    @Override
    public void updateFromDownload(Object result) {
        // Should pass result to requested fragment
    }

    @Override
    public NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo;
    }

    @Override
    public void onProgressUpdate(int progressCode, int percentComplete) {
        switch(progressCode) {
            // You can add UI behavior for progress updates here.
            case Progress.ERROR:
                break;
            case Progress.CONNECT_SUCCESS:
                Log.i("NetworkActivity", "Connect_Success");
                break;
            case Progress.GET_INPUT_STREAM_SUCCESS:
                Log.i("NetworkActivity", "Get Input Stream Success");
                break;
            case Progress.PROCESS_INPUT_STREAM_IN_PROGRESS:
                Log.i("NetworkActivity", "Input STream in Progress");
                break;
            case Progress.PROCESS_INPUT_STREAM_SUCCESS:
                Log.i("NetworkActivity", "Input Stream Success");
                break;
        }
    }

    @Override
    public void finishDownloading() {
        downloading = false;
        if (networkFragment != null) {
            networkFragment.cancelDownload();
        }
    }

    private void startDownload() {
        if (!downloading && networkFragment != null) {
            // Execute the async download.
            networkFragment.startDownload();
            downloading = true;
        }
    }
}

