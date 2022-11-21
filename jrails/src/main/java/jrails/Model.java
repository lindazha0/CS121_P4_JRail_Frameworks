package jrails;

import java.io.*;
import java.util.*;
import java.lang.reflect.*;

public class Model {
    // static fields
    static int counter = 0;
    static String dbName = "./db.txt";
    static Map<Integer, Object> dbMap = new HashMap<>(); // the two always up-to-date
    static String seperator = "  ,  ";

    // instance fields
    private int id = 0;
    private static Object newInstance;
    private static Object invoke;
    public void setID(int id){
        this.id = id;
    }

    /**
     * Helper:
     * return a field String from a specific object
     * @return String of this.field values, beginning with id
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    private static String getFieldString(int id, Object o) throws IllegalArgumentException, IllegalAccessException {
        String fieldVals = String.valueOf(id);
        for (Field f : o.getClass().getFields()) {
            fieldVals = fieldVals + seperator + f.get(o);
        }

        return fieldVals;
    }

    /**
     * Helper: 
     * save dbMap to the db disk file
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws IOException
     */
    private static void saveDBMap() throws IllegalArgumentException, IllegalAccessException, IOException{
        // replace the line in file
        reset();

        // rewrite the updated dbMap to db
        BufferedWriter bw = new BufferedWriter(new FileWriter(dbName));
        for (Integer id : dbMap.keySet()) {
            bw.write(getFieldString(id, dbMap.get(id)));
            bw.newLine();
        }
        bw.close();
    }
    

    /**
     * if dbMap empty, load db file to it
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws IOException
     */
    private static void maintainDBMap() throws IllegalArgumentException, IllegalAccessException, IOException{
        if(dbMap.keySet().isEmpty()){
            saveDBMap();
        }
    }


    /** 
     * load dbfile to dbMap
     * @param cls 
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private static void loadDBMap(Class cls) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
        // System.out.println("Hereby Reading DB:");
        try{
            // read line by line
            BufferedReader br = new BufferedReader(new FileReader(dbName));
            String line= br.readLine();
            
            while(line  != null){
                // System.out.println(line);
                String[] fields = line.split(seperator);

                // parsing fields & construct dbMap objects
                int i=0;
                int id = Integer.parseInt(fields[++i]);
                Object instance = cls.getDeclaredConstructor().newInstance();

                // set id
                cls.getMethod("setID").invoke(instance, id);

                // set other fields
                for(Field f:cls.getFields()){
                    f.set(instance, fields[++i]);
                }

                // load to dbMap
                dbMap.put(id, instance);

                // next line
                line= br.readLine();
            }
            br.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * save current instance to the dist file
     */
    public void save() {
        /* this is an instance of the current model */
        try {
            // if no db file, create one
            File db = new File(dbName);

            // create the buffer writer
            BufferedWriter bw = new BufferedWriter(new FileWriter(db));
            Field[] fields = this.getClass().getFields();

            if (!db.isFile() || !db.exists()) {
                db.createNewFile();

                // write to the db
                // first row: field names
                String fieldNames = "id";
                for (Field f : fields) {
                    fieldNames = fieldNames + seperator + f.getName();
                }
                bw.write(fieldNames);
                bw.newLine();
            }
            else{
                // if db file is not empty, maintain dbMap if needed
                maintainDBMap();
            }

            // set id if not saved before
            if (this.id == 0) {
                synchronized (this) {
                    counter++;
                    this.id = counter;
                }

                // update dbMap
                dbMap.put(this.id, this);

                // add a new line to file
                String this_entry = getFieldString(this.id, this);

                // write current instance field vals
                bw.write(this_entry);
                bw.newLine();
            } else {
                // update dbMap
                dbMap.put(this.id, this);
            
                saveDBMap();
            }

            // close the buffer writer
            bw.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        } catch(IllegalAccessException e){
            e.printStackTrace();
            
        }
        // throw new UnsupportedOperationException();
        // if exist, open & write to it
        // otherwise create and write
    }

    public int id() {
        return this.id;
        // throw new UnsupportedOperationException();
    }

    public static <T> T find(Class<T> c, int id) {
        try {
            maintainDBMap();
        } catch (IllegalArgumentException | IllegalAccessException | IOException e1) {
            e1.printStackTrace();
        }


        if (!dbMap.containsKey(id)) {
            throw new UnsupportedOperationException();
        }

        try {
            // find Object and construct a new instance
            Object db_entry = dbMap.get(id);
            // Class<?> model = db_entry.getClass();
            T instance = c.getDeclaredConstructor().newInstance();

            // set values
            for (Field f : c.getFields()) {
                f.set(instance, f.get(db_entry));
            }

            return instance;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * load the current db to the dbMap and return a List<T>
     * 
     * @param <T>
     * @param c
     * @return List<T>
     */
    public static <T> List<T> all(Class<T> c) {
        try {
            maintainDBMap();
        } catch (IllegalArgumentException | IllegalAccessException | IOException e1) {
            e1.printStackTrace();
        }

        // Returns a List<element type>
        List<T> t_list = new ArrayList<>();
        for (Integer i : dbMap.keySet()) {
            t_list.add(find(c, i));
        }
        return t_list;
        // throw new UnsupportedOperationException();
    }

    /**
     * remove current model from db
     */
    public void destroy() {
        try {
            maintainDBMap();
        } catch (IllegalArgumentException | IllegalAccessException | IOException e1) {
            e1.printStackTrace();
        }


        if(!dbMap.containsKey(this.id)){
            throw new UnsupportedOperationException();
        }

        dbMap.remove(this.id);

        try {
            // write to disk file
            saveDBMap();
        } catch (IllegalArgumentException | IllegalAccessException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void reset() {
        // empty db
        try {
            dbMap.clear();
            new PrintWriter(dbName).close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // throw new UnsupportedOperationException();
    }
}
