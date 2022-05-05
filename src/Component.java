abstract class Component{
    String id;
    String type;
    Component(){}
    Component(String id, String type){
        this.id=id;
        this.type=type;
    }
    abstract  boolean isConnectedTo(String netlistNodeId);
}