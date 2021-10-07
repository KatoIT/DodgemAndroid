package com.example.dodgem;



public class Minimax {
    public int minVal(Node node, int depth) {
        if (depth == 0 || node.isNodeEnd()) {
            return node.getEvaluation();
        } else {
            int evalMin = Integer.MAX_VALUE;
            for (Node node2 : node.getNextNodes()) {
                int t = maxVal(node2, depth - 1);
                if (evalMin > t) {
                    evalMin = t;
                }
            }
            return evalMin;
        }
    }

    public int maxVal(Node node, int depth) {
        if (depth == 0 || node.isNodeEnd()) {
            return node.getEvaluation();
        } else {
            int evalMax = Integer.MIN_VALUE;
            for (Node node2 : node.getNextNodes()) {
                int t = minVal(node2, depth - 1);
                if (evalMax < t) {
                    evalMax = t;
                }
            }
            return evalMax;
        }
    }


    public Node MiniMaxVal(Node node, int depth) {
        Node chooseNode = null;
        int val = Integer.MIN_VALUE;
        for (Node node2 : node.getNextNodes()) {
            int t = minVal(node2, depth - 1);
            if (val < t) {
                val = t;
                chooseNode = node2;
            }
        }
        return chooseNode;
    }
}
