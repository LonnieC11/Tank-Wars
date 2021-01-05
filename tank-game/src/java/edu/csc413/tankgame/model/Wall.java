package edu.csc413.tankgame.model;

import edu.csc413.tankgame.WallImageInfo;

public class Wall extends Entity{
    //in Wall.java
    private static final String WALL_ID_PREFIX = "wall-";
    private static long uniqueId = 0L;

    public WallImageInfo wallImageInfo;

    public Wall(String id, double x, double y, WallImageInfo wallImageInfo) {
        super(id, x, y, 0, 3);
        this.wallImageInfo = wallImageInfo;
    }
    /*public void makeWalls(GameState gameState){
        for(WallImageInfo imageInfo: WallImageInfo.readWalls()){
            gameState.addWall(new Wall(imageInfo.getX(), imageInfo.getY(), imageInfo));
        }
    }*/

    public static String getUniqueId() {
        return WALL_ID_PREFIX + uniqueId++;
    }

    @Override
    public void move(GameState gameState) {}

    @Override
    public void checkBounds(GameState gameState){}



    @Override
    public double getXBound(){
        return getX() + 32;
    }
    @Override
    public double getYBound(){
        return getY() + 32;
    }
    @Override
    public void decreaseHP(GameState gameState){
        super.decreaseHP(gameState);
    }
}
