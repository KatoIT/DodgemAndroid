package com.example.dodgem;

import java.util.ArrayList;

public class Node {
    private ArrayList<Node> nextNodes;
    private int[] board;
    private int depth;
    private boolean whiteRun;

    /*
     * Hàm constructor sẽ tự tạo ra các con cho đến độ sâu yêu cầu
     */
    public Node(int[] board, int depth, boolean whiteRun) {
        super();
        this.board = board;
        this.depth = depth;
        this.whiteRun = whiteRun;
        //Auto create child node
        this.nextNodes = new ArrayList<>();
        if (this.depth <= Constraint.DEPTH_MAX) {  // kiểm tra độ sâu phù hợp và khác trạng thái cuối
            for (int selected = 0; selected < 9; selected++) {
                if (this.whiteRun) { // lượt quân trắng
                    if (this.board[selected] == Constraint.WHITE) { // Quân Trắng
                        for (int run : Constraint.WHITE_RUN) {
                            if (this.checkMove(selected, run)) {  // Kiểm tra nước có thể di chuyển
                                int[] varBoard = this.board.clone();
                                if (selected > 2) { // Quân trắng không ở hàng trên cùng
                                    varBoard[selected + run] = varBoard[selected];
                                }
                                varBoard[selected] = 0;
                                nextNodes.add(new Node(varBoard, this.depth + 1, !whiteRun));
                            }
                        }
                    }
                } else {
                    if (this.board[selected] == Constraint.BLACK) { // Quân Đen
                        for (int run : Constraint.BLACK_RUN) {
                            if (this.checkMove(selected, run)) {  // Kiểm tra nước có thể di chuyển
                                int[] varBoard = this.board.clone();
                                if (selected % 3 != 2) { // Quân Đen không ở hàng ngoài cùng bên Phải
                                    varBoard[selected + run] = varBoard[selected];
                                }
                                varBoard[selected] = 0;
                                nextNodes.add(new Node(varBoard, this.depth + 1, !whiteRun));
                            }
                        }
                    }
                }
            }
        }
    }


    public ArrayList<Node> getNextNodes() {
        return nextNodes;
    }

    public void setNextNodes(ArrayList<Node> nextNodes) {
        this.nextNodes = nextNodes;
    }

    public int[] getBoard() {
        return board;
    }

    public void setBoard(int[] board) {
        this.board = board;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public boolean isWhiteRun() {
        return whiteRun;
    }

    public void setWhiteRun(boolean whiteRun) {
        this.whiteRun = whiteRun;
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
                        if (i % 3 != 1 && this.board[i - 2] == Constraint.BLACK) {
                            eval += 30;
                        }
                    }
                }
            }
        }
        eval += numberBlack * (-85) + numberWhite * 85;
        if (this.isNodeEnd()) {
            if (this.whiteRun) {
                eval -= 100;
            } else {
                eval += 100;
            }
        }
        return eval;
    }

    /*
     * Nếu trên trên bàn chỉ còn 1 màu cờ hoặc đến lượt đi nhưng không còn nước đi => END
     */
    public boolean isNodeEnd() {
        boolean blackEmpty = true;
        boolean whiteEmpty = true;
        for (int i : this.board) {
            if (i == Constraint.BLACK) {
                blackEmpty = false;
            } else if (i == Constraint.WHITE) {
                whiteEmpty = false;
            }
        }
        if (blackEmpty || whiteEmpty || this.nextNodes.size() == 0) {
            return true;
        } else {
            return false;
        }
    }

    /*
     * kiểm tra nước đi đó có đi được không
     */
    public Boolean checkMove(int selected, int move_) {
        if (selected % 3 == 0 && move_ == -1) {
            return false;
        }
        if (selected % 3 == 2 && move_ == 1) {
            if (board[selected] == Constraint.BLACK) {
                return true;
            }
            return false;
        }
        if (selected < 3 && move_ == -3) {
            if (board[selected] == Constraint.WHITE) {
                return true;
            }
            return false;
        }
        if (selected > 5 && move_ == 3) {
            return false;
        }
        return board[selected + move_] == 0;
    }

}
