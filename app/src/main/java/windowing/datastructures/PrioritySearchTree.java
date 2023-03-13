package windowing.datastructures;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static windowing.datastructures.Point.compareX;

public class PrioritySearchTree {

    Node root;
    PrioritySearchTree leftSubTree;
    PrioritySearchTree rightSubTree;

    public PrioritySearchTree(Node root){
        this.root=root;
    }

    public Node getRoot() {
        return root;
    }

    public PrioritySearchTree getLeftSubTree() {
        return leftSubTree;
    }

    public PrioritySearchTree getRightSubTree() {
        return rightSubTree;
    }

    public void setRoot(Node root) {
        this.root = root;
    }

    public void setLeftSubTree(PrioritySearchTree leftSubTree) {
        this.leftSubTree = leftSubTree;
    }

    public void setRightSubTree(PrioritySearchTree rightSubTree) {
        this.rightSubTree = rightSubTree;
    }

    /**
     * This method is an adaptation (to our specific needs) of the pseudo code algorithm from our report.
     * The method only modify the PST by adding new nodes into it.
     * @param points Set of points that have to be inserted in the PST
     */
    public PrioritySearchTree construct_tree(ArrayList<Point> points){
        PrioritySearchTree tree = null;

        if (points.size() > 1){
            Point min = find_min(points);
            points.remove(min);

            //Compute the median
            int index = find_median_index(points);
            Point point = points.get(index);
            CompositeNumber median = new CompositeNumber(point.getY(), point.getX());
            Node root = new Node(min, median);

            ArrayList<Point> leftPart = (ArrayList<Point>) points.subList(0, index);
            ArrayList<Point> rightPart = (ArrayList<Point>) points.subList(index, points.size() - 1);
            tree = new PrioritySearchTree(root);
            tree.setLeftSubTree(construct_tree(leftPart));
            tree.setRightSubTree(construct_tree(rightPart));

        }
        else if (points.size() == 1) {
            Node root = new Node(points.get(0), new CompositeNumber(points.get(0).getY(), points.get(0).getX()));
            points.remove(0);
            tree = new PrioritySearchTree(root);
        }
        return tree;
    }

    /**
     * Method to find the point with the minimal x coordinate in the set.
     * @param points Set of different points that are not yet inserted in the PST.
     * @return min : the minimal point that should figure in the root of the current tree/subtree.
     */
    private Point find_min(ArrayList<Point> points){
        int index = 0;
        Point min = points.get(0);
        while(index < points.size()){

            Point temp = points.get(index);
            int comparator = compareX(temp, min);

            switch (comparator){
                case 0:
                    index ++;
                    break;
                case 1:
                    min = temp;
                    index++;
                    break;
                default:
                    break;
            }
        }
        return min;
    }

    private int find_median_index(ArrayList<Point> points){
        // If the number of segments is even
        if (points.size() % 2 == 0){
            return (points.size() /2 )-1;
        //Else the number of segments is odd
        }else{
            return ((points.size() + 1) /2 ) -1;
        }
    }
}
