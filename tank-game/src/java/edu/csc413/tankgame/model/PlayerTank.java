package edu.csc413.tankgame.model;

import edu.csc413.tankgame.view.RunGameView;

public class PlayerTank extends Tank{

    public PlayerTank(String id, double x, double y, double angle){
        super(id, x, y, angle, 100);
    }
    //in PlayerTank...
    @Override
    public void move(GameState gameState){
        super.move(gameState);
        if(gameState.upPressed()){
            moveForward();
        }
        if(gameState.downPressed()){
            moveBackward();
        }
        if(gameState.leftPressed()){
            turnLeft();
        }
        if(gameState.rightPressed()){
            turnRight();
        }

        if(getCooldown() == 0) {
            if (gameState.spacePressed()) {
                shoot(gameState);
                resetCooldown();
            }
        }
    }
}
