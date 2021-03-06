package tech.notpaper.go.messaging.entities.simple;

import tech.notpaper.go.messaging.entities.complex.ListEntity;

public interface SimpleEntity extends ListEntity {
	public static boolean isSimpleEntity(String s) {
		try {
			//in order to determine if a String can be a SimpleEntity,
			//we need only know that it can be parsed as a GoString
			new GoString(s);
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}
	
	public Type getType();
	
	public static enum Type {
		INT, FLOAT, STRING, VERTEX, COLOR, MOVE, BOOLEAN;
	}
}
