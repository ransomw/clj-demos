-- :name create-table-user
-- :command :execute
-- :result :raw
CREATE TABLE appuser (
  id serial PRIMARY KEY,
  username varchar(12) UNIQUE,
  password varchar(10),
  created_at timestamp not null default current_timestamp
);

-- :name drop-table-user
-- :command :execute
DROP TABLE IF EXISTS appuser;
