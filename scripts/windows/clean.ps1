Remove-Item -Recurse -Force bin -ErrorAction SilentlyContinue
New-Item -ItemType Directory -Path bin | Out-Null
Remove-Item expense_tracker.db -ErrorAction SilentlyContinue
