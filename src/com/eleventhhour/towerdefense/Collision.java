package com.eleventhhour.towerdefense;

/**
 * Collision class --
 * 
 * This class is a static class that just provides different types of collisions between two GameObjects.
 * 
 * @author Matt Martucciello
 *
 */
public class Collision {
	
	//Static class no need to instantiate
	private Collision() {}
	
	public static boolean axisAlignedBoundedCollision(GameObject obj1, GameObject obj2) {
	    if( obj1.getWorldPosition().y + obj1.getHeight() <= obj2.getWorldPosition().y )
	    	return false;
	    if( obj1.getWorldPosition().y >= obj2.getWorldPosition().y + obj2.getHeight() )
	        return false;    
	    if( obj1.getWorldPosition().x + obj1.getWidth() <= obj2.getWorldPosition().x )
	        return false;
	    if( obj1.getWorldPosition().x >= obj2.getWorldPosition().x + obj2.getWidth() )
	        return false;
	    
		return true;
	}
	
	public static boolean circleCollision(GameObject obj1, GameObject obj2) {
		float dx = (obj2.getWorldPosition().x-obj1.getWorldPosition().x);
		float dy = (obj2.getWorldPosition().y-obj1.getWorldPosition().y);
		int radii = obj1.getRadius() + obj2.getRadius();
		return ((dx*dx) + (dy * dy)) < (radii * radii);
	}
	
	public static boolean circleRectangleCollision(GameObject circle, GameObject rectangle) {
	    float cx = Math.abs(circle.getWorldPosition().x - rectangle.getWorldPosition().x);
	    float cy = Math.abs(circle.getWorldPosition().y - rectangle.getWorldPosition().y);

	    if (cx > (rectangle.getWidth()/2 + circle.getRadius()))
	    	return false;
	    if (cy > (rectangle.getHeight()/2 + circle.getRadius()))
	    	return false;
	    if (cx <= (rectangle.getWidth()/2))
	    	return true;
	    if (cy <= (rectangle.getHeight()/2))
	    	return true;

	    double cornerDistance_sq = Math.pow((cx - rectangle.getWidth()/2),2) + Math.pow((cy - rectangle.getHeight()/2),2);

	    return (cornerDistance_sq <= Math.pow(circle.getRadius(), 2));
	}
}
