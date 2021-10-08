package com.example.dodgem;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    Button buttonPlayGame, buttonPlayGame2, buttonLuatChoi;
    TextView textViewColor;
    ImageView imageViewBlack, imageViewWhite;
    Spinner spinnerDepth;
    int isWhiteOfPlayer = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Ánh xạ
        mapping();
        // Set
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            arrayList.add("Độ khó " + i);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_spinner, arrayList);
        spinnerDepth.setAdapter(adapter);

        // click
        imageViewBlack.setOnClickListener(view -> {
            isWhiteOfPlayer = -1;
            textViewColor.setText("Bạn chọn quân Đen");
            imageViewBlack.setBackgroundResource(R.drawable.square);
            imageViewWhite.setBackgroundResource(R.drawable.square_none);
        });
        imageViewWhite.setOnClickListener(view -> {
            isWhiteOfPlayer = 1;
            textViewColor.setText("Bạn chọn quân Trắng");
            imageViewBlack.setBackgroundResource(R.drawable.square_none);
            imageViewWhite.setBackgroundResource(R.drawable.square);
        });
        buttonPlayGame.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, PlayGameActivity.class);
            intent.putExtra("playBot", true);
            intent.putExtra("depth", spinnerDepth.getSelectedItemPosition() + 1);
            intent.putExtra("chess", isWhiteOfPlayer);
            startActivity(intent);
        });
        buttonPlayGame2.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, PlayGameActivity.class);
            intent.putExtra("playBot", false);
            startActivity(intent);
        });
        buttonLuatChoi.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            // set Message là phương thức thiết lập câu thông báo
            builder.setMessage("Quân đen có thể đi tới ô trống bên phải, ở trên hoặc ở dưới\n" +
                    "Quân trắng có thể đi tới ô trống bên trái, bên phải, ở trên\n" +
                    "Quân đen nếu ở cột ngoài cùng bên phải có thể đi ra ngoài bàn cờ\n" +
                    "Quân trắng nếu ở hàng trên cùng có thể đi ra khỏi bàn cờ\n" +
                    "Ai đưa cả hai quân của mình ra khỏi bàn cờ trước sẽ thắng, hoặc tạo ra tình huống mà đối phương không đi được cũng sẽ thắng\nQuân trắng được ưu tiên đi trước")
                    .setPositiveButton("Tôi đã hiểu", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            builder.create().cancel();
                        }
                    });
            // tạo dialog và hiển thị
            builder.create().show();
        });
    }

    public void mapping() {
        textViewColor = findViewById(R.id.textViewColor);
        buttonLuatChoi = findViewById(R.id.buttonLuatChoi);
        buttonPlayGame = findViewById(R.id.buttonPlayGame);
        buttonPlayGame2 = findViewById(R.id.buttonPlayGame2);
        spinnerDepth = findViewById(R.id.spinnerDepth);
        imageViewBlack = findViewById(R.id.imageViewBlack);
        imageViewWhite = findViewById(R.id.imageViewWhite);
    }
}