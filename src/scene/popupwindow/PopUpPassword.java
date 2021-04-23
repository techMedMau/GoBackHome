package scene.popupwindow;

import controllers.ImageController;
import utils.CommandSolver;
import gameobj.button.Button;
import utils.Delay;

import java.awt.*;
import java.util.ArrayList;

public class PopUpPassword extends PopUpTask {

    private Image password;
    private ArrayList<Button> buttons;
    private Delay delay;
    private boolean isShow;
    private ArrayList<Button> code;
    private ArrayList<Button> input;

    public PopUpPassword(){
        buttons = new ArrayList<>();
        code = new ArrayList<>();
        input = new ArrayList<>();
        password = ImageController.getInstance().tryGet("/password/question.png");
        for(int i = 0; i < 9; i++) {
            buttons.add(new Button(390+i%3*50, 225+i/3*50, 32, 32
                    , ImageController.getInstance().tryGet("/password/"+i+".png")));
        }
        buttons.add(new Button(440, 375, 32, 32
                , ImageController.getInstance().tryGet("/password/9.png")));
        this.delay = new Delay( 90);
        delay.play();
        isShow = true;
        code.add(buttons.get(1));
        code.add(buttons.get(5));
        code.add(buttons.get(1));
        code.add(buttons.get(3));
        code.add(buttons.get(6));
    }

    public void paint(Graphics g){
        super.paint(g);
        if(isShow){
            g.drawImage(password, 390,275,null);
        }
        if(this.delay.count()) {
            isShow = false;
        }
        if(!isShow){
            for (int i = 0; i < buttons.size(); i++) {
                buttons.get(i).paint(g);
            }
            for(int i = 0; i < input.size(); i ++){
                g.drawImage(input.get(i).getImg(), 250+i*30,450, null);
            }
        }
        if(isDone()){
            g.drawImage(finish, 350,125,null);
        }
    }


    @Override
    public void update(){
        super.update();
        if(input.size() == code.size()){
            if(!check()){
                input = new ArrayList<>();
            }else{
                setDone(true);
            }
        }
    }

    @Override
    public CommandSolver.MouseListener mouseListener() {

        return (e, state, trigTime) -> {
            switch (state) {
                case CLICKED:
                    super.mouseListener().mouseTrig(e, state, trigTime);
                    int i;
                    for( i = 0; i < buttons.size(); i ++){
                        if(buttons.get(i).state(e.getPoint())){
                            input.add(buttons.get(i));
                            break;
                        }
                    }
//                    檢查測試用
//                    for(int k = 0; k < input.size(); k++){
//                        System.out.println(input.get(k));
//                    }

                    if (i<buttons.size()){
                        break;
                    }

                    break;
            }
        };
    }

    public boolean check(){
        for(int i = 0; i < code.size(); i++){
            if(input.get(i)!=code.get(i)){
                return false;
            }
        }
        return true;
    }

}
