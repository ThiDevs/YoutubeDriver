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
    //CustomListAdapter adapter;
    final Integer[] itemname2 ={
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                                    List<String> titles = new ArrayList<String>();
                                    while (!verificador) {
                                    try {

                                        Log.e("MyApp","Entrei");
                                        Socket soc2 = new Socket(ip, 7879);
                                        InputStream is = soc2.getInputStream();
                                        InputStreamReader isr = new InputStreamReader(is,"utf-8");
                                        BufferedReader br = new BufferedReader(isr);
                                        String message = br.readLine();
                                        Log.d("MyApp", "Titles:" + message);
                                        separador = Arrays.asList(message.split(";"));
                                        message = br.readLine();
                                        thumbs = Arrays.asList(message.split(";"));
                                        Log.d("MyApp", "Imagens:" + message);

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
                                                int i = 0;
                                                for(String elem : separador) {
                                                    elem2 = elem;
                                                    if (!elem.equals("")) {
                                                        adapter.add(new CardModel(thumbs.get(i), elem2, ""));
                                                        Log.e("MyApp", elem);
                                                    }
                                                    i++;
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
}
