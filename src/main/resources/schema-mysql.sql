/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  japarejo
 * Created: 21-feb-2019
 */

UNLOCK TABLES;
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS `hibernate_sequences`;
DROP TABLE IF EXISTS `Confirmation`;
DROP TABLE IF EXISTS `Experiment`;
DROP TABLE IF EXISTS `SocialNetworkConfiguration`;
DROP TABLE IF EXISTS `SocialNetworkAccount`;
DROP TABLE IF EXISTS `WorkspaceTags`;
DROP TABLE IF EXISTS `Tag`;
DROP TABLE IF EXISTS `Workspace`;
DROP TABLE IF EXISTS `UserAccount_authorities`;
DROP TABLE IF EXISTS `Researcher`;
DROP TABLE IF EXISTS `UserAccount` ;
DROP TABLE IF EXISTS `UserConnection`;
SET FOREIGN_KEY_CHECKS = 1;


# hibernate_sequences
# ------------------------------------------------------------

CREATE TABLE `hibernate_sequences` (
  `sequence_name` varchar(255) DEFAULT NULL,
  `next_val` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# UserConnection
# ------------------------------------------------------------

CREATE Table `UserConnection` (`userId` varchar(255) not null,
    `providerId` varchar(255) not null,
    `providerUserId` varchar(255),
    `rank` int not null,
    `displayName` varchar(255),
    `profileUrl` varchar(512),
    `imageUrl` varchar(512),
    `accessToken` varchar(512) not null,
    `secret` varchar(512),
    `refreshToken` varchar(512),
    `expireTime` bigint,
    primary key (`userId`, `providerId`, `providerUserId`));

CREATE UNIQUE INDEX `UserConnectionRank` on `UserConnection`(`userId`, `providerId`, `rank`);


# UserAccount

CREATE TABLE `UserAccount` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# SocialNetworkAccount
# ------------------------------------------------------------

CREATE TABLE `SocialNetworkAccount` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `accessToken` varchar(255) DEFAULT NULL,
  `createDate` datetime DEFAULT NULL,
  `displayName` varchar(255) DEFAULT NULL,
  `expireTime` bigint(20) DEFAULT NULL,
  `imageUrl` varchar(255) DEFAULT NULL,
  `profileUrl` varchar(255) DEFAULT NULL,
  `providerId` varchar(255) DEFAULT NULL,
  `providerUserId` varchar(255) DEFAULT NULL,
  `rank` int(11) NOT NULL,
  `refreshToken` varchar(255) DEFAULT NULL,
  `secret` varchar(255) DEFAULT NULL,
  `userId` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `userId` (`userId`,`providerId`,`rank`),
  UNIQUE KEY `userId_2` (`userId`,`providerId`,`providerUserId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


# Researcher
# ------------------------------------------------------------

CREATE TABLE `Researcher` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `userAccount_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK3C2B9D5FFC733F2f37dafa8` (`userAccount_id`),
  CONSTRAINT `FK3C2B9D5FFC733F2f37dafa8` FOREIGN KEY (`userAccount_id`) REFERENCES `UserAccount` (`id`)
   	ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Confirmation
# ------------------------------------------------------------

CREATE TABLE `Confirmation` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `confirmationCode` varchar(255) DEFAULT NULL,
  `confirmationDate` datetime DEFAULT NULL,
  `registrationDate` datetime DEFAULT NULL,
  `researcher_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `researcher_id` (`researcher_id`),
  UNIQUE KEY `confirmationCode` (`confirmationCode`),
  KEY `FK86E9E05535D39501` (`researcher_id`),
  CONSTRAINT `FK86E9E05535D39501` FOREIGN KEY (`researcher_id`) REFERENCES `Researcher` (`id`)
   	ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Experiment
# ------------------------------------------------------------

CREATE TABLE `Experiment` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `experimentId` varchar(255) DEFAULT NULL,
  `experimentName` varchar(255) DEFAULT NULL,
  `publicExperiment` tinyint(1) NOT NULL,
  `owner_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK71BBB81D7956A1F6` (`owner_id`),
  CONSTRAINT `FK71BBB81D7956A1F6` FOREIGN KEY (`owner_id`) REFERENCES `Researcher` (`id`)
   	ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# SocialNetworkConfiguration
# ------------------------------------------------------------

CREATE TABLE `SocialNetworkConfiguration` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `notifyWhenExperimentExecutionFinished` tinyint(1) NOT NULL,
  `publishExistingExperimentMadePublic` tinyint(1) NOT NULL,
  `publishNewExperimentExecutionFinished` tinyint(1) NOT NULL,
  `publishNewPublicExperimentCreated` tinyint(1) NOT NULL,
  `publishNewPublicExperimentExecutionStarted` tinyint(1) NOT NULL,
  `service` varchar(255) DEFAULT NULL,
  `actor_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK3B9FB495B3D38B74` (`actor_id`),
  CONSTRAINT `FK3B9FB495B3D38B74` FOREIGN KEY (`actor_id`) REFERENCES `Researcher` (`id`)
   	ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


# Tag
# ------------------------------------------------------------

CREATE TABLE `Tag` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `name` varchar(30) CHARACTER SET utf8 NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;

# UserAccount_authorities
# ------------------------------------------------------------

CREATE TABLE `UserAccount_authorities` (
  `UserAccount_id` int(11) NOT NULL,
  `authority` varchar(255) DEFAULT NULL,
  KEY `FKA380F224FFC733F2` (`UserAccount_id`),
  CONSTRAINT `FKA380F224FFC733F2` FOREIGN KEY (`UserAccount_id`) REFERENCES `UserAccount` (`id`)
   	ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Workspace
# ------------------------------------------------------------

CREATE TABLE `Workspace` (
  `id` int(11) NOT NULL,
  `version` varchar(20)  NOT NULL,
  `owner_id` int(11) NOT NULL,
  `description` varchar(200) CHARACTER SET utf8 DEFAULT '""',
  `downloads` int(11) DEFAULT '0',
  `launches` int(11) DEFAULT '0',
  `lastMod` datetime DEFAULT NULL,
  `name` varchar(100)  NOT NULL DEFAULT '',
  `origin_id` int(11) DEFAULT NULL,
  `wsVersion` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `owner` (`owner_id`),
  KEY `origin` (`origin_id`),
  CONSTRAINT `origin` FOREIGN KEY (`origin_id`) REFERENCES `Workspace` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `owner` FOREIGN KEY (`owner_id`) REFERENCES `Researcher` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;

# WorkspaceTags
# ------------------------------------------------------------

CREATE TABLE `WorkspaceTags` (
  `id_ws` int(11) NOT NULL,
  `id_tag` int(11) NOT NULL,
  KEY `taggedWorkspaces` (`id_ws`),
  KEY `workspaceTags` (`id_tag`),
  CONSTRAINT `taggedWorkspaces` FOREIGN KEY (`id_ws`) REFERENCES `Workspace` (`id`),
  CONSTRAINT `workspaceTags` FOREIGN KEY (`id_tag`) REFERENCES `Tag` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;