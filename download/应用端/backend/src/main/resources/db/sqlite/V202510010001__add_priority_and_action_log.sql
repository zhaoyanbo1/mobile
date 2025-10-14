ALTER TABLE reminder_item ADD COLUMN priority TEXT;

CREATE TABLE IF NOT EXISTS ai_action_log (
                                             id INTEGER PRIMARY KEY AUTOINCREMENT,
                                             conversation_id TEXT NOT NULL,
                                             user_id TEXT NOT NULL,
                                             tool_name TEXT NOT NULL,
                                             status TEXT NOT NULL,
                                             request_payload TEXT,
                                             response_payload TEXT,
                                             error_message TEXT,
                                             created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                             updated_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                             executed_at DATETIME
);

CREATE INDEX IF NOT EXISTS idx_ai_action_log_conversation ON ai_action_log(conversation_id);