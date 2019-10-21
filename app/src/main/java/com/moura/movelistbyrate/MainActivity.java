package com.moura.movelistbyrate;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView moveListView;
    private List<Move> moveList;
    private MoveArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        moveListView = findViewById(R.id.moveListView);
        moveList = new ArrayList<>();
        adapter = new MoveArrayAdapter(this,moveList);
        moveListView.setAdapter(adapter);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String listafilme = getString(R.string.web_service_url)+getString(R.string.laguagem_context);

                new Thread(
                        () -> {
                            try {
                                URL url = new URL(listafilme);
                                HttpURLConnection conn =
                                        (HttpURLConnection) url.openConnection();
                                InputStream is = conn.getInputStream();
                                InputStreamReader isr =
                                        new InputStreamReader(is);
                                BufferedReader reader =
                                        new BufferedReader(isr);
                                //reader.readLine();
                                String linha = null;
                                StringBuilder s = new StringBuilder("");
                                while ((linha = reader.readLine()) != null) {
                                    s.append(linha);
                                }
                                reader.close();
                                runOnUiThread(()->{
                                    Toast.makeText(
                                            MainActivity.this,
                                            //s.toString(),
                                            getString(R.string.List_att),
                                            Toast.LENGTH_LONG
                                    ).show();
                                });

                                lidaComJSON(s.toString());

                            } catch (IOException e) {
                                e.printStackTrace();
                                Toast.makeText(
                                        MainActivity.this,
                                        getString(R.string.connect_error),
                                        Toast.LENGTH_SHORT
                                ).show();
                            }

                        }
                ).start();
            }
        });
    }
    private void lidaComJSON (String jsonTextual){
        //org.json
        try{
            JSONObject json = new JSONObject(jsonTextual);
            JSONArray list = json.getJSONArray("results");
            for (int i = 0; i < list.length(); i++){
                JSONObject iesimo = list.getJSONObject(i);
                String poster_path = iesimo.getString("poster_path");
                String title = iesimo.getString("title");
                String overview = iesimo.getString("overview");

                Move w =
                        new Move(
                                poster_path,
                                title,
                                overview
                        );

                moveList.add(w);

            }
            runOnUiThread(
                    () -> {
                        adapter.notifyDataSetChanged();
                    }
            );

        }
        catch (JSONException e){
            e.printStackTrace();
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
