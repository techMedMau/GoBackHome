package utils;

import java.io.*;
import java.util.ArrayList;

public class Read {
    private String dataPath;
    public Read(String dataPath){
        this.dataPath= getClass().getResource(dataPath).getFile();
    }
    public ArrayList<String> read(){
        ArrayList<String> strings=new ArrayList<>();
        try {
            BufferedReader bufferedReader=new BufferedReader(new FileReader(dataPath));
            String data;
            while (true){
                data=bufferedReader.readLine();
                if (data==null){
                    break;
                }
                strings.add(data);
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strings;
    }
}
