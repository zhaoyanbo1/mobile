CREATE TABLE IF NOT EXISTS ai_conversation (
                                               conversation_id VARCHAR(64) PRIMARY KEY,
    user_id VARCHAR(64) NOT NULL,
    title VARCHAR(255),
    status VARCHAR(32) NOT NULL DEFAULT 'ACTIVE',
    message_count INT NOT NULL DEFAULT 0,
    last_message_time DATETIME,
    archived_at DATETIME,
    deleted_at DATETIME,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS ai_message (
                                          message_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                          conversation_id VARCHAR(64) NOT NULL,
    role VARCHAR(32) NOT NULL,
    name VARCHAR(64),
    content LONGTEXT,
    status VARCHAR(32) NOT NULL,
    finish_reason VARCHAR(64),
    prompt_tokens INT,
    completion_tokens INT,
    total_tokens INT,
    error_message TEXT,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_ai_message_conversation FOREIGN KEY (conversation_id) REFERENCES ai_conversation(conversation_id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE INDEX idx_ai_message_conversation_time ON ai_message(conversation_id, message_id);