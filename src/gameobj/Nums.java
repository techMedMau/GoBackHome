package gameobj;

import gameobj.button.Num;

import java.awt.*;
import java.util.ArrayList;

public class Nums {
    private int quantity;
    private ArrayList<Num> nums;
    private Num target;
    public Nums(int quantity,int x,int y){
        this.quantity=quantity;
        nums=new ArrayList<>();
        for (int i=0;i<this.quantity;i++){
            nums.add(new Num(String.valueOf(i+1),x+51*i,y));
        }
    }
    public void paint(Graphics g){
        nums.forEach(num -> num.paint(g));
    }
    public void show(Point point){
        for (int i=0;i<nums.size();i++){
            System.out.println(nums.get(i).state(point));
            if (nums.get(i).state(point)){
                if (target!=null){
                    target.disShow();
                }
                nums.get(i).show();
                target=nums.get(i);
                break;

            }
        }


    }
    public String getTarget(){
        return target.getStr();
    }

}
