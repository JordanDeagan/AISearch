class City {
    private String city;
    private Vector2 place;
    City(String name, Vector2 loc){
        city = name;
        place = loc;
    }

    String getCity(){
        return city;
    }
    Vector2 getPlace(){
        return place;
    }
}
