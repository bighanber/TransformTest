package com.example.testmodule;

import android.os.Trace;

public class TraceTag {

    private static final String TAG = "TraceTag";

    public static void i(String name) {
        Trace.beginSection(name);
    }

    public static void o() {
        Trace.endSection();
    }
}
