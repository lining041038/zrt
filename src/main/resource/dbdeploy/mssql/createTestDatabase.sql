USE [master];

/****** Object:  Database [databasetotest]    Script Date: 03/21/2012 12:47:13 ******/
CREATE DATABASE [databasetotest] ON  PRIMARY 
( NAME = N'databasetotest', FILENAME = N'D:\databasetotest.mdf' , SIZE = 7168KB , MAXSIZE = UNLIMITED, FILEGROWTH = 1024KB )
 LOG ON 
( NAME = N'databasetotest_log', FILENAME = N'D:\databasetotest_log.ldf' , SIZE = 6912KB , MAXSIZE = 2048GB , FILEGROWTH = 10%);

ALTER DATABASE [databasetotest] SET COMPATIBILITY_LEVEL = 100;

IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [databasetotest].[dbo].[sp_fulltext_database] @action = 'enable'
end;

ALTER DATABASE [databasetotest] SET ANSI_NULL_DEFAULT OFF ;

ALTER DATABASE [databasetotest] SET ANSI_NULLS OFF ;

ALTER DATABASE [databasetotest] SET ANSI_PADDING OFF ;

ALTER DATABASE [databasetotest] SET ANSI_WARNINGS OFF ;

ALTER DATABASE [databasetotest] SET ARITHABORT OFF ;

ALTER DATABASE [databasetotest] SET AUTO_CLOSE OFF ;

ALTER DATABASE [databasetotest] SET AUTO_CREATE_STATISTICS ON ;

ALTER DATABASE [databasetotest] SET AUTO_SHRINK OFF ;

ALTER DATABASE [databasetotest] SET AUTO_UPDATE_STATISTICS ON ;

ALTER DATABASE [databasetotest] SET CURSOR_CLOSE_ON_COMMIT OFF ;

ALTER DATABASE [databasetotest] SET CURSOR_DEFAULT  GLOBAL ;

ALTER DATABASE [databasetotest] SET CONCAT_NULL_YIELDS_NULL OFF ;

ALTER DATABASE [databasetotest] SET NUMERIC_ROUNDABORT OFF ;

ALTER DATABASE [databasetotest] SET QUOTED_IDENTIFIER OFF ;

ALTER DATABASE [databasetotest] SET RECURSIVE_TRIGGERS OFF ;

ALTER DATABASE [databasetotest] SET  DISABLE_BROKER ;

ALTER DATABASE [databasetotest] SET AUTO_UPDATE_STATISTICS_ASYNC OFF ;

ALTER DATABASE [databasetotest] SET DATE_CORRELATION_OPTIMIZATION OFF ;

ALTER DATABASE [databasetotest] SET TRUSTWORTHY OFF ;

ALTER DATABASE [databasetotest] SET ALLOW_SNAPSHOT_ISOLATION OFF ;

ALTER DATABASE [databasetotest] SET PARAMETERIZATION SIMPLE ;

ALTER DATABASE [databasetotest] SET READ_COMMITTED_SNAPSHOT OFF ;

ALTER DATABASE [databasetotest] SET HONOR_BROKER_PRIORITY OFF ;

ALTER DATABASE [databasetotest] SET  READ_WRITE ;

ALTER DATABASE [databasetotest] SET RECOVERY FULL ;

ALTER DATABASE [databasetotest] SET  MULTI_USER ;

ALTER DATABASE [databasetotest] SET PAGE_VERIFY CHECKSUM  ;

ALTER DATABASE [databasetotest] SET DB_CHAINING OFF ;


