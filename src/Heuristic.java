import java.util.ArrayList;

class Heuristic {
    //private initializer
    private Heuristic(){}

    //method for getting euclidian distance between two passed cities
    private static double EuclidDist(Vector2 city1, Vector2 city2){
        double xResult = Math.pow((city1.getX()-city2.getX()),2);       //(x1 - x2)^2
        double yResult = Math.pow((city1.getY()-city2.getY()),2);       //(y1 - y2)^2
        return Math.sqrt(xResult+yResult);                              //return square root
    }

    //method for adding the distance from a parent to currnet Node to cost
    static double traveled(double travle, Vector2 city1, Vector2 city2){
        return (travle + EuclidDist(city1, city2));
    }

    //my heuristic, after multiple revisions
    static double aStarHeur(Node cur, ArrayList<City> rem, City a){
        if(rem.size() == 0){    //if there are no remaining cities, this is the second visit to A
            return 0;           //it is a goal state, so it has a Heuristic 0
        } else if(rem.size() == 1 && rem.get(0).getCity().equals("A")) {    //if A is the only reamining city
            return EuclidDist(cur.getLoc(), a.getPlace());                  //return the distance to it
        } else{
            double estimate = 0;        //returned value
            ArrayList<City> allCities = new ArrayList<>();  //list of all cities remaining
            allCities.add(cur.getCity());                   //including the city this is for
            allCities.addAll(rem);                          //all possible children
            allCities.add(a);                               //and a return trip to A

            //create a memory for each city
            ArrayList[] cityMemory = new ArrayList[allCities.size()];
            for(int i=0; i<allCities.size();i++){
                cityMemory[i] = new ArrayList<Integer>();
            }

            for (int i = 0;i<allCities.size()-2;i++) {  //go through all cities on the list except for A
                double shortest = 0;                    //value of shortest distance to another city
                int visited = 0;                        //which city that was to
                for (int j = 1;j<allCities.size()-1;j++) {          //travel to every other city
                    if(j != i || !cityMemory[i].contains(j)) {      //check if it has traveled to you before
                        double check = EuclidDist(allCities.get(i).getPlace(), allCities.get(j).getPlace());
                        if (shortest == 0 || check < shortest) {    //if the distance to this city is the current shortest
                            shortest = check;               //record the distance to check against
                            visited = j;                    //record the city for memory
                        }
                    }
                }
                estimate+=shortest;         //add the distance to heuristic value
                cityMemory[visited].add(i); //let the city know it was visited by the current city
            }
            return estimate;
        }
    }

    //Hunt's heuristic,
    static double aStarHunt(Node cur, ArrayList<City> rem, City a){
        if(rem.size() == 0){
            return 0;
        } else if(rem.size() == 1 && rem.get(0).getCity().equals("A")) {
            return EuclidDist(cur.getLoc(), a.getPlace());
        } else{
            double estimate = 0;
            ArrayList<City> allCities = new ArrayList<>();
            allCities.add(cur.getCity());
            allCities.addAll(rem);
            allCities.add(a);
            for (int i = 0;i<allCities.size()-2;i++) {
                double shortest = 0;
                for (int j = 1;j<allCities.size()-1;j++) {
                    if(j != i ) {
                        double check = EuclidDist(allCities.get(i).getPlace(), allCities.get(j).getPlace());
                        if (shortest == 0 || check < shortest) {
                            shortest = check;
                        }
                    }
                }
                estimate+=shortest;
            }
            return estimate;
        }
    }

    //our combined heuristic
    static double aStarCombo(Node cur, ArrayList<City> rem, City a){
        if(rem.size() == 0){    //if the city is a goal state
            return 0;
        } else if(rem.size() == 1 && rem.get(0).getCity().equals("A")) {    //if the next city is a goal state
            return EuclidDist(cur.getLoc(), a.getPlace());
        } else{
            double longest = 0;     //the longest distance from the current city to any unexplored city
            double shortest = 0;    //the shortest distance from any unexplored city to A
            for(City other: rem){   //check all cities for distance from the current city and A, and record results
                double longCheck = EuclidDist(cur.getLoc(), other.getPlace());
                double shortCheck = EuclidDist(a.getPlace(), other.getPlace());
                if(longest == 0 || longCheck>longest){
                    longest = longCheck;
                }
                if(shortest == 0 || shortCheck<shortest){
                    shortest = shortCheck;
                }
            }
            return shortest+longest; //add the two distances together and return them
        }
    }
}