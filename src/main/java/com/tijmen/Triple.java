package com.tijmen;

public class Triple<A, B, C> {
    private A left;
    private B middle;
    private C right;

    public Triple(A left, B middle, C right) {
        this.left = left;
        this.middle = middle;
        this.right = right;
    }

    public A getLeft() {
        return left;
    }

    public B getMiddle() {
        return middle;
    }

    public C getRight() {
        return right;
    }
}
