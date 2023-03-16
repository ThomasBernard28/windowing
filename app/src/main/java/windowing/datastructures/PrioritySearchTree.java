package windowing.datastructures;

import java.util.ArrayList;
import java.util.Collections;

public class PrioritySearchTree {

    private Node data;
    private PrioritySearchTree leftTree;
    private PrioritySearchTree rightTree;
    private ArrayList<Segment> segments;
    private ArrayList<Node> nodes;

    public PrioritySearchTree(ArrayList<Segment> segments) {
        this.segments = segments;
        nodes = new ArrayList<Node>();
        // we start by creating our set of points i.e. all segments endpoints 
        for ( Segment s : segments ) {
            nodes.add(new Node(s.get_startComp(), s));
            nodes.add(new Node(s.get_endComp(), s));
        }
        // points are sorted to improve the efficiency when calculating the median
        quicksort(nodes, 0, nodes.size()-1);
        // DEBUG nodes.forEach((n) -> System.out.println(n));
        // now we can construct the tree 
        construct_tree(nodes);
    }

    private PrioritySearchTree(Node data) {
        this.data = data;
    }

    public ArrayList<Node> get_nodes() {
        return nodes;
    }

    /**
     */
    private Node construct_tree(ArrayList<Node> nodes){
        if ( nodes.size() > 1 ) {

            // finding the root of the tree
            data = nodes.get(find_min_index(nodes));
            nodes.remove(data);

            // calculating the median and separate the remaining nodes into left and right trees 
            double median = find_median_value(nodes);
            ArrayList<Node> leftTree = new ArrayList<Node>();
            ArrayList<Node> rightTree = new ArrayList<Node>();
            for ( Node node : nodes ) {
                if ( node.point.get_coord2() <= median ) {
                    leftTree.add(node);
                }
                else {
                    rightTree.add(node);
                }
            }

            // recursively construct left and right trees 
            this.leftTree = new PrioritySearchTree(construct_tree(leftTree));
            this.rightTree = new PrioritySearchTree(construct_tree(rightTree));
            return data;
        }

        if ( nodes.size() == 1 ) {
            data = nodes.get(0); 
            nodes.clear();
        }
        return null;
    }

    /**
     */
    private int find_min_index(ArrayList<Node> nodes){
        int index = 0;
        for ( int n=1; n<nodes.size(); n++ ) {
            if ( nodes.get(index).point.is_x_smaller_than(nodes.get(index).point) ) {
                index = n;
            }
        }
        return index;
    }

    private void quicksort(ArrayList<Node> nodes, int start, int end) {
        if ( start < end ) {
            int part = partition(nodes, start, end);
            quicksort(nodes, start, part-1);
            quicksort(nodes, part+1, end);
        }
    }

    private int partition(ArrayList<Node> nodes, int start, int end) {
        Node node = nodes.get(end);
        int i = start - 1;
        for ( int j=start; j<=end-1; j++ ) {
            if ( nodes.get(j).point.is_y_smaller_than(node.point) ) {
                i++;
                Collections.swap(nodes, i, j);
            }
        }
        Collections.swap(nodes, i+1, end);
        return i+1;
    }

    private double find_median_value(ArrayList<Node> nodes){
        // If the number of nodes is even
        if ( nodes.size() % 2 == 0 ){
            return (nodes.size() /2 )-1;
        }
        //Else the number of nodes is odd
        return ((nodes.size() + 1) /2 ) -1;
    }

}
