-- :name add-a-thing
-- :command :returning-execute
-- :result :raw
INSERT INTO thingone (nameone)
VALUES (:nameone)
RETURNING id;

-- :name all-the-names :? :*
SELECT nameone FROM thingone;
