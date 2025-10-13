-- 用户-奖章 关系表
CREATE TABLE IF NOT EXISTS user_medal (
                                          id          INTEGER PRIMARY KEY AUTOINCREMENT,
                                          user_id     INTEGER NOT NULL,
                                          medal_id    INTEGER NOT NULL,
                                          created_at  DATETIME DEFAULT CURRENT_TIMESTAMP,
    -- 关键点：同一用户同一奖章只允许一条记录（避免重复）
                                          UNIQUE(user_id, medal_id)
);
