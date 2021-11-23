package util;

import javafx.scene.input.KeyCode;

public class Input {
    public KeyCode keyFoward;
    public KeyCode keyRotateLeft;
    public KeyCode keyRotateRight;
    public KeyCode keyFire;

    public Input(KeyCode foward, KeyCode rotateLeft, KeyCode rotateRight, KeyCode fire){
        this.keyFoward = foward;
        this.keyRotateLeft = rotateLeft;
        this.keyRotateRight = rotateRight;
        this.keyFire = fire;
    }

    public KeyCode getKeyFoward() {
        return keyFoward;
    }

    public void setKeyFoward(KeyCode keyFoward) {
        this.keyFoward = keyFoward;
    }

    public KeyCode getKeyRotateLeft() {
        return keyRotateLeft;
    }

    public void setKeyRotateLeft(KeyCode keyRotateLeft) {
        this.keyRotateLeft = keyRotateLeft;
    }

    public KeyCode getKeyRotateRight() {
        return keyRotateRight;
    }

    public void setKeyRotateRight(KeyCode keyRotateRight) {
        this.keyRotateRight = keyRotateRight;
    }

    public KeyCode getKeyFire() {
        return keyFire;
    }

    public void setKeyFire(KeyCode keyFire) {
        this.keyFire = keyFire;
    }
}
