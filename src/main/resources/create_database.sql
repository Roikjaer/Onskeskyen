CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(255) NOT NULL UNIQUE,
                       email VARCHAR(255) UNIQUE,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE wishlists (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           name VARCHAR(255) NOT NULL,
                           user_id BIGINT NOT NULL,
                           is_public TINYINT(1) DEFAULT 0,
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           CONSTRAINT fk_wishlist_user
                               FOREIGN KEY (user_id) REFERENCES users(id)
                                   ON DELETE CASCADE
);

CREATE TABLE wishes (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        title VARCHAR(255) NOT NULL,
                        description TEXT,
                        link VARCHAR(500),
                        price DECIMAL(10,2),
                        reserved TINYINT(1) DEFAULT 0,
                        wishlist_id BIGINT NOT NULL,
                        CONSTRAINT fk_wish_wishlist
                            FOREIGN KEY (wishlist_id) REFERENCES wishlists(id)
                                ON DELETE CASCADE
);

CREATE INDEX idx_wishlist_user ON wishlists(user_id);
CREATE INDEX idx_wish_wishlist ON wishes(wishlist_id);