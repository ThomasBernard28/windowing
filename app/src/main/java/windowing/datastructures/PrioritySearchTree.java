package windowing.datastructures;

import java.util.ArrayList;

public class PrioritySearchTree {

    private Node root;

    private PrioritySearchTree lson;

    private PrioritySearchTree rson;

    public PrioritySearchTree(Node root, PrioritySearchTree lson, PrioritySearchTree rson){
        this.root = root;
        this.lson = lson;
        this.rson = rson;
    }

    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }

    public PrioritySearchTree getLson() {
        return lson;
    }

    public PrioritySearchTree getRson() {
        return rson;
    }

    public void setLson(PrioritySearchTree lson) {
        this.lson = lson;
    }

    public void setRson(PrioritySearchTree rson) {
        this.rson = rson;
    }

    /**
     * This method is an adaptation (to our specific needs) of the pseudo code algorithm from our report.
     * The method only modify the PST by adding new nodes into it.
     * @param segments Set of segments that have to be inserted in the PST
     * @param tree initial tree/substree empty
     */
    public PrioritySearchTree construct_tree(ArrayList<Segment> segments, PrioritySearchTree tree){

        //TODO tester si ce n'est pas plus opti de trier segments avant (notamment pour la médiane)
        if (segments.size() > 1){
            Segment min = find_min(segments);
            segments.remove(min);

            //Compute the median
            int index = find_median_index(segments);
            Segment segment = segments.get(index);
            CompositeNumber yComp = segment.get_yComp();
            double median = (yComp.get_coord1() + yComp.get_coord2())/2;
            tree.root = new Node(median, min);

            //Reduce the set to compute the median on the remaining set. Min will figure in the current tree/substree root.
            ArrayList<Segment> leftPart = (ArrayList<Segment>) segments.subList(0, index);
            ArrayList<Segment> rightPart = (ArrayList<Segment>) segments.subList(index, segments.size() - 1);
            tree.setLson(construct_tree(leftPart, tree.lson));
            tree.setRson(construct_tree(rightPart, tree.rson));

        }
        else if (segments.size() == 1){
            tree.root = new Node(0 , segments.get(0));
            segments.remove(0);
        }
        return tree;
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
