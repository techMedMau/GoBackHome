package scene.popupwindow;

import controllers.ImageController;

import java.awt.*;

public class PopUpPassword extends PopUpTask {
    private Image img;

    public PopUpPassword(){
        this.img = ImageController.getInstance().tryGet("/password/sheet.png");
        
    }

}
