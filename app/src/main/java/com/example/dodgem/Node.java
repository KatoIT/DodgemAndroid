package com.example.dodgem;


import android.util.Log;

import java.util.ArrayList;

public class Node {
    private ArrayList<Node> nextNodes;
    private int[] board;
    private int depth;
    private boolean whiteRun;

    /*
     * Hàm constructor
     */
    public Node(int[] board, int depth, boolean whiteRun, int depth_max) {
        super();
        this.board = board;
        this.depth = depth;
        this.whiteRun = whiteRun;
        this.nextNodes = new ArrayList<>();
        if (depth <= depth_max && !this.is_node_end_2()) {
            for (int selected = 0; selected < 9; selected++) {
                if (this.board[selected] == 0) continue; // Nếu là ô trống thì bỏ qua.
                if (this.whiteRun) {
                    if (this.board[selected] == Constraint.WHITE) { // Quân Trắng
                        for (int run : Constraint.WHITE_RUN) {
                            if (this.checkMove(selected, run)) {  // Kiểm tra nước có thể di chuyển
                                int[] varBoard = this.board.clone();
                                if (selected + run >= 0) { // Quân trắng không ở hàng trên cùng và đi lên
                                    varBoard[selected + run] = varBoard[selected];
                                }
                                varBoard[selected] = 0;
                                this.nextNodes.add(new Node(varBoard, this.depth + 1, !this.whiteRun, depth_max));
                            }
                        }
                    }
                } else {
                    if (this.board[selected] == Constraint.BLACK) { // Quân Đen
                        for (int run : Constraint.BLACK_RUN) {
                            if (this.checkMove(selected, run)) {  // Kiểm tra nước có thể di chuyển
                                int[] varBoard = this.board.clone();
                                if (!(selected % 3 == 2 && run == 1)) { // Quân Đen không ở hàng ngoài cùng bên Phải và đi sang Phải
                                    varBoard[selected + run] = varBoard[selected];
                                }
                                varBoard[selected] = 0;
                                this.nextNodes.add(new Node(varBoard, this.depth + 1, !this.whiteRun, depth_max));
                            }
                        }
                    }
                }
            }
        }
    }

    public void getNode(){
        String txt = "";
        for (int i : this.board
             ) {
            txt += i + ", ";
        }
        txt += "Depth: " + this.depth + ", " + this.whiteRun;
        Log.e("Node", txt);
    }

    /*
     * kiểm tra nước đi đó có đi được không
     */
    public Boolean checkMove(int selected, int move_) {
        if (selected % 3 == 0 && move_ == -1) {
            return false;
        }
        if (selected % 3 == 2 && move_ == 1) {
            return board[selected] == Constraint.BLACK;
        }
        if (selected < 3 && move_ == -3) {
            return board[selected] == Constraint.WHITE;
        }
        if (selected > 5 && move_ == 3) {
            return false;
        }
        return board[selected + move_] == 0;
    }

    public ArrayList<Node> getNextNodes() {
        return nextNodes;
    }

    public int[] getBoard() {
        return board;
    }

    /*
     * Nếu trên trên bàn chỉ còn 1 màu cờ hoặc đến lượt đi nhưng không còn nước đi => END
     */
    public boolean isNodeEnd() {
        return this.nextNodes.size() == 0;
    }

    public boolean is_node_end_2() {
        boolean blackEmpty = true;
        boolean whiteEmpty = true;
        for (int i : this.board) {
            if (i == Constraint.BLACK) {
                blackEmpty = false;
            } else if (i == Constraint.WHITE) {
                whiteEmpty = false;
            }
        }
        return blackEmpty || whiteEmpty;
    }

    /*
     * Vị trí quân cờ ứng với môi giá trị trong Constraint.VALUE_OF_WHITE và Constraint.VALUE_OF_BLACK
     * Quân Trắng: (2 - số quân Trắng còn trên bàn cờ) * 85
     * Quân Đen: (2 - số quân Đen còn trên bàn cờ) * (-85)
     * Là trạng thái cuối:
     *      Khi đến lượt quân Đen:  + 85
     *      Khi đến lượt quân Trắng:  - 85
     */
    public int getEvaluation() {
        int eval = 0;
        int numberBlack = 2;
        int numberWhite = 2;
        for (int i = 0; i < 9; i++) {
            if (this.board[i] == Constraint.BLACK) {  // Quân Đen
                numberBlack--;
                eval += Constraint.VALUE_OF_BLACK[i];
                if (i < 6) {
                    if (this.board[i + 3] == Constraint.WHITE) {
                        eval -= 40;
                    }
                    if (i < 3 && this.board[i + 6] == Constraint.WHITE) {
                        eval -= 30;
                    }
                }
            } else {
                if (this.board[i] == Constraint.WHITE) {  // Quân Trắng
                    numberWhite--;
                    eval += Constraint.VALUE_OF_WHITE[i];
                    if (i % 3 != 0) {
                        if (this.board[i - 1] == Constraint.BLACK) {
                            eval += 40;
                        }
                        if (i % 3 == 2 && this.board[i - 2] == Constraint.BLACK) {
                            eval += 30;
                        }
                    }
                }
            }
        }
        eval += (numberWhite - numberBlack) * 85;
        if (this.isNodeEnd() && !is_node_end_2()) {
            if (this.whiteRun) {
                eval -= 85;
            } else {
                eval += 85;
            }
        }
        return eval;
    }


}
