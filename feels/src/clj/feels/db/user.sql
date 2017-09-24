-- :name add-user
-- :command :returning-execute
-- :result :raw
INSERT INTO appuser (username, password)
VALUES (:username, :password)
RETURNING id;

-- :name get-user :? :*
SELECT * FROM appuser WHERE username = :username;
