package student;

import game.*;

import java.util.Arrays;
import java.util.*;

public class Explorer {

    /**
     * Explore the cavern, trying to find the orb in as few steps as possible.
     * Once you find the orb, you must return from the function in order to pick
     * it up. If you continue to move after finding the orb rather
     * than returning, it will not count.
     * If you return from this function while not standing on top of the orb,
     * it will count as a failure.
     * <p>
     * There is no limit to how many steps you can take, but you will receive
     * a score bonus multiplier for finding the orb in fewer steps.
     * <p>
     * At every step, you only know your current tile's ID and the ID of all
     * open neighbor tiles, as well as the distance to the orb at each of these tiles
     * (ignoring walls and obstacles).
     * <p>
     * To get information about the current state, use functions
     * getCurrentLocation(),
     * getNeighbours(), and
     * getDistanceToTarget()
     * in ExplorationState.
     * You know you are standing on the orb when getDistanceToTarget() is 0.
     * <p>
     * Use function moveTo(long id) in ExplorationState to move to a neighboring
     * tile by its ID. Doing this will change state to reflect your new position.
     * <p>
     * A suggested first implementation that will always find the orb, but likely won't
     * receive a large bonus multiplier, is a depth-first search.
     *
     * @param state the information available at the current state
     */
    public void explore(ExplorationState state) {
        //Init Stack for current path and Set for visited locations
        Stack<Long> path = new Stack<>();
        Set <Long> seen = new HashSet<>();
        path.add(state.getCurrentLocation());
        while(state.getDistanceToTarget() > 0){

            //List of neighbours
            List <NodeStatus> neighbours =  new ArrayList<>(state.getNeighbours());

            //Add current location to Set
            if (!seen.contains(state.getCurrentLocation())){
                seen.add(state.getCurrentLocation());
            }

            //Create list of unvisited neighbours
            List<NodeStatus> temp = new ArrayList<>();
            for (NodeStatus n: neighbours){
                if (!seen.contains(n.getId())){
                    temp.add(n);
                }
            }
            //From temp list check distance to orb
            if(temp.size() > 0){
                NodeStatus nextMove;

                nextMove = temp.get(0);
                for (NodeStatus i: temp){
                    if(i.getDistanceToTarget() < nextMove.getDistanceToTarget()){
                        nextMove = i;
                    }
                }
                //Move to next unvisited location
                state.moveTo(nextMove.getId());

                //Add current location to Stack of visited locations
                path.add(nextMove.getId());
            }else{
                //If list is empty (there are no unvisited neighbours)
                path.pop();
                state.moveTo(path.peek());
            }
        }
    }

    /**
     * Escape from the cavern before the ceiling collapses, trying to collect as much
     * gold as possible along the way. Your solution must ALWAYS escape before time runs
     * out, and this should be prioritized above collecting gold.
     * <p>
     * You now have access to the entire underlying graph, which can be accessed through EscapeState.
     * getCurrentNode() and getExit() will return you Node objects of interest, and getVertices()
     * will return a collection of all nodes on the graph.
     * <p>
     * Note that time is measured entirely in the number of steps taken, and for each step
     * the time remaining is decremented by the weight of the edge taken. You can use
     * getTimeRemaining() to get the time still remaining, pickUpGold() to pick up any gold
     * on your current tile (this will fail if no such gold exists), and moveTo() to move
     * to a destination node adjacent to your current node.
     * <p>
     * You must return from this function while standing at the exit. Failing to do so before time
     * runs out or returning from the wrong location will be considered a failed run.
     * <p>
     * You will always have enough time to escape using the shortest path from the starting
     * position to the exit, although this will not collect much gold.
     *
     * @param state the information available at the current state
     */
    public void escape(EscapeState state) {

        List<Node> route;
        route = dijkstraAlg(state);//Assigns fastest route
        Collections.reverse(route);//Corrects order

        while (state.getCurrentNode() != state.getExit()){
            for (Node location: route) {
                if(state.getCurrentNode().getTile().getGold() > 0){
                    state.pickUpGold();
                }
                if(location != state.getCurrentNode()){
                    state.moveTo(location);
                }
            }
        }
    }

    /**
     * Dijkstra's Algorithm
     * By comparing the weights of all edges in the graph, returns a list of Nodes in traversal order containing the shortest route
     * @param state the information available at the current state
     * @return list
     */
    public static List<Node> dijkstraAlg(EscapeState state){
        LinkedHashMap<Node, Integer> distance = new LinkedHashMap<>(state.getVertices().size()); //Linked hash map (id, distance from source)
        LinkedList<Node> queue = new LinkedList<>();//A queue of all nodes in graph
        Set<Node> visited = new HashSet<>();//Set to hold all visited nodes
        LinkedHashMap<Node, Node> parent = new LinkedHashMap<>(state.getVertices().size());//Nodes that map to parent Nodes

        queue.add(state.getCurrentNode());//Init queue with starting location

        //Init all distance values to max
        for (Node n: state.getVertices()){
            distance.put(n, Integer.MAX_VALUE);
        }
        distance.put(state.getCurrentNode(), 0);//Distance to starting distance is set to 0

        //Actual algorithm
        while (!queue.isEmpty()){// While there are still nodes to visit
            Node a = queue.pop();//remove node from queue
            visited.add(a);//add node to visited set

            //Set<Node> tempNodes = new HashSet<>(a.getNeighbours());//Temporary set for neighbouring nodes (MAYBE NOT NEEDED)
            Set<Edge> tempEdges = new HashSet<>(a.getExits()); //Temporary set for outgoing edges
            List<Edge> edgeOrdered = new ArrayList<>(); //Simple array to hold edges in order of length

            for (Edge e: tempEdges) {
                if(!visited.contains(e.getOther(a))){
                    edgeOrdered.add(e);//Only add edges for the nodes that have been unvisited
                }
            }
            edgeOrdered = bubbleSort(edgeOrdered);//create linked list with edges in order of length
            for (Edge e: edgeOrdered) {
                if(distance.get(a) + e.length() < distance.get(e.getOther(a))){//Check if new distance is shorter
                    distance.put(e.getOther(a), distance.get(a) + e.length()); //Update distance values for edge destinations
                    parent.put(e.getOther(a), a);
                    queue.add(e.getOther(a));
                }
            }
        }
        //Creates list that contains route from exit to current node
        List<Node> route = new ArrayList<>();
        Node childN = state.getExit();
        route.add(childN);
        while (!route.contains(state.getCurrentNode())){
            childN = parent.get(childN);
            route.add(childN);
        }
        return route;
    }

    /**
     * Bubble sort algorithm that re-arranges list order from smallest to largest values
     * @param list a list of Edge objects
     * @return list
     */
    public static List<Edge> bubbleSort(List<Edge> list){
        for (int i = 0; i < list.size()-1 ; i++) {
            for (int j = 0; j < list.size() - i - 1 ; j++) {
                if(list.get(j).length > list.get(j+1).length){
                    Edge x = list.get(j);
                    list.set(j, list.get(j+1));
                    list.set(j+1, x);
                }
            }
        }
        return list;
    }
}
