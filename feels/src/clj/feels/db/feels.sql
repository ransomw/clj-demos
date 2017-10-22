-- :name add-feels
-- :command :returning-execute
-- :result :raw
INSERT INTO feels (appuser_id, happy, sleepy, grumpy)
VALUES (:user-id, :happy, :sleepy, :grumpy)
RETURNING id;

-- :name get-all-feels :? :*
SELECT * FROM feels WHERE appuser_id = :user-id;

-- :name get-curr-feels :? :*
SELECT * FROM feels WHERE appuser_id = :user-id
ORDER BY created_at
LIMIT 1;

-- :name update-feels :! :n
UPDATE feels
SET (happy, sleepy, grumpy) = (:happy, :sleepy, :grumpy)
WHERE id = :feels-id;
