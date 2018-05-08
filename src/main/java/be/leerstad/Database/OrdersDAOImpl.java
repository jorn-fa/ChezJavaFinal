package be.leerstad.Database;

import be.leerstad.Cafe;
import be.leerstad.Consumption;
import be.leerstad.Ober;
import be.leerstad.Order;
import be.leerstad.helpers.DbaseConnection;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OrdersDAOImpl implements Serializable , OrdersDAO {

    private Logger logger = Logger.getLogger("dbase");

    private Connection connection = DbaseConnection.getConnection();

    private String SQL = "insert into orders(orderNumber, beverageID, qty, date, waiterID) values (?,?,?,?,?)";


    @Override
    public void writeOrder(List<Consumption> lijst) {

        int orderNummer = getOrdernummer() + 1; //opvragen max nummer +1
        Date today = Date.valueOf(LocalDate.now());

        try (PreparedStatement ps = connection.prepareStatement(SQL)) {

            for (int i = 0;i<lijst.size();i++)
            {
                ps.setInt(1, orderNummer);
                ps.setInt(2, lijst.get(i).getBeverageId());
                ps.setInt(3, lijst.get(i).getAantal());
                ps.setDate(4, today);
                ps.setInt(5, lijst.get(i).getWaiterID());
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

    public List<Consumption> getBetween(LocalDate start, LocalDate end, Ober ober) {

        ArrayList<Consumption> test = new ArrayList<>();

        Date startDatum = Date.valueOf(start);
        Date eindDatum = Date.valueOf(end);


        String sql = "select * from orders where date between \"" + startDatum + "\" and  \"" + eindDatum + "\" and waiterId= \"" + ober.getID() + "\";";

        logger.debug("execute query = " + sql);

        try (Statement pStatement = connection.createStatement();
             ResultSet resultSet = pStatement.executeQuery(sql)) {

            while (resultSet.next()) {

                int orderNR = resultSet.getInt("orderNumber");
                int beverageId = resultSet.getInt("beverageID");
                int aantal = resultSet.getInt("qty");

                Consumption consumption = new Consumption(orderNR, beverageId, aantal, ober.getID());

                test.add(consumption);
            }
        } catch (SQLException e) {
            logger.error("Something went wrong with getBetween");
        }
        return test;
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
        System.out.println(lijst);

        return lijst;
    }
}