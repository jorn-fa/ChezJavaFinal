package be.leerstad.Database;

import be.leerstad.Ober;
import be.leerstad.helpers.DbaseConnection;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;


public final class WaiterDAOImpl implements WaiterDAO {

    private Connection connection = DbaseConnection.getConnection();
    private Logger logger = Logger.getLogger("dbase");


    @Override
    public HashMap waiterList() {
        HashMap<Integer, String> waiters = new HashMap<>();

        String SQL = "select waiterid,firstname,lastname from waiters";

        try (
                PreparedStatement pStatement = connection.prepareStatement(SQL);
                ResultSet resultSet = pStatement.executeQuery()) {
            while (resultSet.next()) {

                int id = (resultSet.getInt(1));
                String first = resultSet.getString(2);
                String last =  resultSet.getString(3);
                waiters.put( id, first + " " + last);
            }
        } catch (SQLException e) {
            logger.error("something went wrong with waiterList");
            throw new IllegalArgumentException("check sql");
        }
        logger.debug("Opvragen waiter lijst");


        return waiters;
    }

    @Override
    public Ober compareOber(Ober ober) {

        logger.debug("opvragen ober met gegevens : " + ober.getVoornaam() + " " + ober.getNaam());

        String SQL = String.format("select * from waiters where firstname = '%s' and lastname = '%s' ",ober.getVoornaam(),ober.getNaam());

        String voornaam="";
        String naam="";
        int oberID=0;

        try ( PreparedStatement pStatement = connection.prepareStatement(SQL);
              ResultSet resultSet = pStatement.executeQuery()) {
            while (resultSet.next()) {
                oberID = resultSet.getInt("waiterID");
                voornaam= resultSet.getString("firstName");
                naam = resultSet.getString("lastName");
            }
        } catch (SQLException e) {
            logger.debug(e.getMessage());
        }

        if (voornaam.equalsIgnoreCase(ober.getVoornaam()) && naam.equalsIgnoreCase(ober.getNaam()))
        {
            logger.debug("Logged in waiter "+ ober.getVoornaam() + " " + ober.getNaam());
            return new Ober(oberID, naam, voornaam);
        }
        return null;
    }

}
