-- Seed: суперпользователь lena
-- Выполняется автоматически при первом старте postgres-контейнера.
-- INSERT ... WHERE NOT EXISTS — безопасно запускать повторно, дубликатов не будет.

INSERT INTO users (email, password_hash, is_super, created_at, updated_at)
SELECT 'lena', '$2a$12$rfZydJQHYDkupezBKwTJLuYlu8Frh7KcZp.C59qdCryk816H/mSly', TRUE, NOW(), NOW()
WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE email = 'lena'
);
