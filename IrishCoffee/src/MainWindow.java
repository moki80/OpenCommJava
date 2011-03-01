import java.util.ArrayList;
import java.util.LinkedList;

import controlP5.ControlP5;

import processing.core.PApplet;
import processing.core.PFont;

public class MainWindow extends PApplet {

	public static PFont font;
	public static int[] colors = { 0xFF00CCFF, 0xFF00CC99, 0xFF00CC33,
			0xFF0066FF, 0xFF00D699 };
	public static int mainh = 245;
	public static int mainw = 480;
	//ArrayList<Person> people = new ArrayList<Person>();
	
	public static int setupw= 480;
	public static int setuph= 400;
	
	//HanWei
	Tab1 pspace1, pspace2;
	ControlP5 controlP5;

	private Space mainSpace;
	
	public void setup() {
		size(setupw, setuph);
		font = createFont("TAHOMA.TTF", (float) 11.5);
		
		controlP5 = new ControlP5(this);
		
		//NORAS INIT
		ArrayList<Peep> people1= new ArrayList<Peep>();
		people1.add(new Peep(this, this.loadImage("naj.png")));
		people1.add(new Peep(this, this.loadImage("nora.png")));
		ArrayList<Peep> people2= new ArrayList<Peep>();
		people2.add(new Peep(this, this.loadImage("risa.png")));
		people2.add(new Peep(this, this.loadImage("mak.png")));
		people2.add(new Peep(this, this.loadImage("nora.png")));
		pspace1= new PrivateSpace(this, people1, 0, controlP5);
		pspace2= new PrivateSpace(this, people2, setupw/4, controlP5);
		Tab1 chat= new Chat(this, setupw/2, controlP5); 
		
		//HAN WEI
		//controlP5 = new ControlP5(this);
		// create a new instance of the PrivateRoomBtn controller.
		//pspace1 = new PrivateRoomBtn(this, controlP5, "privateRoomBtn1", 0, mainh, (height-mainh)/2, (height-mainh));
		// register the newly created PrivateRoomBtn with controlP5
		controlP5.register(pspace1);
		// create a new instance of the PrivateRoomBtn controller.
		//pspace2 = new PrivateRoomBtn(this, controlP5, "privateRoomBtn2", width-(height-mainh)/2, mainh, (height-mainh)/2, (height-mainh));
		// register the newly created PrivateRoomBtn with controlP5
		controlP5.register(pspace2);	
		controlP5.register(chat);
		
		
		/*people.add(new Person(67, 108, this, 1, loadImage("naj.png"),
				"Najla E.", null));
		people.add(new Person(133, 54, this, 2, loadImage("nora.png"),
				"Nora N.", null));
		people.add(new Person(200, 210, this, 3,
				loadImage("risa.png"), "Risa N.", null));
		people.add(new Person(267, 162, this, 4, loadImage("mak.png"),
				"Makoto B.", null));*/
		

		
		mainSpace = new Space(this, mainh, mainw, 0xFF006699, 255, 0, 0, controlP5, true);
		controlP5.register(mainSpace);
		smooth();
	}

	public void draw() {
		//background(255);
		mainSpace.draw(this);
		
		/*if (!((PrivateSpace)pspace1).isOpen() && !((PrivateSpace)pspace2).isOpen()) {
			background(50);
		}
		else {
			background(0);
		}*/
	}
	
	/**
	 * Look for a SHIFT key Press to begin drawing lasso, and selecting for private space
	 */
	public void keyPressed(){
		if (key == CODED){// Coded keys are ALT, CTRL, SHIFT, UP, DOWN etc....
			if (keyCode == SHIFT && !mainSpace.isCreatingPrivateSpace()){
				mainSpace.beginPrivateSpaceSelection();
				mainSpace.selected = null;
				System.out.println("Selecting For Private Space....");
			}
		}
	}
	
	/**
	 * Look for a SHIFT key release to ask for confirmation for creating private space
	 */
	public void keyReleased(){
		if (key == CODED){ // Coded keys are ALT, CTRL, SHIFT, UP, DOWN etc....
			if (keyCode == SHIFT){
					mainSpace.createNewConfirmBox();
			}
		}
	}

	/**
	 * Create a private space using the people contained in the list people
	 * @param people
	 */
	public void createPrivateSpace(ArrayList<Person> people) {
		System.out.println("Creating Private Space with....");
		//TODO: The real work of creating private space goes here.....
		for (Person p : people){
			System.out.print(p + " | ");
		}
		System.out.println();
	}
}
