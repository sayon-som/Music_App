package com.sayon.music_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class Play extends AppCompatActivity {
    TextView textView;
    ImageView prev;
    ImageView next;
    ImageView play;
    ImageView pause;
    MediaPlayer mediaPlayer;
    SeekBar seekBar;
    ArrayList<File> songs;
    String name;
    Thread seek_update;
    int pos;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        seek_update.interrupt();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        textView = findViewById(R.id.textView);
        prev = findViewById(R.id.prev);
        next = findViewById(R.id.next);


        pause=findViewById(R.id.pause);
        seekBar=findViewById(R.id.seekBar);
        Intent intent = getIntent();
        Bundle bundle=intent.getExtras();
        songs=(ArrayList)bundle.getParcelableArrayList("list");
        name=intent.getStringExtra("current song");
        textView.setText(name);
        textView.setSelected(true);
        pos=intent.getIntExtra("position",0);
        Uri uri=Uri.parse(songs.get(pos).toString());
        mediaPlayer=MediaPlayer.create(this,uri);
        mediaPlayer.start();
        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
mediaPlayer.seekTo(seekBar.getProgress());
            }
        });
        //Setting up the thread to catch with the pace of the song.

seek_update=new Thread(){
    @Override
    public void run() {
        int cur_pos=0;
        try{
            while(cur_pos<mediaPlayer.getDuration()){
                cur_pos=mediaPlayer.getCurrentPosition();
                seekBar.setProgress(cur_pos);
            }
        }
        catch (Exception e){
            System.out.println("error occured");
        }
    }
};
seek_update.start();
pause.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if(mediaPlayer.isPlaying())
        {
            pause.setImageResource(R.drawable.play);
            mediaPlayer.pause();
        }
        else{
            pause.setImageResource(R.drawable.pause);
            mediaPlayer.start();
        }
    }
});
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if(pos!=0){
                    pos-=1;//getting the songs stored in the array list.
                }
                else{
                    pos=songs.size()-1;//to the songs from the array list in a circular fashion.
                }
                Uri uri=Uri.parse(songs.get(pos).toString());
                mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
                mediaPlayer.start();
                pause.setImageResource(R.drawable.pause);
                seekBar.setMax(mediaPlayer.getDuration());

                textView.setText(songs.get(pos).getName().toString());
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if(pos==songs.size()-1){
                    pos=0;
                }
                else{
                    pos+=1;
                }
                Uri uri=Uri.parse(songs.get(pos).toString());
                mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
                mediaPlayer.start();
                pause.setImageResource(R.drawable.pause);
                seekBar.setMax(mediaPlayer.getDuration());
                textView.setText(songs.get(pos).getName().toString());
            }
        });

        




    }
}