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
	
	public static enum CollisionShape {
		RECTANGLE, CIRCLE
	}

	//Static class no need to instantiate
	private Collision() {}
	
	/**
	 * Collide
	 * 
	 * This function will determine the collision shape of the two game objects passed and will call
	 * the appropriate collision function 
	 * 
	 * @param obj1 - object one to collide
	 * @param obj2 - object two to collide
	 * @return whether or not they are colliding
	 */
	public static boolean collide(GameObject obj1, GameObject obj2) {
		if (obj1.getCollidable().getShape() == obj2.getCollidable().getShape()) { //both objects are the same type determine which type
			if (obj1.getCollidable().getShape() == CollisionShape.CIRCLE) //they are circles so call the circle collision
				return Collision.circleCollision(obj1.getCollidable(), obj2.getCollidable());
			else //they are rectangles call AABB collision
				return Collision.axisAlignedBoundedCollision(obj1.getCollidable(), obj2.getCollidable());
		}
		else {
			if (obj1.getCollidable().getShape() == CollisionShape.CIRCLE) //obj1 is a circle and obj2 is a rectangle
				return Collision.circleRectangleCollision(obj1.getCollidable(), obj2.getCollidable());
			else //obj1 is a rectangle and obj2 is a circle
				return Collision.circleRectangleCollision(obj2.getCollidable(), obj1.getCollidable());
		}
	}
	
	/**
	 * axisAlignedBoundedCollision
	 * 
	 * This function will return whether or not two axis aligned boxes are colliding.
	 * 
	 * @param obj1 - object one to collide
	 * @param obj2 - object two to collide
	 * @return whether or not they are colliding
	 */
	private static boolean axisAlignedBoundedCollision(GameObject obj1, GameObject obj2) {
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
	
	/**
	 * 
	 * circleCollision
	 * 
	 * This function will return whether or not two circles are colliding 
	 * 
	 * @param obj1
	 * @param obj2
	 * @return whether or not they are colliding
	 */
	private static boolean circleCollision(GameObject obj1, GameObject obj2) {
		float dx = (obj2.getCenterPosition().x-obj1.getCenterPosition().x);
		float dy = (obj2.getCenterPosition().y-obj1.getCenterPosition().y);
		int radii = obj1.getRadius() + obj2.getRadius();
		return ((dx*dx) + (dy * dy)) < (radii * radii);
	}
	
	/**
	 * 
	 * circleRectangleCollision
	 * 
	 * This function will return whether or not a circle and a rectangle are colliding.
	 * 
	 * @param circle - circle that is colliding
	 * @param rectangle - rectangle that is colliding
	 * @return whether or not they are colliding
	 */
	
	private static boolean circleRectangleCollision(GameObject circle, GameObject rectangle) {
	    float cx = Math.abs(circle.getCenterPosition().x - rectangle.getWorldPosition().x);
	    float cy = Math.abs(circle.getCenterPosition().y - rectangle.getWorldPosition().y);

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
