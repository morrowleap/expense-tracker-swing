#!/usr/bin/env bash
set -e
clear

# compile & run
javac -d bin -cp "sqlite-jdbc-3.50.1.0.jar" $(find src -name "*.java")
java -cp "bin:sqlite-jdbc-3.50.1.0.jar" gui.ExpenseTrackerGUI
