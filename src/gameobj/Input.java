package gameobj;

import utils.Delay;
import utils.Global;

import java.awt.*;
import java.util.ArrayList;


public class Input extends GameObject {
    private InputLine inputLine;
    private Delay delay;
    private String num;
    private int size;
    private boolean pressShift;
    private boolean pressCapsLock;
    private int stringLimit;
    private boolean canNewline;
    private ArrayList<String> strings;

    public Input(int x, int y, int size, int lineHeight, int stringLimit, boolean canNewline) {
        super(x, y, lineHeight, lineHeight);
        num = new String();
        inputLine = new InputLine(painter().left(), painter().top(), lineHeight);//275
        this.size = size;
        delay = new Delay(3);
        this.stringLimit = stringLimit;
        this.canNewline = canNewline;
        strings=new ArrayList<>();
    }

    @Override
    public void paintComponent(Graphics g) {
        Font fn = new Font(Global.FONT, Font.PLAIN, size);
        g.setFont(fn);
        if (g.getFontMetrics().stringWidth(num)>=stringLimit){
            newline();
        }
        inputLine.setString(g.getFontMetrics().stringWidth(num),g.getFontMetrics().getAscent()*strings.size());
        inputLine.paint(g);
        for (int i=0;i<strings.size();i++){
            g.drawString(strings.get(i), painter().left(), painter().bottom()+g.getFontMetrics().getAscent()*i);
        }
        g.drawString(num, painter().left(), painter().bottom()+g.getFontMetrics().getAscent()*strings.size());

    }

    @Override
    public void update() {

    }

    public int length() {
        return num.length();
    }

    public void substring() {
        delay.loop();
        if (delay.count()){
            if (num.length()==0){
                if (strings.size()==0){return;}
                num=strings.get(strings.size()-1);
                strings.remove(strings.size()-1);
            }
            if (num.length()!=0) {
                num = num.substring(0, num.length() - 1);
            }
        }

    }
    public void newline(){
        if (canNewline){
            strings.add(num);
            num = new String();
            return;
        }
        num = num.substring(0, num.length() - 1);

    }

    public void add(char c) {
        if ((c >= 44 && c <= 125) || c==222||c==192 ||c==32 || c==10) {
            if (c==222){
                c='\'';
            }
            if (c==192){
                c='`';
            }

            if (pressShift) {
                switch (c) {
                    case 10:
                        newline();
                        return;
                    case '/':
                        c = '?';
                        break;
                    case '1':
                        c = '!';
                        break;
                    case '2':
                        c = '@';
                        break;
                    case '`':
                        c = '~';
                        break;
                    case '3':
                        c = '#';
                        break;
                    case '4':
                        c = '$';
                        break;
                    case '5':
                        c = '%';
                        break;
                    case '6':
                        c = '^';
                        break;
                    case '7':
                        c = '&';
                        break;
                    case '8':
                        c = '*';
                        break;
                    case '9':
                        c = '(';
                        break;
                    case '0':
                        c = ')';
                        break;
                    case '=':
                        c = '+';
                        break;
                    case ';':
                        c = ':';
                        break;
                    case '\\':
                        c = '|';
                        break;
                    case ']':
                        c = '}';
                        break;
                    case '[':
                        c = '{';
                        break;
                    case '.':
                        c = '>';
                        break;
                    case ',':
                        c = '<';
                        break;
                    case '\'':
                        c = '\"';
                        break;

                }
            }
            if (c >= 65 && c <= 90 && !pressCapsLock) {
                c += 32;
            }
            num += c;

        }
    }

    public String getNum() {
        return num;
    }

    public void shift() {
        pressShift = true;
    }

    public void disShift() {
        pressShift = false;
    }

    public void CapsLock() {
        pressCapsLock = !pressCapsLock;

    }

}
