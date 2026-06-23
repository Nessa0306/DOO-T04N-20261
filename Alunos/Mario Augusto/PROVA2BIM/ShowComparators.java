package com.tvtracker.util;

import com.tvtracker.model.Show;
import java.util.Comparator;

public class ShowComparators {

    public static Comparator<Show> byName() {
        return Comparator.comparing(
            s -> s.getName() != null ? s.getName().toLowerCase() : "");
    }

    public static Comparator<Show> byRating() {
        return Comparator.comparingDouble(Show::getRatingAverage).reversed();
    }

    public static Comparator<Show> byStatus() {
        return Comparator.comparingInt(s -> statusOrder(s.getStatus()));
    }

    public static Comparator<Show> byPremiered() {
        return Comparator.comparing(
            s -> s.getPremiered() != null ? s.getPremiered() : "");
    }

    private static int statusOrder(String s) {
        if (s == null) return 99;
        return switch (s) {
            case "Running"            -> 0;
            case "Ended"              -> 1;
            case "To Be Determined"   -> 2;
            default                   -> 3;
        };
    }
}