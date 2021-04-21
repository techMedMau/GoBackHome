package gameobj;


import controllers.ImageController;
import utils.Global;
import java.awt.*;
import java.util.ArrayList;

public class TalkFrame extends GameObject{
    private ArrayList<Message> messages;
    private int size;
    private static final int HEAD_H=32;
    private static final int GAP=5;
    private int height;

    public TalkFrame(int x, int y, int size) {
        super(x, y, 260, 404);
        this.size=size;
        messages=new ArrayList<>();

    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.lightGray);
        g.fillRect(left(),top(), painter().width(), painter().height());
        if (messages.size()==0){return;}
        Font fn = new Font(Global.FONT, Font.PLAIN, size);
        g.setFont(fn);
        g.setColor(Color.BLACK);
        height=g.getFontMetrics().getAscent();//句子高
        int count=0;//count是到目前為止的句數
        for (int i=0;i<messages.size();i++){
            g.drawImage(ImageController.getInstance().tryGet(messages.get(i).header),painter().left()+GAP,
                    painter().top()+i*HEAD_H+count*height+GAP*(i+count+1),null );
            for (int k=0;k<messages.get(i).strings.size();k++){
                g.drawString(messages.get(i).strings.get(k), painter().left()+10,
                        painter().top()+(i+1)*HEAD_H+(count+1)*height+GAP*(i+count+++1));
            }
        }

    }

    @Override
    public void update() {
        int count=0;
        for (int i=0;i<messages.size();i++){
            for (int k=0;k<messages.get(i).strings.size();k++){
                count++;
            }
        }
        if (painter().top()+messages.size()*HEAD_H+(count+1)*height+GAP*(messages.size()+count)>painter().bottom()){
            painter().setBottom(painter().top()+messages.size()*HEAD_H+(count+1)*height+GAP*(messages.size()+count));
            collider().setBottom(painter().top()+messages.size()*HEAD_H+(count+1)*height+GAP*(messages.size()+count));
        }

    }
    public void getMessage(String header,ArrayList<String> strings){
        messages.add(new Message(header,strings));
    }
    public static class Message{
        private String header;
        private ArrayList<String> strings;
        public Message(String header,ArrayList<String> strings){
            this.header=header;
            this.strings=strings;
        }

    }
}
