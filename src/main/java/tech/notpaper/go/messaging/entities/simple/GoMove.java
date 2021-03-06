package tech.notpaper.go.messaging.entities.simple;

public class GoMove implements SimpleEntity {
	
	private Color color;
	private Vertex vertex;
	private boolean pass = false;
	
	public GoMove(Color color, Vertex vertex) {
		this.color = color;
		this.vertex = vertex;
	}
	
	public GoMove(String input) {
		String[] tokens = input.split(" ");
		this.color = Color.fromString(tokens[0]);
		if (tokens[1].toLowerCase().equals("pass")) {
			pass = true;
			this.vertex = new Vertex("a1");
		} else {
			this.vertex = new Vertex(tokens[1]);
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(color);
		sb.append(" ");
		sb.append(pass ? "pass" : vertex);
		
		return sb.toString();
	}
	
	public Color getColor() {
		return color;
	}
	
	public Vertex getVertex() {
		return vertex;
	}
	
	@Override
	public Type getType() {
		return SimpleEntity.Type.MOVE;
	}
}
