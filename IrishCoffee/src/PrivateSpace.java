import java.awt.event.MouseEvent;
import java.util.ArrayList;

import processing.core.PApplet;
import controlP5.ControlP5;

public class PrivateSpace extends Tab1 {
	protected ArrayList<Person> people;
	protected ControlP5 c;
	protected PApplet parent;
//	private boolean isOpen;					// Han-Wei: Feb 26: Discard this
	protected PrivateViewScreen privateViewScreen;	// Han-Wei: Feb 26: Such screen dims the entire background when a private room shows up
//	int oldX, oldY;							// Han-Wei: Feb 26: Discard this
//	boolean isMoved;						// Han-Wei: Feb 26: Discard this
	protected boolean locked;							// Han-Wei: Feb 26: Add this
	protected float difx, dify;						// Han-Wei: Feb 26: Add this
	protected ArrayList<Peep> peeps; // NORA
	protected Space privateRoom;

	public PrivateSpace(PApplet theParent, ArrayList<Person> group, int x, ControlP5 /*Han-Wei: Feb 26 Bug! Correct this*/theControlP5) {
		super(theParent, x, MainWindow.mainh, MainWindow.setupw / 4, MainWindow.setuph - MainWindow.mainh, /*Han-Wei: Feb 26 Bug! Correct this*/ theControlP5);
		people = group;
		this.parent = theParent;
		this.c = new ControlP5(this.parent);

		privateRoom = new Space(parent, 200, 200, 255, 255,2 * (x - (parent.width) / 2), -2 * y, c);
		c.register(privateRoom);

		// parent.registerDraw(this);
//		dothis();
		
		privateViewScreen = new PrivateViewScreen(parent, c, "PrivateViewScreen", 0, 0, MainWindow.setupw, MainWindow.setuph);
		this.c.register(privateViewScreen);
		
		peeps = new ArrayList<Peep>();
		createPeeps(); // changed by NORA
		parent.registerMouseEvent(this);
	}
	
	/**
	 * 
	 * Get the people in the private space. Get each person's images and names,
	 * and assign them a position. Put this information in a Peep
	 * 
	 * -NORA
	 * 
	 */
	public void createPeeps() {
		for (int i = 0; i < people.size(); i++) {
			peeps.add(new Peep(parent, people.get(i).getImage(), people.get(i)
					.getName(), x + w / 3, (y + tabh )+ (h - tabh) / (people.size() + 1)
					* (i + 1)));
		}
	}

//	public void dothis() {
//		Peep p;
//		int x, y;
//		for (int i = 0; i < people.size(); i++) {
//			p = people.get(i);
//			if (i % 2 == 0)
//				x = super.x + p.icon.width / 2;
//			else
//				x = super.x + super.w / 2 + p.icon.width / 2;
//			if (i < 2)
//				y = MainWindow.mainh + tabh + p.icon.height / 2;
//			else
//				y = MainWindow.mainh + tabh + p.icon.height / 2 + 54;
//			p.setXY(x, y/*-100*/);
//		}
//	}

	/**
	 * 
	 * Draw: PrivateSpace, Title Tab, Up to 3 icons, Icons with names underneath
	 * Animate: Blossoming out of private rooms
	 * 
	 */
	public void draw(PApplet parent) {
		// Choose color of tab, and draw tab title
		if (number == 0)
			parent.fill(0xFFFF9900);
		else if (number == 1)
			parent.fill(0xFF800080);
		else
			parent.fill(0xFF00FF00);
		parent.stroke(MainWindow.colors[1]);
		parent.rect(x, y, w, tabh);

//		Han-Wei: Feb 26: Add this line
		parent.pushMatrix();
		parent.translate(position().x(), position().y());
		
//		Han-Wei: Feb 26: Correct this line
		parent.rect(0, 0, w, tabh);
		parent.fill(value);
		parent.rect(0, tabh, w, h - tabh);


		// draw the background of the controller.
		if(getIsInside()) {
			parent.fill(150);
		} 
		else {
			parent.fill(100);
		}
		parent.rect(0,0,width,height);
		parent.popMatrix();	
		
		// animate the private room
		if (privateViewScreen.isOpen()) {
//			Han-Wei: Feb 26: Discard this line
//			System.out.println("blossoming");

			
//			Han-Wei: Feb 26: Discard this line
//			privateRoom.draw(parent);
//			privateViewScreen.draw(parent);
			
			privateViewScreen.position().x = 0;
			privateViewScreen.position().y = 0;			
		}
		else{
			privateViewScreen.position().x = -privateViewScreen.getWidth();
			privateViewScreen.position().y = -privateViewScreen.getHeight();
		}
	}
	
	// Han-Wei: Feb 26: Add this
	public void mouseEvent(MouseEvent event) {
		switch (event.getID()) {
		case MouseEvent.MOUSE_CLICKED:
			if(getIsInside()) {
//				isOpen = !isOpen;
				privateViewScreen.setIsOpen(!privateViewScreen.isOpen());
			}
			break;
		case MouseEvent.MOUSE_PRESSED:
			if(getIsInside()) {
			    locked = true; 
			    parent.fill(255, 255, 255);					
			} else {
				locked = false;
			}
			difx = parent.mouseX - position().x;
			dify = parent.mouseY - position().y;
			break;
		case MouseEvent.MOUSE_DRAGGED:
			  if(locked) {
				  position().x = parent.mouseX - difx; 
				  position().y = parent.mouseY - dify; 
			  }
			  break;
		case MouseEvent.MOUSE_RELEASED:
			locked = false;
			break;
		}		
	}	
	
}
