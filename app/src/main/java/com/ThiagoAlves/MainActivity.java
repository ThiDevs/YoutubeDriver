package com.ThiagoAlves;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.support.v7.widget.CardView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity {
    EditText Text,IP;
    String msg = "";
    String ip = "";
    Socket soc;
    PrintWriter writer;
    Button Ok;
    Thread t;
    List<String> separador;
    List<String> thumbs;
    List<String> desc;
    List<String> channel;
    List<String> datayt;

    Boolean verificador = false;

    CardsAdapter adapter;
    ListView listView;

    ListView lvCards;
    int itemPosition;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, "ca-app-pub-4653575622321119~5903741568");

        AdView adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        Search Pesquisa = new Search("Raplord");


        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-4653575622321119/5751149581");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());


        Text = findViewById(R.id.Text);
        IP =  findViewById(R.id.text3);
        Ok =  findViewById(R.id.Ok);
        lvCards =  findViewById(R.id.list);

        adapter = new CardsAdapter(this);
        lvCards.setAdapter(adapter);
        lvCards.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                itemPosition = position;

                Thread EnviarIndexVideo = new Thread(new Runnable() {
                    public void run() {
                        try {
                            Socket soc5 = new Socket(ip, 7878);
                            PrintWriter writer = new PrintWriter(soc5.getOutputStream());
                            writer.write("@CodVIDEO==" + itemPosition);
                            writer.flush();
                            writer.close();
                            soc5.close();
                            Toast.makeText(getApplicationContext(),"Video Solicitado :"+itemPosition , Toast.LENGTH_SHORT)
                                    .show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }});
                EnviarIndexVideo.start();

                // Show Alert
                Toast.makeText(getApplicationContext(),"Position :"+itemPosition+"  ListItem : " , Toast.LENGTH_SHORT)
                        .show();

                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG2", "The interstitial wasn't loaded yet.");
                }



            }

        });

        Text.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {



            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                /*This method is called to notify you that, within s, the count characters beginning at start are about to be replaced by new text with length after. It is an error to attempt to make changes to s from this callback.*/

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

                Log.e("Teclas", s.toString());
                msg = s.toString();
                t = new Thread(new Runnable() {
                    public void run() {
                        try {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ip = IP.getText().toString();
                                }
                            });
                        }   catch(Exception e){
                            e.printStackTrace();
                        }
                        try {
                            soc = new Socket(ip, 7878);
                        } catch(Exception e){
                            e.printStackTrace();
                        }
                        try{
                            writer = new PrintWriter(soc.getOutputStream());
                            writer.write(msg);
                            writer.flush();
                            writer.close();
                            soc.close();
                        } catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                });
                t.start();

            }
        });


        Ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Thread OK = new Thread(new Runnable() {
                    public void run() {
                        try{ t.sleep(1000);}catch (Exception e){}
                        try {
                            Socket soc2 = new Socket(ip, 7878);
                            PrintWriter writer = new PrintWriter(soc2.getOutputStream());
                            writer.write("@Cod==1");
                            writer.flush();
                            writer.close();
                            soc2.close();


                            Thread OK2 = new Thread(new Runnable() {
                                public void run() {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                setAdapter();
                                                lvCards.setAdapter(null);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                    try{ t.sleep(1000);}catch (Exception e){}
                                    while (!verificador) {

                                    try {

                                        Socket soc2 = new Socket(ip, 7879);
                                        InputStream is = soc2.getInputStream();
                                        InputStreamReader isr = new InputStreamReader(is,"utf-8");
                                        BufferedReader br = new BufferedReader(isr);

                                        String message = br.readLine();
                                        separador = Arrays.asList(message.split(";"));

                                        message = br.readLine();
                                        thumbs = Arrays.asList(message.split(";"));

                                        message = br.readLine();
                                        desc = Arrays.asList(message.split(";"));

                                        message = br.readLine();
                                        channel = Arrays.asList(message.split(";"));

                                        message = br.readLine();
                                        datayt = Arrays.asList(message.split(";"));

                                        verificador = true;

                                        soc2.close();

                                    } catch (Exception e) {
                                    }
                                    }

                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                try{
                                                    int i = 0;
                                                    for(String Title : separador) {
                                                        String Thumb = "";
                                                        String Desc = "";
                                                        String Channel = "";
                                                        String Metadata = "";
                                                        try{
                                                            Thumb = thumbs.get(i);
                                                        } catch (Exception e ){

                                                        }
                                                        try{
                                                            Desc = desc.get(i);
                                                        } catch (Exception e ){

                                                        }
                                                        try{
                                                            Channel = channel.get(i);
                                                        } catch (Exception e ){

                                                        }
                                                        try{
                                                            Metadata =  datayt.get(i);
                                                        } catch (Exception e ){

                                                        }


                                                    if (!Title.equals("")) {
                                                        adapter.add(new CardModel(Thumb, Title, Desc,Channel,Metadata));
                                                    }
                                                    i++;
                                                }
                                                    lvCards.setAdapter(adapter);
                                                    verificador = false;
                                                } catch(Exception e){
                                                    e.printStackTrace();
                                                }


                                            }});


                                }
                            });
                            OK2.start();

                            try {
                                soc.close();
                            } catch (Exception e){
                                e.printStackTrace();
                            }
                        } catch (UnknownHostException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }});

                OK.start();



            }
        });


    }

    public void setAdapter() {
        adapter = new CardsAdapter(this);
    }
}

