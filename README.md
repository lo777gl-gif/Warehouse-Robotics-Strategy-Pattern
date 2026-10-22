# Warehouse robots - BEFORE refactoring

First commit. One class, `RobotChooser`, knows every way to choose a robot and picks the way with a `switch` on a text.
Adding a new way means editing this class. The tests in `RobotChoiceBehaviourTest` describe what the choice must do.

```
./run.sh          # demo (Windows: run.bat)
mvn test          # 7 tests
```
