-- Create tables for collaborative team activities
CREATE TABLE IF NOT EXISTS team_activity (
                                             id INTEGER PRIMARY KEY AUTOINCREMENT,
                                             creator_user_id INTEGER NOT NULL,
                                             title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    activity_time DATETIME NOT NULL,
    location VARCHAR(255) NOT NULL,
    min_participants INTEGER NOT NULL,
    max_participants INTEGER NOT NULL,
    created_at DATETIME NOT NULL DEFAULT (datetime('now')),
    updated_at DATETIME NOT NULL DEFAULT (datetime('now')),
    CONSTRAINT fk_team_activity_creator FOREIGN KEY (creator_user_id) REFERENCES user_info(user_info_id)
    );

CREATE INDEX IF NOT EXISTS idx_team_activity_time ON team_activity(activity_time);

CREATE TABLE IF NOT EXISTS team_activity_participant (
                                                         id INTEGER PRIMARY KEY AUTOINCREMENT,
                                                         activity_id INTEGER NOT NULL,
                                                         user_id INTEGER NOT NULL,
                                                         is_host INTEGER NOT NULL DEFAULT 0,
                                                         joined_at DATETIME NOT NULL,
                                                         CONSTRAINT uq_team_activity_participant UNIQUE(activity_id, user_id),
    CONSTRAINT fk_team_activity_participant_activity FOREIGN KEY (activity_id) REFERENCES team_activity(id) ON DELETE CASCADE,
    CONSTRAINT fk_team_activity_participant_user FOREIGN KEY (user_id) REFERENCES user_info(user_info_id) ON DELETE CASCADE
    );

CREATE INDEX IF NOT EXISTS idx_team_activity_participant_activity ON team_activity_participant(activity_id);

CREATE TABLE IF NOT EXISTS team_activity_application (
                                                         id INTEGER PRIMARY KEY AUTOINCREMENT,
                                                         activity_id INTEGER NOT NULL,
                                                         applicant_user_id INTEGER NOT NULL,
                                                         status VARCHAR(32) NOT NULL,
    created_at DATETIME NOT NULL,
    decision_at DATETIME,
    decision_by INTEGER,
    CONSTRAINT uq_team_activity_application UNIQUE(activity_id, applicant_user_id),
    CONSTRAINT fk_team_activity_application_activity FOREIGN KEY (activity_id) REFERENCES team_activity(id) ON DELETE CASCADE,
    CONSTRAINT fk_team_activity_application_applicant FOREIGN KEY (applicant_user_id) REFERENCES user_info(user_info_id) ON DELETE CASCADE
    );

CREATE INDEX IF NOT EXISTS idx_team_activity_application_activity ON team_activity_application(activity_id);
CREATE INDEX IF NOT EXISTS idx_team_activity_application_status ON team_activity_application(status);

-- Link reminders with team activities so approved participants receive alerts
ALTER TABLE reminder_item ADD COLUMN team_activity_id INTEGER;
CREATE INDEX IF NOT EXISTS idx_reminder_item_team_activity ON reminder_item(team_activity_id);