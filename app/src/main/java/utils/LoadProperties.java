package utils;

import android.content.Context;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by azwady on 10/12/2015.
 */
public class LoadProperties {

    public String getIP (Context context) throws IOException {
        int READ_BLOCK_SIZE = 100;

        FileInputStream inputStream = context.openFileInput("setting.txt");
        InputStreamReader InputRead= new InputStreamReader(inputStream);
        char[] inputBuffer= new char[READ_BLOCK_SIZE];
        String s="";
        int charRead;
        while ((charRead=InputRead.read(inputBuffer))>0) {
            // char to string conversion
            String readstring=String.copyValueOf(inputBuffer,0,charRead);
            s +=readstring;
        }
        InputRead.close();
        System.out.print(s);

        String iptext;
        iptext = s;
        return iptext;
    }
}
