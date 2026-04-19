INSERT INTO users (username, email) VALUES
                                        ('alice', 'alice@email.com'),
                                        ('bob', 'bob@email.com');

INSERT INTO wishlists (name, user_id, is_public) VALUES
                                                     ('Alice Birthday', 1, 1),
                                                     ('Bob Christmas', 2, 0);

INSERT INTO wishes (title, description, link, price, wishlist_id) VALUES
                                                                      ('iPhone 15', 'Latest Apple phone', NULL, 9999.99, 1),
                                                                      ('AirPods', 'Noise cancelling headphones', NULL, 1999.99, 1),
                                                                      ('PS5', 'Gaming console', NULL, 4999.99, 2);