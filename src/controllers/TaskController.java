package controllers;

import scene.Scene;
import scene.popupwindow.*;

public class TaskController {
    private static TaskController taskController;
    private PopUpWindows currentPopUp;
    public static TaskController getTaskController(){
        if (taskController==null){
            taskController=new TaskController();
        }
        return taskController;
    }
    private TaskController(){}
    public PopUpWindows changePopUp(Task task){
        PopUpWindows popUpWindows=task.getPopUp();
        if (popUpWindows!=null){
            PopUpWindows tmp=currentPopUp;
            popUpWindows.sceneBegin();
            this.currentPopUp=popUpWindows;
            if (tmp!=null){
                tmp.sceneEnd();
            }
        }
        return currentPopUp;

    }
    public PopUpWindows getCurrentPopUp(){
        return currentPopUp;
    }
    public enum Task{
        FIND_PRAY(new PopUpFindPray()),
        LIT_UP(new PopUpLitUp()),
        LUMBER(new PopUpLumber()),
        PASSWORD(new PopUPPassword()),
        RECOVER(new PopUpRecover()),
        WATER(new PopUpWater());
        private PopUpWindows popUpWindows;
        Task(PopUpWindows popUpWindows){
            this.popUpWindows=popUpWindows;
        }
        public PopUpWindows getPopUp(){
            return popUpWindows;
        }
    }
}
