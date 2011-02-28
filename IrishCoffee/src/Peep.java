import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;


public class Peep {
	PApplet parent;
	float x, y;
	int w, h;
	PImage icon;
	
	public Peep(PApplet parent, PImage image){
		this.parent=parent;
		icon=image;
		parent.registerDraw(this);
	}
	
	public void setXY(int x, int y){
		this.x=x;
		this.y=y;
	}
	
	public void draw() {
		parent.imageMode(PConstants.CENTER);
		parent.image(icon, x, y);
		parent.fill(255);
		parent.stroke(0);
		//parent.textFont(MainWindow.font);
		parent.textAlign(PConstants.CENTER);
//		parent.text(name, x, y + halfH + 12);
	}

}
