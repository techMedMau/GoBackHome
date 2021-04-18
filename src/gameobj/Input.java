package gameobj;

import utils.Delay;
import utils.Global;

import java.awt.*;


public class Input extends GameObject{
    private InputLine inputLine;
    private Delay delay;
    private String num;
    private int size;
    private int inputChange;
    public Input(int x, int y, int size,int lineHeight,int inputChange) {
        super(x, y, lineHeight, lineHeight);
        num=new String();
        inputLine = new InputLine(painter().left(), painter().top(), lineHeight);//275
        this.size=size;
        this.inputChange=inputChange;
        delay=new Delay(3);
    }

    @Override
    public void paintComponent(Graphics g) {
        inputLine.paint(g);
        Font fn = new Font(Global.FONT,Font.PLAIN,size);
        g.setFont(fn);
        g.drawString(num,painter().left(),painter().bottom());
    }

    @Override
    public void update() {

    }
    public int length(){
        return num.length();
    }
    public void substring(){
        delay.loop();
        if (delay.count()){
            num=num.substring(0,num.length()-1);
            inputLine.translateX(-inputChange);
        }
    }
    public void add(char c){
        if (Character.isDigit(c)&&num.length()<20){
            num+=c;
            inputLine.translateX(inputChange);
        }
    }
    public String getNum(){
        return num;
    }
}
