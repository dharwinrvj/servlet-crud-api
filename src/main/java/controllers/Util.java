package controllers;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import static java.util.stream.Collectors.joining;

public class Util {
    public final static String REQUEST_METHOD = null;
    public final static String USER_ID = null;
    public final static String USER_NAME = null;
    public final static String USER_EMAIL = null;
    public final static String USER_COUNTRY = null;

    public static String readInputStream(InputStream stream) {
        return new BufferedReader(new InputStreamReader(stream)).lines().collect(joining("\n"));
    }
}