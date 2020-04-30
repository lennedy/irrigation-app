package com.example.irrigation2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imgView = (ImageView) findViewById(R.id.imageView8);
        imgView.setImageResource(R.drawable.circvermelho);

        TextView t = (TextView) findViewById(R.id.textView);
        t.setText("    Gramado da Frente");

        TextView t2 = (TextView) findViewById(R.id.textView5);
        t2.setText("    Canteiros da Frente");

        TextView t3 = (TextView) findViewById(R.id.textView4);
        t3.setText("    Canteiros do Lado");
        DownloadJsonAsyncTask d = new DownloadJsonAsyncTask();
        d.execute("Teste");
    }

    public void clickButtom(View view){
        ImageView imgView = (ImageView) findViewById(R.id.imageView8);
        imgView.setImageResource(R.drawable.circverde);
    }
}
