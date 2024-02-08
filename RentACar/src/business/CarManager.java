package business;

import core.Helper;
import dao.BookDao;
import dao.CarDao;
import entity.Book;
import entity.Car;
import entity.Model;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CarManager {
    private final CarDao carDao;
    private final BookDao bookDao;

    public CarManager() {
        this.carDao = new CarDao();
        this.bookDao = new BookDao();
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
        ArrayList<String> bookOrWhere = new ArrayList<>();

        joinWhere.add("c.car_model_id = m.model_id");


        strtDate = LocalDate.parse(strtDate, DateTimeFormatter.ofPattern("dd/MM/yyyy")).toString();
        fnshDate = LocalDate.parse(fnshDate, DateTimeFormatter.ofPattern("dd/MM/yyyy")).toString();

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

   //     System.out.println(query);

        ArrayList<Car> searchedCarList = this.carDao.selectByQuery(query);

        bookOrWhere.add("('" + strtDate + "' BETWEEN book_strt_date AND book_fnsh_date)");
        bookOrWhere.add("('" + fnshDate + "' BETWEEN book_strt_date AND book_fnsh_date)");
        bookOrWhere.add("(book_strt_date BETWEEN '" +strtDate +"' AND '"+ fnshDate + "')");
        bookOrWhere.add("(book_fnsh_date BETWEEN '" +strtDate +"' AND '"+ fnshDate + "')");

        String bookOrWhereStr = String.join(" OR ", bookOrWhere);
        String bookQuery = "SELECT * FROM public.book WHERE " +bookOrWhereStr;
        System.out.println(bookQuery);

        ArrayList<Book> bookList = this.bookDao.selectByQuery(bookQuery);
        ArrayList<Integer> busyCarId = new ArrayList<>();
        for (Book book : bookList){
            busyCarId.add(book.getCar_id());
        }

        searchedCarList.removeIf(car -> busyCarId.contains(car.getId()));
     //   System.out.println(bookList);
        return searchedCarList;
    }
}
