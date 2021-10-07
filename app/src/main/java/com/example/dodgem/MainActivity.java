package com.example.dodgem;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    Stack<int[]> boardUndo = new Stack<>();
    ArrayList<ImageView> imageViews = new ArrayList<>();
    ArrayList<Integer> validate = new ArrayList<>();
    ImageView imageView1, imageView2, imageView3, imageView4, imageView5, imageView6, imageView7, imageView8, imageView9;
    Button buttonOut, buttonUndo, buttonNew;
    int[] board = Constraint.BOARD.clone();
    int[] boardUndoNext = Constraint.BOARD.clone();
    int selected = -1;
    boolean isPlayerRun = Constraint.IS_PLAYER_RUN;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuSetting:
                openSetting();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Ánh xạ
        buttonOut = findViewById(R.id.buttonOut);
        buttonUndo = findViewById(R.id.buttonUndo);
        buttonNew = findViewById(R.id.buttonNew);
        imageView1 = findViewById(R.id.imageView1);
        imageView2 = findViewById(R.id.imageView2);
        imageView3 = findViewById(R.id.imageView3);
        imageView4 = findViewById(R.id.imageView4);
        imageView5 = findViewById(R.id.imageView5);
        imageView6 = findViewById(R.id.imageView6);
        imageView7 = findViewById(R.id.imageView7);
        imageView8 = findViewById(R.id.imageView8);
        imageView9 = findViewById(R.id.imageView9);

        imageViews.add(imageView1);
        imageViews.add(imageView2);
        imageViews.add(imageView3);
        imageViews.add(imageView4);
        imageViews.add(imageView5);
        imageViews.add(imageView6);
        imageViews.add(imageView7);
        imageViews.add(imageView8);
        imageViews.add(imageView9);

        // Vẽ bàn cờ
        this.Draw();
        // click
        imageView1.setOnClickListener(view -> {
            this.Select(0);
        });
        imageView2.setOnClickListener(view -> {
            this.Select(1);
        });
        imageView3.setOnClickListener(view -> {
            this.Select(2);
        });
        imageView4.setOnClickListener(view -> {
            this.Select(3);
        });
        imageView5.setOnClickListener(view -> {
            this.Select(4);
        });
        imageView6.setOnClickListener(view -> {
            this.Select(5);
        });
        imageView7.setOnClickListener(view -> {
            this.Select(6);
        });
        imageView8.setOnClickListener(view -> {
            this.Select(7);
        });
        imageView9.setOnClickListener(view -> {
            this.Select(8);
        });
        buttonUndo.setOnClickListener(view -> {
            board = boardUndo.pop();
            boardUndoNext = board.clone();
            this.Draw();
            if (boardUndo.empty()) {
                buttonUndo.setVisibility(View.INVISIBLE);
                buttonNew.setVisibility(View.INVISIBLE);
            }
        });
        buttonNew.setOnClickListener(view -> {
            isPlayerRun = Constraint.IS_PLAYER_RUN;
            board = Constraint.BOARD.clone();
            boardUndoNext = board.clone();
            boardUndo.clear();
            buttonUndo.setVisibility(View.INVISIBLE);
            buttonNew.setVisibility(View.INVISIBLE);
            this.Draw();
            if (!isPlayerRun) {
                MachineMove();
            }
        });
        buttonOut.setOnClickListener(view -> {
            board[selected] = 0;
            this.Draw();
            buttonOut.setVisibility(View.INVISIBLE);
            MachineMove();
        });
        if (!isPlayerRun) {
            MachineMove();
        }

    }

    public void Highlight() {
        imageViews.get(selected).setBackgroundResource(R.drawable.square);
    }

    // Chọn quân cờ Highlight,
    public void Select(int selected_) {
        this.Draw2();
        if (board[selected_] != 0 && board[selected_] == Constraint.COLOR_OF_PLAYER) {
            selected = selected_;
            if (isPlayerRun && Constraint.COLOR_OF_PLAYER == board[selected_]) {
                // Highlight quân cờ được chọn
                imageViews.get(selected).setBackgroundResource(R.drawable.square);
                // Highlight nước có thể di chuyển
                if (board[selected_] == Constraint.WHITE) { // Quân trắng được chọn
                    for (int i : Constraint.WHITE_RUN) {
                        if (this.checkMove(i)) {
                            int var = selected_ + i;
                            if (board[var] == 0) {
                                validate.add(var);
                                imageViews.get(var).setBackgroundResource(R.drawable.square_next);
                            }
                        }
                    }
                } else {
                    if (board[selected_] == Constraint.BLACK) { // Quân đen được chọn
                        for (int i : Constraint.BLACK_RUN) {
                            if (this.checkMove(i)) {
                                int var = selected_ + i;
                                if (board[var] == 0) {
                                    validate.add(var);
                                    imageViews.get(var).setBackgroundResource(R.drawable.square_next);
                                }
                            }
                        }
                    }

                }
            }

        } else {
            // di chuyển quân cờ
            if (isPlayerRun && board[selected_] == 0 && validate.contains(selected_)) {
                boardUndo.add(boardUndoNext);
                board[selected_] = board[selected];
                board[selected] = 0;
                selected = -1;
                validate.clear();
                isPlayerRun = false;
                buttonUndo.setVisibility(View.VISIBLE);
                buttonNew.setVisibility(View.VISIBLE);
                buttonOut.setVisibility(View.INVISIBLE);
                this.Draw();
                Node node = new Node(board.clone(), 0, true);
                if (node.isNodeEnd()) {
                    showEnd();
                } else {
                    MachineMove();
                }
            }
        }
    }

    // Vẽ bàn cờ
    public void Draw() {
        this.Draw2();
        for (int i = 0; i < 9; i++) {
            if (board[i] == -1) {
                imageViews.get(i).setImageResource(R.drawable.black_chess_piece);
            } else {
                if (board[i] == 1) {
                    imageViews.get(i).setImageResource(R.drawable.white_chess_piece);
                } else {
                    imageViews.get(i).setImageResource(R.drawable.square_none);
                }
            }
        }
    }

    // Xóa Highlight ô đề cử
    public void Draw2() {
        for (int i = 0; i < 9; i++) {
            imageViews.get(i).setBackgroundResource(R.drawable.square_none);
        }
    }

    // Kiểm tra nước có thể đi
    public Boolean checkMove(int move_) {
        if (selected % 3 == 0 && move_ == -1) {
            return false;
        }
        if (selected % 3 == 2 && move_ == 1) {
            if (board[selected] == Constraint.BLACK) {
                buttonOut.setVisibility(View.VISIBLE);
            }
            return false;
        }
        if (selected < 3 && move_ == -3) {
            if (board[selected] == Constraint.WHITE) {
                buttonOut.setVisibility(View.VISIBLE);
            }
            return false;
        }
        if (selected > 5 && move_ == 3) {
            return false;
        }
        return true;
    }

    // Lượt máy di chuyển
    public void MachineMove() {
        Minimax minimax = new Minimax();
        Node node = new Node(board.clone(), 0, true);
        node = minimax.MiniMaxVal(node, Constraint.DEPTH_MAX);
        board = node.getBoard().clone();
        boardUndoNext = board.clone();
//        Toast.makeText(MainActivity.this, board[0] + ", " + board[1] + ", " + board[2] + ", " + board[3] + ", " + board[4] + ", " + board[5] + ", " + board[6] + ", " + board[7] + ", " + board[8] + ", ", Toast.LENGTH_LONG).show();
        isPlayerRun = true;
        if (Constraint.DEPTH_MAX <= 5) {  // delay 0.3 giây
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                Draw();
            }, 300);
        } else {
            this.Draw();
        }
        if (node.isNodeEnd()) {
            showEnd();
        }
    }

    public void showEnd() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // set Message là phương thức thiết lập câu thông báo
        builder.setMessage("KatoIT!!!")
                // positiveButton là nút thuận : đặt là OK
                .setPositiveButton("New Game", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        isPlayerRun = Constraint.IS_PLAYER_RUN;
                        board = Constraint.BOARD.clone();
                        boardUndoNext = board.clone();
                        boardUndo.clear();
                        buttonUndo.setVisibility(View.INVISIBLE);
                        buttonNew.setVisibility(View.INVISIBLE);
                        Draw();
                        if (!isPlayerRun) {
                            MachineMove();
                        }
                    }
                })
                // ngược lại negative là nút nghịch : đặt là cancel
                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        finish();
                    }
                });
        // tạo dialog và hiển thị
        builder.create().show();
    }

    public void openSetting() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View alert = LayoutInflater.from(this).inflate(R.layout.activity_dialog_setting, null);
        builder.setView(alert);
        AlertDialog dialog = builder.create();
        ImageView imageViewBlack, imageViewWhite;
        Button buttonExit, buttonSave;
        imageViewBlack = alert.findViewById(R.id.imageViewBlack);
        imageViewWhite = alert.findViewById(R.id.imageViewWhite);
        buttonExit = alert.findViewById(R.id.buttonExit);
        buttonSave = alert.findViewById(R.id.buttonSave);
        imageViewBlack.setOnClickListener(view -> {
            imageViewBlack.setBackgroundResource(R.drawable.square);
            imageViewWhite.setBackgroundResource(R.drawable.square_none);
        });
        imageViewWhite.setOnClickListener(view -> {
            imageViewBlack.setBackgroundResource(R.drawable.square_none);
            imageViewWhite.setBackgroundResource(R.drawable.square);
        });
        buttonExit.setOnClickListener(view -> {
            dialog.cancel();
        });

        dialog.show();
    }

}