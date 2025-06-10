#!/usr/bin/env bash
set -e
clear

# compile & run
javac -d bin -cp "sqlite-jdbc-3.50.1.0.jar" src/tracker/*.java
java -cp "bin:sqlite-jdbc-3.50.1.0.jar" tracker.ExpenseTrackerGUI
