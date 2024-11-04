import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * The class can read songs data, get a range or set a filter to the songs. To sum up, it can deal
 * with data. For example, if the user wants to use a filter or set the max or min to find the sons
 * they want, this class would fulfill the requests
 *
 * @author Yuhan Guo
 */
public class Backend implements BackendInterface {
  IterableRedBlackTree<Song> tree;
  //use the filterValue to set a filter
  private Integer filterValue = null;

  /**
   * create a backend instance
   *
   * @param tree the tree that would be used
   */
  public Backend(IterableSortedCollection<Song> tree) {
    this.tree = (IterableRedBlackTree<Song>) tree;
  }

  /**
   * Loads data from the .csv file referenced by filename.
   *
   * @param filename is the name of the csv file to load data from
   * @throws IOException when there is trouble finding/reading file
   */
  @Override
  public void readData(String filename) throws IOException {
    File file = new File(filename);
    try (Scanner scnr = new Scanner(file)) {
      if (!scnr.hasNextLine()) {
        return; // Empty file, nothing to process
      }

      // Reading header line and mapping column indices
      String[] headers = scnr.nextLine().split(",");
      int[] indices = getHeaderIndices(headers,
          new String[] {"title", "artist", "top genre", "year", "bpm", "nrgy", "dnce", "dB",
              "live"});

      // Processing each row containing song data
      while (scnr.hasNextLine()) {
        String[] parts = parseLine(scnr.nextLine());

        Song newSong = new Song(parts[indices[0]], // title
            parts[indices[1]], // artist
            parts[indices[2]], // genre
            Integer.parseInt(parts[indices[3]]), // year
            Integer.parseInt(parts[indices[4]]), // bpm
            Integer.parseInt(parts[indices[5]]), // energy
            Integer.parseInt(parts[indices[6]]), // danceability
            Integer.parseInt(parts[indices[7]]), // loudness
            Integer.parseInt(parts[indices[8]])  // liveness
        );

        //TODO REMOVE
        System.out.println(newSong.getTitle());
        tree.insert(newSong); // Insert into the tree
      }
    } catch (FileNotFoundException e) {
      System.out.println("File not found");
    }
  }

  // Helper method to map headers to indices
  private int[] getHeaderIndices(String[] headers, String[] requiredHeaders) throws IOException {
    int[] indices = new int[requiredHeaders.length];
    Arrays.fill(indices, -1);

    for (int i = 0; i < headers.length; i++) {
      for (int j = 0; j < requiredHeaders.length; j++) {
        if (headers[i].equals(requiredHeaders[j])) {
          indices[j] = i;
        }
      }
    }

    for (int index : indices) {
      if (index == -1) {
        throw new IOException("Missing required header");
      }
    }

    return indices;
  }

  // Helper method to handle potential quoted commas
  private String[] parseLine(String line) {
    return line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
  }


  /**
   * Retrieves a list of song titles from the tree passed to the contructor.
   *
   * @param low  is the minimum Danceability of songs in the returned list
   * @param high is the maximum Danceability of songs in the returned list
   * @return the list of songs that fit the range
   */
  @Override
  public List<String> getRange(Integer low, Integer high) {

    //if high equals null then no max value
    if (high == null) {
      tree.setIteratorMax(null);
    } else {
      //if high is existed then set it
      tree.setIteratorMax(new Comparable<Song>() {
        @Override
        public int compareTo(Song o) {
          return high - o.getDanceability();
        }
      });
    }

    //if low equals null then no low value
    if (low == null) {
      tree.setIteratorMin(null);
    } else {
      //if low is existed then set it
      tree.setIteratorMin(new Comparable<Song>() {
        @Override
        public int compareTo(Song o) {
          return low - o.getDanceability();
        }
      });
    }

    //if set the filter before then use the value. if not then use null filter
    return setFilter(filterValue);
  }

  /**
   * Retrieves a list of song titles that have a Speed that is smaller than the specified
   * threshold.
   *
   * @param threshold filters returned song titles to only include songs that have a Speed that is
   *                  smaller than this threshold.
   * @return the list of songs that fit the filter
   */
  @Override
  public List<String> setFilter(Integer threshold) {
    List<String> toReturn = new ArrayList<>();
    Iterator<Song> iterator = tree.iterator();
    this.filterValue = threshold;

    //no filter then the reset filterValue to nothing
    if (filterValue == null) {
      while (iterator.hasNext()) {
        toReturn.add(iterator.next().getTitle());
      }
      return toReturn;
    } else {
      while (iterator.hasNext()) {
        Song song = iterator.next();
        //if the speed is bigger than the threshold then keep it
        if (song.getBPM() < this.filterValue) {
          toReturn.add(song.getTitle());
        }
      }
    }

    return toReturn;
  }

  /**
   * This method returns a list of song titles representing the five most Recent songs that both
   * fall within any attribute range specified by the most recent call to getRange, and conform to
   * any filter set by the most recent call to setFilter.
   *
   * @return a list of song titles representing the five most Recent songs that both fall within
   * any attribute range specified by the most recent call to getRange, and conform to any filter
   */
  @Override
  public List<String> fiveMost() {
    List<String> list = setFilter(filterValue);
    List<String> toReturn = new ArrayList<>();
    // the number of songs are less then 5
    if (list.size() < 5) {
      return list;
    }

    //find 5 recent sons that fit the filter and range
    for (int i = 0; i < 5; i++) {
      toReturn.add(list.get(list.size() - i - 1));
    }

    return toReturn;
  }

}
