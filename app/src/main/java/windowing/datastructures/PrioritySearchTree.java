package windowing.datastructures;

import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.Collections;

public class PrioritySearchTree {

    private Node data;
    private PrioritySearchTree leftTree;
    private PrioritySearchTree rightTree;
    private ArrayList<Segment> segments;
    private ArrayList<Node> nodes;
    private ArrayList<Segment> reportedSegments;

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

    public Node getData() {
        return data;
    }

    public PrioritySearchTree getLeftTree() {
        return leftTree;
    }

    public PrioritySearchTree getRightTree() {
        return rightTree;
    }

    private PrioritySearchTree() {
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
            System.out.println("Root : " + data.point.toString());

            // calculating the median and separate the remaining nodes into left and right trees 
            //double median = find_median_value(nodes);
            CompositeNumber compositeMedian = find_median(nodes);
            data.compositeMedian = compositeMedian;
            System.out.println("Median : " + compositeMedian);
            //data.median = median;
            ArrayList<Node> leftTree = new ArrayList<Node>();
            ArrayList<Node> rightTree = new ArrayList<Node>();
            /*
            for ( Node node : nodes ) {
                if ( node.point.get_coord2() <= median ) {
                    leftTree.add(node);
                }
                else {
                    rightTree.add(node);
                }
            }

             */
            for (Node node : nodes){
                if (node.point.is_y_smaller_than(compositeMedian)|| node.point.is_equal_to(compositeMedian)){
                    //System.out.println("-------- I'm adding data in left tree --------");
                    //System.out.println("Point : " + node.point.toString());
                    //System.out.println("Median : " + compositeMedian.toString());
                    leftTree.add(node);
                }
                else{
                    //System.out.println("-------- I'm adding nodes in right tree ---------");
                    //System.out.println("Point : " + node.point.toString());
                    //System.out.println("Median : " + compositeMedian.toString());
                    rightTree.add(node);
                }
            }

            // recursively construct left and right trees 
            this.leftTree = new PrioritySearchTree();
            this.rightTree = new PrioritySearchTree();
            System.out.println("Going in left tree");
            this.leftTree.construct_tree(leftTree);
            System.out.println("Going in right tree");
            this.rightTree.construct_tree(rightTree);
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
            if ( nodes.get(n).point.is_x_smaller_than(nodes.get(index).point) ) {
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
        if ( nodes.size() == 1 ) {
            return nodes.get(0).point.get_coord2();
        }
        if ( nodes.size() % 2 == 0 ) {
            return nodes.get(nodes.size()/2).point.get_coord2();
        }
        return nodes.get(nodes.size()/2 +1).point.get_coord2();
    }

    // Method to compute a CompositeNumber median in order to sort properly all points
    // considering the case where all y coordinates in the set are equals.
    private CompositeNumber find_median(ArrayList<Node> nodes){
        // Median will be a composite number (x|y) with the median on y and x on values.
        CompositeNumber median;
        if (nodes.size() == 1){
            median = new CompositeNumber(nodes.get(0).point.get_coord1(), nodes.get(0).point.get_coord2()/2);
        }
        if (nodes.size() % 2 == 0){
            median = new CompositeNumber(((nodes.get((nodes.size()/2)-1).point.get_coord1())+(nodes.get(nodes.size()/2).point.get_coord1()))/2
                    ,((nodes.get((nodes.size()/2)-1).point.get_coord2())+(nodes.get(nodes.size()/2).point.get_coord2()))/2);
        }
        else{
            median = new CompositeNumber(nodes.get((nodes.size() - 1)/2).point.get_coord1(), nodes.get((nodes.size()-1)/2).point.get_coord2());
        }
        return median;
    }

    public ArrayList<Segment> query(double xMin, double yMin, double xMax, double yMax) {
        reportedSegments = new ArrayList<Segment>();
        apply_window(xMin, yMin, xMax, yMax, reportedSegments);
        return reportedSegments;
    }

    /*
     * This is dogshit, it must be completely reworked.
     */
    private void apply_window(double xMin, double yMin, double xMax, double yMax, ArrayList<Segment> reportedSegments) {
        if ( data != null ) {
            double x = data.point.get_coord1(); 
            double y = data.point.get_coord2(); 
            // first case : at least one endpoint lie within the window
            if ( x>=xMin && x<=xMax && y>=yMin && y<=yMax ) {
                if ( !reportedSegments.contains(data.segment) ) {
                    reportedSegments.add(data.segment);
                    System.out.println("reported one segment : " + data.segment.toString()); // DEBUG
                }
            }
            if ( leftTree != null ) { 
                leftTree.apply_window(xMin, yMin, xMax, yMax, reportedSegments);
            }
            if ( rightTree != null ) { 
                rightTree.apply_window(xMin, yMin, xMax, yMax, reportedSegments);
            }
        // second case : the segment cross entierly the window i.e. no endpoint within the window
        }

    }
}
