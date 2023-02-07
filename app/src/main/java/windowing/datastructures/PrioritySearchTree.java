package windowing.datastructures;

import java.util.ArrayList;

public class PrioritySearchTree {

    private final int SIZE;
    private final Segment[] structure;

    public PrioritySearchTree(int SIZE){
        this.SIZE = SIZE;
        this.structure = new Segment[SIZE];
    }

    public void construct_tree(ArrayList<Segment> segments, PrioritySearchTree tree){
        //TODO tester si ce n'est pas plus opti de trier segments avant (notamment pour la médiane)
        if (segments.size() > 1){
            Segment min = find_min(segments);
            tree.structure[0] = min;
            //Reduce the set to compute the median on the remaining set. Min will figure in the current tree/substree root.
            segments.remove(min);

        }
        else if (segments.size() == 1){
            tree.structure[0] = segments.get(0);
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

}
