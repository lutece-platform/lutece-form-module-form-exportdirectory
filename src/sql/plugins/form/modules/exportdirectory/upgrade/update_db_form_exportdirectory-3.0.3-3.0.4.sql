-- 
-- Add colum for the iteration number for the entry configuration
-- 
ALTER TABLE form_exportdirectory_entryconfiguration ADD COLUMN iteration_number INT DEFAULT -1 AFTER id_form_entry;

-- 
-- Update the existing primary key of the form_exportdirectory_entryconfiguration table to add the iteration_number
-- 
ALTER TABLE form_exportdirectory_entryconfiguration DROP PRIMARY KEY;
ALTER TABLE form_exportdirectory_entryconfiguration ADD PRIMARY KEY (id_form, id_form_entry, iteration_number);