package com.delkappa.manos.videoplayer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Activity;

import android.net.Uri;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

public class MainActivity extends Activity {
    int firstUse=0;
    int videoProgress;
    int videoOrientation;
    int previousOrientation;
    int phoneOrientation;
    VideoView globalVidView;
    String  TAG = "TOTO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"entry onCreate");
        setContentView(R.layout.activity_main);
        if(savedInstanceState!=null){
            this.firstUse++;
            Log.d(TAG,"using saved instance "+videoProgress+" "+videoOrientation);
            this.videoProgress=savedInstanceState.getInt("progression");
            //globalVidView.seekTo(videoProgress);
        }
        else {
            this.videoProgress = 0;
        }

        // Get a reference to the VideoView instance as follows, using the id we set in the XML layout.
        VideoView vidView = (VideoView)findViewById(R.id.video);

        // Add playback controls.
        MediaController vidControl = new MediaController(this);
        // Set it to use the VideoView instance as its anchor.
        vidControl.setAnchorView(vidView);
        // Set it as the media controller for the VideoView object.
        vidView.setMediaController(vidControl);


        // Prepare the URI for the endpoint.
        String vidAddress = "android.resource://" + getPackageName() + "/" + R.raw.video;
        Uri vidUri = Uri.parse(vidAddress);
        // Parse the address string as a URI so that we can pass it to the VideoView object.
        vidView.setVideoURI(vidUri);
        globalVidView = vidView;
        globalVidView.seekTo(videoProgress);
        // Start playback.


        globalVidView.start();


    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        Log.d(TAG,"entry onSave");
        savedInstanceState.putInt("progression",globalVidView.getCurrentPosition());
        savedInstanceState.putInt("orientation",this.getResources().getConfiguration().orientation);
        Log.d(TAG,"entry onSave "+globalVidView.getCurrentPosition()+" "+this.getResources().getConfiguration().orientation);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(TAG,"entry onRestore");
        videoProgress=savedInstanceState.getInt("progression");
        videoOrientation=savedInstanceState.getInt("orientation");
        Log.d(TAG,"entry onRestore "+globalVidView.getCurrentPosition()+" "+this.getResources().getConfiguration().orientation);

    }


    @Override
    protected void onPause() {

        super.onPause();
        Log.d(TAG,"entry onPause");
        globalVidView.pause();
        videoProgress=globalVidView.getCurrentPosition();
        previousOrientation=this.getResources().getConfiguration().orientation;

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"entry onResume");
        phoneOrientation=this.getResources().getConfiguration().orientation;
        globalVidView.pause();

        Log.d(TAG,"firstUse value: "+firstUse);
        if(videoProgress>0 && phoneOrientation == previousOrientation && firstUse>0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Do you want to continue the video at where you stopped ?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //On fait rien xD
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            videoProgress = 0;
                            globalVidView.seekTo(videoProgress);
                        }
                    });
            // Create the AlertDialog object and return it
            AlertDialog alertD = builder.create();
            alertD.show();
        }
        globalVidView.seekTo(videoProgress);
        globalVidView.start();
    }


}