CREATE TABLE IF NOT EXISTS ai_conversation (
                                               conversation_id TEXT PRIMARY KEY,
                                               user_id TEXT NOT NULL,
                                               title TEXT,
                                               status TEXT NOT NULL DEFAULT 'ACTIVE',
                                               message_count INTEGER NOT NULL DEFAULT 0,
                                               last_message_time DATETIME,
                                               archived_at DATETIME,
                                               deleted_at DATETIME,
                                               create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
                                               update_time DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS ai_message (
                                          message_id INTEGER PRIMARY KEY AUTOINCREMENT,
                                          conversation_id TEXT NOT NULL,
                                          role TEXT NOT NULL,
                                          name TEXT,
                                          content TEXT,
                                          status TEXT NOT NULL,
                                          finish_reason TEXT,
                                          prompt_tokens INTEGER,
                                          completion_tokens INTEGER,
                                          total_tokens INTEGER,
                                          error_message TEXT,
                                          create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
                                          update_time DATETIME DEFAULT CURRENT_TIMESTAMP,
                                          FOREIGN KEY (conversation_id) REFERENCES ai_conversation(conversation_id)
    );

CREATE INDEX IF NOT EXISTS idx_ai_message_conversation_time ON ai_message(conversation_id, message_id);