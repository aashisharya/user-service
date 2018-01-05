--  ************** to maintain user details **************

CREATE TABLE users_info (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  contact_no varchar(255) DEFAULT NULL,
  aternate_no varchar(255) DEFAULT NULL,
  email varchar(255) NOT NULL,
  first_name varchar(255) NOT NULL,
  gender varchar(6) DEFAULT NULL,
  last_name varchar(255) DEFAULT NULL,
  middle_name varchar(255) DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY UK_gd6haghxykyvlx54bq9jsy88a (email)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

CREATE TABLE roles (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  created_on datetime DEFAULT NULL,
  description varchar(255) DEFAULT NULL,
  modified_on datetime DEFAULT NULL,
  role varchar(15) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY UK_g50w4r0ru3g9uf6i6fr4kpro8 (role)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

INSERT INTO roles(role,description)
 	VALUES("SUPERADMIN","library application admin"),
 		("ADMIN","library admin"),
 		("AGENT","library teacher"),
 		("USER","library registered user");
					   
CREATE TABLE users_roles (
  user_id bigint(20) NOT NULL,
  role_id bigint(20) NOT NULL,
  KEY FK_k2mq1ee4ob6uw649wgaus1ate (role_id),
  KEY FK_1hjw31qvltj7v3wb5o31jsrd8 (user_id),
  CONSTRAINT FK_1hjw31qvltj7v3wb5o31jsrd8 FOREIGN KEY (user_id) REFERENCES users_info (id),
  CONSTRAINT FK_k2mq1ee4ob6uw649wgaus1ate FOREIGN KEY (role_id) REFERENCES roles (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

CREATE TABLE users_status (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  created_on datetime DEFAULT NULL,
  modified_on datetime DEFAULT NULL,
  status varchar(255) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY UK_o4mxpvg1e9ntnnk7cdwh6fotj (status)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

INSERT INTO users_status(status)
		VALUES("Active"),
			("InActive"),
			("Pending");
			
CREATE TABLE library_user (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  libraryId bigint(20) NOT NULL,
  user_id bigint(20) DEFAULT NULL,
  PRIMARY KEY (id),
  KEY FK_qmqek4vkmwsvldub48fm9ss0q (user_id),
  CONSTRAINT FK_qmqek4vkmwsvldub48fm9ss0q FOREIGN KEY (user_id) REFERENCES users_info (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;
			 	  
CREATE TABLE address (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  city varchar(255) DEFAULT NULL,
  country varchar(255) DEFAULT NULL,
  door_no varchar(255) DEFAULT NULL,
  address_line1 varchar(255) NOT NULL,
  address_line2 varchar(255) DEFAULT NULL,
  pin bigint(20) NOT NULL,
  state varchar(255) DEFAULT NULL,
  user_id bigint(20) DEFAULT NULL,
  PRIMARY KEY (id),
  KEY FK_7rod8a71yep5vxasb0ms3osbg (user_id),
  CONSTRAINT FK_7rod8a71yep5vxasb0ms3osbg FOREIGN KEY (user_id) REFERENCES users_info (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

CREATE TABLE registered_user (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  amount_due double DEFAULT NULL,
  amount_paid double DEFAULT NULL,
  registered_date datetime DEFAULT NULL,
  user_id bigint(20) NOT NULL,
  PRIMARY KEY (id),
  KEY FK_ith03jiuhc1v4btmupa3x96ka (user_id),
  CONSTRAINT FK_ith03jiuhc1v4btmupa3x96ka FOREIGN KEY (user_id) REFERENCES users_info (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

CREATE TABLE users_credential (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  email varchar(50) NOT NULL,
  password varchar(255) NOT NULL,
  status_id bigint(20) NOT NULL,
  user_id bigint(20) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY UK_pm7vqymu850t3yhhevjq72egp (user_id),
  UNIQUE KEY UK_6ugsc1g1umav353run3ic3pje (email),
  KEY FK_rixnv1ljc3umatf56eiopvvxf (status_id),
  CONSTRAINT FK_pm7vqymu850t3yhhevjq72egp FOREIGN KEY (user_id) REFERENCES users_info (id),
  CONSTRAINT FK_rixnv1ljc3umatf56eiopvvxf FOREIGN KEY (status_id) REFERENCES users_status (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

