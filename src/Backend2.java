import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/**
 * Class that implements the backend interface.
 */
public class Backend2 implements BackendInterface {

  // Tree filed storing the songs
  IterableRedBlackTree<Song> tree = null;

  // field storing the lower limit for danceability
  int low_danceability = -1;

  // field storing the higher limit for danceability
  int high_danceability = -1;

  // field storing the lower limit for speed (bpm)
  int speed_filter = -1;

  /**
   * constructor for Backend class
   * @param tree tree object that stores songs
   */
  public Backend2(IterableSortedCollection<Song> tree){
    this.tree = (IterableRedBlackTree<Song>) tree;
  }

  /**
   * Loads data from the .csv file referenced by filename.  You can rely
   * on the exact headers found in the provided songs.csv, but you should
   * not rely on them always being presented in this order or on there
   * not being additional columns describing other song qualities.
   * After reading songs from the file, the songs are inserted into
   * the tree passed to the constructor.
   * @param filename is the name of the csv file to load data from
   * @throws IOException when there is trouble finding/reading file
   */
  public void readData(String filename) throws IOException{

    try{
      File file = new File(filename);
      Scanner scnr = new Scanner(file);

    } catch (IOException e){
      System.out.println("File not found");
    }

    // creating the file and scanner object
    File file = new File(filename);
    Scanner scnr = new Scanner(file);

    // creating variables for different aspects of a song
    String title = "";
    String artist = "";
    String genre = "";
    String year = "";
    String bpm = "";
    String energy = "";
    String danceability = "";
    String loudness = "";
    String liveness = "";

    // creating a list that stores the index of the header values
    int[] headers = new int[9];

    int number_of_ideal_commas = 0;

    // iterating over the first line of headers
    if (scnr.hasNextLine()){

      // getting the data line by line
      String data = scnr.nextLine();
      String[] datacontents = data.split(",");

      number_of_ideal_commas = datacontents.length - 1;

      // getting the indeces of the headers and storing them in headers array
      for (int i=0; i<datacontents.length;i++){

        if (datacontents[i].equals("title")){
          headers[0] = i;
        }
        if (datacontents[i].equals("artist")){
          headers[1] = i;
        }
        if (datacontents[i].equals("top genre")){
          headers[2] = i;
        }
        if (datacontents[i].equals("year")){
          headers[3] = i;
        }
        if (datacontents[i].equals("bpm")){
          headers[4] = i;
        }
        if (datacontents[i].equals("nrgy")){
          headers[5] = i;
        }
        if (datacontents[i].equals("dnce")){
          headers[6] = i;
        }
        if (datacontents[i].equals("dB")){
          headers[7] = i;
        }
        if (datacontents[i].equals("live")){
          headers[8] = i;
        }

      }

    }

    // return if file is empty
    else{return;}

    // iterating over each row containing song data
    while (scnr.hasNextLine()){

      // array of important data for each song
      String[] string_data = new String[9];
      String thisLine = "";
      // string containing line information
      thisLine = scnr.nextLine();


      String[] parts_of_thisline = new String[9];

      // we check if the line has " with find method, if not then we use a simple for loop
      // and part things easily
      if (number_of_ideal_commas == thisLine.split(",").length-1 ){

        // array of different parts of line
        parts_of_thisline = thisLine.split(",");

        // adds the corresponding data into string_data array
        for (int i=0; i<parts_of_thisline.length;i++){
          for (int j=0; j<headers.length; j++){
            if (i == headers[j]){
              string_data[j] = parts_of_thisline[i];
            }
          }
        }

        // assigns the variables with correct data
        title = string_data[0];
        artist = string_data[1];
        genre = string_data[2];
        year = string_data[3];
        bpm = string_data[4];
        energy = string_data[5];
        danceability = string_data[6];
        loudness = string_data[7];
        liveness = string_data[8];

        // if any of the headers were missing, throw error
        if (    title.compareTo("") == 0 ||
            artist.compareTo("") == 0 ||
            genre.compareTo("") == 0 ||
            year.compareTo("") == 0 ||
            bpm.compareTo("") == 0 ||
            energy.compareTo("") == 0 ||
            danceability.compareTo("") == 0 ||
            loudness.compareTo("") == 0 ||
            liveness.compareTo("") == 0
        ){
          throw new IOException("Headers not correctly provided");
        }

        //Arrays.toString(string_data);

        // create song object with data
        Song newSong = new Song(title, artist, genre, Integer.parseInt(year),
            Integer.parseInt(bpm), Integer.parseInt(energy),
            Integer.parseInt(danceability),
            Integer.parseInt(loudness), Integer.parseInt(liveness));

        // add the song object to the tree
        tree.insert(newSong);

      }

      // if the line contains "" we have to deal with special comma case
      else{

        int num_to_subtract = 0;


        int index = 0;

        // array of line parts split by ,
        parts_of_thisline = thisLine.split(",");
        int encounteredWeird = 0;

        while (index < parts_of_thisline.length){

          // string representing one part
          String individualDataParts = "";
          individualDataParts = parts_of_thisline[index];


          // if part has odd ", normal case
          if (individualDataParts.split("\"").length % 2 == 1 && encounteredWeird != 1) {
            for (int j = 0; j < headers.length; j++) {

              if (encounteredWeird == 2){
                if (index-num_to_subtract+1 == headers[j]) {
                  string_data[j] = parts_of_thisline[index];
                }
              }
              else{
                if (index == headers[j]) {
                  string_data[j] = parts_of_thisline[index];
                }
              }


            }
            index++;
          }

          // if part has even ", weird case with comma inside ""
          else{

            for (int j = 0; j < headers.length; j++) {

              // checks if the previous iteration had odd "
              if (encounteredWeird == 1){
                if (index-num_to_subtract == headers[j]) {
                  string_data[j] += parts_of_thisline[index];
                  if (encounteredWeird == 1){
                    if (individualDataParts.split("\"").length % 2 == 1
                        && individualDataParts.
                        charAt(individualDataParts.length()-1) == '\"'){
                      encounteredWeird++;
                    }
                  }
                  num_to_subtract++;
                  break;
                }

              }

              else if (encounteredWeird == 0){
                if (index == headers[j]) {
                  string_data[j] = parts_of_thisline[index] + ",";
                  encounteredWeird++;
                  num_to_subtract = 1;
                  break;
                }

              }
            }

            index++;


          }

        }

        // filling variables with data
        title = string_data[0];
        artist = string_data[1];
        genre = string_data[2];
        year = string_data[3];
        bpm = string_data[4];
        energy = string_data[5];
        danceability = string_data[6];
        loudness = string_data[7];
        liveness = string_data[8];


        // if any of the headers were missing, throw error
        if (    title.compareTo("") == 0 ||
            artist.compareTo("") == 0 ||
            genre.compareTo("") == 0 ||
            year.compareTo("") == 0 ||
            bpm.compareTo("") == 0 ||
            energy.compareTo("") == 0 ||
            danceability.compareTo("") == 0 ||
            loudness.compareTo("") == 0 ||
            liveness.compareTo("") == 0
        ){
          throw new IOException("Headers not correctly provided");
        }

        // creating song object with data
        Song newSong = new Song(title, artist, genre, Integer.parseInt(year),
            Integer.parseInt(bpm), Integer.parseInt(energy),
            Integer.parseInt(danceability),
            Integer.parseInt(loudness), Integer.parseInt(liveness));

        // add the song object to the tree
        tree.insert(newSong);


      }


    }


  }

  /**
   * Retrieves a list of song titles from the tree passed to the contructor.
   * The songs should be ordered by the songs' Danceability, and that fall within
   * the specified range of Danceability values.  This Danceability range will
   * also be used by future calls to the setFilter and getmost Recent methods.
   *
   * If a Speed filter has been set using the setFilter method
   * below, then only songs that pass that filter should be included in the
   * list of titles returned by this method.
   *
   * When null is passed as either the low or high argument to this method,
   * that end of the range is understood to be unbounded.  For example, a null
   * high argument means that there is no maximum Danceability to include in
   * the returned list.
   *
   * @param low is the minimum Danceability of songs in the returned list
   * @param high is the maximum Danceability of songs in the returned list
   * @return List of titles for all songs from low to high, or an empty
   *     list when no such songs have been loaded
   */
  public List<String> getRange(Integer low, Integer high){

    // variables to store high and low range for danceability
    int high_limit_copy;
    int low_limit_copy;

    // if no high specified, unbounded, else high is set
    if (high != null){
      high_danceability = high;
      high_limit_copy = high;
    }else{
      high_limit_copy = Integer.MAX_VALUE;
    }

    // if no low specified, unbounded, else low is set
    if (low != null){
      low_danceability = low;
      low_limit_copy = low;
    }else{
      low_limit_copy = Integer.MIN_VALUE;
    }

    // creating Comparable<Song> object with high value and comparator set as song's
    // danceability
    Comparable<Song> high_song = new Song(
        "",
        "",
        "",
        -1,
        -1,
        -1,
        high_limit_copy,
        -1,
        -1,
        Comparator.comparingInt(Song::getDanceability)
    );

    // creating Comparable<Song> object with low value and comparator set as song's
    // danceability
    Comparable<Song> low_song = new Song(
        "",
        "",
        "",
        -1,
        -1,
        -1,
        low_limit_copy,
        -1,
        -1,
        Comparator.comparingInt(Song::getDanceability)
    );

    // setting tree with bounds
    tree.setIteratorMax(high_song);
    tree.setIteratorMin(low_song);

    // returns an interator object of songs filtered by the bounds we set.
    Iterator<Song> list_of_songs_filtered_danceability = tree.iterator();

    // array of songs that will be fitlered by speed.
    List<Song> list_of_filtered_songs = new ArrayList<Song>();

    // if speed filter, then only songs with lower speed added to array, else all songs copied.
    if (speed_filter != -1){
      while (list_of_songs_filtered_danceability.hasNext()){
        Song iteratingSong = list_of_songs_filtered_danceability.next();
        if (iteratingSong.getBPM() < speed_filter){
          list_of_filtered_songs.add(iteratingSong);
        }
      }
    }
    else{
      while (list_of_songs_filtered_danceability.hasNext()){
        list_of_filtered_songs.add(list_of_songs_filtered_danceability.next());
      }
    }

    // all songs in the arraylist are sorted by their danceability
    list_of_filtered_songs.sort(Comparator.comparingInt(Song::getDanceability));

    // list that will store only the songs title
    List<String> list_of_titles = new ArrayList<>();

    // filling the arraylist with song titles
    for (Song song: list_of_filtered_songs){
      list_of_titles.add(song.getTitle());
    }

    // returning list of titles
    return list_of_titles;

  }


  /**
   * Retrieves a list of song titles that have a Speed that is
   * smaller than the specified threshold.  Similar to the getRange
   * method: this list of song titles should be ordered by the songs'
   * Danceability, and should only include songs that fall within the specified
   * range of Danceability values that was established by the most recent call
   * to getRange.  If getRange has not previously been called, then no low
   * or high Danceability bound should be used.  The filter set by this method
   * will be used by future calls to the getRange and getmost Recent methods.
   *
   * When null is passed as the threshold to this method, then no Speed
   * threshold should be used.  This effectively clears the filter.
   *
   * @param threshold filters returned song titles to only include songs that
   *     have a Speed that is smaller than this threshold.
   * @return List of titles for songs that meet this filter requirement, or
   *     an empty list when no such songs have been loaded
   */
  public List<String> setFilter(Integer threshold){

    // setting the threshold if not null
    if (threshold != null){
      speed_filter = threshold;
    }

    // setting high and low bounds
    int high = 0;
    int low = 0;
    if (high_danceability == -1){
      high = Integer.MAX_VALUE;
    } else{
      high = high_danceability;
    }
    if (low_danceability == -1){
      low = Integer.MIN_VALUE;
    } else{
      low = low_danceability;
    }

    // creating Comparable<Song> object with high value and comparator set as song's
    // danceability
    Comparable<Song> high_song = new Song(
        "",
        "",
        "",
        -1,
        -1,
        -1,
        high,
        -1,
        -1,
        Comparator.comparingInt(Song::getDanceability)
    );

    // creating Comparable<Song> object with low value and comparator set as song's
    // danceability
    Comparable<Song> low_song = new Song(
        "",
        "",
        "",
        -1,
        -1,
        -1,
        low,
        -1,
        -1,
        Comparator.comparingInt(Song::getDanceability)
    );

    // setting tree with bounds
    tree.setIteratorMax(high_song);
    tree.setIteratorMin(low_song);

    // returns an interator object of songs filtered by the bounds we set.
    Iterator<Song> list_of_songs_filtered_danceability = tree.iterator();

    // array of songs that will be fitlered by speed.
    List<Song> list_of_filtered_songs = new ArrayList<Song>();

    // if speed filter, then only songs with lower speed added to array, else all songs copied.
    if (speed_filter != -1){
      while (list_of_songs_filtered_danceability.hasNext()){
        Song iteratingSong = list_of_songs_filtered_danceability.next();
        if (iteratingSong.getBPM() < speed_filter){
          list_of_filtered_songs.add(iteratingSong);
        }
      }
    }
    else{
      while (list_of_songs_filtered_danceability.hasNext()){
        list_of_filtered_songs.add(list_of_songs_filtered_danceability.next());
      }
    }

    // all songs in the arraylist are sorted by their danceability
    list_of_filtered_songs.sort(Comparator.comparingInt(Song::getDanceability));

    // list that will store only the songs title
    List<String> list_of_titles = new ArrayList<>();

    // filling the arraylist with song titles
    for (Song song: list_of_filtered_songs){
      list_of_titles.add(song.getTitle());
    }

    // returning list of titles
    return list_of_titles;

  }

  /**
   * This method returns a list of song titles representing the five
   * most Recent songs that both fall within any attribute range specified
   * by the most recent call to getRange, and conform to any filter set by
   * the most recent call to setFilter.  The order of the song titles in this
   * returned list is up to you.
   *
   * If fewer than five such songs exist, return all of them.  And return an
   * empty list when there are no such songs.
   *
   * @return List of five most Recent song titles
   */
  public List<String> fiveMost(){

    // setting high and low bounds
    int high = 0;
    int low = 0;
    if (high_danceability == -1){
      high = Integer.MAX_VALUE;
    } else{
      high = high_danceability;
    }
    if (low_danceability == -1){
      low = Integer.MIN_VALUE;
    } else{
      low = low_danceability;
    }

    // creating Comparable<Song> object with high value and comparator set as song's
    // danceability
    Comparable<Song> high_song = new Song(
        "",
        "",
        "",
        -1,
        -1,
        -1,
        high,
        -1,
        -1,
        Comparator.comparingInt(Song::getDanceability)
    );

    // creating Comparable<Song> object with low value and comparator set as song's
    // danceability
    Comparable<Song> low_song = new Song(
        "",
        "",
        "",
        -1,
        -1,
        -1,
        low,
        -1,
        -1,
        Comparator.comparingInt(Song::getDanceability)
    );

    // setting tree with bounds
    tree.setIteratorMax(high_song);
    tree.setIteratorMin(low_song);

    // returns an interator object of songs filtered by the bounds we set.
    Iterator<Song> list_of_songs_filtered_danceability = tree.iterator();

    // array of songs that will be fitlered by speed.
    List<Song> list_of_filtered_songs = new ArrayList<Song>();

    // if speed filter, then only songs with lower speed added to array, else all songs copied.
    if (speed_filter != -1){
      while (list_of_songs_filtered_danceability.hasNext()){
        Song iteratingSong = list_of_songs_filtered_danceability.next();
        if (iteratingSong.getBPM() < speed_filter){
          list_of_filtered_songs.add(iteratingSong);
        }
      }
    }
    else{
      while (list_of_songs_filtered_danceability.hasNext()){
        list_of_filtered_songs.add(list_of_songs_filtered_danceability.next());
      }
    }

    // all songs in the arraylist are sorted by their danceability
    list_of_filtered_songs.sort(Comparator.comparingInt(Song::getDanceability));

    // list that will store only the songs title
    List<String> list_of_titles = new ArrayList<>();

    // filling the arraylist with song titles
    for (Song song: list_of_filtered_songs){
      list_of_titles.add(song.getTitle());
    }

    // if the list at most 5 elements, return the entire list.
    if (list_of_titles.size() <= 5) {
      return list_of_titles;
    }

    // return the last 5 elements of the list.
    return list_of_titles.subList(list_of_titles.size() - 5, list_of_titles.size());

  }

  /**
   * Private method to check the contents of the tree for testing purposes.
   * @return list of the contents
   */
  private List<Song> testerOutputCheck(){
    Iterator<Song> iterObj = tree.iterator();
    List<Song> songs = new ArrayList<>();
    while (iterObj.hasNext()){
      songs.add(iterObj.next());
    }
    return songs;
  }


}

