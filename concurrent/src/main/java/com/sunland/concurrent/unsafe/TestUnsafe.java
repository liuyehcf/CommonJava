package com.sunland.concurrent.unsafe;

import sun.misc.Unsafe;

import static sun.misc.Unsafe.getUnsafe;

/**
 * Created by liuye on 2017/4/8 0008.
 */
public class TestUnsafe {
    public static void main(String[] args){
        Unsafe unsafe=getUnsafe();
    }
}
