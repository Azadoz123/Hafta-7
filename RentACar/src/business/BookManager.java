package business;

import core.Db;
import core.Helper;
import dao.BookDao;
import entity.Book;
import entity.Car;
import entity.Model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class BookManager {
    private final BookDao bookDao;
    private final Connection connection;
    private CarManager carManager;
    public BookManager(){
        this.bookDao = new BookDao();
        this.carManager = new CarManager();
        this.connection = Db.getInstance();
    }
    public ArrayList<Book> findAll(){
        return this.bookDao.findAll();
    }
    public ArrayList<Object[]> getForTable(int size, ArrayList<Book> bookList){
        ArrayList<Object[]> bookObjList = new ArrayList<>();
        for (Book book : bookList){
            int i = 0;
            Object[] rowObject = new Object[size];
            rowObject[i++] = book.getId();
            rowObject[i++] = book.getCar_id();
            rowObject[i++] = carManager.getById(book.getCar_id()).getPlate();
            rowObject[i++] = book.getStrt_date();
            rowObject[i++] = book.getFnsh_date();
            rowObject[i++] = book.getName();
            rowObject[i++] = book.getMail();
            rowObject[i++] = book.getPrice();
            rowObject[i++] = book.getNote();
            rowObject[i++] = book.getbCase();
            rowObject[i++] = book.getIdNo();
            bookObjList.add(rowObject);
        }
        return bookObjList;
    }
    public boolean save(Book book){
        return this.bookDao.save(book);
    }
    public Book getById(int id){
        return this.bookDao.getById(id);
    }
    public boolean delete(int id){
        if(this.getById(id) == null){
            Helper.showMessage(id + " ID kayıtlı model bulunamadı !");
            return false;
        }
        return this.bookDao.delete(id);
    }
    public ArrayList<Book> searchForTable(int bookId){
        String select = "SELECT * FROM public.book";
        ArrayList<String> whereList = new ArrayList<>();

        if (bookId != 0){
            whereList.add(" book_id = " + bookId);
        }

        // System.out.println(whereList);
        String whereStr = String.join(" AND ", whereList );
        System.out.println(whereStr);
        String query = select;
        if (whereStr.length() > 0){
            query += " WHERE " + whereStr;
        }
        System.out.println(query);
        return this.bookDao.selectByQuery(query);
    }
}
