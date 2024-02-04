package dao;

import core.Db;
import entity.Car;
import entity.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CarDao {
    private final Connection connection;
    private final BrandDao brandDao;
    private final ModelDao modelDao;

    public CarDao() {
        this.connection = Db.getInstance();
        this.brandDao = new BrandDao();
        this.modelDao = new ModelDao();
    }
    public Car getById(int id){
        Car car = null;
        String query = "SELECT * FROM public.car WHERE car_id = ?";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = this.connection.prepareStatement(query);
            preparedStatement.setInt(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) car = this.match(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return car;
    }
    public ArrayList<Car> findAll(){
        return selectByQuery("SELECT * FROM public.car ORDER BY car_id ASC");
    }

    public ArrayList<Car> selectByQuery(String query){
        ArrayList<Car> carList = new ArrayList<>();
        try {
            ResultSet resultSet = this.connection.createStatement().executeQuery(query);
            while (resultSet.next()){
                carList.add(this.match(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return carList;
    }
    public boolean update(Car car){
        String query = "UPDATE public.car SET " +
                "car_model_id = ?," +
                "car_color = ?," +
                "car_km = ?," +
                "car_plate = ?" +
                " WHERE car_id = ?";

        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            preparedStatement.setInt(1,car.getModel_id());
            preparedStatement.setString(2,car.getColor().toString());
            preparedStatement.setInt(3,car.getKm());
            preparedStatement.setString(4, car.getPlate());
            preparedStatement.setInt(5,car.getId());
            return preparedStatement.executeUpdate() != -1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean save(Car car){
        String query = "INSERT INTO public.car" +
                "(" +
                "car_model_id," +
                "car_color," +
                "car_km," +
                "car_plate" +
                ")" +
                " VALUES(?,?,?,?)";

        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            preparedStatement.setInt(1,car.getModel_id());
            preparedStatement.setString(2,car.getColor().toString());
            preparedStatement.setInt(3,car.getKm());
            preparedStatement.setString(4, car.getPlate());
            return preparedStatement.executeUpdate() != -1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean delete(int id){
        String query = "DELETE FROM public.car WHERE car_id = ?";
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            preparedStatement.setInt(1,id);
            return preparedStatement.executeUpdate() != -1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public Car match(ResultSet resultSet) throws SQLException {
        Car car = new Car();
        car.setId(resultSet.getInt("car_id"));
        car .setModel_id(resultSet.getInt("car_model_id"));
        car.setPlate(resultSet.getString("car_plate"));
        car.setColor(Car.Color.valueOf(resultSet.getString("car_color")));
        car.setKm(resultSet.getInt("car_km"));
        car.setModel(this.modelDao.getById(car.getModel_id()));
        return car;
    }
}
