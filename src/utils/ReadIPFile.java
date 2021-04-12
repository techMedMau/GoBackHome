package utils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ReadIPFile {

    private static FileWriter fw;
    private static FileReader fr;
    private ReadIPFile(){
    }
    private static FileWriter getFW(){
        if (fw==null){
            try {
                fw= new FileWriter(Global.FILE_PATH,true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fw;
    }
    private static FileReader getFR(){
        if (fr==null){
            try {
                fr=new FileReader(Global.FILE_PATH);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return fr;
    }
    public static void write(String str){
        try {
            getFW().write(str);
            getFW().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
