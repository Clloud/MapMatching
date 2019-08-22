package hmm.storage;

import hmm.types.RoadNetwork;
import java.io.*;

public class ToFille {
    public static final String fileName = "E:/Files/Project/2019 Summer Research/MapMatching/data/file.txt";

    public static void writeIntoFile(RoadNetwork rn) {
        ObjectOutputStream oos;
        try {
            oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(fileName)));

            oos.writeObject(rn);

            oos.flush();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("write in success.");
        }
    }

    public static RoadNetwork testReader() {
        RoadNetwork roadnetwork = null;
        try {
            ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(fileName)));
            roadnetwork = (RoadNetwork) ois.readObject();
            ois.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            System.out.println("read out success.");
        }
        return roadnetwork;
    }
/*
    public static void main(String[] args) {
        RoadNetwork rn=new RoadNetwork(RoadDataReader.getData("test.txt"));

        //List<Student> stus = listStudents(student);
        writeIntoFile(rn);
        System.out.println(testReader());
    }
 */
}
