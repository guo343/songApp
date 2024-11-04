import org.junit.jupiter.api.Test;
import java.util.Scanner;
import org.junit.jupiter.api.Assertions;

public class FrontendTests {

  /**
   * Test 1 is G command
   */
  @Test
  public void roleTest1() {
    boolean testPassed = false;
    // test 1: G command
    // Create a iterable tree with type song
    IterableSortedCollection<Song> tree = new Tree_Placeholder();
    // create a backend interface varible that has the placeholder tree
    BackendInterface backend = new Backend_Placeholder(tree);
    // create a textuttester with G, 1, 2, and q
    // this travels to the get song method and then quit when its done
    TextUITester tester = new TextUITester("G\n1\n2\nq");
    // create a new scanner object
    Scanner in = new Scanner(System.in);
    // create new front end interface
    FrontendInterface test1 = new Frontend(in, backend);

    // run the command loop which will prompt for input
    test1.runCommandLoop();
    String output = tester.checkOutput();

    Assertions.assertTrue(output.contains("BO$$"), "Test Passed!");

    // checks to see if output contains BO$$ if so set the testPassed variable to true
//    if (output.contains("BO$$")) {
  //    testPassed = true;
   // } else {
     // testPassed = false;
   // }

    // if statement saying if test passe is true print true
   // if (testPassed) {
     // System.out.println("Passed");
     // return;
   // } else {
     // System.out.println("Failed");
     // return;
   // }

  }

  /**
   * Test 2 is L and Q command
   */
  @Test
  public void roleTest2() {

    boolean testPassed = false;
    // test 2: L and Q test
    // create a new iterable tree
    IterableSortedCollection<Song> tree = new Tree_Placeholder();
    // create a new backend interface placeholder with the tree
    BackendInterface backend = new Backend_Placeholder(tree);
    // create a tester that goes to the load command and input a file, then quit afterward
    TextUITester tester = new TextUITester("L\nfile\nq");
    // create a new scanner object
    Scanner in = new Scanner(System.in);
    // creates a new front end interface for testing
    FrontendInterface test1 = new Frontend(in, backend);

    // run the command loop
    test1.runCommandLoop();
    String output = tester.checkOutput();

    Assertions.assertTrue(output.contains("File loaded successfully"), "Test Passed!");
    // if the output has the given line set testpassed to true
   // if (output.contains("File loaded successfully")) {
     // testPassed = true;
   // } else {
     // testPassed = false;
   // }

    // if statement saying if test passe is true print true
   // if (testPassed) {
    //  System.out.println("Passed");
   // } else {
     // System.out.println("Failed");
   // }
  }

  /**
   * Test 3 is F and D command
   */
  @Test
  public void roleTest3() {
    boolean testPassed = false;
    // test 2: F and D test
    // creataing and setting up the test
    IterableSortedCollection<Song> tree = new Tree_Placeholder();
    BackendInterface backend = new Backend_Placeholder(tree);
    // Goes into filter and set speed to 5, then goes and displays the top 5 songs
    TextUITester tester = new TextUITester("F\n5\nd\nq");
    Scanner in = new Scanner(System.in);
    FrontendInterface test1 = new Frontend(in, backend);

    test1.runCommandLoop();
    String output = tester.checkOutput();
    //  JUnit test to check to see if output contains
    Assertions.assertTrue(output.contains("A L I E N S"), "Test Passed!");
   // if (output.contains("A L I E N S")) {
     // testPassed = true;
   // } else {
     // testPassed = false;
   // }

   // if (output.contains("A L I E N S\r\n" + "BO$$\r\n" + "Cake By The Ocean")) {
     // testPassed = true;
   // } else {
     // testPassed = false;
   // }

   // if (testPassed) {
     // System.out.println("Passed");
   // } else {
     // System.out.println("Failed");
   // }
  }
}
