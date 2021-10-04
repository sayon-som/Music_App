package com.sayon.music_app;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView=findViewById(R.id.listview);

        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        Toast.makeText(MainActivity.this, "Permission given ", Toast.LENGTH_SHORT).show();
                        ArrayList<File>songs=getsongs(Environment.getExternalStorageDirectory());//putting the fetched data into the array list
                        String item[]=new String[songs.size()];
                        for(int i=0;i<songs.size();i++){//the idea is to remove the mp3 word from the songs listed in the list view
                            item[i]=songs.get(i).getName().replace(".mp3","");
                        }
                        /* setting up at the adapter */
                        ArrayAdapter <String> ad = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1,item);
                        listView.setAdapter(ad);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent=new Intent(MainActivity.this,Play.class);
                                String curr=listView.getItemAtPosition(position).toString();
                                intent.putExtra("list",songs);
                                intent.putExtra("current song",curr);
                                intent.putExtra("position",position);
startActivity(intent);


                            }
                        });


                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
permissionToken.continuePermissionRequest();
                    }
                })
                .check();


    }
    public ArrayList<File> getsongs(File file){//getting the required data
        ArrayList arrayList= new ArrayList();
        File songs[]=file.listFiles();//listing the contents of the files from the given directory
        if(songs!=null){
            for(File i:songs){
                 if(!i.isHidden() && i.isDirectory()){
                     arrayList.addAll(getsongs(i));
                 }
                 else{
                     if(i.getName().endsWith(".mp3") && !i.getName().startsWith(".")){
                         arrayList.add(i);
                     }
                 }


            }

        }
        return arrayList;
    }


}