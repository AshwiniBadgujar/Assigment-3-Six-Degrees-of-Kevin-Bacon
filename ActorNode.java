import java.util.ArrayList;

public class ActorNode{

    String actorName = "";
    ArrayList<MovieNode> movieList = new ArrayList<MovieNode>();
    char[] lowercase = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

    public ActorNode(String actor){
        actorName = actor;
    }

    void addMovie(MovieNode movieTitle){
        movieList.add(movieTitle);
    }

    public String toString(){
        return actorName;

    }

}