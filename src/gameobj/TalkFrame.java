package gameobj;


import utils.Global;
import java.awt.*;
import java.util.ArrayList;

public class TalkFrame extends GameObject{
    private ArrayList<Message> messages;
    private int size;

    public TalkFrame(int x, int y, int size) {
        super(x, y, 258, 198);
        this.size=size;
        messages=new ArrayList<>();

    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.lightGray);
        g.fillRect(left(),top(),260,404);
        Font fn = new Font(Global.FONT, Font.PLAIN, size);
        g.setFont(fn);
        g.setColor(Color.BLACK);
        for (int i=0;i<messages.size();i++){
            for (int k=0;k<messages.get(i).strings.size();k++){
                g.drawString(messages.get(i).strings.get(k), painter().left()+40, painter().top()+g.getFontMetrics().getAscent()*(1+i+k));
            }
        }

    }

    @Override
    public void update() {

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
