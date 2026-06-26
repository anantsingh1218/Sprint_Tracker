-- Defer constraint checking for INSERT
ROLLBACK;
BEGIN;
SET CONSTRAINTS ALL DEFERRED;

-- =========================
-- USERS
-- =========================
INSERT INTO Users (username, role, email, passwordHash, createdAt, createdBy, updatedAt, updatedBy)
VALUES
    ('Alice', 'ROLE_PM', 'alice@gmail.com', '12345hsnmo39@19xhq][23',
     CURRENT_TIMESTAMP, 'SYSTEM', CURRENT_TIMESTAMP, 'SYSTEM'),

    ('Bob', 'ROLE_Developer', 'bob@gmail.com', '1234hsnmo39@19xhq][23|',
     CURRENT_TIMESTAMP, 'SYSTEM', CURRENT_TIMESTAMP, 'SYSTEM'),

    ('Charlie', 'ROLE_Scrum_Master', 'charlie@gmail.com', '1245hsnmo39@19xhq][23\\',
     CURRENT_TIMESTAMP, 'SYSTEM', CURRENT_TIMESTAMP, 'SYSTEM');


-- =========================
-- PRODUCT
-- =========================
INSERT INTO ProductTable (productName, description, ownerId, createdAt, createdBy, updatedAt, updatedBy)
VALUES
    ('FSM', 'Field Service Management', 1,
     CURRENT_TIMESTAMP, 'SYSTEM', CURRENT_TIMESTAMP, 'SYSTEM'),

    ('Starwatch', 'Analytics Platform', 1,
     CURRENT_TIMESTAMP, 'SYSTEM', CURRENT_TIMESTAMP, 'SYSTEM');


-- =========================
-- SPRINT
-- =========================
INSERT INTO Sprint (sprintName, sprintDuration, status, productId, startDate, endDate)
VALUES
    ('Sprint 1', 15, 'ACTIVE', 1, '2026-06-01', '2026-06-15'),
    ('Sprint 2', 15, 'PLANNED', 2, '2026-06-16', '2026-06-30');


-- =========================
-- FEATURE
-- =========================
INSERT INTO FeatureTable (title, description, productId, sprintId, featureStatus, priority, estimatedStoryPoints, remainingStoryPoints, createdAt, createdBy, updatedAt, updatedBy)
VALUES
    ('Lead Management', 'Manage customer leads', 1, 1, 'IN_PROGRESS', 'HIGH', 8, 3,
     CURRENT_TIMESTAMP, 'SYSTEM', CURRENT_TIMESTAMP, 'SYSTEM'),

    ('Business Dashboards', 'Analytics dashboards', 2, 2, 'OPEN', 'MEDIUM', 13, 13,
     CURRENT_TIMESTAMP, 'SYSTEM', CURRENT_TIMESTAMP, 'SYSTEM');


-- =========================
-- STORY
-- =========================
INSERT INTO StoryTable (title, body, featureId, userId, storyStatus, priority, sprintId, storyPoints, createdAt, createdBy, updatedAt, updatedBy)
VALUES
    ('Lead Creation', 'As a sales rep I can create leads', 1, 2, 'IN_PROGRESS', 'HIGH', 1, 5,
     CURRENT_TIMESTAMP, 'SYSTEM', CURRENT_TIMESTAMP, 'SYSTEM'),

    ('Lead Search', 'As a sales rep I can search leads', 1, 2, 'OPEN', 'MEDIUM', 1, 3,
     CURRENT_TIMESTAMP, 'SYSTEM', CURRENT_TIMESTAMP, 'SYSTEM'),

    ('Dashboard Creation', 'As a manager I can create dashboards', 2, 2, 'OPEN', 'HIGH', 2, 8,
     CURRENT_TIMESTAMP, 'SYSTEM', CURRENT_TIMESTAMP, 'SYSTEM');


-- =========================
-- TASK
-- =========================
INSERT INTO TaskTable (title, body, userId, taskStatus, priority, storyId, sprintId, originalEstimateHours, remainingEstimateHours, createdAt, createdBy, updatedAt, updatedBy)
VALUES
    ('Gather Requirements', 'Collect lead requirements', 2, 'DONE', 'MEDIUM', 1, 1, 4, 0,
     CURRENT_TIMESTAMP, 'SYSTEM', CURRENT_TIMESTAMP, 'SYSTEM'),

    ('Design Lead Screen', 'Create lead UI', 2, 'IN_PROGRESS', 'HIGH', 1, 1, 8, 3,
     CURRENT_TIMESTAMP, 'SYSTEM', CURRENT_TIMESTAMP, 'SYSTEM'),

    ('Implement Lead API', 'Create backend APIs', 2, 'OPEN', 'HIGH', 1, 1, 12, 12,
     CURRENT_TIMESTAMP, 'SYSTEM', CURRENT_TIMESTAMP, 'SYSTEM'),

    ('Dashboard Wireframe', 'Create dashboard wireframe', 2, 'OPEN', 'MEDIUM', 3, 2, 6, 6,
     CURRENT_TIMESTAMP, 'SYSTEM', CURRENT_TIMESTAMP, 'SYSTEM');


-- =========================
-- BUGS
-- =========================
INSERT INTO Bugs (bugStatus, priority, description, assignedTo, storyId, sprintId, originalEstimateHours, remainingEstimateHours, createdAt, createdBy, updatedAt, updatedBy)
VALUES
    ('OPEN', 'CRITICAL', 'Lead page crashes on save', 2, 1, 1, 6, 6,
     CURRENT_TIMESTAMP, 'SYSTEM', CURRENT_TIMESTAMP, 'SYSTEM'),

    ('IN_PROGRESS', 'HIGH', 'Duplicate lead creation issue', 2, 1, 1, 8, 3,
     CURRENT_TIMESTAMP, 'SYSTEM', CURRENT_TIMESTAMP, 'SYSTEM'),

    ('OPEN', 'MEDIUM', 'Dashboard timeout issue', 2, 3, 2, 10, 10,
     CURRENT_TIMESTAMP, 'SYSTEM', CURRENT_TIMESTAMP, 'SYSTEM');


-- =========================
-- ATTACHMENTS
-- =========================
INSERT INTO Attachments (fileName, filePath, uploadedBy, createdAt, createdBy, updatedAt, updatedBy)
VALUES
    ('lead-error.png', 'uploads/lead-error.png', 2,
     CURRENT_TIMESTAMP, 'SYSTEM', CURRENT_TIMESTAMP, 'SYSTEM'),

    ('dashboard-wireframe.pdf', 'uploads/dashboard-wireframe.pdf', 2,
     CURRENT_TIMESTAMP, 'SYSTEM', CURRENT_TIMESTAMP, 'SYSTEM');


-- =========================
-- ATTACHMENT MAPPING
-- =========================
INSERT INTO AttachmentMapping (attachmentId, entityType, entityId)
VALUES
    (1, 'BUG', 1),
    (2, 'TASK', 4);


-- =========================
-- COMMENTS
-- =========================
INSERT INTO Comments (comment, entityType, entityId, createdBy, createdAt, updatedBy, updatedAt)
VALUES
    ('Need API validation before release', 'STORY', 1, 'Alice',
     CURRENT_TIMESTAMP, 'Alice', CURRENT_TIMESTAMP),

    ('Crash reproducible in QA environment', 'BUG', 1, 'Charlie',
     CURRENT_TIMESTAMP, 'Charlie', CURRENT_TIMESTAMP);


-- =========================
-- USER-PRODUCT MAPPING
-- =========================
INSERT INTO UserProductMapping (userId, productId)
VALUES
    (1, 1),
    (2, 1),
    (3, 2);


-- =========================
-- WORKLOG
-- =========================
INSERT INTO WorkLog (taskId, bugId, userId, workDate, hoursSpent, remarks)
VALUES
    (1, NULL, 2, '2026-06-02', 4, 'Requirement gathering completed'),
    (2, NULL, 2, '2026-06-04', 5, 'Lead screen design completed'),
    (3, NULL, 2, '2026-06-05', 3, 'Started API development'),
    (3, NULL, 2, '2026-06-06', 4, 'Implemented create lead endpoint'),
    (NULL, 1, 2, '2026-06-07', 2, 'Root cause analysis'),
    (NULL, 1, 2, '2026-06-08', 3, 'Fixed save crash issue'),
    (NULL, 2, 2, '2026-06-09', 5, 'Duplicate lead bug investigation');

-- =========================
-- DSU NOTES
-- =========================
INSERT INTO DSUNotes ( notesDate, entityType, entityId, status, completedWork, blockers, nextplan, createdBy, createdAt, updatedBy, updatedAt)
VALUES
    (  '2026-06-03', 'STORY', 1, 'BLOCKED','completed task 1','Escalated to xyz Team','Resolve Blocker', 'Bob', CURRENT_TIMESTAMP, 'Bob', CURRENT_TIMESTAMP),
    (  '2026-06-04', 'BUG', 2,'OPEN','As per plan','None','As per decided plan', 'Bob', CURRENT_TIMESTAMP, 'Bob' , CURRENT_TIMESTAMP);

SET CONSTRAINTS ALL IMMEDIATE;
COMMIT;