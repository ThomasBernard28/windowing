package windowing.datastructures;

import java.util.ArrayList;

public class PrioritySearchTree {

    private Node root;

    public PrioritySearchTree(Node root){
        this.root = root;
    }

    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }


    /**
     * This method is an adaptation (to our specific needs) of the pseudo code algorithm from our report.
     * The method only modify the PST by adding new nodes into it.
     * @param segments Set of segments that have to be inserted in the PST
     * @param tree initial tree/substree empty
     */
    public void construct_tree(ArrayList<Segment> segments, PrioritySearchTree tree){
        ArrayList<Segment> rootSegments = new ArrayList<Segment>();

        //TODO tester si ce n'est pas plus opti de trier segments avant (notamment pour la médiane)
        if (segments.size() > 1){
            Segment min = find_min(segments);
            segments.remove(min);
            double median = find_median(segments);
            tree.root = new Node(median, min);
            //Reduce the set to compute the median on the remaining set. Min will figure in the current tree/substree root.


        }
        else if (segments.size() == 1){
            rootSegments.add(segments.get(0));
            tree.root = new Node(0 , segments.get(0));
        }
        else{
            throw new IllegalArgumentException("The size of the segment sets must be at lest 1");
        }

    }

    /**
     * Method to find the segment with the minimal x coordinate in the set.
     * @param segments Set of differents segments that are not yet inserted in the PST.
     * @return min : the minimal segment that should figure in the root of the current tree/subtree.
     */
    private Segment find_min(ArrayList<Segment> segments){
        //TODO: Voir comment opti cette méthode au niveau de l'extraction des coordonnées.
        int index = 0;
        Segment min = segments.get(0);
        while (index < segments.size()){
            Segment temp = segments.get(index);
            //Si la plus petite coordonnée x de temp est strictement inférieure à la plus petite coordonnée x du min courant
            if (temp.get_xComp().get_coord1() < min.get_xComp().get_coord1()){
                min = temp;
                index ++;
            }
            //Si les 2 coordonnées x sont égales on va regarder celui qui le plus petit x'
            else if (temp.get_xComp().get_coord1() == min.get_xComp().get_coord1()){
                if (temp.get_xComp().get_coord2() < min.get_xComp().get_coord2()){
                    min = temp;
                    index ++;
                }
            }
            else{
                index ++;
            }
        }
        return min;
    }

    private double find_median(ArrayList<Segment> segments){
        int index = find_median_index(segments);
        Segment segment = segments.get(index);
        CompositeNumber yComp = segment.get_yComp();
        return (yComp.get_coord1() + yComp.get_coord2())/2;
    }

    private int find_median_index(ArrayList<Segment> segments){
        // If the number of segments is even
        if (segments.size() % 2 == 0){
            return (segments.size() /2 )-1;
        //Else the number of segments is odd
        }else{
            return ((segments.size() + 1) /2 ) -1;
        }
    }
}
