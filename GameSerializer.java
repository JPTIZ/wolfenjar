import java.util.ArrayList;
import java.io.IOException;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class GameSerializer {
    public static void serialize(String filename, ArrayList<Object> data) {
        try {
            FileOutputStream fos = new FileOutputStream(filename);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            for (Object obj : data) {
                oos.writeObject(obj);
            }
            oos.close();
            fos.close();
            Game.log("Game saved succesfully ("+filename+")");
        } catch (IOException e) {
            Game.log("Error saving game: "+e.getMessage());
            e.printStackTrace();
            return;
        }
    }

    public static ArrayList<Object> load(String filename) {
        ArrayList<Object> data = new ArrayList<>();
        try {
            FileInputStream fis = new FileInputStream(filename);
            ObjectInputStream ois = new ObjectInputStream(fis);
            while (true) {
                try {
                    data.add(ois.readObject());
                    Game.log("Loaded: "+data.get(data.size()-1));
                } catch (EOFException eof) {
                    Game.log("EOF reached.");
                    break;
                }
            }
            ois.close();
            fis.close();
            Game.log("Game loaded ("+filename+")");
        } catch (Exception e) {
            Game.log("Error loading game: "+e.getMessage());
            return null;
        }
        return data;
    }
}