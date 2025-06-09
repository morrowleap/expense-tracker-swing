Overview:

ExpenseTracker/
├── README.md
├── resources/
│   ├── script.sql
│   └── script_sqlite.sql
├── scripts/
│   ├── clean.sh
│   └── run.sh
├── sqlite-jdbc-3.50.1.0.jar
└── src/
    └── ExpenseTrackerGUI.java


Instructions:

# 1. Compile .java → bin/, with JDBC on classpath:
javac -d bin -cp sqlite-jdbc-3.50.1.0.jar src/*.java

# 2. Run from bin/, include JDBC JAR:
java  -cp bin:sqlite-jdbc-3.50.1.0.jar src.ExpenseTrackerGUI