package edu.csc413.tankgame;

        import edu.csc413.tankgame.model.*;
        import edu.csc413.tankgame.view.MainView;
        import edu.csc413.tankgame.view.RunGameView;

        import java.awt.event.ActionEvent;
        import java.awt.event.ActionListener;
        import java.awt.event.KeyEvent;
        import java.awt.event.KeyListener;
        import java.util.Collection;
        import java.util.Iterator;
        import java.util.List;
        import java.util.stream.Collectors;

/**
 * GameDriver is the primary controller class for the tank game. The game is launched from GameDriver.main, and
 * GameDriver is responsible for running the game loop while coordinating the views and the data models.
 */
public class GameDriver {
    // TODO: Implement.
    // Add the instance variables, constructors, and other methods needed for this class. GameDriver is the centerpiece
    // for the tank game, and should store and manage the other components (i.e. the views and the models). It also is
    // responsible for running the game loop.
    private final String START_BUTTON_ACTION_COMMAND = "start_ac";
    private final String EXIT_BUTTON_ACTION_COMMAND = "exit_ac";

    private final MainView mainView;
    private final RunGameView runGameView;
    private final GameState gameState = new GameState();

    public GameDriver() {
        ActionListener listener = getListener();
        KeyListener keyListener = new InputListener(gameState);
        mainView = new MainView(listener, keyListener);
        runGameView = mainView.getRunGameView();

    }
    private ActionListener getListener(){
        ActionListener listener=event ->{
            String actionCommand = event.getActionCommand();
            if(actionCommand.equals(START_BUTTON_ACTION_COMMAND)){
                mainView.setScreen(MainView.Screen.RUN_GAME_SCREEN);
                runGame();
            }
            else if(actionCommand.equals(EXIT_BUTTON_ACTION_COMMAND)){
                mainView.closeGame();
            }
        };
        return listener;
    }
    public void start() {
        // TODO: Implement.
        // This should set the MainView's screen to the start menu screen.

        mainView.setScreen(MainView.Screen.START_MENU_SCREEN);

    }

    private void runGame() {
        Tank playerTank  = new PlayerTank(
                GameState.PLAYER_TANK_ID,
                RunGameView.PLAYER_TANK_INITIAL_X,
                RunGameView.PLAYER_TANK_INITIAL_Y,
                RunGameView.PLAYER_TANK_INITIAL_ANGLE
        );
        Tank aiTank = new TurretAiTank(
                GameState.AI_TANK_ID,
                RunGameView.AI_TANK_INITIAL_X,
                RunGameView.AI_TANK_INITIAL_Y,
                RunGameView.AI_TANK_INITIAL_ANGLE
        );
        Tank aiTank2 = new CushionAiTank(
                GameState.AI_TANK_ID_2,
                RunGameView.AI_TANK_2_INITIAL_X,
                RunGameView.AI_TANK_2_INITIAL_Y,
                RunGameView.AI_TANK_2_INITIAL_ANGLE
        );
        gameState.addEntity(playerTank);
        gameState.addEntity(aiTank);
        gameState.addEntity(aiTank2);


        runGameView.addDrawableEntity(
                GameState.PLAYER_TANK_ID,
                RunGameView.PLAYER_TANK_IMAGE_FILE,
                playerTank.getX(),
                playerTank.getY(),
                playerTank.getAngle()
        );
        runGameView.addDrawableEntity(
                GameState.AI_TANK_ID,
                RunGameView.AI_TANK_IMAGE_FILE,
                aiTank.getX(),
                aiTank.getY(),
                aiTank.getAngle()
        );
        runGameView.addDrawableEntity(
                GameState.AI_TANK_ID_2,
                RunGameView.AI_TANK_IMAGE_FILE,
                aiTank2.getX(),
                aiTank2.getY(),
                aiTank2.getAngle()
        );
        //ADD WALLS HERE!!!

        List<WallImageInfo> infoList = WallImageInfo.readWalls();
        for(WallImageInfo imageInfo: infoList){
            String wallId = Wall.getUniqueId();
            Wall wall = new Wall(wallId, imageInfo.getX(), imageInfo.getY(), imageInfo);
            gameState.addEntity(wall);
            runGameView.addDrawableEntity(
                    wall.getId(),
                    imageInfo.getImageFile(),
                    wall.getX(),
                    wall.getY(),
                    wall.getAngle()
            );
        }

        Runnable gameRunner = () -> {
            while (update()) {
                runGameView.repaint();
                try {
                    Thread.sleep(8L);
                } catch (InterruptedException exception) {
                    throw new RuntimeException(exception);
                }
            }
        };
        new Thread(gameRunner).start();
    }

    // TODO: Implement.
    // update should handle one frame of gameplay. All tanks and shells move one step, and all drawn entities
    // should be updated accordingly. It should return true as long as the game continues.
    private boolean update() {
        for(Entity entity: gameState.getEntities()){
            entity.move(gameState);
        }
        //Part 2: check collisions

        //Ask gameState -- any new shells to draw?
        //If so, call addDrawableEntity() from RunGameView

        for(Shell shell: gameState.getShells()){
            runGameView.addDrawableEntity(
                    shell.getId(),
                    RunGameView.SHELL_IMAGE_FILE,
                    shell.getX(),
                    shell.getY(),
                    shell.getAngle()
            );
            gameState.addEntity(shell);
        }
        gameState.getShells().clear();

        //in GameDriver.update() right after "adding new shells" block
        //Ask all tanks, shells, etc. to check bounds
        for(Entity entity: gameState.getEntities()){
            entity.checkBounds(gameState);
        }
        for(int i = 0; i < gameState.getEntities().size() - 1; i++){
            for(int j = i+1; j < gameState.getEntities().size(); j++){

                Entity entity1 = gameState.getEntities().get(i);
                Entity entity2 = gameState.getEntities().get(j);

                if(entitiesOverlap(entity1, entity2)){
                    handleCollision(entity1, entity2);
                }
            }
        }

        for(Entity entity: gameState.getDeadEntities()){
            runGameView.removeDrawableEntity(entity.getId());
            gameState.removeEntity(entity);
            if(entity.getId().equals(GameState.PLAYER_TANK_ID)){
                endGame();
                return false;
            }
        }
        if(gameState.getEntities().stream().filter(entity -> entity instanceof Tank).collect(Collectors.toList()).size() <= 1){
            endGame();
            return false;
        }

        gameState.getDeadEntities().clear();

        //esc keyListener
        if(gameState.escPressed()){
            endGame();
            return false;
        }
        for(Entity entity: gameState.getEntities()){
            runGameView.setDrawableEntityLocationAndAngle(entity.getId(), entity.getX(), entity.getY(), entity.getAngle());
        }
        return true;
    }
    //in GameDriver
    private void endGame(){
        gameState.getEntities().clear();
        gameState.getDeadEntities().clear();
        gameState.getShellsToRemove().clear();
        gameState.getShells().clear();
        runGameView.reset();
        mainView.setScreen(MainView.Screen.END_MENU_SCREEN);
        //return false;
    }
    private class InputListener implements KeyListener {
        private final GameState gameState;

        public InputListener(GameState gameState){
            this.gameState = gameState;
        }

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            int keyCode = e.getKeyCode();
            if(keyCode == KeyEvent.VK_D){
                gameState.setIsRightPressed(true);
            }
            else if(keyCode == KeyEvent.VK_A){
                gameState.setIsLeftPressed(true);
            }
            else if(keyCode == KeyEvent.VK_W){
                gameState.setIsUpPressed(true);
            }
            else if(keyCode == KeyEvent.VK_S){
                gameState.setIsDownPressed(true);
            }
            else if(keyCode == KeyEvent.VK_SPACE){
                gameState.setSpacePressed(true);
            }
            else if(keyCode == KeyEvent.VK_ESCAPE){
                gameState.setEscPressed(true);
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            int keyCode = e.getKeyCode();
            if(keyCode == KeyEvent.VK_D){
                gameState.setIsRightPressed(false);
            }
            else if(keyCode == KeyEvent.VK_A){
                gameState.setIsLeftPressed(false);
            }
            else if(keyCode == KeyEvent.VK_W){
                gameState.setIsUpPressed(false);
            }
            else if(keyCode == KeyEvent.VK_S){
                gameState.setIsDownPressed(false);
            }
            else if(keyCode == KeyEvent.VK_SPACE){
                gameState.setSpacePressed(false);
            }
            else if(keyCode == KeyEvent.VK_ESCAPE){
                gameState.setEscPressed(false);
            }
        }
    }

    private boolean entitiesOverlap(Entity entity1, Entity entity2){
        return entity1.getX() < entity2.getXBound()
                && entity1.getXBound() > entity2.getX()
                && entity1.getY() < entity2.getYBound()
                && entity1.getYBound() > entity2.getY();
    }
    //in GameDriver
    private void handleCollision(Entity entity1, Entity entity2){
        if(entity1 instanceof Tank && entity2 instanceof Tank){
            //handle the tank tank collision
            double xDistance1 = entity1.getXBound() - entity2.getX();
            double xDistance2 = entity2.getXBound() - entity1.getX();
            double yDistance1 = entity1.getYBound() - entity2.getY();
            double yDistance2 = entity2.getYBound() - entity1.getY();

            double distanceHalved;

            if(xDistance1 < xDistance2 && xDistance1 < yDistance1 && xDistance1 < yDistance2){
                distanceHalved = (xDistance1/2);

                entity1.setX(entity1.getX() - distanceHalved);
                entity2.setX(entity2.getX() + distanceHalved);


                //System.out.println("something happens");
            }

            else if(xDistance2 < xDistance1 && xDistance2 < yDistance1 && xDistance2 < yDistance2){
                distanceHalved = (xDistance2/2);

                entity1.setX(entity1.getX() + distanceHalved);
                entity2.setX(entity2.getX() - distanceHalved);

                //System.out.println("something else happens");
            }

            else if(yDistance1 < xDistance1 && yDistance1 < xDistance2 && yDistance1 < yDistance2){
                distanceHalved = (yDistance1/2);

                entity1.setY(entity1.getY() - distanceHalved);
                entity2.setY(entity2.getY() + distanceHalved);

                //System.out.println("something else happens again");
            }

            else if(yDistance2 < xDistance1 && yDistance2 < xDistance2 && yDistance2 < yDistance1){
                distanceHalved = (yDistance2/2);

                entity1.setY(entity1.getY() + distanceHalved);
                entity2.setY(entity2.getY() - distanceHalved);


                //System.out.println("something else happens again again");
            }

        }

        //continuation of handleCollision...
        else if(entity1 instanceof Shell || entity2 instanceof Shell){
            //handle shell-entity collision.
            if(entity1 instanceof Shell && entity2 instanceof Tank){
                if( !((Shell) entity1).getTankID().equals(entity2.getId()) ){
                    entity1.decreaseHP(gameState);
                    entity2.decreaseHP(gameState);
                }
            }
            else if(entity1 instanceof Tank && entity2 instanceof Shell){
                if( !((Shell) entity2).getTankID().equals(entity1.getId()) ){
                    entity1.decreaseHP(gameState);
                    entity2.decreaseHP(gameState);
                }
            }
            else{
                entity1.decreaseHP(gameState);
                entity2.decreaseHP(gameState);
            }
        }
        else if(entity1 instanceof Tank && entity2 instanceof Wall){
            //handle tank-wall collision
            double xDistance1 = entity1.getXBound() - entity2.getX();
            double xDistance2 = entity2.getXBound() - entity1.getX();
            double yDistance1 = entity1.getYBound() - entity2.getY();
            double yDistance2 = entity2.getYBound() - entity1.getY();

            double distance;

            if(xDistance1 < xDistance2 && xDistance1 < yDistance1 && xDistance1 < yDistance2){
                distance = xDistance1;

                entity1.setX(entity1.getX() - distance);

            }

            else if(xDistance2 < xDistance1 && xDistance2 < yDistance1 && xDistance2 < yDistance2){
                distance = xDistance2;

                entity1.setX(entity1.getX() + distance);

            }

            else if(yDistance1 < xDistance1 && yDistance1 < xDistance2 && yDistance1 < yDistance2){
                distance = yDistance1;

                entity1.setY(entity1.getY() - distance);

            }

            else if(yDistance2 < xDistance1 && yDistance2 < xDistance2 && yDistance2 < yDistance1){
                distance  = yDistance2;

                entity1.setY(entity1.getY() + distance);

            }
        }

    }

    public static void main(String[] args) {
        GameDriver gameDriver = new GameDriver();
        gameDriver.start();
    }
}
