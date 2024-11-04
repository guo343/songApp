import java.io.IOException;
import java.util.List;
import java.util.Scanner;


public class Frontend implements FrontendInterface {

  private Scanner scnr;
  private BackendInterface backend;

  public Frontend(Scanner scnr, BackendInterface backend) {
    this.scnr = scnr;
    this.backend = backend;
  }

  /**
   * Repeatedly gives the user an opportunity to issue new commands until they select Q to quit.
   * Uses the scanner passed to the constructor to read user input.
   */
  @Override
  public void runCommandLoop() {
    String command = "";

    while (!command.equalsIgnoreCase("Q")) {
      System.out.println("Enter a command (or Q to quit):");
      displayMainMenu();
      command = scnr.nextLine().trim();

      if (command.equalsIgnoreCase("Q")) {
        System.out.println("Exiting...");
      } else {
        System.out.println("You entered: " + command);
      }

      if (command.equalsIgnoreCase("L")) {
        System.out.println("==================");
        loadFile();
      } else if (command.equalsIgnoreCase("G")) {
        System.out.println("==================");
        getSongs();
      } else if (command.equalsIgnoreCase("F")) {
        System.out.println("==================");
        setFilter();
      } else if (command.equalsIgnoreCase("D")) {
        System.out.println("==================");
        displayTopFive();
      }
    }
  }

  /**
   * Displays the menu of command options to the user. Giving the user the instructions of entering
   * L, G, F, D, or Q (case insensitive) to load a file, get songs, set filter, display the top
   * five, or quit.
   */
  @Override
  public void displayMainMenu() {

    System.out.println("Please enter a command: ");
    System.out.println("L: load a file");
    System.out.println("G: get songs");
    System.out.println("F: set filter");
    System.out.println("D: Display the top five");
    System.out.println("Q: to quit");
    System.out.print("Enter a command: ");

  }

  /**
   * Provides text-based user interface for prompting the user to select the csv file that they
   * would like to load, provides feedback about whether this is successful vs any errors are
   * encountered. [L]oad Song File
   *
   * When the user enters a valid filename, the file with that name should be loaded. Uses the
   * scanner passed to the constructor to read user input and the backend passed to the constructor
   * to load the file provided by the user. If the backend indicates a problem with finding or
   * reading the file by throwing an IOException, a message is displayed to the user, and they will
   * be asked to enter a new filename.
   */
  @Override
  public void loadFile() {
    String file = "";
    boolean fileLoaded = false;

    while (!fileLoaded) {

      System.out.println("Please enter the file that you want to load");
      file = scnr.nextLine().trim();

      try {

        // use backend to load the file
        backend.readData(file);
        System.out.println("File loaded successfully");
        System.out.println("==================");
        break;

      } catch (IOException e) {
        System.out.println("Cannot load the file.");
        System.out.println(e.getMessage());
      }
    }
  }

  /**
   * Provides text-based user interface and error handling for retrieving a list of song titles that
   * are sorted by Danceability. The user should be given the opportunity to optionally specify a
   * minimum and/or maximum Danceability to limit the number of songs displayed to that range. [G]et
   * Songs by Danceability
   *
   * When the user enters only two numbers (pressing enter after each), the first of those numbers
   * should be interpreted as the minimum, and the second as the maximum Danceability. Uses the
   * scanner passed to the constructor to read user input and the backend passed to the constructor
   * to retrieve the list of sorted songs.
   */
  @Override
  public void getSongs() {
    Integer maxDance = null;
    Integer minDance = null;
    boolean vInput = false;

    while (!vInput) {
      try {
        System.out.println("Please enter the minimum dancebility");
        String minIn = scnr.nextLine().trim();

        if (!minIn.isEmpty()) {
          minDance = Integer.parseInt(minIn);
        }

        System.out.println("Please enter the maximum dancebility");
        String maxIn = scnr.nextLine().trim();

        if (!maxIn.isEmpty()) {
          maxDance = Integer.parseInt(maxIn);
        }

        vInput = true;

        List<String> songs = backend.getRange(minDance, maxDance);

        if (songs.isEmpty()) {
          System.out.println("No songs found within the range");
        } else {
          System.out.println("Songs sorted by dancebility");
          for (String song : songs) {
            System.out.println(song);
          }
        }
        System.out.println("==================");
      } catch (Exception e) {

        System.out.println("Invalid input. Please enter a valid number");

      }
    }
  }

  /**
   * Provides text-based user interface and error handling for setting a filter threshold. This and
   * future requests to retrieve songs will will only return the titles of songs that are smaller
   * than the user specified Speed. The user should also be able to clear any previously specified
   * filters. [F]ilter Songs by Speed
   *
   * When the user enters only a single number, that number should be used as the new filter
   * threshold. Uses the scanner passed to the constructor to read user input and the backend passed
   * to the constructor to set the filters provided by the user and retrieve songs that maths the
   * filter criteria.
   */
  @Override
  public void setFilter() {

    System.out
        .println("Please enter a speed threshold to filter songs by or C to clear the filters");

    Integer threshold = null;

    boolean vInput = false;

    while (!vInput) {
      String input = scnr.nextLine();
      if (input.equalsIgnoreCase("C")) {
        threshold = null;
        // clear filter
        backend.setFilter(threshold);

        System.out.println("Filter cleared");
        // set vinput = true, to break out of the while loop
        vInput = true;
      } else {
        try {
          // set the threshold to the input
          threshold = Integer.parseInt(input);

          // set filter through backend using array list
          List<String> filteredSongs = backend.setFilter(threshold);

          // use backend to get the songs that are filtered and store into list

          System.out.println("Filtered songs (speed < " + threshold + "):");

          // print out the filtered songs using a for loop
          for (String song : filteredSongs) {
            System.out.println(song);
          }
          vInput = true;
          System.out.println("==================");

        } catch (Exception e) {

          System.out.println("Invalid input");
          System.out.println("Please enter a integer");
          continue;

        }
      }
    }
  }

  /**
   * Displays the titles of up to five of the most Recent songs within the previously set
   * Danceability range and smaller than the specified Speed. If there are no such songs, then this
   * method should indicate that and recommend that the user change their current range or filter
   * settings. [D]isplay five most Recent
   *
   * The user should not need to enter any input when running this command. Uses the backend passed
   * to the constructor to retrieve the list of up to five songs.
   */
  @Override
  public void displayTopFive() {

    List<String> top5 = backend.fiveMost();

    if (top5.isEmpty()) {
      System.out.println("No songs found with the current Danceability range or Speed filter.");
      System.out.println("Please edit your speed or dancebility range.");
    } else {
      System.out.println("Top 5 songs are: ");
      for (String songs : top5) {
        System.out.println(songs);
      }
    }
    System.out.println("==================");
  }

}
