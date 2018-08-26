
Create table node_sequence (
seq_no bigint not null default 0
);

CREATE OR REPLACE FUNCTION incrementSequence() RETURNS TRIGGER  as $$
   BEGIN
	  UPDATE node_sequence SET seq_no = seq_no + 1;
      RETURN NEW;
   END;
 $$ LANGUAGE plpgsql;


CREATE  TRIGGER node_update AFTER UPDATE OF deleted ON node
FOR EACH ROW EXECUTE PROCEDURE incrementSequence();

CREATE TRIGGER node_insert AFTER INSERT ON public.node
    FOR EACH ROW EXECUTE PROCEDURE public.incrementsequence();