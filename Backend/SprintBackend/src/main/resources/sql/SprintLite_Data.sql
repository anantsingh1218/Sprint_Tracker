-- Defer constraint checking for INSERT
BEGIN;
SET CONSTRAINTS ALL DEFERRED;

INSERT INTO Sprint (id, sprintName, sprintDuration, status, startDate, endDate)
VALUES
  (1, 'Sprint 1', 15, 'ACTIVE', '2026-06-01', '2026-06-15'),
  (2, 'Sprint 2', 15, 'PLANNED', '2026-06-16', '2026-06-30');
INSERT INTO Users (id, username, role, email, passwordHash, createdAt, createdBy, updatedAt, updatedBy)
VALUES
  (1, 'Alice', 'Product Manager', 'alice@gmail.com', '12345hsnmo39@19xhq][23', NULL, NULL, NULL, NULL),
      (2, 'Bob', 'Developer','bob@gmail.com', '1234hsnmo39@19xhq][23|', NULL, NULL, NULL, NULL),
  (3, 'Charlie', 'Scrum Master','charlie@gmail.com', '1245hsnmo39@19xhq][23\', NULL, NULL, NULL, NULL);
INSERT INTO ProductTable (productId, productName, description, ownerId, createdAt, createdBy, updatedAt, updatedBy)
VALUES
  (1, 'FSM', 'Field Service Management', 1, NULL, NULL, NULL, NULL),
  (2, 'Starwatch', 'Analytics Platform', 1, NULL, NULL, NULL, NULL);
INSERT INTO FeatureTable (featureId, title, description, productId, sprintId, featureStatus, priority, estimatedStoryPoints, remainingStoryPoints, createdAt, createdBy, updatedAt, updatedBy)
VALUES
  (1, 'Lead Management', 'Manage customer leads', 1, 1, 'IN_PROGRESS', 'HIGH', 8, 3, NULL, NULL, NULL, NULL),
  (2, 'Business Dashboards', 'Analytics dashboards', 2, 2, 'OPEN', 'MEDIUM', 13, 13, NULL, NULL, NULL, NULL);
INSERT INTO StoryTable (id, title, body, featureId, userId, storyStatus, priority, sprintId, storyPoints, createdAt, createdBy, updatedAt, updatedBy)
VALUES
  (1, 'Lead Creation', 'As a sales rep I can create leads', 1, 2, 'IN_PROGRESS', 'HIGH', 1, 5, NULL, NULL, NULL, NULL),
  (2, 'Lead Search', 'As a sales rep I can search leads', 1, 2, 'OPEN', 'MEDIUM', 1, 3, NULL, NULL, NULL, NULL),
  (3, 'Dashboard Creation', 'As a manager I can create dashboards', 2, 2, 'OPEN', 'HIGH', 2, 8, NULL, NULL, NULL, NULL);
INSERT INTO TaskTable (id, title, body, userId, taskStatus, priority, storyId, sprintId, originalEstimateHours, remainingEstimateHours, createdAt, createdBy, updatedAt, updatedBy)
VALUES
  (1, 'Gather Requirements', 'Collect lead requirements', 2, 'DONE', 'MEDIUM', 1, 1, 4, 0, NULL, NULL, NULL, NULL),
  (2, 'Design Lead Screen', 'Create lead UI', 2, 'IN_PROGRESS', 'HIGH', 1, 1, 8, 3, NULL, NULL, NULL, NULL),
  (3, 'Implement Lead API', 'Create backend APIs', 2, 'OPEN', 'HIGH', 1, 1, 12, 12, NULL, NULL, NULL, NULL),
  (4, 'Dashboard Wireframe', 'Create dashboard wireframe', 2, 'OPEN', 'MEDIUM', 3, 2, 6, 6, NULL, NULL, NULL, NULL);
INSERT INTO Bugs (id, bugStatus, priority, description, assignedTo, storyId, sprintId, originalEstimateHours, remainingEstimateHours, createdAt, createdBy, updatedAt, updatedBy)
VALUES
  (1, 'OPEN', 'CRITICAL', 'Lead page crashes on save', 2, 1, 1, 6, 6, NULL, NULL, NULL, NULL),
  (2, 'IN_PROGRESS', 'HIGH', 'Duplicate lead creation issue', 2, 1, 1, 8, 3, NULL, NULL, NULL, NULL),
  (3, 'OPEN', 'MEDIUM', 'Dashboard timeout issue', 2, 3, 2, 10, 10, NULL, NULL, NULL, NULL);
INSERT INTO Attachments (id, fileName, filePath, uploadedBy, createdAt, createdBy, updatedAt, updatedBy)
VALUES
  (1, 'lead-error.png', 'uploadslead-error.png', 2, NULL, NULL, NULL, NULL),
  (2, 'dashboard-wireframe.pdf', 'uploadsdashboard-wireframe.pdf', 2, NULL, NULL, NULL, NULL);
INSERT INTO AttachmentMapping (id, attachmentId, entityType, entityId)
VALUES
  (1, 1, 'BUG', 1),
  (2, 2, 'TASK', 4);
INSERT INTO Comments (id, comment, entityType, entityId, createdBy, createdAt, updatedBy, updatedAt)
VALUES
  (1, 'Need API validation before release', 'STORY', 1, 'Alice', NULL, 'Alice', NULL),
  (2, 'Crash reproducible in QA environment', 'BUG', 1, 'Charlie', NULL, 'Charlie', NULL);
INSERT INTO UserProductMapping (id, userId, productId)
VALUES
  (1, 1, 1),
  (2, 2, 1),
  (3, 3, 2);
INSERT INTO WorkLog (id, taskId, bugId, userId, workDate, hoursSpent, remarks)
VALUES
  (1, 1, NULL, 2, '2026-06-02', 4, 'Requirement gathering completed'),
  (2, 2, NULL, 2, '2026-06-04', 5, 'Lead screen design completed'),
  (3, 3, NULL, 2, '2026-06-05', 3, 'Started API development'),
  (4, 3, NULL, 2, '2026-06-06', 4, 'Implemented create lead endpoint'),
  (5, NULL, 1, 2, '2026-06-07', 2, 'Root cause analysis'),
  (6, NULL, 1, 2, '2026-06-08', 3, 'Fixed save crash issue'),
  (7, NULL, 2, 2, '2026-06-09', 5, 'Duplicate lead bug investigation');

INSERT INTO DSUNotes (id, notes, notesDate, entityType, entityId, createdBy, createdAt, updatedBy, updatedAt)
VALUES
    (1, 'Completed Effectively', '2026-06-03', 'STORY', 1, null, null, null, null),
    (2, 'Requirements were modified , expect delay', '2026-06-04', 'BUG', 2, null, null, null, null);

SET CONSTRAINTS ALL IMMEDIATE;
COMMIT;