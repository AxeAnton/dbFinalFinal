-- все записи
-- SELECT имя_столбца1, имя_столбца2
-- FROM имя_таблицы
-- WHERE ;
SELECT * FROM author;

-- названия статей по возрастанию дат
-- ORDER BY DESC - по убыванию
-- ORDER BY ASC - по возрастанию
SELECT title, created_on
FROM article ORDER BY created_on;

-- = < > <= >= !=
SELECT * FROM article
WHERE author_id >= 2;

-- BETWEEN ... AND ... диапазон
SELECT title, created_on FROM article
WHERE EXTRACT(YEAR FROM created_on)
BETWEEN 2015 AND 2018;

-- AND OR NOT
SELECT title FROM article
WHERE author_id = 1
AND created_on > '2018-12-12';

-- IN(value1, value2, value3)
SELECT * FROM article
WHERE author_id IN(2, 3, 12); -- OR

-- NOT IN(value1, value2, value3)
SELECT * FROM article
WHERE author_id NOT IN(2, 1, 3);

-- LIKE
-- Py%
-- %on
-- %o%
SELECT * FROM article
WHERE text LIKE '%про%';

-- sum avg min max
SELECT ceil(AVG(age)) as author_age FROM author;
-- Вложенные select запросы - пользователи с возрастом выше среднего
SELECT * FROM author
WHERE age > (SELECT AVG(age) FROM author);

SELECT ceil(MIN(age)) as author_age FROM author;
-- Вложенные select запросы - пользователи с возрастом выше среднего
SELECT * FROM author
WHERE age = (SELECT MIN(age) FROM author);

SELECT ceil(MIN(age)) as author_age FROM author;

SELECT count(title) AS articles_count FROM article;

-- GROUP BY
SELECT count(article.title)
AS articles_count, author.name
FROM author, article
WHERE author.author_id = article.author_id
GROUP BY author.author_id;

-- JOIN
SELECT u.name, a.title
FROM author u
INNER JOIN article a
ON u.author_id = a.author_id;
-- вместо ON можно использовать
-- USING(id), если названия столбцов,
-- по которым таблицы связаны одинаковые

-- LEFT JOIN - позволяет извлекать
-- данные из таблицы, по возможности
-- дополняя их данными из другой таблицы
-- RIGHT JOIN
SELECT u.name, a.title
FROM author u
RIGHT JOIN article a
ON u.author_id = a.author_id;

SELECT u.name, a.title
FROM author u
LEFT JOIN article a
ON u.author_id = a.author_id;

-- пользователи,  которые не писали статей
SELECT u.name
FROM author u
LEFT JOIN article a
ON u.author_id = a.author_id
WHERE a.title IS NULL;
