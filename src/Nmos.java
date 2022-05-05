
class Nmos extends Component{
    Ml ml;



    class Netlist {
        String gate, drain, source;

        public Netlist(String gate, String drain, String source) {
            this.gate = gate;
            this.drain = drain;
            this.source = source;
        }

    }

    Netlist netlist;
    public Nmos(String id, Ml ml, String gate, String drain, String source) {
        super(id,"nmos");
        this.id=id;
        this.ml = ml;
        netlist=new Netlist(gate,drain,source);
    }
    public Nmos(String id, double def,double min , double max, String gate, String drain, String source) {
        super(id,"nmos");
        this.id=id;
        this.ml = new Ml(def,min,max);
        netlist=new Netlist(gate,drain,source);
    }
    @Override
    boolean isConnectedTo(String netlistNodeId) {
        return netlist.gate.equals(netlistNodeId) ||netlist.drain.equals(netlistNodeId)||netlist.source.equals(netlistNodeId);
    }
}
