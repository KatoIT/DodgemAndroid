package com.example.dodgem;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Stack;

public class PlayGameActivity extends AppCompatActivity {

    Stack<int[]> boardUndo = new Stack<>();
    ArrayList<ImageView> imageViews = new ArrayList<>();
    ArrayList<Integer> validate = new ArrayList<>();
    TextView textViewDepth;
    ImageView imageView1, imageView2, imageView3, imageView4, imageView5, imageView6, imageView7, imageView8, imageView9;
    Button buttonOut, buttonUndo, buttonNew;
    Intent intent;
    int[] board = Constraint.BOARD.clone();
    int[] boardUndoNext = Constraint.BOARD.clone();
    int depth = 1;
    int selected = -1;
    int isWhiteOfPlayer = 1;
    boolean playBot = true;
    boolean isPlayerRun = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);
        // Ánh xạ
        this.mapping();
        // Nhận dữ liệu setting
        intent = this.getIntent();
        playBot = intent.getBooleanExtra("playBot", true);
        if (playBot) {
            isWhiteOfPlayer = intent.getIntExtra("chess", -1);
            depth = intent.getIntExtra("depth", 5);
            textViewDepth.setText("Độ khó " + depth);
            if (isWhiteOfPlayer == -1) {
                isPlayerRun = false;
                MachineMove();
            }
        }else {
            textViewDepth.setText("");
        }
        // Lùì lại 1 bước
        buttonUndo.setOnClickListener(view -> {
            board = boardUndo.pop();
            boardUndoNext = board.clone();
            this.Draw();
            if (boardUndo.empty()) {
                buttonUndo.setVisibility(View.INVISIBLE);
                buttonNew.setVisibility(View.INVISIBLE);
            }
        });
        // Chơi ván mới
        buttonNew.setOnClickListener(view -> {
            newGame();
        });
        // Di chuyển quân ra khỉ bàn cờ
        buttonOut.setOnClickListener(view -> {
            board[selected] = 0;
            if (playBot) {
                isPlayerRun = !isPlayerRun;
            }
            this.Draw();
            buttonOut.setVisibility(View.INVISIBLE);
            Node node = new Node(board.clone(), depth, isWhiteOfPlayer != 1, depth);
            if (node.isNodeEnd()) {
//                Log.e("Info", "End in Move!");
                showEnd("You");
            } else {
                if (!isPlayerRun && playBot) {
//                    Log.e("Info", "Machine run!");
                    MachineMove();
                } else {
                    boardUndo.add(boardUndoNext);
                    boardUndoNext = board.clone();
                    isWhiteOfPlayer = -1 * isWhiteOfPlayer;
                }
            }
        });


    }

    public void newGame(){
        if (playBot) {
            isWhiteOfPlayer = intent.getIntExtra("chess", -1);
            depth = intent.getIntExtra("depth", 5);
            if (isWhiteOfPlayer == 1) {
                isPlayerRun = true;
            }else {
                isPlayerRun = false;
            }
        }else {
            isWhiteOfPlayer = 1;
        }
        board = Constraint.BOARD.clone();
        boardUndoNext = board.clone();
        boardUndo.clear();
        buttonUndo.setVisibility(View.INVISIBLE);
        buttonNew.setVisibility(View.INVISIBLE);
        this.Draw();
        if (!isPlayerRun && playBot) {
            MachineMove();
        }
    }
    // Lấy vị trí ô cờ được chọn
    public void getLocationImageView(View view) {
        this.Select(Integer.parseInt(view.getTag().toString()));
    }

    // Highlight
    public void Select(int selected_) {
        this.Draw2();
        if (board[selected_] != 0) {
            selected = selected_;
            if (playBot) { // ng vs bot
                if (isPlayerRun && isWhiteOfPlayer == board[selected_]) {
                    Highlight(selected_);
                }
            } else { // ng vs ng
                if (isWhiteOfPlayer == board[selected_]) {
                    Highlight(selected_);
                }
            }
        } else {
            move(selected_);

        }
    }

    public void Highlight(int selected_) {
        // Highlight quân cờ được chọn
        imageViews.get(selected).setBackgroundResource(R.drawable.square);
        validate.clear();
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

    public void move(int selected_) {
        // di chuyển quân cờ
        if (board[selected_] == 0 && validate.contains(selected_)) {
            boardUndo.add(boardUndoNext); // Thêm vào Stack Undo
            board[selected_] = board[selected]; // quân đc chọn di chuyển
            board[selected] = 0; // quân đc chọn trống
            selected = -1; // xóa quân cờ được chọn
            validate.clear(); // loại bỏ list ô có thể đi
            if (playBot){
                isPlayerRun = !isPlayerRun; // Dảo lượt chơi
            }
            buttonUndo.setVisibility(View.VISIBLE); // Hiện nút Undo
            buttonNew.setVisibility(View.VISIBLE); // Hiện nút New game
            buttonOut.setVisibility(View.INVISIBLE); // Ản nút OUT
            this.Draw();  // Vẽ lại bàn cờ
            Node node = new Node(board.clone(), depth, isWhiteOfPlayer != 1, depth);
            if (node.isNodeEnd()) {
//                Log.e("Info", "End in Move!");
                showEnd("You");
            } else {
                if (playBot) {
//                    Log.e("Info", "Machine run!");
                    MachineMove();
                } else {
                    boardUndoNext = board.clone();
                    isWhiteOfPlayer = -1 * isWhiteOfPlayer;
                }
            }
        }
    }

    // Vẽ bàn cờ
    public void Draw() {
        this.Draw2();
        for (int i = 0; i < 9; i++) {
            if (board[i] == -1) {
                imageViews.get(i).setImageResource(R.drawable.chess_piece_black);
            } else {
                if (board[i] == 1) {
                    imageViews.get(i).setImageResource(R.drawable.chess_piece_white);
                } else {
                    imageViews.get(i).setImageResource(R.drawable.square_transparent);
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
        if(selected > 5 && move_ == 3) {
            return false;
        }
        return true;
    }

    // Lượt máy di chuyển
    public void MachineMove() {
        Node node = new Node(board.clone(), 0, isWhiteOfPlayer != 1, depth);
        node = new Minimax().MiniMaxVal(node, depth, (isWhiteOfPlayer != 1));
        board = node.getBoard().clone();
        boardUndoNext = board.clone();
        isPlayerRun = !isPlayerRun;
        if (Constraint.DEPTH_MAX <= 5) {  // delay 0.3 giây
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                Draw();
            }, 300);
        } else {
            this.Draw();
        }
        node = new Node(board.clone(), depth, isWhiteOfPlayer == 1, depth);
        if (node.isNodeEnd()) {
            showEnd("Bot");
//            Log.e("Info", "End in MachineMove!");
        }else {
//            Log.e("Info", "Player run!");
        }
    }

    public void showEnd(String isPlayerWin) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // set Message là phương thức thiết lập câu thông báo
        builder.setMessage(isPlayerWin + " Win")
                // positiveButton là nút thuận : đặt là OK
                .setPositiveButton("Ván mới", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        newGame();
                    }
                })
                // ngược lại negative là nút nghịch : đặt là cancel
                .setNegativeButton("Thoát", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        finish();
                    }
                });
        // tạo dialog và hiển thị
        builder.create().show();
    }

    public void mapping() {
        // Ánh xạ
        buttonOut = findViewById(R.id.buttonOut);
        textViewDepth = findViewById(R.id.textViewDepth);
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
        this.Draw();
    }

}