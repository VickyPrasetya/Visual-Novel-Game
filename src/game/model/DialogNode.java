package game.model;

import java.util.*;

public class DialogNode {

      // Properti untuk dialog biasa
    public String character;
    public String text;
    public String next;

    // Properti untuk node yang berisi pilihan
    public List<choiceData> choices; 
}
