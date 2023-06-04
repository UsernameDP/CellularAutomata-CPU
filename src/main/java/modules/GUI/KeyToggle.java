package modules.GUI;

import imgui.ImBool;
import imgui.ImGui;

public class KeyToggle {

    private boolean currentState;
    private boolean lastState;
    private ImBool p_open;
    private int KeyValue;

    public KeyToggle(int KeyValue){
        p_open = new ImBool(false);
        currentState = false;
        lastState = false;
        this.KeyValue = KeyValue;
    }
    public KeyToggle(int KeyValue, ImBool p_open, boolean currentState, boolean lastState){
        this.p_open = p_open;
        this.currentState = currentState;
        this.lastState = lastState;
        this.KeyValue = KeyValue;
    }

    public ImBool getP_open(){
        return p_open;
    }

    public void updateP_Open(){
        this.currentState = ImGui.getIO().getKeysDown(this.KeyValue);

        if(this.currentState != this.lastState){
            if(this.currentState){
                if(p_open.get()){
                    p_open.set(false);
                }else{ //if p_open.get() is false
                    p_open.set(true);
                }
            }
        }

        this.lastState = this.currentState;
    }
}
