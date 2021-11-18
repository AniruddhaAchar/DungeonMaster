# Dungeon Master

## About

A dungeon scroller game where players have to move through a maze that is randomly generated. The
maze has a starting and an ending point. There is at least one path between all locations in the
maze.

A player can pass through tunnels, move into caves. Caves have treasures and these treasures can be
picked up by the player. Tunnels and caves can have arrows that the player can pick up.

Caves may have monsters in them. Monsters are here Otyughs. Otyughs are smelly monsters that eat
flesh. They are solitary creatures and only one Otyughs lives in a cave. If a player enters a cave
occupied by an Otyughs that is healthy, the player is eaten alive. If the Otyughs is hurt by the
player, then the player has a 50% chance of surviving the encounter.

To kill a monster, the player has to shoot an arrow. The arrows are special and can travel through
tunnels even if the tunnels change directions. But they only travel straight through caves. To shoot
an arrow, the player specifies the direction they want shoot and how far they want to shoot the
arrow. If the arrow lands in a cave with a monster, the monster takes one hit. To kill the monster,
two hits are needed.

1. This game is developed as an example for the overall SDK that I am trying to write. This SDK
   would help anyone to develop a new game. "Dungeon Master" is an example of how to use the SDK for
   the game model.
2. The game is built to be extendable and expandable. The rules of the games can be made up as a
   player plays the games, new "Actions" can be added to the game on the fly and new items and
   characters can be created when needed.

## Features

Ability for the location to what are the possible ways in which it's state can be changed. There is
an overarching "Action" interface that will define the strategy that a specific component can take.

Change in dynamic states in the game occurs through events. Events are published to an event bus and
the subscribers to these events listen to specific events and take actions.

There is a text view that is present game on a console. The controller has methods to getting help,
moving the player and collecting objects.

## How to run the jar

Please have the META-INF folder in the same directory as DungeonMaster.jar.

To run the jar, use the command
`java -jar DungeonMaster.jar`

This will run the jar with the default settings. Please
read [How to use the program](#how-to-use-the-program)

## How to use the program

The jar takes the following flags.

```
      -dic, --degIntConn=<degreeOfInterConnections>
                     Degree of inter-connectivity between the grid locations. (Default = 0)
  -h, --help         Show this help message and exit.
  -mc, --maxcolumns=<maxCols>
                     Maximum number of columns in the game. (Default = 10)
  -mr, --maxrow=<maxRows>
                     Maximum number of rows in the game. (Default = 10)
  -pt, --perTreasure=<percentTreasure>
                     Percentage of caves with treasure. (Default = 10)
  -V, --version      Print version information and exit.
  -w, --iswrappeds   Make the grid wrapped or not.
                     Adding this flag makes it wrapped.
  -nm --numberMonsters Number of monsters in the game.
```

To play the game, you can use the `help` command to get to know the commands needed to play the
game.

```
The game uses verb-[noun]+[qualifier] syntax for the command.
Some verbs may have nouns while some verbs may have qualifiers as well. 
Valid commands:
help
Move command:
   move <direction>
Valid directions are:
   north
   south
   east
   west
To move east, you can enter:
		`move east`
Pick command:
   pick <item>
Valid items:
   treasure
   arrow
Valid pick command:
		`pick arrow`
Shoot arrow command:
   shoot <direction> <distance>
Valid shoot command:
		`shoot north 3`
		
You can quit the game by entering `quit`.
Valid directions are the same as above. Distance should be a positive integer.

All these commands have a shorthand. Just use the first letter of the command.
```

### Example runs

```
java -jar DungeonMaster.jar -pt 100 -mr 30 -mc 30
```

This builds a non wrapped 30 &times; 30 grid maze with 100 % of caves with treasures. The
interconnectivity being one.

```
java -jar DungeonMaster.jar -dic 5
```

This builds a non wrapped 10 &times; 10 grid maze with 10% of caves with treasures. The
interconnectivity between caves is 5.

```
java -jar DungeonMaster.jar -w
```

This builds a wrapped 10 &times; 10 grid maze with 10% of caves with treasures. The
interconnectivity between caves is 1.

## Description of examples

`killMonser.txt` A game play where the monster is killed.

`killPlayer.txt` A game play where the player is killed by the monster.

`pickArrow.txt` A game play where arrow is picked by the player.

`winGame.txt` A game play where the player wins a game by entering the end cave.

`pickTreasure.txt` A game play where a player picks up a treasure.

## Design changes

View and view model was implemented.
New event driven model was implemented to handle dynamic actions in the game.
Improved performance of the model end location selection by allowing better distance finding
algorithm.

## Assumption

There are three major assumptions that are made when developing the game.

1. There are only player in the game.
2. The game will be played on a single threaded application.
3. The game is always on a 2D plane.
4. The play is event driven.
5. The application always runs on a single thread.

Players can pick up unlimited number of treasures.

## Limitation

The game is generic and has room for extendability. This makes some necessary casting. Given how the
game generates the maze, there is a small possibility that the game may be in an invalid state. The
driver should manage this and try and recreate a failed generation of the model.

The model does allow to be generic, there is time complexity when generating the dungeons. When
large grids are generated, it will take a longer time for the model to be generated. This is because
of the constraints that are posed on how the model needs to be generated i.e. model connectedness
and distance between the start and end locations. This is a limitation of using graph theory to
generate the graphs.

This application cannot be run on a multithreaded system. States change with the assumption that
there is only one manipulator state i.e. one thread. In a multithreaded process there would be wrong
state update.

## Citation

* [Finding if graph is connected.](https://algorithms.tutorialhorizon.com/check-if-given-undirected-graph-is-connected-or-not/)
* [Shortest path](https://www.geeksforgeeks.org/shortest-path-unweighted-graph/)
* [Some help with graph.](https://gist.github.com/kevinmorio/f7102c5094aa748503f9)