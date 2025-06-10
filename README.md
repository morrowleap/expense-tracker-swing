Instructions:

# 1. Compile .java → bin/, with JDBC on classpath:
javac -d bin -cp sqlite-jdbc-3.50.1.0.jar src/tracker/*.java

# 2. Run from bin/, include JDBC JAR:
java -cp bin:sqlite-jdbc-3.50.1.0.jar tracker.ExpenseTrackerGUI


Via shell scripts:

% scripts/clean.sh
% scripts/init_db.sh
% scripts/run.sh
