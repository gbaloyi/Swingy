package gbaloyi.swingy.database;

import java.util.List;
import java.util.ArrayList;

import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import gbaloyi.swingy.model.hero.Crab;
import gbaloyi.swingy.model.hero.Hero;
import gbaloyi.swingy.model.hero.Octopus;
import gbaloyi.swingy.model.artifact.Helm;
import gbaloyi.swingy.model.hero.HeroEnum;
import gbaloyi.swingy.model.artifact.Armor;
import gbaloyi.swingy.model.hero.Cuttlefish;
import gbaloyi.swingy.model.artifact.Weapon;
import gbaloyi.swingy.controller.HeroFactory;
import gbaloyi.swingy.model.artifact.Artifact;
import gbaloyi.swingy.model.artifact.ArtifactEnum;



public class DatabaseHandler {

    private Statement statement;
    private Connection connection;
    private static DatabaseHandler databaseHandler;
    private byte[] byteArray;
    private ResultSet resultSet;
    private PreparedStatement preparedStatement;
    private ByteArrayInputStream byteArrayInputStream;

    
    public static synchronized DatabaseHandler getInstance() {
        return (databaseHandler == null) ? new DatabaseHandler() : databaseHandler;
    }

    private Connection databaseConnection() throws SQLException, ClassNotFoundException {
        String createTable = "CREATE TABLE IF NOT EXISTS heroes "
            + "(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, type TEXT, level INTEGER, "
            + "attack INTEGER, defense INTEGER, experience INTEGER, "
            + "hitpoints INTEGER, armor BLOB, helm BLOB, weapon BLOB)";
        String sqliteDriver = "org.sqlite.JDBC";
        String sqliteUrl = "jdbc:sqlite:swingy.db";

        Class.forName(sqliteDriver);
        connection = DriverManager.getConnection(sqliteUrl); 
        statement = connection.createStatement();

        statement.executeUpdate(createTable);
        if (statement != null) {
            statement.close();
        }
        return (connection);
    }

    private ByteArrayInputStream serialization(Hero hero, ArtifactEnum type) {
        try {
           
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
           
            switch (type) {
                case ARMOR:
                    objectOutputStream.writeObject(hero.getArmor());
                    break;
                case HELM:
                    objectOutputStream.writeObject(hero.getHelm());
                    break;
                case WEAPON:
                    objectOutputStream.writeObject(hero.getWeapon());
                    break;
                default:
                    return null;
            }
            byteArray = byteArrayOutputStream.toByteArray();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        byteArrayInputStream = new ByteArrayInputStream(byteArray);
        return (byteArrayInputStream);
    }

    private Object deserialization(ResultSet resultSet, String column)
    throws SQLException, IOException, ClassNotFoundException{
        byteArray = resultSet.getBytes(column);
        byteArrayInputStream = new ByteArrayInputStream(byteArray); 
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        return objectInputStream.readObject();
    }

    public void insertHero(Hero hero) throws SQLException, IOException, ClassNotFoundException {
        connection = this.databaseConnection();
        String insertStatement = "INSERT INTO heroes "
            +"(name, type, level, attack, defense, experience, hitpoints, armor, helm, weapon)"
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        preparedStatement = connection.prepareStatement(insertStatement);

        preparedStatement.setString(1, hero.getName());
        preparedStatement.setString(2, hero.getType());
        preparedStatement.setInt(3, hero.getLevel());
        preparedStatement.setInt(4, hero.getAttack());
        preparedStatement.setInt(5, hero.getDefense());
        preparedStatement.setInt(6, hero.getExperience());
        preparedStatement.setInt(7, hero.getHitPoints());
        preparedStatement.setBinaryStream(8, serialization(hero, ArtifactEnum.ARMOR), byteArray.length);
        preparedStatement.setBinaryStream(9, serialization(hero, ArtifactEnum.HELM), byteArray.length);
        preparedStatement.setBinaryStream(10, serialization(hero, ArtifactEnum.WEAPON), byteArray.length);
        preparedStatement.executeUpdate();
    
        connection.close();
        preparedStatement.close();
    }

   
    public void updateHero(Hero hero) throws SQLException, IOException, ClassNotFoundException {
        connection = this.databaseConnection();
        String updateStatement = "UPDATE heroes" 
        + " SET level = ?, attack = ?, defense = ?, experience = ?, hitpoints = ?, armor = ?, helm = ?, weapon = ? "
        + "WHERE name = ?";
        preparedStatement = connection.prepareStatement(updateStatement);

        preparedStatement.setInt(1, hero.getLevel());
        preparedStatement.setInt(2, hero.getAttack());
        preparedStatement.setInt(3, hero.getDefense());
        preparedStatement.setInt(4, hero.getExperience());
        preparedStatement.setInt(5, hero.getHitPoints());
        preparedStatement.setBinaryStream(6, serialization(hero, ArtifactEnum.ARMOR), byteArray.length);
        preparedStatement.setBinaryStream(7, serialization(hero, ArtifactEnum.HELM), byteArray.length);
        preparedStatement.setBinaryStream(8, serialization(hero, ArtifactEnum.WEAPON), byteArray.length);
        preparedStatement.setString(9, hero.getName());
        preparedStatement.executeUpdate();

        connection.close();
        preparedStatement.close();
    }

    public void deleteHero(String name) throws SQLException, ClassNotFoundException {
        connection = this.databaseConnection();
        String deleteStatement = "DELETE FROM heroes WHERE name = ?";
        preparedStatement = connection.prepareStatement(deleteStatement);

        preparedStatement.setString(1, name);
        preparedStatement.executeUpdate();

        connection.close();
        preparedStatement.close();
    }

    public Hero retrieveHeroData(String name) throws SQLException, ClassNotFoundException, IOException {
        Hero hero = null;
        connection = this.databaseConnection();
        String selectStatement = "SELECT * FROM heroes WHERE name = ?";
        preparedStatement = connection.prepareStatement(selectStatement);

        preparedStatement.setString(1, name);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            switch (resultSet.getString("type")) {
                case "Crab":
                    hero = (Hero)HeroFactory.newHero(name, HeroEnum.CRAB);
                    break;
                case "Cuttlefish":
                    hero = (Hero)HeroFactory.newHero(name, HeroEnum.CUTTLEFISH);
                    break;
                case "Octopus":
                    hero = (Hero)HeroFactory.newHero(name, HeroEnum.OCTOPUS);
                default:
                    break;
            }
       
            hero.setLevel(resultSet.getInt("level"));
            hero.setAttack(resultSet.getInt("attack"));
            hero.setDefense(resultSet.getInt("attack"));
            hero.setExperience(resultSet.getInt("experience"));
            hero.setHitPoints(resultSet.getInt("hitpoints"));
            hero.setArmor((Armor)deserialization(resultSet, "armor"));
            hero.setHelm((Helm)deserialization(resultSet, "helm"));
            hero.setWeapon((Weapon)deserialization(resultSet, "weapon"));
        }

        connection.close();
        preparedStatement.close();
        resultSet.close();
        return hero;
    }

 
    public List<Hero> retrieveDatabase() 
    throws IOException, SQLException, ClassNotFoundException {
        List<Hero> heros = new ArrayList<>();
        connection = this.databaseConnection();
        statement = connection.createStatement();
        resultSet = statement.executeQuery("SELECT * FROM heroes");

        while (resultSet.next()) {
            Hero hero = null;
            switch (resultSet.getString("type")) {
                case "Crab":
                    hero = new Crab();
                    break;
                case "Cuttlefish":
                    hero = new Cuttlefish();
                    break;
                case "Octopus":
                    hero = new Octopus();
                    break;
            }
            hero.setName(resultSet.getString("name"));
            hero.setType(resultSet.getString("type"));
            hero.setLevel(resultSet.getInt("level"));
            hero.setAttack(resultSet.getInt("attack"));
            hero.setDefense(resultSet.getInt("defense"));
            hero.setExperience(resultSet.getInt("experience"));
            hero.setHitPoints(resultSet.getInt("hitpoints"));
            hero.setArmor((Armor) deserialization(resultSet, "armor"));
            hero.setHelm((Helm) deserialization(resultSet, "helm"));
            hero.setWeapon((Weapon) deserialization(resultSet, "weapon"));
            heros.add(hero);
        }

        connection.close();
        statement.close();
        return heros;
    }

    public void retrieveAllHeroes() throws SQLException, ClassNotFoundException, IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String selectStatement = "SELECT * FROM heroes";

        connection = this.databaseConnection();
        resultSet = statement.executeQuery(selectStatement);
        while (resultSet.next()) {
            stringBuilder.append("Name: ").append(resultSet.getString("name")).append("\n")
                .append("Type: ").append(resultSet.getString("type")).append("\n")
                .append("Level: ").append(resultSet.getInt("level")).append("\n")
                .append("Experience: ").append(resultSet.getInt("experience")).append("\n")
                .append("Attack: ").append(resultSet.getInt("attack")).append("\n")
                .append("Defense: ").append(resultSet.getInt("defense")).append("\n")
                .append("Hit Points: ").append(resultSet.getInt("hitpoints")).append("\n")
                .append("Armor: ").append(((Artifact) deserialization(resultSet, "armor")).getName()).append("\n")
                .append("Helm: ").append(((Artifact) deserialization(resultSet, "helm")).getName()).append("\n")
                .append("Weapon: ").append(((Artifact) deserialization(resultSet, "weapon")).getName()).append("\n\n");
        }
        System.out.println(stringBuilder.toString());

        connection.close();
        resultSet.close();
    }

    public int numberOfHeroes() throws SQLException, ClassNotFoundException {
        int numberOfHeroes;
        String selectCount = "SELECT COUNT(*) FROM heroes";

        connection = this.databaseConnection();
        resultSet = statement.executeQuery(selectCount);
        numberOfHeroes = resultSet.getInt(1);

        connection.close();
        resultSet.close();
        return numberOfHeroes;
    }

    public boolean heroExists(String name) throws ClassNotFoundException, SQLException {
        int duplicates = 0;
        connection = this.databaseConnection();
        String selectStatement = "SELECT * FROM heroes WHERE NAME = '" + name +"'";
        resultSet = statement.executeQuery(selectStatement);

        while(resultSet.next()) {
            duplicates++;
        }
        return (duplicates > 0) ? true : false;
    }
}