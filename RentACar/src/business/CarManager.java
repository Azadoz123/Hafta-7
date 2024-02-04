package business;

import core.Helper;
import dao.CarDao;
import entity.Car;
import entity.Model;

import java.util.ArrayList;

public class CarManager {
    private final CarDao carDao;

    public CarManager() {
        this.carDao = new CarDao();
    }
    public Car getById(int id){
        return this.carDao.getById(id);
    }
    public ArrayList<Car> findAll(){
        return this.carDao.findAll();
    }
    public ArrayList<Object[]> getForTable(int size, ArrayList<Car> carList){
        ArrayList<Object[]> carObjList = new ArrayList<>();
        for (Car car : carList){
            int i = 0;
            Object[] rowObject = new Object[size];
            rowObject[i++] = car.getId();
            rowObject[i++] = car.getModel().getBrand().getName();
            rowObject[i++] = car.getModel().getName();
            rowObject[i++] = car.getPlate();
            rowObject[i++] = car.getColor();
            rowObject[i++] = car.getKm();
            rowObject[i++] = car.getModel().getYear();
            rowObject[i++] = car.getModel().getType();
            rowObject[i++] = car.getModel().getFuel();
            rowObject[i++] = car.getModel().getGear();
            carObjList.add(rowObject);
        }
        return carObjList;
    }
    public boolean save (Car car){
        if(this.getById(car.getId()) != null){
            Helper.showMessage("error");
            return false;
        }
        return this.carDao.save(car);
    }
    public boolean update(Car car){
        if(this.getById(car.getId()) == null){
            Helper.showMessage(car.getId() + " ID kayıtlı model bulunamadı !");
            return false;
        }
        return this.carDao.update(car);
    }
    public boolean delete(int id){
        if(this.getById(id) == null){
            Helper.showMessage(id + " ID kayıtlı model bulunamadı !");
            return false;
        }
        return this.carDao.delete(id);
    }

    public ArrayList<Car> SearchForBooking(String strtDate, String fnshDate, Model.Type type, Model.Gear gear, Model.Fuel fuel){
        String query = "Select * FROM public.car as c LEFT JOIN public.model as m";

        ArrayList<String> where = new ArrayList<>();
        ArrayList<String> joinWhere = new ArrayList<>();

        joinWhere.add("c.car_model_id = m.model_id");

        if (fuel != null){
            where.add("m.model_fuel = '" + fuel.toString() +"'");
        }

        if (gear != null){
            where.add("m.model_gear = '" + gear.toString() +"'");
        }
        if (type != null){
            where.add("m.model_type = '" + type.toString() +"'");
        }

        String whereStr = String.join(" AND ",where);
        String joinStr = String.join(" AND ", joinWhere);

        if(joinStr.length() > 0 ){
            query += " ON " + joinStr;
        }
        if(whereStr.length() > 0){
            query += " WHERE " + whereStr;
        }

        System.out.println(query);
        return this.carDao.selectByQuery(query);
    }
}
