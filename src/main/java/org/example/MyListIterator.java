package org.example;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MyListIterator implements Iterator<String> {
    private List<String> list;
    private int num;

    public MyListIterator(List<String> list) {
        this.list = list;
        num = list.size();
    }

    @Override
    public boolean hasNext() {
        if((num -1) >= 0) {
            return true;
        }
        return false;
    }

    @Override
    public String next() {
        num -= 1;
        return list.get(num);
    }
}
