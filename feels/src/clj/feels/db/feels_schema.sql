-- :name create-table-feels
-- :command :execute
-- :result :raw
CREATE TABLE feels (
  id serial PRIMARY KEY,
  appuser_id integer references appuser (id),
  happy integer not null,
  sleepy integer not null,
  grumpy integer not null,
  created_at timestamp not null default current_timestamp
);

-- :name drop-table-feels
-- :command :execute
DROP TABLE IF EXISTS feels;
