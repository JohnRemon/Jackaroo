package application.boardView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class gridLoader {
    private static ArrayList<int[]> grid = new ArrayList<>();

    public static void loadGrid() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("tile_clicks.txt"));
            for (int i = 0; i < 100; i++)
            {
                int[] point = new int[2];
                String[] line = br.readLine().split(",");
                point[0] = Integer.parseInt(line[0]);
                point[1] = Integer.parseInt(line[1]);
                grid.add(point);
            }
        } catch (IOException e) {
            System.out.println("Couldnt find file");
        }
    }
    public static ArrayList<int[]> getGrid() {
        return grid;
    }
}
