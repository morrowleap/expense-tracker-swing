#!/usr/bin/env bash
set -e
clear

# recreate database
rm -f expense_tracker.db
sqlite3 expense_tracker.db < script_sqlite.sql

# compile & run
rm -rf bin
mkdir -p bin
javac -d bin -cp "sqlite-jdbc-3.50.1.0.jar" src/*.java
java -cp "bin:sqlite-jdbc-3.50.1.0.jar" src.ExpenseTrackerGUI
