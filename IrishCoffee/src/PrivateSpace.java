import java.awt.event.MouseEvent;
import java.util.ArrayList;

import controlP5.ControlP5;
import controlP5.ControlP5XMLElement;
import processing.core.PApplet;

public class PrivateSpace extends Tab1 {
	ArrayList<Peep> people;
	ControlP5 c;
	PApplet parent;
	private boolean isOpen;
	Space privateRoom;

	public PrivateSpace(PApplet parent, ArrayList<Peep> group, int x,
			ControlP5 c) {
		super(parent, x, MainWindow.mainh, MainWindow.setupw / 4,
				MainWindow.setuph - MainWindow.mainh, c);
		people = group;
		this.parent = parent;
		this.c = new ControlP5(this.parent);

		privateRoom = new Space(parent, 200, 200, 255, 255,
				2 * (x - (parent.width) / 2), -2 * y, c, false);
		c.register(privateRoom);

		// parent.registerDraw(this);
		dothis();
		parent.registerMouseEvent(this);
	}

	public void dothis() {
		Peep p;
		int x, y;
		for (int i = 0; i < people.size(); i++) {
			p = people.get(i);
			if (i % 2 == 0)
				x = super.x + p.icon.width / 2;
			else
				x = super.x + super.w / 2 + p.icon.width / 2;
			if (i < 2)
				y = MainWindow.mainh + tabh + p.icon.height / 2;
			else
				y = MainWindow.mainh + tabh + p.icon.height / 2 + 54;
			p.setXY(x, y/*-100*/);
		}
	}

	public void draw(PApplet parent) {
		if (number == 0)
			parent.fill(0xFFFF9900);
		else if (number == 1)
			parent.fill(0xFF800080);
		else
			parent.fill(0xFF00FF00);
		parent.stroke(MainWindow.colors[1]);
		parent.rect(x, y, w, tabh);
		parent.fill(value);
		parent.rect(x, y + tabh, w, h - tabh);

		// animate the private room
		if (isOpen) {
			System.out.println("blossoming");
			privateRoom.position().x += (100 - privateRoom.position().x) * 0.2;
			privateRoom.position().y += (100 - privateRoom.position().y) * 0.2;
			privateRoom.draw(parent);
		}

		if (!isOpen) {
			privateRoom.position().x += (2 * privateRoom.x - privateRoom
					.position().x) * 0.2;
			privateRoom.position().y += (2 * privateRoom.y - privateRoom
					.position().y) * 0.2;
		}
	}

	public void mouseEvent(MouseEvent event) {
		if (getIsInside()) {
			switch (event.getID()) {
			case MouseEvent.MOUSE_PRESSED:
				isOpen = !isOpen;
				privateRoom.isShown = !privateRoom.isShown;
				break;
			}
		}
	}

	public boolean isOpen() {
		return isOpen;
	}

	// set the value of isOpen
	public void setIsOpen(boolean theIsOpen) {
		isOpen = theIsOpen;
	}

}
