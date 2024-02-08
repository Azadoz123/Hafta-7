package dao;

import core.Db;
import entity.Book;
import entity.Car;


import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class BookDao {
    private Connection connection;
    private final CarDao carDao;

    public BookDao() {
        this.connection = Db.getInstance();
        this.carDao = new CarDao();
    }
    public ArrayList<Book> findAll(){
        return selectByQuery("SELECT * FROM public.book ORDER BY book_id ASC");
    }
    public ArrayList<Book> selectByQuery(String query){
        ArrayList<Book> bookList = new ArrayList<>();
        try {
            ResultSet resultSet = this.connection.createStatement().executeQuery(query);
            while (resultSet.next()){
                bookList.add(this.match(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return bookList;
    }
    public boolean save(Book book){
        String query = "INSERT INTO public.book" +
                "(" +
                "book_car_id," +
                "book_name," +
                "book_idno," +
                "book_mail," +
                "book_mpno," +
                "book_strt_date," +
                "book_fnsh_date," +
                "book_prc," +
                "book_case," +
                "book_note" +
                ")" +
                " VALUES(?,?,?,?,?,?,?,?,?,?)";

        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            preparedStatement.setInt(1,book.getCar_id());
            preparedStatement.setString(2,book.getName());
            preparedStatement.setString(3,book.getIdNo());
            preparedStatement.setString(4,book.getMpNo());
            preparedStatement.setString(5, book.getMail());
            preparedStatement.setDate(6, Date.valueOf(book.getStrt_date()));
            preparedStatement.setDate(7, Date.valueOf(book.getFnsh_date()));
            preparedStatement.setInt(8,book.getPrice());
            preparedStatement.setString(9,book.getbCase());
            preparedStatement.setString(10,book.getNote());
            return preparedStatement.executeUpdate() != -1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public Book getById(int id){
        Book book = null;
        String query = "SELECT * FROM public.book WHERE book_id = ?";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = this.connection.prepareStatement(query);
            preparedStatement.setInt(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) book = this.match(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return book;
    }
    public boolean delete(int id){
        String query = "DELETE FROM public.book WHERE book_id = ?";
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            preparedStatement.setInt(1,id);
            return preparedStatement.executeUpdate() != -1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public Book match(ResultSet resultSet) throws SQLException {
        Book book = new Book();
        book.setId(resultSet.getInt("book_id"));
        book .setbCase(resultSet.getString("book_case"));
        book.setName(resultSet.getString("book_name"));
        book.setCar_id(resultSet.getInt("book_car_id"));
        book.setCar(this.carDao.getById(resultSet.getInt("book_car_id")));
        book.setStrt_date(LocalDate.parse(resultSet.getString("book_strt_date")));
        book.setFnsh_date(LocalDate.parse(resultSet.getString("book_fnsh_date")));
        book.setIdNo(resultSet.getString("book_idno"));
        book.setMpNo(resultSet.getString("book_mpno"));
        book.setMail(resultSet.getString("book_mail"));
        book.setNote(resultSet.getString("book_note"));
        book.setPrice(resultSet.getInt("book_prc"));
        return book;
    }
}
