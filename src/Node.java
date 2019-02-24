import java.util.ArrayList;

class Node {
    final private ArrayList<Node> children; //list of all successors of this Node
    private ArrayList<City> citiesLeft;     //all possible cities that can be visited from this Node
    private ArrayList<City> citiesGiven;    //all cities in the scenario
    private double heuristic;               //the heuristic value given to this Node
    private double cost;                    //the cost it took to reach this Node
    private double value;                   //total added value of cost and heuristic
    private City city;                      //what city this Node represents
    private Node parent;                    //what Node this Node is a successor of
    private int type;                       //which heuristic function to use

    //initializer, taking in the parent node, list of all cities, what city this node
    //represents, what it cost to get to this node so far, and what heuristic to use
    Node(Node sent, ArrayList<City> allCities, City location, double pastCost, int heur){
        //set up variables
        citiesGiven = allCities;
        city = location;
        parent = sent;
        type = heur;
        children = new ArrayList<>();

        //create list of cities left to be visited based on the path already traveled
        citiesLeft = new ArrayList<>(allCities);
        for(City seen:this.path()) {
            citiesLeft.remove(seen);
        }
        //if this Node's path has passed through every city once, but not traveled back
        //to A, add A as the only successor to this Node
        if(citiesLeft.size()==0 && !city.getCity().equals("A")) {
            citiesLeft.add(citiesGiven.get(0));
        }

        //calculate the cost to get to this Node
        if(parent!=null) {
            cost = Heuristic.traveled(pastCost, parent.getLoc(), city.getPlace());
        }else {
            cost = 0;
        }

        //calculate the heuristic value of this Node based on passed argument
        if(heur == 1) {         //use my initial heuristic
            heuristic = Heuristic.aStarHeur(this, citiesLeft, citiesGiven.get(0));
            value = cost + heuristic;
        } else if(heur == 2) {  //user Hunt's heuristic
            heuristic = Heuristic.aStarHunt(this, citiesLeft, citiesGiven.get(0));
            value = cost + heuristic;
        } else if(heur == 3){   //use the heuristic we created together
            heuristic = Heuristic.aStarCombo(this, citiesLeft, citiesGiven.get(0));
            value = cost + heuristic;
        }
    }

    //method for grabbing this Node's traveled path
    ArrayList<City> path(){
        ArrayList<City> nPath;  //where to store the path
        if(parent == null){     //if this Node is the initial Node in a path, only add itself and return
            nPath = new ArrayList<>();
            nPath.add(city);
        } else{                 //otherwise, get the path of this Node's parent and add itself before returning
            nPath = parent.path();
            nPath.add(city);
        }
        return nPath;
    }

    //method for generating children
    void expand(){
        //only add children if there are cities left to visit
        if(citiesLeft.size() > 0) {
            for (City city1 : citiesLeft) { //add each city left as a successor of this Node
                children.add(new Node(this, citiesGiven, city1, cost, type));
            }
        }

    }

    //get methods//

    Vector2 getLoc(){
        return city.getPlace();
    }

    double getCost() {
        return cost;
    }

    City getCity(){
        return city;
    }

    ArrayList<Node> getChildren(){
        return children;
    }

    double getValue(){
        return value;
    }

    double getHeuristic(){
        return heuristic;
    }
}
