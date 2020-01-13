package com.example.myapplication;

import android.util.Log;
import android.widget.ImageView;

public class CollisionDetection {

    public CollisionDetection() {
    }

    public boolean collision(ImageView a, ImageView b) {
        float firstObjectLeftTopX, firstObjectLeftBottomX, firstObjectRightTopX, firstObjectRightBottomX;
        float firstObjectLeftTopY, firstObjectLeftBottomY, firstObjectRightTopY, firstObjectRightBottomY;

        float secondObjectLeftTopX, secondObjectLeftBottomX, secondObjectRightTopX, secondObjectRightBottomX;
        float secondObjectLeftTopY, secondObjectLeftBottomY, secondObjectRightTopY, secondObjectRightBottomY;

        float firObjGetX, firObjGetY, firObjHeight, firObjWidth;
        float secObjGetX, secObjGetY, secObjHeight, secObjWidth;

        int iceObstacleMargin = 15;


        firObjGetX = a.getX();
        firObjGetY = a.getY();
        firObjHeight = a.getHeight();
        firObjWidth = a.getWidth();

        secObjGetX = b.getX();
        secObjGetY = b.getY();
        secObjHeight = b.getHeight();
        secObjWidth = b.getWidth();


        firstObjectLeftTopX = firObjGetX + 5;
        firstObjectLeftTopY = firObjGetY - 5;
        firstObjectLeftBottomX = firObjGetX + 5;
        firstObjectLeftBottomY = firObjGetY + firObjHeight - 5;
        firstObjectRightTopX = firObjGetX + firObjWidth - 5;
        firstObjectRightTopY = firObjGetY + 5;
        firstObjectRightBottomX = firObjGetX + firObjWidth - 5;
        firstObjectRightBottomY = firObjGetY + firObjHeight - 5;


        secondObjectLeftTopX = secObjGetX + iceObstacleMargin;
        secondObjectLeftTopY = secObjGetY + iceObstacleMargin;
        secondObjectLeftBottomX = secObjGetX + iceObstacleMargin;
        secondObjectLeftBottomY = secObjGetY + secObjHeight * 0.8f - 5;
        secondObjectRightTopX = secObjGetX + secObjWidth - iceObstacleMargin;
        secondObjectRightTopY = secObjGetY + iceObstacleMargin;
        secondObjectRightBottomX = secObjGetX + secObjWidth - iceObstacleMargin;
        secondObjectRightBottomY = secObjGetY + secObjHeight - iceObstacleMargin;


        if (firstObjectLeftBottomY >= secondObjectLeftTopY && firstObjectLeftBottomY <= secondObjectLeftBottomY) {
            if (firstObjectLeftBottomX >= secondObjectLeftTopX && firstObjectLeftBottomX <= secondObjectRightTopX) {
                Log.d("123456", "collision: bottom left");
                return true;
            }
        }
        if (firstObjectRightBottomY >= secondObjectLeftTopY && firstObjectRightBottomY <= secondObjectLeftBottomY) {
            if (firstObjectRightBottomX >= secondObjectLeftTopX && firstObjectRightBottomX <= secondObjectRightTopX) {
                Log.d("123456", "collision: bottom right");
                return true;
            }
        }

        if (firstObjectLeftTopY <= secondObjectLeftBottomY && firstObjectLeftTopY >= secondObjectRightTopY) {
            if (firstObjectLeftTopX >= secondObjectLeftBottomX && firstObjectLeftTopX <= secondObjectRightBottomX) {
                Log.d("123456", "collision: top left");
                return true;
            }
        }


        if ((firstObjectRightTopY <= secondObjectLeftBottomY && firstObjectRightTopY >= secondObjectRightTopY)) {
            if (firstObjectRightTopX >= secondObjectLeftBottomX && firstObjectRightTopX <= secondObjectRightBottomX) {
                Log.d("123456", "collision: top right ");
                return true;
            }
        }


        return false;
    }

}
