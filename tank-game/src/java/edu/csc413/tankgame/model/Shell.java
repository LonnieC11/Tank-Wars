package edu.csc413.tankgame.model;

/**
 * Model class representing a shell that has been fired by a tank. A shell has a position and an angle, as well as a
 * speed. Shells by default should be unable to turn and only move forward.
 */
// TODO: Notice that Shell has a lot in common with Tank. For full credit, you will need to find a way to share code
// between the two classes so that the logic for e.g. moveForward, etc. are not duplicated.
public class Shell extends Entity{
    private static final String SHELL_ID_PREFIX = "shell-";


    private static long uniqueId = 0L;

    private String tank_Id;

    public Shell(double x, double y, double angle, String tank_Id) {
        super(getUniqueId(), x, y, angle, 6.0, 1);
        this.tank_Id = tank_Id;
    }

    @Override
    public void move(GameState gameState){
        moveForward();
    }
    public String getTankID(){
        return tank_Id;
    }

    private static String getUniqueId() {
        return SHELL_ID_PREFIX + uniqueId++;
    }

    //in Shell.java
    @Override
    public void checkBounds(GameState gameState) {
            if (getX() < GameState.TANK_X_LOWER_BOUND) {
                //setX(GameState.TANK_Y_LOWER_BOUND);
                decreaseHP(gameState);
            }

            if (getX() > GameState.TANK_X_UPPER_BOUND) {
                //setX(GameState.TANK_X_UPPER_BOUND);
                decreaseHP(gameState);
            }

            if (getY() < GameState.TANK_Y_LOWER_BOUND) {
                //setY(GameState.TANK_Y_LOWER_BOUND);
                decreaseHP(gameState);
            }

            if (getY() > GameState.TANK_Y_UPPER_BOUND) {
                //setY(GameState.TANK_Y_UPPER_BOUND);
                decreaseHP(gameState);
            }
    }

    @Override
    public double getXBound(){
        return getX() + 24;
    }
    @Override
    public double getYBound(){
        return getY() + 24;
    }
    @Override
    public void decreaseHP(GameState gameState){
        super.decreaseHP(gameState);
    }
}
