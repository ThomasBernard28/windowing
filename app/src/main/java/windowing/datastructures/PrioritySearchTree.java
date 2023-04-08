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

    public ArrayList<Segment> query(double xMin, double yMin, double xMax, double yMax) {
        reportedSegments = new ArrayList<Segment>();
        //apply_window(xMin, yMin, xMax, yMax, reportedSegments);
        query_pst(xMin, xMax, yMin, yMax, reportedSegments, false, false);
        return reportedSegments;
    }

    /*
     * This is dogshit, it must be completely reworked.
     */
    private void apply_window(double xMin, double xMax, double yMin, double yMax, ArrayList<Segment> reportedSegments) {
        if (data != null) {
            double x = data.point.get_coord1();
            double y = data.point.get_coord2();
            // first case : at least one endpoint lie within the window
            if (x >= xMin && x <= xMax && y >= yMin && y <= yMax) {
                if (!reportedSegments.contains(data.segment)) {
                    reportedSegments.add(data.segment);
                }
            }
            if (leftTree != null) {
                leftTree.apply_window(xMin, xMax, yMin, yMax, reportedSegments);
            }
            if (rightTree != null) {
                rightTree.apply_window(xMin, xMax, yMin, yMax, reportedSegments);
            }
            // second case : the segment cross entierly the window i.e. no endpoint within the window
        }

    }

    private void query_pst(double xMin, double xMax, double yMin, double yMax, ArrayList<Segment> reportedSegments, boolean split, boolean maxSearching) {
        //search unbounded to the left. vsplit is the node where two search path splits
        // for each node on the search path of yMin yMax check if node is in range if so report it
        // for each node on the path of yMin i.e in the left subtree of vsplit : Si le chemin continue à gauche du noeud v reportInSubtree( filsDroit(v), xMax)
        // for each node on the path of yMax i.e int the right subtree of vsplit : Si le chemin continue à droite du noeud v reportInSubtree(filsGauche(v), xMax)
        if (split){
            System.out.println("After split");
            if (data != null){
                double x = data.point.get_coord1();
                double y = data.point.get_coord2();
                CompositeNumber median = data.compositeMedian;

                // First look if the point needs to be reported.
                if(x >= xMin && x <= xMax && y >= yMin && y <= yMax){
                    if(!reportedSegments.contains(data.segment)){
                        reportedSegments.add(data.segment);
                    }
                }
                //Then we will consider if we are in the right or left subtree.
                if(maxSearching){
                    //We are in the right subtree of vsplit
                    if (yMax > median.get_coord2()){
                        // Then the leftsubtree will match the y condition and can be explored only on x condition
                        if(leftTree != null){
                            leftTree.report_in_subtree(xMin, xMax, yMin, yMax, reportedSegments);
                        }
                        if(rightTree != null){
                            rightTree.query_pst(xMin, xMax, yMin, yMax, reportedSegments, true, true);
                        }
                    }
                    else{
                        if(leftTree != null){
                            leftTree.query_pst(xMin, xMax, yMin, yMax, reportedSegments, true, true);
                        }
                    }
                }
                else{
                    // We are in the left subtree of vsplit
                    if (yMin <= median.get_coord2()){
                        //Then the right subtree will match the y condition and can be explored only on x condition
                        if(rightTree != null){
                            rightTree.report_in_subtree(xMin, xMax, yMin, yMax, reportedSegments);
                        }
                        if(leftTree != null){
                            leftTree.query_pst(xMin,xMax,yMin,yMax, reportedSegments, true, false);
                        }
                    }
                    else{
                        if(rightTree != null){
                            rightTree.query_pst(xMin, xMax, yMin, yMax, reportedSegments, true, false);
                        }
                    }
                }
            }
        }

        else{
            if(data != null){
                System.out.println("Looking for split");
                double x = data.point.get_coord1();
                double y = data.point.get_coord2();
                CompositeNumber median = data.compositeMedian;
                System.out.println(median.toString());
                System.out.println("yMax" + yMax);
                System.out.println("yMin" + yMin);

                if (x >= xMin && x <= xMax && y>= yMin && y <= yMax){
                    if(!reportedSegments.contains(data.segment)){
                        reportedSegments.add(data.segment);
                        System.out.println("Reported segment" + data.segment.toString());
                    }
                }
                // In this cas we only need to go in the left subtree
                if (yMax <= median.get_coord2() && yMin <= median.get_coord2()){
                    if(leftTree != null){
                        System.out.println("vsplit not found going in the left subtree");
                        leftTree.query_pst(xMin, xMax, yMin, yMax, reportedSegments, false, false);
                    }
                }
                //In this case we only need to go in the right subtree and we didn't find the vsplit
                else if (yMin >= median.get_coord2() && yMax >= median.get_coord2()){
                    if (rightTree != null){
                        System.out.println("vsplit not found going in the right subtree");
                        rightTree.query_pst(xMin, xMax, yMin, yMax, reportedSegments, false, false);
                    }

                }
                else{
                    //We've found the vsplit point and so we need to explore both subtree
                    // In the case of the left subTree we are looking for the min condition
                    System.out.println("vsplit found");
                    if(leftTree != null){
                        leftTree.query_pst(xMin, xMax, yMin, yMax, reportedSegments, true, false);
                    }
                    // In the cas of the right subTree we are looking for the max condition
                    if (rightTree != null){
                        rightTree.query_pst(xMin, xMax, yMin, yMax, reportedSegments, true, true);
                    }
                }
            }
        }
    }

    public void report_in_subtree(double xMin, double xMax, double yMin, double yMax, ArrayList<Segment> reportedSegments){
        if (data != null){
            System.out.println("In report subtree");
            // Si notre point a une extrémité dans la fenêtre
            if (data.point.get_coord1() <= xMax && data.point.get_coord1() >= xMin){
                if(!reportedSegments.contains(data.segment)){
                    reportedSegments.add(data.segment);
                    System.out.println("Found a segment with at least one endpoitn within the window " + data.segment.toString());
                }
                if(leftTree != null){
                    leftTree.report_in_subtree(xMin, xMax, yMin, yMax, reportedSegments);
                }
                if(rightTree != null){
                    rightTree.report_in_subtree(xMin, xMax, yMin, yMax, reportedSegments);
                }
            }
            // Si notre point n'a pas l'extrémité considérée dans la fenêtre
            else{
                CompositeNumber endPoint = data.segment.get_endComp();
                if (endPoint.get_coord1() <= xMax && endPoint.get_coord1()>= xMin){
                    if(endPoint.get_coord2() >= yMin && endPoint.get_coord2() <= yMax){
                        //Alors l'autre extrémité du segment est dans la fenêtre
                        //On rapporte le segment
                        if(!reportedSegments.contains(data.segment)){
                            reportedSegments.add(data.segment);
                            System.out.println("Found that the other endpoint was in the window " + data.segment.toString());
                        }
                        if(leftTree != null){
                            leftTree.report_in_subtree(xMin, xMax, yMin, yMax, reportedSegments);
                        }
                        if(rightTree != null){
                            rightTree.report_in_subtree(xMin, xMax, yMin, yMax, reportedSegments);
                        }
                    }
                }
                else{
                    //Dans ce cas aucune des deux extrémités ne figure dans le fenêtre. Il faut tout de même
                    //Tester si le segment ne traverse pas la fenêtre.
                    double middleX = (data.point.get_coord1() + endPoint.get_coord1())/2;
                    double middleY = (data.point.get_coord2() + endPoint.get_coord2())/2;
                    if (middleX >= xMin && middleX <= xMax && middleY >= yMin && middleY <= yMax){
                        // Alors le segment traverse bien la fenêtre
                        if (!reportedSegments.contains(data.segment)){
                            reportedSegments.add(data.segment);
                        }
                        if(leftTree != null){
                            leftTree.report_in_subtree(xMin, xMax, yMin, yMax, reportedSegments);
                        }
                        if(rightTree != null){
                            rightTree.report_in_subtree(xMin, xMax, yMin, yMax, reportedSegments);
                        }
                    }
                }
            }
        }
    }

}
