DELETE FROM request;
INSERT INTO request (id, comment, created_at, ord, status, updated_at, client_id_id, operator_id_id) values
(1, null, now(), 'salad', 'new', now(), 1, null);
