package edu.csc413.tankgame.model;

import edu.csc413.tankgame.view.RunGameView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * GameState represents the state of the game "world." The GameState object tracks all of the moving entities like tanks
 * and shells, and provides the controller of the program (i.e. the GameDriver) access to whatever information it needs
 * to run the game. Essentially, GameState is the "data context" needed for the rest of the program.
 */
public class GameState {
    public static final double TANK_X_LOWER_BOUND = 30.0;
    public static final double TANK_X_UPPER_BOUND = RunGameView.SCREEN_DIMENSIONS.width - 100.0;
    public static final double TANK_Y_LOWER_BOUND = 30.0;
    public static final double TANK_Y_UPPER_BOUND = RunGameView.SCREEN_DIMENSIONS.height - 120.0;

    public static final double SHELL_X_LOWER_BOUND = -10.0;
    public static final double SHELL_X_UPPER_BOUND = RunGameView.SCREEN_DIMENSIONS.width;
    public static final double SHELL_Y_LOWER_BOUND = -10.0;
    public static final double SHELL_Y_UPPER_BOUND = RunGameView.SCREEN_DIMENSIONS.height;

    public static final String PLAYER_TANK_ID = "player-tank";
    public static final String AI_TANK_ID = "ai-tank";
    public static final String AI_TANK_ID_2 = "ai-tank-2";

    // TODO: Feel free to add more tank IDs if you want to support multiple AI tanks! Just make sure they're unique.

    // TODO: Implement.
    // There's a lot of information the GameState will need to store to provide contextual information. Add whatever
    // instance variables, constructors, and methods are needed.

    private final List<Entity> entities = new ArrayList<>();

    public void addEntity(Entity entity){
        entities.add(entity);
    }

    public void removeEntity(Entity entity){
        entities.remove(entity);
    }

    public List<Entity> getEntities(){
        return entities;
    }

    public Entity getEntity(String id){
        return entities.stream()
                .filter(entity -> entity.getId().equals(id))
                .findAny()
                .get();
    }

    private boolean isUpPressed;
    private boolean isDownPressed;
    private boolean isLeftPressed;
    private boolean isRightPressed;
    private boolean isSpacePressed;
    private boolean isEscPressed;


    public void setIsUpPressed(boolean isUpPressed) {
        this.isUpPressed = isUpPressed;
    }

    public boolean isUpPressed() {
        return this.isUpPressed;
    }

    public void setIsDownPressed(boolean isDownPressed) {
        this.isDownPressed = isDownPressed;
    }

    public boolean isDownPressed() {
        return this.isDownPressed;
    }

    public boolean isLeftPressed() {
        return this.isLeftPressed;
    }

    public void setIsLeftPressed(boolean isLeftPressed) {
        this.isLeftPressed = isLeftPressed;
    }

    public boolean isRightPressed() {
        return this.isRightPressed;
    }

    public void setIsRightPressed(boolean isRightPressed) {
        this.isRightPressed = isRightPressed;
    }

    public boolean isSpacePressed() {
        return isSpacePressed;
    }

    public void setSpacePressed(boolean isSpacePressed) {
        this.isSpacePressed = isSpacePressed;
    }
    public void setEscPressed(boolean isEscPressed){
        this.isEscPressed = isEscPressed;
    }
    public boolean isEscPressed(){
        return isEscPressed;
    }


    //in GameState...


    public boolean upPressed(){
        return isUpPressed();
    }
    public boolean downPressed(){
        return isDownPressed();
    }
    public boolean leftPressed(){
        return isLeftPressed();
    }
    public boolean rightPressed(){
        return isRightPressed();
    }
    public boolean spacePressed(){
        return isSpacePressed();
    }
    public boolean escPressed(){
        return isEscPressed();
    }

    //List of newly-created shells
    private final List<Shell> shells = new ArrayList<>();

    public void addShell(Shell shell){
        shells.add(shell);
    }

    public List<Shell> getShells(){
        return shells;
    }

    private final List<Shell> shellsToRemove = new ArrayList<>();

    public void addShellToRemove(Shell shell){
        shellsToRemove.add(shell);
    }
    //in GameState
    public List<Shell> getShellsToRemove(){
        return shellsToRemove;
    }

    //BEGAN WORKING: DEC. 17 11:15AM
    private final List<Entity> deadEntities = new ArrayList<>();

    public void addDeadEntity(Entity entity){
        deadEntities.add(entity);
    }

    public List<Entity> getDeadEntities(){
        return deadEntities;
    }
}
