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
        Set <Long> seen = new HashSet<Long>();
        path.add(state.getCurrentLocation());
        while(state.getDistanceToTarget() > 0){

            //List of neighbours
            List <NodeStatus> neighbours =  new ArrayList<NodeStatus>(state.getNeighbours());

            //Add current location to Set
            if (!seen.contains(state.getCurrentLocation())){
                seen.add(state.getCurrentLocation());
            }

            //Create list of unvisited neighbours
            List<NodeStatus> temp = new ArrayList<NodeStatus>();
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

        while (state.getCurrentNode() != state.getExit()){
            //Assign current location to 'here'
            Node here = state.getCurrentNode();

            //Get neighbouring Nodes
            Set<Node> neighbours = here.getNeighbours();

            //List of visited nodes
            List<Node> steppedOn = new ArrayList<>();
            steppedOn.add(state.getCurrentNode());

            //Add neighbours to List
            List<Node> neigh = new ArrayList<>();
            neigh.addAll(here.getNeighbours());

            Node node = neigh.get(0);
            Set node2 = node.getNeighbours();

            //Find route from exit
            Stack<Node> stack = new Stack();
            stack.push(state.getExit());
            System.out.println("Finding route");
            while (stack.peek() != state.getCurrentNode()){
                List<Node> goodFriends = new ArrayList<>();
                goodFriends.addAll(stack.peek().getNeighbours());

                Node moveHere = goodFriends.get(0);
                stack.push(moveHere);
            }

            System.out.println("Route found");
            while (state.getCurrentNode() != state.getExit()){
                stack.pop();
                state.moveTo(stack.peek());
            }


//            boolean fin = false;
//            while (!fin){
//                for (Node n: neighbours){
//
//                }
//            }

//            System.out.println(here.getEdge(state.getExit()));

//            System.out.println("1");
//            Edge nextEdge = here.getEdge(here);
//            System.out.println("2");
//            Node nextMove = nextEdge.getSource();
//            System.out.println("3");
//            state.moveTo(nextMove);
//            System.out.println(here.getEdge(here));

//            System.out.println(state.getCurrentNode());
//            System.out.println(state.getVertices().size());


        }

        //TODO: Escape from the cavern before time runs out
    }

//    List<Node> route(Set<Node> neigh, EscapeState state){
//        List<Node> r;
//        boolean fin = false;
//        while (!fin){
//            for (Node n: neigh ){
//                if (n == state.getExit());{
//                    fin = true;
//                    return r.add(n);
//                }else{
//
//                    return r.add(route(n.getNeighbours(), state));
//                }
//            }
//        }
//        return ;
//    }
}
