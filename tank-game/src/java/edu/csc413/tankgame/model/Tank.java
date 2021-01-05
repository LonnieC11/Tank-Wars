package edu.csc413.tankgame.model;

/**
 * Model class representing a tank in the game. A tank has a position and an angle. It has a movement speed and a turn
 * speed, both represented below as constants.
 */
// TODO: Notice that Tank has a lot in common with Shell. For full credit, you will need to find a way to share code
// between the two classes so that the logic for e.g. moveForward, etc. are not duplicated.
public abstract class Tank extends Entity{

   private final int startCoolDown;
   protected int coolDown;

    //the constructor can stay the same as before, as we can simply call the corresponding super constructor, and pass the movement and turn speeds as constant values.
   //Entity will then use these values to initialize the movement and turn speed variables declared there.

    public Tank(String id, double x, double y, double angle, int startCoolDown){
       super(id, x, y, angle, 2.0, Math.toRadians(3.0), 5);
       this.coolDown = 0;
       this.startCoolDown = startCoolDown;
   }

    // The following methods will be useful for determining where a shell should be spawned when it
    // is created by this tank. It needs a slight offset so it appears from the front of the tank,
    // even if the tank is rotated. The shell should have the same angle as the tank.

    private double getShellX() {
        return getX() + 30.0 * (Math.cos(getAngle()) + 0.5);
    }

    private double getShellY() {
        return getY() + 30.0 * (Math.sin(getAngle()) + 0.5);
    }
    protected void shoot(GameState gameState){
        //as of right now, this creates a new shell every time the space bar is pressed. How can we limit this so
        //that it only shoots after the cooldown ends?

        Shell shell = new Shell(getShellX(), getShellY(), getAngle(), this.getId());
        gameState.addShell(shell);

    }

    @Override
    public void move(GameState gameState){
        if(coolDown > 0)
            decreaseCooldown();
        if(coolDown < 0)
            resetCooldown();
    }

    //in Tank.java
    @Override
    public void checkBounds(GameState gameState){
        if (getX() < GameState.TANK_X_LOWER_BOUND){
            setX(GameState.TANK_X_LOWER_BOUND);
        }

        if (getX() > GameState.TANK_X_UPPER_BOUND) {
            setX(GameState.TANK_X_UPPER_BOUND);
        }

        if (getY() < GameState.TANK_Y_LOWER_BOUND){
            setY(GameState.TANK_Y_LOWER_BOUND);
        }

        if (getY() > GameState.TANK_Y_UPPER_BOUND){
            setY(GameState.TANK_Y_UPPER_BOUND);
        }
    }
    protected void resetCooldown() {
        coolDown = startCoolDown;
    }
    protected void decreaseCooldown(){
        coolDown--;
    }
    protected int getCooldown(){
        return coolDown;
    }
    @Override
    public double getXBound(){
        return getX() + 55;
    }
    @Override
    public double getYBound(){
        return getY() + 55;
    }
    @Override
    public void decreaseHP(GameState gameState){
        super.decreaseHP(gameState);
    }
}