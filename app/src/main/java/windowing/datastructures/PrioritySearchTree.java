package windowing.datastructures;

import java.util.ArrayList;
import java.util.Collections;

/**
 * This class contains all the methods related to the Priority Search Tree.
 * From its construction to its querying.
 */
public class PrioritySearchTree {

    private Node data;
    private PrioritySearchTree leftTree;
    private PrioritySearchTree rightTree;
    private ArrayList<Segment> segments;
    private ArrayList<Node> nodes;

    /**
     * Constructor of the PST. During the construction we only keep the first endpoint of the segment.
     * After creating nodes, we sort them with a quick sort based on their y component.
     * @param segments all the segments that need to be inserted in the PST
     */
    public PrioritySearchTree(ArrayList<Segment> segments) {
        this.segments = segments;
        nodes = new ArrayList<Node>();
        // we start by creating our set of points i.e. all segments endpoints 
        for (Segment s : segments) {
            // We only need to take the starting component for the tree
            nodes.add(new Node(s.get_startComp(), s));
        }
        // points are sorted to improve the efficiency when calculating the median
        quicksort(nodes, 0, nodes.size() - 1);
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
     * This method is used to construct the tree. It is a recursive method.
     * First the method search the lowest point in the set of nodes based on its x coordinate. This point will be the root of the tree.
     * Then the method computes the median of the set of nodes based on their y coordinate.
     * Finally the method separates the set of nodes into two sets, one for the left tree and one for the right tree.
     * @param nodes the set of nodes that need to be inserted in the tree
     * @return the root of the tree
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
                    leftTree.add(node);
                } else {
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
            data.compositeMedian = find_median(nodes);
            nodes.clear();
        }
        return null;
    }

    /**
     * This method is used to find the index of the lowest point in the set of nodes based on its x coordinate.
     * @param nodes the set of nodes
     * @return the index of the lowest point
     */
    private int find_min_index(ArrayList<Node> nodes) {
        int index = 0;
        for (int n = 1; n < nodes.size(); n++) {
            if (nodes.get(n).point.is_x_smaller_than(nodes.get(index).point)) {
                index = n;
            }
        }
        return index;
    }

    /**
     * This method implements the QuickSort from SDDII theory It sorts the set of nodes based on their y coordinate.
     * @param nodes the set of nodes
     * @param start the start index
     * @param end  the end index
     */
    private void quicksort(ArrayList<Node> nodes, int start, int end) {
        if (start < end) {
            int part = partition(nodes, start, end);
            quicksort(nodes, start, part - 1);
            quicksort(nodes, part + 1, end);
        }
    }

    /**
     * This method is an annex to the Quicksort method. It is used to partition the set of nodes.
     * The pivot is the point where all the nodes before the pivot are smaller than the pivot and all the nodes after the pivot are bigger than the pivot.
     * @param nodes the set of nodes
     * @param start the start index
     * @param end   the end index
     * @return the index of the pivot
     */
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


    /**
     * This method is used to compute the median of the set of nodes based on their y coordinate.
     * It's a simple algorithm that uses the theory about the median of a sorted list.
     * In this case the median is a Composite Number. This prevents the case where all y coordinates are equals during the PST construction.
     * @param nodes the set of nodes
     * @return A Composite Number (x|y) that is the median of the set of nodes
     */
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

    /**
     * This method is used to instantiate the ArrayList of the reported segments.
     * The reportedSubTrees ArrayList is used to store the subtrees that are reported by the query_pst() method.
     * These subtrees will then be explored by the report_in_subtree() method in order to find the segments that need to be reported.
     * @param xMin the minimum x coordinate of the window
     * @param xMax the maximum x coordinate of the window
     * @param yMin the minimum y coordinate of the window
     * @param yMax the maximum y coordinate of the window
     * @param invertedSegments a boolean that indicates if the segments are inverted or not (true when we treat vertical segments)
     * @return the ArrayList of the reported segments
     */
    public ArrayList<Segment> query(double xMin, double xMax, double yMin, double yMax, boolean invertedSegments) {
        ArrayList<Segment> reportedSegments = new ArrayList<Segment>();
        ArrayList<PrioritySearchTree> reportedSubTrees = new ArrayList<PrioritySearchTree>();

        //apply_window(xMin, yMin, xMax, yMax, reportedSegments);
        query_pst(xMin, xMax, yMin, yMax, reportedSegments, reportedSubTrees,false, false);
        for (PrioritySearchTree reportedSubtree: reportedSubTrees) {
            reportedSubtree.report_in_subtree(xMin, xMax, yMin, yMax, reportedSegments);
        }
        return reportedSegments;
    }

    /**
     * This method query a PST in order to find the segments that are inside the window. It's an implementation of the reference pseudocode.
     * First it checks that the current node is not null. Then it checks if the current node is inside the window.
     * If it is, it checks if the current node is not already reported. If it's not, it adds the current node to the reportedSegments ArrayList.
     * Then it checks if we already found the split node. If we did, it means that we need to explore both subtrees.
     * If we didn't, it means that we need to explore only one subtree, the one where our y bounds are.
     * The split case conditions allow us to prune some of the subtrees if they are not inside the window.
     * @param xMin the minimum x coordinate of the window
     * @param xMax the maximum x coordinate of the window
     * @param yMin the minimum y coordinate of the window
     * @param yMax the maximum y coordinate of the window
     * @param reportedSegments the ArrayList of the reported segments
     * @param reportedSubTrees the ArrayList of the reported subtrees that will be explored by the report_in_subtree() method
     * @param split a boolean that indicates if we already found the split node
     * @param maxSearching a boolean that indicates if we are searching for the maximum y coordinate of the window (true) i.e we are in the right subtree of the split node.
     */
    private void query_pst(double xMin, double xMax, double yMin, double yMax, ArrayList<Segment> reportedSegments, ArrayList<PrioritySearchTree> reportedSubTrees, boolean split, boolean maxSearching){
        if(data!=null){
            double x = data.point.get_coord1();
            double y = data.point.get_coord2();
            CompositeNumber median = data.compositeMedian;

            if(x >= xMin && x <= xMax && y >= yMin && y <= yMax || (y == data.segment.get_endComp().get_coord2() && segmentIntersectWindow(data.segment, xMin, yMin, yMax))){
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

    /**
     * This method explore the subtree of a given node in order to find the segments that are inside the window.
     * This is an implementation of the report_in_subtree() method of the reference.
     * Those subtrees are reported by the query_pst() method if and only if they are within the y bounds of the window.
     * So that we only need to control that the x coordinate of the current node is within the x bounds of the window.
     * If it is, we check if the current node is inside the window. If it is, we add it to the reportedSegments ArrayList.
     * If the current node is not a leaf and the x coordinate is smaller than the xMax, we explore its subtrees.
     * @param xMin the minimum x coordinate of the window
     * @param xMax the maximum x coordinate of the window
     * @param yMin the minimum y coordinate of the window
     * @param yMax the maximum y coordinate of the window
     * @param reportedSegments the ArrayList of the reported segments
     */
    private void report_in_subtree(double xMin, double xMax, double yMin, double yMax, ArrayList<Segment> reportedSegments){
        if (data != null){
            double x = data.point.get_coord1();
            double y = data.point.get_coord2();

            if(x <= xMax){
                // In this case we can consider the node + the rest of the subtree
                if (x >= xMin && y >= yMin && y <= yMax || (y == data.segment.get_endComp().get_coord2() && segmentIntersectWindow(data.segment, xMin, yMin, yMax))){
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

    /**
     * A simple method to determine if a segment intersect the left bound of the window.
     * As we only consider the first endpoint of the endpoint which is the left extremity of the segment,
     * we only need to check if we intersect the left bound of the window because the other endpoint is always to the right.
     * If we do not intersect the bound it means that the segment is not in the window.
     * As we flip vertical segments coordinates we don't need to adapt the algorithm for vertical segments.
     * They are considered as horizontal segments.
     * @param segment the segment we want to check
     * @param xMin the minimum x coordinate of the window
     * @param yMin the minimum y coordinate of the window
     * @param yMax the maximum y coordinate of the window
     * @return true if the segment intersect the window, false otherwise
     */
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
