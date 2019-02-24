import java.io.*;
import java.util.ArrayList;

class A_star {
    private Node a;                 //initial node
    private int numCities;          //keep track of path length
    private BufferedReader reader;  //to read in the file

    //intializer taking in file path and which heuristic to use
    A_star(String TSPcities, int heur) {
        //Setting up a reader for the passed text file
        File cities = new File(TSPcities);
        try {
            reader = new BufferedReader(new FileReader(cities));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //if everything goes well, read in the cities and set up the appropriate variables
        assert reader != null;
        try {
            numCities = Integer.parseInt(reader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<City> citiesGiven = new ArrayList<>(numCities);
        for(int i=0;i<numCities;i++){
            String[] info = new String[0];
            try {
                info = reader.readLine().split(" ");
            } catch (IOException e) {
                e.printStackTrace();
            }

            citiesGiven.add(new City(info[0],new Vector2(Integer.parseInt(info[1]),
                    Integer.parseInt(info[2]))));
        }

        //set up the initial city A node
        a = new Node(null, citiesGiven, citiesGiven.get(0),0, heur);
    }

    ArrayList<City> search(){
        //keep track of the current node being explored
        Node curNode = a;   //start at A
        //initialize a frontier to keep track of new nodes
        ArrayList<Node> frontier = new ArrayList<>();
        frontier.add(a);
        long startTime = System.currentTimeMillis();
        while((curNode.path().size()<(numCities+1)) && numCities != 1){

            //if a goal state has not been found, choose the next node to explore
            curNode = Choose(frontier);
            frontier.remove(curNode);               //remove it from the frontier
            curNode.expand();                       //expand it
            frontier.addAll(curNode.getChildren()); //and add it's successors to the frontier
            if(System.currentTimeMillis()>=startTime+600000){   //check if this has been running 10 minutes yet
                System.out.println("Too Long");
                return new ArrayList<City>();
            }
        }

        //curNode is still on the last node explored, being the last node of a goal state
        return curNode.path();
    }

    private Node Choose(ArrayList<Node> frontier){
        Node cheapest = null;
        for(Node edge:frontier){    //look through the frontier
            if(cheapest == null || (edge.getValue()<cheapest.getValue())){
                cheapest = edge;    //grab the next node with the shortest perceived path
            }
        }
        return cheapest;    //pass next node back
    }
}