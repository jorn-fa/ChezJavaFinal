package be.leerstad.Database;

import be.leerstad.Consumption;
import be.leerstad.Ober;
import be.leerstad.helpers.DbaseConnection;
import org.apache.log4j.Logger;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RapportDaoImpl implements RapportDAO {

    Logger logger = Logger.getLogger("dbase");
    Connection connection = DbaseConnection.getConnection();
    String SQL;



    @Override
    public List printByDay(LocalDate datum) {

        Date searchDate = Date.valueOf(datum);

        List<Consumption> lijst = new ArrayList<>();

        SQL = "select * from orders where date = '"+ searchDate + "'" ;


        try (
                PreparedStatement ps = connection.prepareStatement(SQL);
                ResultSet resultSet = ps.executeQuery())
        {
            while (resultSet.next()) {
                int orderNubmer = resultSet.getInt("orderNumber");
                int beverageID = resultSet.getInt("beverageID");
                int aantal = resultSet.getInt("qty");
                int waiterID = resultSet.getInt("waiterID");
                Consumption consumption= new Consumption(orderNubmer,beverageID,aantal,waiterID);
                lijst.add(consumption);
            }

        } catch (SQLException e) {
            logger.error("something went wrong with printbyday sql");
            throw new IllegalArgumentException("check sql syntaxis");
        }

                logger.debug("Opvragen verkoop per dag");

            return lijst;
    }

    @Override
    public double giveSaleResult(Ober ober) {

        BeveragesDAOImpl temp = new BeveragesDAOImpl();
        List<Consumption> prijslijst;

        prijslijst=temp.pricelijst();



        double som=0;

        SQL = "select * from orders where waiterID='"+ ober.getID() + "'";
        try (
                PreparedStatement pStatement = connection.prepareStatement(SQL);
                ResultSet resultSet = pStatement.executeQuery()) {

            while (resultSet.next()) {

                int aantal = resultSet.getInt("qty");
                int id = resultSet.getInt("beverageID");
                double total = prijslijst.stream()
                        .filter(x -> x.getBeverageId()==id)
                        .mapToDouble((Consumption::getPrijs))
                        .sum();
                som+=(total*aantal);
            }

        } catch (SQLException e) {
            logger.error("something went wrong with getOrdernummer");
            throw new IllegalArgumentException("check sql");
        }

        logger.debug("Opvragen verkoop per ober ");

        return som;

    }

    public static void main(String[] args) {
        RapportDaoImpl a = new RapportDaoImpl();
        LocalDate date = LocalDate.of(2017,12,21);

        System.out.println(date);
        a.printByDay(date);
        System.out.println("---");
        Ober ober=new Ober(1,"wout","peters");
        System.out.println(a.giveSaleResult(ober));
    }
}
