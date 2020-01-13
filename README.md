# Temple of Gloom Maze Solver

## Objectives

The aims of this project were to:

* Experiment with graph traversal algorithms (DFS, BFS, Dijkstra)
* Learn to work with packages,
* Become accustomed to the features of Java 9,
* Learn to see the use of Java concurrency via the threading model,
* To see the workings of a Java Swing GUI application.


## Overview

There are two major phases during the running of this application:
* the *exploration phase*, and
* the *escape phase*,

## The exploration phase

On the way to the Orb (see the figure below), the layout of the cavern is unknown. 
Only the status of the tile on which the explorer is standing and those immediately around 
is known. 
My goal was to make it to the Orb in as 
few steps as possible. This was not a blind search, as the distance to the Orb was known. This
is equal to the number of tiles on the shortest path to the Orb, if there weren’t walls or 
obstacles in the way.

![Searching for the Orb during the exploration phase](exploration.png)

The solution to this phase was implemented in the method `explore()` in the class `Explorer`.
There is no time limit on the number of steps taken, 
but a higher score is accumulated for finding the Orb in fewer steps. 


Within the method `explore()`, an `ExplorationState` object representing the explorer is used to learn about 
the environment around them. Every time a move is made, this object automatically changes 
to reflect the new location of the explorer. This object includes the following methods:

* `long getCurrentLocation()`:

> return a unique identifier for the tile the explorer is currently on.

* `int getDistanceToTarget()`:

> return the distance from the explorers current location to the Orb.

* `Collection<NodeStatus> getNeighbours()`:

> return information about the tiles to which the explorer can move from their current location.

* `void moveTo(long id)`:

> move the explorer to the tile with ID `id`. 
> This fails if that tile is not adjacent to the current location.   
<br/>

#### A Depth First Search Algorithm was implemented within the `explore()` method to find the shortest path to the orb.
<br/>

## The escape phase

After picking up the Orb, the walls of the cavern shift and a new layout is generated.
In addition, piles of gold fall onto the ground. 
A time limit is started and different edges of the graph to have different weights. 
The goal of the escape phase is to make it to the entrance of the cavern before it collapses. 

![Collecting gold during the escape phase](escape.png)

A score component is based on two additional factors:

1. The amount of gold picked up during the escape phase, and
1. the score from the exploration phase.


The solution code to this part of the problem was written in the method `escape()` in the class 
`Explorer`. Returning from this method while standing 
on the entrance tile will allow the explorer to escape the maze. 
Returning while at any other position, or failing to return before time runs out, 
will cause the game to end and result in a score of 0.

An important point to clarify is that time during this phase is not defined as the CPU time but rather the number of steps taken by the explorer: 
the time remaining decrements regardless of how long is spent deciding which move to make. 
Because of this, it is guaranteed that there will always be given enough time 
to escape the cavern should the shortest path out be taken. 


An `EscapeState` object is used to learn about the environment 
around the explorer. Every time a move is made, this object will automatically 
change to reflect the new location of the explorer. This object includes the following methods:

* `Node getCurrentNode()`:

> return the Node corresponding to the explorers location.

* `Node getExit()`:

> return the `Node` corresponding to the exit to the cavern (the destination).

* `Collection<Node> getVertices()`:

> return a collection of all traversable nodes in the graph.

* `int getTimeRemaining()`:

> return the number of steps the explorer has left before the ceiling collapses.

* `void moveTo(Node n)`:

> move the explorer to node `n`. 
> This will fail if the given node is not adjacent to the explorers current location. 
> Calling this function will decrement the time remaining.

* `void pickUpGold()`:

> collect all gold on the current tile. 
> This will fail if there is no gold on the current tile or it has already been collected.
<br/>

#### Dijkstra's weighted graph traversal algorithm was implemented in the `escape()` method to find the shortest path to the exit.


## The GUI

When running the program, you are presented with a GUI where you can 
watch the explorer making moves. When the GUI is running, each call to `moveTo()` 
blocks until the corresponding move completes on the GUI.

You can use the slider on the right side of the GUI to increase or decrease the speed of the explorer.
Increasing the speed will make the animation finish faster. A timer 
displays the number of steps remaining during the escape phase (both as a number and a percentage). 

You can also see the `bonus` and the number of coins collected, 
followed by the final score computed as the product of these. 
The bonus multiplier begins as `1.3` and slowly decreases as more and more steps are taken 
during the explore stage (after which it is fixed), while the number of coins increases 
as you collect them during the escape phase.

Finally, click on any square in the map to see more detailed information about it on the right, 
including its row and column, the type of tile, and the amount of gold on that square.


## Credits

Thank you to Eric Perdew, Ryan Pindulic, and Ethan Cecchetti from the Department of 
Computer Science at Cornell for the basis of this project.
