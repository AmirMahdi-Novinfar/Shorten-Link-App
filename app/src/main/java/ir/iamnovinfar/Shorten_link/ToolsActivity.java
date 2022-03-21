package ir.iamnovinfar.Shorten_link;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;

public class ToolsActivity extends AppCompatActivity {
    TextView shorten_txt_url;
    ImageView copy,share;

    RelativeLayout open_link,generate_qrcode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools);

        SetupViews();
        String link=getIntent().getStringExtra("finaldata");
        shorten_txt_url.setText(link+"");


        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("link", link);
                clipboard.setPrimaryClip(clip);

                Toast.makeText(ToolsActivity.this, "لینک کپی شد...", Toast.LENGTH_SHORT).show();

            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                /*This will be the actual content you wish you share.*/
                intent.setType("text/plain");
                /*Applying information Subject and Body.*/
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "لینک کوتاه شده:");
                intent.putExtra(android.content.Intent.EXTRA_TEXT, link);
                /*Fire!*/
                startActivity(Intent.createChooser(intent, "انتخاب کنید..."));
            }
        });

        open_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(link));
                startActivity(i);
                Toast.makeText(ToolsActivity.this, "as", Toast.LENGTH_SHORT).show();
            }
        });
        Toast.makeText(ToolsActivity.this, "asss", Toast.LENGTH_SHORT).show();

    }


    private void SetupViews(){

        shorten_txt_url=findViewById(R.id.shorten_txt_url);
        copy=findViewById(R.id.btn_copy_clip);
        share=findViewById(R.id.btn_share);
        open_link=findViewById(R.id.open_link2);
        generate_qrcode=findViewById(R.id.generate_qrcode2);





    }
}