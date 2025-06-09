PRAGMA foreign_keys = ON;

CREATE TABLE IF NOT EXISTS users (
    user_id   INTEGER PRIMARY KEY AUTOINCREMENT,
    username  TEXT    NOT NULL UNIQUE,
    password  TEXT    NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS categories (
    category_id   INTEGER PRIMARY KEY AUTOINCREMENT,
    category_name TEXT    NOT NULL UNIQUE,
    created_at    DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at    DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS expenses (
    expense_id  INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id     INTEGER,
    amount      REAL       NOT NULL,
    date        DATE       NOT NULL,
    description TEXT       NOT NULL,
    category_id INTEGER,
    details     TEXT,
    created_at  DATETIME   DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME   DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY(user_id)    REFERENCES users(user_id),
    FOREIGN KEY(category_id) REFERENCES categories(category_id)
);

CREATE TRIGGER IF NOT EXISTS trg_users_upd
AFTER UPDATE ON users BEGIN
  UPDATE users SET updated_at = CURRENT_TIMESTAMP WHERE user_id = OLD.user_id;
END;

CREATE TRIGGER IF NOT EXISTS trg_categories_upd
AFTER UPDATE ON categories BEGIN
  UPDATE categories SET updated_at = CURRENT_TIMESTAMP WHERE category_id = OLD.category_id;
END;

CREATE TRIGGER IF NOT EXISTS trg_expenses_upd
AFTER UPDATE ON expenses BEGIN
  UPDATE expenses SET updated_at = CURRENT_TIMESTAMP WHERE expense_id = OLD.expense_id;
END;
