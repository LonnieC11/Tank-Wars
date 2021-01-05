package edu.csc413.tankgame.model;

public class CushionAiTank extends Tank{

    public CushionAiTank(String id, double x, double y, double angle){
        super(id, x, y, angle, 200);
    }

    @Override
    public void move(GameState gameState){
        super.move(gameState);
        Entity playerTank = gameState.getEntity(GameState.PLAYER_TANK_ID);
        double adjustedX;
        double adjustedY;
        if(gameState.upPressed()){
            adjustedX = 75 + (playerTank.getX() + MOVEMENT_SPEED * Math.cos(getAngle()));
            adjustedY = 75 + (playerTank.getY() + MOVEMENT_SPEED * Math.sin(getAngle()));
        }
        else if(gameState.downPressed()){
            adjustedX = (playerTank.getX() - MOVEMENT_SPEED * Math.cos(getAngle())) - 75;
            adjustedY = (playerTank.getY() - MOVEMENT_SPEED * Math.sin(getAngle())) - 75;
        }
        else{
            adjustedX = playerTank.getX();
            adjustedY = playerTank.getY();
        }
        double dx = adjustedX - getX();
        double dy = adjustedY - getY();
        double angleToPlayer = Math.atan2(dy, dx);
        double angleDifference = getAngle() - angleToPlayer;
        angleDifference -= Math.floor(angleDifference / Math.toRadians(360.0)+0.5) * Math.toRadians(360.0);
        if(angleDifference < -TURN_SPEED){
             turnRight();
        } else if(angleDifference > TURN_SPEED){
            turnLeft();
        }

        double distance = Math.sqrt(dx*dx + dy*dy);
        if(distance > 400.0){
            moveForward();
        } else if(distance < 200.0){
            moveBackward();
        }

        if(getCooldown() == 0) {
            shoot(gameState);
            resetCooldown();
        }
    }

    @Override
    public void decreaseHP(GameState gameState) {
        super.decreaseHP(gameState);
    }

}
