
class Resistor extends Component{

    Resistance resistance;

    class Netlist{
        String t1,t2;
        Netlist(){};
        Netlist(String t1,String t2){
            this.t1=t1;
            this.t2=t2;
        };

    }



    Netlist netlist;

    @Override
    boolean isConnectedTo(String netlistNodeId) {
        return netlist.t1.equals(netlistNodeId) ||netlist.t2.equals(netlistNodeId);
    }
    Resistor(String id, Resistance resistance, String t1, String t2) {
        super(id,"resistor");
        this.id=id;
        this.resistance = resistance;
        netlist= new Netlist(t1, t2);

    }
    Resistor(String id, double def ,double min,double max, String t1, String t2) {
        super(id,"resistor");
        this.id=id;
        this.resistance = new Resistance(def,min,max);
        netlist= new Netlist(t1, t2);


    }


}
