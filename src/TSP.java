import java.util.ArrayList;

public class TSP {
    public static void main(String[] args){
        A_star aStarTest;
        long startTime, endTime, localTime;
        aStarTest = new A_star("./randTSP/problem36", 1);

        //run all 16 folders with my heuristic//

//        for (int c = 1; c <= 16; c++) {
//            for (int i = 1; i < 11; i++) {
//                aStarTest = new A_star("./randTSP/" + c + "/instance_" + i + ".txt", 1);
//
//                startTime = System.currentTimeMillis();
//                ArrayList<City> path2 = aStarTest.search();
//                endTime = System.currentTimeMillis();
//
//                localTime = endTime - startTime;
//                System.out.println("Time taken instance " + i + ": " + localTime + " milliseconds");
//            }
//            System.out.println("Finished all instances of " + c + " cities with my heuristic.\n");
//        }

        //run all 16 folders with Hunt's heuristic//

//        for (int c = 1; c <= 16; c++) {
//            for (int i = 1; i < 11; i++) {
//                aStarTest = new A_star("./randTSP/" + c + "/instance_" + i + ".txt", 2);
//
//                startTime = System.currentTimeMillis();
//                ArrayList<City> path2 = aStarTest.search();
//                endTime = System.currentTimeMillis();
//
//                localTime = endTime - startTime;
//                System.out.println("Time taken instance " + i + ": " + localTime + " milliseconds");
//            }
//            System.out.println("Finished all instances of " + c + " cities with Hunt's heuristic.\n");
//        }

        //run all 16 folders with the newly created heuristic//

//        for (int c = 1; c <= 16; c++) {
//            for (int i = 1; i < 11; i++) {
//                aStarTest = new A_star("./randTSP/" + c + "/instance_" + i + ".txt", 3);
//
//                startTime = System.currentTimeMillis();
//                ArrayList<City> path2 = aStarTest.search();
//                endTime = System.currentTimeMillis();
//
//                localTime = endTime - startTime;
//                System.out.println("Time taken instance " + i + ": " + localTime + " milliseconds");
//            }
//            System.out.println("Finished all instances of " + c + " cities with our heuristic.\n");
//        }

        //run the three schedules on the 36 city instance//

//        Sim_Anneal anealTest = new Sim_Anneal("./randTSP/problem36", 5);
//        ArrayList<City> path1 = anealTest.search1();
//        System.out.println(anealTest.getBestDist() + "\n");
//
//        ArrayList<City> path2 = anealTest.search2();
//        System.out.println(anealTest.getBestDist() + "\n");
//
//        ArrayList<City> path3 = anealTest.search3();
//        System.out.println(anealTest.getBestDist() + "\n");

        //compare A_Star and Sim_Anneal on a folder to compare path distances//

//        int size = 16;
//        for(int i = 1; i<11; i++){
//
//            A_star aStarTest = new A_star("./randTSP/"+size+"/instance_" + i + ".txt", 1);
//            ArrayList<City> path1 = aStarTest.search();
//
//
//            Sim_Anneal anealTest = new Sim_Anneal("./randTSP/"+size+"/instance_" + i + ".txt", 2);
//            ArrayList<City> path2 = anealTest.search1();
//            System.out.println(aStarTest.curNode.getCost() + " || "+anealTest.getBestDist());
//        }
    }


}
