package com.moura.movelistbyrate;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


import java.util.List;


public class MoveArrayAdapter extends ArrayAdapter<Move> {

    private List<Move> filmes;
    public MoveArrayAdapter(Context context, List<Move> filmes){
        super(context,-1,filmes);
        this.filmes = filmes;
    }
    @Override
    public int getCount(){
        return filmes.size();
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Move filmeDaVez = filmes.get(position);

       MoveViewHolder mvh = null;
        if (convertView == null){
            LayoutInflater inflater =
                    LayoutInflater.from(getContext());
            convertView = inflater.inflate(
                    R.layout.list_iten,
                    parent,
                    false
            );
            mvh = new MoveViewHolder();

            mvh.titleTextView =
                    convertView.findViewById (R.id.tituleTextView);
            mvh.overviewTextView =
                    convertView.findViewById(R.id.overviewTextView);
            mvh.moveImageView =
                    convertView.findViewById(R.id.moveImageView);

            convertView.setTag(mvh);
        }
        mvh = (MoveViewHolder) convertView.getTag();


        mvh.titleTextView.setText(
                getContext().getString(
                        R.string.title_move,
                        filmeDaVez.title
                )
        );

        mvh.overviewTextView.setText(
                getContext().getString(
                        R.string.overview_move,
                        filmeDaVez.overview
                )
        );

        new Thread (
                new DownloadImagem (mvh, filmeDaVez, getContext())
        ).start();
        return convertView;
    }
}
class ConfiguraImagem implements  Runnable{

    private MoveViewHolder mvh;
    private Bitmap imagem;

    public ConfiguraImagem (MoveViewHolder mvh, Bitmap imagem){
        this.mvh = mvh;
        this.imagem = imagem;
    }
    @Override
    public void run() {
        mvh.moveImageView.setImageBitmap(imagem);
    }
}
class DownloadImagem implements Runnable{
    private MoveViewHolder mvh;
    private Move filmeDaVez;
    private Context context;

    public DownloadImagem (MoveViewHolder mvh, Move filmeDaVez, Context context){
        this.mvh = mvh;
        this.filmeDaVez = filmeDaVez;
        this.context = context;

    }
    @Override
    public void run() {
        try{
            URL imagemURL = new URL (
                    filmeDaVez.iconUrl
            );
            HttpURLConnection conn =
                    (HttpURLConnection)imagemURL.openConnection();

            InputStream is = conn.getInputStream();
            Bitmap imagem =
                    BitmapFactory.decodeStream(is);
            ((Activity)context).
                    runOnUiThread(new ConfiguraImagem(mvh, imagem));

        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

}
