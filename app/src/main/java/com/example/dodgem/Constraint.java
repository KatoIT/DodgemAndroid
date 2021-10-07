package com.example.dodgem;

public interface Constraint {
    boolean IS_PLAYER_RUN = false;
    int COLOR_OF_PLAYER = -1;
    int WHITE = 1;
    int BLACK = -1;
    int DEPTH_MAX = 10;
    int[] BLACK_RUN = {1, 3, -3};
    int[] WHITE_RUN = {-3, -1, 1};
    int[] BOARD = {-1, 0, 0, -1, 0, 0, 0, 1, 1};
    int[] VALUE_OF_WHITE = {30, 35, 40, 15, 20, 25, 0, 5, 10};
    int[] VALUE_OF_BLACK = {-10, -25, -40, -5, -20, -35, 0, -15, -30};
}
