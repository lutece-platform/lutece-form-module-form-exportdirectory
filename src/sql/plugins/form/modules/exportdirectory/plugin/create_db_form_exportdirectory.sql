DROP TABLE IF EXISTS form_exportdirectory_formconfiguration;
DROP TABLE IF EXISTS form_exportdirectory_entryconfiguration;

--
-- table struture for form_exportdatabase_formconfiguration
--

CREATE TABLE form_exportdirectory_formconfiguration (
  id_form  		  INT 		   NOT NULL,
  id_directory    INT 		   NOT NULL,
  PRIMARY KEY(id_form)
);

--
-- table struture for form_exportdatabase_entryconfiguration
--

CREATE TABLE form_exportdirectory_entryconfiguration (
  id_form  		INT			 NOT NULL,
  id_form_entry      VARCHAR(255) NOT NULL,
  iteration_number   INT DEFAULT -1,
  id_directory_entry   VARCHAR(255) NOT NULL,
  PRIMARY KEY(id_form, id_form_entry, iteration_number)
);
