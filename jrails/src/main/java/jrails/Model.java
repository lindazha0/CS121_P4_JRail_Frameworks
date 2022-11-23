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
        StringBuilder fieldNames = new StringBuilder("id");
        for (Field f : Class.forName(className).getFields()) {
            fieldNames.append(seperator).append(f.getName());
        }
        bw.write(fieldNames.toString());
        bw.newLine();
        bw.close();
    }

    /**
     * Helper:
     * return a field String from a specific object
     * @return String of this. Field values, beginning with id
     */
    public static String getFieldString(int id, Object o) throws  IllegalAccessException {
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
        System.out.println("save dbMap to db file");
    }


    /**
     * if dbMap empty, load db file to it
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    private static void maintainDBMap(Class cls) throws IllegalArgumentException, IllegalAccessException, IOException, InstantiationException, InvocationTargetException, NoSuchMethodException, SecurityException, NoSuchFieldException {
        if(dbMap.keySet().isEmpty()){
            loadDBMap(cls);
        }
    }

    private static Object getFieldValue(Object input, Field f){
        String inType = input.getClass().getSimpleName();
        String type = ((Class) f.getType()).getSimpleName();
        System.out.println("convert "+inType+" "+input+" to "+type+": ");

        switch (type){
            case "Integer", "int":
                switch (inType){
                    case "Integer":
                        case "int": return input;
                    case "String":
                        if (input.equals("false") || input.toString().isEmpty() || input.toString().equals(" ")){return 0;}
                        if (input.equals("true")){return 1;}
                        try{
                            return Integer.valueOf(input.toString());
                        }catch (Exception e){return 1;}
                    case "boolean":
                    case "Boolean":
                        return (Boolean)input? 1:0;
                    default:
                        throw new IllegalStateException("Unexpected value: " + inType);
                }

            case "String":
                return String.valueOf(input);

            case "Boolean":
            case "boolean":
                switch (inType){
                    case "Integer":
                    case "int":
                            return !(Integer.valueOf(0)).equals(input);
                    case "String":
                        return Boolean.valueOf(input.toString());
                    case "Boolean":
                    case "boolean":
                        return input;
                    default:
                        throw new IllegalStateException("Unexpected value: " + inType);
                }
            default:
                throw new IllegalStateException("Unexpected value: " + type);
//                return null;
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
        try{
            // read line by line
            BufferedReader br = new BufferedReader(new FileReader(dbName));
            String line= br.readLine();
            if(line==null || line.isEmpty()){
                // empty db
                br.close();
                writeFirstRow();
                System.out.println("Empty db & dbMap!");
                return;
            }

            // begins from 2nd line
            line= br.readLine();

            while(line!=null && !line.isEmpty()){
                // System.out.println(line);
                String[] fields = line.split(seperator);

                // parsing fields & construct dbMap objects
                int i=0;
                int id = Integer.parseInt(fields[i++]);
                Object instance = cls.getDeclaredConstructor().newInstance();

                // set id
                cls.getMethod("setID", int.class).invoke(instance, id);

                // set other fields
                for(Field f:cls.getFields()){
                    f.set(instance, getFieldValue(fields[i++], f));
                }

                // load to dbMap
                dbMap.put(id, instance);
                line= br.readLine();
            }
            br.close();
            System.out.println("load db of "+dbMap.keySet()+" to dbMap: "+dbMap);
        }
        catch (IOException | ClassNotFoundException e) {
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

                // write to the db
                writeFirstRow();
            }
            else{
                // if db file is not empty, maintain dbMap if needed
                maintainDBMap(this.getClass());
            }

            // set id if not saved before
            if (this.id == 0) {
                synchronized (this) {
                    counter++;
                    this.id = counter;
                }

                // update dbMap
                dbMap.put(this.id, this);

                synchronized (this){
                    // add a new line to file
                    String this_entry = getFieldString(this.id, this);

                    // write current instance field vals
                    BufferedWriter bw = new BufferedWriter(new FileWriter(dbName, true));
                    bw.write(this_entry);
                    bw.newLine();
                    bw.close();
                }

            } else {
                // update dbMap
                dbMap.put(this.id, this);

                // update db file
                saveDBMap();
            }
            System.out.println("***  saved: "+getFieldString(this.id, this)+" to: "+dbMap.keySet()+"  ***");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public int id() {
        try {
            System.out.println("---  invoke id() at " + this + " : " + getFieldString(this.id, this)+"  ---");
        }catch (Exception e){e.printStackTrace(); return 0;}
        return this.id;
    }

    public static <T> T find(Class<T> c, int id) {
        try {
            maintainDBMap(c);
        } catch (IllegalArgumentException | IllegalAccessException | IOException | InstantiationException |
                 InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException e1) {
            e1.printStackTrace();
        }

        if (!dbMap.containsKey(id)) {
            System.out.println("dbMap ID Not Found: "+id);
            return null;
        }
        System.out.println("dbMap Lookup: "+id+" for "+c.toString());


        // materialize new instance
        try {
            // find Object and construct a new instance
            Object db_entry = dbMap.get(id);
            T instance = c.getDeclaredConstructor().newInstance();

            // set ID
            c.getMethod("setID", int.class).invoke(instance, id);

            // set values
            for(int i=0; i < c.getFields().length; i++){
                Field f = c.getFields()[i];
                Field db_f = db_entry.getClass().getFields()[i];
                f.set(instance, getFieldValue(db_f.get(db_entry), f));
            }
            System.out.println("! Lookup & setFields "+id+" : "+getFieldString(id, instance));
            return instance;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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
        System.out.println("Fetch all db entries in "+c.getSimpleName()+" form");

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

        try{System.out.println("***  destroyed: "+getFieldString(this.id, this)+" to: "+dbMap.keySet()+"  ***");}
        catch (Exception e){e.printStackTrace();}
        dbMap.remove(this.id);

        try {
            // write to disk file
            saveDBMap();
        } catch (IllegalArgumentException | IllegalAccessException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void reset() {
        System.out.println("------------- Reset DB! ---------------- ");
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
