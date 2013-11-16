package com.jantox.rvtools;

public class Decoration {

	private Vector3f pos;
	private Model model;
	private float angle;
	
	public Decoration(Model m, Vector3f pos, float angle) {
		this.model = m;
		this.pos = pos;
		this.angle = angle;
	}
	
	public void render(Renderer renderer) {
		this.model.render(renderer, pos, new Vector3f(0, 1, 0), angle);
	}
	
}
