package be.leerstad.Database;

import be.leerstad.Consumption;
import be.leerstad.helpers.DbaseConnection;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BeveragesDAOImpl implements BeveragesDAO {

    Logger logger = Logger.getLogger("dbase");



    @Override
    public List<Consumption> pricelijst() {
        Connection connection = DbaseConnection.getConnection();
        List<Consumption> lijst = new ArrayList<>();

        String SQL = "select * from beverages";

        try (
                PreparedStatement pStatement = connection.prepareStatement(SQL);
                ResultSet resultSet = pStatement.executeQuery()) {
            while (resultSet.next()) {
                int id = (resultSet.getInt("beverageID"));
                double prijs = (resultSet.getDouble("price"));
                String name = (resultSet.getString("beverageName"));
                lijst.add(new Consumption(id,name,prijs));
            }
        } catch (SQLException e) {
            logger.error("something went wrong with pricelist");
            throw new IllegalArgumentException("check sql");
        }
        logger.debug("Opvragen prijslijst");

        return lijst;

    }

    @Override
    public HashMap priceList() {
        Connection connection = DbaseConnection.getConnection();
        HashMap<Integer, Double> pricelist = new HashMap<Integer, Double>();

        List<Consumption> lijst = new ArrayList<>();


        String SQL = "select * from beverages";

        try (
                PreparedStatement pStatement = connection.prepareStatement(SQL);
                ResultSet resultSet = pStatement.executeQuery()) {
            while (resultSet.next()) {

                int id = (resultSet.getInt("beverageID"));
                double prijs = (resultSet.getDouble("price"));
                String name = (resultSet.getString("beverageName"));
                pricelist.put( id, prijs);

                lijst.add(new Consumption(id,name,prijs));
            }
        } catch (SQLException e) {
            logger.error("something went wrong with pricelist");
            throw new IllegalArgumentException("check sql");
        }
        logger.debug("Opvragen prijslijst");
        return pricelist;

    }



}
