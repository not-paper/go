package tech.notpaper.go.board.impl;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

import tech.notpaper.go.board.Vertex;


public class DefaultVertex extends Point implements Vertex {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4566642165438026131L;
	private State state;
	
	public DefaultVertex north;
	public DefaultVertex south;
	public DefaultVertex east;
	public DefaultVertex west;
	
	public DefaultVertex(int x, int y) {
		super(x,y);
		this.state = State.NEUTRAL;
	}
	
	@Override
	public String toString() {
		//return the move like a Go move
		// i.e. A11, C9
		// so take the x and add 97 for the ascii offset to lowercase a
		
		return String.valueOf((char)(((int)this.getX())+97)) + String.valueOf((int)this.getY());
	}
	
	@Override
	public State getState() {
		return this.state;
	}
	
	@Override
	public void setState(State state) {
		this.state = state;
	}
	
	/**
	 * Determine the number of liberties a vertex has by recursively traversing it's
	 * like-color neighbors to find neutral spaces that are touched, i.e. liberties
	 * 
	 * The liberty count for a neutral vertex will be 0
	 * 
	 * For non-neutral vertices opposite the color of the active player
	 * a liberty count of 0 may indicate a captured piece for
	 * the active player
	 * 
	 * @return liberties - The number of liberties this vertex has
	 */
	public int countLiberties() {
		return countLibertiesHelper(new HashSet<>());
	}
	
	protected int countLibertiesHelper(Set<Vertex> visited) {
		visited.add(this);
		
		if (this.getState() == State.NEUTRAL) {
			return 0;
		}
		
		int liberties = 0;
		
		//for each neighbor
		for (DefaultVertex n : new DefaultVertex[] {this.north, this.south, this.east, this.west}) {
			//if that neighbor is not the edge of the board, or we haven't already visited it
			if (n != null && !visited.contains(n)) {
				//determine the state of the neighbor
				switch (n.getState()) {
				case NEUTRAL:
					//if it is neutral, we have a liberty
					liberties += 1;
					break;
				default:
					//if it is the same color, look for more liberties
					if (this.getState() == n.getState()) {
						liberties += n.countLibertiesHelper(visited);
					}
					break;
				}
			}
		}
		
		
		return liberties;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Vertex)) {
			return false;
		}
		
		DefaultVertex other = (DefaultVertex) obj;
		
		if (!(this.getX() == other.getX()
				&& this.getY() == other.getY())) {
			return false;
		}
		
		return this.getState() == other.getState();
	}
	
	private static final int coeff = 3071;
	@Override
	public int hashCode() {
		switch(this.state) {
		case BLACK:
			return (int)(coeff*this.x) + (int)(coeff*this.y);
		case WHITE:
			return (int)(coeff*this.x) + (int)(coeff*this.y);
		case NEUTRAL:
			return (int)(coeff*this.x) + (int)(coeff*this.y);
			default:
				return super.hashCode();
		}
		
	}
}
