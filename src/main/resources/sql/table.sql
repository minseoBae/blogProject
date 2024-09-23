CREATE TABLE posts (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       title VARCHAR(255) NOT NULL,
                       content TEXT NOT NULL,
                       status BOOLEAN NOT NULL,
                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       image_url VARCHAR(255),
                       likes INT DEFAULT 0,
                       views INT DEFAULT 0,
                       user_id BIGINT,
                       FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE images (
                        image_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        original_name VARCHAR(255) NOT NULL,
                        file_path VARCHAR(255),
                        post_id BIGINT,
                        FOREIGN KEY (post_id) REFERENCES posts(id)
);

CREATE TABLE likes (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       post_id BIGINT,
                       user_id BIGINT,
                       FOREIGN KEY (post_id) REFERENCES posts(id),
                       FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE tags (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      name VARCHAR(255) NOT NULL,
                      user_id BIGINT,
                      FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE post_tags (
                           post_id BIGINT,
                           tag_id BIGINT,
                           PRIMARY KEY (post_id, tag_id),
                           FOREIGN KEY (post_id) REFERENCES posts(id),
                           FOREIGN KEY (tag_id) REFERENCES tags(id)
);