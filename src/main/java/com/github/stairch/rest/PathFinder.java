package com.github.stairch.rest;

import com.github.stairch.dtos.PointDTO;
import com.github.stairch.dtos.SnakeDTO;
import com.github.stairch.types.Move;

import java.util.ArrayList;
import java.util.List;

public class PathFinder {

    private int fieldWidth;
    private int fieldHeight;

    public PathFinder(final int fieldWidth, final int fieldHeight){
        this.fieldWidth = fieldWidth;
        this.fieldHeight = fieldHeight;

    }

    public SnakeDTO getOwnSnake(String ownSnake, List<SnakeDTO> allSnakes){
        for(SnakeDTO snake : allSnakes){
            if(snake.getId() == ownSnake){
                return snake;
            }
        }
        return null;
    }

    public PointDTO getClosestFood(SnakeDTO ownSnake, List<PointDTO> Foods){
        int listLength = Foods.size();
        int lessDiff = 5000000;
        PointDTO closestFood;
        PointDTO foodNow;

        closestFood = Foods.get(0);

        if (listLength > 1)
        {
            //get x,y Coordinates from ownSnake
            PointDTO ownSnakeHeadPosition = ownSnake.getCoords().get(0);
            int ownSnakeX = ownSnakeHeadPosition.getX();
            int ownSnakeY = ownSnakeHeadPosition.getY();
            for(int i = 0; i < listLength; i++){
                //Get x,y coordinates from food
                PointDTO food = Foods.get(i);
                int foodX = food.getX();
                int foodY = food.getY();

                //Calc how close
                int diffX = ownSnakeX - foodX;
                int diffY = ownSnakeY - foodY;
                if (diffX < 0){
                    diffX = -diffX;
                }
                if (diffY < 0){
                    diffY = -diffY;
                }

                int diff = diffY + diffX;
                if (diff < lessDiff){
                    lessDiff = diff;
                    closestFood = Foods.get(i);
                }
            }
        }
        return closestFood;
    }

    public Move moveToFood(SnakeDTO ownSnake, PointDTO Food, List<SnakeDTO> allSnakes){
        int counter = 0;
        PointDTO[] snake;
        PointDTO ownHead = ownSnake.getCoords().get(0);

        int distX = Food.getX() - ownHead.getX();
        if (distX < 0) {
            distX *= (-1);
        }
        int distY = Food.getY() - ownHead.getY();
        if (distY < 0) {
            distY *= (-1);
        }


        if (distX > distY{
            boolean isPositiv;
            PointDTO nextX = ownHead;
            if(Food.getX() - ownHead.getX() < 0){
                nextX.setX(ownHead.getX()-1);
                isPositiv = false;
            }else{
                nextX.setX(ownHead.getX()+1);
                isPositiv = true;
            }
            boolean pointIsFree = pointIsFree(nextX, allSnakes, this.fieldWidth, this.fieldHeight);

            switch (Food.getX() - ownHead.getX()){
                case < 0{

                }
            }
        }

        return null;
    }

    public boolean pointIsFree(PointDTO target, List<SnakeDTO> allSnakes, int fieldWidth, int fieldHeight){
        if(target.getX() > fieldWidth ||   //out of Field
                target.getX() < 0 ||
                target.getY() > fieldHeight ||
                target.getY() < 0){
            return false;
        }
        if(getOccupiedFields(allSnakes).contains(target)) {  //wenn eine lebende Snake auf dem Target Feld ist
            return false;
        }
        return true;
    }

    //TODO: schwanz weg?
    public List<PointDTO> getOccupiedFields(List<SnakeDTO> snakes){
        List<PointDTO> occupiedPoints = new ArrayList<>();
        for(SnakeDTO snake : snakes){
            for(PointDTO point : snake.getCoordsAsPoints()){
                occupiedPoints.add(point);
            }
        }
        return occupiedPoints;
    }
}
