package com.ThiagoAlves;

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
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    EditText Text,IP;
    String msg = "";
    String ip = "";
    Socket soc;
    PrintWriter writer;
    Button Ok;
    Thread t;
    List<String> separador;
    List<String> value = new ArrayList<String>();
    List<String> Links;
    List<String> Urls_Links = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    ListView listview;
    int itemPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listview  = (ListView) findViewById(R.id.list);
        Text = (EditText) findViewById(R.id.Text);
        IP = (EditText) findViewById(R.id.text3);
        Ok = (Button) findViewById(R.id.Ok);

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, value);

        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                itemPosition = position;

                // ListView Clicked item value
                String  itemValue    = (String) listview.getItemAtPosition(position);
                Thread EnviarIndexVideo = new Thread(new Runnable() {
                    public void run() {
                        try {
                            Socket soc5 = new Socket(ip, 7878);
                            PrintWriter writer = new PrintWriter(soc5.getOutputStream());
                            writer.write("@CodVIDEO==" + itemPosition);
                            writer.flush();
                            writer.close();
                            soc5.close();
                            Toast.makeText(getApplicationContext(),"Video Solicitado :"+itemPosition , Toast.LENGTH_LONG)
                                    .show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }});
                EnviarIndexVideo.start();

                // Show Alert
                Toast.makeText(getApplicationContext(),"Position :"+itemPosition+"  ListItem : " +itemValue , Toast.LENGTH_LONG)
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
                        try {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //msg = s;
                                    System.out.println(msg);
                                }
                            });
                        }   catch(Exception e){
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
                                    while (true) {
                                        try {
                                            Socket soc2 = new Socket(ip, 7879);
                                            InputStream is = soc2.getInputStream();
                                            InputStreamReader isr = new InputStreamReader(is,"windows-1252");
                                            BufferedReader br = new BufferedReader(isr);
                                            String message = br.readLine();
                                            Log.e("Ue", message);
                                            separador = Arrays.asList(message.split(";"));
                                            for(String elem : separador) {
                                                if (!elem.equals("")) {
                                                    value.add(elem);
                                                }
                                            }

                                           /*message = br.readLine();
                                            Links = Arrays.asList(message.split(";"));
                                            for(String elem : Links) {
                                                if (!elem.equals("")) {
                                                    Urls_Links.add(elem);
                                                }
                                            }
*/

                                            Log.e("Ue", message);
                                            soc2.close();
                                            break;
                                        } catch (Exception e) {
                                        }
                                    }
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            adapter.notifyDataSetChanged();
                                        }
                                    });


                                }
                            });
                            OK2.start();


                            soc.close();
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
