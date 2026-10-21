-- roles table
CREATE TABLE roles (
    id integer NOT NULL,
    role character varying(50) NOT NULL
);

CREATE SEQUENCE roles_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE roles_id_seq OWNED BY roles.id;
ALTER TABLE ONLY roles ALTER COLUMN id SET DEFAULT nextval('roles_id_seq'::regclass);
ALTER TABLE ONLY roles ADD CONSTRAINT roles_pkey PRIMARY KEY (id);
ALTER TABLE ONLY roles ADD CONSTRAINT roles_role_key UNIQUE (role);


-- users table
CREATE TABLE users (
    id integer NOT NULL,
    username character varying(50) NOT NULL,
    email character varying(100) NOT NULL,
    name character varying(50) NOT NULL,
    lastname character varying(50) NOT NULL,
    born_is date NOT NULL,
    phone character varying(12) NOT NULL,
    password character varying(255) NOT NULL,
    is_active boolean NOT NULL,
    description character varying(1000)
);

CREATE SEQUENCE users_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE users_id_seq OWNED BY users.id;
ALTER TABLE ONLY users ALTER COLUMN id SET DEFAULT nextval('users_id_seq'::regclass);
ALTER TABLE ONLY users ADD CONSTRAINT users_pkey PRIMARY KEY (id);
ALTER TABLE ONLY users ADD CONSTRAINT users_email_key UNIQUE (email);
ALTER TABLE ONLY users ADD CONSTRAINT users_phone_key UNIQUE (phone);


-- posts table
CREATE TABLE posts (
    id integer NOT NULL,
    title character varying(1000) NOT NULL,
    description text NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    user_id integer
);

CREATE SEQUENCE posts_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE posts_id_seq OWNED BY posts.id;
ALTER TABLE ONLY posts ALTER COLUMN id SET DEFAULT nextval('posts_id_seq'::regclass);
ALTER TABLE ONLY posts ADD CONSTRAINT posts_pkey PRIMARY KEY (id);


-- comments table
CREATE TABLE comments (
    id integer NOT NULL,
    description text NOT NULL,
    user_id integer,
    reply_id integer,
    post_id integer
);

CREATE SEQUENCE comments_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE comments_id_seq OWNED BY comments.id;
ALTER TABLE ONLY comments ALTER COLUMN id SET DEFAULT nextval('comments_id_seq'::regclass);
ALTER TABLE ONLY comments ADD CONSTRAINT comments_pkey PRIMARY KEY (id);


-- likes table
CREATE TABLE likes (
    user_id integer NOT NULL,
    post_id integer NOT NULL
);

ALTER TABLE ONLY likes ADD CONSTRAINT likes_pkey PRIMARY KEY (user_id, post_id);


-- userroles table
CREATE TABLE userroles (
    user_id integer NOT NULL,
    role_id integer NOT NULL
);

ALTER TABLE ONLY userroles ADD CONSTRAINT userroles_pkey PRIMARY KEY (user_id, role_id);


-- Foreign Keys
ALTER TABLE ONLY posts
    ADD CONSTRAINT user_id FOREIGN KEY (user_id) REFERENCES users(id);

ALTER TABLE ONLY comments
    ADD CONSTRAINT comments_post_id_fkey FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE;

ALTER TABLE ONLY comments
    ADD CONSTRAINT comments_reply_id_fkey FOREIGN KEY (reply_id) REFERENCES comments(id) ON DELETE CASCADE;

ALTER TABLE ONLY comments
    ADD CONSTRAINT comments_user_id_fkey FOREIGN KEY (user_id) REFERENCES users(id);

ALTER TABLE ONLY likes
    ADD CONSTRAINT likes_post_id_fkey FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE;

ALTER TABLE ONLY likes
    ADD CONSTRAINT likes_user_id_fkey FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;

ALTER TABLE ONLY userroles
    ADD CONSTRAINT userroles_role_id_fkey FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE;

ALTER TABLE ONLY userroles
    ADD CONSTRAINT userroles_user_id_fkey FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;