package utils;

import scene.TalkRoomScene;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Write {
    private String dataPath;
    public Write(String dataPath){
        this.dataPath=new String();
        this.dataPath= getClass().getResource(dataPath).getFile();
    }
    public void write(ArrayList<String> strings){
        try {
            BufferedWriter bufferedWriter=new BufferedWriter(new FileWriter(dataPath,true));
            for (int i=0;i<strings.size();i++){
                bufferedWriter.write(strings.get(i));
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
