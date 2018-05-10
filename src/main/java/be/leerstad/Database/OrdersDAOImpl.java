package be.leerstad.Database;

import be.leerstad.Cafe;
import be.leerstad.Consumption;
import be.leerstad.helpers.DbaseConnection;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class OrdersDAOImpl implements Serializable , OrdersDAO {

    private Logger logger = Logger.getLogger("dbase");

    private Connection connection = DbaseConnection.getConnection();




    @Override
    public void writeOrder(List<Consumption> lijst) {

        int orderNummer = getOrdernummer() + 1; //opvragen max nummer +1
        Date today = Date.valueOf(LocalDate.now());
        String SQL = "insert into orders(orderNumber, beverageID, qty, date, waiterID) values (?,?,?,?,?)";

        try (PreparedStatement ps = connection.prepareStatement(SQL)) {

            for (Consumption aLijst : lijst) {
                ps.setInt(1, orderNummer);
                ps.setInt(2, aLijst.getBeverageId());
                ps.setInt(3, aLijst.getAantal());
                ps.setDate(4, today);
                ps.setInt(5, aLijst.getWaiterID());
                ps.executeUpdate();
            }


        } catch (SQLException e) {
            logger.error("SqlExeption @ writeOrder");
            throw (new IllegalArgumentException("Sql statement klopt niet"));

        }
        }


    @Override
    public int getOrdernummer() {

        int nummer = 0;

        try (
                PreparedStatement pStatement = connection.prepareStatement("select max(ID) from orders");
                ResultSet resultSet = pStatement.executeQuery()) {
            while (resultSet.next()) {
                nummer = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            logger.error("something went wrong with getOrdernummer");
            throw new IllegalArgumentException("check sql");
        }
        logger.debug("Opvragen hoogste nummer = " + nummer);
        return nummer;

    }


    @Override
    //geeft volledige lijst op uit orders tabel
    public List<Consumption> orderList() {
        Connection connection = DbaseConnection.getConnection();

        List<Consumption> lijst = new ArrayList<>();

        String SQL = "select waiterID, beverageID , qty from orders";

        try (
                PreparedStatement pStatement = connection.prepareStatement(SQL);
                ResultSet resultSet = pStatement.executeQuery()) {

            while (resultSet.next()) {
                int waiterID = (resultSet.getInt(1));
                int beverageId = (resultSet.getInt(2));
                int qty = (resultSet.getInt(3));
                Consumption consumption = new Consumption(beverageId,qty,waiterID) ;
                lijst.add(consumption);
            }
        } catch (SQLException e) {
            logger.error("something went wrong with Orderlist");
            throw new IllegalArgumentException("check sql");
        }
        logger.debug("Opvragen orderlist");


        return lijst;
    }

    @Override
    public Set<Date> dateList() {
        Connection connection = DbaseConnection.getConnection();

        Set<Date> lijst = new HashSet<>();

        String SQL = "select date from orders where waiterid=" + "\"" + Cafe.getInstance().getOber().getID() + "\"" ;

        try (
                PreparedStatement pStatement = connection.prepareStatement(SQL);
                ResultSet resultSet = pStatement.executeQuery()) {

            while (resultSet.next()) {
                Date datum = (resultSet.getDate(1));
                lijst.add(datum);

            }
        } catch (SQLException e) {
            logger.error("something went wrong with Orderlist");
            throw new IllegalArgumentException("check sql");
        }
        logger.debug("Opvragen orderlist");

        return lijst;
    }
}