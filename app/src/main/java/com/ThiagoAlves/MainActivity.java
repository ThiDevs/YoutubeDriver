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
    String elem2;
    Boolean verificador = false;
    //List<String> value = new ArrayList<String>();
    List<String> Links;
    List<String> Urls_Links = new ArrayList<String>();
   // ArrayAdapter<String> adapter;

    List<CardView> value;
    CardsAdapter adapter;

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

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-4653575622321119/2673683815");
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

                            try{ Thread.sleep(1000);} catch (Exception e){}

                            Thread OK2 = new Thread(new Runnable() {
                                public void run() {

                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    adapter.clear();
                                                } catch (Exception e ){
                                                    StringWriter errors = new StringWriter();
                                                    e.printStackTrace(new PrintWriter(errors));
                                                    Log.d("MyApp",errors.toString());

                                                }


                                                verificador = false;
                                            }
                                        });

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
                                        StringWriter errors = new StringWriter();
                                        e.printStackTrace(new PrintWriter(errors));
                                        Log.e("MyApp",errors.toString());
                                    }
                                    }

                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                try{
                                                int i = 0;
                                                for(String Title : separador) {
                                                    String Thumb = thumbs.get(i);
                                                    String Desc = desc.get(i);
                                                    String Channel = channel.get(i);
                                                    String Metadata =  datayt.get(i);

                                                    if (!Title.equals("")) {
                                                        adapter.add(new CardModel(Thumb, Title, Desc,Channel,Metadata));
                                                    }
                                                    i++;
                                                }
                                                } catch(Exception e){
                                                    e.printStackTrace();
                                                }

                                                try {
                                                    separador.clear();
                                                    thumbs.clear();
                                                    desc.clear();
                                                    channel.clear();
                                                    datayt.clear();
                                                } catch (Exception e){
                                                    StringWriter errors = new StringWriter();
                                                    e.printStackTrace(new PrintWriter(errors));
                                                    Log.d("MyApp",errors.toString());
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
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG2", "The interstitial wasn't loaded yet.");
                }


            }
        });


    }
}
