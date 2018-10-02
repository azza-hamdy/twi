CREATE SEQUENCE public.block_id_seq
  INCREMENT 1
  MINVALUE 0
  MAXVALUE 9223372036854775807
  START 0
  CACHE 1;

CREATE TABLE public.block
(
  id bigint NOT NULL DEFAULT nextval('block_id_seq'::regclass),
  version integer NOT NULL,
  previousBlockHash character varying NOT NULL,
  topHash character varying,
  creationTime timestamp without time zone,
  transactionsNumber integer,
  signature character varying,
  CONSTRAINT "Primary Key" PRIMARY KEY (id)
);

CREATE SEQUENCE public.transaction_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

CREATE TABLE public.transaction
(
  id bigint NOT NULL DEFAULT nextval('transaction_id_seq'::regclass),
  blockId bigint NOT NULL,
  version character varying,
  writerId character varying,
  tagId character varying,
  length integer,
  hash character varying NOT NULL,
  signature character varying NOT NULL,
  index integer NOT NULL,
  creationTime timestamp without time zone NOT NULL,
  transaction json,
  CONSTRAINT "Priamry Key" PRIMARY KEY (id),
  CONSTRAINT "Block Reference" FOREIGN KEY (blockId)
      REFERENCES public.block (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
);

INSERT INTO public.block(version, previousBlockHash) VALUES (0, 'NULL');
