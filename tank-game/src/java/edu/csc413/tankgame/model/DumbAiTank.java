package edu.csc413.tankgame.model;

public class DumbAiTank extends Tank{
    public DumbAiTank(String id, double x, double y, double angle){
        super(id, x, y, angle,175);
    }
    @Override
    public void move(GameState gameState){
        super.move(gameState);
        moveForward();
        turnRight();
        if(getCooldown() == 0) {
            shoot(gameState);
            resetCooldown();
        }
    }
}
