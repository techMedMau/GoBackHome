package controllers;

import gameobj.Alien;
import scene.popupwindow.*;

public class TaskController {
    private static TaskController taskController;
    private PopUpTask currentPopUp;
    public static TaskController getTaskController(){
        if (taskController==null){
            taskController=new TaskController();
        }
        return taskController;
    }
    private TaskController(){}
    public PopUpTask changePopUp(Task task){
        PopUpTask popUpTask=task.getPopUp();
        if (popUpTask!=null){
            popUpTask.sceneBegin();
            this.currentPopUp = popUpTask;
        }
        return currentPopUp;

    }
    public PopUpTask getCurrentPopUp(){
        return currentPopUp;
    }

    public enum Task{
        FIND_DIFFERENT(new PopUpFindDifferent()),
        FIND_PIC(new PopUpFindPic()),
        COLOR_CHANGE(new PopUpColorChange()),
        PASSWORD(new PopUpPassword()),
        LINE_UP(new PopUpLineUp()),
        ROCK(new PopUpRock()),
        CENTER(new PopUpCenter()),
        PUSH(new PopUpPush());
        private PopUpTask popUpTask;
        Task(PopUpTask popUpTask){
            this.popUpTask=popUpTask;
        }
        public PopUpTask getPopUp(){
            return popUpTask;
        }
    }
    public void taskEnd(){
        currentPopUp=null;
    }
}
