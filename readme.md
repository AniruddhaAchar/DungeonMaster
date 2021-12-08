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

Ability for the location to show what are the possible ways in which it's state can be changed.
There is an overarching "Action" interface that will define the strategy that a specific component
can take.

Change in dynamic states in the game occurs through events. Events are published to an event bus and
the subscribers to these events listen to specific events and take actions.

There is a text view that is present game on a console. The controller has methods to getting help,
moving the player and collecting objects.

The GUI view provides an interface for the users to play the game using with audiovisual input. The
user can visually explore the maze, collect items from the maze and kill monsters.

## How to run the jar

Please have the META-INF folder in the same directory as DungeonMaster.jar.

To run the jar, use the command
`java -jar DungeonMaster.jar`

This will bring up the GUI view. To get the CLI view, please run

`hava -jar DungeonMaster.jar -cli`

This will run the jar with the default settings. Please
read [How to use the program](#how-to-use-the-program)

## How to use the program

The jar takes the following flags.

```
-cli           Runs the CLI implementation of the game.
      -debug         Debug mode.
      -dic, --degIntConn=<degreeOfInterConnections>
                     Degree of inter-connectivity between the grid locations.
  -h, --help         Show this help message and exit.
      -mc, --maxcolumns=<maxCols>
                     Maximum number of columns in the game.
      -mr, --maxrow=<maxRows>
                     Maximum number of rows in the game.
      -nm, --numberMonsters=<numberMonsters>
                     Number of monsters in the game.
      -pt, --perTreasure=<percentTreasure>
                     Percentage of caves with treasure.
  -V, --version      Print version information and exit.
  -w, --iswrappeds   Make the grid wrapped or not.
```

To play the CLI game, follow the instructions below.

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

To play the GUI view

1. To move the player:
    1. To move the player, user can use the arrow keys. If the player can move in the specified
       direction then the character on the screen is moved, else you will hear a `beep`.
    2. To move the character using mouse, click on a grid location, if the player can move in that
       direction, the player will move, else you hear a `beep`.
2. To shoot arrows:
    1. Right-click on the grid to bring up a context menu that will help you select the shoot
       direction and distance.
    2. Hit the `s` key and that will again bring the context menu to shoot. Here you can use
       the `WASD` keys to select the directions. To select north, you can use `Alt + W` key.
       Similarly, for the other directions as well.
       ```
       Shoot North -> S, Alt + W
       Shoot South -> S, Alt + S
       Shoot East -> S, Alt + D
       Shoot West -> S, Alt + A
   ```

3. To pick treasures or arrow:
    1. The location will have a chest.
    2. Right click. if the pick context menu appears, you can pick a treasure or an arrow.
    3. Hit P and the context menu comes up. This can be used to pick the item.

4. To restart the game, go to the `Options` menu and select restart.
5. To start a new game, go to the `Options` menu and select new game and fill the dialog box.

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

`gameplay.png` shows how the game looks when some treasures/arrows are found and when a monster is
detected.

## Design changes

The controller and the view are event driven. The controller and view publish and subscribe to
events and act on these events.

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

The player should visit the location when to see if the smell changed.
The view knows only about the current state of the player and is not
aware of any other state changes that occurred.

## Citation

* [Finding if graph is connected.](https://algorithms.tutorialhorizon.com/check-if-given-undirected-graph-is-connected-or-not/)
* [Shortest path](https://www.geeksforgeeks.org/shortest-path-unweighted-graph/)
* [Some help with graph.](https://gist.github.com/kevinmorio/f7102c5094aa748503f9)
* [The player graphics.] (https://luizmelo.itch.io/huntress-2)
