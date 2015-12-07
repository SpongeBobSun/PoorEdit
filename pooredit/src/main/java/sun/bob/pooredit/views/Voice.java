package sun.bob.pooredit.views;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.ImageView;

import java.io.*;
import java.io.File;
import java.util.Calendar;

import sun.bob.pooredit.R;

/**
 * Created by bob.sun on 15/12/2.
 */
public class Voice extends BaseContainer {

    private String voiceFilePath;
    private boolean empty = true;
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private boolean recording = false;
    private boolean playing = false;

    public Voice(Context context) {
        super(context);
    }

    @Override
    public void initUI() {
        ImageView imageView = new ImageView(getContext());
        imageView.setImageResource(R.drawable.voice);
        this.addView(imageView);
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                OnTouchFunction(v);
            }
        });
    }

    @Override
    protected void setType() {

    }

    @Override
    public Object getJsonBean() {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return empty;
    }

    @Override
    public void focus() {

    }

    private boolean OnTouchFunction(View view){
        // TODO: 15/12/6 Use snackbar to record and playing voices.
        if (recording){
            return false;
        }
        if (empty){
            //Record voice.
            final Snackbar snackbar = Snackbar.make(view, "Recording", Snackbar.LENGTH_INDEFINITE);
            mediaRecorder = new MediaRecorder();
            try {
                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                voiceFilePath = "/sdcard/pooredit/audios/";
                java.io.File file = new File(voiceFilePath);
                if (!file.exists()){
                    file.mkdirs();
                }
                voiceFilePath += Calendar.getInstance().getTimeInMillis();
                mediaRecorder.setOutputFile(voiceFilePath);
                mediaRecorder.prepare();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            mediaRecorder.start();
            recording = true;
            snackbar.setAction("Stop", new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mediaRecorder.stop();
                    mediaRecorder.release();
                    recording = false;
                    empty = false;
                    snackbar.dismiss();
                }
            });
            snackbar.show();
        } else {
            //Play voice.
            final Snackbar snackbar = Snackbar.make(view, "Playing", Snackbar.LENGTH_INDEFINITE);
            mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(voiceFilePath);
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            recording = false;
            mediaPlayer.start();
            snackbar.setAction("Stop", new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    recording = false;
                    empty = false;
                    snackbar.dismiss();
                }
            });
            snackbar.show();
        }
        return false;
    }
}
