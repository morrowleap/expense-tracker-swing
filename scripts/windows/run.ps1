# compile & run
$files = Get-ChildItem -Path src -Recurse -Filter *.java | ForEach-Object { $_.FullName }
javac -d bin -cp "sqlite-jdbc-3.50.1.0.jar" $files
java -cp "bin;sqlite-jdbc-3.50.1.0.jar" gui.ExpenseTrackerGUI
