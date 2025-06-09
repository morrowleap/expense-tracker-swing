# recreate database
rm -f expense_tracker.db
sqlite3 expense_tracker.db < resources/script_sqlite.sql
