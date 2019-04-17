package com.alv.alframe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Utils {

    public static boolean debug = true;

    public static void tell(String str){
        if (debug)
            System.out.println(str);
    }

    public static String readResource(Class clazz, String res) {
        InputStream is2 = clazz.getResourceAsStream(res);
        if (is2 != null) {
            InputStreamReader isr2 = new InputStreamReader(is2);
            BufferedReader br2 = new BufferedReader(isr2);
            StringBuilder sb = new StringBuilder();
            String line;

            try {
                while ((line = br2.readLine()) != null) {
                    sb.append(line + "\n");
                }
                br2.close();
                isr2.close();
                is2.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return sb.toString();
        }
        return null;
    }
}
