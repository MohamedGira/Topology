import com.google.gson.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;












/* This Api was implemented  using Gson and JSON java Libraries
*
*   Functions
*       1. Read a topology from a given JSON file and store it in the memory.
*       2. Write a given topology from the memory to a JSON file.
*       3. Query about which topologies are currently in the memory.
*       4. Delete a given topology from memory.
*       5. Query about which devices are in a given topology.
*       6. Query about which devices are connected to a given netlist node in
*           a given topology.
*

* */



class Topology {
    String id;
    ArrayList<Component> components=new ArrayList<>();

}
class TopologyApi {
    static ArrayList<Topology> Topologies=new ArrayList<>();

    static Boolean wrtieTopology(Topology topology, String pathname){

        // Creates a new  file (if the file doesn't exist) to store data into
        try {
            File myObj = new File(pathname);
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return false;
        }

        //Using GSON library, which easily converts or serialize  Java Objects to Json ones.
        Gson gson=new GsonBuilder().setPrettyPrinting().create();

        //Trying to Write data into the file
        try {
            FileWriter myWriter = new FileWriter(pathname);
            JsonElement element= JsonParser.parseString(gson.toJson(topology));
            JsonObject object= element.getAsJsonObject();
            String jsonstr=object.toString();

            // Converting keys such as ml and Default to m(l) and defalut so that it matches the required api output
            jsonstr=jsonstr.replaceAll("ml","m(l)");
            jsonstr=jsonstr.replaceAll("Default","default");

            // using Java JsonParser to parse the new string with accurate keys to Json element
            element=JsonParser.parseString(jsonstr);

            // again using gson to output a pretty formated text into the output file
            myWriter.write(gson.toJson(element));
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
            return true;
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return false;
        }


    }

    static Topology readTopology(String filepath) {


        String data=new String("");
        File myObj = new File(filepath);
        Scanner myReader = null;


        //Trying to access the requested file and converting it to Json Element, which can be converted to json object
        // and manipulated in easy manner
        JsonElement fileElement = null;
        try {
            fileElement = JsonParser.parseReader((new FileReader(filepath)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //creating json object and extracting its data to create the required Topology object
        JsonObject fileobject =fileElement.getAsJsonObject();
        String id= fileobject.get("id").getAsString();
        Topology top=new Topology();
        top.id=id;
        JsonArray componentsList=fileobject.get("components").getAsJsonArray();
        // Iterating through the components, and handling each derived component separately due to different objects per component
        for(JsonElement coponentElement: componentsList){
            JsonObject componentObject=coponentElement.getAsJsonObject();
            String type=componentObject.get("type").getAsString();
            String objid=componentObject.get("id").getAsString();
            JsonObject netlist=componentObject.get("netlist").getAsJsonObject();

            if(type.equals("resistor")){
                JsonObject resistance=componentObject.get("resistance").getAsJsonObject();
                double def= resistance.get("default").getAsDouble();
                double min= resistance.get("min").getAsDouble();
                double max= resistance.get("max").getAsDouble();
                String  t1= netlist.get("t1").getAsString();
                String t2= netlist.get("t2").getAsString();
                top.components.add(new Resistor(objid,def,min,max,t1,t2));
            }else if (type.equals("nmos")){

                JsonObject ml=componentObject.get("m(l)").getAsJsonObject();
                double def= ml.get("default").getAsDouble();
                double min= ml.get("min").getAsDouble();
                double max= ml.get("max").getAsDouble();
                String  gate= netlist.get("gate").getAsString();
                String  drain= netlist.get("drain").getAsString();
                String  source= netlist.get("source").getAsString();
                top.components.add(new Nmos(objid,def,min,max,gate,drain,source));
            }
        }
        //adding the acquired Topology into the memory
        Topologies.add(top);

        return top;

    }

    static ArrayList<Topology>  queryTopology(){
        //returning all stored topologies
        return Topologies;
    }

    static boolean deleteTopology(String id){
        //fetching the required Topology and deletes it from the memory, (true is returned upon successful deletion)
        for (Topology t :Topologies){
            if (t.id.equals(id)){
                Topologies.remove(t);
                return true;
            }
        }
        return false;
    }



    static ArrayList<Component> queryDevices(String topologyid){
        //fetching the topology by its id
        for (Topology t :Topologies){
            // fetching the obtained topology components
            if (t.id.equals(topologyid)){
                return  t.components;
            }
        }
        return null;
    }

    static ArrayList<Component> queryDevicesWithNetlistNode(String TopologyId,String NetlistNodeId){
        ArrayList<Component> result= new ArrayList<Component>();
        //fetching topology by its id
        for (Topology t :Topologies){
            if (t.id.equals(TopologyId)){
                //checking each component if it is connected to the netlist node or not
                for (Component component : t.components){
                    if(component.isConnectedTo(NetlistNodeId)){
                        //adding the component to the results if it matched the query
                        result.add(component);
                    }
                }
            }
        }
        return result;
    }

}


