# Warehouse robots - Strategy pattern (variant 18)

A warehouse has free robots and a queue of customer orders. Two decisions can be made in several ways, so each one is a family of strategies:

| Family | Question | Strategies |
|---|---|---|
| Robot choice (`RobotStrategy`) | Which free robot takes the job? | `NearestRobot`, `MostChargedRobot`, `LongestIdleRobot` |
| Order sequence (`OrderStrategy`) | In which sequence are waiting orders handled? | `ArrivalOrder`, `SmallestFirst`, `SameShelfTogether` |

Each family has a **context** (`RobotAssigner`, `OrderPlanner`) that can swap its strategy at run time, and a **selector** (`StrategySelector`) that picks a strategy for the current situation.

## Run

```
./run.sh          # demo, nothing to type (Windows: run.bat)
mvn test          # 51 tests + coverage report in target/site/jacoco/index.html
```

The demo shows three things: the same input through every strategy of a family, manual choice against automatic choice, and a strategy change while the program runs.

## Classes and their single responsibility

| Class | Responsibility |
|---|---|
| `RobotStrategy` | Promises one way to choose a robot for a job. |
| `NearestRobot` | Chooses the free robot with the smallest distance to the job. |
| `MostChargedRobot` | Chooses the free robot with the highest battery level above the minimum. |
| `LongestIdleRobot` | Chooses the free robot that has waited longest without work. |
| `RobotAssigner` | Holds one robot strategy, lets the program replace it, hands every request to it. |
| `OrderStrategy` | Promises one way to put waiting orders into a sequence. |
| `ArrivalOrder` | Puts orders into the sequence in which they arrived. |
| `SmallestFirst` | Puts orders with fewer items before orders with more items. |
| `SameShelfTogether` | Places orders for the same product next to each other. |
| `OrderPlanner` | Holds one order strategy, lets the program replace it, hands every request to it. |
| `Situation` | Describes the warehouse right now: hour of the day, length of the queue. |
| `StrategySelector` | Turns a situation into one strategy per family, following written rules. |
| `BatterySensor` | Promises a battery level for a robot, hiding where the number comes from. |
| `SimulatedBatterySensor` | Produces repeatable made-up battery levels in place of real hardware. |
| `Robot` | Carries the data of one free robot. |
| `Order` | Carries the data of one waiting order. |
| `Point` | Carries a place in the warehouse, measures distance to another place. |
| `Main` | Runs the demonstration. |

Test classes: `RobotStrategiesTest` and `OrderStrategiesTest` check every strategy directly. `ContextsTest` checks that contexts only hand work to a strategy. `StrategySelectorTest` checks every rule with boundary values. `AllStrategiesTest` runs one check over all strategies of a family. `RobotChoiceBehaviourTest` describes robot choice for both commits. `FakeBatterySensor` gives fixed battery levels.

## Selector rules

| Situation | Robot strategy | Order strategy |
|---|---|---|
| Night (22:00-05:59) | `LongestIdleRobot` (share the work) | - |
| Queue longer than 10 | `NearestRobot` (be fast) | `SmallestFirst` (clear backlog) |
| Queue from 5 to 10 | `MostChargedRobot` | `SameShelfTogether` (fewer walks) |
| Queue shorter than 5 | `MostChargedRobot` (be safe) | `ArrivalOrder` (be fair) |

Night is checked first for robots, so a night rush still uses `LongestIdleRobot`. Orders do not look at the hour.
The `if` statements of the project are only in `StrategySelector`. The contexts have none.

## Outside data

`MostChargedRobot` needs the battery level of a robot. It does not know any real device: it asks our own interface `BatterySensor`.
The demo uses `SimulatedBatterySensor`. Tests use `FakeBatterySensor` with fixed numbers, so results never change and no hardware is needed.
A real sensor means a new class that implements `BatterySensor`. No strategy changes.

## Refactoring history

The robot choice was written first in one class with a `switch` (`1-before`), then changed to the Strategy pattern (`2-after`).
`RobotChoiceBehaviourTest` is the same file in both commits and passes in both. Only the small helper `RobotChoice` in the test folder differs, because the way to call the code changed.

* Commit "before": `<paste link to commit>`
* Commit "after": `<paste link to commit>`

## Why Strategy, not State or Template Method

**State** changes behaviour when the object itself changes: the states know each other and move the object from one to the next. Here nothing moves by itself. The algorithms do not know each other, they have no transitions, and an outside caller (the selector or the user) picks one.

**Template Method** fixes a skeleton in a base class and lets subclasses change single steps. The choice is made when the class is written, through inheritance, and cannot change while the program runs. Here the whole algorithm is replaced, not a step of it. The context uses composition, so `setStrategy` swaps the algorithm at run time.

So the family is a set of interchangeable algorithms behind one interface, chosen from outside: that is Strategy.
