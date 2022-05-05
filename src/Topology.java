import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Topology {
    String id;
    ArrayList<Component> list=new ArrayList<>();

}

 class NetlistItem{
    String node;
    String net;
}

 abstract class Component{
    String id;
    String type;
    ArrayList<NetlistItem> netlist;
    abstract String to_json();
}
class Resistance{
    double Default;
    double min;
    double max;
    Resistance(double Default,double min,double max){
        this.Default=Default;
        this.min=min;
        this.max=max;
    }
}

class ml{
    double Default;
    double min;
    double max;
    ml(double Default,double min,double max){
        this.Default=Default;
        this.min=min;
        this.max=max;
    }

}


class Resistor extends Component{
    Resistance resistance;
    String t1,t2;
    public Resistor(String id, Resistance resistance, String t1, String t2) {
        type="resistor";
        this.id=id;
        this.resistance = resistance;
        this.t1 = t1;
        this.t2 = t2;
    }

    @Override
    String to_json(){
        String str =
                "{\n\"type\": \""+type+"\"," +
                        "\n\"id\": \""+super.id+"\"," +
                        "\n\"resistance\":{" +
                        "\n\t\"default\":"+resistance.Default+"," +
                        "\n\t\"min\":"+resistance.min+"," +
                        "\n\t\"max\":"+ resistance.max+"\n\t}," +
                        "\n\"netlist\":{" +
                        "\n\t\"t1\":\""+t1+"\"," +
                        "\n\t\"t2\":\""+t2+"\"" +
                        "\n\t}\n}" ;

        return str;
    }
}

class Nmos extends Component{
    ml Mofl;
    String gate,drain,source;

    public Nmos(String id,ml mofl, String gate, String drain, String source) {
        this.id=id;
        type="nmos";
        Mofl = mofl;
        this.gate = gate;
        this.drain = drain;
        this.source = source;
    }

    String to_json(){
        String str =
                "{\n\"type\": \""+type+"\"," +
                        "\n\"id\": \""+super.id+"\"," +
                        "\n\"m(l)\":{" +
                        "\n\t\"default\":"+Mofl.Default+"," +
                        "\n\t\"min\":"+Mofl.min+"," +
                        "\n\t\"max\":"+ Mofl.max+"}\n," +
                        "\n\"netlist\":{" +
                        "\n\t\"drain\":\""+drain+"\"," +
                        "\n\t\"gate\":\""+gate+"\"," +
                        "\n\t\"source\":\""+source+"\"\n\t}\n" ;

        return str;
    }
}


class JSONInterperter{
    static Boolean wrtieTopology(Topology topology, String pathname){
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

        StringBuilder topologystr= new StringBuilder("\"topology\": {\n \"id\":\""+topology.id+"\",\n\"components\":[");
        for (Component component: topology.list){
            topologystr.append(component.to_json()).append(",\n");
        }
        topologystr.append("]\n}");

        try {
            FileWriter myWriter = new FileWriter(pathname);
            myWriter.write(String.valueOf(topologystr));
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
            return true;
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return false;
        }


    }
}





class Main {

    public static void main(String[] args) {
        Resistor a=new Resistor("res1",new Resistance(100,10,1000),"vcc","ground");
        Nmos nmos=new Nmos("m1",new ml(1.5,1,2),"vin","n1","vss");
        Topology c = new Topology();
        c.list.add(a);
        c.list.add(nmos);
        c.id="top1";
        JSONInterperter.wrtieTopology(c,"test.json");
    }
}
