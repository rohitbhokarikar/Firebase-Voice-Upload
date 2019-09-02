package com.rb.com.firebasevupload;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.support.constraint.solver.widgets.Snapshot;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {


    private Button mRecordBtn;
    private TextView mRecordLabel;
    private MediaRecorder mRecorder;

    private StorageReference mStorage;
private ProgressDialog mProgressDialog;




    private  String mFileName = null;
    private static final String LOG_TAG = "Record_Log";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mStorage = FirebaseStorage.getInstance().getReference();

        mProgressDialog = new ProgressDialog(this);

        mRecordBtn = (Button)findViewById(R.id.recordbtn);
        mRecordLabel = (TextView)findViewById(R.id.recordlabel);

        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
         mFileName += "/record.3gp";



        mRecordBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                    startRecording();

                    mRecordLabel.setText("Recording Started ... ");

                }


            else if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    stopRecording();

                    mRecordLabel.setText("Recording Stoped ... ");


                }



                return false;
            }

        });
        }


    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        mRecorder.start();
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;

        uploadAudio();
    }

    private void uploadAudio() {

        mProgressDialog.setMessage("Uploading Audio File ....");
        mProgressDialog.show();
        StorageReference filepath = mStorage.child("audio").child("new_audio.3gp");

        Uri uri = Uri.fromFile(new File(mFileName));

        filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                mProgressDialog.setCancelable(false);

                mProgressDialog.dismiss();

                mRecordLabel.setText("UPLOAD DONE !!");

            }
        });



    }
}


