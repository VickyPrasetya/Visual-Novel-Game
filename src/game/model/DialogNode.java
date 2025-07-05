package game.model;

import java.util.*;

public class DialogNode {

    public String label; // opsional, hanya jika ada label
    public String text;
    public String next; // opsional, bisa id scene atau label dialog
    public List<ChoiceData> choices;
}
