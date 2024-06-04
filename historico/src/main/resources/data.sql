DROP TABLE IF EXISTS tab_falhalogin;
CREATE TABLE tab_falhalogin (
  id_falhalogin INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(255),
  date TIMESTAMP
);
COMMIT;