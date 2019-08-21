package hmm.storage;

import hmm.reader.RoadDataReader;
import hmm.types.RoadNetwork;

import java.io.*;
import java.util.ArrayList;

public class ToFille {
    public static void writeIntoFile(RoadNetwork rn) {
        ObjectOutputStream oos;
        try {
            oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("D:/HMM/file.txt")));

            oos.writeObject(rn);

            oos.flush();
            oos.close();
        } catch (IOException e) {
            // Auto-generated catch block
            e.printStackTrace();
        } finally {
            System.out.println("write in success.");
        }
    }

    public static RoadNetwork testReader() {
        RoadNetwork roadnetwork = null;
        try {
            ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream("D:/HMM/file.txt")));
            roadnetwork = (RoadNetwork) ois.readObject();
            ois.close();
        } catch (IOException e) {
            // Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // Auto-generated catch block
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
