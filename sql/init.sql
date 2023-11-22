-- MySQL dump 10.13  Distrib 8.0.33, for Win64 (x86_64)
--
-- Host: localhost    Database: course_work
-- ------------------------------------------------------
-- Server version	8.0.32

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `availability`
--

DROP TABLE IF EXISTS `availability`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `availability` (
  `ID_Availability` int unsigned NOT NULL AUTO_INCREMENT,
  `ID_Product` int unsigned NOT NULL,
  `ID_Warehouse` int unsigned NOT NULL,
  `SectionNumber` int unsigned NOT NULL,
  `StackNumber` int unsigned NOT NULL,
  `Count` double DEFAULT '0',
  `Price` double DEFAULT '0',
  `PackageType` varchar(30) NOT NULL,
  `PackageWidth` double DEFAULT NULL,
  `PackageHeight` double DEFAULT NULL,
  `PackageDepth` double DEFAULT NULL,
  PRIMARY KEY (`ID_Availability`),
  UNIQUE KEY `ID_Availability_UNIQUE` (`ID_Availability`),
  KEY `ID_Product_idx` (`ID_Product`),
  KEY `ID_Warehouse_idx` (`ID_Warehouse`),
  CONSTRAINT `ID_ProductA` FOREIGN KEY (`ID_Product`) REFERENCES `product` (`ID_Product`),
  CONSTRAINT `ID_WarehouseA` FOREIGN KEY (`ID_Warehouse`) REFERENCES `warehouse` (`ID_Warehouse`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=cp1251;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `availability`
--

LOCK TABLES `availability` WRITE;
/*!40000 ALTER TABLE `availability` DISABLE KEYS */;
INSERT INTO `availability` VALUES (1,1,1,1,1,50,4550,'Коробка',0.33,0.13,0.23),(2,2,1,2,1,25,749,'Коробка',0.19,0.03,0.06),(3,3,2,1,1,15,1329,'Коробка',0.1,0.1,0.1),(4,4,3,1,1,20,1973,'Коробка',0.23,0.23,0.17),(6,5,1,1,1,0,15000,'1',1,1,1),(7,5,4,1,1,32,150,'qwerty',1,2,3),(8,2,3,55,66,10,300,'11',22,33,44),(9,2,1,2,2,0,780,'Коробка',0.19,0.03,0.06);
/*!40000 ALTER TABLE `availability` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `availability_link`
--

DROP TABLE IF EXISTS `availability_link`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `availability_link` (
  `ID_Link` int unsigned NOT NULL AUTO_INCREMENT,
  `ID_Document` int unsigned NOT NULL,
  `ID_Availability` int unsigned NOT NULL,
  `Count` double NOT NULL,
  PRIMARY KEY (`ID_Link`),
  UNIQUE KEY `ID_Link_UNIQUE` (`ID_Link`),
  KEY `fk_document_has_availability_availability1_idx` (`ID_Availability`),
  KEY `fk_document_has_availability_document1_idx` (`ID_Document`),
  CONSTRAINT `ID_AvailabilityDHA` FOREIGN KEY (`ID_Availability`) REFERENCES `availability` (`ID_Availability`),
  CONSTRAINT `ID_DocumentDHA` FOREIGN KEY (`ID_Document`) REFERENCES `document` (`ID_Document`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=cp1251;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `availability_link`
--

LOCK TABLES `availability_link` WRITE;
/*!40000 ALTER TABLE `availability_link` DISABLE KEYS */;
INSERT INTO `availability_link` VALUES (5,8,9,-50),(6,8,2,-200),(7,9,2,-15);
/*!40000 ALTER TABLE `availability_link` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `document`
--

DROP TABLE IF EXISTS `document`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `document` (
  `ID_Document` int unsigned NOT NULL AUTO_INCREMENT,
  `Date` date NOT NULL,
  PRIMARY KEY (`ID_Document`),
  UNIQUE KEY `ID_Document_UNIQUE` (`ID_Document`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=cp1251;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `document`
--

LOCK TABLES `document` WRITE;
/*!40000 ALTER TABLE `document` DISABLE KEYS */;
INSERT INTO `document` VALUES (8,'2023-06-28'),(9,'2023-09-13');
/*!40000 ALTER TABLE `document` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `okei_code`
--

DROP TABLE IF EXISTS `okei_code`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `okei_code` (
  `ID_OKEI` int unsigned NOT NULL AUTO_INCREMENT,
  `Name` varchar(30) NOT NULL,
  PRIMARY KEY (`ID_OKEI`),
  UNIQUE KEY `ID_OKEI_UNIQUE` (`ID_OKEI`)
) ENGINE=InnoDB AUTO_INCREMENT=9992 DEFAULT CHARSET=cp1251;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `okei_code`
--

LOCK TABLES `okei_code` WRITE;
/*!40000 ALTER TABLE `okei_code` DISABLE KEYS */;
INSERT INTO `okei_code` VALUES (3,'Миллиметр'),(4,'Сантиметр'),(5,'Дециметр'),(6,'Метр'),(8,'Километр;^тысяча метров'),(9,'Мегаметр;^миллион метров'),(15,'Нанометр'),(18,'Погонный метр'),(19,'Тысяча погонных метров'),(20,'Условный метр'),(39,'Дюйм (25,4 мм)'),(41,'Фут (0,3048 м)'),(43,'Ярд (0,9144 м)'),(47,'Морская миля (1852 м)'),(48,'Тысяча условных метров'),(49,'Километр условных труб'),(50,'Квадратный миллиметр'),(51,'Квадратный сантиметр'),(53,'Квадратный дециметр'),(54,'Тысяча квадратных дециметров'),(55,'Квадратный метр'),(56,'Миллион квадратных дециметров'),(57,'Миллион квадратных метров'),(58,'Тысяча квадратных метров'),(59,'Гектар'),(60,'Тысяча гектаров'),(61,'Квадратный километр'),(62,'Условный квадратный метр'),(71,'Квадратный дюйм (645,16 мм2)'),(73,'Квадратный фут (0,092903 м2)'),(75,'Квадратный ярд (0,8361274 м2)'),(81,'Квадратный метр общей площади'),(84,'Квадратный метр жилой площади'),(109,'Ар (100 м2)'),(110,'Кубический миллиметр'),(112,'Литр;^кубический дециметр'),(113,'Кубический метр'),(114,'Тысяча кубических метров'),(115,'Миллиард кубических метров'),(116,'Декалитр'),(118,'Децилитр'),(119,'Тысяча декалитров'),(120,'Миллион декалитров'),(121,'Плотный кубический метр'),(122,'Гектолитр'),(123,'Условный кубический метр'),(126,'Мегалитр'),(128,'Тысяча полулитров'),(129,'Миллион полулитров'),(130,'Тысяча литров;^1000 литров'),(131,'Кубический дюйм (16387,1 мм3)'),(132,'Кубический фут (0,02831685 м3)'),(133,'Кубический ярд (0,764555 м3)'),(159,'Миллион кубических метров'),(160,'Гектограмм'),(161,'Миллиграмм'),(163,'Грамм'),(164,'Микрограмм'),(165,'Тысяча каратов метрических'),(166,'Килограмм'),(167,'Миллион каратов метрических'),(169,'Тысяча тонн'),(170,'Килотонна'),(171,'Миллион тонн'),(172,'Тонна условного топлива'),(173,'Сантиграмм'),(175,'Тысяча тонн условного топлива'),(176,'Миллион тонн условного топлива'),(178,'Тысяча тонн переработки'),(179,'Условная тонна'),(207,'Тысяча центнеров'),(212,'Ватт'),(214,'Киловатт'),(215,'Мегаватт;^тысяча киловатт'),(222,'Вольт'),(223,'Киловольт'),(226,'Вольт-ампер'),(227,'Киловольт-ампер'),(230,'Киловар'),(231,'Метр в час'),(232,'Килокалория'),(233,'Гигакалория'),(234,'Тысяча гигакалорий'),(235,'Миллион гигакалорий'),(236,'Калория в час'),(237,'Килокалория в час'),(238,'Гигакалория в час'),(239,'Тысяча гигакалорий в час'),(241,'Миллион ампер-часов'),(242,'Миллион киловольт-ампер'),(243,'Ватт-час'),(245,'Киловатт-час'),(248,'Киловольт-ампер реактивный'),(249,'Миллиард киловатт-часов'),(251,'Лошадиная сила'),(252,'Тысяча лошадиных сил'),(253,'Миллион лошадиных сил'),(254,'Бит'),(255,'Байт'),(256,'Килобайт'),(257,'Мегабайт'),(258,'Бод'),(260,'Ампер'),(263,'Ампер-час (3,6 кКл)'),(264,'Тысяча ампер-часов'),(270,'Кулон'),(271,'Джоуль'),(273,'Килоджоуль'),(274,'Ом'),(276,'Грей'),(277,'Микрогрей'),(278,'Миллигрей'),(279,'Килогрей'),(280,'Градус Цельсия'),(281,'Градус Фаренгейта'),(282,'Кандела'),(283,'Люкс'),(284,'Люмен'),(287,'Генри'),(288,'Кельвин'),(289,'Ньютон'),(290,'Герц'),(291,'Килогерц'),(292,'Мегагерц'),(293,'Гигагерц'),(294,'Паскаль'),(295,'Терагерц'),(296,'Сименс'),(297,'Килопаскаль'),(298,'Мегапаскаль'),(302,'Гигабеккерель'),(303,'Килобеккерель'),(304,'Милликюри'),(305,'Кюри'),(306,'Грамм делящихся изотопов'),(307,'Мегабеккерель'),(308,'Миллибар'),(309,'Бар'),(310,'Гектобар'),(312,'Килобар'),(313,'Тесла'),(314,'Фарад'),(316,'Киллограмм на кубический метр'),(318,'Зиверт'),(319,'Микрозиверт'),(320,'Моль'),(321,'Миллизиверт'),(323,'Беккерель'),(324,'Вебер'),(327,'Узел (миля/ч)'),(328,'Метр в секунду'),(330,'Оборот в секунду'),(331,'Оборот в минуту'),(333,'Километр в час'),(335,'Метр на секунду в квадрате'),(337,'Миллиметр водяного столба'),(338,'Миллиметр ртутного столба'),(339,'Сантиметр водяного столба'),(348,'Фемтосекунда'),(349,'Кулон на килограмм'),(350,'Пикосекунда'),(351,'Наносекунда'),(352,'Микросекунда'),(353,'Миллисекунда'),(354,'Секунда'),(355,'Минута'),(356,'Час'),(359,'Сутки'),(360,'Неделя'),(361,'Декада'),(362,'Месяц'),(364,'Квартал'),(365,'Полугодие'),(366,'Год'),(368,'Десятилетие'),(383,'Рубль'),(384,'Тысяча рублей'),(385,'Миллион рублей'),(386,'Миллиард рублей'),(387,'Триллион рублей'),(388,'Квадрильон рублей'),(414,'Пассажиро-километр'),(423,'Тысяча пассажиро-километров'),(424,'Миллион пассажиро-километров'),(426,'Пар грузовых поездов в сутки'),(427,'Пассажиропоток'),(449,'Тонно-километр'),(450,'Тысяча тонно-километров'),(451,'Миллион тонно-километров'),(479,'Тысяча наборов'),(499,'Килограмм в секунду'),(508,'Тысяча метров кубических в час'),(509,'Километр в сутки'),(510,'Грамм на киловатт-час'),(511,'Килограмм на гигакалорию'),(512,'Тонно-номер'),(513,'Автотонна'),(514,'Тонна тяги'),(515,'Дедвейт-тонна'),(516,'Тонна-танид'),(521,'Человек на квадратный метр'),(522,'Человек на квадратный километр'),(533,'Тонна пара в час'),(534,'Тонна в час'),(535,'Тонна в сутки'),(536,'Тонна в смену'),(537,'Тысяча тонн в сезон'),(538,'Тысяча тонн в год'),(539,'Человеко-час'),(540,'Человеко-день'),(541,'Тысяча человеко-дней'),(542,'Тысяча человеко-часов'),(543,'Тысяча условных банок в смену'),(544,'Миллион единиц в год'),(545,'Посещение в смену'),(546,'Тысяча посещений в смену'),(547,'Пара в смену'),(548,'Тысяча пар в смену'),(550,'Миллион тонн в год'),(552,'Тонна переработки в сутки'),(554,'Центнер переработки в сутки'),(556,'Тысяча голов в год'),(557,'Миллион голов в год'),(558,'Тысяча птицемест'),(559,'Тысяча кур-несушек'),(560,'Минимальная заработная плата'),(561,'Тысяча тонн пара в час'),(562,'Тысяча прядильных веретен'),(563,'Тысяча прядильных мест'),(596,'Кубический метр в секунду'),(598,'Кубический метр в час'),(616,'Бобина'),(625,'Лист'),(626,'Сто листов'),(639,'Доза'),(640,'Тысяча доз'),(641,'Дюжина (12 шт.)'),(642,'Единица'),(643,'Тысяча единиц'),(644,'Миллион единиц'),(657,'Изделие'),(661,'Канал'),(673,'Тысяча комплектов'),(683,'Сто ящиков'),(698,'Место'),(699,'Тысяча мест'),(704,'Набор'),(709,'Тысяча номеров'),(715,'Пара (2 шт.)'),(724,'Тысяча гектаров порций'),(728,'Пачка'),(729,'Тысяча пачек'),(730,'Два десятка'),(732,'Десять пар'),(733,'Дюжина пар'),(734,'Посылка'),(735,'Часть'),(736,'Рулон'),(737,'Дюжина рулонов'),(740,'Дюжина штук'),(744,'Процент'),(745,'Элемент'),(746,'Промилле (0,1 процента)'),(747,'Базисный пункт'),(751,'Тысяча рулонов'),(761,'Тысяча станов'),(762,'Станция'),(775,'Тысяча тюбиков'),(776,'Тысяча условных тубов'),(778,'Упаковка'),(779,'Миллион упаковок'),(780,'Дюжина упаковок'),(781,'Сто упаковок'),(782,'Тысяча упаковок'),(792,'Человек'),(793,'Тысяча человек'),(794,'Миллион человек'),(796,'Штука'),(797,'Сто штук'),(798,'Тысяча штук'),(799,'Миллион штук'),(800,'Миллиард штук'),(802,'Квинтильон штук (Европа)'),(808,'Миллион экземпляров'),(810,'Ячейка'),(812,'Ящик'),(820,'Крепость спирта по массе'),(821,'Крепость спирта по объему'),(831,'Литр чистого (100 %) спирта'),(836,'Голова'),(837,'Тысяча пар'),(838,'Миллион пар'),(839,'Комплект'),(840,'Секция'),(841,'Килограмм пероксида водорода'),(847,'Тонна 90 %-го сухого вещества'),(852,'Килограмм оксида калия'),(859,'Килограмм гидроксида калия'),(861,'Килограмм азота'),(863,'Килограмм гидроксида натрия'),(865,'Килограмм пятиокиси фосфора'),(867,'Килограмм урана'),(868,'Бутылка'),(869,'Тысяча бутылок'),(870,'Ампула'),(871,'Тысяча ампул'),(872,'Флакон'),(873,'Тысяча флаконов'),(874,'Тысяча тубов'),(875,'Тысяча коробок'),(876,'Условная единица'),(877,'Тысяча условных единиц'),(878,'Миллион условных единиц'),(879,'Условная штука'),(880,'Тысяча условных штук'),(881,'Условная банка'),(882,'Тысяча условных банок'),(883,'Миллион условных банок'),(884,'Условный кусок'),(885,'Тысяча условных кусков'),(886,'Миллион условных кусков'),(887,'Условный ящик'),(888,'Тысяча условных ящиков'),(889,'Условная катушка'),(890,'Тысяча условных катушек'),(891,'Условная плитка'),(892,'Тысяча условных плиток'),(893,'Условный кирпич'),(894,'Тысяча условных кирпичей'),(895,'Миллион условных кирпичей'),(896,'Семья'),(897,'Тысяча семей'),(898,'Миллион семей'),(899,'Домохозяйство'),(900,'Тысяча домохозяйств'),(901,'Миллион домохозяйств'),(902,'Ученическое место'),(903,'Тысяча ученических мест'),(904,'Рабочее место'),(905,'Тысяча рабочих мест'),(906,'Посадочное место'),(907,'Тысяча посадочных мест'),(908,'Номер'),(909,'Квартира'),(910,'Тысяча квартир'),(911,'Койка'),(912,'Тысяча коек'),(913,'Том книжного фонда'),(914,'Тысяча томов книжного фонда'),(915,'Условный ремонт'),(916,'Условный ремонт в год'),(917,'Смена'),(918,'Лист авторский'),(920,'Лист печатный'),(921,'Лист учетно-издательский'),(922,'Знак'),(923,'Слово'),(924,'Символ'),(925,'Условная труба'),(930,'Тысяча пластин'),(937,'Миллион доз'),(949,'Миллион листов-оттисков'),(950,'Вагоно (машино)-день'),(951,'Тысяча вагоно (машино)-часов'),(953,'Тысяча место-километров'),(954,'Вагоно-сутки'),(955,'Тысяча поездо-часов'),(956,'Тысяча поездо-километров'),(957,'Тысяча тонно-миль'),(958,'Тысяча пассажиро-миль'),(959,'Автомобиле-день'),(960,'Тысяча автомобиле-тонно-дней'),(961,'Тысяча автомобиле-часов'),(962,'Тысяча автомобиле-место-дней'),(963,'Приведенный час'),(964,'Самолето-километр'),(965,'Тысяча километров'),(966,'Тысяча тоннаже-рейсов'),(967,'Миллион тонно-миль'),(968,'Миллион пассажиро-миль'),(969,'Миллион тоннаже-миль'),(970,'Миллион пассажиро-место-миль'),(971,'Кормо-день'),(972,'Центнер кормовых единиц'),(973,'Тысяча автомобиле-километров'),(974,'Тысяча тоннаже-суток'),(975,'Суго-сутки'),(977,'Канало-километр'),(978,'Канало-концы'),(979,'Тысяча экземпляров'),(980,'Тысяча долларов'),(981,'Тысяча тонн кормовых единиц'),(982,'Миллион тонн кормовых единиц'),(983,'Судо-сутки'),(984,'Центнеров с гектара'),(985,'Тысяча голов'),(986,'Тысяча краско-оттисков'),(987,'Миллион краско-оттисков'),(988,'Миллион условных плиток'),(989,'Человек в час'),(990,'Пассажиров в час'),(991,'Пассажиро-миля'),(2311,'Грей в секунду'),(2312,'Грей в минуту'),(2313,'Грей в час'),(2314,'Микрогрей в секунду'),(2315,'Микрогрей в час'),(2316,'Миллигрей в час'),(2351,'Зиверт в час'),(2352,'Микрозиверт в секунду'),(2353,'Микрозиверт в час'),(2354,'Миллизиверт в час'),(2355,'Градус (плоского угла)'),(2356,'Минута (плоского угла)'),(2357,'Секунда (плоского угла)'),(2541,'Бит в секунду'),(2543,'Килобит в секунду'),(2545,'Мегабит в секунду'),(2547,'Гигабит в секунду'),(2551,'Байт в секунду'),(2552,'Гигабайт в секунду'),(2553,'Гигабайт'),(2554,'Терабайт'),(2555,'Петабайт'),(2556,'Эксабайт'),(2557,'Зеттабайт'),(2558,'Йоттабайт'),(2561,'Килобайт в секунду'),(2571,'Мегабайт в секунду'),(2581,'Эрланг'),(2931,'Гигагерц'),(3135,'Децибел'),(3181,'Человеко-зиверт'),(3231,'Беккерель на метр кубический'),(3831,'Рубль тонна'),(3841,'Тысяча рублей на человека'),(5401,'Дето-день'),(5423,'Человек в год'),(5451,'Посещение'),(5562,'Тысяча гнезд'),(6421,'Единиц в год'),(6422,'Вызов'),(6423,'Посевная единица'),(6424,'Штамм'),(7923,'Абонент'),(8361,'Особь'),(8751,'Коробка'),(9061,'Миллион гектаров'),(9062,'Миллиард гектаров'),(9111,'Койко-день'),(9113,'Пациенто-день'),(9245,'Запись'),(9246,'Документ'),(9491,'Лист-оттиск'),(9501,'Вагоно (машино)-час'),(9557,'Миллион голов'),(9641,'Летный час'),(9642,'Балл'),(9802,'Миллион долларов'),(9803,'Миллиард долларов'),(9805,'Доллар за тонну'),(9822,'Миллион евро'),(9823,'Миллиард евро'),(9930,'Антитоксическая единица'),(9931,'Тысяча антитоксических единиц'),(9940,'Антитрипсиновая единица'),(9941,'Тысяча антитрипсиновых единиц'),(9950,'Индекс Реактивности'),(9960,'Килобеккерель на миллилитр'),(9961,'Мегабеккерель на миллилитр'),(9983,'Протеолитическая единица'),(9985,'Микрограмм на миллилитр'),(9986,'Микрограмм в сутки'),(9987,'Микрограмм в час'),(9988,'Микрограмм на дозу'),(9990,'Миллимоль на миллилитр'),(9991,'Миллимоль на литр');
/*!40000 ALTER TABLE `okei_code` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `ID_Order` int unsigned NOT NULL AUTO_INCREMENT,
  `ID_Document` int unsigned DEFAULT NULL,
  `ID_Supplier` int unsigned NOT NULL,
  `ID_Warehouse` int unsigned NOT NULL,
  `Price` double NOT NULL,
  `Count` double NOT NULL,
  `Completed` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`ID_Order`),
  UNIQUE KEY `ID_Order_UNIQUE` (`ID_Order`),
  KEY `fk_document_has_supplier_supplier1_idx` (`ID_Supplier`),
  KEY `fk_document_has_supplier_document1_idx` (`ID_Document`),
  KEY `fk_order_from_warehouse_warehouse1_idx` (`ID_Warehouse`),
  CONSTRAINT `fk_order_from_warehouse_warehouse1` FOREIGN KEY (`ID_Warehouse`) REFERENCES `warehouse` (`ID_Warehouse`),
  CONSTRAINT `ID_DocumentDHS` FOREIGN KEY (`ID_Document`) REFERENCES `document` (`ID_Document`),
  CONSTRAINT `ID_SupplierDHS` FOREIGN KEY (`ID_Supplier`) REFERENCES `supplier` (`ID_Supplier`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=cp1251;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (8,9,1,1,2,15,6),(9,8,1,1,2,250,6),(12,NULL,2,3,500,13,1),(13,NULL,2,1,1,123,2),(14,NULL,2,1,2,0,2);
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `person`
--

DROP TABLE IF EXISTS `person`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `person` (
  `ID_Person` int unsigned NOT NULL AUTO_INCREMENT,
  `ID_Post` int unsigned NOT NULL,
  `ID_Supplier` int unsigned DEFAULT NULL,
  `FirstName` varchar(45) NOT NULL,
  `SecondName` varchar(45) NOT NULL,
  `ThirdName` varchar(45) DEFAULT NULL,
  `Login` varchar(64) NOT NULL,
  `Password` varchar(512) NOT NULL,
  PRIMARY KEY (`ID_Person`),
  UNIQUE KEY `ID_Person_UNIQUE` (`ID_Person`),
  KEY `fk_person_roles1_idx` (`ID_Post`),
  KEY `fk_person_supplier1_idx` (`ID_Supplier`),
  CONSTRAINT `fk_person_supplier1` FOREIGN KEY (`ID_Supplier`) REFERENCES `supplier` (`ID_Supplier`),
  CONSTRAINT `ID_PostP` FOREIGN KEY (`ID_Post`) REFERENCES `post` (`ID_Post`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=cp1251;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `person`
--

LOCK TABLES `person` WRITE;
/*!40000 ALTER TABLE `person` DISABLE KEYS */;
INSERT INTO `person` VALUES (1,3,NULL,'Администратор','Главный','','admin','8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918'),(2,2,NULL,'Всеволод','Петров','Дмитриевич','petrov123','a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3'),(3,1,2,'Николай','Комаров','Григорьевич','komarov234','114bd151f8fb0c58642d2170da4ae7d7c57977260ac2cc8905306cab6b2acabc'),(4,2,NULL,'Sergay','Smirnov','','smirnov321','8d23cf6c86e834a7aa6eded54c26ce2bb2e74903538c61bdd5d2197997ab2f72'),(5,1,1,'Александр','Пушкин','Сергеевич','pushkin111','f6e0a1e2ac41945a9aa7ff8a8aaa0cebc12a3bcc981a929ad5cf810a090e11ae'),(6,2,NULL,'Кладовщик','Кладовщик','Кладовщикович','klad','571d78c150670b0b7dd713e02776b41b2e45284e1b00bef4acfb72818ac67fe8'),(7,1,NULL,'Поставщик','Поставщик','Поставщикович','post','72231043bc1807e6f740b235eb7511ecb33255a6a375435631196de8a9750d4b');
/*!40000 ALTER TABLE `person` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `post`
--

DROP TABLE IF EXISTS `post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `post` (
  `ID_Post` int unsigned NOT NULL AUTO_INCREMENT,
  `Name` varchar(60) NOT NULL,
  PRIMARY KEY (`ID_Post`),
  UNIQUE KEY `idroles_UNIQUE` (`ID_Post`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=cp1251;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post`
--

LOCK TABLES `post` WRITE;
/*!40000 ALTER TABLE `post` DISABLE KEYS */;
INSERT INTO `post` VALUES (1,'Поставщик'),(2,'Кладовщик'),(3,'Администратор');
/*!40000 ALTER TABLE `post` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product` (
  `ID_Product` int unsigned NOT NULL AUTO_INCREMENT,
  `Name` varchar(128) NOT NULL,
  `Description` varchar(512) DEFAULT NULL,
  `Nomenclature` varchar(30) NOT NULL,
  `Article` varchar(30) DEFAULT NULL,
  `ID_OKEI` int unsigned NOT NULL,
  `Mass` double NOT NULL,
  `PercentNDS` int unsigned NOT NULL,
  `StorageConditions` varchar(1024) DEFAULT NULL,
  `Deleted` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`ID_Product`),
  UNIQUE KEY `ID_Product_UNIQUE` (`ID_Product`),
  KEY `ID_OKEI_idx` (`ID_OKEI`),
  CONSTRAINT `ID_OkeiP` FOREIGN KEY (`ID_OKEI`) REFERENCES `okei_code` (`ID_OKEI`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=cp1251;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES (1,'New balance 530','ОБРАТИТЕ ВНИМАНИЕ! МАЛОМЕРЯТ НА 1 РАЗМЕР. Женские кроссовки – это идеальный выбор обуви для многих сезонов года, включая весну и лето. Они являются универсальными, их можно использовать не только для бега или фитнеса, но и в повседневной жизни. Эти кеды, хоть и были изначально ','6404110000','154810200',796,0.35,20,'',0),(2,'Штатив для телефона монопод','Идеальный аксессуар для съемки фото и видео на вашем смартфоне. Наш штатив для телефона - это то, что Вам нужно! Этот многофункциональный аксессуар может использоваться для любых условий съемки. Монопод для телефона имеет удобный держатель, который подходит для большинства смартфонов. Он легко регулируется для наилучшего угла съемки и может использоваться, как настольный или напольный. Селфи палка также идеально подходит для профессиональных фотографов и блогеров, которые хотят получить высококачественные ф','9006910000','155603227',796,0.15,20,'',0),(3,'Аккумулятор Nikon EN-EL14','Аккумулятор Nikon EN-EL14 емкостью 1030 mAh, позволяющей делать до 3500 кадров на одном полном заряде при работе с фотоаппаратами Nikon серии D , Coolpix и др. Сохраняет до 90% своего ресурса после 1000 циклов заряда/разряда при регулярном использовании в течение 10 лет.- Используйте все энергоемкие функции вашего современного фотоаппарата: полного заряда аккумуляторной батареи Nikon EN-EL14 достаточно для создания 3500 фотографий- Возможность подзарядки аккумулятора в любое время и при любом остаточном зар','8507600000','61040747',796,0.1,20,'',0),(4,'Электрический чайник PWK 1753CGL','Запатентованная технология WATERWAY PRO удобный залив воды без открытия крышки. Инновационная сталь на дне чайника. Максимально легкая очистка. Английский контроллер STRIX: 15 000 циклов кипячения, что равно 10 годам интенсивного использования. Корпус из высококачественного термостойкого стекла. Инструментальная проверка однородности и чистоты стекла. Яркая внутренняя подсветка. Двусторонняя шкала контроля уровня воды. Фильтр для очистки воды. Автоматический и ручной выключатель. Вращающийся корпус на 360 г','8516797000','119911966',796,1.51,20,'',0),(5,'Ганджубас','Не знаете чем отвлечься от городской суеты? Попробуйте нашу революционную разработку в сфере развлечений! Эффект дополненной реальности гарантирован.','323231231','00000001',867,3,11,'',0);
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `supplier`
--

DROP TABLE IF EXISTS `supplier`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `supplier` (
  `ID_Supplier` int unsigned NOT NULL AUTO_INCREMENT,
  `ID_Product` int unsigned NOT NULL,
  `Name` varchar(128) NOT NULL,
  `City` varchar(32) NOT NULL,
  `AddressFull` varchar(128) DEFAULT NULL,
  `OKPO` int unsigned NOT NULL,
  `PaymentAccount` varchar(20) NOT NULL,
  `Phone` varchar(20) NOT NULL,
  `ZipCode` varchar(6) NOT NULL,
  `BankInfo` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`ID_Supplier`),
  UNIQUE KEY `ID_Supplier_UNIQUE` (`ID_Supplier`),
  KEY `ID_Product_idx` (`ID_Product`),
  CONSTRAINT `ID_ProductS` FOREIGN KEY (`ID_Product`) REFERENCES `product` (`ID_Product`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=cp1251;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `supplier`
--

LOCK TABLES `supplier` WRITE;
/*!40000 ALTER TABLE `supplier` DISABLE KEYS */;
INSERT INTO `supplier` VALUES (1,5,'Шишки-Гашишки Ltd & Co','г. Упырь','г. Упырь, ул. Наркоманская, д. 666',2842708,'12345678901234567890','8 (800) 555-35-35','152000','Bitcoin кошелёк'),(2,2,'111111','222222','333333',444444,'555555','666666','777777','888888');
/*!40000 ALTER TABLE `supplier` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `warehouse`
--

DROP TABLE IF EXISTS `warehouse`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `warehouse` (
  `ID_Warehouse` int unsigned NOT NULL AUTO_INCREMENT,
  `Name` varchar(128) NOT NULL,
  `City` varchar(64) NOT NULL,
  `Street` varchar(64) NOT NULL,
  `BuildingNumber` int unsigned NOT NULL,
  `BuildingLiteral` char(1) DEFAULT NULL,
  PRIMARY KEY (`ID_Warehouse`),
  UNIQUE KEY `ID_Warehouse_UNIQUE` (`ID_Warehouse`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=cp1251;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `warehouse`
--

LOCK TABLES `warehouse` WRITE;
/*!40000 ALTER TABLE `warehouse` DISABLE KEYS */;
INSERT INTO `warehouse` VALUES (1,'Санкт_Петербург_РФЦ','Санкт-Петербург','п. Петро-Славянка, ул. Софийская',118,''),(2,'ХОРУГВИНО_РФЦ','Солнечногорск','деревня Хоругвино',32,''),(3,'Новая_Рига_РФЦ','Москва','п. Ильинское-Усово, Новорижское шоссе',1,''),(4,'Екатеринбург_РФЦ_НОВЫЙ','Екатеринбург','территория Логопарк Кольцовский',15,'');
/*!40000 ALTER TABLE `warehouse` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-11-22 11:12:29
