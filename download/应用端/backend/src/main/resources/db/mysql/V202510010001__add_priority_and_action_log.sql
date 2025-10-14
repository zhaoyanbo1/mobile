ALTER TABLE `reminder_item` ADD COLUMN `priority` varchar(32) DEFAULT NULL;

CREATE TABLE IF NOT EXISTS `ai_action_log` (
                                               `id` BIGINT NOT NULL AUTO_INCREMENT,
                                               `conversation_id` varchar(255) NOT NULL,
    `user_id` varchar(255) NOT NULL,
    `tool_name` varchar(128) NOT NULL,
    `status` varchar(32) NOT NULL,
    `request_payload` longtext,
    `response_payload` longtext,
    `error_message` varchar(512),
    `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
    `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `executed_at` datetime DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_ai_action_log_conversation` (`conversation_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;