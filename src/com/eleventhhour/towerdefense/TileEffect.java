package com.eleventhhour.towerdefense;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import com.eleventhhour.towerdefense.Level.TileType;
import com.eleventhhour.towerdefense.TileEffect.EffectType;

public class TileEffect implements ITileEffect {
	private static long lastID;
	private long ID;
	protected Tile tile; //the tile effect can get all positioning from it's parent tile
	protected float modifier;
	protected EffectType effectType;
	protected int totalDuration;
	protected int currentDuration;
	
	public enum EffectType {
		DAMAGE,SPEED		
	}
	
	public TileEffect(EffectType effectType, float modifier, int totalDuration, Tile tile) {
		this.ID = TileEffect.lastID++;
		this.effectType = effectType;
		this.modifier = modifier;
		this.totalDuration = totalDuration;
		this.tile = tile;
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, GameplayState gs, int delta) {
		this.currentDuration += delta;
	}

	@Override
	public void render(GameContainer gc, Graphics g, Vector2f offset) {
		if (this.effectType == EffectType.DAMAGE)
			g.setColor(Color.red);
		else
			g.setColor(new Color(0, 0, 255));
		
		Vector2f worldPos = this.tile.getWorldPosition();
		
		g.fillRect(worldPos.x, worldPos.y , this.tile.getWidth(), this.tile.getHeight());
	}

	@Override
	public long getId() {
		return this.ID;
	}

	@Override
	public Vector2f getWorldPosition() {
		return this.tile.getWorldPosition();
	}

	@Override
	public Vector2f getCenterPosition() {
		return this.tile.getCenterPosition();
	}

	@Override
	public Vector2f getTilePosition() {
		return this.tile.getTilePosition();
	}

	@Override
	public int getWidth() {
		return this.tile.getWidth();
	}

	@Override
	public int getHeight() {
		return this.tile.getHeight();
	}

	@Override
	public int getRadius() {
		return this.tile.getRadius();
	}

	@Override
	public float getModifier() {
		return this.modifier;
	}

	@Override
	public EffectType getEffectType() {
		return this.effectType;
	}

	@Override
	public boolean isOver() {
		return this.currentDuration >= this.totalDuration;
	}

}
