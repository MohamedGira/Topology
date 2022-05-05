

import com.google.gson.*;

import java.io.*;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Scanner;





class Main {

    public static void main(String[] args) {

        /* the two functions are tested by comparing the input and output files*/

        // Function 1:  Read a topology from a given JSON file and store it in the memory.
        Topology c = TopologyApi.readTopology("topology.json");

        //Function 2:Write a given topology from the memory to a JSON file.
        TopologyApi.wrtieTopology(c,"newtopology.json");

        /*
        *               results
        *   Both have the save content, but with a different order.
        *   But " As per JSON standard, official definition of object states:
        *       An object is an unordered set of name/value pairs. Therefore the order does not matter."
        *
        *
        * */




        //Function 3:Query about which topologies are currently in the memory.
        ArrayList<Topology> result=TopologyApi.queryTopology();

        for (int i=0;i<result.size();i++){
            Topology t=result.get(i);
            System.out.println("Topology"+(i+1)+"Id:"+t.id);
            System.out.print("Topology Components: ");
            for (Component component : t.components){
                System.out.print("{ type: "+component.type+", id:"+component.id+"   .   ");
            }
            System.out.println("}");

        }

        /*               results
         *
         *   Topology1Id:top1
         *   Topology Components: { type: resistor, id:res1   .   { type: nmos, id:m1   .   }
         *
         *
         * */

        //Function 4: Delete a given topology from memory.

        System.out.println( "Size before removal: "+TopologyApi.Topologies.size());
        TopologyApi.deleteTopology("top1");
        System.out.println( "Size after removal: "+TopologyApi.Topologies.size());
            /*
            *       results
            *
            * Size before removal: 1
            * Size after removal: 0
            * */


        /*
        *  Reloading the topology object to the memory
        */
         c = TopologyApi.readTopology("topology.json");


        //Function 5: Query about which devices are in a given topology.

        ArrayList<Component> top1components=TopologyApi.queryDevices("top1");


        for (Component component : top1components){
            System.out.print("{ type: "+component.type+", id:"+component.id+"} ");
        }
        System.out.println();


        /*
        *           results
        *    { type: resistor, id:res1} { type: nmos, id:m1}
        * */

        //Function 6: Query about which devices are connected to a given netlist node in given topology.

        System.out.print("Devices connected to n1: ");
        ArrayList<Component> n1NodeComponents=TopologyApi.queryDevicesWithNetlistNode("top1","n1");

        for (Component component : n1NodeComponents){
            System.out.print("{ type: "+component.type+", id:"+component.id+"} ");
        }
        System.out.println();

        System.out.print("Devices connected to vss: ");
        ArrayList<Component> vssNodeComponents=TopologyApi.queryDevicesWithNetlistNode("top1","vss");

        for (Component component : vssNodeComponents){
            System.out.print("{ type: "+component.type+", id:"+component.id+"} ");
        }
        System.out.println();
        System.out.print("Devices connected to vdd: ");
        ArrayList<Component> vddNodeComponents=TopologyApi.queryDevicesWithNetlistNode("top1","vdd");

        for (Component component : vddNodeComponents){
            System.out.print("{ type: "+component.type+", id:"+component.id+"} ");
        }
        System.out.println(");

        /*
        *       results
        *
        * Devices connected to n1: { type: resistor, id:res1} { type: nmos, id:m1}
        * Devices connected to vss: { type: nmos, id:m1}
        * Devices connected to vdd: { type: resistor, id:res1}
        *
        * */


        }




}

/*
* {
  "id": "top1",
  "list": [
    {
      "resistance": {
        "Default": 100.0,
        "min": 10.0,
        "max": 1000.0
      },
      "t1": "vcc",
      "t2": "ground",
      "id": "res1",
      "type": "resistor"
    },
    {
      "Mofl": {
        "Default": 1.5,
        "min": 1.0,
        "max": 2.0
      },
      "gate": "vin",
      "drain": "n1",
      "source": "vss",
      "id": "m1",
      "type": "nmos"
    }
  ]
}

Process finished with exit code 0

* */
