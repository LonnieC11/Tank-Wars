package edu.csc413.tankgame.model;

import edu.csc413.tankgame.view.RunGameView;

public abstract class Entity{

    public double MOVEMENT_SPEED;
    public double TURN_SPEED;

    private final String id;
    private double x;
    private double y;
    private double angle;
    private int hp;

    //basic Constructor to help ensuing constructors be cleaner
    public Entity(String id, double x, double y, double angle, int hp) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.hp = hp;
    }
    //Constructor that will be used for shells. Note: this constructor only takes in movement speed, since bullets aren't supposed to turn.
    //We will also use it to initialize the Tank's movement speed.
    public Entity(String id, double x, double y, double angle, double MOVEMENT_SPEED, int hp) {
        this(id, x, y, angle, hp);
        this.MOVEMENT_SPEED = MOVEMENT_SPEED;
    }

    //Constructor that will be used for tanks. Note: this one does have both moving and turn speeds, which will allow it to use the turn methods.
    public Entity(String id, double x, double y, double angle, double MOVEMENT_SPEED, double TURN_SPEED, int hp) {
        this(id, x, y, angle, MOVEMENT_SPEED, hp);
        this.TURN_SPEED = TURN_SPEED;
    }

    public String getId() {
        return id;
    }
    public void setX(double x){
        this.x = x;
    }
    public double getX() {
        return x;
    }

    public void setY(double y){
        this.y = y;
    }
    public double getY() {
        return y;
    }

    public double getAngle() {
        return angle;
    }



    public abstract double getXBound();
    public abstract double getYBound();

    public double getMovementSpeed() {
        return MOVEMENT_SPEED;
    }

    public double getTurnSpeed() {
        return TURN_SPEED;
    }

    public void setTurnSpeed(double turnSpeed) {
        TURN_SPEED = turnSpeed;
    }
    public abstract void move(GameState gameState);

    public abstract void checkBounds(GameState gameState);



    // TODO: The methods below are provided so you don't have to do the math for movement. However, note that they are
    // protected. You should not be calling these methods directly from outside the Tank class hierarchy. Instead,
    // consider how to design your Tank class(es) so that a Tank can represent both a player-controlled tank and an AI
    // controlled tank.

    protected void moveForward() {
        x += MOVEMENT_SPEED * Math.cos(angle);
        y += MOVEMENT_SPEED * Math.sin(angle);
    }

    protected void moveBackward() {
        x -= MOVEMENT_SPEED * Math.cos(angle);
        y -= MOVEMENT_SPEED * Math.sin(angle);
    }

    protected void turnLeft() {
        angle -= TURN_SPEED;
    }

    protected void turnRight() {
        angle += TURN_SPEED;
    }


    //in Entity
    public int getHP(){
        return hp;
    }

    private void setHP(){
        hp--;
    }

    public void decreaseHP(GameState gameState){
        setHP();
        if(hp <= 0){
            gameState.addDeadEntity(this);
        }
    }

}
