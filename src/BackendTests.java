import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Scanner;

public class BackendTests {

  /**
   * Integration Test 1 is f g q command but didn't find any song
   */
  @Test
  public void backendTest1() {
    IterableRedBlackTree<Song> tree = new IterableRedBlackTree<>();
    Backend backend = new Backend(tree);
    TextUITester tester = new TextUITester("l\nsongs.csv\nG\n1\n2\nq");
    Scanner in = new Scanner(System.in);
    Frontend test = new Frontend(in, backend);
    test.runCommandLoop();
    String output = tester.checkOutput();
    Assertions.assertTrue(output.contains("No songs found within the range"));

  }

  /**
   * Integration Test 2 is f g command and check the songs in the range of 1 and 200
   */
  @Test
  public void backendTest2() {
    IterableRedBlackTree<Song> tree = new IterableRedBlackTree<>();
    Backend backend = new Backend(tree);
    TextUITester tester = new TextUITester("l\nsongs.csv\nG\n1\n200\nq");
    Scanner in = new Scanner(System.in);
    Frontend test = new Frontend(in, backend);
    test.runCommandLoop();
    String output = tester.checkOutput();
    Assertions.assertTrue(output.contains(
        "No Brainer (feat. Justin Bieber, Chance the Rapper & Quavo)") && output.contains(
        "Call You Mine") && output.contains("Lemon") && output.contains(
        "How Do You Sleep?") && output.contains("Girls Like You (feat. Cardi B)"));

  }

  /**
   * Integration Test 3 is f command and check the songs that speed less than 100
   */
  @Test
  public void backendTest3() {
    IterableRedBlackTree<Song> tree = new IterableRedBlackTree<>();
    Backend backend = new Backend(tree);
    TextUITester tester = new TextUITester("l\nsongs.csv\nf\n100\nq");
    Scanner in = new Scanner(System.in);
    Frontend test = new Frontend(in, backend);
    test.runCommandLoop();
    String output = tester.checkOutput();
    Assertions.assertTrue(
        output.contains("Taki Taki (feat. Selena Gomez, Ozuna & Cardi B)") && output.contains(
            "Familiar") && output.contains("Lemon") && output.contains(
            "Ferrari") && output.contains("Don't Call Me Up"));
  }

  /**
   * Integration Test 4 is d command
   */
  @Test
  public void backendTest4() {
    IterableRedBlackTree<Song> tree = new IterableRedBlackTree<>();
    Backend backend = new Backend(tree);
    TextUITester tester = new TextUITester("l\nsongs.csv\nd\nq");
    Scanner in = new Scanner(System.in);
    Frontend test = new Frontend(in, backend);
    test.runCommandLoop();
    String output = tester.checkOutput();
    Assertions.assertTrue(output.contains("Kissing Strangers") && output.contains(
        "Kills You Slowly") && output.contains("Lemon") && output.contains(
        "I Don't Care (with Justin Bieber)") && output.contains("How Do You Sleep?"));
  }

}

