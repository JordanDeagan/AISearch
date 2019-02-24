import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

class Sim_Anneal {
    private ArrayList<Node> curPath;        //current path stored as Nodes
    private ArrayList<Node> initPath;       //initial path stored as Nodes for all 3
    private ArrayList<City> bestPath;       //best path found so far stored as cities
    private ArrayList<City> initCity;       //initial path created stored as cities
    private ArrayList<City> citiesGiven;    //list of all cities and locations
    private BufferedReader reader;          //to read in the file
    private double bestDist;                //distance traveled along bestPath
    private double initDist;                //distance traveled along initPath
    private int numCities;                  //keep track of path length
    private Node a;                         //initial city A Node
    private Random rand;                    //allows for random sampling
    private double temp;                    //Temperature of the annealing process
    private long waitTime;                  //how long to wait normally if not prematurely exited
    private long startTime;                 //keep track of when the annealing started
    private int noImprove;                  //keeps track of how many cycles it has been since an improvement

    //initializer taking in the file location and how long to run before exiting
    Sim_Anneal(String TSPcities, long min) {
        //initialize values
        waitTime = min*60000;
        rand = new Random();
        bestPath = new ArrayList<>();
        bestDist = 0;

        //set up and read in the passed file
        File cities = new File(TSPcities);
        try {
            reader = new BufferedReader(new FileReader(cities));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assert reader != null;
        try {
            numCities = Integer.parseInt(reader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }

        //create list from file
        citiesGiven = new ArrayList<>(numCities);
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

        //create city A node
        a = new Node(null, citiesGiven, citiesGiven.get(0),0, 0);
    }

    private void initPath(){
        //initialize values for a new annealing process
        //setting curPath and bestPath to a new path
        curPath = new ArrayList<>();
        ArrayList<City> available = new ArrayList<>(citiesGiven);   //all cities except A
        available.remove(0);                                  //removing A
        noImprove = 0;

        //establish new path starting at A
        curPath.add(a);
        bestPath.add(citiesGiven.get(0));

        //add the rest of the cities randomly using the previously created list
        if(numCities>1) {  //alter approach based on number of cities
            for (int i = 1; i < numCities; i++) {
                int city = rand.nextInt(available.size());
                curPath.add(new Node(curPath.get(i - 1), citiesGiven,
                        available.get(city), curPath.get(i - 1).getCost(), 0));
                bestPath.add(available.remove(city));
            }

            //add A back in as final stop
            curPath.add(new Node(curPath.get(curPath.size() - 1), citiesGiven, citiesGiven.get(0),
                    curPath.get(curPath.size() - 1).getCost(), 0));
            bestPath.add(citiesGiven.get(0));
            bestDist = curPath.get(numCities).getCost();
        } else{
            bestDist = curPath.get(0).getCost();
        }

        //set initPath, initCity, and initDist
        initPath = curPath;
        initCity = bestPath;
        initDist = bestDist;
    }

    //initial search with first crak at temp
    ArrayList<City> search1(){
        //intialize new search
        initPath();
        //see initial path length
        System.out.println(bestDist);
        startTime = System.currentTimeMillis();
        while(System.currentTimeMillis()<(startTime+waitTime) && noImprove<1000){
            //keep track of time and alter temp at set periods
            annealSchedule(.75, .5, .25, .10, .01);

            //if there is not enough cities, exit, else keep running
            if(numCities>2) {
                search();
            } else{
                noImprove = 1001;
            }
        }
        return bestPath;
    }

    //new version with annealing schedule that starts higher and lowers slower
    ArrayList<City> search2(){
        //set inits to the same as search 1
        curPath = initPath;
        bestPath = initCity;
        bestDist = initDist;
        noImprove = 0;
        System.out.println(bestDist);
        startTime = System.currentTimeMillis();
        while(System.currentTimeMillis()<(startTime+waitTime) && noImprove<1000){
            annealSchedule(1, .66, .33, .15, 0);
            if(numCities>2) {
                search();
            } else{
                noImprove = 1001;
            }
        }
        return bestPath;
    }

    //new version with annealing schedule starting at a recomended temp and lowers quicker
    ArrayList<City> search3(){
        //set inits to the same as search 1
        curPath = initPath;
        bestPath = initCity;
        bestDist = initDist;
        noImprove = 0;
        System.out.println(bestDist);
        startTime = System.currentTimeMillis();
        while(System.currentTimeMillis()<(startTime+waitTime) && noImprove<1000){
            annealSchedule(.80, .40, .20, .10, .05);
            if(numCities>2) {
                search();
            } else{
                noImprove = 1001;
            }
        }
        return bestPath;
    }

    //search method for code reuse
    private void search(){
        //establish two nodes for switching
        int node1 = ThreadLocalRandom.current().nextInt(1, numCities);  //location of the first swapped city
        int node2 = 0;                         //location of the second swapped city
        while (node2 == 0 || node2 == node1) {
            node2 = ThreadLocalRandom.current().nextInt(1, numCities);
        }

        //swap based on the two nodes
        ArrayList<Node> newPath = swap(curPath, node1, node2);
        noImprove += 1;

        //check the difference between the current path and new path
        //new path is better then the current path
        if (newPath.get(numCities).getCost() < curPath.get(numCities).getCost()) {
            //new path is better than the best path so far
            if (newPath.get(numCities).getCost() < bestDist) {
                moveUp(newPath);
            } else {    //new path is only better than current path
                curPath = newPath;
            }
            //reset the improvement tracker
            noImprove = 0;
        } else if (rand.nextDouble() < temp) {  //new path is worse, jump based on temp
            curPath = newPath;
        }
    }

    private void annealSchedule(double start, double third, double half, double twoThird, double lastQuarter){
        if (System.currentTimeMillis()<(startTime+(waitTime/3))){
            //ensuring temp is set to initial value
            temp = start;
        } else if(System.currentTimeMillis()==(startTime+(waitTime/3))){
            // after one-third of the time has passed lower the temp to the second value
            temp = third;
            System.out.println(bestDist + " 1/3");
        } else if(System.currentTimeMillis()==(startTime+(waitTime/2))){
            // after half of the time has passed lower the temp to the third value
            temp = half;
            System.out.println(bestDist + " 1/2");
        } else if(System.currentTimeMillis()==(startTime+((2*waitTime)/3))){
            // after two-thirds of the time has passed lower the temp to the fourth value
            temp = twoThird;
            System.out.println(bestDist + " 2/3");
        } else if(System.currentTimeMillis()==(startTime+((3*waitTime)/4))){
            // after three-fourths of the time has passed lower the temp to the final value
            temp = lastQuarter;
            System.out.println(bestDist + " 3/4");
        }
    }

    //method called if new path is better than the previous best path
    private void moveUp(ArrayList<Node> newPath){
        ArrayList<City> alterable = new ArrayList<>();
        curPath = newPath;
        for(Node city:curPath){
            alterable.add(city.getCity());
        }
        bestPath = alterable;
        bestDist = curPath.get(numCities).getCost();
//        System.out.println(bestDist + " || "+(System.currentTimeMillis()-startTime));
    }

    double getBestDist(){
        return bestDist;
    }

    //method for creating the new path during a swap
    private ArrayList<Node> swap(ArrayList<Node> prev, int nodeOne, int nodeTwo){
        //create the new path
        ArrayList<Node> next = new ArrayList<>();
        next.add(prev.get(0));                  //add initial City A
        for(int i=1;i<prev.size();i++){
            if( i!=nodeOne && i!=nodeTwo){      //add the cities not being swapped
                next.add(new Node(next.get(i-1),citiesGiven,prev.get(i).getCity(),next.get(i-1).getCost(), 0));
            }
            else if(i == nodeOne){              //add the first of the swapped cities into it's new location
                next.add(new Node(next.get(i-1),citiesGiven,prev.get(nodeTwo).getCity(),next.get(i-1).getCost(), 0));
            }
            else {                              //add the other swapped city
                next.add(new Node(next.get(i-1),citiesGiven,prev.get(nodeOne).getCity(),next.get(i-1).getCost(), 0));
            }
        }
        return next;
    }
}
