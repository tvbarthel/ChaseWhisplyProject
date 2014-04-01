package fr.tvbarthel.games.chasewhisply.model;

public class MathUtils {

    public static int randomize(int min, int max) {
        return min + (int) (Math.random() * ((max - min) + 1));
    }
}
