package com.fixed4fun.jumpingkunav2;

import android.view.View;
import android.widget.ImageView;

public class CollisionDetection {

    public CollisionDetection() {
    }

    public boolean collision(ImageView a, View b) {
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

        //check for top bottom corner
        if (firstObjectLeftBottomY >= secondObjectLeftTopY && firstObjectLeftBottomY <= secondObjectLeftBottomY) {
            if (firstObjectLeftBottomX >= secondObjectLeftTopX && firstObjectLeftBottomX <= secondObjectRightTopX) {
                return false;
            }
        }

        //check for middle of right X edge
        if (firstObjectLeftBottomY - (firObjHeight / 2) >= secondObjectLeftTopY && firstObjectLeftBottomY - (firObjHeight / 2) <= secondObjectLeftBottomY) {
            if (firstObjectLeftBottomX >= secondObjectLeftTopX && firstObjectLeftBottomX <= secondObjectRightTopX) {
                return true;
            }
        }

        //check for right bottom corner
        if (firstObjectRightBottomY >= secondObjectLeftTopY && firstObjectRightBottomY <= secondObjectLeftBottomY) {
            if (firstObjectRightBottomX >= secondObjectLeftTopX && firstObjectRightBottomX <= secondObjectRightTopX) {
                return true;
            }
        }

        //check for top left  corner
        if (firstObjectLeftTopY <= secondObjectLeftBottomY && firstObjectLeftTopY >= secondObjectRightTopY) {
            if (firstObjectLeftTopX >= secondObjectLeftBottomX && firstObjectLeftTopX <= secondObjectRightBottomX) {
                return true;
            }
        }

        //check for right top corner
        if ((firstObjectRightTopY <= secondObjectLeftBottomY && firstObjectRightTopY >= secondObjectRightTopY)) {
            if (firstObjectRightTopX >= secondObjectLeftBottomX && firstObjectRightTopX <= secondObjectRightBottomX) {
                return true;
            }
        }

        //check for middle of right X edge
        if ((firstObjectRightTopY + firObjHeight / 2 <= secondObjectLeftBottomY && firstObjectRightTopY + firObjHeight / 2 >= secondObjectRightTopY)) {
            if (firstObjectRightTopX >= secondObjectLeftBottomX && firstObjectRightTopX <= secondObjectRightBottomX) {
                return true;
            }
        }


        return false;
    }

}
