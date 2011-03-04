import java.util.ArrayList;

import java.util.LinkedList;

import processing.core.*;
import controlP5.ControlP5;
import controlP5.ControlP5XMLElement;
import controlP5.Controller;
import controlP5.Tab;

public class Space extends Controller {
	// NORA: might not need this for now since I created an ArrayList like this
	// in class MainWindow
	ArrayList<Person> spacePeople = new ArrayList<Person>();
	public Person selected;
	public int h, w, x, y;
	PApplet parent;
	public int color, trans;
	
	private LinkedList<Person> privateSpaceCreationList;
	private boolean creatingPrivateSpace = false;
	public ConfirmBox confirmingPrivateSpace = null;
	

	
	PGraphics selectionLine;

	public Space(PApplet parent, int h, int w, int color, int trans, int x, int y, ControlP5 theControlP5 /*Han-Wei: Feb 26 Discard this: boolean show, */) {
		super(theControlP5, (Tab) (theControlP5.getTab("default")), "name",	x, y, w, h);
		
		this.parent = parent;
		this.h = h;
		this.w = w;
		this.x = x;
		this.y = y;
		this.color = color;
		this.trans = trans;
		//parent.registerDraw(this);
		spacePeople.add(new Person(67, 108, parent, 1, "naj.png","Najla E.", this));
		spacePeople.add(new Person(133, 54, parent, 2, "nora.png","Nora N.", this));
		spacePeople.add(new Person(200, 210, parent, 3,"risa.png", "Risa N.", this));
		spacePeople.add(new Person(267, 162, parent, 4,"mak.png","Makoto B.", this)); 
		
		for (Person p : MainWindow.people){//For each person p in MainWindow.people			
			p.setSpace(this);
		}
//		for (int i = 0; i < MainWindow.people.size(); i++) {
//			MainWindow.people.get(i).setSpace(this);
//		}
	}

	public void draw(PApplet parent) {
		
		parent.pushMatrix();
		parent.translate(position().x(), position().y());		
		
		parent.rectMode(PConstants.LEFT);
		parent.fill(color, trans);
		parent.rect(0, 0, w, h);
		
		if (this.isCreatingPrivateSpace()) {
			// draw lasso if selecting people for private space
			if (parent.mousePressed) {
				selectionLine.stroke(255);
				selectionLine.strokeWeight(6);
				selectionLine.beginDraw();
				selectionLine.line(parent.mouseX, parent.mouseY, parent.pmouseX, parent.pmouseY);
				selectionLine.endDraw();
			}
			parent.image(selectionLine, 0, 0);
		}
		
		for (Person p : this.spacePeople){
			p.draw();
		}
		
		if (confirmingPrivateSpace != null){
			confirmingPrivateSpace.draw();
		}
		parent.popMatrix();
	}
	
	/**
	 * Clear list of people selected for private space, Clear lasso image, and
	 * set to "creating private space" mode
	 */
	public void beginPrivateSpaceSelection() {
		privateSpaceCreationList = new LinkedList<Person>();
		selectionLine = parent.createGraphics(w, h, PApplet.P2D);
		creatingPrivateSpace = true;
	}

	/**
	 * True if in "creating private space" mode
	 * 
	 * @return
	 */
	public boolean isCreatingPrivateSpace() {
		return creatingPrivateSpace;
	}

	/**
	 * Add the person to list of people to put in private space upon completion
	 * 
	 * @param person
	 */
	public void addToPrivateSpace(Person person) {
		if (!privateSpaceCreationList.contains(person)) {
			privateSpaceCreationList.add(person);
		}
	}

	/**
	 * Remove all from list of people pending to be placed in private space
	 */
	public void clearPrivateSpaceSelections() {
		for (Person p : spacePeople) {
			p.selectedForPrivateSpace = false;
		}
	}

	/**
	 * remove person from list of people pending placement in private space
	 * 
	 * @param person
	 */
	public void removeFromPrivateSpace(Person person) {
		privateSpaceCreationList.remove(person);
	}

	/**
	 * If the box asking to confirm the creation of a new space is not showing,
	 * create and show it
	 */
	public void createNewConfirmBox() {
		if (this.confirmingPrivateSpace == null) {
			if (this.privateSpaceCreationList.size() >= 1){
				confirmingPrivateSpace = new ConfirmBox(this, this.w / 2 - 45, 10);
				confirmingPrivateSpace.show();
			}
			else{
				cancelPrivateSpace();
			}
		}
		
	}

	/**
	 * User confirmed private space: so do the work
	 */
	public void confirmPrivateSpace() {
		if (confirmingPrivateSpace != null) {
			confirmingPrivateSpace = null;
		}
		if (parent instanceof MainWindow)
			((MainWindow)parent).createPrivateSpace(new ArrayList<Person>(privateSpaceCreationList));
		clearPrivateSpaceSelections();
		creatingPrivateSpace = false;
	}

	/**
	 * Terminate creation of private space.
	 */
	public void cancelPrivateSpace() {
		if (confirmingPrivateSpace != null) {
			confirmingPrivateSpace = null;
		}
		clearPrivateSpaceSelections();
		creatingPrivateSpace = false;
	}

	@Override
	public void addToXMLElement(ControlP5XMLElement arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValue(float arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
}

// text outline
// color scheme
// initial configuration of people
// images underneath other images
// take whole name