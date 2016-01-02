SELECT f.id AS FILE_ID, f.title AS FILE_TITLE, f.filename AS FILE_NAME, c.title AS CAT_TITLE
FROM jml_phocadownload f 
 LEFT JOIN jml_phocadownload_categories c ON f.catid = c.id
 
 
 WITH tree (title, id)

AS (SELECT title, id
  FROM jml_phocadownload_categories
    UNION ALL
  SELECT title, id
        FROM jml_phocadownload_categories C
        INNER JOIN tree T ON T.id = C.id)

SELECT *
FROM   tree