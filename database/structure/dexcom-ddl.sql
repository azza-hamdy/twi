-- Node
CREATE TABLE node (
    id bigint NOT NULL,
    serial_id character varying,
    -- cloud_long_term_key character varying NOT NULL,
    date_created timestamp without time zone DEFAULT now() NOT NULL,
    -- session_sequence_number bigint DEFAULT 1 NOT NULL,
    last_activity timestamp without time zone,
    deleted boolean
);

CREATE SEQUENCE node_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE node OWNER TO postgres;

ALTER TABLE ONLY node
    ADD CONSTRAINT node_pkey PRIMARY KEY (id);

ALTER TABLE ONLY node
    ADD CONSTRAINT serial_id_unique UNIQUE (serial_id);

ALTER TABLE node_id_seq OWNER TO postgres;

ALTER SEQUENCE node_id_seq OWNED BY node.id;

ALTER TABLE ONLY node ALTER COLUMN id SET DEFAULT nextval('node_id_seq'::regclass);

-- Controller
CREATE SEQUENCE controller_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
    
ALTER TABLE controller_id_seq OWNER TO postgres;

CREATE TABLE controller (
    id bigint DEFAULT nextval('controller_id_seq'::regclass) NOT NULL,
    device_number integer NOT NULL,
    date_created timestamp without time zone DEFAULT now() NOT NULL,
    date_updated timestamp without time zone,
    cloud_long_term_key character varying NOT NULL,
    session_sequence_number bigint DEFAULT 1 NOT NULL,
    last_activity timestamp without time zone,
    lat double precision,
    lng double precision
);

ALTER TABLE controller OWNER TO postgres;

ALTER TABLE ONLY controller
    ADD CONSTRAINT controller_pkey PRIMARY KEY (id);

-- Log
CREATE TABLE log (
    id bigint NOT NULL,
    controller_id bigint NOT NULL,
    node_id bigint NOT NULL,
    log_time bigint NOT NULL,
    flag integer NOT NULL,
    egv numeric NOT NULL,
    bg numeric NOT NULL,
    bg_time bigint NOT NULL,
    seq_no bigint NOT NULL,
    ble_address character varying,
    stream_count integer NOT NULL,
    date_created timestamp without time zone DEFAULT now() NOT NULL
);
ALTER TABLE log OWNER TO postgres;

ALTER TABLE ONLY log
    ADD CONSTRAINT log_pkey PRIMARY KEY (id);

CREATE SEQUENCE log_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE log_id_seq OWNER TO postgres;

ALTER SEQUENCE log_id_seq OWNED BY log.id;

ALTER TABLE ONLY log ALTER COLUMN id SET DEFAULT nextval('log_id_seq'::regclass);

-- Stream
CREATE TABLE stream (
    id bigint NOT NULL,
    log_time bigint NOT NULL,
    egv numeric NOT NULL,
    log_id bigint NOT NULL,
    date_created timestamp without time zone DEFAULT now() NOT NULL
);

ALTER TABLE stream OWNER TO postgres;

ALTER TABLE ONLY stream
    ADD CONSTRAINT stream_pkey PRIMARY KEY (id);

CREATE SEQUENCE stream_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE stream_id_seq OWNER TO postgres;

ALTER SEQUENCE stream_id_seq OWNED BY stream.id;

ALTER TABLE ONLY stream ALTER COLUMN id SET DEFAULT nextval('stream_id_seq'::regclass);

-- User
CREATE TABLE users (
    id bigint NOT NULL,
    firest_name character varying,
    last_name character varying,
    user_name character varying NOT NULL,
    password character varying NOT NULL
);
ALTER TABLE users OWNER TO postgres;

CREATE SEQUENCE users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE users_id_seq OWNER TO postgres;

ALTER TABLE ONLY users ALTER COLUMN id SET DEFAULT nextval('users_id_seq'::regclass);

-- Stream Header
CREATE SEQUENCE public.stream_header_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

ALTER SEQUENCE public.stream_header_id_seq
    OWNER TO postgres;


CREATE TABLE public.stream_header
(
    id bigint NOT NULL DEFAULT nextval('stream_header_id_seq'::regclass),
    starting_log_time bigint NOT NULL,
    egv_count numeric NOT NULL,
    period int NOT NULL,
    log_id bigint NOT NULL,
    date_created timestamp without time zone NOT NULL DEFAULT now(),
    CONSTRAINT stream_header_pkey PRIMARY KEY (id),
    CONSTRAINT stream_header_log_foreign_key FOREIGN KEY (log_id)
        REFERENCES public.log (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

-- Referential Integrity Constrains
ALTER TABLE ONLY log
    ADD CONSTRAINT log_controller_foreign_key FOREIGN KEY (controller_id) REFERENCES controller(id);


ALTER TABLE ONLY log
    ADD CONSTRAINT log_node_foreign_key FOREIGN KEY (node_id) REFERENCES controller(id);

ALTER TABLE ONLY stream
    ADD CONSTRAINT stream_log_foreign_key FOREIGN KEY (log_id) REFERENCES log(id);
