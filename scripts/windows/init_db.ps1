Remove-Item expense_tracker.db -ErrorAction SilentlyContinue
sqlite3 expense_tracker.db < resources\script_sqlite.sql
