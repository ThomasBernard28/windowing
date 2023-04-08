package windowing.datastructures;

import java.util.ArrayList;
import java.util.Collections;

public class PrioritySearchTree {

    private Node data;
    private PrioritySearchTree leftTree;
    private PrioritySearchTree rightTree;
    private ArrayList<Segment> segments;
    private ArrayList<Node> nodes;
    private ArrayList<Segment> reportedSegments;

    private ArrayList<PrioritySearchTree> reportedSubTrees;

    public PrioritySearchTree(ArrayList<Segment> segments) {
        this.segments = segments;
        nodes = new ArrayList<Node>();
        // we start by creating our set of points i.e. all segments endpoints 
        for (Segment s : segments) {
            // We only need to take the starting component for the tree
            nodes.add(new Node(s.get_startComp(), s));
            //nodes.add(new Node(s.get_endComp(), s));
        }
        // points are sorted to improve the efficiency when calculating the median
        quicksort(nodes, 0, nodes.size() - 1);
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
     *
     */
    private Node construct_tree(ArrayList<Node> nodes) {
        if (nodes.size() > 1) {

            // finding the root of the tree
            data = nodes.get(find_min_index(nodes));
            nodes.remove(data);

            // computing the median and separate the remaining nodes into left and right trees
            CompositeNumber compositeMedian = find_median(nodes);
            data.compositeMedian = compositeMedian;

            ArrayList<Node> leftTree = new ArrayList<Node>();
            ArrayList<Node> rightTree = new ArrayList<Node>();

            for (Node node : nodes) {
                if (node.point.is_y_smaller_than(compositeMedian) || node.point.is_equal_to(compositeMedian)) {
                    //System.out.println("-------- I'm adding data in left tree --------");
                    //System.out.println("Point : " + node.point.toString());
                    //System.out.println("Median : " + compositeMedian.toString());
                    leftTree.add(node);
                } else {
                    //System.out.println("-------- I'm adding nodes in right tree ---------");
                    //System.out.println("Point : " + node.point.toString());
                    //System.out.println("Median : " + compositeMedian.toString());
                    rightTree.add(node);
                }
            }

            // recursively construct left and right trees 
            this.leftTree = new PrioritySearchTree();
            this.rightTree = new PrioritySearchTree();
            this.leftTree.construct_tree(leftTree);
            this.rightTree.construct_tree(rightTree);
            return data;
        }

        if (nodes.size() == 1) {
            data = nodes.get(0);
            CompositeNumber median = find_median(nodes);
            data.compositeMedian = median;
            nodes.clear();
        }
        return null;
    }


    private int find_min_index(ArrayList<Node> nodes) {
        int index = 0;
        for (int n = 1; n < nodes.size(); n++) {
            if (nodes.get(n).point.is_x_smaller_than(nodes.get(index).point)) {
                index = n;
            }
        }
        return index;
    }

    private void quicksort(ArrayList<Node> nodes, int start, int end) {
        if (start < end) {
            int part = partition(nodes, start, end);
            quicksort(nodes, start, part - 1);
            quicksort(nodes, part + 1, end);
        }
    }

    private int partition(ArrayList<Node> nodes, int start, int end) {
        Node node = nodes.get(end);
        int i = start - 1;
        for (int j = start; j <= end - 1; j++) {
            if (nodes.get(j).point.is_y_smaller_than(node.point)) {
                i++;
                Collections.swap(nodes, i, j);
            }
        }
        Collections.swap(nodes, i + 1, end);
        return i + 1;
    }

    private double find_median_value(ArrayList<Node> nodes) {
        if (nodes.size() == 1) {
            return nodes.get(0).point.get_coord2();
        }
        if (nodes.size() % 2 == 0) {
            return nodes.get(nodes.size() / 2).point.get_coord2();
        }
        return nodes.get(nodes.size() / 2 + 1).point.get_coord2();
    }

    // Method to compute a CompositeNumber median in order to sort properly all points
    // considering the case where all y coordinates in the set are equals.
    private CompositeNumber find_median(ArrayList<Node> nodes) {
        // Median will be a composite number (x|y) with the median on y and x on values.
        CompositeNumber median;
        if (nodes.size() == 1) {
            median = new CompositeNumber(nodes.get(0).point.get_coord1(), nodes.get(0).point.get_coord2() / 2);
        }
        if (nodes.size() % 2 == 0) {
            median = new CompositeNumber(((nodes.get((nodes.size() / 2) - 1).point.get_coord1()) + (nodes.get(nodes.size() / 2).point.get_coord1())) / 2
                    , ((nodes.get((nodes.size() / 2) - 1).point.get_coord2()) + (nodes.get(nodes.size() / 2).point.get_coord2())) / 2);
        } else {
            median = new CompositeNumber(nodes.get((nodes.size() - 1) / 2).point.get_coord1(), nodes.get((nodes.size() - 1) / 2).point.get_coord2());
        }
        return median;
    }

    public ArrayList<Segment> query(double xMin, double xMax, double yMin, double yMax) {
        reportedSegments = new ArrayList<Segment>();
        reportedSubTrees = new ArrayList<PrioritySearchTree>();
        //apply_window(xMin, yMin, xMax, yMax, reportedSegments);
        query_pst(xMin, xMax, yMin, yMax, reportedSegments, reportedSubTrees,false, false);
        for (PrioritySearchTree reportedSubtree: reportedSubTrees) {
            reportedSubtree.report_in_subtree(xMin, xMax, yMin, yMax, reportedSegments);
        }
        return reportedSegments;
    }
    private void query_pst(double xMin, double xMax, double yMin, double yMax, ArrayList<Segment> reportedSegments, ArrayList<PrioritySearchTree> reportedSubTrees, boolean split, boolean maxSearching){
        if(data!=null){
            double x = data.point.get_coord1();
            double y = data.point.get_coord2();
            CompositeNumber median = data.compositeMedian;

            if(x >= xMin && x <= xMax && y >= yMin && y <= yMax || segmentIntersectWindow(data.segment, xMin, yMin, yMax)){
                //If the current endPoint is inside the bounds we check if it's not already reported
                if (!reportedSegments.contains(data.segment)){
                    reportedSegments.add(data.segment);
                }
            }

            if(!split){
                //In this cas we haven't found vSplit. It means that yMax and yMin are located in the same subtree

                if (yMax <= median.get_coord2()){
                    //In this case vSplit isn't found so we need to explore the left subtree
                    if (leftTree != null){
                        leftTree.query_pst(xMin, xMax, yMin, yMax, reportedSegments, reportedSubTrees, false, false);
                    }
                }
                else if (yMin >= median.get_coord2()){
                    //In this case vSplit isn't found so we need to explore the right subtree
                    if(rightTree != null){
                        rightTree.query_pst(xMin, xMax, yMin, yMax, reportedSegments, reportedSubTrees, false, false);
                    }
                }
                else{
                    //This means we found vSplit so we are gonna explore left subtree in search of the yMin and the right subtree in search of yMax
                    if (leftTree != null){
                        leftTree.query_pst(xMin, xMax, yMin, yMax, reportedSegments, reportedSubTrees, true, false);
                    }
                    if(rightTree != null){
                        rightTree.query_pst(xMin, xMax, yMin, yMax, reportedSegments, reportedSubTrees, true, true);
                    }
                }
            }
            else{
                if(!maxSearching){
                    //Which means we are currently in the left subtree of the vSplit node
                    if(yMin <= median.get_coord2()){
                        //Which means that the right subtree of the current node is within the y bounds of the window.
                        if (rightTree != null){
                            if (!reportedSubTrees.contains(rightTree)){
                                reportedSubTrees.add(rightTree);
                            }
                        }
                        if (leftTree!= null){
                            leftTree.query_pst(xMin, xMax, yMin, yMax, reportedSegments, reportedSubTrees, true, false);
                        }
                    }
                    else{
                        //Which means there are no nodes in the left subtree with y coordinates within the bounds
                        if (rightTree != null){
                            rightTree.query_pst(xMin, xMax, yMin, yMax, reportedSegments, reportedSubTrees, true, false);
                        }
                    }
                }
                else {
                    //We are in the right subtree of vSplit node
                    if (yMax > median.get_coord2()){
                        //Which means that the left subtree of the current node is within the y bounds of the window
                        if (leftTree != null){
                            if (!reportedSubTrees.contains(leftTree)){
                                reportedSubTrees.add(leftTree);
                            }
                        }
                        if (rightTree != null){
                            rightTree.query_pst(xMin, xMax, yMin, yMax, reportedSegments, reportedSubTrees, true, true);
                        }
                    }
                    else{
                        //Which means we need to explore the left subtree
                        if (leftTree!=null){
                            leftTree.query_pst(xMin, xMax, yMin, yMax, reportedSegments, reportedSubTrees, true, true);
                        }
                    }
                }
            }
        }
    }
    private void report_in_subtree(double xMin, double xMax, double yMin, double yMax, ArrayList<Segment> reportedSegments){
        if (data != null){
            double x = data.point.get_coord1();
            double y = data.point.get_coord2();

            if(x <= xMax){
                // In this case we can consider the node + the rest of the subtree
                if (x >= xMin && y >= yMin && y <= yMax || segmentIntersectWindow(data.segment, xMin, yMin, yMax)){
                    if (!reportedSegments.contains(data.segment)){
                        reportedSegments.add(data.segment);
                    }
                }
                if (leftTree!=null){
                    leftTree.report_in_subtree(xMin, xMax, yMin, yMax, reportedSegments);
                }
                if (rightTree!=null){
                    rightTree.report_in_subtree(xMin, xMax, yMin, yMax, reportedSegments);
                }
            }
        }
    }

    private boolean segmentIntersectWindow(Segment segment, double xMin, double yMin, double yMax){
        double x1 = segment.get_startComp().get_coord1();
        double y = segment.get_startComp().get_coord2();
        double x2 = segment.get_endComp().get_coord1();
        //We do not use y2 because all segments are horizontal

        if (y <= yMax && y >= yMin){
            if (x1 <= xMin && x2 >= xMin){
                //This means that we intersect the window.
                return true;
            }
        }return false;
    }
}
