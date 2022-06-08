-- СОЗДАНИЕ ТАБЛИЦ
CREATE TABLE IF NOT EXISTS author(
   author_id serial PRIMARY KEY,
   name VARCHAR (50) NOT NULL,
    age INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS article(
	article_id serial PRIMARY KEY,
	title VARCHAR (50) UNIQUE NOT NULL,
	text TEXT NOT NULL,
	created_on TIMESTAMP NOT NULL,
	author_id INTEGER NOT NULL,
	CONSTRAINT author_article FOREIGN KEY (author_id)
	REFERENCES author(author_id)
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

-- ВСТАВКА ДАННЫХ В ТАБЛИЦУ
INSERT INTO author (name, age)
VALUES
('qwerty', 28),
('reanno', 46),
('ivan334', 32),
('apple', 21),
('wind23', 23);

INSERT INTO article (title, text, created_on, author_id)
VALUES
('Stream API', 'Статья про Stream API', '2017-10-19 10:23', 1),
('Lambda', 'Статья про Lambda', '2019-03-27 16:33', 1),
('Java 13', 'Статья про Java 13', '2019-11-28 17:10', 2),
('Garbage Collectors', 'Статья про GC', '2019-08-07 11:55', 2),
('Hibernate', 'Статья про Hibernate', '2018-12-01 21:30', 2),
('Collections API', 'Collections API', '2019-11-22 22:29', 3);

