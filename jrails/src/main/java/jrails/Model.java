package jrails;

import java.io.*;
import java.util.*;
import java.lang.reflect.*;

public class Model {
    // static fields
    static int counter = 0;
    static String className;
    final static String dbName = "./db.txt";
    static Map<Integer, Object> dbMap = new HashMap<>(); // the two always up-to-date
    static String seperator = " ｜ ";

    // instance fields
    private int id = 0;
    public void setID(int id){
        this.id = id;
    }

    private static void writeFirstRow() throws IOException, ClassNotFoundException {
        // create the buffer writer
        BufferedWriter bw = new BufferedWriter(new FileWriter(dbName));

        // first row: field names
        String fieldNames = "id";
        for (Field f : Class.forName(className).getFields()) {
            fieldNames = fieldNames + seperator + f.getName();
        }
        bw.write(fieldNames);
        bw.newLine();
        bw.close();
    }

    /**
     * Helper:
     * return a field String from a specific object
     * @return String of this.field values, beginning with id
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    private static String getFieldString(int id, Object o) throws IllegalArgumentException, IllegalAccessException {
        StringBuilder fieldVals = new StringBuilder(String.valueOf(id));
        for (Field f : o.getClass().getFields()) {
            fieldVals.append(seperator).append(f.get(o));
        }

        return fieldVals.toString();
    }

    /**
     * Helper: 
     * save dbMap to the db disk file
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws IOException
     */
    private static void saveDBMap() throws IllegalArgumentException, IllegalAccessException, IOException, ClassNotFoundException {
        // rewrite the updated dbMap to db
        writeFirstRow();
        BufferedWriter bw = new BufferedWriter(new FileWriter(dbName, true));
        for (Integer id : dbMap.keySet()) {
            String str = getFieldString(id, dbMap.get(id));
            bw.write(str);
            bw.newLine();
        }
        bw.close();
    }


    /**
     * if dbMap empty, load db file to it
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws IOException
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws InstantiationException
     */
    private static void maintainDBMap(Class cls) throws IllegalArgumentException, IllegalAccessException, IOException, InstantiationException, InvocationTargetException, NoSuchMethodException, SecurityException, NoSuchFieldException {
        if(dbMap.keySet().isEmpty()){
            System.out.println("load db to dbMap");
            loadDBMap(cls);
        }
    }

    private static Object getFieldValue(String input, Field f){
        String type = ((Class) f.getType()).getSimpleName();
        switch (type){
            case "Integer", "int":
                return Integer.valueOf(input);

            case "String":
                return input;
            case "Boolean":
                return Boolean.valueOf(input);
            default:
                System.out.println("invalid type: "+type);
                return null;
//                throw new InvalidObjectException();
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
    private static void loadDBMap(Class cls) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, NoSuchFieldException {
        // System.out.println("Hereby Reading DB:");
        try{
            // read line by line
            BufferedReader br = new BufferedReader(new FileReader(dbName));
            String line= br.readLine();
            
            while(line  != null){
                // begins from 2nd line
                line= br.readLine();


                // System.out.println(line);
                String[] fields = line.split(seperator);

                // parsing fields & construct dbMap objects
                int i=0;
                int id = Integer.parseInt(fields[i++]);
                Object instance = cls.getDeclaredConstructor().newInstance();

                // set id
//                cls.getField("id").set(instance, id);
                cls.getMethod("setID", int.class).invoke(instance, id);

                // set other fields
                for(Field f:cls.getFields()){
                    f.set(instance, getFieldValue(fields[i++],f));
                }

                // load to dbMap
                dbMap.put(id, instance);


            }
            br.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void readDB(){
        System.out.println("Hereby Reading DB:");
        try{
            // read at once
            BufferedReader br = new BufferedReader(new FileReader(dbName));
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
    
    /**
     * save current instance to the dist file
     */
    public void save() {
        /* this is an instance of the current model */
        try {
            Field[] fields = this.getClass().getFields();
            className = this.getClass().getName();


            // if no db file, create one
            File db = new File(dbName);
            if (!db.isFile() || !db.exists()) {
                db.createNewFile();

                synchronized (this){
                    // write to the db
                    writeFirstRow();
                }
//                System.out.println("after add 1st line:");
//                readDB();
            }
            else{
                // if db file is not empty, maintain dbMap if needed
                maintainDBMap(this.getClass());
//                System.out.println("after maintain DBMap:");
//                readDB();
            }

            // set id if not saved before
            if (this.id == 0) {
                synchronized (this) {
                    counter++;
                    this.id = counter;
                }

                // update dbMap
                dbMap.put(this.id, this);
//                System.out.println(dbMap.keySet());


                synchronized (this){
                    // add a new line to file
                    String this_entry = getFieldString(this.id, this);

                    // write current instance field vals
                    BufferedWriter bw = new BufferedWriter(new FileWriter(dbName, true));
                    bw.write(this_entry);
                    bw.newLine();
                    bw.close();
                }

//                System.out.println("after add new line:");
//                readDB();
            } else {
                // update dbMap
                dbMap.put(this.id, this);
//                System.out.println(dbMap.keySet());

                // update db file
                saveDBMap();
//                System.out.println("after saveDBMap:");
//                readDB();

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        // throw new UnsupportedOperationException();
    }

    public int id() {
        return this.id;
        // throw new UnsupportedOperationException();
    }

    public static <T> T find(Class<T> c, int id) {
        try {
            maintainDBMap(c);
        } catch (IllegalArgumentException | IllegalAccessException | IOException | InstantiationException |
                 InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException e1) {
            e1.printStackTrace();
        }


        if (!dbMap.containsKey(id)) {
            return null;
        }

        // materialize new instance
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
            maintainDBMap(c);
        } catch (IllegalArgumentException | IllegalAccessException | IOException | InstantiationException |
                 InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException e1) {
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
            maintainDBMap(this.getClass());
        } catch (IllegalArgumentException | IllegalAccessException | IOException | InstantiationException |
                 InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException e1) {
            e1.printStackTrace();
        }

        if(!dbMap.containsKey(this.id)){
            throw new UnsupportedOperationException();
        }

        dbMap.remove(this.id);

        try {
            // write to disk file
            saveDBMap();
        } catch (IllegalArgumentException | IllegalAccessException | IOException | ClassNotFoundException e) {
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
