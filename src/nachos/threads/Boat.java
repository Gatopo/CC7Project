package nachos.threads;
import nachos.ag.BoatGrader;

public class Boat
{
    static BoatGrader bg;

    private static int children_on_Oahu;
    private static int children_on_Molokai;
    private static int adults_on_Oahu;
    private static int adults_on_Molokai;
    private static int people_on_the_boat;
    private static int childrenFromBoat;
    private static boolean boat_available;
    private static boolean go_back;
    private static Lock oahu = new Lock();
    private static Lock molokai = new Lock();
    private static Condition island_oahu = new Condition(oahu);
    private static Condition island_molokai = new Condition(molokai);
    private static Condition children_first = new Condition(oahu);
    private static Condition on_boat = new Condition(oahu);
    private static Semaphore doneSem = new Semaphore(0);

    public static void selfTest()
    {
	BoatGrader b = new BoatGrader();
	
	    System.out.println("\n ***Testing Boats with only 2 children***");
	    begin(3, 2, b);

//	    System.out.println("\n ***Testing Boats with 2 children, 1 adult***");
//     	begin(1, 2, b);

//     	System.out.println("\n ***Testing Boats with 3 children, 3 adults***");
//      begin(3, 3, b);
    }

    public static void begin( int adults, int children, BoatGrader b )
    {
    	// Store the externally generated autograder in a class
    	// variable to be accessible by children.
    	bg = b;

        children_on_Oahu = children;
        adults_on_Oahu = adults;
        children_on_Molokai = 0;
        adults_on_Molokai = 0;
        boat_available = true;
        go_back = false;
        
       	// Create threads here. See section 3.4 of the Nachos for Java
    	// Walkthrough linked from the projects page.
    	
    	/*
	    Runnable r = new Runnable() {
    	    public void run() {
                    SampleItinerary();
                }
            };
            KThread t = new KThread(r);
            t.setName("Sample Boat Thread");
            t.fork();*/

        for (int i = 0; i < children; i++) {
            new KThread(
                new Runnable () {
                    public void run() {
                        ChildItinerary(); 
                    } 
                }
            ).setName("c" + i).fork();
        }

        for (int i = 0; i < adults; i++) {
            new KThread(
                new Runnable () {
                    public void run() {
                        AdultItinerary(); 
                    } 
                }
            ).setName("a" + i).fork();
        }
        doneSem.P();
    }

    static void AdultItinerary()
    {
	/* This is where you should put your solutions. Make calls
	   to the BoatGrader to show that it is synchronized. For
	   example:
	       bg.AdultRowToMolokai();
	   indicates that an adult has rowed the boat across to Molokai
	*/
        oahu.acquire();
        while(children_on_Oahu > 1 || !boat_available){
            children_first.sleep();
        }
        adults_on_Oahu--;
        boat_available = false;
        oahu.release();
        bg.AdultRowToMolokai();
        molokai.acquire();
        adults_on_Molokai++; 
        island_molokai.wake();  
        molokai.release();
    }

    static void ChildItinerary()
    {
        while(children_on_Oahu + adults_on_Oahu > 1){
            oahu.acquire();
            if(children_on_Oahu == 1){
                children_first.wake();
            }
            while(people_on_the_boat > 1 || !boat_available){
                island_oahu.sleep();
            }
            if (people_on_the_boat == 0) { 
                people_on_the_boat++;
                island_oahu.wake();
                on_boat.sleep();
                bg.ChildRideToMolokai();
                on_boat.wake();
            } else {
                people_on_the_boat++;
                on_boat.wake();
                bg.ChildRowToMolokai();
                on_boat.sleep();
            }
            people_on_the_boat--;
            children_on_Oahu--;
            boat_available=false;
            oahu.release();
            molokai.acquire();
            children_on_Molokai++;
            childrenFromBoat++;
            if(childrenFromBoat == 1){
                island_molokai.sleep();
            }
            children_on_Molokai--;
            childrenFromBoat = 0;
            molokai.release();
            bg.ChildRowToOahu();
            oahu.acquire();
            children_on_Oahu++;
            boat_available = true;
            oahu.release();
        }
        oahu.acquire();
        children_on_Oahu--;
        oahu.release();
        bg.ChildRowToMolokai();
        molokai.acquire();
        children_on_Molokai++;
        molokai.release();
        //System.out.println("Information");
        System.out.println("+----------+----------+-----------+");
        System.out.println("|          |   Oahu   |   Molokai |");
        System.out.println("+----------+----------+-----------+");
        System.out.println("| Children | " + children_on_Oahu + "        | " + children_on_Molokai + "         |");
        System.out.println("+----------+----------+-----------+");
        System.out.println("| Adults   | " + adults_on_Oahu + "        | " + adults_on_Molokai+ "         |");
        System.out.println("+----------+----------+-----------+");
        doneSem.V();
    }

    static void SampleItinerary()
    {
	// Please note that this isn't a valid solution (you can't fit
	// all of them on the boat). Please also note that you may not
	// have a single thread calculate a solution and then just play
	// it back at the autograder -- you will be caught.
	System.out.println("\n ***Everyone piles on the boat and goes to Molokai***");
	bg.AdultRowToMolokai();
	bg.ChildRideToMolokai();
	bg.AdultRideToMolokai();
	bg.ChildRideToMolokai();
    }
    
}
