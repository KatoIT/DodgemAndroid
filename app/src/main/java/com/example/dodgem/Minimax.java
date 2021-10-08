package com.example.dodgem;

import java.util.ArrayDeque;
import java.util.Queue;

public class Minimax {
    static int minimax(Node node, int depth, Boolean isMax) {
        if (node.isNodeEnd() || depth == 0) {
            return node.getEvaluation();
        }
        if (isMax) {
            int evalMax = -1000;
            for (Node node2 : node.getNextNodes()) {
                int t = minimax(node2, depth - 1, false);
                evalMax = Math.max(t, evalMax);
            }
            return evalMax;
        } else {
            int evalMin = 1000;
            for (Node node2 : node.getNextNodes()) {
                int t = minimax(node2, depth - 1, true);
                evalMin = Math.min(t, evalMin);
            }
            return evalMin;
        }
    }

    public Node MiniMaxVal(Node node, int depth, boolean isWhite) {
        Node chooseNode = node;
        if (isWhite) {
            int val = -1000;
            for (Node node2 : node.getNextNodes()) {
                int t = minimax(node2, depth - 1, false);
                if (val < t) {
                    val = t;
                    chooseNode = node2;
                }
            }
        } else {
            int val = 1000;
            for (Node node2 : node.getNextNodes()) {
                int t = minimax(node2, depth - 1, true);
                if (val > t) {
                    val = t;
                    chooseNode = node2;
                }
            }
        }
//        Log.e("Hello ", chooseNode.getBoard()[0] + ", "+chooseNode.getBoard()[1] + ", "+chooseNode.getBoard()[2] + ", "+chooseNode.getBoard()[3] + ", "+chooseNode.getBoard()[4] + ", "+chooseNode.getBoard()[5] + ", "+chooseNode.getBoard()[6] + ", "+chooseNode.getBoard()[7] + ", "+chooseNode.getBoard()[8]);
        return chooseNode;
    }
}
