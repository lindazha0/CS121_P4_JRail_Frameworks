package jrails;

import java.io.*;

public class TryIO {
    static String path = "./db.txt";

    public static void main(String[] args){
        File db = new File(path);
        // create file if not exsisting
        if (!db.isFile() || !db.exists()) {
            try {
                db.createNewFile();
            } catch (Exception e) {
                System.err.format("Create File error: %s%N", e);
            }
        }

        try {// line write
            BufferedWriter bw = new BufferedWriter(new FileWriter(db));
            bw.write("ID"+","+"Title"+","+"Author"+",");
            bw.newLine();
            bw.write("1"+","+"balabala"+","+"Zhang San"+",");
            bw.newLine();
            bw.write("2"+","+"bilibili"+","+"Li Si"+",");
            bw.close();

            readDB();

            // over write
            PrintWriter pw =new PrintWriter(path);
            pw.close();
            
            readDB();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Helper
     * read & print file contexts
     */
    static void readDB(){
        System.out.println("Hereby Reading DB:");
        try{
            // read at once
            BufferedReader br = new BufferedReader(new FileReader(path));
            String line= br.readLine();
            while(line  != null){
                System.out.println(line);
                line= br.readLine();
            }
            br.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}
