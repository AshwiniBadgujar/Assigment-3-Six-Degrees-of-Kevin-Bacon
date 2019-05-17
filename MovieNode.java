import java.util.ArrayList;

public class MovieNode{

    private String movieName = "";
    ArrayList<ActorNode> actorList = new ArrayList<ActorNode>();
    char[] lowercase = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

    public MovieNode(String movie){
        movieName = movie;
    }

    void addActor(ActorNode actorName){
        actorList.add(actorName);
    }

    public String toString(){
        return movieName;
    }
}