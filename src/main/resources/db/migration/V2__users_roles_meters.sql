-- Insert default roles
INSERT INTO roles (name)
VALUES ('ROLE_ADMIN'), ('ROLE_USER');

-- Insert default users
INSERT INTO users (IDNP, name, surname, password, enabled, token_expired)
VALUES ('admin', 'Admin', 'User', '{noop}admin', TRUE, FALSE),
       ('123456789', 'Ivan', 'Ivanov', '{noop}user', TRUE, FALSE);

-- Assign ROLE_ADMIN to admin user
INSERT INTO user_roles (user_id, role_id)
VALUES ((SELECT user_id FROM users WHERE IDNP = 'admin'),
        (SELECT role_id FROM roles WHERE name = 'ROLE_ADMIN'));

-- Assign ROLE_USER to user
INSERT INTO user_roles (user_id, role_id)
VALUES ((SELECT user_id FROM users WHERE IDNP = '123456789'),
        (SELECT role_id FROM roles WHERE name = 'ROLE_USER'));

CREATE TABLE meters (
                        meter_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        serial_number VARCHAR(100) NOT NULL,
                        type VARCHAR(50),
                        installed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        user_id BIGINT NOT NULL,
                        CONSTRAINT fk_meter_user FOREIGN KEY (user_id)
                            REFERENCES users (user_id)
                            ON DELETE CASCADE
);