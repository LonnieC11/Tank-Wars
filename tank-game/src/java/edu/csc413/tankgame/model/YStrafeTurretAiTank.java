package edu.csc413.tankgame.model;

public class YStrafeTurretAiTank extends Tank{
    public YStrafeTurretAiTank(String id, double x, double y, double angle){
        super(id, x, y, angle, 100);
    }

    @Override
    public void move(GameState gameState) {
        super.move(gameState);
    }
}
